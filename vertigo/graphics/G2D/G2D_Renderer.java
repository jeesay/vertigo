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

package vertigo.graphics.G2D;

import java.awt.Color;
import java.awt.Graphics;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import vertigo.graphics.BO;
import vertigo.graphics.IBO;
import vertigo.graphics.VBO;
import vertigo.math.Matrix4;
import vertigo.math.Point4;
import vertigo.math.Vector3;
import vertigo.scenegraph.Camera;
import vertigo.scenegraph.Node;
import vertigo.scenegraph.Shape;
import vertigo.scenegraph.World;

/**
 *
 * @author tomo
 */
public class G2D_Renderer {

    private ArrayList<Node> drawables;
    private Camera cam_;
    private Graphics g;
     
    public G2D_Renderer() {
         drawables = new ArrayList<Node>();
    }

    public void setWorld(World world) {
       loadDrawables(world); 
    }
    
    public void setGraphicsContext(Graphics g) {
        this.g = g;
    }

    public void draw() {
        for (Node shape : drawables) {
            drawShape((Shape) shape);
        }
    }
    
    private void drawShape(Shape obj) {
        // Matrix 
        Matrix4 mvp = new Matrix4();
        mvp.mul(cam_.getProjection(), cam_.getViewMatrix() );
        mvp.mul(obj.getModelMatrix());

        // Draw
        if (obj.getDrawingStyle().equals("POINTS")) {
            drawPoints(obj,mvp);
        } else if (obj.getDrawingStyle().equals("LINES")) {
            drawLines(obj,mvp);
        } else if (obj.getDrawingStyle().equals("LINE_STRIP")) {
            drawLineStrips(obj,mvp);
        } else if (obj.getDrawingStyle().equals("TRIANGLES")) {
            drawTriangles(obj,mvp);
        } else if (obj.getDrawingStyle().equals("TRIANGLE_STRIP")) {
            drawTriangleStrips(obj,mvp);
        }
    }

    private void drawPoints(Shape obj, Matrix4 mvp) {
        Point4 point = new Point4();
        float halfWidth  = cam_.getViewport().x / 2.0f;
        float halfHeight = cam_.getViewport().y / 2.0f;

        VBO vbo = (VBO) obj.getGeometry().getBO(0);
        FloatBuffer data = vbo.getFloatBuffer();
        int[] points = new int[vbo.capacity()/3 * 2];
        int count = 0;
        for (int i = vbo.getOffset(); i < vbo.capacity(); i += vbo.getStride()) {
            point.set(vbo.getFloatBuffer().get(i), vbo.getFloatBuffer().get(i + 1), vbo.getFloatBuffer().get(i + 2),1.0f);
            mvp.transform(point);
            points[count++] = Math.round(point.x / point.w * halfWidth  + halfWidth);
            points[count++] = Math.round(point.y / point.w * halfHeight + halfHeight);
            // points[count++] = Math.round((1.0f - point.y) / point.w * halfHeight + halfHeight);
        }

        g.setColor(new Color(1.0f, 0.5f, 0.2f));
        for (int i = 0; i < count - 1; i+=2) {
            g.fillOval(points[i], points[i+1], 5, 5);
        }
    }

    private void drawLineStrips(Shape obj, Matrix4 mvp) {
        Point4 point = new Point4();
        float halfWidth  = cam_.getViewport().x / 2.0f;
        float halfHeight = cam_.getViewport().y / 2.0f;
        int[] points = null;
        IBO ibo = null;
        int count = 0;

        for (BO bo : obj.getGeometry().getAllBO() ) {
            if (bo instanceof VBO) {
                VBO vbo = (VBO) bo;
                if (vbo.getType().equals("V3F") ) {
                    points = new int[vbo.capacity()/vbo.getStride() * 2];
                    FloatBuffer data = vbo.getFloatBuffer();
                    for (int i = vbo.getOffset(); i < vbo.capacity(); i += vbo.getStride()) {
                        point.set(vbo.getFloatBuffer().get(i), vbo.getFloatBuffer().get(i + 1), vbo.getFloatBuffer().get(i + 2),1.0f);
                        mvp.transform(point);
                        points[count++] = Math.round(point.x / point.w * halfWidth  + halfWidth);
                        // points[count++] = Math.round(point.y / point.w * halfHeight + halfHeight);
                        points[count++] = Math.round( (1.0f - point.y) / point.w * halfHeight + halfHeight);
                    }
                }
            }
            else { // IBO
                ibo = (IBO) bo;
            }
        }

        // Draw
        int[] col = obj.getMaterial().getColor();
        g.setColor(new Color(col[0]/255.0f, col[1]/255.0f, col[2]/255.0f));

        if (ibo != null) { // Indexed
            for (int i = 0; i < ibo.getIntBuffer().capacity() - 1; i++) {
                int j = ibo.getIntBuffer().get(i);
                int k = ibo.getIntBuffer().get(i + 1);
                g.drawLine(points[j * 2], points[j * 2 + 1], points[k * 2], points[k * 2 + 1]);
            }
        }
        else {
            for (int i = 0; i < count - 2; i+=2)
                g.drawLine(points[i], points[i + 1], points[i + 2], points[i + 3]);
        }

    }

    private void drawLines(Shape obj, Matrix4 mvp) {
    }

    private void drawTriangles(Shape obj, Matrix4 mvp) {
    }

    private void drawTriangleStrips(Shape obj, Matrix4 mvp) {
        drawLineStrips(obj, mvp);
    }


    private void loadDrawables(Node obj) {
        if (obj instanceof Camera) {
        System.out.println("G2D_Rend: Load camera " + obj);

            cam_ = (Camera) obj;
        } else if (obj instanceof Shape) {
        System.out.println("G2D_Rend: Load drawables " + obj);
            drawables.add((Shape) obj);
        }
        for (Node child : obj.getChildren()) {
            loadDrawables(child);
        }
    }

} // End of class G2D_Renderer

