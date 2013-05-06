
//package com.jogl.examples.trianglerenderer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import com.jogamp.common.nio.Buffers;
import vertigo.graphics.BufferData;

public class VBORenderer {

    private int _totalNumVerts;
    private int _vbo;
    private int _vertexStride;
    private int _colorPointer;
    private int _vertexPointer;
    private float[] triangles;
    private BufferData buff;

    public VBORenderer() {
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

    public void init(GL2 gl) {
        _totalNumVerts = triangles.length * 3;

        //Generate a VBO pointer / handle.
        IntBuffer buf = Buffers.newDirectIntBuffer(1);
        gl.glGenBuffers(1, buf);
        _vbo = buf.get();

        //Interleave vertex/color data.
        FloatBuffer data = Buffers.newDirectFloatBuffer(triangles.length * 18);
        for (int i = 0; i < triangles.length; i++) {
            for (int j = 0; j < 3; j++) {
                data.put(triangles[i].getColors()[j]);
                data.put(triangles[i].getVertices()[j]);
            }
        }
        data.rewind();

        int bytesPerFloat = Float.SIZE / Byte.SIZE;

        //transfer data to vbo.
        int numBytes = data.capacity() * bytesPerFloat;
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, _vbo);
        gl.glBufferData(GL.GL_ARRAY_BUFFER, numBytes, data, GL.GL_STATIC_DRAW);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);

        _vertexStride = 6 * bytesPerFloat;
        _colorPointer = 0;
        _vertexPointer = 3 * bytesPerFloat;
    }

    public void render(GL2 gl) {
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, _vbo);

        gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL2.GL_COLOR_ARRAY);

        gl.glColorPointer(3, GL.GL_FLOAT, _vertexStride, _colorPointer);
        gl.glVertexPointer(3, GL.GL_FLOAT, _vertexStride, _vertexPointer);

        gl.glDrawArrays(GL.GL_TRIANGLES, 0, _totalNumVerts);

        gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL2.GL_COLOR_ARRAY);

        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
    }

    public void dispose(GL2 gl) {
        gl.glDeleteBuffers(1, new int[]{_vbo}, 0);
    }

    public String toString() {
        return "Vertex Buffer Object (VBO)";
    }
}
