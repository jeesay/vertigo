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
package vertigo.scenegraph;

import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import vertigo.graphics.event.MouseObserver;
import vertigo.graphics.event.MouseSignal;
import vertigo.graphics.Visitor;
import vertigo.math.Matrix4;
import vertigo.math.Point2;
import vertigo.math.Point3;
import vertigo.math.Vector3;
import ij.IJ;


/**
 * Class Camera
 *
 * @author Florin Buga
 * @author Olivier Catoliquot
 * @author Clement Delestre
 * @author Jean-Christophe Taveau
 * @version 0.1
 *
 */
public class Camera extends Node implements MouseObserver {

    private Matrix4 view_matrix;
    private Matrix4 proj_matrix;
    private int vp_width;
    private int vp_height;
    private float fovy;
    private float aspect;
    private float znear;
    private float zfar;
    private int proj_type = 0;
    private final static float ZOOM_FACTOR = 0.01;

    private final static int PERSPECTIVE = 0;
    private final static int ORTHOGRAPHIC = 1;


    private static List<String> output = Arrays.asList("Screen", "Image");
    private static List<String> types = Arrays.asList("Perspective", "Orthographic");

    public Camera() {
        super();
        proj_matrix = new Matrix4();
        proj_type = PERSPECTIVE;
        fovy = (float) (45.0 * Math.PI / 180.0);
        aspect = 1.0f;
        znear = 0.1f;
        zfar = 100.0f;
        name = "camera";
        view_matrix = new Matrix4();
        view_matrix.setIdentity();
        this.setOutput("Screen");
    }

    public Camera(String name){
        super(name);
        proj_matrix = new Matrix4();
        proj_type = PERSPECTIVE;
        zoom = 1.0;
        fovy = (float) (50.0 * Math.PI / 180.0);
        aspect = 1.0f;
        znear = 0.1f;
        zfar = 100.0f;
        name = "camera";
        view_matrix = new Matrix4();
        view_matrix.setIdentity();
        this.setOutput("Screen");
}
  

    /**
     * Set the Camera's perspective
     *
     * @param fovy,aspect, zNear, zFar
     */
    public void setPerspective(float fovy, float aspect, float zNear, float zFar) {
        this.fovy = fovy;
        this.aspect = aspect;
        this.znear = zNear;
        this.zfar = zFar;
        setDirty(Node.PROJMATRIX,true);
    }

    /**
     * Set the Camera's Field of View(fov) for perspective projection
     *
     * @param fovy
     */
    public void setFieldOfView(float fovy) {
        this.fovy = fovy;
        setDirty(Node.PROJMATRIX,true);
    }

    /**
     * Set the Aspect ratio  for perspective projection
     *
     * @param fovy,aspect, zNear, zFar
     */
    public void setAspect(float aspect) {
        this.aspect = aspect;
        setDirty(Node.PROJMATRIX,true);
    }

    /**
     * Set the near and far Z-planes for perspective projection
     *
     * @param fovy,aspect, zNear, zFar
     */
    public void setPlanes(float zNear, float zFar) {
        this.znear = zNear;
        this.zfar = zFar;
        setDirty(Node.PROJMATRIX,true);
    }

    /**
     * Set the stereoscopic
     */
    public void setStereoscopic() {
    }

    /**
     * Set the Camera's type ("Mono" or "Stereo")
     *
     * @param type as String
     */
    public Point2 getViewport() {
        return new Point2(vp_width,vp_height);
    }

    /**
     * Set the Camera's type ("Mono" or "Stereo")
     *
     * @param type as String
     */
    public void setViewport(int width, int height) {
        vp_width = width;
        vp_height = height;
        this.aspect = (float) vp_width / (float) vp_height;
        setDirty(Node.PROJMATRIX,true);
    }

    /**
     * Set the Camera's type ("Mono" or "Stereo")
     *
     * @param type as String
     */
    public void setType(String type) {
        // TO DO
    }

    /**
     * Get the View matrix
     *
     */
    public Matrix4 getViewMatrix() {
        return view_matrix;
    }

    /**
     * Get the  projection matrix
     *
     */
    public Matrix4 getProjection() {
        if (isDirty(Node.PROJMATRIX)) {
            updateProjMatrix();
        }
        return proj_matrix;
    }

    /**
     * Set the Camera's projection
     *
     * @param type as String
     */
    public void setProjection(String type) {
        int index = types.indexOf(name);
        switch (index) {
            case PERSPECTIVE: //TODO
                break;
            case ORTHOGRAPHIC: // TODO
                break;
            default:
                IJ.log("Enter \"Perspective\" or \"Orthographic\"");
        }
    }

