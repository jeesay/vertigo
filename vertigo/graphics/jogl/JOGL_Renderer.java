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
package vertigo.graphics.jogl;

import com.jogamp.common.nio.Buffers;
import ij.IJ;
import java.nio.IntBuffer;
import java.util.ArrayList;
import javax.media.opengl.GL;
import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import vertigo.graphics.Attribute;
import vertigo.graphics.BO;
import vertigo.graphics.IBO;
import vertigo.graphics.Renderer;
import vertigo.graphics.ShaderProg;
import vertigo.graphics.Uniform;
import vertigo.graphics.UpdateVisitor;
import vertigo.graphics.VBO;
import vertigo.scenegraph.Camera;
import vertigo.scenegraph.Node;
import vertigo.scenegraph.Shape;
import vertigo.scenegraph.World;
import vertigo.scenegraph.Light;
import vertigo.scenegraph.Material;

/**
 * Class JOGL_Renderer
 *
 * @author Florin Buga
 * @author Olivier Catoliquot
 * @author Clement Delestre
 * @version 0.1
 *
 */
public class JOGL_Renderer implements GLEventListener, Renderer {

    // private GLCanvas canvas;
    private Camera cam;
    private World world;
    /**
     * The window's background red component
     *
     * @see JOGL_Renderer#setBackgroundColor(float, float, float)
     */
    private float red;
    /**
     * The window's background green component
     *
     * @see JOGL_Renderer#setBackgroundColor(float, float, float)
     */
    private float green;
    /**
     * The window's background blue component
     *
     * @see JOGL_Renderer#setBackgroundColor(float, float, float)
     */
    private float blue;
    /**
     * The JOGL_Visitor
     *
     * @see JOGL_Renderer#display()
     * @see JOGL_Visitor
     */
    private UpdateVisitor visitor;
    private Camera cam_;
    private ArrayList<Shape> shapes = new ArrayList();
    private ArrayList<Light> lights = new ArrayList();
    private ArrayList<Attribute> attribute = new ArrayList();
    private ShaderProg glshader;
    private boolean isIndexed;
    private VBO vbo3f;
    private IBO ibo;
    private int capacity = 0;

    /**
     * Constructor.
     */
    public JOGL_Renderer() {
        visitor = new UpdateVisitor();
    }

