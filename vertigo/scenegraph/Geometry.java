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
import vertigo.graphics.IBO;
/**
 * Class Camera
 *
 * @author Florin Buga
 * @author Olivier Catoliquot
 * @author Clement Delestre
 * @author Jean-Christophe Taveau
 * @version 0.1
 *
 */
public class Geometry {
/**
 * A geometry is composed of several BO.
 * @see Geometry#addBuffer(java.lang.String, float[]) 
 */
    private ArrayList<BO> boList;
    /**
     * The data of each BO.
     * @see Geometry#getBufferData() 
     */
    private ArrayList<BufferData> buffList;

    public Geometry() {
        boList = new ArrayList<BO>();
        buffList = new ArrayList<BufferData>();
    }

    public ArrayList<BufferData> getBufferData() {
        return buffList;
    }

    public ArrayList<BO> getAllBO() {
        return boList;
    }

    public int getBufferCount() {
        return buffList.size();
    }

/***
OBSOLETE
    public void addBuffert(String[] types, float[] data) {
        BufferData buff = new BufferData(data);
        int stride = 0;
        int offset = 0;
        for (int i = 0; i < types.length; i++) {
            stride += getSize(types[i]);
        }
        for (int i = 0; i < types.length; i++) {
            VBO vbo = new VBO(stride, offset, types[i]);
            vbo.setBuffData(buff);
            boList.add(vbo);
            offset += getSize(types[i]);
        }
    }
***/

    /**
     * Gets the number of BO (VBO & IBO)
     * @return number of BO
     */
    public int getNumberBO() {
        return boList.size();
    }

    /**
     * Sets the geometry
     *
     * @param type - Data type: V3F,C3F,T2F,T3F, N3F,etc.
     * @param primitives - an array containing the data for each vertex
     * (coordinates, normal, colors,etc.).
     */
    public void addBuffer(String type, float[] data) {
        VBO vbo = new VBO(type,0,getSize(type) );
        BufferData buf = new BufferData(data);
        vbo.setBufferData(buf);
        boList.add(vbo);
        buffList.add(buf);
    }

    /**
     * Sets the whole geometry in one single buffer. Ex: X Y Z R G B X Y Z R ...
     * May contain various types of data: XYZ-coordinates, Normals, Colors,
     * UV-TexCoords,etc.
     *
     * @param types - Data type: V3F,C3F,T2F,T3F, N3F,etc.
     * @param primitives - an array containing the data for each vertex
     * (coordinates, normal, colors,etc.).
     * @param contents - a String containing the various data type present in
     * the primitives array. Ex: "[V3F,C3F,N3F,T2F]" corresponds to
     * Vertex, Color, Normal and finally TexCoords data for each vertex.
     */
    public void addBuffer(String[] types, float[] data) {
        BufferData buf = new BufferData(data);
        buffList.add(buf);

        // Calc stride
        int stride = 0;
        for (int i = 0; i < types.length; i++) {
            stride += getSize(types[i]);
        }
        int offset = 0;
        for (int i = 0; i < types.length; i++) {
            VBO vbo = new VBO(types[i], offset, stride );
            vbo.setBufferData(buf);
            boList.add(vbo);
            offset += getSize(types[i]);
        }

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
  /**
     * Sets indices
     *
     * @param indices int[]
     */
    public void addIndices(int[] indexes) {
        IntBuffer buf = BufferTools.newIntBuffer(indexes.length);
        buf.put(indexes);
        buf.rewind();
        IBO ibo = new IBO();
        ibo.setIntBuffer(buf);
        boList.add(ibo);
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
  /**
     * Sets indices
     *
     * @param indices int[]
     */
    public void setIndices(int[] indexes) {
        addIndices(indexes);
    }

    public BO getBO(int index) {
        // HACK: if (boList.isEmpty() ) {System.out.println("Geom. buffer is empty");return null;}
        // HACK: for (BO bo : boList) System.out.println("Geom. buffer "+bo);
        return boList.get(index);
    }

    private int getSize(String type) {
        if (type.contains("3")) {
            return 3;
        } else if (type.contains("2")) {
            return 2;
        }
        return 4;
    }


} // end of class Geometry
