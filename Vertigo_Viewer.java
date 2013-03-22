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
package vertigo;

import vertigo.scenegraph.Scene;
import vertigo.scenegraph.Camera;
import vertigo.scenegraph.Light;

import ij.IJ;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;
import vertigo.graphics.Renderer;


public class Vertigo_Viewer implements PlugIn {
    private String title_;
    private float red;
    private float green;
    private float blue;
    private Scene scene_;
    private Camera camera_;
    private Renderer renderer;

    public void run (String options) {
test();
    }

    /**
* Sets The title of the Window
*
* @param a_title A string containing the title
*/
    public void setTitle(String a_title) {
        title_ = a_title;
    }

    /**
* Sets the background color
*
* @param red integer value between 0 and 255 for the red component
* @param green integer value between 0 and 255 for the red component
* @param blue integer value between 0 and 255 for the red component
*/
    public void setBackgroundColor(int red, int green, int blue) {
        this.red = red / 255.0f;
        this.green = green / 255.0f;
        this.blue = blue / 255.0f;
    }

    /**
* Displays the window and triggers the OpenGL rendering in an infinite loop.
*/
    public void show() {
        renderer = (Renderer) new vertigo.graphics.jogl.Renderer(getScene());
        renderer.display();
    }

    /**
* Gets the root of the scene graph.
*
*/
    public Scene getScene() {
        return scene_;
    }

    /**
* Gets the Camera for *this* (default) scene graph. This is a convenient method equivalent to `getScene().getCamera()'.
*
*/
    public Camera getCamera() {
        return camera_;
    }

    public static void main(String [] args) {
        System.out.println("main");
        test();
    }


    private static void test () {
        // create a scene, add a light, test getName method
	Scene scene = new Scene();
        Light light=new Light();
        scene.add(light);
        System.out.println("La scène se nomme : "+scene.getName());
        //test traverseUp and traverseDown methods
        scene.traverseUp();
       light.traverseDown();
       
       // add the second light
       Light two=new Light();
       scene.add(two);
       scene.traverseDown();
       // add a cam
       Camera cam = new Camera();
	cam.add(scene);
        cam.traverseUp(); // cam is the new root
      
       
	
	
    }

}// end of class Vertigo_Viewer
