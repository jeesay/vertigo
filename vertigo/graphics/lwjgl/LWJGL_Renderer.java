/*
 * $Id:$
 *
 * Vertigo_viewer: 3D Viewer Plugin for ImageJ.
 * Copyright (C) 2013 Jean-Christophe Taveau.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 *
 * Authors :
 * Florin Buga
 * Olivier Catoliquot
 * Clement Delestre
 */
package vertigo.graphics.lwjgl;

import java.awt.Dimension;
import org.lwjgl.opengl.GL11;
import vertigo.scenegraph.Camera;
import vertigo.scenegraph.World;
import vertigo.graphics.Renderer;

/**
 * Class LWJGL_Renderer
 *
 * @author Florin Buga
 * @author Olivier Catoliquot
 * @author Clement Delestre
 * @version 0.1
 *
 */
public class LWJGL_Renderer implements Renderer {
   /**
     * The window's background red component
     *
     * @see LWJGL_Renderer#setBackgroundColor(float, float, float) 
     */
    private float red;
     /**
     * The window's background green component
     *
     * @see LWJGL_Renderer#setBackgroundColor(float, float, float) 
     */
    private float green;
     /**
     * The window's background blue component
     *
     * @see LWJGL_Renderer#setBackgroundColor(float, float, float) 
     */
    private float blue;
    private World world;
    private Camera cam;
    private Dimension newDim;
    /**
     * The LWJGL_Visitor
     *
     * @see LWJGL_Renderer#display() 
     * @see LWJGL_Visitor
     */
    LWJGL_Visitor LWJGLVisitor;
/**
 * Constructor.
 */
    public LWJGL_Renderer() {
        LWJGLVisitor = new LWJGL_Visitor();
    }

    @Override
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
    public void display() {
        GL11.glClearColor(red, green, blue, 1.0f);
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
        world.accept(LWJGLVisitor);
    }

    void syncViewportSize(int i, int i0, int width, int height) {
        GL11.glViewport(0, 0, newDim.width, newDim.height);
    }
    
    /**
     * Clean memory when the window is closed
     */
    public void dispose() {
        LWJGLVisitor.dispose();
    }
} // end of class LWJGL_Renderer
