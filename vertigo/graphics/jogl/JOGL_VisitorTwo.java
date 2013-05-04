/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vertigo.graphics.jogl;

import com.jogamp.opengl.util.gl2.GLUT;
import java.nio.IntBuffer;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.glu.GLU;
import vertigo.graphics.BO;
import vertigo.graphics.BufferTools;
import vertigo.graphics.IBO;
import vertigo.graphics.VBO;
import vertigo.graphics.Visitor;
import vertigo.scenegraph.BackStage;
import vertigo.scenegraph.Camera;
import vertigo.scenegraph.Light;
import vertigo.scenegraph.Lighting;
import vertigo.scenegraph.Node;
import vertigo.scenegraph.Scene;
import vertigo.scenegraph.Shape;
import vertigo.scenegraph.Stage;
import vertigo.scenegraph.Transform;
import vertigo.scenegraph.Viewing;
import vertigo.scenegraph.World;

/**
 *
 * @author clement
 */
public class JOGL_VisitorTwo implements Visitor, GLEventListener {

    private Camera cam_;
    private boolean isIndexed = false;
    private GLU glu;
    private GLUT glut;
    private GL3 gl;
    private GLCapabilities caps;
    private GLCanvas canvas;

    @Override
    public void visit(BackStage obj) {
        // do nothing
    }

    @Override
    public void visit(Camera obj) {
        cam_ = obj;
        setModelMatrix(obj);
    }

    @Override
    public void visit(Light obj) {
        setModelMatrix(obj);
    }

    @Override
    public void visit(Lighting obj) {
        // do nothing
    }

    @Override
    public void visit(Scene obj) {
        setModelMatrix(obj);
    }

    @Override
    public void visit(Shape obj) {
        mulModelMatrix(obj);
        drawShape(obj);
    }

    @Override
    public void visit(Stage obj) {
        // do nothing
    }

    @Override
    public void visit(Transform obj) {
        // do nothing
    }

    @Override
    public void visit(Viewing obj) {
        // do nothing
    }

    @Override
    public void visit(World obj) {
        // do nothing
    }

    private void setModelMatrix(Node obj) {
        if (obj.isDirty(Node.MATRIX)) {
            obj.setModelMatrix(obj.getParent().getModelMatrix());
            obj.setDirty(Node.MATRIX, true);
        }
    }

    private void mulModelMatrix(Node obj) {
        if (obj.isDirty(Node.MATRIX)) {
            obj.getModelMatrix().mul(obj.getParent().getModelMatrix());
            obj.setDirty(Node.MATRIX, true);
        }
    }

    private void drawShape(Shape obj) {
        // PreProcessing
        processShape(obj);
        // OpenGL
    }

    private void processShape(Shape obj) {
        // boolean isIndexed = false;
        IBO ibo = null;
System.out.println("Process Shape");
        // PreProcessing
        if (obj.isDirty(Node.MATRIX)) {
            obj.getModelMatrix().mul(obj.getParent().getModelMatrix());
            obj.setDirty(Node.MATRIX, false);
        }
        // Geometry: VBO
       // if (obj.isDirty(Node.VBO)) {
            processBO(obj);
            obj.setDirty(Node.VBO, false);
       // }


    }

