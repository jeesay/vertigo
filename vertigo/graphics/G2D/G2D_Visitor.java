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

import java.awt.Graphics;
import vertigo.graphics.Visitor;
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
    private final G2D_Renderer renderer;

    public G2D_Visitor() {
        renderer = new G2D_Renderer();
    }


    @Override
    public void visit(BackStage obj) {
        // Do nothing -the matrix is always identity
        System.out.println("BackStage: " +obj.getName() );
    }

    @Override
    public void visit(Camera obj) {
        // Do nothing ?
        cam_ = obj;
        System.out.println("Camera: " +obj.getName() );
    }

    @Override
    public void visit(Light obj) {
        // Do nothing ?
        System.out.println("Light: " +obj.getName() );
    }

    @Override
    public void visit(Lighting obj) {
        // Do nothing
        System.out.println("Lighting: " +obj.getName() );
    }

    @Override
    public void visit(Scene obj) {
        // Update matrix
        setModelMatrix(obj);
        System.out.println("Scene: " +obj.getName() );
    }

    @Override
    public void visit(Shape obj) {
        System.out.println("Shape: " +obj.getName() +" "+obj);
        // Update matrix
        mulModelMatrix(obj);
        // update AABB
    }

    @Override
    public void visit(Stage obj) {
        // Do nothing
        System.out.println("Stage: " +obj.getName() );
    }

    @Override
    public void visit(Transform obj) {
        mulModelMatrix(obj);
    }

    @Override
    public void visit(Viewing obj) {
        // Do nothing
        System.out.println("Viewing: " +obj.getName() );
    }

    @Override
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

} // End of class G2D_Visitor
