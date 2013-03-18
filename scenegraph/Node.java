/*
 * $Id:$
 *
 * crazybio_viewer: 3D Viewer Plugin for ImageJ.
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

import java.util.ArrayList;
import vertigo.math.Matrix4;

public class Node {

    private Node parent;
    private ArrayList<Node> children;
    private Matrix4 matrix;

    //private-protected ?
    public Node() {
        children = new ArrayList();
        Node parent = null;
        matrix = new Matrix4();
        matrix.setIdentity();
    }

    /**
     *
     *
     */
    public void setParent(Node anode) {
        this.parent = anode;
    }

    public void add(Node anode) {

        children.add(anode);
        anode.setParent(this);

    }

    public void remove(Node anode) {

        children.remove(anode);

    }

    public ArrayList<Node> getChildren() {
        return children;
    }

    public Node getChild(int index) {
        return children.get(index);
    }

    public void getParent() {
    }

    public void size() {
    }

    public void traverseUp() {
    }

    public void traverseDown() {
    }
    
    
} // End of class Node
