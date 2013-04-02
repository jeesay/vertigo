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
import vertigo.math.Matrix4;
import ij.IJ;

public class Camera extends Node {

    private Matrix4 proj_matrix;
private static List<String> output = Arrays.asList("Screen","Image");
    public Camera() {
        super();
        proj_matrix = new Matrix4();
        name = "Camera";
        this.setOutput("Screen");
    }

    public void setParent(Node anode) {
        System.out.println("Scene must not have any parents...");
    }

    public void setPerspective(float fovy, float aspect, float zNear, float zFar) {
    }

    public void setStereoscopic() {
    }

    public void setType(String type) {
        // TO DO
    }

    public void set(String type, float[] params) {
        //params, float or int ?
        // params = eyes distance, convergence
        // TODO
    }

    public void setOutput(String type) {
        // screen or image. Default=screen
        //TODO
        int index = output.indexOf(type);
        switch (index) {
            case 0: //SCREEN TODO 
                break;
            case 1 : // IMAGE TODO
                break;
            default :
                IJ.log("Valeur incorrecte. Veuillez entrer Screen ou Image.");
        }
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
    public void frustum(float left, float right, float bottom, float top, float near, float far) {
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
    public void perspective(float fovy, float aspect, float zNear, float zFar) {
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
    public void ortho(float left, float right, float bottom, float top, float near, float far) {
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
} // End of class Camera
