/*
 * $Id:$
 *
 * Vertigo: 3D Viewer Plugin for ImageJ.
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
package vertigo.scenegraph;

import vertigo.graphics.Visitor;

/**
 * Class Viewing
 *
 * @author Florin Buga
 * @author Olivier Catoliquot
 * @author Clement Delestre
 * @version 0.1
 *
 */
public class Viewing extends Node {
    /**
     * Regular constructor.
     * @see Node
     */
    public Viewing(){
        super();
        name="viewing";
    }
      /**
     * Constructor with name.
     * @param name
     * @see Node
     */
    public Viewing(String name){
        super(name);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
        for (Node child : getChildren() )
            child.accept(visitor);
    }
} // end of class Viewing
