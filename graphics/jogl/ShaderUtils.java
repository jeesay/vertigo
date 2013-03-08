/*
 * $Id:$
 *
 * Vertigo_viewer: 3D Viewer Plugin for ImageJ.
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

package vertigo.graphics.jogl;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import ij.IJ;


import javax.media.opengl.GL3;

public class ShaderUtils {

  public String[] vsrc;
  public String[] fsrc;

    // loads the shaders
    // in this example we assume that the shader is a file located in the applications JAR file.
    //
    public static String[] loadShader(String name) {
        StringBuilder sb = new StringBuilder();
        System.out.println(name);
        try {
            InputStream is = ShaderUtils.class.getResourceAsStream(name);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while ((line = br.readLine()) != null) {

                sb.append(line);
                sb.append('\n');
            }
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Shader is " + sb.toString());
        return new String[]{sb.toString()};
    }

    // This compiles and loads the shader to the video card.
    // if there is a problem with the source the program will exit at this point.
    //
    public static int attachShaders(GL3 gl, String[] vertexCode, String[] fragmentCode) throws Exception {
        int vertexShaderProgram;
        int fragmentShaderProgram;
        int shaderprogram;
        vertexShaderProgram = gl.glCreateShader(GL3.GL_VERTEX_SHADER);
        fragmentShaderProgram = gl.glCreateShader(GL3.GL_FRAGMENT_SHADER);
        gl.glShaderSource(vertexShaderProgram, 1, vertexCode, null, 0);
        gl.glCompileShader(vertexShaderProgram);
        gl.glShaderSource(fragmentShaderProgram, 1, fragmentCode, null, 0);
        gl.glCompileShader(fragmentShaderProgram);
        shaderprogram = gl.glCreateProgram();
        //
        gl.glAttachShader(shaderprogram, vertexShaderProgram);
        gl.glAttachShader(shaderprogram, fragmentShaderProgram);
        gl.glLinkProgram(shaderprogram);
        gl.glValidateProgram(shaderprogram);
        IntBuffer intBuffer = IntBuffer.allocate(1);
        gl.glGetProgramiv(shaderprogram, GL3.GL_LINK_STATUS, intBuffer);

        if (intBuffer.get(0) != 1) {
            gl.glGetProgramiv(shaderprogram, GL3.GL_INFO_LOG_LENGTH, intBuffer);
            int size = intBuffer.get(0);
            System.err.println("Program link error: ");
            if (size > 0) {
                ByteBuffer byteBuffer = ByteBuffer.allocate(size);
                gl.glGetProgramInfoLog(shaderprogram, size, intBuffer, byteBuffer);
                for (byte b : byteBuffer.array()) {
                    System.err.print((char) b);
                }
            } else {
                System.out.println("Unknown");
            }
            System.exit(1);
        }
        return shaderprogram;
    }

    // this function is called when you want to activate the shader.
    // Once activated, it will be applied to anything that you draw from here on
    // until you call the dontUseShader(GL) function.
    public static void useShader(GL3 gl, int shaderprogram) {
        gl.glUseProgram(shaderprogram);
    }

    // when you have finished drawing everything that you want using the shaders,
    // call this to stop further shader interactions.
    public void dontUseShader(GL3 gl) {
        gl.glUseProgram(0);
    }

      public static ShaderProg updateShader(GL3 gl,ShaderProg prog) {
        int progID = -1;
        if (prog.isDirty()) {
            try {
                progID = attachVFShaders(gl, prog);
            } catch (Exception ex) {
                Logger.getLogger(JOGL_Renderer.class.getName()).log(Level.SEVERE, null, ex);
            }
            prog.setProgram(progID);

            gl.glUseProgram(progID);
            for (Uniform uniform : prog.getAllUniforms()) {
                uniform.setLocation(gl.glGetUniformLocation(progID, uniform.getName()));
            }
            for (Attribute attribute : prog.getAllAttributes()) {
                attribute.setLocation(gl.glGetAttribLocation(progID, attribute.getName()));
            }
            gl.glUseProgram(0);

        }
          return prog;
    }

    // This compiles and loads the shader to the video card.
    // if there is a problem with the source the program will exit at this point.
    //
    private static int attachVFShaders(GL3 gl, ShaderProg prog) throws Exception {
        int vertexShaderProgram;
        int fragmentShaderProgram;
        int shaderprogram;
        vertexShaderProgram = gl.glCreateShader(GL3.GL_VERTEX_SHADER);
        fragmentShaderProgram = gl.glCreateShader(GL3.GL_FRAGMENT_SHADER);
        gl.glShaderSource(vertexShaderProgram, 1, prog.getVertexSource(), null, 0);
        gl.glCompileShader(vertexShaderProgram);
        gl.glShaderSource(fragmentShaderProgram, 1, prog.getFragmentSource(), null, 0);
        gl.glCompileShader(fragmentShaderProgram);
        shaderprogram = gl.glCreateProgram();
        //
        gl.glAttachShader(shaderprogram, vertexShaderProgram);
        gl.glAttachShader(shaderprogram, fragmentShaderProgram);
        gl.glLinkProgram(shaderprogram);
        gl.glValidateProgram(shaderprogram);
        IntBuffer intBuffer = IntBuffer.allocate(1);
        gl.glGetProgramiv(shaderprogram, GL3.GL_LINK_STATUS, intBuffer);

        if (intBuffer.get(0) != 1) {
            gl.glGetProgramiv(shaderprogram, GL3.GL_INFO_LOG_LENGTH, intBuffer);
            int size = intBuffer.get(0);
            System.err.println("Program link error: ");
            if (size > 0) {
                ByteBuffer byteBuffer = ByteBuffer.allocate(size);
                gl.glGetProgramInfoLog(shaderprogram, size, intBuffer, byteBuffer);
                for (byte b : byteBuffer.array()) {
                    System.err.print((char) b);
                }
            } else {
                System.out.println("Unknown");
            }
            System.exit(1);
        }
        return shaderprogram;
    }


} // End of class ShaderUtils
