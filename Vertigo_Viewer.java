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

import vertigo.scenegraph.Scene;
import vertigo.scenegraph.Camera;
import vertigo.scenegraph.Shape;
import vertigo.scenegraph.Light;
import vertigo.scenegraph.World;
import vertigo.scenegraph.Stage;
import vertigo.scenegraph.BackStage;
import vertigo.scenegraph.Node;
import vertigo.scenegraph.Lighting;
import vertigo.scenegraph.Viewing;
import vertigo.scenegraph.transform.ArcBall;
import vertigo.graphics.Window3D;

import ij.IJ;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;
import java.util.ArrayList;
import java.util.Iterator;
import org.lwjgl.LWJGLException;
import vertigo.graphics.lwjgl.Test;
import vertigo.graphics.lwjgl.TheQuadExampleDrawElements;

public class Vertigo_Viewer implements PlugIn {

    private String title_;
    private int window_width;
    private int window_height;
    private int red;
    private int green;
    private int blue;
    private Scene scene_;
    private Camera camera_;
    private Window3D graphWin;
    private World world_;
    public static final String VERTIGO_VERSION = "0.01";

    public Vertigo_Viewer() {
        default_scenegraph();
        window_width = 512;
        window_height = 512;
        title_ = "Vertigo";
    }

    @Override
    public void run(String options) {
        //test();
        IJ.showMessage("About VERTIGO",
                "<html>"
                + "This plugin only works via scripts in JavaScript.<br />"
                + "See the tutorials in http://crazybiocomputing.blogspot.com/p/plugins.html.</html>");
    }

    /**
     * Sets The title of the Window
     *     
* @param  a_title A string containing the title
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
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public void setDimension(int w, int h) {
        window_width = w;
        window_height = h;
    }

    /**
     * Displays the window and triggers the OpenGL rendering in an infinite
     * loop.
     */
    public void show() throws LWJGLException {
        try {
            System.out.println("LWJGL Renderer created");
            graphWin = new vertigo.graphics.lwjgl.LWJGL_Window();
            show("LWJGL");
        } catch (ExceptionInInitializerError e) {
            try {
                graphWin = new vertigo.graphics.jogl.JOGL_Window();
                show("JOGL");
            } catch (ExceptionInInitializerError ei) {
                // try & catch for tests
                try {
                    graphWin = (Window3D) new vertigo.graphics.text.Text_Renderer();
                    show("TEXT");
                } catch (ExceptionInInitializerError eie) {
                    IJ.showMessage("Vertigo ERROR", "<html>Please download JOGL or LWJGL.</html>");
                }
            }
        }
        try {
            graphWin.setBackgroundColor(red, green, blue);
            graphWin.setDimension(window_width, window_height);
            graphWin.setTitle(title_);

            graphWin.setVisible(true);

        } catch (NullPointerException nullp) {
            IJ.showMessage("Vertigo ERROR", "Can't create a graphics window.");
        }


    }

    /**
     * Run the rendering engine for display
     *
     * @param name of the rendering engine
     */
    public void show(String render)  {
        if (render.equals("G2D")) {
            graphWin = (Window3D) new vertigo.graphics.G2D.G2D_Window();
            initWindow();

        } else if (render.equals("LWJGL")) {
            try {
                graphWin = new vertigo.graphics.lwjgl.LWJGL_Window();

                // TheQuadExampleDrawElements tqe= new TheQuadExampleDrawElements();
                // SimpleInterleavedVboExample s = new SimpleInterleavedVboExample();
                //Test t=new Test();
                initWindow();
            } catch (ExceptionInInitializerError e) {
                IJ.showMessage("Vertigo ERROR", "Can't create a graphics window. Please download LWJGL or check your ClassPath.");
            }

        } else if (render.equals("JOGL")) {
            try {

                graphWin = new vertigo.graphics.jogl.JOGL_Window();

                initWindow();
            } catch (ExceptionInInitializerError e) {
                IJ.showMessage("Vertigo ERROR", "Can't create a graphics window. Please download JOGL or check your ClassPath.");

            }
        } else if (render.equals("TEXT")) {
            graphWin = (Window3D) new vertigo.graphics.text.Text_Renderer(camera_, scene_);
            initWindow();

        }
    }

    /**
     * Gets the world (aka root) of the scene graph.
     *
     * @return world
     */
    public World getWorld() {
        return world_;
    }

