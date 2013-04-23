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
 * @author Authors :
 *Florin Buga
 *Olivier Catoliquot
 *Clement Delestre
 */

public class MouseSignal extends Signal {


    
 
    
    // CTRL SHIFT ALT
    
    // DRAG MOVE
    

    // LEFT MIDDLE RIGHT
    private int button;

    //WHEEL UP DOWN
    private int wheel;
    private int status;
    private int mouse_x;
    private int mouse_y;
    private boolean empty=true;
    private boolean down=false;
    
    public MouseSignal() {
    
    }
 
    public int getX(){
        return mouse_x;
    }

    public int getY(){
        return mouse_y;
    }
 
   /**
    * Set Button ID
    *
    * @param BUTTON1, BUTTON2 or BUTTON3
    */
    public void setButton(int button){
        this.button=button;
    }

   /**
    * Get Button ID
    *
    * @return BUTTON1, BUTTON2 or BUTTON3
    */
    public int getButton(){
        return this.button;
    }

   /**
    * Get wheel scroll
    *
    * @return -1, 0, +1 corresponding to wheel scroll as UP, NONE, DOWN, respectively
    */
    public int getWheel(){
        return this.wheel;
    }


   /**
    * Set wheel scroll
    *
    * @return -1, 0, +1 corresponding to wheel scroll as UP, NONE, DOWN, respectively
    */
    public void setWheel(int wheel){
        this.wheel=wheel;
    }


    public void setXY(int x, int y) {
        mouse_x=x;
        mouse_y=y;
    }

    public boolean isEmpty() {
        return empty;
    }

   /**
    * Set Button status
    *
    * @param PRESSED, RELEASED, MOVED,
    */
    public void setButtonStatus(int status) {
        this.status=status;
    }

   /**
    * Get Button status
    *
    * @return PRESSED, RELEASED, MOVED,
    */
    public int getButtonStatus() {
        return this.status;
    }

    public void setEmpty(){
        empty=true;
    }

    public void setDown(){
        down=true;
    }

    public boolean isDragged(int button) {
        return (isButtonDown(button) && status==Signal.MOVED);
    }
    
    public boolean isButtonDown(int button) {
        return down;
    }
    
    public boolean isButtonDown() {
        return down;
    }
    
    @Override
    public String toString() {
       return "[Button : " +button+" Status : " +status +" Wheel :  "+wheel+" xy("+mouse_x+";"+mouse_y + ") ]";
    }
    
    
} // end of class MouseSignal
