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
package vertigo.graphics.text;

import ij.IJ;
import vertigo.graphics.Renderer;
import vertigo.scenegraph.Camera;
import vertigo.scenegraph.Node;
import vertigo.scenegraph.Scene;
import vertigo.scenegraph.World;

/**
 * Text Renderer used for debugging
 *
 * @author Florin Buga, Olivier Catoliquot, Clement Delestre
 *
 *
 */
public class Text_Renderer implements Renderer {

    public static int handle = 99;
    private Scene scene = new Scene();
    private Camera camera = new Camera();
    private World world = new World();
    private String title;
    private int window_width;
    private int window_height;

    public Text_Renderer(Camera cam, Scene scene) {
        this.scene = scene;
        this.camera = cam;
    }

    public Text_Renderer() {
        IJ.log("Renderer: TEXT");
    }
    public int glGenBufffer(int[] vbo){
            handle++;
            return handle;
    }
    @Override
    public void display() {
        world.accept(new PrintVisitor());
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
            processNode(child);
        }
    }

    private void updateMatrix(Node n) {
        n.getModelMatrix().mul(n.getParent().getModelMatrix());
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

    @Override
    public void setDimension(int w, int h) {
        window_width = w;
        window_height = h;
        //Do Nothing
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
        //Do Nothing
    }

    @Override
    public void setBackgroundColor(int red, int green, int blue) {
        //Do Nothing
    }

    @Override
    public void createWindow() {
        IJ.log("Window: " + title + " [" + window_width + "," + window_height + "]");

    }

    @Override
    public void init(World world) {
        this.world = world;
        
    }
} // End of class Text_Renderer
