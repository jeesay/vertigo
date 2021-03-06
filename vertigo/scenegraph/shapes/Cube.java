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
package vertigo.scenegraph.shapes;

import vertigo.scenegraph.Geometry;
import vertigo.scenegraph.Shape;

/**
 * Predefined shape: the cube WIRECUBE, FLATCUBE, CUBE
 *
 * @author Jean-Christophe Taveau
 *
 **/
public class Cube extends Shape {

    private int type;
    private float w_, h_, d_;

    private float[] wireVertices = {  
        // Front face
        -1.0f, -1.0f,  1.0f,
         1.0f, -1.0f,  1.0f,
         1.0f,  1.0f,  1.0f,
        -1.0f,  1.0f,  1.0f,
   
         // Back face
        -1.0f, -1.0f, -1.0f,
        -1.0f,  1.0f, -1.0f,
         1.0f,  1.0f, -1.0f,
         1.0f, -1.0f, -1.0f
    };
    private int[] wireIndices = {0,1,2,3,0,4,5,3,5,6,2,6,7,1,7,4};

    private float[] flatvertices = {
  // Front face: white
  -1.0f, -1.0f,  1.0f, 1.0f,  1.0f,  1.0f,  1.0f,
   1.0f, -1.0f,  1.0f, 1.0f,  1.0f,  1.0f,  1.0f,
   1.0f,  1.0f,  1.0f, 1.0f,  1.0f,  1.0f,  1.0f,
  -1.0f,  1.0f,  1.0f, 1.0f,  1.0f,  1.0f,  1.0f,
   
  // Back face: red
  -1.0f, -1.0f, -1.0f, 1.0f,  0.0f,  0.0f,  1.0f,
  -1.0f,  1.0f, -1.0f, 1.0f,  0.0f,  0.0f,  1.0f,
   1.0f,  1.0f, -1.0f, 1.0f,  0.0f,  0.0f,  1.0f,
   1.0f, -1.0f, -1.0f, 1.0f,  0.0f,  0.0f,  1.0f,
   
  // Top face: green
  -1.0f,  1.0f, -1.0f, 0.0f,  1.0f,  0.0f,  1.0f,
  -1.0f,  1.0f,  1.0f, 0.0f,  1.0f,  0.0f,  1.0f,
   1.0f,  1.0f,  1.0f, 0.0f,  1.0f,  0.0f,  1.0f,
   1.0f,  1.0f, -1.0f, 0.0f,  1.0f,  0.0f,  1.0f,
   
  // Bottom face: blue
  -1.0f, -1.0f, -1.0f, 0.0f,  0.0f,  1.0f,  1.0f,
   1.0f, -1.0f, -1.0f, 0.0f,  0.0f,  1.0f,  1.0f,
   1.0f, -1.0f,  1.0f, 0.0f,  0.0f,  1.0f,  1.0f,
  -1.0f, -1.0f,  1.0f, 0.0f,  0.0f,  1.0f,  1.0f,
   
  // Right face: yellow
   1.0f, -1.0f, -1.0f, 1.0f,  1.0f,  0.0f,  1.0f,
   1.0f,  1.0f, -1.0f, 1.0f,  1.0f,  0.0f,  1.0f,
   1.0f,  1.0f,  1.0f, 1.0f,  1.0f,  0.0f,  1.0f,
   1.0f, -1.0f,  1.0f, 1.0f,  1.0f,  0.0f,  1.0f,
   
  // Left face: purple
  -1.0f, -1.0f, -1.0f, 1.0f,  0.0f,  1.0f,  1.0f,
  -1.0f, -1.0f,  1.0f, 1.0f,  0.0f,  1.0f,  1.0f,
  -1.0f,  1.0f,  1.0f, 1.0f,  0.0f,  1.0f,  1.0f,
  -1.0f,  1.0f, -1.0f, 1.0f,  0.0f,  1.0f,  1.0f
    };

    float[] flatIndices = {
       0,  1,  2,      0,  2,  3,    // front
       4,  5,  6,      4,  6,  7,    // back
       8,  9, 10,      8, 10, 11,   // top
      12, 13, 14,     12, 14, 15,   // bottom
      16, 17, 18,     16, 18, 19,   // right
      20, 21, 22,     20, 22, 23    // left
    };


    public static final int WIRE = 0;
    public static final int FLAT = 1;
    public static final int CUBE = 2;

    public Cube() {
        super();
        this.name = "cube";
        type = WIRE;
        w_ = 1.0f;
        h_ = 1.0f;
        d_ = 1.0f;
}

    public Cube(String name) {
        super(name);
        if (name.equals("Wire") ) {
            setDrawingStyle("LINE_STRIP");
            setType(WIRE);
        }
        else if  (name.equals("Flat") ) {
            setDrawingStyle("TRIANGLES");
            setType(FLAT);
        }
        else if  (name.equals("Cube") ) {
            setDrawingStyle("TRIANGLES"); 
            setType(CUBE);
        }
        else{
            setDrawingStyle("POINTS");
            setType(WIRE);
        }
        w_ = 1.0f;
        h_ = 1.0f;
        d_ = 1.0f;
}

    public void setType(int type) {
        this.type = type;
    }

    public void setSize(float width, float height, float depth) {
        w_ = width;
        h_ = height;
        d_ = depth;
    }

    @Override
    public Geometry getGeometry() {
        if (geo.getAllBO().isEmpty() )
           switch (this.type) {
           case WIRE : 
               create_wirecube();
               break;
           case FLAT : 
               create_flatcube();
               break;
           case CUBE : 
               create_texcube();
               break;
           default : 
               create_flatcube();
               break;
           }
        return geo;
    }

    private void create_wirecube() {
        float[] data;
        // 1- modify dimension
        if (w_ != 1.0f && h_ != 1.0f && d_ != 1.0f) {
            data = new float[wireVertices.length];
            for (int i =0; i< wireVertices.length;i+=3) {
               data[i]   = wireVertices[i  ] * w_;
               data[i+1] = wireVertices[i+1] * h_;
               data[i+2] = wireVertices[i+2] * d_;
            }
        }
        else
            data = wireVertices;

        for (int i =0; i<wireVertices.length;i+=3) 
            System.out.println(wireVertices[i  ] +" "+wireVertices[i+1] +" "+wireVertices[i+2]);

        // 2- create geometry
        geo.addBuffer("V3F",data);
        geo.addIndices(wireIndices);
        // 3- create material
        // use default
      
    }

    private void create_flatcube() {
           if (w_ != 1.0f && h_ != 1.0f && d_ != 1.0f) {
            for (int i =0; i<flatvertices.length;i+=3) {
               flatvertices[i  ] *= w_;
               flatvertices[i+1] *= h_;
               flatvertices[i+2] *= d_;
            }
        }
           // 2- create geometry
           geo.setVertices(new String[]{"V3F","C4F"},flatvertices);
           // 3- create material
           material.setShaderMaterial("flat");
    }

    private void create_texcube() {
           if (w_ != 1.0f && h_ != 1.0f && d_ != 1.0f) {
            for (int i =0; i<wireVertices.length;i+=3) {
               wireVertices[i  ] *= w_;
               wireVertices[i+1] *= h_;
               wireVertices[i+2] *= d_;
            }
        }
           geo.setVertices("V3F",wireVertices);
    }


    
} // End of class Cube
