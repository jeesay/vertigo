/*
 * $Id:$
 *
 * Vertigo: 3D Viewer Plugin for ImageJ.
 * Copyright (C) 2013  Jean-Christophe Taveau.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301  USA, or see the FSF site: http://www.fsf.org.
 *
 * Authors :
 * Florin Buga
 * Olivier Catoliquot
 * Clement Delestre
 */
package vertigo.graphics.lwjgl;

import ij.IJ;
import java.awt.Graphics;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL40;
import vertigo.graphics.Attribute;
import vertigo.graphics.BO;
import vertigo.graphics.BufferTools;
import vertigo.graphics.IBO;
import vertigo.graphics.Props;
import vertigo.graphics.ShaderProg;
import vertigo.graphics.Uniform;
import vertigo.graphics.VBO;
import vertigo.graphics.Visitor;
import vertigo.math.Matrix4;
import vertigo.scenegraph.BackStage;
import vertigo.scenegraph.Camera;
import vertigo.scenegraph.Geometry;
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
 * @author Florin Buga Olivier Catoliquot Clement Delestre
 */
public class LWJGL_Visitor implements Visitor {

    private int level;
    private Camera cam_;
    private Graphics g;

    @Override
    public void visit(BackStage obj) {
        //do nothing
    }

    @Override
    public void visit(Camera obj) {
        cam_ = obj;
        setModelMatrix(obj);
      
        //glGetUniformLocation 
        //GL20.glUniformMatrix4(level, true, view);

    }

    @Override
    public void visit(Light obj) {
        setModelMatrix(obj);
    }

    @Override
    public void visit(Lighting obj) {
        //do nothing
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
        //do nothing
    }

    @Override
    public void visit(Transform obj) {
        setModelMatrix(obj);
    }

    @Override
    public void visit(Viewing obj) {
        //do nothing
    }

    @Override
    public void visit(World obj) {
        //do nothing
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
        boolean isIndexed = false;
        IBO ibo = null;
        ShaderProg glshader = new ShaderProg();
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
        // Material: shader
        if (obj.isDirty(Node.SHADER)) {
            glshader = processShader(obj);
            obj.setDirty(Node.SHADER, false);
        }

        /*
         GL11.glBegin(GL11.GL_POINTS);
         GL11.glVertex3f(0f, 1f, 0f);
         GL11.glVertex3f(-1f, 0f, 0f);
         GL11.glVertex3f(-0.7f, 0f, 0f);
         GL11.glVertex3f(-0.6f, 0f, 0f);
         GL11.glVertex3f(-0.5f, 0f, 0f);
         GL11.glVertex3f(1f, 0f, 0f);
         GL11.glEnd();*/


        // Use Program
         glshader = obj.getMaterial().getShaderMaterial();
        GL20.glUseProgram(glshader.getHandle());
        ArrayList<Uniform> uniforms = glshader.getAllUniforms();
        for (Uniform uni : uniforms) {
            if (uni.getType().equals("view_matrix")) {
                GL20.glGetUniformLocation(glshader.getHandle(), uni.getName());
                GL20.glUniformMatrix4(uni.getHandle(), false,cam_.getProjection().toBuffer());
            } else if (uni.getType().equals("proj_matrix")) {
                GL20.glGetUniformLocation(glshader.getHandle(), uni.getName());
                GL20.glUniformMatrix4(uni.getHandle(), false, cam_.getViewMatrix().toBuffer());
            } else if (uni.getType().equals("matrix")) {
                GL20.glGetUniformLocation(glshader.getHandle(), uni.getName());
                GL20.glUniformMatrix4(uni.getHandle(), false,obj.getMatrix().toBuffer());
            }
        }
        // Update uniforms
        // GL30.glUniform2u(bo.getHandle(), vbo.getFloatBuffer());


        ArrayList<Attribute> atribute = glshader.getAllAttributes();

//for (String uniforms : allUniforms) {

        // uniformFadeFactor = glGetUniformLocation(glshader, uniforms);
        ///////////////////////////////////////////////////////
        //int uniformFadeFactor= GL20.glGetUniformLocation(glshader.getUniformLocation(glshader.getName()), glshader.getName());
        //GL20.glUniformMatrix4(uniformFadeFactor, false, null);
        ///////////////////////////////////////////////////
        // glshader.getUniformLocation(glshader.getName());
        //        }




        // Bind BOs and Update attributes



//GL11.glInterleavedArrays(format, stride, pointer);
//use for interleaved







        ShaderUtils.useShader(obj.getMaterial().getShaderMaterial().getHandle());



        //ShaderUtils.dontUseShader();


        int a = 0;
        while (a < atribute.size()) {
            for (BO bo : obj.getGeometry().getBuffers()) {
                if (bo instanceof VBO) {
                    VBO vbo = (VBO) bo;
                    if (vbo.getType().equals(atribute.get(a).getVBOtype(atribute.get(a).getName()))) { //null exception ?
                        if (!vbo.isBound()) {
                            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo.getHandle());
                            // GL20.glGetAttribLocation(atribute.get(a).getHandle(), vbo.getType());
                            GL20.glEnableVertexAttribArray(atribute.get(a).getHandle()); //set the VBO's offset

                            //  GL20.glenableVertexAttribArray(shaderProgram.vertexPositionAttribute);
                            GL20.glVertexAttribPointer(vbo.getOffset(), vbo.capacity(), false, vbo.getStride(), vbo.getFloatBuffer());
                            // i or vbo.getOffset?
                            //GL20.glVertexAttribPointer(i, vbo.capacity(), vbo.getSize(), false, vbo.getStride(),vbo.getFloatBuffer());
                            //GL20.glVertexAttribPoin

                            vbo.setBound(true);
                        }
                    }




                    //String type;
                    //int stride =0;
                    //ArrayList<String> types = (ArrayList<String>) vbo.getProps().keys();
                    // for (String type : props.getkeys() ) {
                    //   for (String attribute : allAttributes) {
                    /* 
                     int numtype = calcIndex(type);
                     switch (numtype) {
                     case 207: // V3F
                     if (attribute.equals("aVertexPosition")) {
                     //gl.getAttribLocation(shaderProgram, "aVertexPosition");
                     GL20.glGetAttribLocation(glshader.getHandle(), "aVertexPosition");
                     GL20.glEnableVertexAttribArray(i); //set the vertex's position

                     //  GL20.glenableVertexAttribArray(shaderProgram.vertexPositionAttribute);
                     GL20.glVertexAttribPointer(i, vbo.capacity(), false, getSize(attribute), vbo.getFloatBuffer());
                     ShaderUtils.useShader(glshader.getHandle());
                                    
                     }
                     //aVertexPosition
                     case 189: // C4F
                     //aVertexColor
                     case 204: // T2F
                     //aTexture
                     default: // Do nothing  
                     }*/
                    // }
                    //   if (vbo.IsInterleaved()){
                    //     stride+=vbo.getStride(type);
                    //      get(type);
                    //}

                    //GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo.getHandle());
                    //glvertexAttribPointer(glshader.getAttributeLocation(allAttributes.get(i)),
                    //...)

                    //  for (String attribute : allAttributes) {

                    //GL20.glVertexAttribPointer(i, vbo.capacity(), false, getSize(attribute), vbo.getFloatBuffer());
                    // link vertex and shader's attributes
                    //  }
                    // i : indice
                    // capacity : size
                    //normalised ???-> False
                    //getSize -> Stirde
                    // getFloatBuffer : java.nio.FloatBuffer buffer
                    //GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, i, vbo.getFloatBuffer());
                    //update VBO

                    // glshader.getAttributeLocation(allAttributes.get(i))

                } else if (bo instanceof IBO) {
                    ibo = (IBO) bo;
                    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, bo.getHandle());
                    isIndexed = true;
                }
            }
            a++;
        }
