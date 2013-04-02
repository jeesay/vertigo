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
 * Jean-Christophe Taveau
 *
 */

package vertigo.graphics;

import java.nio.IntBuffer;

public class IBO extends BO {
  
  IntBuffer buffer;
  String type;
  int stride;
  
  public IBO() {
    super();
  }
  
  public void setIntBuffer(IntBuffer buf) {
        buffer = buf;
        this.type = "INDX";
        this.stride = 0;
    }

    public IntBuffer getIntBuffer() {
        return buffer;
    }

    public int getSize() {
        return buffer.capacity();
    }

} // End of class IBO
