package com.esri.geoevent.processor.cacheawarefieldcalculator;

import com.esri.ges.core.component.ComponentException;
import com.esri.ges.manager.tag.TagManager;
import com.esri.ges.messaging.GeoEventCreator;
import com.esri.ges.messaging.Messaging;
import com.esri.ges.processor.GeoEventProcessor;
import com.esri.ges.processor.GeoEventProcessorServiceBase;

public class CacheAwareFieldCalculatorService extends GeoEventProcessorServiceBase
{
  private GeoEventCreator geoEventCreator;
  private TagManager tagManager;
  
  public CacheAwareFieldCalculatorService()
  {
    definition = new CacheAwareFieldCalculatorDefinition();
  }
  
  @Override
  public GeoEventProcessor create() throws ComponentException
  {
    CacheAwareFieldCalculator fieldCalculator = new CacheAwareFieldCalculator(definition);
    fieldCalculator.setGeoEventCreator(geoEventCreator);
    fieldCalculator.setTagManager(tagManager);
    return fieldCalculator;
  }
  
  public void setMessaging(Messaging messaging)
  {
    this.geoEventCreator = messaging.createGeoEventCreator();
  }
  
  public void setTagManager(TagManager tagManager)
  {
    this.tagManager = tagManager;
  }
}