/**
 * $Id:$
 *
 * Vertigo: 3D Viewer Plugin for ImageJ. Copyright (C) 2013 Jean-Christophe
 * Taveau.
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
import vertigo.graphics.BufferData;
import vertigo.graphics.VBO;
import vertigo.graphics.BufferTools;
import vertigo.graphics.PackedVBO;
import vertigo.graphics.IBO;

public class Geometry {

    private ArrayList<BO> buffers;
    private BufferData buffdata;

    public Geometry() {
        buffers = new ArrayList<BO>();
        System.out.println("Une geometry est créée.");
    }

    public void presente() {
        System.out.println("number of BO " + buffers.size() + " size of :  BufferData " + buffdata.getCapacity());

    }

    public BufferData getBufferData() {
        return buffdata;
    }

    public ArrayList<BO> getBuffers() {
        return buffers;
    }

    public int getCount() {
        return buffdata.getCapacity();
    }

    public void addBuffert(String[] types, float[] data) {
        buffdata = new BufferData(data);
        int stride = 0;
        int offset = 0;
        for (int i = 0; i < types.length; i++) {
            stride += getSize(types[i]);
        }


        for (int i = 0; i < types.length; i++) {
            VBO vbo = new VBO(stride, offset, types[i]);
            vbo.setBuffData(buffdata);
            buffers.add(vbo);
            offset += getSize(types[i]);
        }
    }

    public int getNumberBO() {
        return buffers.size();
    }

    private int getSize(String type) {
        if (type.contains("3")) {
            return 3;
        } else if (type.contains("2")) {
            return 2;
        }
        return 4;
    }

    /**
     * Sets the geometry
     *
     * @param type - Data type: V3F,C3F,T2F,T3F, N3F,etc.
     * @param primitives - an array containing the data for each vertex
     * (coordinates, normal, colors,etc.).
     */
    public void addBuffer(String type, float[] data) {
        // FloatBuffer buf = BufferTools.newFloatBuffer(data.length);
        //buf.put(data);
        //buf.rewind();

        VBO vbo = new VBO(type);
        //vbo.setFloatBuffer(type, buf);

        System.out.println("VBO created !!!");
        buffdata = new BufferData(data);
        vbo.setBuffData(buffdata);
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
    public void addBuffer(String[] types, float[] data) {
        FloatBuffer buf = BufferTools.newFloatBuffer(data.length);
        buf.put(data);
        buf.rewind();
        VBO vbo = new VBO();
        vbo.setFloatBuffer(types, buf);
        buffers.add(vbo);
    }

    /**
     * Sets the geometry
     *
     * @param type - Data type: V3F,C3F,T2F,T3F, N3F,etc.
     * @param primitives - an array containing the data for each vertex
     * (coordinates, normal, colors,etc.).
     */
    public void setVertices(String type, float[] data) {
        addBuffer(type, data);
    }

    public void addIndices(int[] indexes) {
        IntBuffer buf = BufferTools.newIntBuffer(indexes.length);
        buf.put(indexes);
        buf.rewind();
        IBO ibo = new IBO();
        ibo.setIntBuffer(buf);
        System.out.println("IBO ajouté");
        buffers.add(ibo);
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
        addBuffer(types, data);
    }

    public void setIndices(int[] indexes) {
        addIndices(indexes);
    }

    public BO getBO(int index) {
        // HACK: if (buffers.isEmpty() ) {System.out.println("Geom. buffer is empty");return null;}
        // HACK: for (BO bo : buffers) System.out.println("Geom. buffer "+bo);
        return buffers.get(index);
    }
} // end of class Geometry
