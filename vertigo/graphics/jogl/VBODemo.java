package de.gaffga.threed.demos.vbo;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.GLUT;

/**
 * This class shows how vertex buffer objects (VBO) can be used to accelerate
 * rendering.
 * 
 * @author Stefan Gaffga
 */
public class VBODemo implements GLEventListener, KeyListener {

	// ============================================================================================
	// ===
	// === internal SwitchableFlag class
	// ===
	// ============================================================================================

	/**
	 * Class that provides infos and a boolean flag that can be switched on and off by the user.
	 * 
	 * @author Stefan Gaffga
	 */
	private class SwitchableFlag {
		/** The key code to switch this flag */
		private char key;
		
		/** the current value */
		private boolean flag;
		
		/** The text for the help dialog. */
		private String helpText;
		
		/**
		 * Constructor.
		 * 
		 * @param aKey the key this flag can be toggled with
		 * @param initialValue the value the flag gets as a default value
		 * @param helpText the text that will be shown in the help dialog
		 */
		public SwitchableFlag(char aKey, boolean initialValue, String helpText) {
			this.key = aKey;
			this.flag = initialValue;
			this.helpText = helpText;
		}
		
		/**
		 * Toggles the flag on or off.
		 */
		public void switchFlag() {
			flag = !flag;
			updateHelpDialog();
		}

		/**
		 * Returns the key char.
		 * 
		 * @return the key char
		 */
		public char getKey() {
			return key;
		}

		/**
		 * Returns the current flag state.
		 * 
		 * @return the current state
		 */
		public Boolean getFlag() {
			return flag;
		}

		/**
		 * Returns the help text.
		 * 
		 * @return the help text
		 */
		public String getHelpText() {
			return helpText;
		}
	}
	
	// ============================================================================================
	// ===
	// === Class attributes
	// ===
	// ============================================================================================
	
	/** An instance of the GLUT utility library. */
	GLUT glut = new GLUT();
	
	/** The JFrame object. */
	private JFrame frame;
	
	/** GLCanvas object. */
	private GLCanvas canvas;
	
	/** The help dialog. */
	private JDialog helpDialog;

	/** The TextPane that contains the help text. */
	JTextPane helpTextArea = new JTextPane();

	/** The caption of the main window. */
	private String caption = "VBO Demo - press h for help - ";
	
	/** Angle the object rotates slowly around. */
	private double viewAngle;
	
	// -- This is for calculating the frames per second -------------------------------------------

	/** The factor everything that is time controlled is scaled by to get the same speed 
	 * at all FPS values. */
	private double fpsFactor;
	
	/** Time since we count the frames. */
	private long frameCountStartTime;
	
	/** Number of frames rendered since. */
	private long frameCount;
	
	// -- This is for the flags that can be toggled by the user -----------------------------------

	/** The list of switchable flags. */
	private Map<String, SwitchableFlag> switchableFlags = new HashMap<String, SwitchableFlag>();
	
	// -- These are attributes necessary for the Demo Mesh ----------------------------------------

	/** List of vertices to render. */
	private ByteBuffer vertices;
	
	/** List of indices to render. */
	private ByteBuffer indices;
	
	/** The number of rows in the demo mesh. */ 
	private int numRows = 100;
	
	/** The number of columns in the demo mesh. */ 
	private int numCols = 100;
	
	/** The number of vertices of our demo rendering mesh. */
	private int numVertices;
	
	/** The number of indices of out demo rendering mesh. */
	private int numTriangles;
	
	// -- These are attributes necessary for the VBO stuff ----------------------------------------
	
	/** Storage for the two VBO buffers we need. */
	private IntBuffer vboBuffers;
	
	/** The index in the vboBuffers which contains the Buffer Id for the vertices */
	private static final int BUFFER_VERTICES = 0;
	
	/** The index in the vboBuffers which contains the Buffer Id for the indices */
	private static final int BUFFER_INDICES = 1;
	
	// ============================================================================================
	// ===
	// === main() Method
	// ===
	// ============================================================================================
	
	/**
	 * The main method.
	 * 
	 * @param args command line args
	 */
	public static void main(String[] args) {
		VBODemo demo = new VBODemo();
		demo.run();
	}
	
	// ============================================================================================
	// ===
	// === Constructor 
	// ===
	// ============================================================================================
	
	/**
	 * Default constructor.
	 */
	public VBODemo() {
		initDisplay();
		initFlags();
	}
	
	// ============================================================================================
	// ===
	// === public Methods
	// ===
	// ============================================================================================
	
	/**
	 * Shows the frame.
	 */
	public void run() {
		frame.setVisible(true);
		canvas.requestFocusInWindow();
	}

	// ============================================================================================
	// ===
	// === private Methods 
	// ===
	// ============================================================================================
	
