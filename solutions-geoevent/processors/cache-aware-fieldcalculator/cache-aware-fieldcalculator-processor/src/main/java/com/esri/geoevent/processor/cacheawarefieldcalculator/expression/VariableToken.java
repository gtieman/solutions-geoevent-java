package com.esri.geoevent.processor.cacheawarefieldcalculator.expression;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Stack;

import com.esri.ges.core.geoevent.Field;
import com.esri.ges.core.geoevent.FieldDefinition;
import com.esri.ges.core.geoevent.FieldExpression;
import com.esri.ges.core.geoevent.GeoEvent;

public class VariableToken extends CalculationToken
{
  /**
   * construct a new {@link VariableToken}
   * 
   * @param value
   *            the value of the token
   */
  VariableToken(FieldExpression fe)
  {
    super(fe);
  }

  @Override
  public boolean equals(Object obj)
  {
    return (obj instanceof VariableToken) ? super.getValue().equals(((VariableToken) obj).getValue()) : false;
  }

  @Override
  public int hashCode()
  {
    return value.hashCode();
  }
  
  @Override
  void mutateStackForCalculation(Stack<Object> stack, GeoEvent geoEvent)
  {
    Object value = null;
    Field field = geoEvent.getField((FieldExpression)this.value);
    if (field != null)
    {
      FieldDefinition fd = field.getDefinition();
      value = field.getValue();
      if (fd != null)
      {
        switch (fd.getType())
        {
          case Group:
            break;
          case Boolean:
            if (value == null)
              value = Boolean.FALSE;
            break;
          case Date:
            if (value == null)
            {
              Calendar calendar = Calendar.getInstance();
              calendar.setTimeInMillis(0);
              value = calendar.getTimeInMillis();
            }
            else
              value = ((Date)value).getTime();
            break;
          case Geometry:
            break;
          case String:
            if (value == null)
              value = "";
            break;
          case Short:
            if (value == null)
              value = (short)0;
            break;
          case Integer:
            if (value == null)
              value = 0;
            break;
          case Long:
            if (value == null)
              value = 0l;
            break;
          case Float:
            if (field.getValue() == null)
              value = 0f;
            break;
          case Double:
            if (value == null)
              value = 0d;
            break;
        }
      }
    }
    stack.push(value);
  }
  
  @Override
  void mutateStackForInfixTranslation(Stack<Token> operatorStack, List<Token> tokens)
  {
    tokens.add(this);
  }
}