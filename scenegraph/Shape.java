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
    Geometry geo;
    Material material;

    public Shape(){
	super();
        geo = new Geometry();
        material = new Material();
    }
    
    public void setGeometry(String type, float[] data) {
    	// check if the string 'type' contains comma -> packedGeometry otherwise single Geometry
    	if (isPacked(type) ) 
    		geo.setPackedGeometry(type,data);
    	else
    		geo.setGeometry(type,data);
    	
    }
    
    public void setIndexedGeometry(String type, float[] data, int[] indices) {
    	// check if the string 'type' contains comma -> packedGeometry otherwise single Geometry
    	if (isPacked(type) ) 
    		geo.setPackedGeometry(type,data);
    	else
    		geo.setGeometry(type,data);
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
    
    
} // End of class Shape
