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

import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.Display;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL11;
import vertigo.graphics.Renderer;
import vertigo.scenegraph.World;

public class LWJGL_Renderer implements Renderer {

    private int width;
    private int height;
    private float red;
    private float green;
    private float blue;
    private String title_ = "";

    //public LWJGL_Renderer(){} 
    //singleton
    public LWJGL_Renderer() {
        System.out.println("constructor");
    }

    public void initShader() {
    }

    public void initVBO() {
    }

    @Override
    public void display() {
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);

        //process dirty
        // display scenegraph
        //camera
        //scene nodes
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
    public void createWindow(String title, int w, int h) {
        setDimension(w, h);
        setTitle(title);
        createContext();
        System.out.println("On crée une window");
    }

    @Override
    public void init(World _world) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /*
     * Copyright (c) 2008-2011, Matthias Mann
     *
     * All rights reserved.
     *
     * Redistribution and use in source and binary forms, with or without
     * modification, are permitted provided that the following conditions are met:
     *
     *     * Redistributions of source code must retain the above copyright notice,
     *       this list of conditions and the following disclaimer.
     *     * Redistributions in binary form must reproduce the above copyright
     *       notice, this list of conditions and the following disclaimer in the
     *       documentation and/or other materials provided with the distribution.
     *     * Neither the name of Matthias Mann nor the names of its contributors may
     *       be used to endorse or promote products derived from this software
     *       without specific prior written permission.
     *
     * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
     * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
     * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
     * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
     * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
     * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
     * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
     * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
     * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
     * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
     * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
     */
    private void createContext() {
        /**
         * ****
         * Frame frame = new Frame("title_"); frame.setLayout(new BorderLayout());
         * final Canvas canvas = new Canvas();
         *
         * canvas.addComponentListener(new ComponentAdapter() {
         *
         * @Override public void componentResized(ComponentEvent e) {
         * newCanvasSize.set(canvas.getSize()); } });
         *
         * frame.addWindowFocusListener(new WindowAdapter() {
         * @Override public void windowGainedFocus(WindowEvent e) {
         * canvas.requestFocusInWindow(); } });
         *
         * frame.addWindowListener(new WindowAdapter() {
         * @Override public void windowClosing(WindowEvent e) { closeRequested =
         * true; } });
         *
         * frame.add(canvas, BorderLayout.CENTER);
         *
         * try { Display.setParent(canvas); Display.setVSyncEnabled(true);
         *
         * frame.setPreferredSize(new Dimension(1024, 786));
         * frame.setMinimumSize(new Dimension(250, 250)); frame.pack();
         * frame.setVisible(true); Display.create();
         *
         * LWJGLRenderer renderer = new LWJGLRenderer();
         *
         * Dimension newDim;
         *
         * while(!Display.isCloseRequested() && !closeRequested) { newDim =
         * newCanvasSize.getAndSet(null);
         *
         * if (newDim != null) { GL11.glViewport(0, 0, newDim.width,
         * newDim.height); renderer.syncViewportSize(); }
         *
         * display(); Display.update(); }
         *
         * Display.destroy(); frame.dispose(); System.exit(0); } catch
         * (LWJGLException e) { e.printStackTrace(); } catch (IOException e) {
         * e.printStackTrace(); }
         *
         ****
         */
        try {
            Display.setDisplayMode(new DisplayMode(width, height));
            Display.setTitle(title_);
            Display.setInitialBackground(red, green, blue);
            Display.create();

        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(0);
        }
        while (!Display.isCloseRequested()) {
            Display.sync(60);
            pollInput();
            display();
            Display.update();

        }
        Display.destroy();
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
