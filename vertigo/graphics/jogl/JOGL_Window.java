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

import javax.media.opengl.GLProfile;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.awt.GLCanvas;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import vertigo.graphics.OGL_Window;
import vertigo.scenegraph.World;

public class JOGL_Window implements OGL_Window {

    private int width = 400;
    private int height = 400;
    private static final String WIN_TITLE = "Vertigo JOGL - ";
    private String win_title;
    private World world;
    private Frame frame;
    private JOGL_Renderer panel;

    public JOGL_Window() {
        panel = new JOGL_Renderer();
    }

  
    @Override
    public void setWorld(World _world) {
        world = _world;
        panel.setWorld(_world);
    }

    @Override
    public void setDimension(int w, int h) {
        width = w;
        height = h;

    }

    @Override
    public void setBackgroundColor(int red, int green, int blue) {
        panel.setBackgroundColor((red / 255.0f), (green / 255.0f), (blue / 255.0f));
    }

    @Override
    public void setTitle(String title) {
        win_title = title;
    }

    @Override
    public void setVisible(boolean flag) {
        if (flag == true) {
            createWindow();
        }
    }

    private void createWindow() {
        frame = new Frame();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();  // Ferme l'application si on clic sur la croix en haut à droite
            }
        });

        // Set the title
        frame.setTitle(WIN_TITLE + win_title);
        //Set the size
        frame.setSize(width, height);

        GLProfile glp = GLProfile.getDefault();
        GLProfile.initSingleton();
        GLCapabilities caps = new GLCapabilities(glp);
        GLCanvas canvas = new GLCanvas(caps);
        canvas.addGLEventListener(panel);

        frame.add(canvas);
        frame.setVisible(true); // Make the frame visible
    }
} //end of class JOGL_Window