    /**
     * Gets the scene of the scene graph. Convenient method equivalent to
     * getWorld().get("Stage").get("Scene");
     *
     */
    public Scene getScene() {
        return scene_;
    }

    /**
     * Gets the Camera for *this* (default) scene graph. This is a convenient
     * method equivalent to `getScene().getCamera()'.
     *
     */
    public Camera getCamera() {
        return camera_;
    }

    public static void main(String[] args) throws LWJGLException {
        System.out.println("main");
        Vertigo_Viewer ve = new Vertigo_Viewer();
        ve.setBackgroundColor(20, 200, 80);
        ve.setTitle("Mon titre");
        ve.show("LWJGL");

        //test();
    }

    private static void test() {
        /**
         * *****
         * Scene scene = new Scene(); Light light = new Light();
         * scene.add(light); // test getName and default Name
         * System.out.println("La scène se nomme : " + scene.getName());
         *
         * light.traverseDown();
         *
         * // add the second light Light two = new Light(); scene.add(two);
         *
         * Camera cam = new Camera(); cam.add(scene); Shape first_shape = new
         * Shape(); scene.add(first_shape);
         *
         *
         * Shape parent_shape = new Shape();
         * parent_shape.setName("parent_shape"); Shape son_shape = new Shape();
         * son_shape.setName("son_shape"); Shape daughter_shape = new Shape();
         * daughter_shape.setName("daughter_shape"); // add son & daughter to
         * parent parent_shape.add(son_shape); parent_shape.add(daughter_shape);
         *
         * Shape s = new Shape(); Shape ac = new Shape(); ac.setName("Another
         * child."); s.add(ac); scene.add(s);
         *
         * scene.add(parent_shape); cam.traverseDownT(); // traverseDownT is
         * better than traverseDown
         *
         * // World's test World w = new World(); w.setName("World"); BackStage
         * bs = new BackStage(); bs.setName("BackStage"); Stage stage = new
         * Stage(); stage.setName("Stage");
         *
         * w.add(stage); // Add stage before backstage w.add(bs);
         *
         * //w.traverseDownT(); Node test = w.getChild(0); //get the first
         * child if (test instanceof Stage) { System.out.println("stage"); }
         * else { System.out.println("backstage"); }
         *
         * Viewing viewing = new Viewing(); Lighting lighting = new Lighting();
         * bs.add(lighting); bs.add(viewing); w.traverseDownT(); test =
         * bs.getChild(0);
         *
         * if (test instanceof Viewing) { System.out.println("viewing"); } else
         * { System.out.println("lighting"); }
         *
         *
         * Node newnode = w.getNode("BackStage"); System.out.println("Le nom du
         * newnode est " + newnode.getName()); ****
         */
    }

    public Node getNode(String name) {
        Node a_node = searchName(world_, name);
        if (a_node == null) {
            IJ.log("The " + name + " node was not found");
        }
        return a_node;
    }

    private Node searchName(Node a_node, String name) {
        if (a_node.getName().equals(name)) {
            IJ.log("this is " + a_node.getName());
            return a_node;
        } else {
            ArrayList children = a_node.getChildren();
            for (Iterator<Node> it = children.iterator(); it.hasNext();) {
                Node nodetemp = it.next();
                a_node = searchName(nodetemp, name);
                if (a_node != null) {
                    return a_node;
                }
            }
        }
        return null;
    }

    private void default_scenegraph() {
        world_ = new World();
        camera_ = new Camera();
        scene_ = new Scene();
        BackStage bs = new BackStage();
        Stage stage = new Stage();

        world_.add(stage);

        ArcBall mouseRot = new ArcBall();
        stage.add(mouseRot);
        mouseRot.add(scene_);
        world_.add(bs);
        Viewing vw = new Viewing();
        vw.add(camera_);
        bs.add(vw);
        Lighting lights = new Lighting();
        bs.add(lights);
        lights.add(new Light("sun"));
        lights.add(new Light("spot"));

    }

    private void initWindow() {
        graphWin.setBackgroundColor(red, green, blue);
        graphWin.setDimension(window_width, window_height);
        graphWin.setTitle(title_);
        graphWin.setWorld(getWorld());
        graphWin.setVisible(true);
    }
}// end of class Vertigo_Viewer
