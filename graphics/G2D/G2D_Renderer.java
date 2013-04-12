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

import vertigo.graphics.Renderer;
import vertigo.scenegraph.Scene;
import vertigo.scenegraph.World;

    /**
     * Renderer using default java.awt.Graphics without graphics hardware acceleration
     *
     * @author Jean-Christophe Taveau
     */
public class G2D_Renderer implements Renderer {


    @Override
    public void createWindow() {

    }

    @Override
    public void init(World _world) {

    }


    @Override
    public void display(){

    }

    @Override
    public void setDimension(int w, int h) {

    }

    @Override
    public void setTitle(String title) {
        window_title=title;
    }

    @Override
    public void setBackgroundColor(int red, int green, int blue) {

    }

}
