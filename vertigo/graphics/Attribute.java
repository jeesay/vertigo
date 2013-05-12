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
 * Class Attribute
 *
 * @author Florin Buga
 * @author Olivier Catoliquot
 * @author Clement Delestre
 * @version 0.1
 *
 */
public class Attribute {

    /**
     * The attribute's name
     *
     * @see Attribute#getName()
     */
    private String name;
    /**
     * The attribute's handle
     *
     * @see Attribute#setHandle(int)
     * @see Attribute#getHandle()
     */
    private int handle = -1;
    /**
     * The attribute's type
     *
     * @see Attribute#getType()
     */
    private String type;

    public Attribute(String name) {
        this.name = name;
    }

    public Attribute(String name, String type) {
        this.name = name;
        this.type = type;
        System.out.println(" Name is " + name + " and type : " + type + " **** ");
    }

    /**
     * Gets Attribute's type
     *
     * @return type
     */
    public String getType() {
        return type;
    }

    public Attribute(String name, int handle) {
        this.name = name;
        this.handle = handle;
    }

    /**
     * Sets Attribute's handle
     *
     * @param handle
     */
    public void setHandle(int handle) {
        this.handle = handle;
    }

    /**
     * Gets Attribute's handle
     *
     * @return handle
     */
    public int getHandle() {
        return handle;
    }

    /**
     * Gets Attribute's type
     *
     * @return type
     */
    public String getVBOtype(String type) {
        return table.get(type);
    }

    /**
     * Gets Attribute's name
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets Attribute's size
     *
     * @return size
     */
    public int getSize() {
        if (type.contains("4")) {
            return 4;
        } else if (type.contains("3")) {
            return 3;
        } else if (type.contains("2")) {
            return 2;
        }
        return 4;
    }
} // end of class Attribute
