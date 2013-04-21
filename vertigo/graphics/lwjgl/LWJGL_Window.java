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

import java.awt.*;
import java.awt.Color.*;
import java.awt.event.*;
import java.util.concurrent.atomic.AtomicReference;
import org.lwjgl.opengl.Display;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL11;
import vertigo.graphics.OGL_Window;
import vertigo.scenegraph.World;

public class LWJGL_Window implements OGL_Window {

    private int width = 640;
    private int height = 480;
    private String title_ = "Vertigo LWJGL - ";
    private static boolean closeRequested;
    private final static AtomicReference<Dimension> newCanvasSize = new AtomicReference<Dimension>();
    private Frame frame;
    private Dimension newDim;
    private LWJGL_Renderer renderer;
    private String win_title="";
//recup matrice avec le visiteur
    // VBO pour tous les cas (boucle)  une ou deux passes si une marche pas (isDirty), matrice parent x fils
    //public LWJGL_Window(){} 
    //singleton
    public LWJGL_Window() {
        System.out.println("constructor");
        closeRequested = false;
        renderer=new LWJGL_Renderer();
        //createWindow();
        //display();
    }

    public static void main(String args[]) {
        LWJGL_Window lwjgl_Window = new LWJGL_Window();
    }

        private void createWindow() {
        frame = new Frame(title_+win_title);
        frame.setLayout(new BorderLayout());
        final Canvas canvas = new Canvas();
        //canvas.add(renderer);
        System.out.println("createWindow");
 
    canvas.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                newCanvasSize.set(canvas.getSize());
            }
        });

        frame.addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                canvas.requestFocusInWindow();
            }
        });


        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
              closeRequested = true;
            }
        });


        frame.add(canvas, BorderLayout.CENTER);

        try {
            Display.setParent(canvas);
            Display.setVSyncEnabled(true);
            frame.setPreferredSize(new Dimension(width, height));
            frame.pack();
            frame.setVisible(true);
            Display.create();

        } catch (LWJGLException e) {
            //e.printStackTrace();
            frame.dispose();            
            System.out.println("ERROR HERE.");
        }
        /**
         * **
         * // JButton button = new JButton("Exit"); // Create a new canvas and
         *   its size. Canvas canvas = new Canvas(); canvas.setSize(width,
         * height); // The setParent method attaches the // opengl window to the
         * awt canvas. try { Display.setParent(canvas); } catch (Exception e) {
         * e.printStackTrace(); System.exit(0); } frame.setBackground(new
         * Color(red,green,blue)); // Construct the GUI as normal
         * //frame.add(button, BorderLayout.NORTH); frame.add(canvas,
         * BorderLayout.CENTER); frame.pack(); frame.setVisible(true);
         *
         */
    }

    public void initShader() {
    }

    public void initVBO() {
    }

    public void display() {
System.out.println("Display  method.");
        // Make sure you run the game, which
        // executes on a separate thread.
        while (!Display.isCloseRequested() && !closeRequested) {
            newDim = newCanvasSize.getAndSet(null);

            if (newDim != null) {
                GL11.glViewport(0, 0, newDim.width, newDim.height);
                //renderer.syncViewportSize();
            }
            Display.sync(60);
            pollInput();
            displayScene();
            Display.update();
        }
        System.out.println("exit");
        Display.destroy();        
        frame.dispose();
        //process dirty
        // display scenegraph
        //camera
        //scene nodes
         //boucle for(Shape)
    }
    
    public void dispose() {
        // Do nothing
    }

    private void displayScene() {/*
        GL11.glClearColor(red, green, blue, 1.0f);
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);*/
        System.out.println("Display Scene method.");
        renderer.display();

    }

   
    @Override
    public void setDimension(int w, int h) {
        System.out.println("setDimension");
        width = w;
        height = h;
    }

    @Override
    public void setTitle(String title) {

        win_title = title;
    }

    @Override
    public void setBackgroundColor(int red, int green, int blue) {
        renderer.setBackgroundColor((red / 255.0f), (green / 255.0f), (blue / 255.0f));
    }

    @Override
    public void setWorld(World _world) {
    }

    private void pollInput() {

        if (Mouse.isButtonDown(0)) {
            int x = Mouse.getX();
            int y = Mouse.getY();

            System.out.println("CLICK @ X: " + x + " Y: " + y);
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            System.out.println("space bar");
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            Display.destroy();
        }

        while (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                if (Keyboard.getEventKey() == Keyboard.KEY_A) {
                    System.out.println("A Key Pressed");
                }

            } else {
                if (Keyboard.getEventKey() == Keyboard.KEY_A) {
                    System.out.println("A Key Released");
                }
            }
        }
    }

 
        @Override
    public void setVisible(boolean flag) {
        if (flag == true) {
            createWindow();
        }
    }
} // end of class LWJGL_Window