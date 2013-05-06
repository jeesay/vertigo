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

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color.*;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.util.Observer;
import java.util.concurrent.atomic.AtomicReference;
import org.lwjgl.opengl.Display;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import vertigo.graphics.KeyboardDispatcher;
import vertigo.graphics.MouseDispatcher;
import vertigo.graphics.Window3D;
import vertigo.graphics.TimerDispatcher;
import vertigo.graphics.event.KeyboardObserver;
import vertigo.graphics.event.MouseObserver;
import vertigo.graphics.event.TimerObserver;
import vertigo.scenegraph.Node;
import vertigo.scenegraph.World;
import vertigo.graphics.event.KeyboardObserver;
import vertigo.graphics.event.KeyboardSignal;
import vertigo.graphics.event.MouseObserver;
import vertigo.graphics.event.Signal;
import vertigo.graphics.event.MouseSignal;
import vertigo.graphics.event.TimerObserver;
import vertigo.graphics.event.ViewportObserver;
import vertigo.graphics.event.ViewportSignal;

public class LWJGL_Window implements Window3D, MouseWheelListener {

    private int width = 640;
    private int height = 480;
    private String win_title = "Vertigo LWJGL - ";
    private static boolean closeRequested;
    private final static AtomicReference<Dimension> newCanvasSize = new AtomicReference<Dimension>();
    private Frame frame;
    private Dimension newDim;
    private LWJGL_Renderer renderer;
    private final KeyboardDispatcher keyboardDispatcher;
    private final MouseDispatcher mouseDispatcher;
    private final TimerDispatcher timerDispatcher;
    private final ViewportDispatcher vpDispatcher;
    private MouseSignal mouse_event;
    private KeyboardSignal key_event;
    private ViewportSignal vp_event;
    private Signal allevent;
//recup matrice avec le visiteur
    // VBO pour tous les cas (boucle)  une ou deux passes si une marche pas (isDirty), matrice parent x fils
    //public LWJGL_Window(){} 
    //singleton
    private World world;

    public LWJGL_Window() {
        System.out.println("constructor");
        mouseDispatcher = MouseDispatcher.getInstance();
        keyboardDispatcher = KeyboardDispatcher.getInstance();
        timerDispatcher = TimerDispatcher.getInstance();
        vpDispatcher = ViewportDispatcher.getInstance();
        closeRequested = false;
        renderer = new LWJGL_Renderer();
        mouse_event = new MouseSignal();
        key_event = new KeyboardSignal();
        allevent = new Signal();
        //Mouse.setGrabbed(true);

        // loadObserver(); 

    }

    private void createWindow() {
        frame = new Frame(win_title);
        frame.setLayout(new BorderLayout());
        final Canvas canvas = new Canvas();
        System.out.println("createWindow");
        canvas.addMouseWheelListener(this);
        canvas.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                newCanvasSize.set(canvas.getSize());
            }
        });
        System.out.println("add Listener");
        frame.addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                canvas.requestFocusInWindow();
            }
        });

        System.out.println("add focus listener");
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeRequested = true;
            }
        });

        System.out.println("add window listener");
        frame.add(canvas, BorderLayout.CENTER);
        System.out.println("add canvas");
        try {
            Display.setParent(canvas);
            System.out.println("set parent");
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
         * its size. Canvas canvas = new Canvas(); canvas.setSize(width,
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
                //renderer.syncViewportSize(0, 0, newDim.width, newDim.height);
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

    private void displayScene() {

        //System.out.println("Display Scene method.");
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
        win_title += title;
    }

    @Override
    public void setBackgroundColor(int red, int green, int blue) {
        renderer.setBackgroundColor((red / 255.0f), (green / 255.0f), (blue / 255.0f));
    }

    @Override
    public void setWorld(World _world) {
        world = _world;
        renderer.setWorld(_world);
        System.out.println("entrée dans load observer");
        loadObserver(_world);
        System.out.println("sortie de load observer");
    }

    private void pollInput() {



        mouse_event.setWheel((int) Math.signum(Mouse.getDWheel()));
        mouse_event.setButton(Signal.NO_BUTTON);
        mouse_event.setButtonStatus(Signal.NONE);
        mouse_event.setXY(Mouse.getX(), Mouse.getY());
        int x = Mouse.getDX();
        int y = Mouse.getDY();

        if (x != 0 && y != 0) {
            mouse_event.setButtonStatus(Signal.MOVED);
            //System.out.println("dragged " + x + " " + y);
        }

        if (Mouse.isButtonDown(0)) {

            mouse_event.setButton(Signal.BUTTON_LEFT);
            mouse_event.setWheel(0);


            mouseDispatcher.fireUpdate(mouse_event);

            //System.out.println(mouse_event);
        } else if (Mouse.isButtonDown(1)) {
            mouse_event.setButton(Signal.BUTTON_RIGHT);
            mouse_event.setWheel(0);
            //System.out.println(mouse_event);
        } else if (Mouse.isButtonDown(2)) {
            mouse_event.setButton(Signal.BUTTON_MIDDLE);
            mouse_event.setWheel(0);
            System.out.println(mouse_event);
            System.out.println("MIDDLE");
        }



        System.out.println("Grab" + Mouse.isGrabbed());
        int dWheel = Mouse.getDWheel(); //don't work
        System.out.println("The WHEEL : " + dWheel);
        if (dWheel < 0) {
            System.out.println("DOWN");
        } else if (dWheel > 0) {
            System.out.println("UP");
        }



        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            System.out.println("space bar");
        } else if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            Display.destroy();
            frame.dispose();
        } else if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            System.out.println("left shift");
        } else if (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
            System.out.println("right shift");
        } else if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
            System.out.println("Left Control");
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && mouse_event.isButtonDown()) {
            System.out.println("Souris enfoncé et L shift aussi");
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


        if (!mouse_event.isEmpty()) {
            mouseDispatcher.fireUpdate(mouse_event);
            mouse_event.setEmpty();
        }
        if (!key_event.isEmpty()) {
            mouseDispatcher.fireUpdate(key_event);
            key_event.setEmpty();
        }
    }

    @Override
    public void setVisible(boolean flag) {
        System.out.println("set visible");
        if (flag == true) {
            createWindow();
            display();
        }
    }

    private void loadObserver(Node obj) {
        System.out.println("Load Observer observer " + obj);
        if (obj instanceof MouseObserver) {
            mouseDispatcher.addObserver((Observer) obj);
        }
        if (obj instanceof ViewportObserver) {
            viewportDispatcher.addObserver((Observer) obj);
        }
        if (obj instanceof TimerObserver) {
            timerDispatcher.addObserver((Observer) obj);
        }
        if (obj instanceof KeyboardObserver) {
            keyboardDispatcher.addObserver((Observer) obj);
        }
        for (Node child : obj.getChildren()) {
            loadObserver(child);
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent mwe) {
        System.out.println("HERE ************************     1    ");
        if (mwe.getWheelRotation() < 0) {
            System.out.println("HERE ************************     2");
            System.out.println("Rotated Up... " + mwe.getWheelRotation());
            mouse_event.setWheel(Signal.WHEEL_UP);
            mouse_event.setButton(0);
            System.out.println(mouse_event);
        } else {
            System.out.println("Rotated Down... " + mwe.getWheelRotation());
            mouse_event.setWheel(Signal.WHEEL_DOWN);
            mouse_event.setButton(0);
            System.out.println(mouse_event);
            System.out.println("HERE ************************    3");
        }
        mouseDispatcher.fireUpdate(mouse_event);
    }
} // end of class LWJGL_Window
