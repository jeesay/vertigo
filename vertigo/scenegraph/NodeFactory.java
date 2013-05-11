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
package vertigo.scenegraph;

import ij.IJ;

import vertigo.scenegraph.shapes.Cube;
import vertigo.scenegraph.shapes.Pyramid;
import vertigo.scenegraph.shapes.Sphere;
import vertigo.scenegraph.shapes.Tetrahedron;
import vertigo.scenegraph.shapes.Torus;

public class NodeFactory {

    /**
     *
     * @author Jean-Christophe Taveau
     */
   public static Node get(String name) {
        int index = calcIndex(name);
        Node a_node = null;
        IJ.log("get node " + name + " " + index);
        switch (index) {
            case 869: // BackStage
                IJ.log("Add a BackStage is not allowed.");
                break;
            case 585: // Camera
                a_node = new Camera("Camera");
                break;
            case 383: // Cube
                a_node = new Cube("Cube");
                break;
            case 774: // FlatCube
                a_node = new Cube("Flat");
                break;
            case 1117: // FlatPyramid
                break;
            case 1543: // FlatTetrahedron
                break;
            case 525: // Group
                a_node = new Group();
                break;
            case 504: // Light
                a_node = new Light();
                break;
            case 822: // Lighting
                IJ.log("Add a Lighting is not allowed.");
                break;
            case 726: // Pyramid
                a_node = new Pyramid();
                break;
            case 494: // Scene
                a_node = new Scene();
                break;
            case 497: // Shape
                a_node = new Shape();
                break;
            case 615: // Sphere
                a_node = new Sphere();
                break;
            case 500: // Stage              
                IJ.log("Add a Stage is not allowed.");
                break;
            case 1152: // Tetrahedron
                a_node = new Tetrahedron();
                break;
            case 541: // Torus
                a_node = new Torus();
                break;
            case 956: // Transform
                a_node = new Transform();
                break;
            case 729: // Viewing
                IJ.log("Add a Viewing is not allowed.");
                break;
            case 790: // WireCube
                a_node = new Cube("Wire");
                break;
            case 1133: // WirePyramid
                 a_node = new Pyramid("Wire");
                break;
            case 1022: // WireSphere
                break;
            case 948: // WireTorus
                break;
            case 520: // World
                IJ.log("Add a World is not allowed.");
                break;
            default:
                // Do nothing

                IJ.showMessage("Error Node ", name + " is unknown");
        }
        return a_node;
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
} // End of class NodeFactory

