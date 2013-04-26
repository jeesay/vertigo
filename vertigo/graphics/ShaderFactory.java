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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ShaderFactory {

    /**
     *
     * @author Jean-Christophe Taveau
     */
    private static HashMap<Integer, ShaderProg> table = new HashMap<Integer, ShaderProg>();

    public static ShaderProg get(String name) {
        int index = calcIndex(name);
        ShaderProg shader;
        switch (index) {
            case 741: // default
                if (table.get(741) == null) {
                    table.put(741, create_monochrome());
                }
                shader = table.get(741);
                break;
            case 423: // flat
                shader = new ShaderProg("flat");
                shader.loadVertexShader();
                shader.loadFragmentShader();
                shader.addUniform("uModelMatrix", "matrix");
                shader.addUniform("uViewMatrix", "view_matrix");
                shader.addUniform("uProjMatrix", "proj_matrix");
                shader.addAttribute("aVertexPosition", "V3F");
                break;
            case 540: // phong
                shader = new ShaderProg("phong");
                shader.loadVertexShader();
                shader.loadFragmentShader();
                shader.addUniform("uModelMatrix", "matrix");
                shader.addUniform("uViewMatrix", "view_matrix");
                shader.addUniform("uProjMatrix", "proj_matrix");
                shader.addAttribute("aVertexPosition", "V3F");
                break;
            default: // Do nothing  
                shader = new ShaderProg();
        }
        return shader;
    }

    /**
     * Calc index by summing all the Unicode values in the string 'name'
     *
     * @param name
     * @return index
     */
    private static int calcIndex(String name) {
        int index = 0;
        for (int i = 0; i < name.length(); i++) {
            index += (int) name.charAt(i);
        }
        return index;
    }

    private static ShaderProg create_monochrome() {
        ShaderProg shader = new ShaderProg("monochrome");
        shader.loadVertexShader();
        shader.loadFragmentShader();
        shader.addUniform("uModelMatrix", "matrix");
        shader.addUniform("uViewMatrix", "view_matrix");
        shader.addUniform("uProjMatrix", "proj_matrix");
        shader.addUniform("uColor", "C4F");
        shader.addAttribute("aVertexPosition", "V3F");

        return shader;
    }
}// end of class ShaderFactory