package com.esri.geoevent.solutions.adapter.cot;

/*
 * #%L
 * CoTAdapterService.java - Esri :: AGES :: Solutions :: Adapter :: CoT - Esri - 2013
 * org.codehaus.mojo-license-maven-plugin-1.5
 * $Id: update-file-header-config.apt.vm 17764 2012-12-12 10:22:04Z tchemit $
 * $HeadURL: https://svn.codehaus.org/mojo/tags/license-maven-plugin-1.5/src/site/apt/examples/update-file-header-config.apt.vm $
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


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.xml.sax.InputSource;

import com.esri.ges.adapter.Adapter;
import com.esri.ges.adapter.AdapterDefinition;
import com.esri.ges.adapter.AdapterDefinitionBase;
import com.esri.ges.adapter.AdapterService;
import com.esri.ges.adapter.AdapterType;
import com.esri.ges.core.ConfigurationException;
import com.esri.ges.core.component.ComponentException;
import com.esri.ges.core.geoevent.DefaultFieldDefinition;
import com.esri.ges.core.geoevent.DefaultGeoEventDefinition;
import com.esri.ges.core.geoevent.FieldDefinition;
import com.esri.ges.core.geoevent.FieldType;
import com.esri.ges.core.geoevent.GeoEventDefinition;
import com.esri.ges.core.property.PropertyDefinition;
import com.esri.ges.core.property.PropertyException;
import com.esri.ges.core.property.PropertyType;

public class CoTAdapterService extends AdapterDefinitionBase implements AdapterService
{

	String guid;
	private ArrayList<CoTDetailsDeff> dynamicMessageAttributes;
	private List<FieldDefinition> fieldDefinitions;
	String xsdDirectory = null;
	public static final String COT_TYPES_PATH_LABEL = "CoT Types Path";
	public static final String XSD_PATH_LABEL = "XSD_Path";
	public static final String MAXIMUM_BUFFER_SIZE_LABEL = "Max_Buffer_Size";

	// public ArrayList<CoTTypeDef>coTHash;
	public CoTAdapterService()
	{
		super(AdapterType.INBOUND);
		try {
			try
			{
				propertyDefinitions.put(COT_TYPES_PATH_LABEL, new PropertyDefinition(COT_TYPES_PATH_LABEL, PropertyType.String, "", "CoT Types Path", "This is the complete path for the cot types file.", false, false) );
				propertyDefinitions.put(XSD_PATH_LABEL, new PropertyDefinition(XSD_PATH_LABEL, PropertyType.String, "", "XSD_Path", "This directory is where additional xsd files can be installed.  When the Adapter is installed, any xsd files found in this folder are used to add schema elements to the CoT GeoEvent Definition.", false, false ) );
				propertyDefinitions.put(MAXIMUM_BUFFER_SIZE_LABEL, new PropertyDefinition(MAXIMUM_BUFFER_SIZE_LABEL, PropertyType.Integer, ""+(1024*1024), "Maximum Buffer Size", "This is the maximum number of XML characters that will be stored in memory while looking for a complete Cursor on Target message.  If the adapter receives more than this amount of text, it will assume that it has somehow missed the end of the message, and it will scan for something that looks like the beginning of a new message.", false, false ) );
			} catch (PropertyException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			FieldDefinition detailField = new DefaultFieldDefinition("detail", FieldType.Group);

			getBuiltInSchemas(detailField);

			// INCLUDE the following line if we want to convert CoTTypes to
			// human readable. - NOTE load from JAR is OK.
			// coTHash=CoTUtilities.getCoTTypeMap(this.getClass().getResourceAsStream("/CoTTypes/CoTtypes.xml"));

			// INCLUDE the following line if we are going to extract attributes
			// from XSD files. -NOTE problem loading from JAR
			getAdditionalSchemasFromXSDFolder(detailField);

			GeoEventDefinition geoEventDefinition = new DefaultGeoEventDefinition();
			geoEventDefinition.setName("CoT");
			this.fieldDefinitions = new ArrayList<FieldDefinition>();
			fieldDefinitions.add(new DefaultFieldDefinition("version",
					FieldType.Double));
			fieldDefinitions.add(new DefaultFieldDefinition("uid",
					FieldType.String, "TRACK_ID"));
			fieldDefinitions.add(new DefaultFieldDefinition("type",
					FieldType.String));
			fieldDefinitions.add(new DefaultFieldDefinition("2525b",
					FieldType.String));
			fieldDefinitions.add(new DefaultFieldDefinition(
					"typeDescription", FieldType.String));
			fieldDefinitions.add(new DefaultFieldDefinition("how",
					FieldType.String));
			fieldDefinitions.add(new DefaultFieldDefinition(
					"parsedhow", FieldType.String));
			fieldDefinitions.add(new DefaultFieldDefinition("time",
					FieldType.Date));
			fieldDefinitions.add(new DefaultFieldDefinition("start",
					FieldType.Date, "TIME_START"));
			fieldDefinitions.add(new DefaultFieldDefinition("stale",
					FieldType.Date));
			fieldDefinitions.add(new DefaultFieldDefinition("access",
					FieldType.String));
			fieldDefinitions.add(new DefaultFieldDefinition("opex",
					FieldType.String));
			fieldDefinitions.add(new DefaultFieldDefinition(
					"opexDescription", FieldType.String));
			fieldDefinitions.add(new DefaultFieldDefinition("qos",
					FieldType.String));
			fieldDefinitions.add(new DefaultFieldDefinition(
					"qosDescription", FieldType.String));
			fieldDefinitions.add(new DefaultFieldDefinition("point",
					FieldType.Geometry, "GEOMETRY"));
			fieldDefinitions.add( detailField );

			

			geoEventDefinition.setFieldDefinitions(fieldDefinitions);
			geoEventDefinitions.put(geoEventDefinition.getName(),
					geoEventDefinition);
			guid = geoEventDefinition.getGuid();
		} catch (ConfigurationException ex) {

		}
	}

	public List<FieldDefinition> getFieldDefinitions() {
		return this.fieldDefinitions;

	}

	public ArrayList<CoTDetailsDeff> getDynamicMessageAttributes() {
		return this.dynamicMessageAttributes;
	}
	/* keep for future enhancement
	private String extractName(String nameAndType) {
		return nameAndType.substring(0, nameAndType.indexOf("&"));

	}*/
	/* keep for future enhancement
	private FieldType extractType(String nameAndType) {

		

		if (nameAndType.toLowerCase().contains("decimal")) {
			return FieldType.Double;

		} else if (nameAndType.toLowerCase().contains("integer")) {
			return FieldType.Integer;

		} else {
			return FieldType.String;

		}

	}*/

	@Override
	public String getName()
	{
		return "CursorOnTarget";
	}

	@Override
	public String getDomain()
	{
		return "com.esri.geoevent.solutions.adapter.cot.inbound";
	}

	@Override
	public String getDescription() {
		return "This adapter is designed to adapt Cursor on Target (CoT) data into GeoEvents.  The adapter comes pre-bundled with several XSD files and can import others.";
	}

	@Override
	public String getContactInfo() {
		return "tracking@esri.com";
	}

	@Override
	public Map<String, GeoEventDefinition> getGeoEventDefinitions() {
		return geoEventDefinitions;
	}

	@Override
	public GeoEventDefinition getGeoEventDefinition(String name) {
		return geoEventDefinitions.get(name);
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Property Definitions:\n ");
		if (propertyDefinitions != null) {
			for (PropertyDefinition pd : propertyDefinitions.values()) {
				sb.append(" " + pd.toString());
			}
		}
		sb.append("\nGeoEvent Definitions:\n");
		if (geoEventDefinitions != null) {
			for (GeoEventDefinition ge : geoEventDefinitions.values()) {
				sb.append("  " + ge.toString() + "\n");
			}
		}
		return sb.toString();
	}

	@Override
	public AdapterDefinition getAdapterDefinition() {
		return this;
	}

	private String readXSD(String fileName) {
		StringBuffer strContent = new StringBuffer("");
		try {
			FileInputStream fis = new FileInputStream(fileName);

			int ch;

			while ((ch = fis.read()) != -1)
				strContent.append((char) ch);
			fis.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return strContent.toString();

	}

	private void getBuiltInSchemas( FieldDefinition detailsDef )
	{
		loadXSD("XSD-Add-on/CoT__flow-tags_.xsd", detailsDef );
		loadXSD("XSD-Add-on/CoT_remarks.xsd", detailsDef );
		loadXSD("XSD-Add-on/CoT_request.xsd", detailsDef );
		loadXSD("XSD-Add-on/CoT_sensor.xml", detailsDef );
		loadXSD("XSD-Add-on/CoT_shape.xsd", detailsDef );
		loadXSD("XSD-Add-on/CoT_spatial.xsd", detailsDef );
		loadXSD("XSD-Add-on/CoT_track.xsd", detailsDef );
		loadXSD("XSD-Add-on/CoT_uid.xsd", detailsDef );
	}

	private void loadXSD(String resource, FieldDefinition detailsDef )
	{
		try
		{
			InputStream is = getClass().getClassLoader().getResourceAsStream(resource);
			InputSource source = new InputSource();
			source.setByteStream(is);
			CoTDetailsDeff.parseXSD( source, detailsDef);
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	private void getAdditionalSchemasFromXSDFolder( FieldDefinition detailsDef )
	{
		String directory = "src/main/resources/XSD-Add-on/";
		File dir = new File( directory );
		if( ! dir.exists() )
			return;
		if( ! dir.isDirectory() )
			return;

		System.out.println("Scanning directory " + dir.getAbsolutePath());
		// String directory="XSD Add-on/";
		try {
			// get a list of all xsd files in a directory.

			ArrayList<String> fileList = getXSDFiles(directory);
			// attempt to load each of them in an array of strings. 1 xsd file =
			// 1 string

			// System.out.println(System.getProperty("user.dir"));
			// EDIT: select a folder dialog box?
			// include these xsd anyway?
			// String
			// sampleTagsFile=readXSD("src/main/resources/XSD Add-on/CoT_spatial.xsd");


			for (String fileName : fileList)
			{
				String xsdContent = readXSD(directory + fileName);
				InputSource source = new InputSource();
				source.setCharacterStream(new StringReader(xsdContent));

				CoTDetailsDeff.parseXSD( source, detailsDef);
			}


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private ArrayList<String> getXSDFiles(String directory) {


		File folder = new File(directory);
		String files;
		ArrayList<String> retFiles = new ArrayList<String>();

		File[] listOfFiles = folder.listFiles();
		for (File listOfFile : listOfFiles)
		{
			if (listOfFile.isFile()) {
				files = listOfFile.getName();
				if (files.toLowerCase().endsWith(".xsd")) {

					retFiles.add(files);
				}
			}
		}

		return retFiles;

	}

	@Override
	public Adapter createAdapter() throws ComponentException
	{
		try
		{
			return new CoTAdapter(this, guid);
		} catch (ConfigurationException e)
		{
			throw new ComponentException(e.getMessage());
		}
	}

	private static void report( int indent, FieldDefinition def )
	{
		for( int i = 0; i < indent; i++ )
			System.out.print(" ");
		System.out.println( def );
		if( def.getType() == FieldType.Group )
		{
			for( FieldDefinition child : def.getChildren() )
				report( indent + 4, child);
		}
	}


	public static void main( String[] args )
	{
		try
		{
			//GeoEventDefinition cotDef = new DefaultGeoEventDefinition();
			FieldDefinition detailsField = new DefaultFieldDefinition("detail", FieldType.Group);
			CoTAdapterService service = new CoTAdapterService();
			service.getAdditionalSchemasFromXSDFolder(detailsField);
			report(0,detailsField);
		} catch (ConfigurationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}