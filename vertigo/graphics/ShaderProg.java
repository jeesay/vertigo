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

    private HashMap<String, Integer> attributes;
    private HashMap<String, Integer> uniforms;
    private int program;
    private String name;
    private String vshader;
    private String fshader;
    private String gshader;
    private boolean dirty_;
    private String path="shaders/";

    /**
     * Constructs and initializes an empty ShaderProg.
     */
    public ShaderProg() {
        attributes = new HashMap<String, Integer>();
        uniforms = new HashMap<String, Integer>();
        name = null;
        dirty_ = true;
    }

    /**
     * Constructs and initializes a ShaderProg from its name.
     * @param Name of the shader. The vertex, fragment, ang geometry shader
     * sources must be 'name'.vs, 'name'.fs, 'name'.gs, respectively.
     */
    public ShaderProg(String name) {
        attributes = new HashMap<String, Integer>();
        uniforms = new HashMap<String, Integer>();
        this.name = name;
        dirty_ = true;
    }

    public boolean isDirty() {
        return dirty_;
    }

    public void setDirty(boolean flag) {
        dirty_ = flag;
    }

    public void loadVertexShader() {
        vshader = loadShader(this.name + ".vs");
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
        uniforms.put(name, -1);
    }

    public void setUniformLocation(String name, int uniformToken) {
        uniforms.put(name, uniformToken);
    }

    public ArrayList<String> getAllUniforms() {
        return new ArrayList(uniforms.keySet());
    }


    public int getUniformLocation(String key) {
        return uniforms.get(key);
    }

    public void addAttribute(String attr){
        attributes.put(attr, -1);
    }

    public void setAttributeLocation(String name, int attributeToken) {
        attributes.put(name, attributeToken);
    }

    public ArrayList<String> getAllAttributes() {
        return new ArrayList(attributes.keySet());
    }


    public int getAttributeLocation(String key) {
        return attributes.get(key);
    }

    /**
     * Loads the shaders
     * We assume that the shader is a file located in the  JAR file
     * in the subpackage shaders
     */
    private String loadShader(String name) {
        StringBuilder sb = new StringBuilder();
        try {
            InputStream is = ShaderFactory.class.getResourceAsStream(path+name);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line).append('\n');
            }
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        System.out.println("Shader is <Path shader> " + (path+name) +'\n'+ sb.toString());
        return sb.toString();
    }
}


