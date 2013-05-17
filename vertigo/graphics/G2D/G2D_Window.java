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
package vertigo.graphics.G2D;

import java.awt.Color;
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
import javax.swing.JFrame;

import vertigo.graphics.KeyboardDispatcher;
import vertigo.graphics.MouseDispatcher;
import vertigo.graphics.TimerDispatcher;
import vertigo.graphics.ViewportDispatcher;

import vertigo.graphics.Window3D;
import vertigo.graphics.event.KeyboardObserver;
import vertigo.graphics.event.MouseObserver;
import vertigo.graphics.event.TimerObserver;
import vertigo.graphics.event.ViewportObserver;
import vertigo.graphics.event.MouseSignal;
import vertigo.graphics.event.ViewportSignal;
import vertigo.graphics.event.Signal;

import vertigo.scenegraph.Node;
import vertigo.scenegraph.Transform;
import vertigo.scenegraph.transform.ArcBall;
import vertigo.scenegraph.World;

public class G2D_Window implements Window3D, MouseMotionListener, MouseListener, KeyListener, MouseWheelListener{

    private int width = 320;
    private int height = 240;
    private float red;
    private float green;
    private float blue;
    private static final String WIN_TITLE = "Vertigo G2D - ";
    private String window_title = "";
    private World world;
    private JFrame frame;
    private G2D_Panel panel;

    private MouseDispatcher mouseDispatcher; 
    private TimerDispatcher timerDispatcher;
    private KeyboardDispatcher keyboardDispatcher;
    private ViewportDispatcher viewportDispatcher;

    /**
     * Constructor
     *
     */
    public G2D_Window() {
        mouseDispatcher = MouseDispatcher.getInstance();
        keyboardDispatcher = KeyboardDispatcher.getInstance();
        timerDispatcher = TimerDispatcher.getInstance();
        viewportDispatcher = ViewportDispatcher.getInstance();
        red = 0.0f; 
        green = 0.0f;
        blue = 0.0f;
    }

    @Override
    public void setWorld(World world) {
        this.world = world;
    }

    @Override
    public void setDimension(int w, int h) {
        width = w;
        height = h;
    }

    @Override
    public void setTitle(String title) {
        window_title = title;
    }

    @Override
    public void setBackgroundColor(int red, int green, int blue) {
        this.red = red / 255.0f;
        this.green = green / 255.0f;
        this.blue = blue / 255.0f;
    }

    @Override
    public void setVisible(boolean flag) {
        if (flag) {
            createWindow();
        }
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        System.out.println("Mouse clicked");
    }

    @Override
    public void mousePressed(MouseEvent me) {
/***
        System.out.println("Button : " + me.getButton() + " " + " " + me.MOUSE_PRESSED+ " " + me.MOUSE_RELEASED);
        System.out.println("Button : " + me.MOUSE_MOVED+ " " + me.MOUSE_DRAGGED+ " " + me.MOUSE_ENTERED+ " " + me.MOUSE_EXITED);
        System.out.println("Modifiers int : " + me.getModifiers() + " " + " " + me.getModifiersEx());
        System.out.println("Modifiers text : " + MouseEvent.getMouseModifiersText(me.getModifiers()) + " " + " " + MouseEvent.getMouseModifiersText(me.getModifiersEx()));
        
        System.out.println("Mouse pressed; # of clicks: " + me.getClickCount());
***/

        MouseSignal mouse_event = new MouseSignal();
        mouse_event.setXY(me.getX(), me.getY());

        switch ( me.getButton() ) {
        case MouseEvent.BUTTON1 :
            mouse_event.setButton(Signal.BUTTON_LEFT);
            break;
        case MouseEvent.BUTTON2 :
            mouse_event.setButton(Signal.BUTTON_MIDDLE);
            break;
        case MouseEvent.BUTTON3 :
            mouse_event.setButton(Signal.BUTTON_RIGHT);
            break;
        default:
           // Do nothing ?
        }
        mouseDispatcher.fireUpdate(mouse_event);
        panel.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        // System.out.println("Mouse button UP.");
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        // System.out.println("Mouse Entered " + me.getX() + " " + me.getY());
    }

    @Override
    public void mouseExited(MouseEvent me) {
        // System.out.println("Mouse exited.");
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        MouseSignal mouse_event = new MouseSignal();
        mouse_event.setXY(me.getX(), me.getY());
        mouse_event.setButton(Signal.BUTTON_LEFT);
        mouse_event.setButtonStatus(Signal.MOVED);
        // System.out.println("dragged " + me.getX() + " " + me.getY());
        mouseDispatcher.fireUpdate(mouse_event);
        panel.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent me) {
        MouseSignal mouse_event = new MouseSignal();
        mouse_event.setXY(me.getX(), me.getY());
        mouse_event.setButton(Signal.NO_BUTTON);
        mouse_event.setButtonStatus(Signal.MOVED);
        // System.out.println("moved " + me.getX() + " " + me.getY());
        mouseDispatcher.fireUpdate(mouse_event);
        panel.repaint();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent mwe) {
        MouseSignal mouse_event = new MouseSignal();
        if (mwe.getWheelRotation() < 0) {
            System.out.println("Wheel Up... " + mwe.getWheelRotation());
            mouse_event.setWheel(Signal.WHEEL_UP);
            mouse_event.setButton(0);
            System.out.println(mouse_event);
        } else {
            System.out.println("Wheel Down... " + mwe.getWheelRotation());
            mouse_event.setWheel(Signal.WHEEL_DOWN);
            mouse_event.setButton(0);
            System.out.println(mouse_event);
        }
        mouseDispatcher.fireUpdate(mouse_event);
        panel.repaint();
    }


    @Override
    public void keyTyped(KeyEvent ke) {
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        char press = ke.getKeyChar();
        // TODO mouseDispatcher.fireUpdate(mouse_event);
        System.out.println("You have pressed " + press);

    }

    @Override
    public void keyReleased(KeyEvent ke) {
    }

    private void createWindow() {
        loadObserver(world);
        // Create window3D
        frame = new JFrame(WIN_TITLE + window_title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        panel = new G2D_Panel();
        panel.setWorld(world);
        panel.setBackground(new Color(red, green, blue));
        panel.addMouseListener(this);
        panel.addMouseWheelListener(this);
        panel.addKeyListener(this);
        panel.addMouseMotionListener(this);
        frame.setContentPane(panel);
        frame.setVisible(true);
    }
    
     private void loadObserver(Node obj) {
        if (obj instanceof MouseObserver) {
            mouseDispatcher.addObserver((Observer) obj);
        } 
        if (obj instanceof TimerObserver) {
            timerDispatcher.addObserver((Observer) obj);
        }
        if (obj instanceof KeyboardObserver) {
            keyboardDispatcher.addObserver((Observer) obj);
        }
        if (obj instanceof ViewportObserver) {
            System.out.println("Add Viewport " + obj);
            viewportDispatcher.addObserver((Observer) obj);
        }
        for (Node child : obj.getChildren()) {
            loadObserver(child);
        }
    }
} // End of class G2D_Window

