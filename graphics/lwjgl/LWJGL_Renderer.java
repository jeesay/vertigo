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
package vertigo.graphics.lwjgl;

import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.Display;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.LWJGLException;
import vertigo.graphics.Renderer;


class LWJGL_Renderer implements Renderer  { 

    //public LWJGL_Renderer(){} 
    //singleton
    public void initShader(){
	
    }
    public void initVBO(){
	
    }

    public void display(){
        try {
            Display.setDisplayMode(new DisplayMode(400,300));
            Display.create();
        } catch (LWJGLException e){
            e.printStackTrace();
            System.exit(0);
        }
        while (!Display.isCloseRequested()){
            Display.update();
        }
        Display.destroy();
    }
     public void pollInput() {
		
        if (Mouse.isButtonDown(0)) {
	    int x = Mouse.getX();
	    int y = Mouse.getY();
			
	    System.out.println("CLICK SOURIS @ X: " + x + " Y: " + y);
	}
		
	if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
	    System.out.println("BARRE D'ESPACE EST ENFONCE");
	}
		
	while (Keyboard.next()) {
	    if (Keyboard.getEventKeyState()) {
	        if (Keyboard.getEventKey() == Keyboard.KEY_A) {
		    System.out.println("Q Key Pressed");
		}
                
	    } else {
	        if (Keyboard.getEventKey() == Keyboard.KEY_A) {
		    System.out.println("Q Key Released");
	        }
	    }
	}
    }
   	//boucle for(Shape)
}
