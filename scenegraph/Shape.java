/*
 * $Id:$
 *
 * crazybio_viewer: 3D Viewer Plugin for ImageJ.
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

package vertigo.scenegraph;



public class Shape extends Node {
    protected Geometry geo;
    protected Material material;
public static int count = 0; 
    public Shape(){
	super();
        geo = new Geometry();
        material = new Material();
        name="Shape_n"+count;
        count++;
    }
    
    public void setGeometry(String type, float[] data) {
    	geo.setGeometry(type,data);
        if (type.equals("V3F") )
            bbox.expand(data);
    }
    
    public void setGeometry(String[] types, float[] data) {
        if (types.length == 1 && types[0].equals("V3F") ) {
    		geo.setGeometry(types[0],data);
        	bbox.expand(data);
        }
        else {
    	    geo.setGeometry(types,data);
            int offset = 0; // TODO
            int step = 0; // TODO
            for (int i=offset;i< data.length;i+=step)
        	bbox.expand(data[i],data[i+1],data[i+2]);
        }
    }
    
    public void setIndexedGeometry(String type, float[] data, int[] indices) {
    	setGeometry(type,data);
	setIndices(indices);
    }
    
    public void setIndexedGeometry(String[] types, float[] data, int[] indices) {
    	setGeometry(types,data);
	setIndices(indices);
    }
    
    public void setIndices(int[] indices) {
	geo.setIndices(indices);
    }
    
    public void setColor(float red, float green, float blue, float alpha) {
    	// TODO
    }
    
    public void setShaderMaterial(String shaderName) {
		// TODO
    }
    
    private boolean isPacked(String type) {
    	// TODO
            boolean coma=false;
         coma=type.matches(".*,.*");
    	return coma;
    }
    
    public void setDrawingStyle(String style){
        //TODO
    }
} // End of class Shape
