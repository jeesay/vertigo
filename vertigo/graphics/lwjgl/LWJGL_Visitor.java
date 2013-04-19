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
package vertigo.graphics.lwjgl;

import java.awt.Graphics;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import vertigo.graphics.BO;
import vertigo.graphics.BufferTools;
import vertigo.graphics.IBO;
import vertigo.graphics.Visitor;
import vertigo.scenegraph.BackStage;
import vertigo.scenegraph.Camera;
import vertigo.scenegraph.Geometry;
import vertigo.scenegraph.Light;
import vertigo.scenegraph.Lighting;
import vertigo.scenegraph.Node;
import vertigo.scenegraph.Scene;
import vertigo.scenegraph.Shape;
import vertigo.scenegraph.Stage;
import vertigo.scenegraph.Transform;
import vertigo.scenegraph.Viewing;
import vertigo.scenegraph.World;

/**
 *
 * @author Florin Buga Olivier Catoliquot Clement Delestre
 */
public class LWJGL_Visitor implements Visitor {

    private int level;
    private Camera cam_;
    private Graphics g;

    @Override
    public void visit(BackStage obj) {
        //do nothing
    }

    @Override
    public void visit(Camera obj) {
        cam_ = obj;
        setModelMatrix(obj);
    }

    @Override
    public void visit(Light obj) {
        setModelMatrix(obj);
    }

    @Override
    public void visit(Lighting obj) {
        //do nothing
    }

    @Override
    public void visit(Scene obj) {
        setModelMatrix(obj);
    }

    @Override
    public void visit(Shape obj) {
        mulModelMatrix(obj);
    }

    @Override
    public void visit(Stage obj) {
        //do nothing
    }

    @Override
    public void visit(Transform obj) {
        setModelMatrix(obj);
    }

    @Override
    public void visit(Viewing obj) {
        //do nothing
    }

    @Override
    public void visit(World obj) {
        //do nothing
    }

    private void setModelMatrix(Node obj) {
        if (obj.isDirty(Node.MATRIX)) {
            obj.setModelMatrix(obj.getParent().getModelMatrix());
            obj.setDirty(Node.MATRIX, true);
        }
    }

    private void mulModelMatrix(Node obj) {
        if (obj.isDirty(Node.MATRIX)) {
            obj.getModelMatrix().mul(obj.getParent().getModelMatrix());
            obj.setDirty(Node.MATRIX, true);
        }
    }

    private void drawShape(Shape obj) {
        // PreProcessing
        processShape(obj);
        // OpenGL

    }

    private void processShape(Shape obj) {
        // PreProcessing
        if (obj.isDirty(Node.MATRIX)) {
            obj.getModelMatrix().mul(obj.getParent().getModelMatrix());
            obj.setDirty(Node.MATRIX, false);
        }
        // Geometry: VBO
        if (obj.isDirty(Node.VBO)) {
            processVBO(obj);
            obj.setDirty(Node.VBO, false);
        }
        // Material: shader
        if (obj.isDirty(Node.SHADER)) {
            obj.setDirty(Node.SHADER, false);
        }
    }

    private void processVBO(Shape obj) {
        int count = obj.getGeometry().getBuffers().size();
        IntBuffer  bufferid = BufferTools.newIntBuffer(count);
        GL15.glGenBuffers(bufferid);
        for (BO bo : obj.getGeometry().getBuffers()) {
            // bo.setHandle();
            // glBindBuffer();
            if (bo instanceof IBO) {
                // glBufferData();
            } else {
                // glBufferData();
            }
            // glBindBuffer(0);
        }

    }
} // End of class LWJGL_Visitor
