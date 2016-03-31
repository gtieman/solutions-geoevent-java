package com.esri.geoevent.processor.cacheawarefieldcalculator.expression;

import java.util.Stack;
import com.esri.ges.core.geoevent.GeoEvent;

public abstract class CalculationToken extends Token
{
  CalculationToken(Object value)
  {
    super(value);
  }
  abstract void mutateStackForCalculation(Stack<Object> stack, GeoEvent geoEvent);
}