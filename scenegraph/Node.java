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
    protected String name;

    public Node() {
        children = new ArrayList();
        Node parent = null;
        matrix = new Matrix4();
        matrix.setIdentity();
        name = "node";
    }

    /**
     * Sets Node name
     *
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get Node name
     *
     * @return String
     */
    public String getName() {
        return this.name;
    }

    /**
     *
     *
     */
    public void setParent(Node a_node) {
        this.parent = a_node;
    }

    public void add(Node a_node) {
        children.add(a_node);
        a_node.setParent(this);
    }

    public void remove(Node a_node) {
        children.remove(a_node);
    }

    public ArrayList<Node> getChildren() {
        return children;
    }

    public Node getChild(int index) {
        return children.get(index);
    }

    public Node getParent() {
        return parent;
    }

    public int size() {
        return children.size();
    }

    public void traverseUp() {
    }

    public void traverseDown() {
    }
} // End of class Node
