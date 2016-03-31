package com.esri.geoevent.solutions.processor.geometry;

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


import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;

import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.MapGeometry;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.SpatialReference;
import com.esri.ges.core.component.ComponentException;
import com.esri.ges.core.geoevent.GeoEvent;
import com.esri.ges.processor.GeoEventProcessorBase;
import com.esri.ges.processor.GeoEventProcessorDefinition;
import com.esri.ges.spatial.GeometryException;
import com.esri.ges.spatial.Spatial;

public class PolygonProcessor extends GeoEventProcessorBase {
	Spatial spatial;
	private static final Log LOG = LogFactory.getLog(PolygonProcessor.class);
	private SpatialReference srIn;
	private SpatialReference srBuffer;
	private SpatialReference srOut;
	public PolygonProcessor(GeoEventProcessorDefinition definition, Spatial s)
			throws ComponentException {
		super(definition);
		spatial = s;
		geoEventMutator= true;
	}

	@Override
	public GeoEvent process(GeoEvent evt){
		try {
			int inwkid = (Integer) properties.get("wkidin").getValue();
			int outwkid = (Integer) properties.get("wkidout").getValue();
			//int bufferwkid = (Integer) properties.get("wkidbuffer").getValue();
			srIn = SpatialReference.create(inwkid);
			//srBuffer = SpatialReference.create(bufferwkid);
			srOut = SpatialReference.create(outwkid);

			String eventfld = properties.get("polyfld").getValue().toString();
			String[] arr = eventfld.split(":");
			String geostring = (String) evt.getField(arr[1]);

			String format = properties.get("polyformat").getValue().toString();
			com.esri.ges.spatial.Geometry geo = null;
			if (format.equals("Json")) {
				geo = constructJsonGeometry(geostring);
			} else if (format.equals("CAP")) {
				geo = constructCAPGeometry(geostring);
			}
			evt.setGeometry(geo);
			return evt;
		} catch (Exception ex) {
			LOG.error(ex.getMessage());
			LOG.error(ex.getStackTrace());
			return null;
		}
	}

	private com.esri.ges.spatial.Geometry constructJsonGeometry(String jsongeo) {
		try {
			String jsonOut = jsongeo;
			JsonFactory jf = new JsonFactory();
			JsonParser json = jf.createJsonParser(jsongeo);
			MapGeometry mgeo = GeometryEngine.jsonToGeometry(json);
			if (!mgeo.getSpatialReference().equals(srOut)) {
				com.esri.core.geometry.Geometry geo = mgeo.getGeometry();
				com.esri.core.geometry.Geometry projGeo = GeometryEngine
						.project(geo, srIn, srOut);
				jsonOut = GeometryEngine.geometryToJson(srOut, projGeo);
			}
			return spatial.fromJson(jsonOut);
		} catch (JsonParseException ex) {
			LOG.error("Unable to parse json");
			LOG.error(ex.getMessage());
			LOG.error(ex.getStackTrace());
			return null;
		} catch (IOException ex) {
			LOG.error(ex.getMessage());
			LOG.error(ex.getStackTrace());
			return null;
		} catch (GeometryException ex) {
			LOG.error(ex.getMessage());
			LOG.error(ex.getStackTrace());
			return null;
		}
	}

	private com.esri.ges.spatial.Geometry constructCAPGeometry(String geoString)
			throws GeometryException {
		try {
			String[] pairs = geoString.split(" ");

			Polygon polygon = new Polygon();
			Boolean firstit = true;
			for (String coords : pairs) {

				String[] tuple = coords.split(",");
				Double x = Double.parseDouble(tuple[0]);
				Double y = Double.parseDouble(tuple[1]);
				Point p = new Point(x, y);
				Double z = Double.NaN;
				if (tuple.length > 2) {
					z = Double.parseDouble(tuple[2]);
					p.setZ(z);
				}
				if (firstit) {
					polygon.startPath(p);
					firstit = false;
				} else {
					polygon.lineTo(p);
				}
			}
			polygon.closeAllPaths();
			String json = GeometryEngine.geometryToJson(srIn, polygon);
			return spatial.fromJson(json);
		} catch (GeometryException ex) {
			LOG.error(ex.getMessage());
			LOG.error(ex.getStackTrace());
			return null;
		}
	}

}
