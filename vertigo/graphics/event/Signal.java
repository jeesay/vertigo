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
package vertigo.graphics.event;

/**
 * Class Signal, composed of public variables for each signal type.
 *
 * @author Florin Buga
 * @author Olivier Catoliquot
 * @author Clement Delestre
 * @version 0.1
 *
 */
public class Signal {

    /**
     * No button
     */
    public static final int NO_BUTTON = 0;
    /**
     * Left button.
     */
    public static final int BUTTON_LEFT = 1;
    /**
     * Middle button (wheel).
     */
    public static final int BUTTON_MIDDLE = 2;
    /**
     * Right Button.
     */
    public static final int BUTTON_RIGHT = 3;
    /**
     * Wheel up.
     */
    public static final int WHEEL_UP = 1;
    /**
     * Wheel down.
     */
    public static final int WHEEL_DOWN = -1;
    /**
     * Empty keyboard.
     */
    public static final int NONE = 100;
    /**
     * One letter is pressed.
     */
    public static final int PRESSED = 101;
    /**
     * One letter is released.
     */
    public static final int RELEASED = 102;
    /**
     * Mouse clicking.
     */
    public static final int CLICKED = 103;
    /**
     * Mouse moving.
     */
    public static final int MOVED = 104;
    /**
     * Mouse dragging.
     */
    public static final int DRAGGED = 105;
    /**
     * Mouse entering the window.
     */
    public static final int ENTERED = 106;
    /**
     * Mouse exiting the window.
     */
    public static final int EXITED = 107;
} // end of class Signal
