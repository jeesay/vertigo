
import com.jogamp.common.nio.Buffers;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.*;
import java.nio.IntBuffer;
import vertigo.graphics.BufferData;
import vertigo.graphics.BufferTools;
import vertigo.graphics.IBO;

public class SimpleScene implements GLEventListener {

    private double theta = 0;
    private double s = 0;
    private double c = 0;
    static float[] triangles;
    static BufferData buff;
    private float[] wireVertices = {
        // Front face
        -0.5f, -0.5f, 0.5f,
        0.5f, -0.5f, 0.5f,
        0.5f, 0.5f, 0.5f,
        -0.5f, 0.5f, 0.5f,
        // Back face
        -0.5f, -0.5f, -0.5f,
        -0.5f, 0.5f, -0.5f,
        0.5f, 0.5f, -0.5f,
        0.5f, -0.5f, -0.5f
    };
    private int[] wireIndices = {0, 1, 2, 3, 0, 4, 5, 3, 5, 6, 2, 6, 7, 1, 7, 4};

    static private void genTriangle() {
        triangles = new float[9];



        triangles[0] = 0.0f;
        triangles[1] = 1.0f;
        triangles[2] = 0.0f;
        triangles[3] = -1.0f;
        triangles[4] = -1.0f;
        triangles[5] = 0.0f;
        triangles[6] = 1.0f;
        triangles[7] = -1.0f;
        triangles[8] = 0.0f;
        buff = new BufferData(triangles);

    }

    public static void main(String[] args) {
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        GLCanvas canvas = new GLCanvas(caps);

        Frame frame = new Frame("AWT Window Test");
        frame.setSize(300, 300);
        frame.add(canvas);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        canvas.addGLEventListener(new SimpleScene());

        // genTriangle();


        FPSAnimator animator = new FPSAnimator(canvas, 60);
        animator.add(canvas);
        animator.start();
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        update();
        render(drawable);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void init(GLAutoDrawable drawable) {
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
    }

    private void update() {
        theta += 0.01;
        s = Math.sin(theta);
        c = Math.cos(theta);
    }

    private void render(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        // draw a triangle filling the window
    /*    gl.glBegin(GL.GL_TRIANGLES);
         gl.glColor3f(1, 0, 0);
         gl.glVertex2d(-c, -c);
         gl.glColor3f(0, 1, 0);
         gl.glVertex2d(0, c);
         gl.glColor3f(0, 0, 1);
         gl.glVertex2d(s, -s);
         gl.glEnd();*/

        buff = new BufferData(wireVertices);
        int bytesPerFloat = Float.SIZE / Byte.SIZE;
        int bytesPerInt = Integer.SIZE / Byte.SIZE;
        // variable de dessins
        int numBytes = buff.getCapacity() * bytesPerFloat;
        // wireVertices.length*4  or
        int _vertexPointer = 3 * bytesPerFloat;
        int _vertexStride = 3 * bytesPerFloat;
        int _totalNumVerts = wireVertices.length * 3;


        // 2 VBO
        IntBuffer buf = Buffers.newDirectIntBuffer(2);
        // BIND VBO
        gl.glGenBuffers(2, buf);
        int _vbo = buf.get(0);

        // pas sur de l ordre
        gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, _vbo);
        gl.glBufferData(GL.GL_ARRAY_BUFFER, wireVertices.length*4, buff.getFloatBuffer(), GL.GL_STATIC_DRAW);
        gl.glVertexPointer(3, GL.GL_FLOAT, _vertexStride, _vertexPointer);

        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);

       // gl.glDrawArrays(GL.GL_LINE_LOOP, 0, _totalNumVerts);

        //gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);

        // bind IBO
        int _ibo = buf.get(1);

        IntBuffer ind = BufferTools.newIntBuffer(wireIndices.length);
        ind.put(wireIndices);
        ind.flip();
        int numBytesI = ind.capacity() * bytesPerInt;
        //IntBuffer indices =new IntBuffer(wireIndices);

        System.out.println("Debugage : "+wireIndices.length+" "+numBytesI+" "+ind.capacity()+" "+_ibo+" "+_vbo+" "+bytesPerInt+" "+Byte.SIZE);
         System.out.println("Debugage "+wireIndices.length+" * 4 =  "+wireIndices.length*4);
        gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, _ibo);
        
        gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, numBytesI, ind, GL.GL_STATIC_DRAW);

        gl.glDrawElements(GL2.GL_TRIANGLES, wireIndices.length, GL.GL_UNSIGNED_INT, 0); // note: the total number of indices

        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);

    }
} //end of Class

// http://code.google.com/p/jogl-cg-examples/source/browse/src/com/jogl/examples/trianglerenderer/VBORenderer.java?r=ee3e821b3330bffef70e6aeae41ccb111717f550&spec=svn6874a13fda3473895b64b1b1cd3657f90fb0baa4