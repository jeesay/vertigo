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
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.*;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.Display;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL11;
import vertigo.graphics.Renderer;
import vertigo.scenegraph.World;

public class LWJGL_Renderer implements Renderer {

    private int width = 640;
    private int height = 480;
    private float red;
    private float green;
    private float blue;
    private String title_ = "Vertigo LWJGL - ";
    private static boolean closeRequested;
    private final static AtomicReference<Dimension> newCanvasSize = new AtomicReference<Dimension>();
    Frame frame;
    Dimension newDim;
//recup matrice avec le visiteur
    // VBO pour tous les cas (boucle)  une ou deux passes si une marche pas (isDirty), matrice parent x fils
    //public LWJGL_Renderer(){} 
    //singleton
    public LWJGL_Renderer() {
        System.out.println("constructor");
        closeRequested = false;
        //createWindow();
        //display();
    }

    public static void main(String args[]) {
        new LWJGL_Renderer();
    }

    @Override
    public void createWindow() {
        frame = new Frame(title_);
        frame.setLayout(new BorderLayout());
        final Canvas canvas = new Canvas();

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

    /*    frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeRequested = true;
            }
        });*/

        frame.addWindowListener(new WindowAdapter() {
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
            e.printStackTrace();
        }
        /**
         * **
         * // JButton button = new JButton("Exit"); // Create a new canvas and
         * set its size. Canvas canvas = new Canvas(); canvas.setSize(width,
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

    @Override
    public void display() {

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
    }

    private void displayScene() {
        GL11.glClearColor(red, green, blue, 1.0f);
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);

    }

    //boucle for(Shape)
    @Override
    public void setDimension(int w, int h) {
        width = w;
        height = h;
    }

    @Override
    public void setTitle(String title) {
        title_ = title;
    }

    @Override
    public void setBackgroundColor(int red, int green, int blue) {
        this.red = red / 255.0f;
        this.green = green / 255.0f;
        this.blue = blue / 255.0f;
    }

    @Override
    public void init(World _world) {
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
}