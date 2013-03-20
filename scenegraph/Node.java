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
import java.util.Iterator;

public class Node {
/** 
 * Classe Node
 *@author Florin Buga Olivier Catoliquot Clement Delestre
 * @version 1.0

 */
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
     *@param String
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
     *Sets node's Parent
     * 
     *@param Node 
     */
    public void setParent(Node a_node) {
        this.parent = a_node;
    }
    
/**
	 * Method for add one child.
         * 
	 * @param Node anode
	 */
    public void add(Node a_node) {
        children.add(a_node);
        a_node.setParent(this);
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
 * @param index
 * @return ArrayList
 */
    public Node getChild(int index) {
        return children.get(index);
    }
/**
	 * Get the Node's parent
	 * @return  Node.
	 */
    public Node getParent() {
        return parent;
    }
/**
	 * Get the node's number of children.
	 * @return  int.
	 */
    public  int size() {
        return children.size();
    }

    public Node traverseUp() {
        if (parent==null){
                System.out.println("La racine a été trouvée !");
               return this;
        }
        else{
             Node a_node=this.getParent();
             a_node.traverseUp();
             
        }
        return null;
    }

    public Node traverseDown() {
           if(children.isEmpty()){
               System.out.println("Une feuille a été trouvée !");
               return this;
           }
           else {
            for (Iterator i = children.iterator(); i.hasNext();){
		Node nodetemp=(Node)i.next();
                nodetemp.traverseDown();
            }
           }
           return null;
    }
} // End of class Node
