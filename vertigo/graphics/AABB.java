/*
 * $Id:$
 *
 * vertigo: 3D Viewer Plugin for ImageJ.
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
 * Jean-Christophe Taveau
 *
 */

package vertigo.graphics;

import vertigo.math.Point3;
import vertigo.math.Point4;

/**
 * Axis-Aligned Bounding Box
 *
 *  @author: Jean-Christophe Taveau
 *
 **/

public class AABB {

    private float min_x = Float.MAX_VALUE;
    private float min_y = Float.MAX_VALUE;
    private float min_z = Float.MAX_VALUE;
    private float max_x = Float.MIN_VALUE;
    private float max_y = Float.MIN_VALUE;
    private float max_z = Float.MIN_VALUE;

    private boolean empty_;

    public AABB() {
        // Do nothing
        empty_ = true;
    }

    public AABB(float x0,float y0,float z0,float x1,float y1,float z1) {
        expand(x0,y0,z0);
        expand(x1,y1,z1);
    }

    public AABB(float[] points) {
        expand(points);
    }

    public AABB(Point3 v0, Point3 v1) {
        expand(v0.x,v0.y,v0.z);
        expand(v1.x,v1.y,v1.z);
    }

    public AABB(Point3 v0, float w, float h, float d) {
        expand(v0.x,v0.y,v0.z);
        expand(v0.x+w,v0.y+h,v0.z+d);
    }

    /**
     * Get a bounding sphere enclosing this bounding box
     * @return an array of 4 floats containing XYZ-coordinates of the center and the sphere radius, respectively.
     */
    public float[] getBoundingShere4f() {
        return new float[] {
            (min_x + max_x)/2.0f,
            (min_y + max_y)/2.0f, 
            (min_z + max_z)/2.0f,
            (float) Math.sqrt(
                (min_x - max_x)*(min_x - max_x)
              + (min_y - max_y)*(min_y - max_y)
              + (min_z - max_z)*(min_z - max_z) )
        };
    }

    /**
     * Get a bounding sphere enclosing this bounding box
     * @return a Point4 containing XYZ-coordinates of the center and the sphere radius, respectively.
     */
    public Point4 getBoundingShere() {
        return new Point4(
            (min_x + max_x)/2.0f,
            (min_y + max_y)/2.0f, 
            (min_z + max_z)/2.0f,
            (float) Math.sqrt(
                (min_x - max_x)*(min_x - max_x)
              + (min_y - max_y)*(min_y - max_y)
              + (min_z - max_z)*(min_z - max_z) )
        );
    }

    /**
     * Get radius of a bounding sphere enclosing this bounding box
     * @return array of float containing X-, Y- and Z-coordinates, respectively
     */    public float getRadius() {
        return (float) Math.sqrt(
                (min_x - max_x)*(min_x - max_x)
              + (min_y - max_y)*(min_y - max_y)
              + (min_z - max_z)*(min_z - max_z) );
    }

    /**
     * Get center of a bounding sphere enclosing this bounding box
     * @return array of float containing X-, Y- and Z-coordinates, respectively
     */
    public float[] getCenter3f() {
        return new float[]{ (min_x + max_x)/2.0f,(min_y + max_y)/2.0f, (min_z + max_z)/2.0f };
    }

    /**
     * Get center of this bounding box
     * @return Point3
     */
    public Point3 getCenter() {
        return new Point3( (min_x + max_x)/2.0f,(min_y + max_y)/2.0f, (min_z + max_z)/2.0f );
    }

    public float getMinX() {
      return min_x;
    }

    public float getMinY() {
      return min_y;
    }

    public float getMinZ() {
      return min_x;
    }

    public float getMaxX() {
      return max_x;
    }

    public float getMaxY() {
      return max_y;
    }

    public float getMaxZ() {
      return max_x;
    }

    public float[] getMin3f() {
      return new float[]{min_x,min_y,min_z};
    }

    public Point3 getMin() {
      return new Point3(min_x,min_y,min_z);
    }

    public float[] getMax3f() {
      return new float[]{max_x,max_y,max_z};
    }

    public Point3 getMax() {
      return new Point3(max_x,max_y,max_z);
    }

    public float getWidth() {
      return Math.abs(min_x - max_x);
    }

    public float getHeight() {
      return Math.abs(min_y - max_y);
    }

    public float getDepth() {
      return Math.abs(min_z - max_z);
    }

    public final void expand(float x, float y, float z) {
        min_x = Math.min(x,min_x);
        max_x = Math.max(x,max_x);
        min_y = Math.min(y,min_y);
        max_y = Math.max(y,max_y);
        min_z = Math.min(z,min_z);
        max_z = Math.max(z,max_z);
        empty_ = false;
    }

    public final void expand(float[] points) {
        for (int i=0; i < points.length; i+=3)
            expand(points[i],points[i+1],points[i+2]);
    }

    public void expand(Point3 v) {
        expand(v.x,v.y,v.z);
    }

    public void expand(AABB other) {
        expand(other.getMinX(), other.getMinY(), other.getMinZ() );
        expand(other.getMaxX(), other.getMaxY(), other.getMaxZ() );
    }

    public boolean isEmpty() {
        return empty_;
    }

    public boolean contains(float x, float y, float z) {
        if (x >= min_x && x <= max_x && y >= min_y && y <= max_y && z >= min_z && z <= max_z)
            return true;
        else
            return false;
    }

} // End of class AABB

