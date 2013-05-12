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
 * Class BO
 *
 * @author Florin Buga
 * @author Olivier Catoliquot
 * @author Clement Delestre
 * @version 0.1
 *
 */
public class BO {
/**
 * the BO's handle
 * @see BO#getHandle() 
 * @see BO#isDirty() 
 * @see BO#setHandle(int) 
 */
    protected int handle;
    /**
     * the BO's stride
     * @see VBO
     */
    protected int stride;
    /**
     * The BO type
     * @see VBO
     * @see IBO
     */
    protected String type;

    public BO() {
        handle = -1;
    }
  /**
     * Sets the handle
     *
     * @param handle
     */
    public void setHandle(int value) {
        handle = value;
    }
/**
     * Gets the handle
     *
     * @return handle
     */
    public int getHandle() {
        return handle;
    }
/**
     * BO is dirty if it is not bound
     *
     * @return boolean
     */
    public boolean isDirty() {
        return (handle == -1);
    }
} // End of class BO