	/**
	 * Initializes the switchable flags list.
	 */
	private void initFlags() {
		switchableFlags.put("wireframe", new SwitchableFlag('w', false, "render wireframe"));
		switchableFlags.put("lighting", new SwitchableFlag('l', true, "use lighting"));
		switchableFlags.put("vbo", new SwitchableFlag('v', false, "use VBO"));
	}
	
	/**
	 * Initializes the window and sets us as the listener for GL events.
	 */
	private void initDisplay() {
		frame = new JFrame();
		canvas = new GLCanvas(new GLCapabilities());
		canvas.addGLEventListener(this);
		canvas.addKeyListener(this);
		frame.setLayout(new BorderLayout());
		frame.add(canvas, BorderLayout.CENTER);
		frame.setSize(600,600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/**
	 * Updates the help dialog text.
	 */
	private void updateHelpDialog() {
		helpTextArea.setText(getHelpString());
	}
	
	/**
	 * This is called once per frame and updates the FPS value and shows it in the title bar of the frame.
	 */
	private void updateFps() {
		frameCount++;
		long now = System.nanoTime();
		if ( now - frameCountStartTime > 500*1e6) {
			// Time per frame in milliseconds:
			double timePerFrame = 1e-6 * (double)(now - frameCountStartTime)/(double)frameCount;
			double framesPerSecond = 1000.0 / timePerFrame;
			fpsFactor = 1.0 / framesPerSecond;
			
			frame.setTitle(caption+" "+(int)(framesPerSecond)+" FPS ");
			
			frameCount=0;
			frameCountStartTime=System.nanoTime();
		}
	}
	
	/**
	 * Creates the help text from the switchable flags.
	 * 
	 * @return the help string
	 */
	private String getHelpString() {
		StringBuffer txt = new StringBuffer();
		txt.append("<html><table>"+
				"<tr><th>Key</th><th>Description</th><th>Current value</th></tr>" +
				"<tr><td><strong>h</strong></td><td>show this help screen</td></tr>");
		
		for ( String key : switchableFlags.keySet() ) {
			SwitchableFlag flag = switchableFlags.get(key);
			txt.append("<tr><td><strong>");
			txt.append(flag.getKey());
			txt.append("</strong></td><td>");
			txt.append(flag.getHelpText());
			txt.append("</td><td>");
			txt.append(flag.getFlag() ? "<strong>ON</strong>":"OFF");
			txt.append("</td></tr>");
		}
		txt.append("</table></html>");
		return txt.toString();
	}
	
	/**
	 * Shows or hides the modeless help dialog.
	 */
	private void toggleHelpDialog() {
		if ( helpDialog == null ) {
			helpDialog = new JDialog(frame, "Help");
			helpDialog.setModal(false);
			helpDialog.setSize(300, 300);
			helpDialog.setLayout(new BorderLayout());
			helpDialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
			JPanel centerPanel = new JPanel();
			centerPanel.setLayout(new BorderLayout());
			helpTextArea.setContentType("text/html");
			helpTextArea.setEditable(false);
			JScrollPane scrollPane = new JScrollPane(helpTextArea);
			centerPanel.add(scrollPane);
			helpDialog.add(centerPanel);
			
			helpTextArea.setText(getHelpString());
		} 
		
		if ( helpDialog.isVisible() ) {
			helpDialog.setVisible(false);
		} else {
			helpDialog.setLocation(frame.getLocation().x+frame.getWidth(), frame.getLocation().y);
			helpDialog.setVisible(true);
		}
		
		frame.requestFocus();
		canvas.requestFocusInWindow();
	}

	/**
	 * Draws the contents of the scene.
	 * 
	 * @param drawable the GL drawable
	 */
	private void renderScene(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		
		if ( !switchableFlags.get("vbo").getFlag() ) {
			// The user choosed not to use VBOs
			
			if ( vboBuffers!=null ) {
				// switch them off if they are currently active
				switchVBOsOff(gl);
			}

			// Draw triangles using the indices in the "indices" array. The indices point to
			// the array specified above in glInterleavedArrays
			gl.glDrawElements(GL.GL_TRIANGLES, numTriangles*3, GL.GL_UNSIGNED_INT, indices);
	
		} else {
			// The user whishes to use VBOs
			
			if ( vboBuffers==null ) {
				// switch them on if they are currently inactive
				switchVBOsOn(gl);
			}
			
			// We send 0 as the buffer pointer to indicate that we want to use the VBO 
			gl.glDrawElements(GL.GL_TRIANGLES, numTriangles*3, GL.GL_UNSIGNED_INT, 0);
		}
		
	}

	/**
	 * Switches the VBOs on by sending the data to the gfx card.
	 * 
	 * @param gl the GL context
	 */
	private void switchVBOsOn(GL gl) {
		gl.glDisableClientState(GL.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL.GL_NORMAL_ARRAY);

		vboBuffers = IntBuffer.allocate(2);
		gl.glGenBuffersARB(2, vboBuffers);
		gl.glBindBufferARB(GL.GL_ARRAY_BUFFER_ARB, vboBuffers.get(BUFFER_VERTICES));
		gl.glBindBufferARB(GL.GL_ELEMENT_ARRAY_BUFFER_ARB, vboBuffers.get(BUFFER_INDICES));

		gl.glBufferDataARB(GL.GL_ARRAY_BUFFER_ARB, numVertices*2*3*4, vertices, GL.GL_STATIC_DRAW_ARB);
		gl.glBufferDataARB(GL.GL_ELEMENT_ARRAY_BUFFER_ARB, numTriangles*3*4, indices, GL.GL_STATIC_DRAW_ARB);

		// When using VBOs we give 0 for the buffer-Pointer. This means for OpenGL: the buffer is
		// stored in the GFX card. OpenGL knows which to take because there are
		// only two: The vertex array and the index array. Before VBOs existed there
		// have been two options: Use separate arrays for vertices, normals, colors etc.
		// or use an interleaved arrays. The latter means that vertex coords, colors and
		// other data alternate in a single array. Using VBOs we can only use interleaved 
		// arrays now. But we are not limited to fixed vertex formats (e.g. GL_N3F_V3F) as
		// before VBOs. Using glVertexPointer and friends we can freely specify the vertex format.
		// (c) is the offset for the data and (d) is the distance to the next data set.
		// 
		// Every index points to the vertex array which is interleaved and therefore supplies
		// vertex coordinates and normals.
		
		//                             (d)   (c) 
		gl.glNormalPointer(GL.GL_FLOAT, 24,   0);
		//                                 (d)   (c) 
		gl.glVertexPointer(3, GL.GL_FLOAT, 24,   12);

		gl.glEnableClientState(GL.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL.GL_NORMAL_ARRAY);

	}

	/**
	 * Turning off the VBOs by freeing the bindings and the ids.
	 * 
	 * @param gl the GL context
	 */
	private void switchVBOsOff(GL gl) {
		// Disable the vertices array again
		gl.glDisableClientState(GL.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL.GL_NORMAL_ARRAY);

		// Sending 0 als BufferId deactivates the buffers
		gl.glBindBufferARB(GL.GL_ARRAY_BUFFER_ARB, 0);
		gl.glBindBufferARB(GL.GL_ELEMENT_ARRAY_BUFFER_ARB, 0);
		// free the ids, too
		if ( vboBuffers!=null ) {
			gl.glDeleteBuffersARB(2,vboBuffers);
		}
		// set the storage for the ids to zero so we know we freed them
		vboBuffers=null;
		
		// When not using VBOs we specify the local vertices buffer 
		// Enable the vertex array stored in the buffer "vertices"
		gl.glInterleavedArrays(GL.GL_N3F_V3F, 0, vertices);
		gl.glEnableClientState(GL.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL.GL_NORMAL_ARRAY);
	}

	/**
	 * Fills the vertices and indices array with data for us to render.
	 */
	private void initRenderData() {
		float x, z;
		float minX = -30;
		float maxX = 30;
		float minZ = -30;
		float maxZ = 30;
		
		float[] verticesArray = new float[numRows*numCols*2*3];
		int vertexIndex=0;
		numVertices=0;
		for ( int row=0 ; row<numRows ; row++ ) {
			for ( int col=0 ; col<numCols ; col++ ) {
				x = col*(maxX-minX)/numCols+minX;
				z = row*(maxZ-minZ)/numRows+minZ;
				
				// First the normals, then the vertices (according to GL_N3F_V3F)
				float xn = (float)(Math.cos(x/10)*1/10);
				float zn = (float)(-Math.sin(z/2)*1/2);
				verticesArray[vertexIndex++]=xn;
				verticesArray[vertexIndex++]=1-(float)Math.sqrt(zn*zn + xn*xn);
				verticesArray[vertexIndex++]=zn;

				// then the vertex
				verticesArray[vertexIndex++]=x;
				verticesArray[vertexIndex++]=(float)(Math.sin(x/10)+Math.cos(z/2));
				verticesArray[vertexIndex++]=z;
				
				numVertices++;
			}
		}
		
		// build the triangles using the indices into the vertex array 
		int[] indicesArray = new int[(numRows-1)*(numCols-1)*2*3];
		numTriangles=0;
		int indexIndex=0;
		for ( int row=0 ; row<numRows-1 ; row++ ) {
			for ( int col=0 ; col<numCols-1 ; col++ ) {
				indicesArray[indexIndex++] = row*numCols+col;
				indicesArray[indexIndex++] = row*numCols+col+1;
				indicesArray[indexIndex++] = (row+1)*numCols+col;
				numTriangles++;
				
				indicesArray[indexIndex++] = row*numCols+col+1;
				indicesArray[indexIndex++] = (row+1)*numCols+col+1;
				indicesArray[indexIndex++] = (row+1)*numCols+col;
				numTriangles++;
			}
		}

		// Create a direct buffer for the indices
		indices = ByteBuffer.allocateDirect(4*indexIndex).order(ByteOrder.nativeOrder());
		// Copy the indices to the buffer
		IntBuffer ibuf = indices.asIntBuffer();
		for ( int i=0 ; i<indexIndex ; i++ ) {
			ibuf.put(i, indicesArray[i]);
		}
		
		// Create a direct buffer for the vertices
		vertices = ByteBuffer.allocateDirect(4*vertexIndex).order(ByteOrder.nativeOrder());
		// Copy the vertices to the buffer
		FloatBuffer fbuf = vertices.asFloatBuffer();
		for ( int i=0 ; i<vertexIndex ; i++ ) {
			fbuf.put(i, verticesArray[i]);
		}
	}
	
	// ============================================================================================
	// ===
	// === Implementation of GLEventListener 
	// ===
	// ============================================================================================
	
	/**
	 * This gets called the render the contents.
	 * 
	 * @param drawable the GL drawable object
	 */
	@Override
	public void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		
		gl.glClear(GL.GL_COLOR_BUFFER_BIT|GL.GL_DEPTH_BUFFER_BIT);
		
		gl.glLoadIdentity();
		gl.glRotated(viewAngle/5, 1, 0, 0);
		gl.glRotated(viewAngle, 0, 1, 0);
		gl.glRotated(viewAngle/3, 0, 0, 1);
		
		if ( switchableFlags.get("wireframe").getFlag() ) {
			gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_LINE);
		} else {
			gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);
		}
		
