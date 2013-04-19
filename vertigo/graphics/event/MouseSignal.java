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
    
    // PUSH, RELEASED, CLICK,DOUBLE_CLICK
    private int buttonStatus;
    
    // LEFT MIDDLE RIGHT
 
    
    // CTRL SHIFT ALT
    
    // DRAG MOVE
    
    //WHEEL UP DOWN
    private int button;
    private int wheel;
 public MouseSignal(){
     
 }
    public void setButton(int button){
        this.button=button;
    }
    public void setWheel(int wheel){
        this.wheel=wheel;
    }
    
    
    
} // end of class MouseSignal
