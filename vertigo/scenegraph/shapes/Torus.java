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
package vertigo.scenegraph.shapes;

import vertigo.scenegraph.Geometry;
import vertigo.scenegraph.Shape;

/**
 * Class Torus
 *
 * @author Florin Buga
 * @author Olivier Catoliquot
 * @author Clement Delestre
 * @version 0.1
 *
 */
public class Torus extends Shape {

    private float PI2 = (float) (2.0 * Math.PI);

    // Variables controlling the fineness of the polygonal mesh
    private int NumWraps;
    private int NumPerWrap;
    private float[] data;
    private int count = 0;

    // Variables controlling the size of the torus
    private float MajorRadius;
    private float MinorRadius;

    // Variables controlling the texture
    private int TextureWrapVert;
    private int TextureWrapHoriz;

    public Torus() {
        super();
        name = "torus";
        NumWraps = 40;
        NumPerWrap = 20;
        MajorRadius = 1.0f;
        MinorRadius = 0.5f;
        TextureWrapVert=6;
        TextureWrapHoriz=6;
        setDrawingStyle("TRIANGLE_STRIP");
    }

    public void setInnerRadius(float radius) {
        MinorRadius = radius;
    }

    public void setOuterRadius(float radius) {
        MajorRadius = radius;
    }

    public void setStep(int horizontalStep, int verticalStep) {
        NumWraps = horizontalStep;
        NumPerWrap = verticalStep;
    }

    @Override
    public Geometry getGeometry() {
        if (geo.getAllBO().isEmpty() )
            createTorus();
        return geo;
    }



/*
 * Algorithm to create the torus 
 * Adapted from TextureTorus.c
 * Author: Samuel R. Buss
 *
 * Software accompanying the book
 *		3D Computer Graphics: A Mathematical Introduction with OpenGL,
 *		by S. Buss, Cambridge University Press, 2003.
 *
 * Software is "as-is" and carries no warranty.  It may be used without
 *   restriction, but if you modify it, please change the filenames to
 *   prevent confusion between different versions.
 * Bug reports: Sam Buss, sbuss@ucsd.edu.
 * Web page: http://math.ucsd.edu/~sbuss/MathCG
 */
    private void createTorus() {
	// Create the torus: 8 elements per vertex (X,Y,Z),(NX,NY,NZ),(U,V)
	int length = NumWraps * (NumPerWrap + 1) * 2 * 8;
	data = new float[length];
	for (int i=0; i<NumWraps; i++ ) {
		for (int j=0; j<=NumPerWrap; j++) {
			putVertTexture(i, j);
			putVertTexture(i+1,j);
		}
	}
        geo.addBuffer(new String[]{"V3F","N3F","T2F"}, data);
    }


   /*
    * issue vertex command for segment number j of wrap number i.
    */
    private void putVertTexture(int i, int j) {
	float wrapFrac = (j%NumPerWrap)/(float)NumPerWrap;
	float wrapFracTex = (float)j/(float)NumPerWrap;
	float phi = PI2 * wrapFrac;
	float thetaFrac = ((float)(i%NumWraps)+wrapFracTex)/(float)NumWraps;
	float thetaFracTex = ((float)i+wrapFracTex)/(float)NumWraps;
	float theta = PI2 * thetaFrac;
	float sinphi = (float) Math.sin(phi);
	float cosphi = (float) Math.cos(phi);
	float sintheta = (float) Math.sin(theta);
	float costheta = (float) Math.cos(theta);
	float y = MinorRadius * sinphi;
	float r = MajorRadius + MinorRadius*cosphi;
	float x = sintheta * r;
	float z = costheta * r;

	data[count++]= x;
	data[count++]= y;
	data[count++]= z;
	data[count++]= sintheta * cosphi;
	data[count++]= sinphi;
	data[count++]= costheta * cosphi;
	data[count++]= wrapFracTex  * (float)TextureWrapVert;
	data[count++]= thetaFracTex * (float)TextureWrapHoriz;

    }


} // end of class Torus
