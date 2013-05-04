/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vertigo.graphics.lwjgl;

import org.lwjgl.*;
import org.lwjgl.opengl.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.Date;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.ARBVertexBufferObject.*;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.GL15;
import static org.lwjgl.util.glu.GLU.gluLookAt;

/**
 *
 * @author tomo
 */
public class Test {

    static void initContext() throws LWJGLException {
        int w = 640;
        int h = 480;
        Display.setDisplayMode(new DisplayMode(w, h));
        Display.setFullscreen(false);
        Display.create();
        glViewport(0, 0, w, h);
    }

    static void renderLoop() {
        while (!Display.isCloseRequested()) {
            preRender();
            drawVertexBufferObject();
            Display.update();
            Display.sync(10 /* desired fps */);
        }

        Display.destroy();
    }

    static void preRender() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
    }

    public Test() throws LWJGLException {
        initContext();
        renderLoop();
    }

    public static void main(String[] argv) throws LWJGLException {
        Test t = new Test();
        //t.drawVertexBufferObject();
    }

    static void drawVertexBufferObject() {

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
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        // perform rotation transformations
        GL11.glPushMatrix();

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




        // create geometry buffers
            /*
         FloatBuffer cBuffer = BufferUtils.createFloatBuffer(24);
         cBuffer.put(1).put(0).put(0);
         cBuffer.put(0).put(1).put(0);
         cBuffer.put(0).put(0).put(1);
         cBuffer.put(0).put(0).put(1);
         cBuffer.put(1).put(0).put(0);
         cBuffer.put(0).put(1).put(0);
         cBuffer.put(0).put(0).put(1);
         cBuffer.put(0).put(0).put(1);
         cBuffer.flip();*/

        FloatBuffer vBuffer = BufferUtils.createFloatBuffer(24);
        vBuffer.put(-0.5f).put(-0.5f).put(0.5f);
        vBuffer.put(0.5f).put(-0.5f).put(0.5f);
        vBuffer.put(0.5f).put(0.5f).put(0.5f);
        vBuffer.put(-0.5f).put(0.5f).put(0.5f);
        vBuffer.put(-0.5f).put(-0.5f).put(-0.5f);
        vBuffer.put(-0.5f).put(0.5f).put(-0.5f);
        vBuffer.put(0.5f).put(0.5f).put(-0.5f);
        vBuffer.put(0.5f).put(-0.5f).put(-0.5f);
        vBuffer.flip();

        //

        // create index buffer
        IntBuffer iBuffer = BufferUtils.createIntBuffer(16);

        // 0, 1, 2, 3, 0, 4, 5, 3, 5, 6, 2, 6, 7, 1, 7, 4}
        iBuffer.put(0);
        iBuffer.put(1);
        iBuffer.put(2);
        iBuffer.put(3);
        iBuffer.put(0);
        iBuffer.put(4);
        iBuffer.put(5);
        iBuffer.put(3);
        iBuffer.put(5);
        iBuffer.put(6);
        iBuffer.put(2);
        iBuffer.put(6);
        iBuffer.put(7);
        iBuffer.put(1);
        iBuffer.put(7);
        iBuffer.put(4);
        iBuffer.flip();

        IntBuffer ib = BufferUtils.createIntBuffer(2);

        GL15.glGenBuffers(ib);
        int vHandle = ib.get(0);
        //int cHandle = ib.get(1);
        //int iHandle = ib.get(2);
        int iHandle = ib.get(1);

        System.out.println(vHandle + " : vHandle et iHandle vaut : " + iHandle);
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        // glEnableClientState(GL11.GL_COLOR_ARRAY);

        glBindBufferARB(GL_ARRAY_BUFFER_ARB, vHandle);
        glBufferDataARB(GL_ARRAY_BUFFER_ARB, vBuffer, GL_STATIC_DRAW_ARB);
        glVertexPointer(3, GL11.GL_FLOAT, /* stride */ 3 << 2, 0L);


        //      glBindBufferARB(GL_ARRAY_BUFFER_ARB, cHandle);
//        glBufferDataARB(GL_ARRAY_BUFFER_ARB, cBuffer, GL_STATIC_DRAW_ARB);
           // glColorPointer(3, GL_FLOAT, /* stride */ 3 << 2, 0L);


        glBindBufferARB(GL_ELEMENT_ARRAY_BUFFER_ARB, iHandle);
        glBufferDataARB(GL_ELEMENT_ARRAY_BUFFER_ARB, iBuffer, GL_STATIC_DRAW_ARB);

        //glDrawArrays(GL_TRIANGLES, 0, 3 /* elements */);
        glDrawElements(GL11.GL_LINE_LOOP, /* elements */ 16, GL_UNSIGNED_INT, 0L);
        //gluLookAt(0.0f, -1.0f, 4.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 15.0f);

        // glBindBufferARB(GL_ARRAY_BUFFER_ARB, 0);
        //glBindBufferARB(GL_ELEMENT_ARRAY_BUFFER_ARB, 0);
        // glDisableClientState(GL_COLOR_ARRAY);
        //glDisableClientState(GL_VERTEX_ARRAY);
        System.out.println("Debug print : " + iBuffer + vBuffer);
        // cleanup VBO handles

        //ib.put(0, vHandle);


        // ib.put(1, cHandle);
        //ib.put(2, cHandle);

        //glDeleteBuffersARB(ib);
    }
} // end of Test
// www.tgustafson.com/2012/03/a-simple-lwjgl-interleaved-vbo-example/