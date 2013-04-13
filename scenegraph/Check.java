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
package vertigo.scenegraph;

/**
 *
 * @author Clement DELESTRE
 */
public class Check {

    public boolean checkCam(World w) {
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

    public boolean checkScene(Scene s) {
        int error = 0;
        int len = s.size();
        for (int i = 0; i < len - 1; i++) {
            Node node = s.getChild(i);
            if (node instanceof Shape) {
            } else {
                error++;
            }
        }
        if (error == 0) {
            return true;
        }
        return false;
    }

    public boolean checkViewing(Viewing v) {
        int error = 0;
        int len = v.size();
        for (int i = 0; i < len - 1; i++) {
            Node node = v.getChild(i);
            if (node instanceof Camera) {
            } else {
                error++;
            }
        }
        if (error == 0) {
            return true;
        }
        return false;
    }

    public boolean checkLighting(Lighting l) {
        int error = 0;
        int len = l.size();
        for (int i = 0; i < len - 1; i++) {
            Node node = l.getChild(i);
            if (node instanceof Light) {
            } else {
                error++;
            }
        }
        if (error == 0) {
            return true;
        }
        return false;
    }
} // end of class Check
