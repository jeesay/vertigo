/*
 * Copyright (C) 2011
 * Tim Gustafson
 * tjg@tgustafson.com
 * http://tgustafson.com/
 * All Rights Reserved
 *
 * Licensed for non-commercial use by an individual for educational purposes only.
 */

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Date;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL32;
import org.lwjgl.util.glu.GLU;
import vertigo.graphics.IBO;

public class SimpleInterleavedVboExample {

    public void start() {
        // create our display window
        try {
            Display.setTitle("Simple Interleaved Vbo Example");
            Display.setDisplayMode(new DisplayMode(600, 600));
            Display.create();
        } catch (Exception e) {
            System.err.println(e.toString());
            System.exit(1);
        }

        // set up OpenGL
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        GL11.glShadeModel(GL11.GL_SMOOTH);

        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        //GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
        //GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);

        // set up lighting
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_LIGHT0);

        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, floatBuffer(1.0f, 1.0f, 1.0f, 1.0f));
        GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, 25.0f);

        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, floatBuffer(-5.0f, 5.0f, 15.0f, 0.0f));

        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, floatBuffer(1.0f, 1.0f, 1.0f, 1.0f));
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, floatBuffer(1.0f, 1.0f, 1.0f, 1.0f));

        GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, floatBuffer(0.1f, 0.1f, 0.1f, 1.0f));

        // set up the camera
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GLU.gluPerspective(45.0f, 1.0f, 0.1f, 100.0f);

        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();

       int [] wireIndices = {0, 1, 2, 3, 0, 4, 5, 3, 5, 6, 2, 6, 7, 1, 7, 4};
        
        
        GLU.gluLookAt(
                0.0f,
                0.0f,
                5.0f,
                0.0f,
                0.0f,
                0.0f,
                0.0f,
                1.0f,
                0.0f);

        // create our vertex buffer objects
        IntBuffer buffer = BufferUtils.createIntBuffer(2);
        GL15.glGenBuffers(buffer);

        int vertex_buffer_id = buffer.get(0);
int iboId = buffer.get(1);

        float[] vertex_data_array = {
            //   x      y      z      nx     ny     nz     r      g      b      a
            
                // Front face
        -1.0f, -1.0f, 1.0f,
        1.0f, -1.0f, 1.0f,
        1.0f, 1.0f, 1.0f,
        -1.0f, 1.0f, 1.0f,
        // Back face
        -1.0f, -1.0f, -1.0f,
        -1.0f, 1.0f, -1.0f,
        1.0f, 1.0f, -1.0f,
        1.0f, -1.0f, -1.0f
                /*
            
            // back quad
            1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f,
            -1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f,
            -1.0f, -1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f,
            1.0f, -1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f,
            // front quad
            1.0f, 1.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f, 1.0f,
            -1.0f, 1.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f, 1.0f,
            -1.0f, -1.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f, 1.0f,
            1.0f, -1.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f, 1.0f,
            // left quad
            -1.0f, 1.0f, -1.0f, -1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f,
            -1.0f, 1.0f, 1.0f, -1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f,
            -1.0f, -1.0f, 1.0f, -1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f,
            -1.0f, -1.0f, -1.0f, -1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f,
            // right quad
            1.0f, 1.0f, -1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f,
            1.0f, -1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f,
            1.0f, -1.0f, -1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f,
            // top quad
            -1.0f, 1.0f, -1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f,
            -1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, -1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f,
            // bottom quad
            -1.0f, -1.0f, -1.0f, 0.0f, -1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f,
            -1.0f, -1.0f, 1.0f, 0.0f, -1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f,
            1.0f, -1.0f, 1.0f, 0.0f, -1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f,
            1.0f, -1.0f, -1.0f, 0.0f, -1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f*/
        };

        FloatBuffer vertex_buffer_data = BufferUtils.createFloatBuffer(vertex_data_array.length);
        vertex_buffer_data.put(vertex_data_array);
        vertex_buffer_data.rewind();
        
        
        IntBuffer ibobuff=BufferUtils.createIntBuffer(wireIndices.length);
        ibobuff.put(wireIndices);
        ibobuff.rewind();

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertex_buffer_id);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertex_buffer_data, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        
                GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER,iboId);
                GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, ibobuff, GL15.GL_STATIC_DRAW);
                GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        
        
        

        // set up frame rate counter stuff
        int framerate_count = 0;
        long framerate_timestamp = new Date().getTime();
        double rotate_x, rotate_y, rotate_z;

        while (!Display.isCloseRequested()) {
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

            // render the cube
            GL11.glVertexPointer(3, GL11.GL_FLOAT, 12, 0);
            //12=3*4
          //  GL11.glNormalPointer(GL11.GL_FLOAT, 40, 12);
            //GL11.glColorPointer(4, GL11.GL_FLOAT, 40, 24);

            //GL11.glDrawArrays(GL11.GL_LINES, 0, vertex_data_array.length);
GL11.glDrawElements(GL11.GL_LINES,ibobuff);
            // restore the matrix to pre-transformation values
            GL11.glPopMatrix();

            // update the display
            Display.update();
        }

        // clean things up
        Display.destroy();
    }

    public FloatBuffer floatBuffer(float a, float b, float c, float d) {
        float[] data = new float[]{a, b, c, d};
        FloatBuffer fb = BufferUtils.createFloatBuffer(data.length);
        fb.put(data);
        fb.flip();
        return fb;
    }

    public static void main(String[] args) {
        SimpleInterleavedVboExample example = new SimpleInterleavedVboExample();
        example.start();
    }

    public SimpleInterleavedVboExample() {
        this.start();
    }
}

// http://tgustafson.com/2012/03/a-simple-lwjgl-interleaved-vbo-example/