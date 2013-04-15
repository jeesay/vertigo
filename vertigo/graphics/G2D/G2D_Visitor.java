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

import ij.IJ;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.nio.FloatBuffer;
import vertigo.graphics.IBO;
import vertigo.graphics.VBO;
import vertigo.graphics.Visitor;
import vertigo.math.Matrix4;
import vertigo.math.Point3;
import vertigo.scenegraph.BackStage;
import vertigo.scenegraph.Camera;
import vertigo.scenegraph.Light;
import vertigo.scenegraph.Lighting;
import vertigo.scenegraph.Node;
import vertigo.scenegraph.Scene;
import vertigo.scenegraph.Shape;
import vertigo.scenegraph.Stage;
import vertigo.scenegraph.Transform;
import vertigo.scenegraph.Viewing;
import vertigo.scenegraph.World;

public class G2D_Visitor implements Visitor {

    private int level;
    private Camera cam_;
    private Graphics g;

    void setGraphicsContext(Graphics g) {
        this.g = g;
    }

    public void visit(BackStage obj) {
        // Do nothing -the matrix is always identity
        System.out.println("BackStage: " +obj.getName() );
    }

    public void visit(Camera obj) {
        // Do nothing ?
        cam_ = obj;
        System.out.println("Camera: " +obj.getName() );
    }

    public void visit(Light obj) {
        // Do nothing ?
        System.out.println("Light: " +obj.getName() );
    }

    public void visit(Lighting obj) {
        // Do nothing
        System.out.println("Lighting: " +obj.getName() );
    }

    public void visit(Scene obj) {
        // Update matrix
        setModelMatrix(obj);
        System.out.println("Scene: " +obj.getName() );
    }

    public void visit(Shape obj) {
        System.out.println("Shape: " +obj.getName() +" "+obj);
        // Update matrix
        mulModelMatrix(obj);
        // Draw 
        if (obj.getDrawingStyle().equals("POINTS") ) {
            drawPoints(obj);
        }
        else if (obj.getDrawingStyle().equals("LINES") ) {
            drawLines(obj);
        }
        else if (obj.getDrawingStyle().equals("LINESTRIP") ) {
            drawLineStrips(obj);
        }
        else if (obj.getDrawingStyle().equals("TRIANGLES") ) {
            drawTriangles(obj);
        }
    }

    public void visit(Stage obj) {
        // Do nothing
        System.out.println("Stage: " +obj.getName() );
    }

    public void visit(Transform obj) {
        mulModelMatrix(obj);
    }

    public void visit(Viewing obj) {
        // Do nothing
        System.out.println("Viewing: " +obj.getName() );
    }

    public void visit(World obj) {
        // Do nothing
        System.out.println("World: " +obj.getName() );
    }


    private void setModelMatrix(Node obj) {
        if (obj.isDirty(Node.MATRIX)) {
            obj.setModelMatrix(obj.getParent().getModelMatrix() );
            obj.setDirty(Node.MATRIX,true);
        }
    }

    private void mulModelMatrix(Node obj) {
        if (obj.isDirty(Node.MATRIX)) {
            obj.setModelMatrix(obj.getParent().getModelMatrix() );
            obj.setDirty(Node.MATRIX,true);
        }
    }

    private void drawPoints(Shape obj) {
        System.out.println("draw points: " +cam_.getProjection() );
        Matrix4 mvp = new Matrix4();
        mvp.mul(cam_.getProjection(), cam_.getViewMatrix());
        mvp.mul(obj.getModelMatrix() );
        Point3 point = new Point3();
//        Point point2d = new Point();

        VBO vbo = (VBO) obj.getGeometry().getBO(0);
        FloatBuffer data = vbo.getFloatBuffer();
        for (int i=vbo.getOffset("V3F"); i< vbo.capacity();i+=vbo.getStride("V3F")) {
            System.out.println(vbo.getFloatBuffer().get(i)+"; "+vbo.getFloatBuffer().get(i+1)+"; "+vbo.getFloatBuffer().get(i+2));
            point.set(vbo.getFloatBuffer().get(i),vbo.getFloatBuffer().get(i+1),vbo.getFloatBuffer().get(i+2) +5.0f);
            mvp.transform(point);
            float x = point.x / point.z * cam_.getViewport().x/2.0f + cam_.getViewport().x/2.0f;
            float y = point.y / point.z * cam_.getViewport().y/2.0f + cam_.getViewport().y/2.0f;
            System.out.println("TRNSF "+point.x+"; "+point.y+"; "+point.z+"--> PROJ: "+x+" "+y);
            g.setColor(new Color(1.0f,0.5f,0.2f) );
            g.fillOval(Math.round(x),Math.round(y),5,5 );
        }
    }

    private void drawLines(Shape obj) {
        Matrix4 mvp = new Matrix4();
        mvp.mul(cam_.getProjection(), cam_.getViewMatrix());
        mvp.mul(obj.getModelMatrix() );
        Point3 point = new Point3();

        VBO vbo = (VBO) obj.getGeometry().getBO(0);
        int[] points = new int[vbo.capacity()];
        int count=0;
        FloatBuffer data = vbo.getFloatBuffer();
        for (int i=vbo.getOffset("V3F"); i< vbo.capacity();i+=vbo.getStride("V3F")) {
            point.set(vbo.getFloatBuffer().get(i),vbo.getFloatBuffer().get(i+1),vbo.getFloatBuffer().get(i+2) +5.0f);
            mvp.transform(point);
            points[count++] = Math.round( point.x / point.z * cam_.getViewport().x/2.0f + cam_.getViewport().x/2.0f );
            points[count++] = Math.round( point.y / point.z * cam_.getViewport().y/2.0f + cam_.getViewport().y/2.0f );
        }

        // Indices
        IBO ibo = (IBO) obj.getGeometry().getBO(1);
        g.setColor(new Color(1.0f,0.5f,0.2f) );
        for (int i=0; i< ibo.getIntBuffer().capacity()-1;i++) {
            int j = ibo.getIntBuffer().get(i);
            int k = ibo.getIntBuffer().get(i+1);
            g.drawLine(points[j*2],points[j*2+1],points[k*2],points[k*2+1] );
        }
    }

    private void drawIndexedLines(Shape obj) {
/**
        Matrix4 mvp = obj.getModelMatrix() cam.getViewMatrix() cam_.getProjection();
        for (int i=0; i< obj.getGeometry().;i++) {
            transform(mvp);
            g2d.drawLine(x0,y0,x1,y1);
        }
        for (int i=0;i<indices;i+=2) {
            g2d*.drawLine()
**/
    }

    private void drawLineStrips(Shape obj) {}

    private void drawTriangles(Shape obj) {}
}

