/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vertigo.graphics.lwjgl;

import java.nio.IntBuffer;
import java.util.Date;
import javax.media.opengl.glu.GLU;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL40;
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
import static org.lwjgl.util.glu.GLU.gluLookAt;


/**
 *
 * @author tomo
 */
public class LWJGL_VisitorTwo implements Visitor {

    private Camera cam_;
    private boolean isIndexed = false;

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

        // PreProcessing
        if (obj.isDirty(Node.MATRIX)) {
            obj.getModelMatrix().mul(obj.getParent().getModelMatrix());
            obj.setDirty(Node.MATRIX, false);
        }
        // Geometry: VBO
        if (obj.isDirty(Node.VBO)) {
            processBO(obj);
            obj.setDirty(Node.VBO, false);
        }

        /* GL11.glBegin(GL11.GL_POINTS);
         GL11.glVertex3f(0f, 1f, 0f);
         GL11.glVertex3f(-1f, 0f, 0f);
         GL11.glVertex3f(-0.7f, 0f, 0f);
         GL11.glVertex3f(-0.6f, 0f, 0f);
         GL11.glVertex3f(-0.5f, 0f, 0f);
         GL11.glVertex3f(1f, 0f, 0f);
         GL11.glEnd();
         */
        /*
            GL11.glBegin(GL11.GL_LINES);
         // x axis in red
         GL11.glColor3f(1, 0, 0);
         GL11.glVertex3i(0, 0, 0);
         GL11.glVertex3i(10, 0, 0);
         // y axis in green
         GL11.glColor3f(0, 1, 0);
         GL11.glVertex3i(0, 0, 0);
         GL11.glVertex3i(0, 10, 0);
         // z axis in blue
         GL11.glColor3f(0, 0, 0);
         GL11.glVertex3i(0, 0, 0);
         GL11.glVertex3i(0, 0, 10);
         GL11.glEnd();*/
        double rotate_x, rotate_y, rotate_z;
        long framerate_timestamp = new Date().getTime();
         int framerate_count = 0;
         framerate_count++;
           Date d = new Date();
            long this_framerate_timestamp = d.getTime();

            if ((this_framerate_timestamp - framerate_timestamp) >= 1000) {
                System.err.println("Frame Rate: " + framerate_count);
                framerate_count = 0;
                framerate_timestamp = this_framerate_timestamp;
            }

        
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
         

        for (BO bo : obj.getGeometry().getBuffers()) {
            if (bo instanceof IBO) {
                if (isIndexed) {
                    ibo = (IBO) bo;
                    System.out.println("isIndexed is true");
                    GL11.glDrawElements(getOpenGLStyle(obj.getDrawingStyle()), ibo.getIntBuffer());
                } else {
                    System.out.println("isIndexed is false");
                    GL11.glDrawArrays(getOpenGLStyle(obj.getDrawingStyle()), 0, obj.getGeometry().getCount());
                }
            }
        }

    }

    private void processBO(Shape obj) {
        //IBO ibon = null;
        int count = obj.getGeometry().getCount();
        IntBuffer bufferid = BufferTools.newIntBuffer(count);
        System.out.println(" count is : " + count + " and bufferid : " + bufferid);
        GL15.glGenBuffers(bufferid);
        int i = 0;
        for (BO bo : obj.getGeometry().getBuffers()) {
            // get ID
            int handle = bufferid.get(i);
            System.out.println("ID is : " + handle);
            bo.setHandle(handle);
            //or bo.setHandle(i); ??
            i++;
            System.out.println("Handle is ! : " + bo.getHandle());

            if (bo instanceof IBO) {
                isIndexed = true;
                System.out.println("Here IBO ! ");
                IBO ibo = (IBO) bo;
                // bind a buffer
                GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, handle);
                System.out.println("Bind buffer IBO ");
                GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo.getIntBuffer(), GL15.GL_STATIC_DRAW);
                System.out.println("Buffer data IBO ");
                // release ibo
                GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
                System.out.println("Released IBO ! ");
            } else {

                System.out.println(" VBO --");
                GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
                VBO vbo = (VBO) bo;
                System.out.println(" The VBO is stride : " + vbo.getStride() + " offset :  " + vbo.getOffset());


                // bind a buffer
                //GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, handle);
                // glBindBufferARB(GL15.GL_ARRAY_BUFFER, handle);

                GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, handle);

                System.out.println(" test --");
                GL11.glVertexPointer(vbo.getSize(), GL11.GL_FLOAT, vbo.getStride(), 0);
                //GL11.glVertexPointer(vbo.getSize(), GL11.GL_FLOAT, vbo.getStride()<<2, 0);
                //glVertexPointer(3, GL_FLOAT, /* stride */3 << 2, 0L);
                
                GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vbo.getFloatBuff(), GL15.GL_STATIC_DRAW);
                System.out.println(" test again *--");
                // release vbo
                GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
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
                return GL11.GL_LINES;
            case 1116: // LINES_ADJACENCY
                return GL32.GL_LINES_ADJACENCY;
            case 705: // LINE_LOOP
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
} // end of lass visitor Two
