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
import java.util.HashMap;

/**
 * Class Attribute
 *
 * @author Florin Buga Olivier Catoliquot Clement Delestre
 */
public class Attribute {

    private String name;
    private int handle = -1;
    private static HashMap<String, String> table;
    private  String type;

    public Attribute(String name) {
        this.name = name;
        initTable();
    }

        public Attribute(String name,String type) {
        this.name = name;
        this.type=type;
        //initTable();
    }
    public String getType(){
        return type;
    }
        
    public Attribute(String name, int handle) {
        this.name = name;
        this.handle = handle;
        initTable();
    }

    private static void initTable() {
        // define the VBO/Attribute pair
        table.put("aPositionVertex", "V3F");
        table.put("aColorVertex", "C4F");
        table.put("aNormalVertex", "N3F");
    }

    public void setHandle(int handle) {
        this.handle = handle;
    }

    public int getHandle() {
        return handle;
    }

    public String getVBOtype(String type) {
        return table.get(type);
    }

    public String getName() {
        return name;
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
