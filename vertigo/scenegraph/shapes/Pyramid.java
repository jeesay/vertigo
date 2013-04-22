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

import vertigo.scenegraph.Shape;

/**
 * Class Pyramid
 *
 * @author Florin Buga
 * @author Olivier Catoliquot
 * @author Clement Delestre
 * @version 0.1
 *
<<<<<<< HEAD
 * @author Jean-Christophe Taveau
=======
>>>>>>> e679ec07b42b376145c0f6d07941e8d680408633
 */

public class Pyramid extends Shape {

    private int type;
    private float w_, h_, d_;

    private static final int WIRE = 0;
    private static final int FLAT = 1;

    float[] vertices = {
            // Front face
             0.0f,  1.0f,  0.0f,
            -1.0f, -1.0f,  1.0f,
             1.0f, -1.0f,  1.0f,

            // Right face
             0.0f,  1.0f,  0.0f,
             1.0f, -1.0f,  1.0f,
             1.0f, -1.0f, -1.0f,

            // Back face
             0.0f,  1.0f,  0.0f,
             1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f, -1.0f,

            // Left face
             0.0f,  1.0f,  0.0f,
            -1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f,  1.0f
    };

    float[] colors = {
            // Front face
            1.0f, 0.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f,

            // Right face
            1.0f, 0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f,

            // Back face
            1.0f, 0.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f,

            // Left face
            1.0f, 0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f
    };

    public Pyramid() {
        super();
        this.name = "pyramid";
        setDrawingStyle("TRIANGLES");
        w_ = 1.0f;
        h_ = 1.0f;
        d_ = 1.0f;
}

    public Pyramid(String name) {
        super(name);
        if (name.equals("Wire") ) {
            setDrawingStyle("LINES");
            setType(WIRE);
        }
        else{
            setDrawingStyle("TRIANGLES");
            setType(FLAT);
        }

        w_ = 1.0f;
        h_ = 1.0f;
        d_ = 1.0f;
    }
<<<<<<< HEAD

    public void setType(int type) {
        switch (type) {
        case WIRE:
            create_wirepyramid(); 
            break;
        default:
            create_pyramid(); 
        }
    }

    private void create_wirepyramid() {
        // 1- modify dimension
        if (w_ != 1.0f && h_ != 1.0f && d_ != 1.0f) {
/**
            for (int i =0; i<wireVertices.length;i+=3) {
               vertices[i  ] *= w_;
               vertices[i+1] *= h_;
               vertices[i+2] *= d_;
            }
**/
        }
        // 2- create geometry
        geo.addBuffer("V3F",vertices);
        geo.addBuffer("C3F",colors);
        // 3- create material
        // use default
      
    }

    private void create_pyramid() {
    }

}
=======
} // End of class Pyramid
>>>>>>> e679ec07b42b376145c0f6d07941e8d680408633
