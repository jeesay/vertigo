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
            case 1079: // monochrome
                if (table.get(1079) == null) {
                    table.put(1079, create_monochrome());
                    System.out.println("We create a monochrome " + name);
                }
                shader = table.get(1079);
                break;
            case 423: // flat
                if (table.get(423) == null) {
                    table.put(423, create_flat());
                    System.out.println("We create a flat " + name);
                }
                shader = table.get(423);
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
        shader.addUniform("M_Matrix", "matrix");
        shader.addUniform("V_Matrix", "view_matrix");
        shader.addUniform("P_Matrix", "proj_matrix");
        shader.addUniform("uColor", "C4F");
        shader.addAttribute("aVertexPosition", "V3F");

        return shader;
    }

    private static ShaderProg create_flat() {
        ShaderProg shader = new ShaderProg("monochrome");
        System.out.println("We create a monochrome here");
        shader.loadVertexShader();
        shader.loadFragmentShader();
        shader.addUniform("M_Matrix", "matrix");
        shader.addUniform("V_Matrix", "view_matrix");
        shader.addUniform("P_Matrix", "proj_matrix");
        shader.addUniform("uColor", "C4F");
        shader.addAttribute("aVertexPosition", "V3F");

        return shader;
    }
}// end of class ShaderFactory