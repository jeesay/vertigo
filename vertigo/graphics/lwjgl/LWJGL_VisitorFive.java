/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vertigo.graphics.lwjgl;

import ij.IJ;
import java.nio.IntBuffer;
import java.util.Date;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glColorPointer;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glVertexPointer;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL40;
import vertigo.graphics.BO;
import vertigo.graphics.IBO;
import vertigo.graphics.ShaderProg;
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
 * @author Clement DELESTRE
 */
public class LWJGL_VisitorFive implements Visitor {

    private Camera cam_;
    private boolean isIndexed = false;
    private int iboid;
    private IBO ibo = null;
    private VBO vbo3f = null;
    private int capacity = 0;

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


        // PreProcessing
        if (obj.isDirty(Node.MATRIX)) {
            obj.getModelMatrix().mul(obj.getParent().getModelMatrix());
            obj.setDirty(Node.MATRIX, false);
        }
        // Geometry: VBO
        //  if (obj.isDirty(Node.VBO)) {
        processBO(obj);
        obj.setDirty(Node.VBO, false);
        // }
        if (obj.isDirty(Node.SHADER)) {
            processShader(obj);
            obj.setDirty(Node.SHADER, false);
        }
        ShaderUtils.useShader(obj.getMaterial().getShaderMaterial().getHandle()); //use the shader
        
        
        
    }

    private void processShader(Shape obj) {
        //ShaderProg or void ?
        int handle;
        ShaderProg glshader = obj.getMaterial().getShaderMaterial();
        // compile once
        System.out.println("The handle 1 : " + glshader.getHandle());
        if (glshader.getHandle() == ShaderProg.UNKNOWN) {
            //TEST
            System.out.println("The handle 2 : " + glshader.getHandle());
            try {
                handle = ShaderUtils.attachShaders(glshader.getVertexSource(), glshader.getFragmentSource());
                System.out.println("The handle 4 : " + handle);
                glshader.setHandle(handle);
                System.out.println("The handle 5  : " + glshader.getHandle());
            } catch (Exception e) {
                IJ.log("Error with the Shader " + e);
            } //Compile, Link error
        }
        //ShaderUtils.updateShader(glshader);
        // TEST

    }

    private void processBO(Shape obj) {

        ////// ROTATE
/*
         int framerate_count = 0;
         long framerate_timestamp = new Date().getTime();
         double rotate_x, rotate_y, rotate_z;

         // increment frame rate counter, and display current frame rate
         // if it is time to do so
         framerate_count++;

         Date d = new Date();
         long this_framerate_timestamp = d.getTime();

         if ((this_framerate_timestamp - framerate_timestamp) >= 1000) {
         System.err.println("Frame Rate: " + framerate_count);
         framerate_count = 0;
         framerate_timestamp = this_framerate_timestamp;
         }

         // clear the display
         //GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

         // perform rotation transformations
         //GL11.glPushMatrix();

         rotate_x = ((double) this_framerate_timestamp / 300.0) % 360.0;
         rotate_y = ((double) this_framerate_timestamp / 200.0) % 360.0;
         rotate_z = ((double) this_framerate_timestamp / 100.0) % 360.0;

         GL11.glRotated(
         rotate_x,
         1.0,
         0.0,
         0.0);

         GL11.glRotated(
         rotate_y,
         0.0,
         1.0,
         0.0);

         GL11.glRotated(
         rotate_z,
         0.0,
         0.0,
         1.0);
         ///// END ROTATE
         * */













        int nbBO = obj.getGeometry().getNumberBO(); //return 2 for the cube (1 vbo + 1 ibo)
        IntBuffer ib = BufferUtils.createIntBuffer(nbBO);
        GL15.glGenBuffers(ib);
        System.out.println(" Number of BO is : " + nbBO);



        int i = 0;
        for (BO bo : obj.getGeometry().getBuffers()) {
            int boHandle = ib.get(i);
            System.out.println("Handle is : " + boHandle);
            bo.setHandle(boHandle);
            i++;


            if (bo instanceof IBO) {
                isIndexed = true;
                System.out.println("Here IBO ! ");
                ibo = (IBO) bo;
                GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, boHandle);
                GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo.getIntBuffer(), GL15.GL_STATIC_DRAW);
                System.out.println("buff : " + ibo.getIntBuffer());
                //glBindBufferARB(GL_ELEMENT_ARRAY_BUFFER_ARB, 0);

            } else {


                VBO vbo = (VBO) bo;
                enable(vbo);


                GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, boHandle);
                GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vbo.getFloatBuff(), GL15.GL_STATIC_DRAW);
                pointer(vbo);
                // glVertexPointer(vbo.getSize(), GL11.GL_FLOAT, /* stride */ vbo.getStride() << 2, 0L);
                System.out.println("Size of VBO : " + vbo.getSize() + " Stride of VBO : " + vbo.getStride());
                System.out.println("buff : " + vbo.getFloatBuff());
                // capacity = vbo.capacity();
                //glBindBufferARB(GL_ARRAY_BUFFER_ARB, 0);

            }
        }
        if (isIndexed) { // draw with index
            System.out.println("isIndexed is true, style is " + obj.getDrawingStyle());
            System.out.println("Size of IBO :  " + ibo.getSize());
            glDrawElements(getOpenGLStyle(obj.getDrawingStyle()), /* elements */ ibo.getSize(), GL_UNSIGNED_INT, 0L);
            // glBindBufferARB(GL_ARRAY_BUFFER_ARB, 0);
            // glDisableClientState(GL_VERTEX_ARRAY);


        } else {
            // draw withouh index

            System.out.println("isIndexed is false and capacity is " + capacity);
            GL11.glDrawArrays(getOpenGLStyle(obj.getDrawingStyle()), 0, capacity / vbo3f.getStride());

            //GL20.glDisableVertexAttribArray(0);
            // GL30.glBindVertexArray(0);
        }
        // glDeleteBuffersARB(ib);
        System.out.println("Here for loop ! i=" + i);
    }

    private int getOpenGLStyle(String vertigo_style) {
        int style = calcIndex(vertigo_style);
        switch (style) {
            case 379: // LINES
                return GL11.GL_LINES;
            case 1116: // LINES_ADJACENCY
                return GL32.GL_LINES_ADJACENCY;
            case 705: // LINE_LOOP
                System.out.println("Here we have LINE_LOOP");
                return GL11.GL_LINE_LOOP;
            case 793: // LINE_STRIP
                return GL11.GL_LINE_STRIP;
            case 1530: // LINE_STRIP_ADJACENCY
                return GL32.GL_LINE_STRIP_ADJACENCY;
            case 520: // PATCHES
                return GL40.GL_PATCHES;
            case 477: // POINTS
                return GL11.GL_POINTS;
            case 681: // TRIANGLES
                return GL11.GL_TRIANGLES;
            case 1418: // TRIANGLES_ADJACENCY
                return GL32.GL_TRIANGLES_ADJACENCY;
            case 906: // TRIANGLE_FAN
                return GL11.GL_TRIANGLE_FAN;
            case 1095: // TRIANGLE_STRIP
                return GL11.GL_TRIANGLE_STRIP;
            case 1832: // TRIANGLE_STRIP_ADJACENCY
                return GL32.GL_TRIANGLE_STRIP_ADJACENCY;
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

    /**
     * Attrib the good type of pointer
     *
     * @param vbo
     *
     */
    private void pointer(VBO vbo) {
        if (vbo.getType().contains("V")) {
            glVertexPointer(vbo.getSize(), GL11.GL_FLOAT, /* stride */ vbo.getStride() << 2, 0L);
        } else if (vbo.getType().contains("C")) {
            glColorPointer(vbo.getSize(), GL11.GL_FLOAT, /* stride */ vbo.getStride() << 2, 0L);
        }
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
            GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);

        }
        if (vbo.getType().contains("C")) {
            GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
        }
    }
} // end of class LWJGL_Visitor
// http://www.java-gaming.org/index.php?topic=24272.0