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

import java.nio.FloatBuffer;

/**
 * Class BufferData create float buffer
 *
 * @author Florin Buga
 * @author Olivier Catoliquot
 * @author Clement Delestre
 * @version 0.1
 */
public class BufferData {

    /**
     * The float buffer's handle
     *
     * @see BufferData#setHandle(int)
     */
    private int handle = -1;
    /**
     * The FloatBuffer buff
     *
     * @see BufferData#BufferData(float[])
     */
    private FloatBuffer buff;
    private boolean bound = false;

    public BufferData() {
    }

    /**
     * Create FloatBuffer buff, put data in buff
     *
     * @param data
     */
    public BufferData(float[] data) {
        buff = BufferTools.newFloatBuffer(data.length);
        buff.put(data);
        buff.flip();
    }

    /**
     *
     * @param data
     */
    public BufferData(FloatBuffer data) {
        buff.put(data);
        buff.flip();
    }

    /**
     *
     * @return false if is not bound
     */
    public boolean isBound() {
        return bound;
    }

    /**
     * set bound
     *
     * @param bound
     */
    public void setBound(boolean bound) {
        this.bound = bound;
    }

    /**
     * set bound to false
     *
     * @param bound
     */
    public void dontBind() {
        bound = false;
    }

    /**
     * set handle
     *
     * @param handle
     */
    public void setHandle(int handle) {
        this.handle = handle;
    }

    /**
     *
     * @return handle
     */
    public int getHandle() {
        return handle;
    }

    /**
     *
     * @return buff's capacity
     */
    public int getCapacity() {
        return buff.capacity();
    }

    /**
     *
     * @return buff
     */
    public FloatBuffer getFloatBuffer() {
        return buff;
    }
} // end of class BufferData