    @Override
    public void setBackgroundColor(float red, float green, float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public void setWorld(World world) {
        this.world = world;
        this.cam = (Camera) world.getNode("camera");
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        // Do nothing
        System.out.println("init");
        loadDrawables(world);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        // Do nothing
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        System.out.println("display");
        GL3 gl = drawable.getGL().getGL3();  // up to OpenGL 3
        gl.glClearColor(red, green, blue, 1.0f);
        gl.glClear(GL3.GL_COLOR_BUFFER_BIT | GL3.GL_DEPTH_BUFFER_BIT);
        // Update matrices via Visitor
        world.accept(visitor);
        for (Shape shape : shapes) {
            // shape
            System.out.println("Here the shape " + shape.getName());
            processShader(drawable, shape.getMaterial());
            // processUniform(drawable, shape);
            drawShape(drawable, shape);
        }
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        // Your OpenGL codes to set up the view port, projection mode and view volume. 
        cam.setViewport(width, height);
//        cam.setAspect(width/height);

    }

    private void update() {
        // nothing to update yet
    }

    private void loadDrawables(Node obj) {
        if (obj instanceof Camera) {
            cam_ = (Camera) obj;
        } else if (obj instanceof Shape) {
            shapes.add((Shape) obj);

        } else if (obj instanceof Light) {
            lights.add((Light) obj);
        }
        for (Node child : obj.getChildren()) {
            loadDrawables(child);
        }
    }

    private void processUniform(GLAutoDrawable drawable, Shape shape) {
        GL3 gl = drawable.getGL().getGL3();
        ArrayList<Uniform> uniforms = glshader.getAllUniforms();
        System.out.println("version " + gl.glGetString(gl.GL_VERSION));
        System.out.println("Here the shape " + shape.getName());
        System.out.println("Size of arrayList : " + uniforms.size());
        for (Uniform uni : uniforms) {
            System.out.println("Handle : " + glshader.getHandle() + " name [" + uni.getName() + "] type : " + uni.getType());
            if (uni.getType().equals("view_matrix")) {
                int vlocation = gl.glGetUniformLocation(glshader.getHandle(), uni.getName());
                System.out.println(" View : " + vlocation);
                gl.glUniformMatrix4fv(vlocation, 16, false, cam_.getViewMatrix().toColumnBuffer());
                // gl.gluniformMatrix4
                System.out.println(cam_.getViewMatrix());
            } else if (uni.getType().equals("proj_matrix")) {
                int plocation = gl.glGetUniformLocation(glshader.getHandle(), uni.getName());
                System.out.println(" Projection : " + plocation);
                gl.glUniformMatrix4fv(plocation, 16, false, cam_.getProjection().toColumnBuffer());
                System.out.println(cam_.getProjection());
            } else if (uni.getType().equals("matrix")) {
                System.out.println("We re here !!! Model matrix ");
                int mlocation = gl.glGetUniformLocation(glshader.getHandle(), uni.getName());
                System.out.println(" Location : " + mlocation);
                gl.glUniformMatrix4fv(mlocation, 16, false, shape.getModelMatrix().toColumnBuffer());
                System.out.println(shape.getModelMatrix());
            }
        }
    }

    private void processShader(GLAutoDrawable drawable, Material material) {
        int handle;
        GL3 gl = drawable.getGL().getGL3();
        glshader = material.getShaderMaterial();
        // compile once
        System.out.println("The handle 1 : " + glshader.getHandle());
        if (glshader.getHandle() == ShaderProg.UNKNOWN) {
            //TEST
            System.out.println("The handle 2 : " + glshader.getHandle());
            try {/*
                 String[] fragment = new String[glshader.getFragmentSource().length()];
                 for (int i = 0; i < glshader.getFragmentSource().length(); i++) {
                 fragment[i] = Character.toString(glshader.getFragmentSource().charAt(i));
                 }*/
                /*      String[] vertex = new String[glshader.getVertexSource().length()];
                 for (int i = 0; i < glshader.getVertexSource().length(); i++) {
                 vertex[i] = Character.toString(glshader.getVertexSource().charAt(i));
                 }


                 String[] fragment = new String[glshader.getFragmentSource().length()];
                 for (int i = 0; i < glshader.getFragmentSource().length(); i++) {
                 fragment[i] = Character.toString(glshader.getFragmentSource().charAt(i));
                 }
                 */
                String[] vertex = glshader.getVertexSource().split(System.getProperty("line.separator"));
                String[] fragment = glshader.getFragmentSource().split(System.getProperty("line.separator"));
                for (int i = 0; i < fragment.length; i++) {
                    System.out.println("fragment : " + fragment[i]);
                }

                // String[] vertex = glshader.getVertexSource().split(System.getProperty("line.separator"));
                //String[] vertex = new String[]{glshader.getVertexSource()};
                System.out.println(" VERTEX SOURCE : " + vertex);
                System.out.println(" FRAGMENT SOURCE : " + fragment);

                handle = ShaderUtils.attachShaders(gl, vertex, fragment, glshader);
                System.out.println("The handle 3 : " + handle);
                glshader.setHandle(handle);
                System.out.println("The handle 4  : " + glshader.getHandle());
            } catch (Exception e) {
                IJ.log("Error with the Shader " + e);
            } //Compile, Link error
        }

    }

    private void drawShape(GLAutoDrawable drawable, Shape obj) {
        GL3 gl = drawable.getGL().getGL3();
        if (obj.isDirty(Node.SHADER)) {
            processShader(drawable, obj.getMaterial());
            System.out.println("The HANDLE : " + glshader.getHandle());
            obj.setDirty(Node.SHADER, false);
        }

        gl.glUseProgram(obj.getMaterial().getShaderMaterial().getHandle());
        processUniform(drawable, obj);

        // boolean isIndexed = false;
        // PreProcessing
        //   if (obj.isDirty(Node.MATRIX)) {
        //  }
        // Geometry: VBO
        //  if (obj.isDirty(Node.VBO)) {
        processBO(obj, drawable);
        obj.setDirty(Node.VBO, false);
        // }
        gl.glUseProgram(0);

    }

    private void processBO(Shape obj, GLAutoDrawable drawable) {

        GL3 gl = drawable.getGL().getGL3();
        int nbBO = obj.getGeometry().getNumberBO(); //return 2 for the cube (1 vbo + 1 ibo)

        System.out.println("Number of BO is : " + nbBO);
        /*
         //int[] buffer = new int[nbBO]; 
         IntBuffer buffer = IntBuffer.allocate(nbBO); */
        // Generate VBO

        IntBuffer buf = Buffers.newDirectIntBuffer(nbBO);
        //IntBuffer buf = BufferUtils.createIntBuffer(nbBO);
        System.out.println("(again)Number of BO is : " + nbBO + " buf : " + buf);
        gl.glGenBuffers(nbBO, buf);

        int i = 0;
        int bytesPerFloat = Float.SIZE / Byte.SIZE;
        int bytesPerInt = Integer.SIZE / Byte.SIZE;
        System.out.println("Debugage : " + i + " " + bytesPerFloat + " " + bytesPerInt + " " + buf);

        for (BO bo : obj.getGeometry().getAllBO()) {
            System.out.println("For loop");
            int boHandle = buf.get(i);
            // get ID
            System.out.println("Handle is : " + boHandle);
            bo.setHandle(boHandle);
            i++;
            // System.out.println("Buffer test : " + buffer[i]);

            if (bo instanceof IBO) {
                isIndexed = true;
                System.out.println("Here IBO ! ");
                ibo = (IBO) bo;
                int numBytes = ibo.getIntBuffer().capacity() * bytesPerInt;

                gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, boHandle);
                gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, numBytes, ibo.getIntBuffer(), GL.GL_STATIC_DRAW);

                /*
                 // bind a buffer
                 //gl.glBindBuffer(gl.GL_ELEMENT_ARRAY_BUFFER, handle);
                 System.out.println("Bind buffer IBO ");
                 gl.glBufferData(gl.GL_ELEMENT_ARRAY_BUFFER, numBytes, ibo.getIntBuffer(), gl.GL_STATIC_DRAW);
                 System.out.println("Buffer data IBO ");
                 // release ibo
                 gl.glBindBuffer(gl.GL_ELEMENT_ARRAY_BUFFER, 0);
                 System.out.println("Released IBO ! ");*/
            } else {
                System.out.println("Here VBO ! ");
                VBO vbo = (VBO) bo;
                //enable(vbo);
                gl.glBindBuffer(GL.GL_ARRAY_BUFFER, boHandle);
                gl.glBufferData(GL.GL_ARRAY_BUFFER, vbo.capacity() * 4, vbo.getFloatBuffer(), GL.GL_STATIC_DRAW);
                // pointer(vbo, bytesPerFloat);
                attribute = glshader.getAllAttributes();

                for (Attribute attrib : attribute) {
                    int alocation = gl.glGetAttribLocation(glshader.getHandle(), attrib.getName());
                    System.out.println(" alocation : " + alocation + " name " + attrib.getName());
                    gl.glEnableVertexAttribArray(alocation);
                    gl.glVertexAttribPointer(alocation, attrib.getSize(), gl.GL_FLOAT, false, vbo.getStride() * bytesPerFloat, 0L);
                }

                //  gl.glVertexPointer(vbo.getSize(), GL.GL_FLOAT, vbo.getStride() * bytesPerFloat, vbo.getSize() * bytesPerFloat);




                /*
                 int numBytes = vbo.getFloatBuff().capacity() * bytesPerFloat;

                 System.out.println(" The VBO is stride : " + vbo.getStride() + " offset :  " + vbo.getOffset());
                 // bind a buffer
                 //  gl.glBindBuffer(gl.GL_ARRAY_BUFFER, handle);
                 System.out.println(" test --");
                 gl.glVertexPointer(vbo.getSize(), gl.GL_FLOAT, vbo.getStride(), 0);

                 gl.glBufferData(gl.GL_ELEMENT_ARRAY_BUFFER, numBytes, vbo.getFloatBuff(), gl.GL_STATIC_DRAW);

                 System.out.println(" test again *--");
                 // release vbo
                 gl.glBindBuffer(gl.GL_ARRAY_BUFFER, 0);
                 // GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
                 System.out.println("Released VBO");*/


            }
            System.out.println("Here for loop ! i=" + i);
        }


