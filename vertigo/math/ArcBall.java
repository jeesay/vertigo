/*
 * $Id:$
 *
 * vertigo, volume viewing Tools for ImageJ.
 * Copyright (C) 2009-2013  Jean-Christophe Taveau.
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
 *  Mickael Escudero
 *  Ibouniyamine Nabihoudine
 *  Frederic Romagné
 *  Michael Vacher
 */

package vertigo.math;

import java.util.Observable;

/**
 * Adapted from Nehe Lesson 48 (@author Pepijn Van Eeckhoudt)
 * http://www.java-tips.org/other-api-tips/jogl/arcball-rotation-nehe-tutorial-jogl-port.html
 */
public class ArcBall  {

    private static final float Epsilon = 1.0e-5f;
    Vector3 StVec;          //Saved click vector
    Vector3 EnVec;          //Saved drag vector
    float adjustWidth;       //Mouse bounds width
    float adjustHeight;      //Mouse bounds height
    private Matrix4 LastRot;
    private Matrix4 ThisRot;
    private Quat4 NewRot;
    private final Object matrixLock = new Object();


    public ArcBall() {
        StVec = new Vector3();
        EnVec = new Vector3();
        LastRot = new Matrix4();
        ThisRot = new Matrix4();
        NewRot = new Quat4();
    }

 
    public void update(Observable o, Object an_event) {
/*
        if (an_event instanceof VIJ_Event) {
            e = (VIJ_Event) an_event;
            switch (e.type) {
                case VIJ_Event.MOUSE_PRESS:
                    click(e.x, e.y);
                    break;
                case VIJ_Event.MOUSE_DRAG:
                    drag(e.x, e.y);
                    this.getParent().setMatrix4(ThisRot);
                    break;
                case VIJ_Event.MOUSE_RELEASE:
                    break;
                default:
                // Do nothing
            }

        }*/
    }

    /**
     *  Reset view (keyboard R or mouse centre button)
     */
    void reset() {
        synchronized (matrixLock) {
            LastRot.setIdentity();    // Reset Rotation
            ThisRot.setIdentity();    // Reset Rotation
        }

    }

    public void setBounds(float NewWidth, float NewHeight) {
        assert ((NewWidth > 1.0f) && (NewHeight > 1.0f));

        //Set adjustment factor for width/height
        adjustWidth = 1.0f / ((NewWidth - 1.0f) * 0.5f);
        adjustHeight = 1.0f / ((NewHeight - 1.0f) * 0.5f);
    }

    //Mouse down
    private void click(int mouse_x, int mouse_y) {
        LastRot.set(ThisRot);
        mapToSphere(new Point2(mouse_x, mouse_y), this.StVec);

    }

    //Mouse drag, calculate rotation
    private void drag(int new_mouse_x, int new_mouse_y) {

        //Map the point to the sphere
        this.mapToSphere(new Point2(new_mouse_x, new_mouse_y), this.EnVec);

        //Return the quaternion equivalent to the rotation
        Vector3 Perp = new Vector3();

        //Compute the vector perpendicular to the begin and end vectors
        Perp.cross(StVec, EnVec);

        //Compute the length of the perpendicular vector
        if (Perp.length() > Epsilon) //if its non-zero
        {
            //We're ok, so return the perpendicular vector as the transform after all
            NewRot.x = Perp.x;
            NewRot.y = Perp.y;
            NewRot.z = Perp.z;
            //In the quaternion values, w is cosine (theta / 2), where theta is rotation angle
            NewRot.w = StVec.dot(EnVec);
        } else //if its zero
        {
            //The begin and end vectors coincide, so return an identity transform
            NewRot.x = NewRot.y = NewRot.z = NewRot.w = 0.0f;
        }

        ThisRot.set(NewRot);     // Convert Quaternion Into Matrix3fT
        ThisRot.mul(ThisRot, LastRot);   // Accumulate Last Rotation Into This One
    }

    private void mapToSphere(Point2 point, Vector3 vector) {
        //Copy parameter into temp point
        Point2 tempPoint = new Point2(point.x, point.y);

        // TODO setBounds((int) this.getParent().getFloatProperty("viewportWidth"), (int) this.getParent().getFloatProperty("viewportHeight"));

        //Adjust point coords and scale down to range of [-1 ... 1]
        tempPoint.x = (tempPoint.x * this.adjustWidth) - 1.0f;
        tempPoint.y = 1.0f - (tempPoint.y * this.adjustHeight);

        //Compute the square of the length of the vector to the point from the center
        float length = (tempPoint.x * tempPoint.x) + (tempPoint.y * tempPoint.y);

        //If the point is mapped outside of the sphere... (length > radius squared)
        if (length > 1.0f) {
            //Compute a normalizing factor (radius / sqrt(length))
            float norm = (float) (1.0 / Math.sqrt(length));

            //Return the "normalized" vector, a point on the sphere
            vector.x = tempPoint.x * norm;
            vector.y = tempPoint.y * norm;
            vector.z = 0.0f;
        } else //Else it's on the inside
        {
            //Return a vector to a point mapped inside the sphere sqrt(radius squared - length)
            vector.x = tempPoint.x;
            vector.y = tempPoint.y;
            vector.z = (float) Math.sqrt(1.0f - length);
        }

    }
}