    private void processBO(Shape obj) {
        //IBO ibon = null;
        
          int nbBO = obj.getGeometry().getNumberBO(); //return 2 for the cube (1 vbo + 1 ibo)
    
         System.out.println(" Number of BO is : " + nbBO);
        //int[] buffer = new int[nbBO]; 
        IntBuffer buffer = IntBuffer.allocate(nbBO); 

   
        int i = 0;
        
        for (BO bo : obj.getGeometry().getBuffers()) {
            // get ID
            gl.glGenBuffers(nbBO, buffer);
            gl.glBindBuffer(GL.GL_ARRAY_BUFFER, buffer[i]); 
            i++;
           // System.out.println("Buffer test : " + buffer[i]);

            if (bo instanceof IBO) {


                int bytesPerInt = Integer.SIZE / Byte.SIZE;
                isIndexed = true;
                System.out.println("Here IBO ! ");
                IBO ibo = (IBO) bo;

                int numBytes = ibo.getIntBuffer().capacity() * bytesPerInt;

                // bind a buffer
                //gl.glBindBuffer(gl.GL_ELEMENT_ARRAY_BUFFER, handle);
                System.out.println("Bind buffer IBO ");
                gl.glBufferData(gl.GL_ELEMENT_ARRAY_BUFFER, numBytes, ibo.getIntBuffer(), gl.GL_STATIC_DRAW);

                System.out.println("Buffer data IBO ");
                // release ibo
                gl.glBindBuffer(gl.GL_ELEMENT_ARRAY_BUFFER, 0);
                System.out.println("Released IBO ! ");
            } else {
                int bytesPerFloat = Integer.SIZE / Byte.SIZE;
                System.out.println(" VBO --");
                GL2 gl2 = gl.getGL2();
                gl.glEnableClientState(gl2.GL_VERTEX_ARRAY);

                VBO vbo = (VBO) bo;

                int numBytes = vbo.getFloatBuff().capacity() * bytesPerFloat;

                System.out.println(" The VBO is stride : " + vbo.getStride() + " offset :  " + vbo.getOffset());
                // bind a buffer
              //  gl.glBindBuffer(gl.GL_ARRAY_BUFFER, handle);
                System.out.println(" test --");
                gl2.glVertexPointer(vbo.getSize(), gl.GL_FLOAT, vbo.getStride(), 0);
             
                gl.glBufferData(gl.GL_ELEMENT_ARRAY_BUFFER, numBytes, vbo.getFloatBuff(), gl.GL_STATIC_DRAW);

                System.out.println(" test again *--");
                // release vbo
                gl.glBindBuffer(gl.GL_ARRAY_BUFFER, 0);
                // GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
                System.out.println("Released VBO");
            }
        }
        System.out.println("Here for loop ! i=" + i);


    }

    private int getOpenGLStyle(String vertigo_style) {
        int style = calcIndex(vertigo_style);
        switch (style) {
            case 379: // LINES
                return GL.GL_LINES;
            /*   case 1116: // LINES_ADJACENCY
             return GL.GL_LINES_ADJACENCY;*/
            case 705: // LINE_LOOP
                return GL.GL_LINE_LOOP;
            case 793: // LINE_STRIP
                return GL.GL_LINE_STRIP;
            /*  case 1530: // LINE_STRIP_ADJACENCY
             return GL.GL_LINE_STRIP_ADJACENCY;*/
            /* case 520: // PATCHES
             return GL.GL_PATCHES;*/
            case 477: // POINTS
                return GL.GL_POINTS;
            case 681: // TRIANGLES
                return GL.GL_TRIANGLES;
            /* case 1418: // TRIANGLES_ADJACENCY
             return GL.GL_TRIANGLES_ADJACENCY;*/
            case 906: // TRIANGLE_FAN
                return GL.GL_TRIANGLE_FAN;
            case 1095: // TRIANGLE_STRIP
                return GL.GL_TRIANGLE_STRIP;
            /*  case 1832: // TRIANGLE_STRIP_ADJACENCY
             return GL.GL_TRIANGLE_STRIP_ADJACENCY;*/
            default: // Do nothing  
                return -1;
        }
    }

    /**
     * Calc index by summing all the Unicode values in the string 'name'
     *
     * @param name
     * @return index
     */
    private static int calcIndex(String name) {
        int index = 0;
        for (int i = 0; i < name.length(); i++) {
            index += (int) name.charAt(i);
        }
        return index;
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        //  GL gl = drawable.getGL();  
        gl = drawable.getGL().getGL3();  // up to OpenGL 3
        glu = new GLU();
        glut = new GLUT();
        //
        //gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

    }

    @Override
    public void dispose(GLAutoDrawable glad) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void display(GLAutoDrawable glad) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void reshape(GLAutoDrawable glad, int i, int i1, int i2, int i3) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void setGLDrawable(GLAutoDrawable drawable) {
        System.out.println("drawable");
    }
} // end of class JOGL Visitor
// http://forum.jogamp.org/Can-someone-please-help-me-complete-this-sample-code-td3207414.html