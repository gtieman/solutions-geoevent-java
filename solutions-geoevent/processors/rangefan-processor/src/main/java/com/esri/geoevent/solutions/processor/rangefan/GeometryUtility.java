package com.esri.geoevent.solutions.processor.rangefan;

/*
 * #%L
 * Esri :: AGES :: Solutions :: Processor :: Geometry
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2013 - 2014 Esri
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;

public class GeometryUtility {

	public GeometryUtility() {}
	
	public static double Geo2Arithmetic(double inAngle)
	{
		return _geo2Arithmetic(inAngle);
	}
	
	private static double _geo2Arithmetic(double inAngle)
	{
		return 360.0-(inAngle+270.0)%360.0;
	}
	
	public static Point Rotate(Point center, Point inPt, double rotationAngle)
	{
		return _rotate(center, inPt, rotationAngle);
	}
	
	public static String parseGeometryType(Geometry.Type t)
	{
		return _parseGeometryType(t);
	}
	
	private static String _parseGeometryType(Geometry.Type t)
	{
		String type = null;
		if(t == Geometry.Type.Point)
		{
			type = "esriGeometryPoint";
		}
		else if (t==Geometry.Type.Polyline)
		{
			type = "esriGeometryPolyline";
		}
		else if (t==Geometry.Type.Polygon)
		{
			type = "esriGeometryPolygon";
		}
		else if (t==Geometry.Type.MultiPoint)
		{
			type = "esriGeometryMultiPoint";
		}
		return type;
	}
	
	private static Point _rotate(Point center, Point inPt, double ra)
	{
		double x = inPt.getX();
		double y = inPt.getY();
		double cx = center.getX();
		double cy = center.getY();
		double cosra = Math.cos(ra);
		double sinra = Math.sin(ra);
		double rx = cx + cosra * (x - cx) - sinra * (y - cy);
		double ry = cy + sinra * (x - cx) + cosra * (y-cy);
		Point rPt = new Point(rx,ry);
		return rPt;
	}
	
	public Polygon GenerateEllipse(Point center, double majorAxis, double minorAxis, double ra)
	{
		Polygon ellipse = new Polygon();
		for (int i = 0; i < 360; ++i)
		{
			double theta = Math.toRadians(i);
			Point p = ellipsePtFromAngle(center, majorAxis, minorAxis, theta);
			p = GeometryUtility.Rotate(center, p, ra);
			if (i == 0) {
				ellipse.startPath(p);
			}
			else{
				ellipse.lineTo(p);
			}
		}
		ellipse.closeAllPaths();
		return ellipse;
	}
	
	private Point ellipsePtFromAngle(Point center, double rh, double rv, double angle)
	{
		double x = center.getX();
		double y = center.getY();
		double c = Math.cos(angle);
		double s = Math.sin(angle);
		double ta = s/c;
		double tt = ta * (rh/rv);
		double d = 1.0 / Math.sqrt(1.0 + Math.pow(tt, 2));
		double ex = x + Math.copySign(rh*d, c);
		double ey = y + Math.copySign(rv * tt * d, s);
		return new Point(ex,ey);
		
	}

}
