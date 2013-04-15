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
import vertigo.math.Vector3;
import java.util.Iterator;

/**
 * Class Transform
 *
 * @author Florin Buga
 * @author Olivier Catoliquot
 * @author Clement Delestre
 * @version 0.1
 *
 */
public class Transform extends Node {

    // Local matrix
    private Matrix4 matrix;

    /**
     * Constructor
     */
    public Transform() {
        // Init matrix
        matrix = new Matrix4();
        matrix.setIdentity();
    }

    public Transform(String name){
        default_create();
        this.name=name; 
        // Init matrix
        matrix = new Matrix4();
        matrix.setIdentity();
    }

    /**
     * Set Rotation about X axis
     *
     * @param angle_in_degrees rotation angle expressed in degree
     */
    public void setRotX(float angle_in_degree) {
        // TODO
    }

    /**
     * Set Rotation about Y axis
     *
     * @param angle_in_degrees rotation angle expressed in degree
     */
    public void setRotY(float angle_in_degree) {
        // TODO
    }

    /**
     * Set Rotation about Z axis
     *
     * @param angle_in_degrees rotation angle expressed in degree
     */
    public void setRotZ(float angle_in_degree) {
        // TODO
    }

    /**
     * Set Rotation
     *
     * @param angle_in_degrees rotation angle expressed in degree
     * @param axis_x,axis_y,axis_z XYZ coordinates of the rotation axis
     */
    public void setRotation(float angle_in_degree, float axis_x, float axis_y, float axis_z) {
        // TODO
    }

    /**
     * Set Translation
     *
     * @param tx,ty,tz as floats
     */
    public void setTranslation(float tx, float ty, float tz) {
        matrix.setTranslation(new Vector3(tx, ty, tz));
    }

    /**
     * Set Rotation about X axis
     *
     * @param tx,ty,tz as floats
     */
    public void rotX(float angle_in_degree) {
        // TODO
    }

    /**
     * Set Rotation about Y axis
     *
     * @param tx,ty,tz as floats
     */
    public void rotY(float angle_in_degree) {
        // TODO
    }

    /**
     * Set Rotation about Z axis
     *
     * @param tx,ty,tz as floats
     */
    public void rotZ(float angle_in_degree) {
        // TODO
    }

    /**
     * Set Rotation
     *
     * @param tx,ty,tz as floats
     */
    public void rotate(float angle_in_degree, float axis_x, float axis_y, float axis_z) {
        // TODO
    }

    /**
     * Set Translation
     *
     * @param tx,ty,tz as floats
     */
    public void translate(float tx, float ty, float tz) {
        setTranslation(tx, ty, tz);
    }


    /**
     * Set Position
     *
     * @param tx,ty,tz as floats
     */
    public void setPosition(float tx, float ty, float tz) {
        setTranslation(tx, ty, tz);
    }

    /**
     * Set Direction
     *
     * @param x,y,z as floats
     */
    public void setDirection(float x, float y, float z) {
        //TODO with Matrix4 (look at)
    }

    /**
     * Set the scale
     *
     * @param scale as floats
     */
    public void setScale(float s) {
        //TODO
        matrix.setScale(s);
    }

    /**
     * Get this matrix
     *
     * @return Node.
     */
    public Matrix4 getMatrix() {
        return matrix;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
    


} // End of class Transform
