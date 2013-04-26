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

import java.nio.FloatBuffer;

/**
 *
 * @author Clement Delestre
 */
public class BufferData {
    private int handle=-1;
    private FloatBuffer buff;
    private boolean bound=false;

public BufferData(){
}
    
     public  BufferData(float[] data){
         buff=BufferTools.newFloatBuffer(data.length);
         buff.put(data);
     }
     
         public  BufferData(FloatBuffer data){
         //BufferData();
         //buff=BufferTools.newFloatBuffer(data.length);
         buff.put(data);
     }
     
     
     public boolean isBound(){
         return bound;
     }
     
     public void setBound(boolean bound){
         this.bound=bound;
     }
     public void dontBind(){
         bound=false;
     }
     public void setHandle(int handle){
         this.handle=handle;
     }
     public int getHandle(){
            return handle;
     }
     public int getCapacity(){
         return buff.capacity();
     }
    
} // end of class BufferData
