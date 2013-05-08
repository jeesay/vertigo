/*
 * $Id:$
 *
 * crazybio_viewer: 3D Viewer Plugin for ImageJ.
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
 *Florin Buga
 *Olivier Catoliquot
 *Clement Delestre
 */
package vertigo.graphics.jogl;

import java.awt.Graphics;
import java.util.Observer;
import javax.media.opengl.GLAutoDrawable;
import vertigo.graphics.MouseDispatcher;
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

/**
 * @author Florin Buga Olivier Catoliquot Clement Delestre
 */
public class JOGL_Visitor implements Visitor {

    private GLAutoDrawable drawable;
    private int level;
    private Camera cam_;
    private Graphics g;

    void setGLDrawable(GLAutoDrawable drawable) {
        this.drawable = drawable;
    }

    @Override
    public void visit(BackStage obj) {
        //do nothing
    }

    @Override
    public void visit(Camera obj) {
        cam_ = obj;
        setModelMatrix(obj);

        //glGetUniformLocation 
        //GL20.glUniformMatrix4(level, true, view);  
        //location (int) transpose (boolean) matrice (float buffer)

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
        drawShape(obj);
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
        processShape(obj);
    }

    private void processShape(Shape obj) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
} // end of class JOGL_Visitor
