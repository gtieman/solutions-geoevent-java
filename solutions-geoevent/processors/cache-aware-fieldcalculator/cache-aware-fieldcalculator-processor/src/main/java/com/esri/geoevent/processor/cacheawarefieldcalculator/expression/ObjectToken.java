package com.esri.geoevent.processor.cacheawarefieldcalculator.expression;

import java.util.List;
import java.util.Stack;

import com.esri.ges.core.geoevent.GeoEvent;

/**
 * A {@link Token} for Numbers
 * 
 */
public class ObjectToken extends CalculationToken
{
  private final Object value;

  /**
   * construct a new {@link ObjectToken}
   * 
   * @param value
   *          the value of the Object as a {@link String}
   */
  ObjectToken(Object value)
  {
    super(value.toString());
    this.value = value;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof ObjectToken)
    {
      final ObjectToken t = (ObjectToken) obj;
      return t.getValue().equals(this.getValue());
    }
    return false;
  }

  @Override
  public int hashCode()
  {
    return getValue().hashCode();
  }

  @Override
  void mutateStackForCalculation(Stack<Object> stack, GeoEvent geoEvent)
  {
    stack.push(value instanceof String ? ((String)value).replaceAll("'", "") : value);
  }

  @Override
  void mutateStackForInfixTranslation(Stack<Token> operatorStack, List<Token> tokens)
  {
    tokens.add(this);
  }
}