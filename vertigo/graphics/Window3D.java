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
package vertigo.graphics;

import vertigo.scenegraph.World;

/**
 *
 * @author Clement Delestre
 */

public interface Window3D {
    
    public void setWorld(World _world);
    
    public void setDimension(int w,int h);
    
    public void setTitle(String title);
    
    public void setBackgroundColor(int red, int green, int blue);

   public void setVisible(boolean flag);
   
} // end of class Window3D
