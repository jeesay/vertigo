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

/**
 * Class Uniform
 *
 * @author Florin Buga
 * @author Olivier Catoliquot
 * @author Clement Delestre
 * @version 0.1
 *
 */
public class Uniform {
  /**
 * The uniform's name
 * @see Uniform#getName() 
 */
    private String name;
       /**
 * The uniform's handle
 * @see Uniform#getHandle() 
 */
    private int handle = -1;
         /**
 * The uniform's type
 * @see Uniform#getType() 
 */
    private  String type;
    
    
    public Uniform (String name){
        this.name=name;
    }
     public Uniform (String name,String type){
        this.name=name;
        this.type=type;
    }
    public Uniform (String name, int handle){
        this.name=name;
        this.handle=handle;
    }
       /**
     * Gets Uniform's name
     *
     * @return name
     */
    public String getName(){
        return name;
    }
 /**
     * Gets Uniform's handle
     *
     * @return handle
     */
    public int getHandle() {
        return handle;
    }
 /**
     * Gets Uniform's type
     *
     * @return type
     */
    public String getType() {
        return type;
    }
} // end of class Uniform