    /**
     * Set the Camera's projection
     *
     * @param type as String and parametres as float[]
     */
    public void setProjection(String type, float[] params) {
        // params = fov,aspect,far,near
        // TODO
          int index = types.indexOf(name);
        switch (index) {
            case PERSPECTIVE: 
                fovy   = params[0];
                aspect = params[1];
                znear  = params[2];
                zfar   = params[3];
                break;
            case 1: // TODO
               ortho(params[0],params[1],params[2],params[3],params[4],params[5]);
                break;
            default:
                IJ.showMessage("Vertigo ERROR","Enter \"Perspective\" or \"Orthographic\"");
        }
    }

    /**
     * Set the Camera's type
     *
     * @param type as String parametres as float[]
     */
    public void set(String type, float[] params) {
        // params = eyes distance, convergence
        // TODO
    }

    /**
     * Set the Camera's output "Screen" or "Image" ("Screen" by default)
     *
     * @param type as String
     */
    public void setOutput(String type) {
        // screen or image. Default=screen
        //TODO
        int index = output.indexOf(type);
        switch (index) {
            case 0: //SCREEN TODO 
                break;
            case 1: // IMAGE TODO
                break;
            default:
                IJ.showMessage("Vertigo ERROR","Wrong type");
        }
    }

    public void lookAt(String node_name) {
        Point3 cg;
        if (node_name.equals("origin") )
            cg = new Point3(0.0f,0.0f,0.0f);
        else
            cg = getNode(node_name).getBoundingBox().getCenter();

        Vector3 up = new Vector3(0.0f,1.0f,0.0f);
        Point3 pos = new Point3(0.0f,0.0f,0.0f); // TODO
        look_at(pos,cg,up);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

  @Override
    public void update(Observable o, Object o1) {
        MouseSignal e = (MouseSignal) o1;
        System.out.println("Camera : Zoom" + e);  
        // if Mouse Wheel
        // this.zoom += ZOOM_FACTOR * e.getMouseWheel();
    }


    /**
     *
     * Creates a perspective projection transform that mimics a standard,
     * camera-based, view-model. This transform maps coordinates from Eye
     * Coordinates (EC) to Clipping Coordinates (CC). Note that unlike the
     * similar function in OpenGL, the clipping coordinates generated by the
     * resulting transform are in a right-handed coordinate system (as are all
     * other coordinate systems in Java 3D).
     *
     * The frustum function-call establishes a view model with the eye at the
     * apex of a symmetric view frustum. The arguments define the frustum and
     * its associated perspective projection: (left, bottom, -near) and (right,
     * top, -near) specify the point on the near clipping plane that maps onto
     * the lower-left and upper-right corners of the window respectively,
     * assuming the eye is located at (0, 0, 0).
     *
     * @param left - the vertical line on the left edge of the near clipping
     * plane mapped to the left edge of the graphics window
     * @param right - the vertical line on the right edge of the near clipping
     * plane mapped to the right edge of the graphics window
     * @param bottom - the horizontal line on the bottom edge of the near
     * clipping plane mapped to the bottom edge of the graphics window
     * @param top - the horizontal line on the top edge of the near
     * @param near - the distance to the frustum's near clipping plane. This
     * value must be positive, (the value -near is the location of the near clip
     * plane).
     * @param far - the distance to the frustum's far clipping plane. This value
     * must be positive, and must be greater than near.
     *
     *
     */
    private void frustum(float left, float right, float bottom, float top, float near, float far) {
        float rl = right - left;
        float tb = top - bottom;
        float fn = far - near;

        proj_matrix.m00 = 2.0f * near / rl;
        proj_matrix.m01 = 0.0f;
        proj_matrix.m02 = (right + left) / rl;
        proj_matrix.m03 = 0.0f;

        proj_matrix.m10 = 0.0f;
        proj_matrix.m11 = 2.0f * near / tb;
        proj_matrix.m12 = (top + bottom) / tb;
        proj_matrix.m13 = 0.0f;

        proj_matrix.m20 = 0.0f;
        proj_matrix.m21 = 0.0f;
        proj_matrix.m22 = -(far + near) / fn;
        proj_matrix.m23 = -2.0f * far * near / fn;

        proj_matrix.m30 = 0.0f;
        proj_matrix.m31 = 0.0f;
        proj_matrix.m32 = -1.0f;
        proj_matrix.m33 = 0.0f;


    }

    /**
     * Creates a perspective projection transform that mimics a standard,
     * camera-based, view-model. This transform maps coordinates from Eye
     * Coordinates (EC) to Clipping Coordinates (CC). Note that unlike the
     * similar function in OpenGL, the clipping coordinates generated by the
     * resulting transform are in a right-handed coordinate system (as are all
     * other coordinate systems in Java 3D). Also note that the field of view is
     * specified in radians.
     *
     * @param fovy specifies the field of view in the y direction, in radians
     * (the original vecmath package used fov*X*).
     * @param aspect specifies the aspect ratio and thus the field of view in
     * the x direction. The aspect ratio is the ratio of x to y, or width to
     * height.
     * @param zNear the distance to the frustum's near clipping plane. This
     * value must be positive, (the value -zNear is the location of the near
     * clip plane).
     * @param zFar the distance to the frustum's far clipping plane.
     */
    private void perspective(float fovy, float aspect, float zNear, float zFar) {
        float tan = (float) Math.tan(fovy / 2.0f);
        float top = zNear * tan;
        float right = top * aspect;

        frustum(-right, right, -top, top, zNear, zFar);
    }

    /**
     * Creates an orthographic projection transform that mimics a standard,
     * camera-based, view-model. This transform maps coordinates from Eye
     * Coordinates (EC) to Clipping Coordinates (CC). Note that unlike the
     * similar function in OpenGL, the clipping coordinates generated by the
     * resulting transform are in a right-handed coordinate system (as are all
     * other coordinate systems in Java 3D).
     *
     * @param left - the vertical line on the left edge of the near clipping
     * plane mapped to the left edge of the graphics window
     * @param right - the vertical line on the right edge of the near clipping
     * plane mapped to the right edge of the graphics window
     * @param bottom - the horizontal line on the bottom edge of the near
     * clipping plane mapped to the bottom edge of the graphics window
     * @param top - the horizontal line on the top edge of the near clipping
     * plane mapped to the top edge of the graphics window
     * @param near - the distance to the frustum's near clipping plane. (the
     * value -near is the location of the near clip plane)
     * @param far - the distance to the frustum's far clipping plane.
     */
    private void ortho(float left, float right, float bottom, float top, float near, float far) {
        float rl = right - left;
        float tb = top - bottom;
        float fn = far - near;

        proj_matrix.m00 = 2.0f / rl;
        proj_matrix.m01 = 0.0f;
        proj_matrix.m02 = 0.0f;
        proj_matrix.m03 = -(right + left) / rl;

        proj_matrix.m10 = 0.0f;
        proj_matrix.m11 = 2.0f / tb;
        proj_matrix.m12 = 0.0f;
        proj_matrix.m13 = -(top + bottom) / tb;

        proj_matrix.m20 = 0.0f;
        proj_matrix.m21 = 0.0f;
        proj_matrix.m22 = -2.0f / fn;
        proj_matrix.m23 = -(far + near) / fn;

        proj_matrix.m30 = 0.0f;
        proj_matrix.m31 = 0.0f;
        proj_matrix.m32 = 0.0f;
        proj_matrix.m33 = 1.0f;

    }

    /**
     * Helping function that specifies the position and orientation of a view
     * view_matrix.
     * @param eye - the location of the eye
     * @param center - a point in the virtual world where the eye is looking
     * @param up - an up vector specifying the frustum's up direction
     *
     **/
    private void look_at(Point3 eye, Point3 center, Vector3 up) {
        // find orthogonal 3 unit vectors u, v, n
        //
        // n ... center -> eye
        // u ... x+ (right)
        // v ... y+ (up)

        Vector3 n = new Vector3(eye);  // n = (eye - center)/||n||
        n.sub(center);
        n.normalize();

        Vector3 u = new Vector3(up);  // u = up x n
        u.cross(up,n);
        u.normalize();

        Vector3 v = new Vector3();    // v = n x u
        v.cross(n, u);

        /*
         *       [  u  ]
         *   R = [  v  ]
         *       [  n  ]
         *
         *   M = [                     ]
         *       [    R      - R center]
         *       [                     ]
         *       [ 0  0  0           1 ]
         */
        view_matrix.m00 = u.x;
        view_matrix.m01 = u.y;
        view_matrix.m02 = u.z;
        view_matrix.m03 = -(u.x * eye.x + u.y * eye.y + u.z * eye.z);

        view_matrix.m10 = v.x;
        view_matrix.m11 = v.y;
        view_matrix.m12 = v.z;
        view_matrix.m13 = -(v.x * eye.x + v.y * eye.y + v.z * eye.z);

        view_matrix.m20 = n.x;
        view_matrix.m21 = n.y;
        view_matrix.m22 = n.z;
        view_matrix.m23 = -(n.x * eye.x + n.y * eye.y + n.z * eye.z);

        view_matrix.m30 = 0;
        view_matrix.m31 = 0;
        view_matrix.m32 = 0;
        view_matrix.m33 = 1;
        // view_matrix.resetType();

    }

    private void updateProjMatrix() {
        switch (proj_type) {
            case PERSPECTIVE:
                perspective(fovy * zoom,aspect,znear, zfar); 
                setDirty(Node.PROJMATRIX,false);
                break;
            case ORTHOGRAPHIC: // ortho TODO
                break;
            default:
                IJ.showMessage("Wrong type");
        }

   }

} // End of class Camera