// Draw
        if (isIndexed) {

            GL11.glDrawElements(getOpenGLStyle(obj.getDrawingStyle()), ibo.getIntBuffer());

        } else {
            System.out.println("isIndexed is false");
            GL11.glDrawArrays(getOpenGLStyle(obj.getDrawingStyle()), 0, obj.getGeometry().getCount());
        }
        GL20.glUseProgram(0);
    }

    private void processBO(Shape obj) {
        //int count = obj.getGeometry().getBuffers().size();
        //int count = obj.getGeometry().getCount();
        int count = obj.getGeometry().getCount();
        System.out.println(" count is : " + count);
        IntBuffer bufferid = BufferTools.newIntBuffer(count);
        GL15.glGenBuffers(bufferid);
        int i = 0;
        for (BO bo : obj.getGeometry().getBuffers()) {
            // get ID
            int handle = bufferid.get(i);
            System.out.println("ID is : " + handle);
            bo.setHandle(handle);
            i++;
            System.out.println("Handle is : " + bo.getHandle());

            if (bo instanceof IBO) {
                IBO ibo = (IBO) bo;
                // bind a buffer
                GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, handle);
                GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo.getIntBuffer(), GL15.GL_STATIC_DRAW);
                // release ibo
                GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
            } else {
                VBO vbo = (VBO) bo;
                // bind a buffer
                GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, handle);
                GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vbo.getFloatBuffer(), GL15.GL_STATIC_DRAW);
                // release vbo
                GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
            }
        }
    }

    private ShaderProg processShader(Shape obj) {
        int handle;
        ShaderProg glshader = obj.getMaterial().getShaderMaterial();
        // compile once
        if (glshader.getHandle() == ShaderProg.UNKNOWN) {
            try {
                handle = ShaderUtils.attachShaders(glshader.getVertexSource(), glshader.getFragmentSource());
                glshader.setHandle(handle);
            } catch (Exception e) {
                IJ.log("Error with the Shader");
            } //Compile, Link error
        }

        ShaderUtils.updateShader(glshader);
        return glshader;
    }

    /* 
     * Get the Shape's drawing style and convert it in OpenGL style
     * @param String vertigo_style
     * @return tab[0] : OpenGL variable tab[1] size of this variable
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

    private int getSize(String type) {
        if (type.contains("color")) {
            return 4;
        } else if (type.contains("Vertex")) {
            return 3;
        } else if (type.contains("texture")) {
            return 2;
        }
        return 4;
    }
} // End of class LWJGL_Visitor