        if (isIndexed) { // draw with index
            gl.glDrawElements(getOpenGLStyle(obj.getDrawingStyle()), ibo.getSize(), GL.GL_UNSIGNED_INT, 0L);

        } else {
            gl.glDrawArrays(getOpenGLStyle(obj.getDrawingStyle()), 0, capacity / vbo3f.getStride());
        }
    }

    private int getOpenGLStyle(String vertigo_style) {
        int style = calcIndex(vertigo_style);
        switch (style) {
            case 379: // LINES
                return GL.GL_LINES;
            /*   case 1116: // LINES_ADJACENCY
             return GL.GL_LINES_ADJACENCY;*/
            case 705: // LINE_LOOP
                return GL.GL_LINE_LOOP;
            case 793: // LINE_STRIP
                return GL.GL_LINE_STRIP;
            /*  case 1530: // LINE_STRIP_ADJACENCY
             return GL.GL_LINE_STRIP_ADJACENCY;*/
            /* case 520: // PATCHES
             return GL.GL_PATCHES;*/
            case 477: // POINTS
                return GL.GL_POINTS;
            case 681: // TRIANGLES
                return GL.GL_TRIANGLES;
            /* case 1418: // TRIANGLES_ADJACENCY
             return GL.GL_TRIANGLES_ADJACENCY;*/
            case 906: // TRIANGLE_FAN
                return GL.GL_TRIANGLE_FAN;
            case 1095: // TRIANGLE_STRIP
                return GL.GL_TRIANGLE_STRIP;
            /*  case 1832: // TRIANGLE_STRIP_ADJACENCY
             return GL.GL_TRIANGLE_STRIP_ADJACENCY;*/
            default: // Do nothing  
                return -1;
        }
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


} //end of class JOGL_Renderer
