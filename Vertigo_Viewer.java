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
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Class Vertigo_Viewer. The main class of Vertigo.
 *
 * @author Florin Buga
 * @author Olivier Catoliquot
 * @author Clement Delestre
 * @version 0.1
 *
 */


public class Vertigo_Viewer implements PlugIn {

    /**
     * The window's title
     *
     * @see Vertigo_Viewer#setTitle(java.lang.String)
     */
    private String title_;
    /**
     * The window's width
     *
     * @see Vertigo_Viewer#setDimension(int, int)
     */
    private int window_width = 512;
    /**
     * The window's height
     *
     * @see Vertigo_Viewer#setDimension(int, int)
     */
    private int window_height = 512;
    /**
     * The window's background red component
     *
     * @see Vertigo_Viewer#setBackgroundColor(int, int, int)
     */
    private int red = 85;
    /**
     * The window's background green component
     *
     * @see Vertigo_Viewer#setBackgroundColor(int, int, int)
     */
    private int green = 85;
    /**
     * The window's background blue component
     *
     * @see Vertigo_Viewer#setBackgroundColor(int, int, int)
     */
    private int blue = 85;
    /**
     * The scene of scenegraph
     *
     * @see Vertigo_Viewer#getScene()
     */
    private Scene scene_;
    /**
     * The camera of scenegraph
     *
     * @see Vertigo_Viewer#getCamera()
     */
    private Camera camera_;
    private Window3D graphWin;
    /**
     * The world of scenegraph
     *
     * @see Vertigo_Viewer#getWorld()
     */
    private World world_;
    /**
     * The vertigo's version
     */
    public static final String VERTIGO_VERSION = "0.01";

    /**
     * Constructor, create a default scenegraph.
     */
    public Vertigo_Viewer() {
        default_scenegraph();
        title_ = " ";
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
* @param a_title a string containing the title
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

    /**
     * Sets the window's dimension
     *     
* @param width
     * @param height
     */
    public void setDimension(int w, int h) {
        window_width = w;
        window_height = h;
    }

    /**
     * Displays the window and triggers the OpenGL rendering in an infinite
     * loop.
     */
    public void show() {
        try {
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
                    IJ.showMessage("Vertigo ERROR", "Please download JOGL or LWJGL.");
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
     * Run the rendering engine for display.
     *
     * @param render : Name of the rendering engine
     */
    public void show(String render) {
        if (render.equals("G2D")) {
            graphWin = (Window3D) new vertigo.graphics.G2D.G2D_Window();
            initWindow();

        } else if (render.equals("LWJGL")) {
            try {
                graphWin = new vertigo.graphics.lwjgl.LWJGL_Window();
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
     * Gets the world (a.k.a. root) of the scene graph.
     *
     * @return world
     */
    public World getWorld() {
        return world_;
    }

    /**
     * Gets the scene of the scene graph. Convenient method equivalent to
     * getWorld().get("Stage").get("Scene");
     * @return scene
     */
    public Scene getScene() {
        return scene_;
    }

    /**
     * Gets the Camera for *this* (default) scene graph. This is a convenient
     * method equivalent to `getScene().getCamera()'.
     * @return camera
     *
     */
    public Camera getCamera() {
        return camera_;
    }

    /**
     * Gets a node of the scene graph.
     *
     * @param name of node
     * @return Node.
     */
    public Node getNode(String name) {
        Node a_node = searchName(world_, name);
        if (a_node == null) {
            IJ.log("The " + name + " node was not found");
        }
        return a_node;
    }


    private Node searchName(Node a_node, String name) {
        if (a_node.getName().equals(name)) {
            return a_node;
        } else {
            for (Node tmp : a_node.getChildren() ) {
                a_node = searchName(tmp, name);
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
