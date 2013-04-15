

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

import vertigo.graphics.Renderer;
import vertigo.scenegraph.Scene;
import vertigo.scenegraph.World;


public class JOGL_Renderer implements Renderer { 
    
    private JOGL_Renderer instance;
    
    public JOGL_Renderer() {
        // TODO
    }
    
    public JOGL_Renderer(Scene scene) {
        if (instance==null)
            newInstance(scene);
    }
    
    @Override
    public void setBackgroundColor(int red, int green, int blue) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void setDimension(int w, int h) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setTitle(String title) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void init(World _world) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void createWindow() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    private void newInstance(Scene a_scene) {
        
    }
    //public JOGL_Renderer(){} 
    //singleton
    public void initShader(){
	
    }
    public void initVBO(){
	
    }

    public void display(){

	//boucle for(Shape)

    }
} //end of class JOGL_Renderer
