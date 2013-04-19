/*
 * $Id:$
 *
 * vertigo: 3D Viewer Plugin for ImageJ.
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
 * Florin Buga
 * Olivier Catoliquot
 * Clement Delestre
 */
package vertigo.scenegraph.transform;

import java.util.Observable;
import vertigo.graphics.event.MouseObserver;
import vertigo.graphics.event.Signal;
import vertigo.scenegraph.Transform;


public class ArcBall extends Transform implements MouseObserver {

    private int mouse_x;
    private int mouse_y;

    public ArcBall() {
        super();
        this.name = "arcball";
    }



  @Override
    public void update(Observable o, Object o1) {
        Signal e = (Signal) o1;
        System.out.println("Acrball : Event" + e);  
    }

    
} // end of class ArcBall

