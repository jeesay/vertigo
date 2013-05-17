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
 * MERCHANTABILITY or FITNESS FOradiusA PARTICULAradiusPURPOSE.  See the GNU
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
 * Class Sphere
 *
 * @author Florin Buga
 * @author Olivier Catoliquot
 * @author Clement Delestre
 * @version 0.1
 *
 */
public class Sphere extends Shape {

    private float[] data;
    private int[] indexData;
    private int count;
    private int icount;
    private float radius;
    private int loops;
    private int segmentsPerLoop;

    public Sphere() {
        super();
        count = 0;
        icount=0;
        loops = 15;
        segmentsPerLoop = 15;
        name = "sphere";
        radius = 1.0f;
        setDrawingStyle("TRIANGLE_STRIP");
    }

    @Override
    public Geometry getGeometry() {
        if (geo.getAllBO().isEmpty() )
            createSpiralSphere(radius,loops,segmentsPerLoop);
        return geo;
    }


	// From Enigma posted 11 Oct 2005
	// http://www.gamedev.net/topic/350823-rendering-a-sphere-using-triangle-strips/

	private void createSpiralSphere( float radius, int loops, int segmentsPerLoop)
	{
		// Create the torus: 8 elements per vertex (X,Y,Z),(NX,NY,NZ),(U,V)
		int length = (loops + 2) * segmentsPerLoop * 8;
		data = new float[length];
		indexData = new int[(loops + 2) * segmentsPerLoop * 2];

		for ( int loopSegmentNumber = 0; loopSegmentNumber < segmentsPerLoop; ++loopSegmentNumber)
		{
			double theta = 0;
			double phi = loopSegmentNumber * 2 * Math.PI / segmentsPerLoop;
			float sinTheta = (float) Math.sin(theta);
			float sinPhi = (float) Math.sin(phi);
			float cosTheta = (float) Math.cos(theta);
			float cosPhi = (float) Math.cos(phi);
			// X,Y,Z
			data[count++] = radius * cosPhi * sinTheta;
			data[count++] = radius * sinPhi * sinTheta;
			data[count++] = radius * cosTheta;
			// NX,NY,NZ
			data[count++] = cosPhi * sinTheta;
			data[count++] = sinPhi * sinTheta;
			data[count++] = cosTheta;
			// U, V ?
			data[count++] = segmentsPerLoop / (float) loopSegmentNumber;
			data[count++] = 0.0f;

		}
		for ( int loopNumber = 0; loopNumber <= loops; ++loopNumber)
		{
			for ( int loopSegmentNumber = 0; loopSegmentNumber < segmentsPerLoop; ++loopSegmentNumber)
			{
				double theta = (loopNumber * Math.PI / (double) loops) + ((Math.PI * loopSegmentNumber) / (double) (segmentsPerLoop * loops));
				if (loopNumber == loops)
				{
					theta = Math.PI;
				}
				double phi = loopSegmentNumber * 2 * Math.PI / (float) segmentsPerLoop;
				float sinTheta = (float) Math.sin(theta);
				float sinPhi = (float) Math.sin(phi);
				float cosTheta = (float) Math.cos(theta);
				float cosPhi = (float) Math.cos(phi);
				// X,Y,Z
				data[count++] = radius * cosPhi * sinTheta;
				data[count++] = radius * sinPhi * sinTheta;
				data[count++] = radius * cosTheta;
				// NX,NY,NZ
				data[count++] = cosPhi * sinTheta;
				data[count++] = sinPhi * sinTheta;
				data[count++] = cosTheta;
				// U, V ?
				data[count++] = segmentsPerLoop / (float) loopSegmentNumber;
				data[count++] = loops / (float) loopNumber;

			}
		}
		for ( int loopSegmentNumber = 0; loopSegmentNumber < segmentsPerLoop; ++loopSegmentNumber)
		{
			indexData[icount++] = loopSegmentNumber;
			indexData[icount++] = segmentsPerLoop + loopSegmentNumber;
		}
		for ( int loopNumber = 0; loopNumber < loops; ++loopNumber)
		{
			for ( int loopSegmentNumber = 0; loopSegmentNumber < segmentsPerLoop; ++loopSegmentNumber)
			{
				indexData[icount++] = ((loopNumber + 1) * segmentsPerLoop) + loopSegmentNumber;
				indexData[icount++] = ((loopNumber + 2) * segmentsPerLoop) + loopSegmentNumber;
			}
		}

		geo.addBuffer(new String[]{"V3F","N3F","T2F"}, data);
		geo.setIndices( indexData);
	}


} // end of class Sphere
