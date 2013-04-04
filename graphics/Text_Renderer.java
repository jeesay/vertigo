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
package vertigo.graphics;

import vertigo.scenegraph.Camera;
import vertigo.scenegraph.Node;
import vertigo.scenegraph.Scene;

/**
 * Text Renderer used for debugging
 * 
 * @author tomo
 */
public class Text_Renderer implements Renderer {

    private final Scene scene;
    private final Camera camera;

    public Text_Renderer(Camera cam, Scene scene) {
        this.scene = scene;
        this.camera = cam;
    }

    @Override
    public void display() {
        processNode(scene);
    }

    private void processNode(Node n) {
        // process
        if (n.isDirty(Node.MATRIX)) {
            updateMatrix(n);
        }
        if (n.isDirty(Node.AABB)) {
            //Update AABB
        }
        if (n.isDrawable() && n.isDirty(Node.VBO)) {
            // Update VBO
        }

        if (n.isDrawable() && n.isDirty(Node.SHADER)) {
            // Update Shader
        }

        // Draw

        if (n.isDrawable()) {
            displayNode(n);
        }

        for (Node child : n.getChildren()) {
            processNode(n);
        }
    }

    private void updateMatrix(Node n) {
        n.getMatrix().mul(n.getParent().getModelMatrix());
        n.setDirty(Node.MATRIX, false);
    }

    private void updateAABB(Node n) {
        n.updateBoundingBox();
    }

    private void updateVBO(Node n) {
        // OpenGL get VBO handles
    }

    private void displayNode(Node n) {
        // OpenGL
        // Update matrices
        // Activate Shader, VBOs, etc.
        // Draw stuff
    }
} // End of class Text_Renderer