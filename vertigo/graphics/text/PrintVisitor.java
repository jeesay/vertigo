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
package vertigo.graphics.text;

import ij.IJ;
import vertigo.graphics.Visitor;

import vertigo.scenegraph.BackStage;
import vertigo.scenegraph.Camera;
import vertigo.scenegraph.Light;
import vertigo.scenegraph.Lighting;
import vertigo.scenegraph.Node;
import vertigo.scenegraph.Scene;
import vertigo.scenegraph.Shape;
import vertigo.scenegraph.Stage;
import vertigo.scenegraph.Transform;
import vertigo.scenegraph.Viewing;
import vertigo.scenegraph.World;

public class PrintVisitor implements Visitor {

    private int level;

    @Override
    public void visit(BackStage obj) {
        IJ.log(computeLevel(obj) +"BackStage: " +obj.getName() );
    }

    @Override
    public void visit(Camera obj) {
        IJ.log(computeLevel(obj) +"Camera: " +obj.getName() );
    }

    @Override
    public void visit(Light obj) {
        IJ.log(computeLevel(obj) +"Light: " +obj.getName() );
    }

    @Override
    public void visit(Lighting obj) {
        IJ.log(computeLevel(obj) +"Lighting: " +obj.getName() );
    }

    public void visit(Node obj) {
        IJ.log(computeLevel(obj) +"Node: " +obj.getName() );
    }

    @Override
    public void visit(Scene obj) {
        IJ.log(computeLevel(obj) +"Scene: " +obj.getName() );
    }

    @Override
    public void visit(Shape obj) {
        IJ.log(computeLevel(obj) +"Shape: " +obj.getName() );
    }

    @Override
    public void visit(Stage obj) {
        IJ.log(computeLevel(obj) +"Stage: " +obj.getName() );
    }

    @Override
        public void visit(Transform obj) {
        IJ.log(computeLevel(obj) +"Transform: " +obj.getName() );
    }

    @Override
    public void visit(Viewing obj) {
        IJ.log(computeLevel(obj) +"Viewing: " +obj.getName() );
    }

    @Override
    public void visit(World obj) {
        IJ.log(computeLevel(obj) +" World: " +obj.getName() );
    }

    public String computeLevel(Node obj) {
        if (obj instanceof World) 
            return "+";
        else {
            Node n = obj; 
            String level = "";
            do {
                level+=".";
                n = n.getParent();
            } while ( !(n instanceof World));
            level+="+-";
            return level;
        }  
    }
} // end of class PrintVisitor

