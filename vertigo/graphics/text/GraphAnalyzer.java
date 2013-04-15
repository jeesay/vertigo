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

/**
 *
 * @author Clement DELESTRE
 */
public class GraphAnalyzer implements Visitor {

    public GraphAnalyzer() {
        // TODO
    }

    @Override
    public void visit(BackStage obj) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void visit(Camera obj) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void visit(Light obj) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void visit(Lighting obj) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void visit(Scene obj) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void visit(Shape obj) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void visit(Stage obj) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void visit(Transform obj) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void visit(Viewing obj) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void visit(World obj) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }


    private boolean checkCam(World w) {
        int camnumber = 0;
        int len = w.size();
        for (int i = 0; i < len - 1; i++) {
            Node node = w.getChild(i);
            if (node instanceof Camera) {
                camnumber++;
            }
        }
        if (camnumber == 1) {
            return true;
        }
        return false;
    }

    private boolean checkScene(Scene s) {
        int error = 0;
        int len = s.size();
        for (int i = 0; i < len - 1; i++) {
            Node node = s.getChild(i);
            if (!(node instanceof Shape)) {
                error++;
            }
        }
        if (error == 0) {
            return true;
        }
        return false;
    }

    private boolean checkViewing(Viewing v) {
        int error = 0;
        int len = v.size();
        for (int i = 0; i < len - 1; i++) {
            Node node = v.getChild(i);
            if (!(node instanceof Camera)) {
                error++;
            }
        }
        if (error == 0) {
            return true;
        }
        return false;
    }

    private boolean checkLighting(Lighting l) {
        int error = 0;
        ArrayList<Node> children = l.getChildren();
        for (Iterator<Node> it = children.iterator(); it.hasNext();) {
            Node nodetemp = it.next();
            error += checkLightingR(nodetemp, error);
        }
        if (error == 0) {
            return true;
        }
        return false;
    }

    private int checkLightingR(Node n, int error) {
        if (!(n instanceof Light)) {
            error++;
            IJ.log("The object " + n.getName() + " is misplaced.");
        }
        ArrayList<Node> children = n.getChildren();
        for (Iterator<Node> it = children.iterator(); it.hasNext();) {
            Node nodetemp = it.next();
            error += checkLightingR(nodetemp, error);
        }
        return error;
    }

} // End of class GraphAnalyzer


