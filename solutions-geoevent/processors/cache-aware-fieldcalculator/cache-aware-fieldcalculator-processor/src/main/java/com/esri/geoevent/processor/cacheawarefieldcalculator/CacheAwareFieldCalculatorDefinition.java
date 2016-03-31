package com.esri.geoevent.processor.cacheawarefieldcalculator;

import java.util.ArrayList;
import java.util.List;

import com.esri.ges.core.property.LabeledValue;
import com.esri.ges.core.property.PropertyDefinition;
import com.esri.ges.core.property.PropertyType;
import com.esri.ges.processor.GeoEventProcessorDefinitionBase;

public class CacheAwareFieldCalculatorDefinition extends GeoEventProcessorDefinitionBase
{
  public CacheAwareFieldCalculatorDefinition()
  {
    try
    {
    	List<LabeledValue> resultDestinationAllowableValues = new ArrayList<>();
    	resultDestinationAllowableValues.add(new LabeledValue("Existing Field", "Existing Field"));
    	resultDestinationAllowableValues.add(new LabeledValue("New Field", "New Field"));
      propertyDefinitions.put("expression", new PropertyDefinition("expression", PropertyType.String, "", "Expression", "Expression", true, false));

      propertyDefinitions.put("resultDestination", new PropertyDefinition("resultDestination", PropertyType.String, "Existing Field", "Result Destination", "Result Destination", true, false, resultDestinationAllowableValues));
      // Case 1: Overwriting Existing Field
      propertyDefinitions.put("existingFieldName", new PropertyDefinition("existingFieldName", PropertyType.String, "", "Existing Field Name", "Existing Field Name", "resultDestination=Existing Field", false, false));
      // Case 2: Creating New Field
      propertyDefinitions.put("newFieldName", new PropertyDefinition("newFieldName", PropertyType.String, "", "New Field Name", "New Field Name", "resultDestination=New Field", false, false));
      
      List<LabeledValue> newFieldTypeAllowableValues = new ArrayList<>();
      newFieldTypeAllowableValues.add(new LabeledValue("Boolean", "Boolean"));
      newFieldTypeAllowableValues.add(new LabeledValue("Date", "Date"));
      newFieldTypeAllowableValues.add(new LabeledValue("Double", "Double"));
      newFieldTypeAllowableValues.add(new LabeledValue("Float", "Float"));
      newFieldTypeAllowableValues.add(new LabeledValue("Geometry", "Geometry"));
      newFieldTypeAllowableValues.add(new LabeledValue("Integer", "Integer"));
      newFieldTypeAllowableValues.add(new LabeledValue("Long", "Long"));
      newFieldTypeAllowableValues.add(new LabeledValue("Short", "Short"));
      newFieldTypeAllowableValues.add(new LabeledValue("String", "String"));
      propertyDefinitions.put("newFieldType", new PropertyDefinition("newFieldType", PropertyType.String, "", "New Field Type", "New Field Type", "resultDestination=New Field", false, false, newFieldTypeAllowableValues));
      propertyDefinitions.put("newFieldTag", new PropertyDefinition("newFieldTag", PropertyType.String, "", "New Field Tag", "New Field Tag", "resultDestination=New Field", false, false));
      propertyDefinitions.put("newGeoEventDefinitionName", new PropertyDefinition("newGeoEventDefinitionName", PropertyType.String, "FieldCalculator", "Resulting GeoEvent Definition Name", "Resulting GeoEvent Definition Name", "resultDestination=New Field", false, false));
    }
    catch(Exception e)
    {
      ;
    }
  }
  
  @Override
  public String getName()
  {
    return "CacheAwareFieldCalculator";
  }
  
  @Override
  public String getDomain()
  {
  	return "com.esri.geoevent.processor";
  }
  
  @Override
  public String getVersion()
  {
  	return "10.3.0";
  }
  
  @Override
  public String getLabel()
  {
    return "${com.esri.geoevent.processor.cache-aware-fieldcalculator-processor.PROCESSOR_LABEL}";
  }
  
  @Override
  public String getDescription()
  {
    return "${com.esri.geoevent.processor.cache-aware-fieldcalculator-processor.PROCESSOR_DESC}";
  }
}