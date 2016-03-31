/*
 | Copyright 2013 Esri
 |
 | Licensed under the Apache License, Version 2.0 (the "License");
 | you may not use this file except in compliance with the License.
 | You may obtain a copy of the License at
 |
 |    http://www.apache.org/licenses/LICENSE-2.0
 |
 | Unless required by applicable law or agreed to in writing, software
 | distributed under the License is distributed on an "AS IS" BASIS,
 | WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 | See the License for the specific language governing permissions and
 | limitations under the License.
 */
package com.esri.geoevent.solutions.processor.geometry.test;

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


import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.esri.core.geometry.Point;
import com.esri.geoevent.solutions.processor.geometry.GeometryUtility;
import com.esri.ges.spatial.GeometryType;

public class GeometryUtilityTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGeo2ArithmeticZeroGeo() {
		assertEquals("", 90.0, GeometryUtility.Geo2Arithmetic(0.0), 0.001);
		
	}
	@Test
	public void testGeo2Arithmetic90Geo() {
		assertEquals("", 360.0, GeometryUtility.Geo2Arithmetic(90.0), 0.001);
		
	}
	
	@Test
	public void testGeo2Arithmetic180Geo() {
		assertEquals("", 270.0, GeometryUtility.Geo2Arithmetic(180.0), 0.001);
		
	}
	
	@Test
	public void testGeo2Arithmetic270Geo() {
		assertEquals("", 180.0, GeometryUtility.Geo2Arithmetic(270.0), 0.001);
		
	}
	@Test
	public void testRotateCenter1() {
		Point center = new Point(0,0);
		Point p2rotate = new Point(0,1);
		double ra = Math.toRadians(90.0);
		Point outPt = GeometryUtility.Rotate(center, p2rotate, ra);
		assertEquals("", -1.0, outPt.getX(), 0.001);
	}
	@Test
	public void testRotateCenter2() {
		Point center = new Point(0,0);
		Point p2rotate = new Point(0,1);
		double ra = Math.toRadians(90.0);
		Point outPt = GeometryUtility.Rotate(center, p2rotate, ra);
		assertEquals("", 0.0, outPt.getY(), 0.001);
	}
	@Test
	public void testRotateCenter3() {
		Point center = new Point(0,0);
		Point p2rotate = new Point(0,1);
		double ra = Math.toRadians(180.0);
		Point outPt = GeometryUtility.Rotate(center, p2rotate, ra);
		assertEquals("", 0.0, outPt.getX(), 0.001);
	}
	@Test
	public void testRotateCenter4() {
		Point center = new Point(0,0);
		Point p2rotate = new Point(0,1);
		double ra = Math.toRadians(180.0);
		Point outPt = GeometryUtility.Rotate(center, p2rotate, ra);
		assertEquals("", -1.0, outPt.getY(), 0.001);
	}
	@Test
	public void testRotateCenter5() {
		Point center = new Point(0,0);
		Point p2rotate = new Point(0,1);
		double ra = Math.toRadians(270.0);
		Point outPt = GeometryUtility.Rotate(center, p2rotate, ra);
		assertEquals("", 1.0, outPt.getX(), 0.001);
	}
	@Test
	public void testRotateCenter6() {
		Point center = new Point(0,0);
		Point p2rotate = new Point(0,1);
		double ra = Math.toRadians(270.0);
		Point outPt = GeometryUtility.Rotate(center, p2rotate, ra);
		assertEquals("", 0.0, outPt.getY(), 0.001);
	}
	@Test
	public void testParseGeometryType1() {
		assertSame("","esriGeometryPoint",GeometryUtility.parseGeometryType(GeometryType.Point));
	}
	@Test
	public void testParseGeometryType2() {
		assertSame("","esriGeometryPolyline",GeometryUtility.parseGeometryType(GeometryType.Polyline));
	}
	@Test
	public void testParseGeometryType3() {
		assertSame("","esriGeometryPolygon",GeometryUtility.parseGeometryType(GeometryType.Polygon));
	}
	@Test
	public void testParseGeometryType4() {
		assertSame("","esriGeometryMultiPoint",GeometryUtility.parseGeometryType(GeometryType.MultiPoint));
	}
	@Test
	public void testGenerateEllipse() {
		//fail("Not yet implemented");
	}

}
