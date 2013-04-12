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
package vertigo.graphics.G2D;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.Color;
import javax.swing.JFrame;

import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.Display;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.PixelFormat;

import vertigo.graphics.Renderer;
import vertigo.scenegraph.Scene;
import vertigo.scenegraph.World;

public class G2D_Renderer implements Renderer {

    private int width = 320;
    private int height = 240;
    private float red;
    private float green;
    private float blue;
    private static final String WIN_TITLE = "Vertigo G2D - ";
    private String window_title = "";
    private World world;
    private JFrame frame;
    private G2D_Panel panel;
       

    /**
     * Constructor
     **/
    public G2D_Renderer() {
        // Do nothing ?
    }

    @Override
    public void createWindow() {
        frame = new JFrame(WIN_TITLE+window_title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        panel = new G2D_Panel();
        panel.setWorld(world);
        panel.setBackground(new Color(red,green,blue));
        frame.setContentPane(panel);
        frame.setVisible(true);
    }

    @Override
    public void init(World world) {
        this.world = world;
    }


    @Override
    public void display(){
        // do nothing
        // display is done in the G2D_Panel
    }

    @Override
    public void setDimension(int w, int h) {
        width=w;
        height=h;
    }

    @Override
    public void setTitle(String title) {
        window_title=title;
    }

    @Override
    public void setBackgroundColor(int red, int green, int blue) {
        this.red = red / 255.0f;
        this.green = green / 255.0f;
        this.blue = blue / 255.0f;
    }

}
