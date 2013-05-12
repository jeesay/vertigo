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

import vertigo.graphics.ShaderProg;
import vertigo.graphics.ShaderFactory;
/**
 * Class Material
 *
 * @author Florin Buga
 * @author Olivier Catoliquot
 * @author Clement Delestre
 * @version 0.1
 *
 */
public class Material {

    private float[] color;
    private ShaderProg shader;
    
    public Material() {
        shader = ShaderFactory.get("monochrome");
        color=new float[4];
        this.setColor(0.0f,0.0f,0.0f);
    }
 /**
     * Sets the shader
     *
     * @param shader's name
     */
    public void setShaderMaterial(String shaderName) {
        shader = ShaderFactory.get(shaderName);
    }
    /**
     * Gets the shader
     *
     * @return ShaderProg
     */
    public ShaderProg getShaderMaterial() {
        return shader;
    }
    
       /**
     * Sets the Color (RGB format)
      * @param  red : float value between 0 and 1
     * @param  green : float value between 0 and 1
     * @param  blue : float value between 0 and 1
     */
    
    public void setColor(float red, float green, float blue) {
        color[0] = red;
        color[1] = green;
        color[2] = blue;
        color[3] = 1.0f;
    }
 /**
     * Sets the Color (RGBA format)
     *
     * @param  red : float value between 0 and 1
     * @param  green : float value between 0 and 1
     * @param  blue : float value between 0 and 1
     * @param alpha : float value between 0 and 1
     */
    public void setColor(float red, float green, float blue, float alpha) {
        color[0] = red;
        color[1] = green;
        color[2] = blue;
        color[3] = alpha;
    }
 /**
     * Gets the Material's color
     *
     * @return  color[]
     */
    public float[] getColor() {
        return color;
    }
    
} // end of class Material
