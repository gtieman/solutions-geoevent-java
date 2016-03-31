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
package com.esri.geoevent.solutions.adapter.cap;

import java.io.*;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.parsers.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.*;
import org.xml.sax.*;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.MapGeometry;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.SpatialReference;
import com.esri.ges.adapter.AdapterDefinition;
import com.esri.ges.adapter.InboundAdapterBase;
import com.esri.ges.core.component.ComponentException;
import com.esri.ges.core.geoevent.GeoEvent;
import com.esri.ges.messaging.GeoEventListener;
import com.esri.ges.messaging.MessagingException;


public class CAPInboundAdapter extends InboundAdapterBase
{
	GeoEventListener listener;
	private static final Log LOG = LogFactory.getLog(CAPInboundAdapter.class);
	private static final int MAX_ENTRIES = 20000;
	LinkedHashMap MAP;
	com.esri.core.geometry.SpatialReference arcgisWGS;
	com.esri.core.geometry.Unit arcgisKmUnit; 
		
	public CAPInboundAdapter(AdapterDefinition definition) throws ComponentException
	{
		super(definition);
		 MAP = new LinkedHashMap(MAX_ENTRIES + 1, 1.1f, false){protected boolean removeEldestEntry(Map.Entry eldest){return size() > MAX_ENTRIES;}};
		 arcgisWGS = com.esri.core.geometry.SpatialReference.create(4326);
		 arcgisKmUnit = com.esri.core.geometry.Unit.create(9036);
	}

	
	@Override
	public void receive(ByteBuffer buffer, String channelId)
	{
		//System.out.println("Processing...");
		String data;  
		while(buffer.hasRemaining())
		{
			buffer.mark();  
			
			try {
				byte[] bytearray = new byte[buffer.remaining()];		    
				buffer.get(bytearray);		    
				data = new String(bytearray);
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
		        DocumentBuilder builder = factory.newDocumentBuilder();  
		        Document doc = builder.parse(new InputSource(new StringReader(data)));
		        NodeList alerts = doc.getElementsByTagName("alert");
		        System.out.println();
		        System.out.println(new Date().toString() + ": Processing " + alerts.getLength() + " alerts.");
		        int procAlerts = 0;
		        for (int a = 0; a < alerts.getLength(); a++) {
		            Element alert = (Element) alerts.item(a);
		            
		            NodeList nodeList = alert.getElementsByTagName("identifier");
		            Element line = (Element) nodeList.item(0);
		            
		            String identifier = getCharacterDataFromElement(line);
					if (MAP.containsKey(identifier))
						{
							System.out.println(" Alert: " + identifier + " was processed previously. Skipping to next alert.");						
							continue;
						}
					//System.out.println("	Alert "+ a + ": " + identifier + ". Processing now.");
					MAP.put(identifier, identifier);
		            procAlerts++;
					
					GeoEvent alertMsg = parseAlert(alert, identifier);
		            if (alertMsg != null){
		            	geoEventListener.receive( alertMsg );
		            	System.out.println(" Alert "+ a + ": " + identifier);
						System.out.println(" " + alertMsg.toString());
						 
		            	NodeList codes = alert.getElementsByTagName("code");
		            	for (int c = 0; c < codes.getLength(); c++) {
		            		Element code = (Element) codes.item(c);
		            		GeoEvent codeMsg = parseAlertCode(code, identifier);
		            		if (codeMsg != null){
		    					geoEventListener.receive( codeMsg );
				            	System.out.println("  Code: " + codeMsg.toString());		            			
		            		}		            		
		            	}
		            	
			            NodeList infos = alert.getElementsByTagName("info");
			            for (int i = 0; i < infos.getLength(); i++) {
			            	Element info = (Element) infos.item(i);
			            	String infoID = identifier + "_" + i;
			            	GeoEvent infoMsg = parseAlertInfo(info, identifier, infoID);
			            	if (infoMsg != null){
			            		geoEventListener.receive( infoMsg );
				            	System.out.println("  Info "+ i + ": " + infoID);
				            	System.out.println("  " + infoMsg.toString());
			            		
			            		NodeList categories = info.getElementsByTagName("category");
				            	for (int cat = 0; cat < categories.getLength(); cat++) {
				            		Element category = (Element) categories.item(cat);
				            		GeoEvent catMsg = parseInfoCategory(category, identifier, infoID);
				            		if (catMsg != null){
				    					geoEventListener.receive( catMsg );		
						            	System.out.println("   Category: " + catMsg.toString());            			
				            		}		            		
				            	}
				            	NodeList eventCodes = info.getElementsByTagName("eventCode");
				            	for (int e = 0; e < eventCodes.getLength(); e++) {
				            		Element eventCode = (Element) eventCodes.item(e);
				            		GeoEvent eMsg = parseInfoEventCode(eventCode, identifier, infoID);
				            		if (eMsg != null){
				    					geoEventListener.receive( eMsg );		
						            	System.out.println("   Event code: " + eMsg.toString());		            			
				            		}		            		
				            	}
				            	NodeList responseTypes = info.getElementsByTagName("responseType");
				            	for (int rt = 0; rt < responseTypes.getLength(); rt++) {
				            		Element responseType = (Element) responseTypes.item(rt);
				            		GeoEvent rtMsg = parseInfoResponseType(responseType, identifier, infoID);
				            		if (rtMsg != null){
				    					geoEventListener.receive( rtMsg );		
						            	System.out.println("   Response type: " + rtMsg.toString());		            			
				            		}		            		
				            	}
				            	NodeList parameters = info.getElementsByTagName("parameter");
				            	for (int p = 0; p < parameters.getLength(); p++) {
				            		Element parameter = (Element) parameters.item(p);
				            		GeoEvent pMsg = parseInfoParameter(parameter, identifier, infoID);
				            		if (pMsg != null){
				    					geoEventListener.receive( pMsg );		
						            	System.out.println("   Parameter: " + pMsg.toString());		            			
				            		}		            		
				            	}
				            	NodeList resources = info.getElementsByTagName("resource");
				            	for (int r = 0; r < resources.getLength(); r++) {
				            		Element resource = (Element) resources.item(r);
				            		GeoEvent rMsg = parseInfoResource(resource, identifier, infoID);
				            		if (rMsg != null){
				    					geoEventListener.receive( rMsg );
						            	System.out.println("   Resource "+ r + ": ");		
						            	System.out.println("   " + rMsg.toString());		            			
				            		}		            		
				            	}
				            	NodeList areas = info.getElementsByTagName("area");
				            	for (int ar = 0; ar < areas.getLength(); ar++) {
				            		Element area = (Element) areas.item(ar);
					            	String areaID = infoID + "_" + ar;
				            		GeoEvent areaMsg = parseInfoArea(area, identifier, infoID, areaID);
				            		if (areaMsg != null){
				    					geoEventListener.receive( areaMsg );	
						            	System.out.println("   Area "+ ar + ": ");	
						            	System.out.println("    " + areaMsg.toString());
				    					
				    					NodeList polygons = info.getElementsByTagName("polygon");
						            	for (int pg = 0; pg < polygons.getLength(); pg++) {
						            		Element polygon = (Element) polygons.item(pg);	
						            		System.out.println("     Polygon "+ pg + ": ");
						            		GeoEvent areaGeomMsg = parseInfoAreaGeom(polygon, null, null, identifier, infoID, areaID);
						            		if (areaGeomMsg != null){
						    					geoEventListener.receive( areaGeomMsg );								            			
								            	System.out.println("      " + areaGeomMsg.toString());
						            		}
						            		else
						            		{
						            			System.out.println("      " + getCharacterDataFromElement(polygon));
						            		}
						            	}
				    					
				    					NodeList circles = info.getElementsByTagName("circle");
						            	for (int c = 0; c < circles.getLength(); c++) {
						            		Element circle = (Element) circles.item(c);		
							            	System.out.println("     Circle "+ c + ": ");						            	
						            		GeoEvent areaGeomMsg = parseInfoAreaGeom(null, circle, null, identifier, infoID, areaID);
						            		if (areaGeomMsg != null){
						    					geoEventListener.receive( areaGeomMsg );	
								            	System.out.println("      " + areaGeomMsg.toString());
						            		}
						            		else
						            		{
						            			System.out.println("      " + getCharacterDataFromElement(circle));
						            		}
						            	}
				    					
				    					NodeList geocodes = info.getElementsByTagName("geocode");
						            	for (int g = 0; g < geocodes.getLength(); g++) {
						            		Element geocode = (Element) geocodes.item(g);							            	
						            		GeoEvent areaGeomMsg = parseInfoAreaGeom(null, null, geocode, identifier, infoID, areaID);
						            		if (areaGeomMsg != null){
						    					geoEventListener.receive( areaGeomMsg );
								            	System.out.println("     Geocode "+ g + ": ");		
								            	System.out.println("      " + areaGeomMsg.toString());
						            		}
						            	}
				            		}		            		
				            	}			    				
			            	}	
			            }
		            }
		        }

		        //System.out.println("Processed " + procAlerts + " of " + alerts.getLength() + " alerts.");
				
			} catch (Exception e) {
				String msg = e.getMessage();				
            	System.out.println(msg);
				e.printStackTrace();
			}

			return;
		}
	}
	
