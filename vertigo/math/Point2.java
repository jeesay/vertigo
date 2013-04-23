/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vertigo.math;

/*
 * $Id:$
 *
 * VolTIJ, VOLume viewing Tools for ImageJ.
 * Copyright (C) 2009  Jean-Christophe Taveau.
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
 *  Michael Vacher
 *  Mickael Escudero
 *  Ibouniyamine Nabihoudine
 *  Frederic Romagné
 */



public class Point2 {

   public float x,  y;

   public Point2(float x, float y) {
      this.x = x;
      this.y = y;
   }

    /**
      * Returns a string that contains the values of this Tuple3. The form is (x,y).
      * @return the String representation
      */
    @Override
    public String toString() {
	    return "(" + x + ", " + y +")";
    }
}
