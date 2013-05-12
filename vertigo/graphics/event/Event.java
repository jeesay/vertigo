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
 * Class Event
 *
 * @author Florin Buga
 * @author Olivier Catoliquot
 * @author Clement Delestre
 * @version 0.1
 *
 */
public class Event {
/**
 * Left button.
 */
    public static final int BUTTON_LEFT = 1;
    /**
     * Middle button (wheel).
     */
    public static final int BUTTON_MIDDLE = 2;
    /**
     * Right button.
     */
    public static final int BUTTON_RIGHT = 4;
      /**
     * Wheel up.
     */
    public static final int WHEEL_UP = 1;
     /**
     * Wheel down.
     */
    public static final int WHEEL_DOWN = -1;
    /**
     * Push button.
     */
    public static final int PUSH = 0;
    /**
     * Click button.
     */
    public static final int CLICK = 1;
    public int button;

    @Override
    public String toString() {
       return "The value of button is : " +button;
    }
} // end of class Event
