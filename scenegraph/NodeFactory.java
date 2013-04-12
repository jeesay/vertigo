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
import java.util.List;
import java.util.Arrays;

import vertigo.scenegraph.shapes.Cube;

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
                break;
            case 585: // Camera
                break;
            case 383: // Cube
                a_node = new Cube("Cube");
                break;
            case 774: // FlatCube
                a_node = new Cube("Flat");
                break;
            case 1117: // FlatPyramid
            case 1543: // FlatTetrahedron
            case 525: // Group
            case 504: // Light
                a_node = new Light();
                break;
            case 822: // Lighting
            case 726: // Pyramid
            case 494: // Scene
            case 497: // Shape
                a_node = new Shape();
                break;
            case 615: // Sphere
            case 500: // Stage
            case 1152: // Tetrahedron
            case 541: // Torus
            case 956: // Transform
            case 729: // Viewing
            case 790: // WireCube
                a_node = new Cube("Wire");
                break;
            case 1133: // WirePyramid
            case 1022: // WireSphere
            case 948: // WireTorus
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
        for (int i=0; i< name.length();i++) index += (int) name.charAt(i);
        return index;
    }

} // End of class NodeFactory

