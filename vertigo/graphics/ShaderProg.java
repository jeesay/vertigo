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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import ij.IJ;

/**
 *
 * @author Jean-Christophe Taveau
 */
public class ShaderProg {

    public static int UNKNOWN = -1;
    //private HashMap<String, Attribute> attributes;
    //private HashMap<String, Uniform> uniforms;
    private ArrayList< Attribute> attributes;
    private ArrayList<Uniform> uniforms;
    private int program;
    private String name;
    private String vshader;
    private String fshader;
    private String gshader;
    private boolean dirty_;
    private String path = "shaders/";

    /**
     * Constructs and initializes an empty ShaderProg.
     */
    public ShaderProg() {
        //attributes = new HashMap<String, Attribute>();
        //uniforms = new HashMap<String, Uniform>();
        attributes = new ArrayList< Attribute>();
        uniforms = new ArrayList<Uniform>();
        name = null;
        dirty_ = true;
        program=-1;
    }

    /**
     * Constructs and initializes a ShaderProg from its name.
     *
     * @param Name of the shader. The vertex, fragment, ang geometry shader
     * sources must be 'name'.vs, 'name'.fs, 'name'.gs, respectively.
     */
    public ShaderProg(String name) {
        // attributes = new HashMap<String, Attribute>();
        //uniforms = new HashMap<String, Uniform>();
        attributes = new ArrayList< Attribute>();
        uniforms = new ArrayList<Uniform>();
        this.name = name;
        dirty_ = true;
        program=-1;
        System.out.println("Shader prog is creat : "+name);
    }

    public boolean isDirty() {
        return dirty_;
    }

    public void setDirty(boolean flag) {
        dirty_ = flag;
    }

    public void loadVertexShader() {
        System.out.println("The name 1 : "+name);
        vshader = loadShader(this.name + ".vs");
        System.out.println("The name  2 : "+name);
    }

    public void loadFragmentShader() {
        fshader = loadShader(this.name + ".fs");
    }

    public void loadGeometryShader() {
        gshader = loadShader(this.name + ".gs");
    }

    public String getVertexSource() {
        IJ.log("getVertexSource");
        return vshader;
    }

    public String getFragmentSource() {
        IJ.log("getFragmentSource");
        return fshader;
    }

    public String getGeometrySource() {
        return gshader;
    }

    public int getHandle() {
        return program;
    }

    public void setHandle(int prgm) {
        program = prgm;
        // the shader program is now completed
        setDirty(false);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addUniform(String name) {
        //niforms.put(new Uniform(name), -1);
        uniforms.add(new Uniform(name));
    }

    public void addUniform(String name, String type) {
        //niforms.put(new Uniform(name), -1);
        uniforms.add(new Uniform(name, type));
    }

    public void setUniformLocation(String name, int uniformToken) {
        //uniforms.put(name, uniformToken);
        uniforms.add(new Uniform(name, uniformToken));
    }

    /*public ArrayList<String> getAllUniforms() {
     return new ArrayList(uniforms.keySet());
     }*/
    public ArrayList<Uniform> getAllUniforms() {
        return uniforms;
    }

    public int getUniformLocation(String key) {
        //return uniforms.get(key);
        for (Uniform uni : uniforms) {
            if (uni.getName().equals(key)) {
                return uni.getHandle();
            }
        }
        return -1;
    }

    public void addAttribute(String attr) {
        //attributes.put(attr, -1);
        attributes.add(new Attribute(name));
    }

    public void addAttribute(String attr, String type) {
        //attributes.put(attr, -1);
        attributes.add(new Attribute(name, type));
    }

    public void setAttributeLocation(String name, int attributeToken) {
        //attributes.put(name, attributeToken);
        attributes.add(new Attribute(name, attributeToken));
    }

    /*   public ArrayList<String> getAllAttributes() {
     return new ArrayList(attributes.keySet());
     }
     */
    public ArrayList<Attribute> getAllAttributes() {
        return attributes;
    }

    public int getAttributeLocation(String key) {
        //return attributes.get(key);
        for (Attribute atri : attributes) {
            if (atri.getName().equals(key)) {
                return atri.getHandle();
            }
        }
        return -1;
    }

    /**
     * Loads the shaders We assume that the shader is a file located in the JAR
     * file in the subpackage shaders
     */
    private String loadShader(String name) {
        System.out.println("We load "+name);
        StringBuilder sb = new StringBuilder();
        try {
            System.out.println("We try");
            InputStream is = ShaderFactory.class.getResourceAsStream(path + name);
            if(is==null){
                System.out.println("IS "+name+" is null");
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            //System.out.println("BR is : "+br.readLine());
            while ((line = br.readLine()) != null) {
                sb.append(line).append('\n');
            }
            is.close();
        } catch (Exception e) {
            System.out.println("We catch.");
            e.printStackTrace();
        }
       System.out.println("Shader is <Path shader> " + (path+name) +'\n'+ sb.toString());
        return sb.toString();
    }
} // end of class ShaderProg