		if ( switchableFlags.get("lighting").getFlag() ) {
			gl.glEnable(GL.GL_LIGHTING);
		} else {
			gl.glDisable(GL.GL_LIGHTING);
		}
		
		renderScene(drawable);
		
		viewAngle += fpsFactor * 30;
		updateFps();
	}

	/**
	 * The display settings changed.
	 * 
	 * @param drawable the drawable
	 * @param modeChanged true if the display mode changed
	 * @param deviceChanged true if the device changed
	 */
	@Override
	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
	}

	/**
	 * This is the first time we have a valid GL context.
	 * 
	 * @param drawable the drawable
	 */
	@Override
	public void init(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		// Check for necessary extensions
		if ( !gl.isExtensionAvailable("GL_VERSION_1_5") ) {
			JOptionPane.showMessageDialog(null, "Sorry, your system does not support OpenGL 1.5!");
			System.exit(1);
		}
			
		// Create an Animator that renders new frames as fast as possible
		Animator anim = new Animator(drawable);
		anim.setRunAsFastAsPossible(true);
		anim.start();
		
		frameCount=0;
		frameCountStartTime=System.nanoTime();
		
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glFrustum(-0.1, 0.1, -0.1, 0.1, 0.1, 100.0);
		gl.glTranslatef(0,0,-50);
		
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
		
		gl.glDisable(GL.GL_TEXTURE_2D);
		gl.glEnable(GL.GL_LIGHTING);
		gl.glEnable(GL.GL_LIGHT0);
		
		gl.glEnable(GL.GL_DEPTH_TEST);
		
		gl.glColor3f(1,1,1);
		
		initRenderData();

		// Don't do this: You won't take any advantage of the VBOs anymore then!  
		// (try it yourself by uncommenting the line and comparing the FPS values with and without using VBOs)
		//gl.glLightModeli(GL.GL_LIGHT_MODEL_TWO_SIDE,1);
		
		// Initially set the VBO status
		if ( switchableFlags.get("vbo").getFlag() ) {
			switchVBOsOn(gl);
		} else {
			switchVBOsOff(gl);
		}
	}

	/**
	 * The Windows was resized.
	 * 
	 * @param drawable the drawable
	 * @param x the x new coodinate
	 * @param y the y new coordinate
	 * @param width the new width
	 * @param height the new heiht
	 */
	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
	}

	// ============================================================================================
	// ===
	// === Implementation of KeyListener 
	// ===
	// ============================================================================================

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if ( e.getKeyChar()=='h' ) {
			toggleHelpDialog();
		} else {
			for ( String key : switchableFlags.keySet() ) {
				SwitchableFlag flag = switchableFlags.get(key);
				
				if ( flag.getKey() == e.getKeyChar()) {
					flag.switchFlag();
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void keyReleased(KeyEvent e) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void keyTyped(KeyEvent e) {
	}
} // http://forum.jogamp.org/JOGL-tutorials-td2532772.html