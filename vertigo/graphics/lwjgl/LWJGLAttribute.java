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

import java.util.ArrayList;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import vertigo.graphics.Attribute;
import vertigo.graphics.BO;
import vertigo.graphics.VBO;

/**
 * Class LWJGLAttribute
 *
 * @author Florin Buga Olivier Catoliquot Clement Delestre
 */
public class LWJGLAttribute extends Attribute {

    public LWJGLAttribute(String name) {
        super(name);
    }

    public void bindVBO(ArrayList<BO> buffer, int i) {
        for (BO bo : buffer) {
            VBO vbo = (VBO) bo;
            if (compare( vbo)) {

                GL20.glGetAttribLocation(this.handle, name);
                GL20.glEnableVertexAttribArray(i); //set the vertex's position

                //  GL20.glenableVertexAttribArray(shaderProgram.vertexPositionAttribute);
                GL20.glVertexAttribPointer(i, vbo.capacity(), false, getSize(name), vbo.getFloatBuffer());
                ShaderUtils.useShader(this.handle);
            }
        }

    }
    
     public void bindVBO(VBO vbo, int i) {
      
            if (compare(vbo)) {
                GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo.getHandle());
                GL20.glGetAttribLocation(this.handle, name);
                GL20.glEnableVertexAttribArray(i); //set the vertex's position

                //  GL20.glenableVertexAttribArray(shaderProgram.vertexPositionAttribute);
                GL20.glVertexAttribPointer(i, vbo.capacity(), false, vbo.getStride(name), vbo.getFloatBuffer());
                //GL20.glVertexAttribPointer(i, vbo.capacity(), vbo.getSize(), false, vbo.getStride(),vbo.getFloatBuffer());
                //GL20.glVertexAttribPoin
            
                
                ShaderUtils.useShader(this.handle);
            }
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

    private boolean compare(VBO vbo) {
        if (name.contains("color") && vbo.getType().equals("C4F")) {
            return true;
        } else if (name.contains("Vertex") && vbo.getType().equals("V3F")) {
            return true;
        } else if (name.contains("texture") && vbo.getType().equals("T2F")) {
            return true;
        }
        return false;
    }
}
