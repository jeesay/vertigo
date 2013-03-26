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


public class NodeFactory {

    private static List<String> list = Arrays.asList("Cube","Group", "Light", "Shape");

   /**
    *
    * @author Jean-Christophe Taveau
    */
    public static Node get(String name) {
        int index = list.indexOf(name); 
        Node a_node = null;
        IJ.log("get node "+name + " "+ index);
        switch (index) {
        case 0: // Cube
            // TODO
            break;

        case 2: // Light
          a_node = new Light();
            break;

        case 3: // Shape
            a_node = new Shape();
            break;

        default:
            return null;
        }
        return a_node;
    }

} // End of class NodeFactory
