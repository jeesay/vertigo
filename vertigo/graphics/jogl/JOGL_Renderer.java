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

import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import vertigo.scenegraph.Camera;
import vertigo.scenegraph.World;

/**
 *
 * @author Florin Buga Olivier Catoliquot Clement Delestre
 */
public class JOGL_Renderer implements GLEventListener {

    // private GLCanvas canvas;
    private Camera cam;
    private World world;
    private float red;
    private float green;
    private float blue;
    private JOGL_VisitorTwo visitor;

    public JOGL_Renderer() {
        System.out.println("JOGL_Renderer created");
        // visitor = new JOGL_VisitorTwo();
    }

    public void setBackgroundColor(float red, float green, float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public void setWorld(World world) {
        this.world = world;
        this.cam = (Camera) world.getNode("camera");
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        // Do nothing
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        // Do nothing
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL3 gl = drawable.getGL().getGL3();  // up to OpenGL 3
        gl.glClearColor(red, green, blue, 1.0f);
        gl.glClear(GL3.GL_COLOR_BUFFER_BIT | GL3.GL_DEPTH_BUFFER_BIT);


        // Render via Visitor
        visitor = new JOGL_VisitorTwo();
        visitor.setGLDrawable(drawable);
        world.accept(visitor);


    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        // Your OpenGL codes to set up the view port, projection mode and view volume. 
        cam.setViewport(width, height);
//        cam.setAspect(width/height);

    }

    private void update() {
        // nothing to update yet
    }
} //end of class JOGL_Renderer
