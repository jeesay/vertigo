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
import java.util.Hashtable;
/**
 * Class VBO
 *
 * @author Florin Buga
 * @author Olivier Catoliquot
 * @author Clement Delestre
 * @version 0.1
 *
 */
public class VBO extends BO {

    private FloatBuffer buffer;
    private Hashtable<String, Props> props;
    private BufferData Buffdata;
    /**
     * The VBO's offset
     * @see VBO#getOffset() 
     * @see VBO#getOffset(java.lang.String) 
     */
    private int offset;

    public VBO() {
        super();
        props = new Hashtable<String, Props>();
    }

    public VBO(String type) {
        super();
        this.type = type;
        offset = 0;
        stride = getSize(type);
    }

    public VBO(int stride, int offset, String type) {
        super();
        this.stride = stride;
        this.offset = offset;
        this.type = type;
    }
 /**
     * Gets VBO's size
     *
     * @return size
     */
    public int getSize() {
        return getSize(type);
    }
/**
     * Gets VBO's offset
     *
     * @return offset
     */
    public int getOffset() {
        return offset;
    }

    public void setBound(boolean bound) {
        Buffdata.setBound(bound);
    }

    public boolean isBound() {
        return Buffdata.isBound();
    }

    public Hashtable<String, Props> getProps() {
        return props;
    }
/**
     * Gets VBO's type
     *
     * @return size
     */
    public String getType() {
        return type;
    }
/**
     * Return true if the vbo is interleaved
     *
     * @return true or false
     */
    public boolean IsInterleaved() {
        return (props.size() > 1);
    }
/**
     * Sets the bufferData
     *
     * @param BufferData
     */
    public void setBuffData(BufferData BuffData) {
        this.Buffdata = BuffData;
    }
/**
     * Sets the FloatBuffer
     *
     * @param FloatBuffer, type
     */
    public void setFloatBuffer(String type, FloatBuffer buf) {
        props.put(type, new Props(type, 0, getSize(type)));
        offset = 0;
        stride = getSize(type);
        buffer = buf;
    }
/**
     * Gets the VBO stride
     *
     * @return stride
     */
    public int getStride() {
        return stride;
    }
/**
     * Sets the FloatBuffer
     *
     * @param types, FloatBuffer
     */
    public void setFloatBuffer(String[] types, FloatBuffer buf) {
        buffer = buf;

        //int stride = 0;
        // process stride
        for (int i = 0; i < types.length; i++) {
            stride += getSize(types[i]);
        }
        // process offset
        //int offset = 0;
        for (int i = 0; i < types.length; i++) {
            props.put(types[i], new Props(types[i], offset, stride));
            offset += getSize(types[i]);
        }
    }
/**
     * Gets the FloatBuffer
     *
     * @return FloatBuffer
     */
    public FloatBuffer getFloatBuffer() {
        return buffer;
    }
/**
     * Gets the VBO's offset
     *
     * @return offset
     */
    public int getOffset(String type) {
        return props.get(type).getOffset();
    }
/**
     * Gets the VBO's FloatBuff
     *
     * @return FloatBuff
     */
    public FloatBuffer getFloatBuff() {
        return Buffdata.getFloatBuffer();
    }
/**
     * Gets the VBO's stride
     *
     * @return stride
     */
    public int getStride(String type) {
        return props.get(type).getStride();
    }
/**
     * Gets the VBO's type
     *
     * @return type
     */
    public String getType(String type) {
        return props.get(type).getType();
    }
/**
     * Gets the VBO's length
     *
     * @return length
     */
    public int capacity() {
        // return buffer.capacity();
        return Buffdata.getCapacity();
    }

    private int getSize(String type) {
        if (type.contains("3")) {
            return 3;
        } else if (type.contains("2")) {
            return 2;
        }
        return 4;
    }
} // End of class VBO
