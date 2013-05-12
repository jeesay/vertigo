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
 * Class MouseSignal
 *
 * @author Florin Buga
 * @author Olivier Catoliquot
 * @author Clement Delestre
 * @see Signal
 * @version 0.1
 *
 */
public class MouseSignal extends Signal {

    /**
     * The button of the mouse.
     *
     * @see MouseSignal#getButton()
     * @see MouseSignal#setButton(int)
     */
    private int button;
    /**
     * The mouse's wheel
     *
     * @see MouseSignal#getWheel()
     */
    private int wheel;
    /**
     * The mouse status
     *
     * @see MouseSignal#getButtonStatus()
     */
    private int status;
    /**
     * The mouse's x coordinate
     *
     * @see MouseSignal#getX()
     * @see MouseSignal#setXY(int, int)
     */
    private int mouse_x;
    /**
     * The mouse's y coordinate
     *
     * @see MouseSignal#getY()
     * @see MouseSignal#setXY(int, int)
     */
    private int mouse_y;
    /**
     * True if no button is released.
     *
     * @see MouseSignal#setEmpty()
     * @see MouseSignal#isEmpty()
     */
    private boolean empty = true;
    private boolean down = false;

    public MouseSignal() {
    }

    /**
     * Get mouse's X
     *
     * @return x
     */
    public int getX() {
        return mouse_x;
    }

    /**
     * Get mouse's Y
     *
     * @return y
     */
    public int getY() {
        return mouse_y;
    }

    /**
     * Set Button ID
     *
     * @param button BUTTON1, BUTTON2 or BUTTON3
     */
    public void setButton(int button) {
        this.button = button;
    }

    /**
     * Get Button ID
     *
     * @return BUTTON1, BUTTON2 or BUTTON3
     */
    public int getButton() {
        return this.button;
    }

    /**
     * Get wheel scroll
     *
     * @return -1, 0, +1 corresponding to wheel scroll as UP, NONE, DOWN,
     * respectively
     */
    public int getWheel() {
        return this.wheel;
    }

    /**
     * Set wheel scroll
     *
     * @return -1, 0, +1 corresponding to wheel scroll as UP, NONE, DOWN,
     * respectively
     */
    public void setWheel(int wheel) {
        this.wheel = wheel;
    }

    /**
     * Sets the mouse's coordinates.
     *
     * @param x
     * @param y
     */
    public void setXY(int x, int y) {
        mouse_x = x;
        mouse_y = y;
    }

    /**
     * Return true if the mouse is empty
     *
     * @return empty
     */
    public boolean isEmpty() {
        return empty;
    }

    /**
     * Set Button status
     *
     * @param status PRESSED, RELEASED, MOVED,
     */
    public void setButtonStatus(int status) {
        this.status = status;
    }

    /**
     * Get Button status
     *
     * @return PRESSED, RELEASED, MOVED,
     */
    public int getButtonStatus() {
        return this.status;
    }

    /**
     * Sets the mouse empty.
     */
    public void setEmpty() {
        empty = true;
    }

    public void setDown() {
        down = true;
    }

    /**
     * Return true if the mouse is dragged.
     *
     * @param button
     * @return boolean
     */
    public boolean isDragged(int button) {
        return (isButtonDown(button) && status == Signal.MOVED);
    }

    public boolean isButtonDown(int button) {
        return down;
    }

    public boolean isButtonDown() {
        return down;
    }

    @Override
    public String toString() {
        return "[Button : " + button + " Status : " + status + " Wheel :  " + wheel + " xy(" + mouse_x + ";" + mouse_y + ") ]";
    }
} // end of class MouseSignal
