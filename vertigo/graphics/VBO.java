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

    private BufferData buffer;
    /**
     * The VBO's offset
     * @see VBO#getOffset() 
     * @see VBO#getOffset(java.lang.String) 
     */
    private int offset;

    public VBO() {
        super();
    }



    public VBO(String type, int offset, int stride) {
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
        if (type.contains("3")) {
            return 3;
        } else if (type.contains("2")) {
            return 2;
        }
        return 4;
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
        buffer.setBound(bound);
    }

    public boolean isBound() {
        return buffer.isBound();
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

    public boolean IsInterleaved() {
        return (props.size() > 1);
    }
     */   
   /**
     * Sets the bufferData
     *
     * @param BufferData
     */
    public void setBufferData(BufferData BuffData) {
        this.buffer = BuffData;
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
     * Gets the FloatBuffer
     *
     * @return FloatBuffer
     */
    public FloatBuffer getFloatBuffer() {
        return buffer.getFloatBuffer();
    }


   /**
     * Gets the VBO's length
     *
     * @return length
     */
    public int capacity() {
        // return buffer.capacity();
        return buffer.getCapacity();
    }


} // End of class VBO


/***

    public VBO(String type) {
        super();
        this.type = type;
        offset = 0;
        stride = getSize(type);
    }


    public Hashtable<String, Props> getProps() {
        return props;
    }

***/

   /**
     * Gets the VBO's stride
     *
     * @return stride

    public int getStride(String type) {
        return props.get(type).getStride();
    }
     */

   /**
     * Sets the FloatBuffer
     *
     * @param types, FloatBuffer

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
     */

   /**
     * Sets the FloatBuffer
     *
     * @param FloatBuffer, type

    public void setFloatBuffer(String type, FloatBuffer buf) {
        props.put(type, new Props(type, 0, getSize(type)));
        offset = 0;
        stride = getSize(type);
        buffer = buf;
    }
     */

   /**
     * Gets the VBO's FloatBuff
     *
     * @return FloatBuff

    public FloatBuffer getFloatBuff() {
        return Buffdata.getFloatBuffer();
    }
     */

