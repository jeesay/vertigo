/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vertigo.graphics.jogl;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.util.gl2.GLUT;
import ij.IJ;
import java.nio.IntBuffer;
import java.util.ArrayList;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;
import org.lwjgl.util.glu.GLU;
import vertigo.graphics.Attribute;
import vertigo.graphics.BO;
import vertigo.graphics.IBO;
import vertigo.graphics.ShaderProg;
import vertigo.graphics.Uniform;
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
    private VBO vbo3f;
    private IBO ibo;
    private int capacity = 0;
    private ShaderProg glshader;
    private ArrayList<Attribute> attribute;
    // r
    // rotate
    static float speedPyramid = 2.0f; // rotational speed for pyramid
    static float speedCube = -1.5f;

    @Override
    public void visit(BackStage obj) {
        // do nothing
    }

    @Override
    public void visit(Camera obj) {
        cam_ = obj;

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
        if (obj.isDirty(Node.MATRIX)) {
            obj.getModelMatrix().mul(obj.getParent().getModelMatrix(), obj.getMatrix());
            obj.setDirty(Node.MATRIX, false);
        }
        drawShape(obj);
    }

    @Override
    public void visit(Stage obj) {
        // do nothing
    }

    @Override
    public void visit(Transform obj) {
        if (obj.isDirty(Node.MATRIX)) {
            obj.getModelMatrix().mul(obj.getParent().getModelMatrix(), obj.getMatrix());
            obj.setDirty(Node.MATRIX, false);
        }
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
            obj.setDirty(Node.MATRIX, false);
        }
    }

    private void drawShape(Shape obj) {
        if (obj.isDirty(Node.SHADER)) {
            processShader(obj);
            System.out.println("The HANDLE : " + glshader.getHandle());
            obj.setDirty(Node.SHADER, false);
        }

        gl.glUseProgram(obj.getMaterial().getShaderMaterial().getHandle());
        processUniform(obj);

        // boolean isIndexed = false;
        // PreProcessing
        //   if (obj.isDirty(Node.MATRIX)) {
        obj.setDirty(Node.MATRIX, false);
        //  }
        // Geometry: VBO
        //  if (obj.isDirty(Node.VBO)) {
        processBO(obj);
        obj.setDirty(Node.VBO, false);
        // }
        gl.glUseProgram(0);

    }

    private void processBO(Shape obj) {


        int nbBO = obj.getGeometry().getNumberBO(); //return 2 for the cube (1 vbo + 1 ibo)

        System.out.println("Number of BO is : " + nbBO);
        /*
         //int[] buffer = new int[nbBO]; 
         IntBuffer buffer = IntBuffer.allocate(nbBO); */
        // Generate VBO

        IntBuffer buf = Buffers.newDirectIntBuffer(nbBO);
        //IntBuffer buf = BufferUtils.createIntBuffer(nbBO);
        System.out.println("(again)Number of BO is : " + nbBO + " buf : " + buf);
        gl.glGenBuffers(nbBO, buf);

        int i = 0;
        int bytesPerFloat = Float.SIZE / Byte.SIZE;
        int bytesPerInt = Integer.SIZE / Byte.SIZE;
        System.out.println("Debugage : " + i + " " + bytesPerFloat + " " + bytesPerInt + " " + buf);

        for (BO bo : obj.getGeometry().getBuffers()) {
            System.out.println("For loop");
            int boHandle = buf.get(i);
            // get ID
            System.out.println("Handle is : " + boHandle);
            bo.setHandle(boHandle);
            i++;
            // System.out.println("Buffer test : " + buffer[i]);

            if (bo instanceof IBO) {
                isIndexed = true;
                System.out.println("Here IBO ! ");
                ibo = (IBO) bo;
                int numBytes = ibo.getIntBuffer().capacity() * bytesPerInt;

                gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, boHandle);
                gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, numBytes, ibo.getIntBuffer(), GL.GL_STATIC_DRAW);

                /*
                 // bind a buffer
                 //gl.glBindBuffer(gl.GL_ELEMENT_ARRAY_BUFFER, handle);
                 System.out.println("Bind buffer IBO ");
                 gl.glBufferData(gl.GL_ELEMENT_ARRAY_BUFFER, numBytes, ibo.getIntBuffer(), gl.GL_STATIC_DRAW);
                 System.out.println("Buffer data IBO ");
                 // release ibo
                 gl.glBindBuffer(gl.GL_ELEMENT_ARRAY_BUFFER, 0);
                 System.out.println("Released IBO ! ");*/
            } else {
                System.out.println("Here VBO ! ");
                VBO vbo = (VBO) bo;
                //enable(vbo);
                gl.glBindBuffer(GL.GL_ARRAY_BUFFER, boHandle);
                gl.glBufferData(GL.GL_ARRAY_BUFFER, vbo.capacity() * 4, vbo.getFloatBuff(), GL.GL_STATIC_DRAW);
                // pointer(vbo, bytesPerFloat);
                attribute = glshader.getAllAttributes();

                for (Attribute attrib : attribute) {
                    int alocation = gl.glGetAttribLocation(glshader.getHandle(), attrib.getName());
                    gl.glEnableVertexAttribArray(alocation);
                    gl.glVertexAttribPointer(alocation, attrib.getSize(), gl.GL_FLOAT, false, vbo.getStride() * bytesPerFloat, 0L);
                }

                //  gl.glVertexPointer(vbo.getSize(), GL.GL_FLOAT, vbo.getStride() * bytesPerFloat, vbo.getSize() * bytesPerFloat);




                /*
                 int numBytes = vbo.getFloatBuff().capacity() * bytesPerFloat;

                 System.out.println(" The VBO is stride : " + vbo.getStride() + " offset :  " + vbo.getOffset());
                 // bind a buffer
                 //  gl.glBindBuffer(gl.GL_ARRAY_BUFFER, handle);
                 System.out.println(" test --");
                 gl.glVertexPointer(vbo.getSize(), gl.GL_FLOAT, vbo.getStride(), 0);

                 gl.glBufferData(gl.GL_ELEMENT_ARRAY_BUFFER, numBytes, vbo.getFloatBuff(), gl.GL_STATIC_DRAW);

                 System.out.println(" test again *--");
                 // release vbo
                 gl.glBindBuffer(gl.GL_ARRAY_BUFFER, 0);
                 // GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
                 System.out.println("Released VBO");*/


            }
            System.out.println("Here for loop ! i=" + i);
        }


        if (isIndexed) { // draw with index
            gl.glDrawElements(getOpenGLStyle(obj.getDrawingStyle()), ibo.getSize(), GL.GL_UNSIGNED_INT, 0L);

        } else {
            gl.glDrawArrays(getOpenGLStyle(obj.getDrawingStyle()), 0, capacity / vbo3f.getStride());
        }
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
//        gl = drawable.getGL().getGL3();  // up to OpenGL 3
        //  glu = new GLU();
        // glut = new GLUT();
        //
        //gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        // gl = drawable.getGL().getGL2();
    }

    @Override
    public void dispose(GLAutoDrawable glad) {
        // do nothing
    }

    @Override
    public void display(GLAutoDrawable glad) {
        // do nothing
    }

    @Override
    public void reshape(GLAutoDrawable glad, int i, int i1, int i2, int i3) {
        // do nothing
    }

    void setGLDrawable(GLAutoDrawable drawable) {
        // System.out.println("drawable");
        gl = drawable.getGL().getGL3();
    }

    /**
     * Enable the good type of VBO
     *
     * @param vbo
     */
    private void enable(VBO vbo) {
        if (vbo.getType().contains("V")) {
            vbo3f = vbo;
            capacity = vbo.capacity();
            gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
        }
        if (vbo.getType().contains("C")) {
            gl.glEnableClientState(GL2.GL_COLOR_ARRAY);
        }
    }

    /**
     * Attrib the good type of pointer
     *
     * @param vbo
     *
     */
    /*
     private void pointer(VBO vbo, int bytesPerFloat) {
     if (vbo.getType().contains("V")) {
     gl.glVertexPointer(vbo.getSize(), GL.GL_FLOAT, vbo.getStride() * bytesPerFloat, vbo.getSize() * bytesPerFloat);
     } else if (vbo.getType().contains("C")) {
     gl.glColorPointer(vbo.getSize(), GL.GL_FLOAT, vbo.getStride() * bytesPerFloat, vbo.getSize() * bytesPerFloat);
     }
     }
     */
    private void processShader(Shape obj) {
        int handle;

        glshader = obj.getMaterial().getShaderMaterial();
        // compile once
        System.out.println("The handle 1 : " + glshader.getHandle());
        if (glshader.getHandle() == ShaderProg.UNKNOWN) {
            //TEST
            System.out.println("The handle 2 : " + glshader.getHandle());
            try {/*
                 String[] fragment = new String[glshader.getFragmentSource().length()];
                 for (int i = 0; i < glshader.getFragmentSource().length(); i++) {
                 fragment[i] = Character.toString(glshader.getFragmentSource().charAt(i));
                 }*/
                String[] vertex = new String[glshader.getVertexSource().length()];
                for (int i = 0; i < glshader.getFragmentSource().length(); i++) {
                    vertex[i] = Character.toString(glshader.getVertexSource().charAt(i));
                }

                String[] fragment = new String[]{glshader.getFragmentSource()};
                //String[] vertex = new String[]{glshader.getVertexSource()};
                System.out.println(" VERTEX SOURCE : " + vertex);
                System.out.println(" FRAGMENT SOURCE : " + fragment);
                handle = ShaderUtils.attachShaders(gl, vertex, fragment);
                System.out.println("The handle 3 : " + handle);
                glshader.setHandle(handle);
                System.out.println("The handle 4  : " + glshader.getHandle());
            } catch (Exception e) {
                IJ.log("Error with the Shader " + e);
            } //Compile, Link error
        }

    }

    private void processUniform(Shape obj) {
        ArrayList<Uniform> uniforms = glshader.getAllUniforms();
        for (Uniform uni : uniforms) {
            System.out.println("Handle : "+glshader.getHandle() + " name " + uni.getName());
            if (uni.getType().equals("view_matrix")) {

                int vlocation = gl.glGetUniformLocation(glshader.getHandle(), uni.getName());
                System.out.println(" View : " + vlocation);
                gl.glUniformMatrix4fv(vlocation, 16, false, cam_.getViewMatrix().toColumnBuffer());
                // gl.gluniformMatrix4
                System.out.println(cam_.getViewMatrix());
            } else if (uni.getType().equals("proj_matrix")) {
                int plocation = gl.glGetUniformLocation(glshader.getHandle(), uni.getName());
                System.out.println(" Projection : " + plocation);
                gl.glUniformMatrix4fv(plocation, 16, false, cam_.getProjection().toColumnBuffer());
                System.out.println(cam_.getProjection());
            } else if (uni.getType().equals("matrix")) {
                int mlocation = gl.glGetUniformLocation(glshader.getHandle(), uni.getName());
                System.out.println(" Location : " + mlocation);
                gl.glUniformMatrix4fv(mlocation, 16, false, obj.getModelMatrix().toColumnBuffer());
                System.out.println(obj.getModelMatrix());
            }
        }
    }
} // end of class JOGL Visitor
// http://forum.jogamp.org/Can-someone-please-help-me-complete-this-sample-code-td3207414.html