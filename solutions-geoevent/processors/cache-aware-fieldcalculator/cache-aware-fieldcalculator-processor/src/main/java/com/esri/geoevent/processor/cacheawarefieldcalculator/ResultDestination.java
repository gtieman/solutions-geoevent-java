package com.esri.geoevent.processor.cacheawarefieldcalculator;

import com.esri.ges.util.Validator;

public enum ResultDestination
{
  EXISTING_FIELD, NEW_FIELD;
  
  @Override
  public String toString()
  {
    switch(this)
    {
      case EXISTING_FIELD:
        return "Existing Field";
      case NEW_FIELD:
        return "New Field";
      default:
        return "";
    }
  }
  
  public static ResultDestination fromString(String s) throws IllegalArgumentException
  {
    String name = Validator.compactSpaces(s).trim();
    if (!name.isEmpty())
    {
      if ("Existing Field".equals(name))
        return ResultDestination.EXISTING_FIELD;
      if ("New Field".equals(name))
        return ResultDestination.NEW_FIELD;
    }
    throw new IllegalArgumentException("Illegal result destination '" + name + "' specified.");
  }
}