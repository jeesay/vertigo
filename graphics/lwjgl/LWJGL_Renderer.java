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
import vertigo.scenegraph.Scene;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.Display;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.PixelFormat;
import vertigo.graphics.Renderer;


public class LWJGL_Renderer implements Renderer  {
    private int width = 320;
    private int height = 240;
    private float red;
    private float green;
    private float blue;
    private String window_title = "Vertigo lwjgl";
       
    //public LWJGL_Renderer(){} 
    //singleton
    public LWJGL_Renderer() {
        this.display();
        while (!Display.isCloseRequested()) {	
			// Force a maximum FPS of about 60
			Display.sync(60);
			// Let the CPU synchronize with the GPU if GPU is tagging behind
			Display.update();
		}
    }
    public void initShader(){
	
    }
    public void initVBO(){
	
    }

    @Override
    public void display(){
        try {
            Display.setDisplayMode(new DisplayMode(width,height));
            Display.setTitle(window_title);
            Display.setInitialBackground (red,green,blue);
            Display.create();
            
        } catch (LWJGLException e){
            e.printStackTrace();
            System.exit(0);
        }
        while (!Display.isCloseRequested()){
            Display.update();
            pollInput();
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
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
	    Display.destroy();
            
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
