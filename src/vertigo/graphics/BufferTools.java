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

package vertigo;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

/**
 * A mixture of classes JOGAMP com.jogamp.common.nio.Buffers and org.lwjgl.BufferUtils
 *
 * @author Jean-Christophe Taveau
 */
public final class BufferTools {

    public static int SIZEOF_FLOAT = Float.SIZE / Byte.SIZE;
    public static int SIZEOF_INT = Integer.SIZE / Byte.SIZE;

    public static ByteBuffer newByteBuffer(int numElements) {
        return ByteBuffer.allocateDirect(numElements).order(ByteOrder.nativeOrder());
    }

    public static ShortBuffer newShortBuffer(int numElements) {
        return newByteBuffer(numElements << 1).asShortBuffer();
    }

    public static IntBuffer newIntBuffer(int numElements) {
        return newByteBuffer(numElements << 2).asIntBuffer();
    }

    public static FloatBuffer newFloatBuffer(int numElements) {
        return newByteBuffer(numElements << 2).asFloatBuffer();
    }

    public static DoubleBuffer newDoubleBuffer(int numElements) {
        return newByteBuffer(numElements << 3).asDoubleBuffer();
    }
}

