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
import vertigo.graphics.AABB;
import vertigo.math.Matrix4;
import vertigo.math.Vector3;
import java.util.Iterator;

    /**
     * Class Node
     * @author Florin Buga
     * @author Olivier Catoliquot
     * @author Clement Delestre
     * @version 0.1
     *
     */
public class Node {

    private Node parent;
    private ArrayList<Node> children;
    private Matrix4 matrix;
    private Matrix4 ModelView;
    private AABB bbox;
    private byte dirty_;
    protected String name;


    public static int MATRIX = 0x1;
    public static int GEOMETRY = 0x2;
    public static int SHADER = 0x4;
    public static int AABB = 0x8;
    public static int COLOR = 0xA;

    /**
     * Constructor
     */
    public Node() {
        children = new ArrayList();
        Node parent = null;
        matrix = new Matrix4();
        matrix.setIdentity();
        bbox = new AABB();
        name = "node";
        dirty_ = 0xff;
    }

    /**
     * Set Node name
     * @param String
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get Node name
     * @return String
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set node's Parent
     * @param Node
     */
    public void setParent(Node a_node) {
        this.parent = a_node;
    }

    /**
     * Method for add one child.
     * @param Node anode
     */
    public void add(Node a_node) {
        children.add(a_node);
        a_node.setParent(this);
    }

    /**
     * Add a new node of type 'type'
     *
     * @param String type of node added to the scene graph.
     * Allowed types are: Branch, Camera, Cube, Light, Node, Shape, Transform
     * @return a node of g‪ivent type.
     */
    public Node addNewNode(String type){
        Node newNode= NodeFactory.get(type);
        this.add(newNode);
        newNode.setParent(this);
        return newNode;
    }

    /**
     * Remove one child.
     * @param Node anode
     */
    public void remove(Node a_node) {
        children.remove(a_node);
    }

    /**
     * Get the Node's children.
     * @param Node anode
     * @return ArrayList
     */
    public ArrayList<Node> getChildren() {
        return children;
    }

    /**
     * Get a node's child.
     *
     * @param index
     * @return ArrayList
     */
    public Node getChild(int index) {
        return children.get(index);
    }

    /**
     * Get the Node's parent
     *
     * @return Node.
     */
    public Node getParent() {
        return parent;
    }

    /**
     * Get the node's number of children.
     *
     * @return int.
     */
    public int size() {
        return children.size();
    }

    /**
     * Get the Bounding Box
     *
     * @return Node.
     */
    public AABB getBoundingBox() {
        if (bbox.isEmpty() ) {
            for (Node child : getChildren() )
                bbox.expand(child.getBoundingBox() );
            setDirty(AABB,false);
        }

        return bbox;
    }

    /**
     * Set Translation
     *
     * @param tx,ty,tz as floats
     */
    public void setTranslation(float tx, float ty, float tz){
        matrix.setTranslation(new Vector3(tx,ty,tz));
    }

    /**
     * Set Position
     *
     * @param tx,ty,tz as floats
     */
    public void setPosition(float tx, float ty, float tz){
        setTranslation(tx,ty,tz);
    }

    /**
     * Set Direction
     *
     * @param x,y,z as floats
     */
    public void setDirection(float x,float y, float z){
        //TODO with Matrix4 (look at)
    }

    /**
     * Set the scale
     *
     * @param scale as floats
     */
    public void setScale(float s){
        //TODO
        matrix.setScale(s);
    }
   
    /**
     * Get status of this node
     *
     * @return Flag giving the overall status of this node
     */
    public boolean isDirty(){
        return ( (dirty_ & 0xff) == 0xff);
    }

    /**
     * Get status of this node
     *
     * @param Only check the status of a given properties: MATRIX, VBO, SHADER
     * @return Flag giving the status of this node
     */
    public boolean isDirty(int flag){
        return ( (dirty_ & flag) == flag);
    }

    /**
     * Set status of this node
     *
     * @param Only check the status of a given properties: MATRIX, VBO, SHADER
     * @return Flag giving the status of this node
     */
    public boolean setDirty(int flag, boolean value){
        if (value)
            dirty_ = dirty_ | flag;
        else
            dirty_ = dirty_ & ~flag;
    }

    /**
     * Set status of this node and children
     *
     * @param Only check the status of a given properties: MATRIX, VBO, SHADER
     * @return Flag giving the status of this node
     */
    public boolean setAllDirty(int flag, boolean value){
        if (value)
            dirty_ = dirty_ | flag;
        else
            dirty_ = dirty_ & ~flag;
        for (Node n : children)
            n.setAllDirty(flag,value);
    }


    public boolean check(){
        return true; // TODO
    }

    public void accept(Visitor visitor) {
        // In node, the visitor must be accepted by children to
        // propagate its action.
        visitor.visit(this);
        for (Node child : getChildren()) {
            child.accept(visitor);
        }
    }


   /**
     * Get the root.
     *
     * @return Node.
     */
    public Node traverseUp() {
        if (parent == null) {
            System.out.println("La racine a été trouvée ! Elle se nomme "+this.getName());
            return this;
        } else {
            Node a_node = this.getParent();
            a_node.traverseUp();
        }
        return null;
    }
/**
     * Get the leafs.
     *
     * @return Node.
     */
    public void traverseDown() {
        if (children.isEmpty()) {
            System.out.println("Une feuille a été trouvée ! Elle se nomme "+this.getName());
            
        } else {
            for (Iterator<Node> it = children.iterator(); it.hasNext();) {
                Node nodetemp = it.next();
                nodetemp.traverseDown();
            }
        }
        
    }


} // End of class Node