	private GeoEvent parseAlert(Element element, String identifier)
	{
		GeoEvent msg;
		try
		{
			msg = geoEventCreator.create(((AdapterDefinition)definition).getGeoEventDefinition("CAPAlert").getGuid()); 
			
            try{msg.setField(0, identifier);}catch(Exception ex){LOG.debug("Failed to set 'identifier': " + identifier);}
            
            NodeList nodeList;
            Element line;
            String strValue = "";
            
            try{nodeList = element.getElementsByTagName("sender");
            line = (Element) nodeList.item(0);
            strValue = getCharacterDataFromElement(line);
            msg.setField(1, strValue);}catch(Exception ex){LOG.debug("Failed to set 'sender': " + strValue);}            
            
            try{nodeList = element.getElementsByTagName("sent");
            line = (Element) nodeList.item(0);
            strValue = getCharacterDataFromElement(line);             
            int colon = strValue.lastIndexOf(":");
            strValue = strValue.substring(0, colon) + strValue.substring(colon + 1, strValue.length());
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = format.parse(strValue);                        		
            msg.setField(2, date);}catch(Exception ex){LOG.debug("Failed to set 'sent': " + strValue);}
            
            try{nodeList = element.getElementsByTagName("status");
            line = (Element) nodeList.item(0);
            strValue = getCharacterDataFromElement(line);
            msg.setField(3, strValue);}catch(Exception ex){LOG.debug("Failed to set 'status': " + strValue);}
            
            try{nodeList = element.getElementsByTagName("msgType");
            line = (Element) nodeList.item(0);
            strValue = getCharacterDataFromElement(line);
            msg.setField(4, strValue);}catch(Exception ex){LOG.debug("Failed to set 'msgType': " + strValue);}
            
            try{nodeList = element.getElementsByTagName("source");
            line = (Element) nodeList.item(0);
            strValue = getCharacterDataFromElement(line);
            msg.setField(5, strValue);}catch(Exception ex){LOG.debug("Failed to set 'source': " + strValue);}
            
            try{nodeList = element.getElementsByTagName("scope");
            line = (Element) nodeList.item(0);
            strValue = getCharacterDataFromElement(line);
            msg.setField(6, strValue);}catch(Exception ex){LOG.debug("Failed to set 'scope': " + strValue);}
            
            try{nodeList = element.getElementsByTagName("restriction");
            line = (Element) nodeList.item(0);
            strValue = getCharacterDataFromElement(line);
            msg.setField(7, strValue);}catch(Exception ex){LOG.debug("Failed to set 'restriction': " + strValue);}
            
            try{nodeList = element.getElementsByTagName("addresses");
            line = (Element) nodeList.item(0);
            strValue = getCharacterDataFromElement(line);
            msg.setField(8, strValue);}catch(Exception ex){LOG.debug("Failed to set 'addresses': " + strValue);}
            
            try{nodeList = element.getElementsByTagName("note");
            line = (Element) nodeList.item(0);
            strValue = getCharacterDataFromElement(line);
            msg.setField(9, strValue);}catch(Exception ex){LOG.debug("Failed to set 'note': " + strValue);}
            
            try{nodeList = element.getElementsByTagName("references");
            line = (Element) nodeList.item(0);
            strValue = getCharacterDataFromElement(line);
            msg.setField(10, strValue);}catch(Exception ex){LOG.debug("Failed to set 'references': " + strValue);}
            
            try{nodeList = element.getElementsByTagName("incidents");
            line = (Element) nodeList.item(0);
            strValue = getCharacterDataFromElement(line);
            msg.setField(11, strValue);}catch(Exception ex){LOG.debug("Failed to set 'incidents': " + strValue);}

		}
		catch (MessagingException e)
		{
			return null;
		}		
		return msg;
	}
	private GeoEvent parseAlertCode(Element element, String identifier)
	{
		GeoEvent msg;
		try
		{
			msg = geoEventCreator.create(((AdapterDefinition)definition).getGeoEventDefinition("CAPAlertCode").getGuid()); 
			
            try{msg.setField(0, identifier);}catch(Exception ex){LOG.debug("Failed to set 'identifier': " + identifier);}
            
            NodeList nodeList;
            Element line;
            String strValue = "";
            
            try{
            strValue = getCharacterDataFromElement(element);
            msg.setField(1, strValue);}catch(Exception ex){LOG.debug("Failed to set 'code': " + strValue);}
		}
		catch (MessagingException e)
		{
			return null;
		}		
		return msg;
	}
	private GeoEvent parseAlertInfo(Element element, String identifier, String infoID)
	{
		GeoEvent msg;
		try
		{
			msg = geoEventCreator.create(((AdapterDefinition)definition).getGeoEventDefinition("CAPInfo").getGuid()); 
			
            try{msg.setField(0, identifier);}catch(Exception ex){LOG.debug("Failed to set 'identifier': " + identifier);}
            try{msg.setField(1, infoID);}catch(Exception ex){LOG.debug("Failed to set 'infoID': " + infoID);}
            
            NodeList nodeList;
            Element line;
            String strValue = "";
            String tagName = "";
            
            try{tagName = "language";
             nodeList = element.getElementsByTagName(tagName);
             line = (Element) nodeList.item(0);
             strValue = getCharacterDataFromElement(line);
            msg.setField(2, strValue);}catch(Exception ex){LOG.debug("Failed to set '" + tagName + "': " + strValue);}
            
            try{tagName = "event";
            nodeList = element.getElementsByTagName(tagName);
            line = (Element) nodeList.item(0);
            strValue = getCharacterDataFromElement(line);
            msg.setField(3, strValue);}catch(Exception ex){LOG.debug("Failed to set '" + tagName + "': " + strValue);}
            
            try{tagName = "urgency";
            nodeList = element.getElementsByTagName(tagName);
            line = (Element) nodeList.item(0);
            strValue = getCharacterDataFromElement(line);
            msg.setField(4, strValue);}catch(Exception ex){LOG.debug("Failed to set '" + tagName + "': " + strValue);}
            
            try{tagName = "severity";
            nodeList = element.getElementsByTagName(tagName);
            line = (Element) nodeList.item(0);
            strValue = getCharacterDataFromElement(line);
            msg.setField(5, strValue);}catch(Exception ex){LOG.debug("Failed to set '" + tagName + "': " + strValue);}
            
            try{tagName = "certainty";
            nodeList = element.getElementsByTagName(tagName);
            line = (Element) nodeList.item(0);
            strValue = getCharacterDataFromElement(line);
            msg.setField(6, strValue);}catch(Exception ex){LOG.debug("Failed to set '" + tagName + "': " + strValue);}
            
            try{tagName = "audience";
            nodeList = element.getElementsByTagName(tagName);
            line = (Element) nodeList.item(0);
            strValue = getCharacterDataFromElement(line);
            msg.setField(7, strValue);}catch(Exception ex){LOG.debug("Failed to set '" + tagName + "': " + strValue);}
            
            try{tagName = "effective";
            nodeList = element.getElementsByTagName(tagName);
            line = (Element) nodeList.item(0);
            strValue = getCharacterDataFromElement(line);
            int colon = strValue.lastIndexOf(":");
            strValue = strValue.substring(0, colon) + strValue.substring(colon + 1, strValue.length());
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = format.parse(strValue);
            msg.setField(8, date);}catch(Exception ex){LOG.debug("Failed to set '" + tagName + "': " + strValue);}
            
            try{tagName = "onset";
            nodeList = element.getElementsByTagName(tagName);
            line = (Element) nodeList.item(0);
            strValue = getCharacterDataFromElement(line);
            int colon = strValue.lastIndexOf(":");
            strValue = strValue.substring(0, colon) + strValue.substring(colon + 1, strValue.length());
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = format.parse(strValue);
            msg.setField(9, date);}catch(Exception ex){LOG.debug("Failed to set '" + tagName + "': " + strValue);}
            
            try{tagName = "expires";
            nodeList = element.getElementsByTagName(tagName);
            line = (Element) nodeList.item(0);
            strValue = getCharacterDataFromElement(line);
            int colon = strValue.lastIndexOf(":");
            strValue = strValue.substring(0, colon) + strValue.substring(colon + 1, strValue.length());
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = format.parse(strValue);
            msg.setField(10, date);}catch(Exception ex){LOG.debug("Failed to set '" + tagName + "': " + strValue);}
            
            try{tagName = "senderName";
            nodeList = element.getElementsByTagName(tagName);
            line = (Element) nodeList.item(0);
            strValue = getCharacterDataFromElement(line);
            msg.setField(11, strValue);}catch(Exception ex){LOG.debug("Failed to set '" + tagName + "': " + strValue);}
            
            try{tagName = "headline";
            nodeList = element.getElementsByTagName(tagName);
            line = (Element) nodeList.item(0);
            strValue = getCharacterDataFromElement(line);
            msg.setField(12, strValue);}catch(Exception ex){LOG.debug("Failed to set '" + tagName + "': " + strValue);}
            
            try{tagName = "description";
            nodeList = element.getElementsByTagName(tagName);
            line = (Element) nodeList.item(0);
            strValue = getCharacterDataFromElement(line);
            msg.setField(13, strValue);}catch(Exception ex){LOG.debug("Failed to set '" + tagName + "': " + strValue);}
            
            try{tagName = "instruction";
            nodeList = element.getElementsByTagName(tagName);
            line = (Element) nodeList.item(0);
            strValue = getCharacterDataFromElement(line);
            msg.setField(14, strValue);}catch(Exception ex){LOG.debug("Failed to set '" + tagName + "': " + strValue);}
            
            try{tagName = "web";
            nodeList = element.getElementsByTagName(tagName);
            line = (Element) nodeList.item(0);
            strValue = getCharacterDataFromElement(line);
            msg.setField(15, strValue);}catch(Exception ex){LOG.debug("Failed to set '" + tagName + "': " + strValue);}
            
            try{tagName = "contact";
            nodeList = element.getElementsByTagName(tagName);
            line = (Element) nodeList.item(0);
            strValue = getCharacterDataFromElement(line);
            msg.setField(16, strValue);}catch(Exception ex){LOG.debug("Failed to set '" + tagName + "': " + strValue);}            
		}
		catch (MessagingException e)
		{
			return null;
		}		
		return msg;
	}
	private GeoEvent parseInfoCategory(Element element, String identifier, String infoID)
	{
		GeoEvent msg;
		try
		{
			msg = geoEventCreator.create(((AdapterDefinition)definition).getGeoEventDefinition("CAPInfoCategory").getGuid()); 
			
			try{msg.setField(0, identifier);}catch(Exception ex){LOG.debug("Failed to set 'identifier': " + identifier);}
            try{msg.setField(1, infoID);}catch(Exception ex){LOG.debug("Failed to set 'infoID': " + infoID);}
            
            NodeList nodeList;
            Element line;
            String strValue = "";
            String tagName = "";
            
            try{tagName = "category";
             strValue = getCharacterDataFromElement(element);
            msg.setField(2, strValue);}catch(Exception ex){LOG.debug("Failed to set '" + tagName + "': " + strValue);}

		}
		catch (MessagingException e)
		{
			return null;
		}		
		return msg;
	}
	private GeoEvent parseInfoEventCode(Element element, String identifier, String infoID)
	{
		GeoEvent msg;
		try
		{
			msg = geoEventCreator.create(((AdapterDefinition)definition).getGeoEventDefinition("CAPInfoEventCode").getGuid()); 
			
			try{msg.setField(0, identifier);}catch(Exception ex){LOG.debug("Failed to set 'identifier': " + identifier);}
            try{msg.setField(1, infoID);}catch(Exception ex){LOG.debug("Failed to set 'infoID': " + infoID);}
            
            NodeList nodeList;
            Element line;
            String strValue = "";
            String tagName = "";
            
            try{tagName = "valueName";
             nodeList = element.getElementsByTagName(tagName);
             line = (Element) nodeList.item(0);
             strValue = getCharacterDataFromElement(line);
            msg.setField(2, strValue);}catch(Exception ex){LOG.debug("Failed to set 'Event code " + tagName + "': " + strValue);}
            
            try{tagName = "value";
             nodeList = element.getElementsByTagName(tagName);
             line = (Element) nodeList.item(0);
             strValue = getCharacterDataFromElement(line);
            msg.setField(3, strValue);}catch(Exception ex){LOG.debug("Failed to set 'Event code " + tagName + "': " + strValue);}

		}
		catch (MessagingException e)
		{
			return null;
		}		
		return msg;
	}
	private GeoEvent parseInfoResponseType(Element element, String identifier, String infoID)
	{
		GeoEvent msg;
		try
		{
			msg = geoEventCreator.create(((AdapterDefinition)definition).getGeoEventDefinition("CAPInfoResponseType").getGuid()); 
			
			try{msg.setField(0, identifier);}catch(Exception ex){LOG.debug("Failed to set 'identifier': " + identifier);}
            try{msg.setField(1, infoID);}catch(Exception ex){LOG.debug("Failed to set 'infoID': " + infoID);}
            
            NodeList nodeList;
            Element line;
            String strValue = "";
            String tagName = "";
            
            try{ tagName = "responseType";
             strValue = getCharacterDataFromElement(element);
            msg.setField(2, strValue);}catch(Exception ex){LOG.debug("Failed to set '" + tagName + "': " + strValue);}

		}
		catch (MessagingException e)
		{
			return null;
		}		
		return msg;
	}
	private GeoEvent parseInfoParameter(Element element, String identifier, String infoID)
	{
		GeoEvent msg;
		try
		{
			msg = geoEventCreator.create(((AdapterDefinition)definition).getGeoEventDefinition("CAPInfoParameter").getGuid()); 
			
			try{msg.setField(0, identifier);}catch(Exception ex){LOG.debug("Failed to set 'identifier': " + identifier);}
            try{msg.setField(1, infoID);}catch(Exception ex){LOG.debug("Failed to set 'infoID': " + infoID);}
            
            NodeList nodeList;
            Element line;
            String strValue = "";
            String tagName = "";
            
            try{ tagName = "valueName";
             nodeList = element.getElementsByTagName(tagName);
             line = (Element) nodeList.item(0);
             strValue = getCharacterDataFromElement(line);
            msg.setField(2, strValue);}catch(Exception ex){LOG.debug("Failed to set 'Parameter " + tagName + "': " + strValue);}
            
            try{tagName = "value";
            nodeList = element.getElementsByTagName(tagName);
            line = (Element) nodeList.item(0);
            strValue = getCharacterDataFromElement(line);
            msg.setField(3, strValue);}catch(Exception ex){LOG.debug("Failed to set 'Parameter " + tagName + "': " + strValue);}

		}
		catch (MessagingException e)
		{
			return null;
		}		
		return msg;
	}
	private GeoEvent parseInfoResource(Element element, String identifier, String infoID)
	{
		GeoEvent msg;
		try
		{
			msg = geoEventCreator.create(((AdapterDefinition)definition).getGeoEventDefinition("CAPInfoResource").getGuid()); 
			
			try{msg.setField(0, identifier);}catch(Exception ex){LOG.debug("Failed to set 'identifier': " + identifier);}
            try{msg.setField(1, infoID);}catch(Exception ex){LOG.debug("Failed to set 'infoID': " + infoID);}
            
            NodeList nodeList;
            Element line;
            String strValue = "";
            String tagName = "";
            
            try{ tagName = "resourceDesc";
             nodeList = element.getElementsByTagName(tagName);
             line = (Element) nodeList.item(0);
             strValue = getCharacterDataFromElement(line);
            msg.setField(2, strValue);}catch(Exception ex){LOG.debug("Failed to set '" + tagName + "': " + strValue);}
            
             try{tagName = "mimeType";
            nodeList = element.getElementsByTagName(tagName);
            line = (Element) nodeList.item(0);
            strValue = getCharacterDataFromElement(line);
           msg.setField(3, strValue);}catch(Exception ex){LOG.debug("Failed to set '" + tagName + "': " + strValue);}
            
            try{tagName = "size";
            nodeList = element.getElementsByTagName(tagName);
            line = (Element) nodeList.item(0);
            strValue = getCharacterDataFromElement(line);
            msg.setField(4, strValue);}catch(Exception ex){LOG.debug("Failed to set '" + tagName + "': " + strValue);}
            
            try{tagName = "uri";
            nodeList = element.getElementsByTagName(tagName);
            line = (Element) nodeList.item(0);
            strValue = getCharacterDataFromElement(line);
            msg.setField(5, strValue);}catch(Exception ex){LOG.debug("Failed to set '" + tagName + "': " + strValue);}
            
            try{tagName = "digest";
            nodeList = element.getElementsByTagName(tagName);
            line = (Element) nodeList.item(0);
            strValue = getCharacterDataFromElement(line);
            msg.setField(6, strValue);}catch(Exception ex){LOG.debug("Failed to set '" + tagName + "': " + strValue);}

		}
		catch (MessagingException e)
		{
			return null;
		}		
		return msg;
	}
	private GeoEvent parseInfoArea(Element element, String identifier, String infoID, String areaID)
	{
		GeoEvent msg;
		try
		{
			msg = geoEventCreator.create(((AdapterDefinition)definition).getGeoEventDefinition("CAPInfoArea").getGuid()); 
			
			try{msg.setField(0, identifier);}catch(Exception ex){LOG.debug("Failed to set 'identifier': " + identifier);}
            try{msg.setField(1, infoID);}catch(Exception ex){LOG.debug("Failed to set 'infoID': " + infoID);}
            try{msg.setField(2, areaID);}catch(Exception ex){LOG.debug("Failed to set 'areaID': " + areaID);}
            
            NodeList nodeList;
            Element line;
            String strValue = "";
            String tagName = "";
            
            try{ tagName = "areaDesc";
             nodeList = element.getElementsByTagName(tagName);
             line = (Element) nodeList.item(0);
             strValue = getCharacterDataFromElement(line);
            msg.setField(3, strValue);}catch(Exception ex){LOG.debug("Failed to set '" + tagName + "': " + strValue);}
            
            try{tagName = "altitude";
            nodeList = element.getElementsByTagName(tagName);
            line = (Element) nodeList.item(0);
            strValue = getCharacterDataFromElement(line);
            int intValue = Integer.parseInt(strValue);
            msg.setField(4, intValue);}catch(Exception ex){LOG.debug("Failed to set '" + tagName + "': " + strValue);}
            
            try{tagName = "ceiling";
            nodeList = element.getElementsByTagName(tagName);
            line = (Element) nodeList.item(0);
            strValue = getCharacterDataFromElement(line);
            int intValue = Integer.parseInt(strValue);
            msg.setField(5, intValue);}catch(Exception ex){LOG.debug("Failed to set '" + tagName + "': " + strValue);}

		}
		catch (MessagingException e)
		{
			return null;
		}		
		return msg;
	}
	private GeoEvent parseInfoAreaGeom(Element polygon, Element circle, Element geocode, String identifier, String infoID, String areaID)
	{
		GeoEvent msg;
		try
		{
			msg = geoEventCreator.create(((AdapterDefinition)definition).getGeoEventDefinition("CAPInfoAreaGeom").getGuid()); 
			Integer len = identifier.length();
			try{msg.setField(0, identifier);}catch(Exception ex){LOG.debug("Failed to set 'identifier': " + identifier);}
            try{msg.setField(1, infoID);}catch(Exception ex){LOG.debug("Failed to set 'infoID': " + infoID);}
            try{msg.setField(2, areaID);}catch(Exception ex){LOG.debug("Failed to set 'areaID': " + areaID);}
            SpatialReference sr = SpatialReference.create(4326);
            String tagName;
            NodeList nodeList;
            Element line;
            String strValue = "";
        
            if (polygon != null){
            	tagName = "polygon";
            	
            	try{strValue = getCharacterDataFromElement(polygon);
            	msg.setField(3, strValue);}catch(Exception ex){
            		LOG.debug("Failed to set '" + tagName + "': " + strValue);
            		}
            	
            	String[] coords = strValue.split(" ");
            	if (coords.length >= 4 )
            	{
            		String firstCoordPair = coords[0].trim();
            		String lastCoordPair = coords[coords.length - 1].trim();
            		boolean firstAndLastCoordsAreEqual = firstCoordPair.equals(lastCoordPair);
            		if (!firstAndLastCoordsAreEqual) {
            			System.out.println("      Invalid coordinate list.");
            			System.out.println("      The first (" + firstCoordPair + ") and last (" + lastCoordPair + ") coordinate pairs are not identical.");
            			//return null;
            		}
            			
            		try
            		{
            			
            			com.esri.core.geometry.Polygon capPoly = new com.esri.core.geometry.Polygon();
            			Boolean first = true;
            			for(String pair: coords)
            			{
            				String[] xyArr = pair.split(",");
            				Double y = Double.parseDouble(xyArr[0].trim());
            				Double x = Double.parseDouble(xyArr[1].trim());
            				if (Double.isNaN(y) | y == 0)
								continue;
							if (Double.isNaN(x) | x == 0)
								continue;
							Point p = new Point(x, y);
							
            				//com.esri.core.geometry.Point p = GeometryEngine.project(x, y, sr);
            				if(first)
            				{
            					capPoly.startPath(p);
            					first=false;
            				}
            				else
            				{
            					capPoly.lineTo(p);
            				}
            			}
            			capPoly.closeAllPaths();
            			Geometry simple = GeometryEngine.simplify(capPoly, sr);
            			MapGeometry mapGeo = new MapGeometry(simple, sr);
            			
            			msg.setGeometry(mapGeo);
            			
            		}
            		catch(Exception ex)
            		{
            			LOG.debug("Failed to set polygon");
            		}
            	}          	
            }    
            
            if (circle != null)
            {
            	tagName = "circle";
            	try
            	{
            		strValue = getCharacterDataFromElement(circle);
                	msg.setField(4, strValue);}
            	catch(Exception ex)
            	{
            		LOG.debug("Failed to set '" + tagName + "': " + strValue);
            	}

            	String coordPair = "";
            	String radString = "";
            	try
            	{
            		String[] coords = strValue.split(" ");
            		coordPair = coords[0];
            		String[] xAndY = coordPair.split(",");
            		Double y = Double.parseDouble(xAndY[0]);
            		Double x = Double.parseDouble(xAndY[1]);

            		radString= coords[1];
            		Double radius = Double.parseDouble(radString);
            		if (radius == null | radius <= 0) {
            			System.out.println("      Invalid radius. Radius is "+ radius.toString() + ". It must be non-null and > 0. Setting radius at 0.1.");
            			//return null;
            			radius = 0.1;
            		}
            		Geometry pointBuffer = buffer(x,y,radius);
            		MapGeometry mapGeo = new MapGeometry(pointBuffer, sr);
            		msg.setField(7, mapGeo);
        			System.out.println("");
        			System.out.println("      Set a circle geometry for " + areaID + ".");
        			System.out.println("");
            	}
            	catch(Exception ex)
            	{
            		LOG.debug("Failed to set circle");
            		System.out.println("     Error parsing coordinates ("+ coordPair + ") or radius ("+ radString + ").");
            		return null;
            	}
            }
                
            
            if (geocode != null){
            	tagName = "valueName";
            	nodeList = geocode.getElementsByTagName(tagName);
            	line = (Element) nodeList.item(0);
            	try
            	{
            		strValue = getCharacterDataFromElement(line);
            		msg.setField(5, strValue);
            	}
            	catch(Exception ex)
            	{
            		LOG.debug("Failed to set 'Geocode Name': " + strValue);
            	}
            	
            	tagName = "value";
            	nodeList = geocode.getElementsByTagName(tagName);
            	line = (Element) nodeList.item(0);
            	try
            	{
            		strValue = getCharacterDataFromElement(line);
            		msg.setField(6, strValue);
            	}
            	catch(Exception ex)
            	{
            		LOG.debug("Failed to set 'Geocode Value': " + strValue);
            	}
            }

		}
		catch (MessagingException e)
		{
			return null;
		}		
		return msg;
	}
	
	public static String getCharacterDataFromElement(Element e) {
		Node child = e.getFirstChild();
		if (child instanceof CharacterData) {
			CharacterData cd = (CharacterData) child;
		    return cd.getData();
		}
		return "";
	}
	
	public Polygon buffer(double x, double y, double distance) throws InstantiationException, IllegalAccessException
    {		
		Point arcGisPoint = new Point(x, x, 0);
		Polygon buffer = GeometryEngine.buffer(arcGisPoint, arcgisWGS, distance, arcgisKmUnit);
		return buffer;

    }

	private GeoEvent parseEvent(String data)
	{
		// Create an instance of the message using the guid that we generated when
		// we started up.
		GeoEvent msg;
		try
		{
			msg = geoEventCreator.create(((AdapterDefinition)definition).getGeoEventDefinition("CAPAlert").getGuid());
		}
		catch (MessagingException e)
		{
			return null;
		}
		return msg;
	}

	@Override
	protected GeoEvent adapt(ByteBuffer arg0, String arg1) {
		return null;
	}
}