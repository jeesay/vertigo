/*
 * $Id:$
 *
 * Vertigo_viewer: 3D Viewer Plugin for ImageJ.
 * Copyright (C) 2013 Jean-Christophe Taveau.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 *
 * Authors :
 * Florin Buga
 * Olivier Catoliquot
 * Clement Delestre
 */
package vertigo.graphics.lwjgl;

import ij.IJ;
import java.nio.IntBuffer;
import java.util.ArrayList;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glColorPointer;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glVertexPointer;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;
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
 * Class LWJGL_Visitor
 *
 * @author Florin Buga
 * @author Olivier Catoliquot
 * @author Clement Delestre
 * @version 0.1
 * @see Visitor
 *
 */
public class LWJGL_Visitor implements Visitor {
    /**
     * Camera.
     * @see Camera
     */
    private Camera cam_;
    /**
     * Check if the shape have IBO or not.
     */
    private boolean isIndexed = false;
    /**
     * IBO.
     * @see IBO
     */
    private IBO ibo = null;
    /**
     * VBO.
     * @see VBO.
     */
    private VBO vbo3f = null;
    /**
     * The VBO's length.
     */
    private int capacity = 0;
    /**
     * ShaderProg
     * @see ShaderProg
     */
    private ShaderProg glshader;
    /**
     * ArrayList of shader's attribute.
     * @see Attribute
     */
    private ArrayList<Attribute> attribute;
    private IntBuffer ib;

    @Override
    public void visit(BackStage obj) {
        // do nothing
    }
    
    /**
     * Gets Camera's Matrix
     * @param obj Camera
     *  @see Visitor
     */

    @Override
    public void visit(Camera obj) {
        cam_ = obj;
    }

    /**
     * Process the matrix
     * @param obj Light
     *  @see Visitor
     */
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

    /**
     * Process the matrix and draw the shape.
     * @param obj Shape
     *  @see Visitor
     */
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
/**
 * Process the matrix.
 * @param obj Transform
 * @see Visitor
 */
    @Override
    public void visit(Transform obj) {
        if (obj.isDirty(Node.MATRIX)) {
            obj.getModelMatrix().mul(obj.getParent().getModelMatrix(), obj.getMatrix());
            obj.setDirty(Node.MATRIX, false);
        }
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
            obj.setDirty(Node.MATRIX, false);
        }
    }

    private void drawShape(Shape obj) {
        // PreProcessing
        if (obj.isDirty(Node.SHADER)) {
            processShader(obj);
            obj.setDirty(Node.SHADER, false);
        }
        GL20.glUseProgram(obj.getMaterial().getShaderMaterial().getHandle());
        processUniform(obj);
        // Geometry: VBO and IBO
        processBO(obj);
        obj.setDirty(Node.VBO, false);
        GL20.glUseProgram(0);
    }
/**
 * Process the shader.
 * @param obj shader
 */
    private void processShader(Shape obj) {
        int handle;
        glshader = obj.getMaterial().getShaderMaterial();
        // compile once
        if (glshader.getHandle() == ShaderProg.UNKNOWN) {
            try {
                handle = ShaderUtils.attachShaders(glshader.getVertexSource(), glshader.getFragmentSource());
                glshader.setHandle(handle);
            } catch (Exception e) {
                IJ.log("Error with the Shader " + e);
            } //Compile, Link error
        }

    }
/**
 * Process the BO.
 * @param obj shape
 */
    private void processBO(Shape obj) {

        int nbBO = obj.getGeometry().getNumberBO(); //return 2 for the cube (1 vbo + 1 ibo)
        ib = BufferUtils.createIntBuffer(nbBO);
        GL15.glGenBuffers(ib);
        int i = 0;
        for (BO bo : obj.getGeometry().getBuffers()) {
            int boHandle = ib.get(i);
            bo.setHandle(boHandle);
            i++;
            if (bo instanceof IBO) {
                isIndexed = true;
                ibo = (IBO) bo;
                GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, boHandle);
                GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo.getIntBuffer(), GL15.GL_STATIC_DRAW);
            } else {
                VBO vbo = (VBO) bo;
                if (vbo.getType().contains("V")) {
                    vbo3f = vbo;
                    capacity = vbo.capacity();
                }
                GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, boHandle);
                attribute = glshader.getAllAttributes();
                GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vbo.getFloatBuff(), GL15.GL_STATIC_DRAW);
                for (Attribute attrib : attribute) {
                    if (vbo.getType().equals(attrib.getType())) {
                        int alocation = GL20.glGetAttribLocation(glshader.getHandle(), attrib.getName());
                        GL20.glEnableVertexAttribArray(alocation);
                        GL20.glVertexAttribPointer(alocation, attrib.getSize(), GL11.GL_FLOAT, false, vbo.getStride() << 2, 0L);
                    }
                }
                GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
            }
        }
        if (isIndexed) { // draw with index
            glDrawElements(getOpenGLStyle(obj.getDrawingStyle()), /* elements */ ibo.getSize(), GL_UNSIGNED_INT, 0L);
            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
            GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
            GL20.glDisableVertexAttribArray(0);
        } else {
            // draw withouh index
            GL11.glDrawArrays(getOpenGLStyle(obj.getDrawingStyle()), 0, capacity / vbo3f.getStride());
            GL20.glDisableVertexAttribArray(0);
            GL30.glBindVertexArray(0);
        }
        GL15.glDeleteBuffers(ib);
    }
/**
 * Makes conversion Vertigo style/OpenGL style
 * @param vertigo_style
 * @return 
 */
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
/**
 * Links shader's uniform variable.
 * @param obj shape
 */
    private void processUniform(Shape obj) {

        ArrayList<Uniform> uniforms = glshader.getAllUniforms();
        for (Uniform uni : uniforms) {
            if (uni.getType().equals("view_matrix")) {
                int vlocation = GL20.glGetUniformLocation(glshader.getHandle(), uni.getName());
                GL20.glUniformMatrix4(vlocation, false, cam_.getViewMatrix().toColumnBuffer());
            } else if (uni.getType().equals("proj_matrix")) {
                int plocation = GL20.glGetUniformLocation(glshader.getHandle(), uni.getName());
                GL20.glUniformMatrix4(plocation, false, cam_.getProjection().toColumnBuffer());
            } else if (uni.getType().equals("matrix")) {
                int mlocation = GL20.glGetUniformLocation(glshader.getHandle(), uni.getName());
                GL20.glUniformMatrix4(mlocation, false, obj.getModelMatrix().toColumnBuffer());
            } else if (uni.getType().equals("C4F")) {
                int clocation = GL20.glGetUniformLocation(glshader.getHandle(), uni.getName());
                float[] color = new float[4];
                color = obj.getMaterial().getColor();
                GL20.glUniform4f(clocation, color[0], color[1], color[2], color[3]);
            }
        }
    }

    /**
     * Delete Buffers and Shader when the window is closed.
     */
    public void dispose() {
        GL15.glDeleteBuffers(ib);
        glshader.setHandle(ShaderProg.UNKNOWN);
        GL20.glDeleteProgram(glshader.getHandle());
    }
} // end of class LWJGL_Visitor
// http://www.java-gaming.org/index.php?topic=24272.0