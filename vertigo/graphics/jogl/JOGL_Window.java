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

import javax.media.opengl.GLProfile;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.awt.GLCanvas;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observer;
import vertigo.graphics.EventDispatcher;
import vertigo.graphics.OGL_Window;
import vertigo.graphics.event.KeyboardObserver;
import vertigo.graphics.event.MouseObserver;
import vertigo.graphics.event.Signal;
import vertigo.graphics.event.MouseSignal;
import vertigo.graphics.event.TimerObserver;
import vertigo.scenegraph.Node;
import vertigo.scenegraph.World;

public class JOGL_Window implements OGL_Window, MouseMotionListener, MouseListener, KeyListener, MouseWheelListener {

    private int width = 400;
    private int height = 400;
    private static final String WIN_TITLE = "Vertigo JOGL - ";
    private String win_title;
    private World world;
    private Frame frame;
    private JOGL_Renderer renderer;
    private EventDispatcher eventDispatcher;
    private MouseSignal event;
    private Signal allevent;
    private final EventDispatcher keyboardDispatcher;
    private final EventDispatcher mouseDispatcher;
    private final EventDispatcher timerDispatcher;

    public JOGL_Window() {
        renderer = new JOGL_Renderer();
        mouseDispatcher = EventDispatcher.getInstance();
        keyboardDispatcher = EventDispatcher.getInstance();
        timerDispatcher = EventDispatcher.getInstance();
        event = new MouseSignal();
        allevent = new Signal();

    }

    @Override
    public void setWorld(World _world) {
        world = _world;
        renderer.setWorld(_world);
        System.out.println("entrée dans load observer");
        loadObserver(_world);
        System.out.println("sortie de load observer");
    }

    private void loadObserver(Node obj) {
        System.out.println("Load Observer observer " + obj);
        if (obj instanceof MouseObserver) {
            mouseDispatcher.addObserver((Observer) obj);
        } else if (obj instanceof TimerObserver) {
            timerDispatcher.addObserver((Observer) obj);
        } else if (obj instanceof KeyboardObserver) {
            keyboardDispatcher.addObserver((Observer) obj);
        }
        for (Node child : obj.getChildren()) {
            loadObserver(child);
        }
    }

    @Override
    public void setDimension(int w, int h) {
        width = w;
        height = h;
    }

    @Override
    public void setBackgroundColor(int red, int green, int blue) {
        renderer.setBackgroundColor((red / 255.0f), (green / 255.0f), (blue / 255.0f));
        System.out.println("Background of JOGL");
    }

    @Override
    public void setTitle(String title) {
        win_title = title;
    }

    @Override
    public void setVisible(boolean flag) {
        if (flag == true) {
            createWindow();
        }
    }

    private void createWindow() {
        System.out.println("create window");

        frame = new Frame();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();  // Ferme l'application si on clic sur la croix en haut à droite
            }
        });

        // Set the title
        frame.setTitle(WIN_TITLE + win_title);
        //Set the size
        frame.setSize(width, height);

        GLProfile glp = GLProfile.getDefault();
        GLProfile.initSingleton();
        GLCapabilities caps = new GLCapabilities(glp);
        GLCanvas canvas = new GLCanvas(caps);
        canvas.addMouseListener(this);
        canvas.addMouseWheelListener(this);
        canvas.addKeyListener(this);
        canvas.addGLEventListener(renderer);

        frame.add(canvas);
        frame.setVisible(true); // Make the frame visible


    }

    @Override
    public void mouseClicked(MouseEvent me) {
        System.out.println("La souris est cliquée.");
    }

    @Override
    public void mousePressed(MouseEvent me) {
        System.out.println("Mouse pressed; # of clicks: " + me.getClickCount());
        if (me.getButton() == MouseEvent.BUTTON1) {
            String test = me.getModifiersExText(me.getButton());
            System.out.println("Test is : " + test);
            event.setButton(allevent.BUTTON_LEFT);
            event.setWheel(0);
            System.out.println(event);
        } else if (me.getButton() == MouseEvent.BUTTON3) {
            System.out.println("Right clic.");
            event.setButton(allevent.BUTTON_RIGHT);
            event.setWheel(0);
            System.out.println(event);
        } else if (me.getButton() == MouseEvent.BUTTON2) {
            System.out.println("La molette est enfoncée.");
            event.setButton(allevent.BUTTON_MIDDLE);
            event.setWheel(0);
            System.out.println(event);
        } else {
            System.out.println("Unknow button.");
        }
        eventDispatcher.fireUpdate(event);
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        System.out.println("Un des boutons est relâchée.");
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        System.out.println("La souris est entrée dans la fenêtre en " + me.getX() + " " + me.getY());
    }

    @Override
    public void mouseExited(MouseEvent me) {
        System.out.println("La souris est sortie de la fenêtre.");
    }

    @Override
    public void keyTyped(KeyEvent ke) {
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        char press = ke.getKeyChar();
        mouseDispatcher.fireUpdate(event);
        System.out.println("You have pressed " + press);

    }

    @Override
    public void keyReleased(KeyEvent ke) {
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent mwe) {

        if (mwe.getWheelRotation() < 0) {
            System.out.println("Rotated Up... " + mwe.getWheelRotation());
            event.setWheel(allevent.WHEEL_UP);
            event.setButton(0);
            System.out.println(event);
        } else {
            System.out.println("Rotated Down... " + mwe.getWheelRotation());
            event.setWheel(allevent.WHEEL_DOWN);
            event.setButton(0);
            System.out.println(event);
        }
        mouseDispatcher.fireUpdate(event);
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        //TODO
    }

    @Override
    public void mouseMoved(MouseEvent me) {
        //TODO
    }
} //end of class JOGL_Window

