package com.esri.geoevent.solutions.processor.unitconversion;

import com.esri.ges.core.component.ComponentException;
import com.esri.ges.core.property.PropertyException;
import com.esri.ges.manager.geoeventdefinition.GeoEventDefinitionManager;
import com.esri.ges.messaging.Messaging;
import com.esri.ges.processor.GeoEventProcessor;
import com.esri.ges.processor.GeoEventProcessorServiceBase;

public class UnitConversionProcessorService extends
		GeoEventProcessorServiceBase {
	public GeoEventDefinitionManager manager;
	public Messaging messaging;
	//public TagManager tagManager;
	public UnitConversionProcessorService() throws PropertyException {
		definition = new UnitConversionProcessorDefinition();
	}
	
	@Override
	public GeoEventProcessor create() throws ComponentException {
		return new UnitConversionProcessor(definition, manager, messaging);
	}
	
	public void setMessaging(Messaging m)
	{
		messaging = m;
	}
	
	public void setManager(GeoEventDefinitionManager m)
	{
		manager = m;
	}
	
	
}
