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
package vertigo.graphics;

import java.util.ArrayList;

/**
 * Class Attribute
 *
 * @author Florin Buga Olivier Catoliquot Clement Delestre
 */
public class Attribute {

    protected String name;
    protected int handle;

    public Attribute(String name) {
        this.name = name;
    }

   /* public void bindVBO(ArrayList<BO> buffer) {
        for (BO bo : buffer) {
            VBO vbo = (VBO) bo;
            String VBOtype = vbo.getType();
            if (name.equals(vbo.getType())) {

               /* GL20.glGetAttribLocation(glshader.getHandle(), "aVertexPosition");
                GL20.glEnableVertexAttribArray(i); //set the vertex's position

                //  GL20.glenableVertexAttribArray(shaderProgram.vertexPositionAttribute);
                GL20.glVertexAttribPointer(i, vbo.capacity(), false, getSize(attribute), vbo.getFloatBuffer());
                ShaderUtils.useShader(glshader.getHandle());
            }
        }

    }*/
} // end of class Attribute
