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
import java.util.Observer;

import vertigo.graphics.Visitor;
import vertigo.scenegraph.Node;
import vertigo.scenegraph.Transform;


public class ArcBall extends Transform implements Observer {

    private int mouse_x;
    private int mouse_y;

    public ArcBall() {
        super();
        this.name = "arcball";
    }



    @Override
    public void update(Observable o, Object o1) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
        for (Node child : getChildren()) {
            child.accept(visitor);
        }
    }
}