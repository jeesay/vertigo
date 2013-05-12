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

import java.util.ArrayList;
import vertigo.graphics.AABB;
import vertigo.graphics.Visitor;
import vertigo.math.Matrix4;

/**
 * Class Node
 *
 * @author Florin Buga
 * @author Olivier Catoliquot
 * @author Clement Delestre
 * @version 0.1
 *
 */
public abstract class Node {

    /**
     * Node's parent
     *
     * @see Node#setParent(vertigo.scenegraph.Node)
     */
    private Node parent;
    /**
     * Node's children. Node can have 0 or several children.
     *
     * @see Node#add(vertigo.scenegraph.Node)
     * @see Node#getChildren()
     * @see Node#getChild(int)
     */
    protected ArrayList<Node> children;
    /**
     * Node's coordinate Matrix4
     *
     * @see Node#getModelMatrix()
     * @see Node#setModelMatrix(vertigo.math.Matrix4)
     */
    private Matrix4 modelMatrix;
    /**
     * Node's AABB
     */
    protected AABB bbox;
    private byte dirty_;
    protected boolean drawable_;
    /**
     * The Node's name
     * @see Node#setName(java.lang.String) 
     * @see Node#getName() 
     */
    protected String name;
    
    
    public static byte MATRIX = 0x1;
    public static byte AABB = 0x2;
    public static byte VBO = 0x4;
    public static byte SHADER = 0x8;
    public static byte COLOR = 0x10;
    public static byte PROJMATRIX = 0x20;

    /**
     * Constructor
     */
    public Node() {
        default_create();
    }

    public Node(String name) {
        default_create();
        this.name = name;
    }

    /**
     * Sets Node name
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets Node name
     *
     * @return name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set node's Parent
     *
     * @param parent
     */
    public void setParent(Node a_node) {
        this.parent = a_node;
    }

    /**
     * Method for add one child.
     *
     * @param child
     */
    public void add(Node a_node) {
        children.add(a_node);
        a_node.setParent(this);
    }

    /**
     * Add a new node of type 'type'
     *
     * @param String type of node added to the scene graph. Allowed types are:
     * Branch, Camera, Cube, Light, Node, Shape, Transform
     * @return a node of g‪ivent type.
     */
    public Node addNewNode(String type) {
        Node newNode = NodeFactory.get(type);
        this.add(newNode);
        newNode.setParent(this);
        return newNode;
    }

    /**
     * Remove one child.
     *
     * @param a Node
     */
    public void remove(Node a_node) {
        children.remove(a_node);
    }

    /**
     * Gets the Node's children.
     *
     * @return ArrayList<Node>
     */
    public ArrayList<Node> getChildren() {
        return children;
    }

    /**
     * Gets a node's child.
     *
     * @param index
     * @return child
     */
    public Node getChild(int index) {
        return children.get(index);
    }

    /**
     * Gets the Node's parent
     *
     * @param parent.
     */
    public Node getParent() {
        return parent;
    }

    /**
     * Gets a node from the scenegraph of a given name.
     *
     * @params node_name : Node name.
     * @return Node.
     */
    public Node getNode(String node_name) {
        if (this.getName().equals(node_name)) {
            return this;
        } else {
            Node n = null;
            for (Node child : getChildren()) {
                n = child.getNode(node_name);
                if (n != null) {
                    return n;
                }
            }
        }
        return null;
    }

    /**
     * Gets the node's number of children.
     *
     * @return int.
     */
    public int size() {
        return children.size();
    }

    /**
     * Gets the Bounding Box
     *
     * @return AABB.
     */
    public AABB getBoundingBox() {
        if (bbox.isEmpty() || isDirty(Node.AABB)) {
            updateBoundingBox();
        }
        return bbox;
    }

    /**
     * Force recomputation of the bounding box
     *
     */
    public void updateBoundingBox() {
        for (Node child : getChildren()) {
            bbox.expand(child.getBoundingBox());
        }
        setDirty(AABB, false);
    }

    /**
     * Get the model matrix from local to world coordinates
     *
     * @return Node's matrix.
     */
    public Matrix4 getModelMatrix() {
        return modelMatrix;
    }

    /**
     * Set the model matrix expressed in world coordinates
     *
     */
    public void setModelMatrix(Matrix4 m) {
        modelMatrix = m;
    }

    /**
     * Get status of this node
     *
     * @return Flag giving the overall status of this node
     */
    public boolean isDirty() {
        return ((dirty_ & 0xff) == 0xff);
    }

    /**
     * Get status of this node
     *
     * @param Only check the status of a given properties: MATRIX, VBO, SHADER
     * @return Flag giving the status of this node
     */
    public boolean isDirty(int flag) {
        return ((dirty_ & flag) == flag);
    }

    /**
     * Set status of this node
     *
     * @param Only check the status of a given properties: MATRIX, VBO, SHADER
     * @return Flag giving the status of this node
     */
    public void setDirty(byte flag, boolean value) {
        if (value) {
            dirty_ = (byte) (dirty_ | flag);
        } else {
            dirty_ = (byte) (dirty_ & ~flag);
        }
    }

    /**
     * Set status of this node and propagate to children
     *
     * @param Only check the status of a given properties: MATRIX, VBO, SHADER
     * @return Flag giving the status of this node
     */
    public void setAllDirty(byte flag, boolean value) {
        setDirty(flag, value);
        for (Node n : children) {
            n.setAllDirty(flag, value);
        }
    }

    public boolean check() {
        return true; // TODO
    }

    public abstract void accept(Visitor visitor);

    public boolean isDrawable() {
        return drawable_;
    }

    private void default_create() {
        children = new ArrayList<Node>();
        parent = null;
        // Init matrix
        modelMatrix = new Matrix4();
        modelMatrix.setIdentity();
        bbox = new AABB();
        name = "node";
        dirty_ = (byte) 0xff;
        drawable_ = false;
    }
} // End of class Node
