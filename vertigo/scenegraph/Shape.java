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

import vertigo.graphics.Visitor;
import vertigo.math.AxisAngle4;
import vertigo.math.Matrix4;
import vertigo.math.Vector3;

/**
 * Class Shape
 *
 * @author Florin Buga
 * @author Olivier Catoliquot
 * @author Clement Delestre
 * @version 0.1
 *
 */
public class Shape extends Node {
    /**
     * Each Shape have a Geometry.
     */

    protected Geometry geo;
     /**
     * Each Shape have a Material.
     */
    protected Material material;
     /**
     * Each Shape have a drawable style.
     */
    private String style_;
     /**
     * Each Shape have a local matrix.
     */
    protected Matrix4 matrix;
    /**
     * Number of shapes.
     */
    public static int count = 0;
    /**
     * Constructor.
     */

    public Shape() {
        super();
        // Init matrix
        matrix = new Matrix4();
        matrix.setIdentity();
        drawable_ = true;
        geo = new Geometry();
        material = new Material();
        name = "shape_" + count;
        count++;
    }

    /**
     * Constructor with name.
     * @param name 
     */
    public Shape(String name) {
        super(name);
        // Init matrix
        matrix = new Matrix4();
        matrix.setIdentity();
        drawable_ = true;
        geo = new Geometry();
        material = new Material();
    }

    /**
     * Gets the Shape's Geometry
     *
     * @return geo
     */
    public Geometry getGeometry() {
        return geo;
    }

    /**
     * Sets the Geometry
     *
     * @param types
     * @param data as float[]
     */
    public void setGeometry(String type, float[] data) {
        geo.setVertices(type, data);

        if (type.equals("V3F")) {
            bbox.expand(data);
            setDirty(Node.AABB, false);
        }

    }

    /**
     * Sets the Geometry
     *
     * @param types
     * @param data as float[]
     */
    public void setGeometry(String[] types, float[] data) {
        if (types.length == 1 && types[0].equals("V3F")) {
            geo.setVertices(types[0], data);
            bbox.expand(data);
        } else {
            geo.setVertices(types, data);
            int offset = 0; // TODO
            int step = 0; // TODO
            for (int i = offset; i < data.length; i += step) {
                bbox.expand(data[i], data[i + 1], data[i + 2]);
            }
            setDirty(Node.AABB, false);
        }
    }

    /**
     * Sets the Indexed Geometry
     *
     * @param types, data as float[] 
     *@param indices as int []
     */
    public void setIndexedGeometry(String type, float[] data, int[] indices) {
        setGeometry(type, data);
        setIndices(indices);
    }

    /**
     * Sets the Indexed Geometry
     *
     * @param  data as float[] 
     * @param indices as int []
     */
    public void setIndexedGeometry(String[] types, float[] data, int[] indices) {
        setGeometry(types, data);
        setIndices(indices);
    }

    /**
     * Sets the Indexed Geometry
     *
     * @param indices as int []
     *
     */
    public void setIndices(int[] indices) {
        geo.setIndices(indices);
    }

    /**
     * Sets the Color (RGBA format)
     *
     * @param  red : float value between 0 and 255
     * @param  green : float value between 0 and 255
     * @param  blue : float value between 0 and 255
     * @param alpha : float value between 0 and 255
     */
    public void setColor(int red, int green, int blue, int alpha) {
        material.setColor(red, green, blue, alpha);
    }

    /**
     * Sets the Color (RGB format)
      * @param  red : float value between 0 and 255
     * @param  green : float value between 0 and 255
     * @param  blue : float value between 0 and 255
     */
    public void setColor(int red, int green, int blue) {
        material.setColor(red, green, blue);
    }

    public void setShaderMaterial(String shaderName) {
        // TODO
    }

    private boolean isPacked(String type) {
        // TODO
        boolean coma = false;
        coma = type.matches(".*,.*");
        return coma;
    }

    /**
     * Get the drawing style
     *
     * @return style - Available styles: POINTS, LINES, LINE_STRIP, TRIANGLES,
     * LINE_LOOP, TRIANGLE_FAN
     *
     */
    public String getDrawingStyle() {
        return style_;
    }

    /**
     * Sets the drawing style
     *
     * @param style - Available styles: POINTS, LINES, LINESTRIP, TRIANGLES
     *
     */
    public void setDrawingStyle(String style) {
        style_ = style;
    }

    public void setGraphicsType(String type) {
        //TODO
        // need a factory ?
    }

    /**
     * Rotate this shape
     *
     * @param angle_in_degrees rotation angle expressed in degree
     * @param axis_x,axis_y,axis_z XYZ coordinates of the rotation axis
     */
    public void rotate(float angle_in_degrees, float axis_x, float axis_y, float axis_z) {
        matrix.setRotation(new AxisAngle4(axis_x, axis_y, axis_z, (float) (angle_in_degrees / 180.0f * Math.PI)));
    }

    /**
     * Translate this shape
     *
     * @param tx as floats
     * @param ty as floats
     * @param ty as floats
     */
    public void translate(float tx, float ty, float tz) {
        matrix.setTranslation(new Vector3(tx, ty, tz));
    }

    /**
     * Set the Shape's scale
     *
     * @param float s
     */
    public void scale(float s) {
        //TODO
        matrix.setScale(s);
    }

    /**
     * Get the Shape's Matrix
     *
     * @return Matrix4
     */
    public Matrix4 getMatrix() {
        return matrix;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    /**
     *
     * @return Material
     */
    public Material getMaterial() {
        return material;
    }
} // End of class Shape
