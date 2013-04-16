/**
 * $Id:$
 *
 * Vertigo: 3D Viewer Plugin for ImageJ. Copyright (C) 2013
 * Jean-Christophe Taveau.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 *
 * Authors : Florin Buga Olivier Catoliquot Clement Delestre
 */
package vertigo.scenegraph;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import vertigo.graphics.BO;
import vertigo.graphics.VBO;
import vertigo.graphics.BufferTools;
import vertigo.graphics.PackedVBO;
import vertigo.graphics.IBO;

public class Geometry {

    private ArrayList<BO> buffers;

    public Geometry() {
        buffers = new ArrayList<BO>();
    }

    public ArrayList <BO> getBuffers(){
        return buffers;
}
    
    /**
     * Sets the geometry
     *
     * @param type - Data type: V3F,C3F,T2F,T3F, N3F,etc.
     * @param primitives - an array containing the data for each vertex
     * (coordinates, normal, colors,etc.).
     */
    public void setVertices(String type, float[] data) {
        FloatBuffer buf = BufferTools.newFloatBuffer(data.length);
        buf.put(data);
        buf.rewind();
        VBO vbo = new VBO();
        vbo.setFloatBuffer(type, buf);
        buffers.add(vbo);
    }

    /**
     * Sets the whole geometry in one single buffer. Ex: X Y Z R G B X Y Z R ...
     * May contain various types of data: XYZ-coordinates, Normals, Colors,
     * UV-TexCoords,etc.
     *
     * @param types - Data type: VEC3F,COL3F,TEX2F,TEX3F, NORM3F,etc.
     * @param primitives - an array containing the data for each vertex
     * (coordinates, normal, colors,etc.).
     * @param contents - a String containing the various data type present in
     * the primitives array. Ex: "[VEC3F,COL3F,NORM3F,TEX2F]" corresponds to
     * Vertex, Color, Normal and finally TexCoords data for each vertex.
     */
    public void setVertices(String[] types, float[] data) {
        FloatBuffer buf = BufferTools.newFloatBuffer(data.length);
        buf.put(data);
        buf.rewind();
        PackedVBO vbo = new PackedVBO();
        vbo.setFloatBuffer(types, buf);
        buffers.add(vbo);
    }

    public void setIndices(int[] indexes) {
        IntBuffer buf = BufferTools.newIntBuffer(indexes.length);
        buf.put(indexes);
        buf.rewind();
        IBO ibo = new IBO();
        ibo.setIntBuffer(buf);
        buffers.add(ibo);
    }

    public BO getBO(int index) {
        // HACK: if (buffers.isEmpty() ) {System.out.println("Geom. buffer is empty");return null;}
        // HACK: for (BO bo : buffers) System.out.println("Geom. buffer "+bo);
        return buffers.get(index);
    }

} // end of class Geometry
