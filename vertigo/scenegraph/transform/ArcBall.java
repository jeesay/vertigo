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
 * Florin Buga
 * Olivier Catoliquot
 * Clement Delestre
 */
package vertigo.scenegraph.transform;

import java.util.Observable;
import vertigo.graphics.event.Signal;
import vertigo.graphics.event.MouseObserver;
import vertigo.graphics.event.MouseSignal;
import vertigo.graphics.event.ViewportObserver;
import vertigo.graphics.event.ViewportSignal;
import vertigo.math.Matrix4;
import vertigo.math.Point2;
import vertigo.math.Vector3;
import vertigo.math.Quat4;
import vertigo.scenegraph.Node;
import vertigo.scenegraph.Transform;

/**
 * Class ArcBall
 *
 * @author Florin Buga
 * @author Olivier Catoliquot
 * @author Clement Delestre
 * @version 0.1
 *
 */

/**
 * Adapted from Nehe Lesson 48 (@author Pepijn Van Eeckhoudt)
 * http://www.java-tips.org/other-api-tips/jogl/arcball-rotation-nehe-tutorial-jogl-port.html
 */
public class ArcBall extends Transform implements MouseObserver, ViewportObserver {

    private static final float EPSILON = 1.0e-5f;
    Vector3 vtFrom;          //Saved click vector
    Vector3 vtTo;          //Saved drag vector
    float adjustWidth;       //Mouse bounds width
    float adjustHeight;      //Mouse bounds height
    private int mouse_x;
    private int mouse_y;
    private Quat4 lastRot; // Last 3x3 Rotation matrix
    private Quat4 thisRot; // Current 3x3 Rotation matrix
    private boolean isClicked;
    private boolean isDragging;


    public ArcBall() {
        super();
        this.name = "arcball";
        vtFrom = new Vector3();
        vtTo = new Vector3();
        // Init matrices
        lastRot = new Quat4();
        thisRot = new Quat4();
        thisRot.set(1.0f,0.0f,0.0f,0.0f);
        matrix = new Matrix4();
        matrix.setIdentity();
    }


    @Override
    public void update(Observable o, Object o1) {
        if (o1 instanceof MouseSignal) 
            updateMouse( (MouseSignal) o1 );
        else if (o1 instanceof ViewportSignal) {
            ViewportSignal signal = (ViewportSignal) o1;
            setBounds(signal.getWidth(), signal.getHeight() );
        }
    }

    public void setBounds(float newWidth, float newHeight) {
        assert ((newWidth > 1.0f) && (newHeight > 1.0f));
        //Set adjustment factor for width/height
        adjustWidth = 1.0f / ((newWidth - 1.0f) * 0.5f);
        adjustHeight = 1.0f / ((newHeight - 1.0f) * 0.5f);
    }


    private void updateMouse(MouseSignal e) {
        // if Mouse Down and dragging
        if ( e.getButton() == Signal.BUTTON_LEFT && e.getButtonStatus() == Signal.MOVED ) {
            // System.out.println("Arcball : left drag " + e);
            // Update End Vector And Get Rotation As Quaternion
            drag(new Point2(e.getX(), e.getY()), thisRot);
            
            // Accumulate Last Rotation Into This One
            thisRot.mul(lastRot);
            thisRot.normalize();
            // Set Our Final Transform's Rotation From This One
            matrix.setRotation(thisRot);          
            setAllDirty(Node.MATRIX,true);
        }
        // if Mouse Down and not dragging: First Click
        else if (e.getButton() == Signal.BUTTON_LEFT)             
        {
            // System.out.println("Arcball : left click " + e);
            // Set Last Rotation To Current Rotation
            lastRot.set(thisRot);
            // Update Start Vector And Prepare For Dragging
            click(new Point2(e.getX(), e.getY()) );
        }

        else {
            // Do nothing for other events 
        }

    }

    //Mouse down
    private void click(Point2 newPt) {
        mapToSphere(newPt, this.vtFrom);
    }

    //Mouse drag, calculate rotation
    private void drag(Point2 newPt, Quat4 newRot) {
        //Map the point to the sphere
        mapToSphere(newPt, vtTo);

        //Return the quaternion equivalent to the rotation
        if (newRot != null) {
            Vector3 vAxis = new Vector3();

            //Compute the vector perpendicular to the begin and end vectors
            vAxis.cross(vtFrom, vtTo);

            //Compute the length of the perpendicular vector
            if (vAxis.length() > EPSILON) //if its non-zero
            {
                //We're ok, so return the perpendicular vector as the transform after all
                //In the quaternion values, w is cosine (theta / 2), where theta is rotation angle
                newRot.set(vAxis.x, vAxis.y, vAxis.z, vtFrom.dot( vtTo) );
            } else //if its zero
            {
                //The begin and end vectors coincide, so return an identity transform
                newRot.set(1.0f,0.0f,0.0f,0.0f);
            }
        }
    }


    private void mapToSphere(Point2 point, Vector3 vector) {
        //Copy parameter into temp point
        Point2 tempPoint = new Point2(point.x, point.y);

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
        // System.out.println("MAP " + point + " = " + tempPoint + " -> " + length);
        vector.normalize();
    }

/******
http://cgmath.blogspot.fr/2009/03/arc-ball-rotation-using-quaternion.html
Math3D::vector vtFrom;
Math3D::vector vtTo;
gluUnProject(m_PrePoint.x,m_PrePoint.y,0,fModeView,fProjection,iViewPort,&vtFrom.x,&vtFrom.y,&vtFrom.z);
gluUnProject(point.x,point.y,0,fModeView,fProjection,iViewPort,&vtTo.x,&vtTo.y,&vtTo.z);
vtFrom.y = -vtFrom.y;
vtTo.y = -vtTo.y;

vtFrom.normalize(); 
vtTo.normalize();
Math3D::vector vAxis = vtFrom.Cross(  vtTo );
vAxis.normalize(); 
float fTheta  = acos(vtFrom.Dot(vtTo));
Math3D::Quaternion qTmp;

qTmp.FromAxisAngle( vAxis, fTheta *2 );
qTmp.Normalize();
// multiply the current quaternion with the new one.
m_Quat *= qTmp;
m_PrePoint = point;
// normalise it.
m_Quat.Normalize();
****/


} // end of class ArcBall

