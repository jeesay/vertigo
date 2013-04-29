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
 *Florin Buga
 *Olivier Catoliquot
 *Clement Delestre
 */

package vertigo.graphics.lwjgl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

import ij.IJ;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import vertigo.graphics.Attribute;
import vertigo.graphics.BufferTools;
import vertigo.graphics.ShaderProg;
import vertigo.graphics.Uniform;

public final class ShaderUtils {

    public String[] vsrc;
    public String[] fsrc;

    // loads the shaders
    // in this example we assume that the shader is a file located in the applications JAR file.
    //
/****  TEST de PUSH
 * 
    public static String[] loadShader(String name) {
        StringBuilder sb = new StringBuilder();
        System.out.println(name);
        try {
            InputStream is = ShaderFactory.class.getResourceAsStream(name);
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
***/

    // This compiles and loads the shader to the video card.
    // if there is a problem with the source the program will exit at this point.
    //
    public static int attachShaders(String vertexCode, String fragmentCode) throws Exception {
        int vertexShaderProgram;
        int fragmentShaderProgram;
        int shaderprogram;
        vertexShaderProgram = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        fragmentShaderProgram = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        GL20.glShaderSource(vertexShaderProgram, vertexCode);
        GL20.glCompileShader(vertexShaderProgram);
        GL20.glShaderSource(fragmentShaderProgram, fragmentCode);
        GL20.glCompileShader(fragmentShaderProgram);
        shaderprogram = GL20.glCreateProgram();
        //
        GL20.glAttachShader(shaderprogram, vertexShaderProgram);
        GL20.glAttachShader(shaderprogram, fragmentShaderProgram);
        GL20.glLinkProgram(shaderprogram);
        GL20.glValidateProgram(shaderprogram);
        IntBuffer intBuffer = IntBuffer.allocate(1);
        GL20.glGetProgram(shaderprogram, GL20.GL_LINK_STATUS, intBuffer);

        if (intBuffer.get(0) != 1) {
            GL20.glGetProgram(shaderprogram, GL20.GL_INFO_LOG_LENGTH, intBuffer);
            int size = intBuffer.get(0);
            System.err.println("Program link error: ");
            if (size > 0) {
                ByteBuffer byteBuffer = ByteBuffer.allocate(size);
                GL20.glGetProgramInfoLog(shaderprogram, intBuffer, byteBuffer);
//                for (byte b : byteBuffer.array()) {
//                    System.err.print((char) b);
//                }
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
    public static void useShader(int shaderprogram) {
        GL20.glUseProgram(shaderprogram);
    }

    // when you have finished drawing everything that you want using the shaders,
    // call this to stop further shader interactions.
    public void dontUseShader() {
        GL20.glUseProgram(0);
    }

      public static ShaderProg updateShader(ShaderProg prog) {
        int progID = -1;
        System.out.println("updateShader:\n"+prog.getVertexSource() + "\n------\n"+prog.getFragmentSource());
        if (prog.isDirty()) {
            try {
                System.out.println("AttachShaders");
                progID = attachVFShaders(prog);
            } catch (Exception ex) {
                System.out.println("Exception attachShaders");
                Logger.getLogger(LWJGL_Renderer.class.getName()).log(Level.SEVERE, null, ex);
                // Renderer or LWJGL_Renderer ?
    
            }
            prog.setHandle(progID);

            GL20.glUseProgram(progID);

            for (Uniform uniform : prog.getAllUniforms()) {
                System.out.println("uniform "+uniform.getName());
                prog.setUniformLocation(uniform.getName(),GL20.glGetUniformLocation(progID, uniform.getName()));
            }
            for (Attribute attribute : prog.getAllAttributes()) {
                System.out.println("attr "+attribute.getName());
                prog.setAttributeLocation(attribute.getName(), GL20.glGetAttribLocation(progID, attribute.getName()) );
            }
            GL20.glUseProgram(0);

        }
          return prog;
    }

    // This compiles and loads the shader to the video card.
    // if there is a problem with the source the program will exit at this point.
    //
    private static int attachVFShaders(ShaderProg prog) throws Exception {
        System.out.println("attachVFShader");

        int vertexShaderProgram;
        int fragmentShaderProgram;
        int shaderprogram;
        vertexShaderProgram = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        fragmentShaderProgram = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        
        GL20.glShaderSource(vertexShaderProgram, prog.getVertexSource());
        GL20.glCompileShader(vertexShaderProgram);
        if (GL20.glGetShaderi(vertexShaderProgram, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE)
            System.err.println("Compile error of vertexshader");

        GL20.glShaderSource(fragmentShaderProgram, prog.getFragmentSource());
        GL20.glCompileShader(fragmentShaderProgram);
        if (GL20.glGetShaderi(fragmentShaderProgram, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE)
            System.err.println("Compile error of fragmentshader");

        //
        shaderprogram = GL20.glCreateProgram();

        GL20.glAttachShader(shaderprogram, vertexShaderProgram);
        GL20.glAttachShader(shaderprogram, fragmentShaderProgram);
        GL20.glLinkProgram(shaderprogram);
        GL20.glValidateProgram(shaderprogram);

        if (GL20.glGetProgrami(shaderprogram, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {

            IntBuffer intBuffer = BufferTools.newIntBuffer(1);
            GL20.glGetProgram(shaderprogram, GL20.GL_INFO_LOG_LENGTH, intBuffer);

            int size = intBuffer.get(0);
            System.err.println("Program link error: "+size );
            if (size > 0) {
                ByteBuffer byteBuffer = BufferTools.newByteBuffer(size);
                GL20.glGetProgramInfoLog(shaderprogram, intBuffer, byteBuffer);
                byteBuffer.rewind();
                byte[] bytearray = new byte[byteBuffer.remaining()];
                byteBuffer.get(bytearray);
                System.err.println(bytearray.length);
                String s = new String(bytearray,0,bytearray.length-1,Charset.forName("UTF-8") );
                System.err.print("<<"+s+">>");
/***
                for (byte b : bytearray) {
                    System.err.print(b+";");
                }
***/
                System.err.println(" - End");
            } else {
                System.out.println("Unknown");
            }
            System.exit(1);
        }
                System.out.println("End of attachVFShader");
        return shaderprogram;

    }


} // End of class ShaderUtils
