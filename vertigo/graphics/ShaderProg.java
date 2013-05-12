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
 * @version 0.1
 */
public class ShaderProg {

    /**
     * Checks if ShaderProg is bound.
     *
     */
    public static int UNKNOWN = -1;
    /**
     * The ArrayList's attributes
     *
     * @see ShaderProg#addAttribute(java.lang.String)
     * @see ShaderProg#addAttribute(java.lang.String, java.lang.String)
     * @see ShaderProg#getAllAttributes()
     * @see ShaderProg#getAttributeLocation(java.lang.String)
     * @see ShaderProg#setUniformLocation(java.lang.String, int)
     */
    private ArrayList< Attribute> attributes;
    /**
     * The ArrayList uniforms
     *
     * @see ShaderProg#addUniform(java.lang.String)
     * @see ShaderProg#addUniform(java.lang.String, java.lang.String)
     * @see ShaderProg#getAllUniforms()
     * @see ShaderProg#getUniformLocation(java.lang.String)
     * @see ShaderProg#setUniformLocation(java.lang.String, int)
     */
    private ArrayList<Uniform> uniforms;
    /**
     * The handle of ShaderProg
     *
     * @see ShaderProg#setHandle(int)
     * @see ShaderProg#ShaderProg()
     * @see ShaderProg#ShaderProg(java.lang.String)
     * @see ShaderProg#getHandle()
     * @see ShaderProg#setHandle(int)
     */
    private int program;
    /**
     * The name of ShaderProg
     *
     * @see ShaderProg#setName(java.lang.String)
     * @see ShaderProg#getName()
     */
    private String name;
    /**
     * The Vertex shader
     *
     * @see ShaderProg#getVertexSource()
     * @see ShaderProg#loadVertexShader()
     */
    private String vshader;
    /**
     * The fragment shader
     *
     * @see ShaderProg#getFragmentSource()
     * @see ShaderProg#loadFragmentShader()
     */
    private String fshader;
    /**
     * The geometry shader
     *
     * @see ShaderProg#getGeometrySource()
     * @see ShaderProg#loadGeometryShader()
     */
    private String gshader;
    /**
     * Checks if ShaderProg is dirty
     *
     * @see ShaderProg#isDirty()
     * @see ShaderProg#setDirty(boolean)
     * @see ShaderProg#ShaderProg()
     * @see ShaderProg#ShaderProg(java.lang.String)
     */
    private boolean dirty_;
    private String path = "shaders/";

    /**
     * Constructs and initializes an empty ShaderProg.
     */
    public ShaderProg() {
        attributes = new ArrayList< Attribute>();
        uniforms = new ArrayList<Uniform>();
        name = null;
        dirty_ = true;
        program = -1;
    }

    /**
     * Constructs and initializes a ShaderProg from its name.
     *
     * @param Name name of the shader. The vertex, fragment, ang geometry shader
     * sources must be 'name'.vs, 'name'.fs, 'name'.gs, respectively.
     */
    public ShaderProg(String name) {

        attributes = new ArrayList< Attribute>();
        uniforms = new ArrayList<Uniform>();
        this.name = name;
        dirty_ = true;
        program = -1;
    }

    /**
     *
     * @return boolean
     */
    public boolean isDirty() {
        return dirty_;
    }

    /**
     *
     * @param flag set dirty to flag value
     */
    public void setDirty(boolean flag) {
        dirty_ = flag;
    }

    /**
     * Loads vertex shader
     */
    public void loadVertexShader() {
        vshader = loadShader(this.name + ".vs");
    }

    /**
     * Loads fragment shader
     */
    public void loadFragmentShader() {
        fshader = loadShader(this.name + ".fs");
    }

    /**
     * Loads geometry shader
     */
    public void loadGeometryShader() {
        gshader = loadShader(this.name + ".gs");
    }

    /**
     *
     * @return vertex shader
     */
    public String getVertexSource() {
        IJ.log("getVertexSource");
        return vshader;
    }

    /**
     *
     * @return fragment shader
     */
    public String getFragmentSource() {
        IJ.log("getFragmentSource");
        return fshader;
    }

    /**
     *
     * @return geometry shader
     */
    public String getGeometrySource() {
        return gshader;
    }

    /**
     *
     * @return ShaderProg handle
     */
    public int getHandle() {
        return program;
    }

    /**
     *
     * @param prgm Integer set to the ShaderProg handle
     */
    public void setHandle(int prgm) {
        program = prgm;
        // the shader program is now completed
        setDirty(false);
    }

    /**
     *
     * @return name String of name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name String
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Adds a new uniform shader to uniforms
     *
     * @param name String
     */
    public void addUniform(String name) {
        uniforms.add(new Uniform(name));
    }

    /**
     * Adds a new uniform shader to uniforms
     *
     * @param name String
     * @param type String
     */
    public void addUniform(String name, String type) {
        uniforms.add(new Uniform(name, type));
    }

    /**
     * Adds a new uniform shader to uniforms
     *
     * @param name String
     * @param uniformToken Integer
     */
    public void setUniformLocation(String name, int uniformToken) {
        uniforms.add(new Uniform(name, uniformToken));
    }

    /**
     *
     * @return ArrayList uniforms
     */
    public ArrayList<Uniform> getAllUniforms() {
        return uniforms;
    }

    public int getUniformLocation(String key) {
        for (Uniform uni : uniforms) {
            if (uni.getName().equals(key)) {
                return uni.getHandle();
            }
        }
        return -1;
    }

    /**
     * Adds attribute name to ArrayList attributes
     *
     * @param attr String
     */
    public void addAttribute(String attr) {
        attributes.add(new Attribute(name));
    }

    /**
     * Adds attribute name to ArrayList attributes
     *
     * @param attr String
     * @param type String
     */
    public void addAttribute(String attr, String type) {
        attributes.add(new Attribute(attr, type));
    }

    /**
     * @param name String
     * @param attributeToken Integer
     */
    public void setAttributeLocation(String name, int attributeToken) {
        attributes.add(new Attribute(name, attributeToken));
    }

    /**
     *
     * @return ArrayList attribute
     */
    public ArrayList<Attribute> getAllAttributes() {
        return attributes;
    }

    /**
     *
     * @param key String
     * @return Handle of attribute
     */
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

        StringBuilder sb = new StringBuilder();
        try {

            InputStream is = ShaderFactory.class.getResourceAsStream(path + name);
            if (is == null) {
                System.out.println("IS " + name + " is null");
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line).append('\n');
            }
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Shader is <Path shader> " + (path + name) + '\n' + sb.toString());
        return sb.toString();
    }
} // end of class ShaderProg

