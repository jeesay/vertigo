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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.util.Observer;
import java.util.concurrent.atomic.AtomicReference;
import org.lwjgl.opengl.Display;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL11;
import vertigo.graphics.KeyboardDispatcher;
import vertigo.graphics.MouseDispatcher;
import vertigo.graphics.Window3D;
import vertigo.graphics.TimerDispatcher;
import vertigo.graphics.ViewportDispatcher;
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
/**
 * LWJGL_Window. 
 *
 * @author Florin Buga
 * @author Olivier Catoliquot
 * @author Clement Delestre
 * @version 0.1
 * @see Window3D
 *
 */


public class LWJGL_Window implements Window3D {
       /**
     * The window's width.
     *
     * @see LWJGL_Window#setDimension(int, int) 
     */

    private int width = 640;
      /**
     * The window's height.
     *
     * @see LWJGL_Window#setDimension(int, int) 
     */
    private int height = 480;
        /**
     * The window's title.
     *
     * @see LWJGL_Window#setTitle(java.lang.String) 
     */
    private String win_title = "Vertigo LWJGL - ";
    /**
     * Check if the window is closed or not.
     */
    private static boolean closeRequested;
    private final static AtomicReference<Dimension> newCanvasSize = new AtomicReference<Dimension>();
    private Frame frame;
    private Dimension newDim;
    /**
     * The LWJGL Renderer
     * @see LWJGL_Window#display() 
     */
    private LWJGL_Renderer renderer;
    /**
     * The KeyboardDispatcher.
     * @see KeyboardDispatcher
     */
    private final KeyboardDispatcher keyboardDispatcher;
    /**
     * The MouseDispatcher.
     * @see MouseDispatcher
     */
    private final MouseDispatcher mouseDispatcher;
    /**
     * The TimerDispatcher.
     * @see  TimerDispatcher
     */
    private final TimerDispatcher timerDispatcher;
    /**
     * The ViewportDispatcher.
     * @see TimerDispatcher
     */
    private final ViewportDispatcher vpDispatcher;
    /**
     * The MouseSignal.
     * @see ViewportDispatcher
     */
    private MouseSignal mouse_event;
    /**
     * The KeyboardSignal.
     * @see MouseSignal
     */
    private KeyboardSignal key_event;
    /**
     * The ViewportSignal.
     * @see KeyboardSignal
     */
    private ViewportSignal vp_event;
    /**
     * The World.
     * @see World
     */
    private World world;
/**
 * Constructor
 */
    public LWJGL_Window() {
        mouseDispatcher = MouseDispatcher.getInstance();
        keyboardDispatcher = KeyboardDispatcher.getInstance();
        timerDispatcher = TimerDispatcher.getInstance();
        vpDispatcher = ViewportDispatcher.getInstance();
        closeRequested = false;
        renderer = new LWJGL_Renderer();
        mouse_event = new MouseSignal();
        key_event = new KeyboardSignal();
        vp_event = new ViewportSignal();
    }

    private void createWindow() {
        frame = new Frame(win_title);
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
            System.out.println("ERROR WITH LWJGL WINDOW.");
        }

    }
    /*
     * Display while the window is not closed.
     * 
     */
    public void display() {

        while (!Display.isCloseRequested() && !closeRequested) {
            newDim = newCanvasSize.getAndSet(null);
            if (newDim != null) {
                vp_event.setSize(newDim.width, newDim.height);
                GL11.glViewport(0, 0, newDim.width, newDim.height);
                vpDispatcher.fireUpdate(vp_event);

            }
            Display.sync(60);
            pollInput();
            displayScene();
            Display.update();
        }

        this.dispose();
        Display.destroy();
        frame.dispose();
    }
/**
 * Close the window
 */
    public void dispose() {
        renderer.dispose();
    }

    private void displayScene() {
        renderer.display();
    }

    @Override
    public void setDimension(int w, int h) {
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
        loadObserver(_world);
    }

    private void pollInput() {
        mouse_event.setButton(Signal.NO_BUTTON);
        mouse_event.setButtonStatus(Signal.NONE);
        mouse_event.setWheel(0);
        mouse_event.setXY(Mouse.getX(), Mouse.getY());
        int x = Mouse.getDX();
        int y = Mouse.getDY();

        if (x != 0 && y != 0) {
            mouse_event.setButtonStatus(Signal.MOVED);
        }

        if (Mouse.isButtonDown(0)) {

            mouse_event.setButton(Signal.BUTTON_LEFT);

        }
        if (Mouse.isButtonDown(1)) {
            mouse_event.setButton(Signal.BUTTON_RIGHT);

        }
        if (Mouse.isButtonDown(2)) {

            mouse_event.setButton(Signal.BUTTON_MIDDLE);

        }


        int dWheel = Mouse.getDWheel(); //don't work
   

        if (dWheel < 0) {
            mouse_event.setWheel(-1);
        } else if (dWheel > 0) {
            mouse_event.setWheel(1);
        }
        mouseDispatcher.fireUpdate(mouse_event);

        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
// do nothing yet
        } else if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            this.dispose();
            Display.destroy();
            frame.dispose();
        } else if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
          // do nothing yet
        } else if (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
      // do nothing yet
        } else if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
           // do nothing yet
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
        if (flag == true) {
            createWindow();
            display();
        }
    }

    private void loadObserver(Node obj) {
        if (obj instanceof MouseObserver) {
            mouseDispatcher.addObserver((Observer) obj);
        }
        if (obj instanceof ViewportObserver) {
            vpDispatcher.addObserver((Observer) obj);
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
    
} // end of class LWJGL_Window
