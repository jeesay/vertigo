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

package vertigo.graphics;
/**
 * Class Props set some properties
 * @author Jean-Christophe Taveau
 */
public class Props {
/**
     * The type of Props
     *
     * @see Props#getType() 
     */
    private String type;
    /**
     * The offset of Props
     *
     * @see Props#getOffset() 
     */
    private int offset;
    /**
     * The stride of Props
     *
     * @see Props#stride
     */
    private int stride;
/**
 *  sets the type, offset and stride  of Props
 * @param type String value of Props
 * @param offset Integer value of Props
 * @param stride Integer value of Props
 */
    public Props(String type,int offset, int stride) {
        this.type=type;
        this.offset=offset;
        this.stride=stride;
    }
/**
 * 
 * @return type 
 */
    public String getType() {
        return type;
    }
/**
 * 
 * @return offset
 */
    public int getOffset() {
        return offset;
    }
/**
 * 
 * @return stride
 */
    public int getStride() {
        return stride;
    }

} // End of class Props

