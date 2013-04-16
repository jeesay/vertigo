/*
 * $Id:$
 *
 * Vertigo_viewer: 3D Viewer Plugin for ImageJ.
 * Copyright (C) 2013 Jean-Christophe Taveau.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 *
 * Authors :
 * Florin Buga
 * Olivier Catoliquot
 * Clement Delestre
 */
package vertigo.graphics.text;

import ij.IJ;
import java.util.ArrayList;
import java.util.Iterator;
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

/**
 *
 * @author Clement DELESTRE
 */
public class GraphAnalyzer implements Visitor {
private static int num_cam=0;
private static int num_scene=0;
private static boolean check=true;

    /*public GraphAnalyzer() {
        
        // TODO
    }*/

    @Override
    public void visit(BackStage obj) {
        //do nothing
    }

    @Override
    public void visit(Camera obj) {
        num_cam++;
        if (num_cam > 1){
            IJ.log("ERROR : Camera must be single.");
            check=false;
        }
        if (obj.size() > 0) {
            IJ.log("ERROR : Camera can't have any children.");
            check=false;
        }
    }

    @Override
    public void visit(Light obj) {
          if (obj.size() > 0) {
            IJ.log("ERROR : Light can't have any children.");
            check=false;
        }
    }

    @Override
    public void visit(Lighting obj) {
        for (Node node : obj.getChildren()) {
            if (! (node instanceof Light)) {
                IJ.log("ERROR : The node "+node.getName()+" is misplaced, Lighting must have only Lights.");
                check=false;
            }
        }
    }

    @Override
    public void visit(Scene obj) {
         num_scene++;
         for (Node node : obj.getChildren()) {
            if (! (node instanceof Shape)) {
                IJ.log("ERROR : The node "+node.getName()+" is misplaced, Scene must have only Shapes.");
                check=false;
            }
        }
         if (num_scene>1){
             IJ.log("ERROR : Scene must be single.");
             check=false;
         }
                 
    }

    @Override
    public void visit(Shape obj) {
       //do nothing 
    }

   
    @Override
    public void visit(Stage obj) {
        //do nothing 
    }

    @Override
    public void visit(Transform obj) {
        //do nothing
    }

    @Override
    public void visit(Viewing obj) {
          for (Node node : obj.getChildren()) {
            if (! (node instanceof Camera)) {
                IJ.log("ERROR : The node "+node.getName()+" is misplaced, Viewing must have only Camera.");
                check=false;
            }
        }
    }

    @Override
    public void visit(World obj) {
        //do nothing 
    }

} // End of class GraphAnalyzer


