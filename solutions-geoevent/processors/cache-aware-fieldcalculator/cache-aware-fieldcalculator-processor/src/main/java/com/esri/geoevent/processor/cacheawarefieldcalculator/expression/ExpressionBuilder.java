package com.esri.geoevent.processor.cacheawarefieldcalculator.expression;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.esri.ges.core.geoevent.Field;
import com.esri.ges.core.geoevent.FieldDefinition;
import com.esri.ges.core.geoevent.FieldExpression;
import com.esri.ges.core.geoevent.GeoEvent;
import com.esri.ges.core.geoevent.GeoEventCache;
import com.esri.ges.core.geoevent.GeoEventPropertyName;
import com.esri.ges.util.Converter;

/**
 * This is Expression Builder implementation used to create a Calculable
 * instance for the user
 */
public class ExpressionBuilder
{
  /**
   * Property name for unary precedence choice. You can set
   * System.getProperty(PROPERTY_UNARY_HIGH_PRECEDENCE,"false") in order to
   * change evaluation from an expression like "-3^2" from "(-3)^2" to "-(3^2)"
   */
  public static final String PROPERTY_UNARY_HIGH_PRECEDENCE = "unary.precedence.high";

//  private final Map<String, Object> variables = new LinkedHashMap<String, Object>();
  private final Map<String, Function> functions;
  private final Map<String, Operator> builtInOperators;
  private Map<String, Operator> customOperators = new HashMap<String, Operator>();
  private final List<Character> validOperatorSymbols;
  private final boolean highUnaryPrecedence;
  private String expression;
  private GeoEvent geoEvent;
  private GeoEventCache geoEventCache;

  /**
   * Create a new ExpressionBuilder
   * 
   * @param expression
   *          the expression to evaluate
   */
  public ExpressionBuilder(GeoEvent geoEvent, GeoEventCache geoEventCache, String expression)
  {
    this.geoEvent = geoEvent;
    this.geoEventCache = geoEventCache;
    highUnaryPrecedence = System.getProperty(PROPERTY_UNARY_HIGH_PRECEDENCE) == null || !System.getProperty(PROPERTY_UNARY_HIGH_PRECEDENCE).equals("false");
    functions = getBuiltinFunctions();
    builtInOperators = getBuiltinOperators();
    validOperatorSymbols = getValidOperators();
    this.expression = substituteUnaryOperators(expression, builtInOperators);
  }

  private List<Character> getValidOperators()
  {
    return Arrays.asList('!', '#', '$', '&', ';', ':', '~', '<', '>', '|', '=');
  }

  private Map<String, Operator> getBuiltinOperators()
  {
    Operator add = new Operator("+")
    {
      @Override
      protected Object applyOperation(Object[] values)
      {
        if (values[0] instanceof Number && values[1] instanceof Number)
        {
          if (values[0] instanceof Byte)
          {
            byte value_0 = ((Number)values[0]).byteValue();
            if (values[1] instanceof Byte)
              return value_0 + ((Number)values[1]).byteValue();
            else if (values[1] instanceof Short)
              return value_0 + ((Number)values[1]).shortValue();
            else if (values[1] instanceof Integer)
              return value_0 + ((Number)values[1]).intValue();
            else if (values[1] instanceof Long)
              return value_0 + ((Number)values[1]).longValue();
            else if (values[1] instanceof Float)
              return value_0 + ((Number)values[1]).floatValue();
            else if (values[1] instanceof Double)
              return value_0 + ((Number)values[1]).doubleValue();
          }
          else if (values[0] instanceof Short)
          {
            short value_0 = ((Number)values[0]).shortValue();
            if (values[1] instanceof Byte)
              return value_0 + ((Number)values[1]).byteValue();
            else if (values[1] instanceof Short)
              return value_0 + ((Number)values[1]).shortValue();
            else if (values[1] instanceof Integer)
              return value_0 + ((Number)values[1]).intValue();
            else if (values[1] instanceof Long)
              return value_0 + ((Number)values[1]).longValue();
            else if (values[1] instanceof Float)
              return value_0 + ((Number)values[1]).floatValue();
            else if (values[1] instanceof Double)
              return value_0 + ((Number)values[1]).doubleValue();
          }
          else if (values[0] instanceof Integer)
          {
            int value_0 = ((Number)values[0]).intValue();
            if (values[1] instanceof Byte)
              return value_0 + ((Number)values[1]).byteValue();
            else if (values[1] instanceof Short)
              return value_0 + ((Number)values[1]).shortValue();
            else if (values[1] instanceof Integer)
              return value_0 + ((Number)values[1]).intValue();
            else if (values[1] instanceof Long)
              return value_0 + ((Number)values[1]).longValue();
            else if (values[1] instanceof Float)
              return value_0 + ((Number)values[1]).floatValue();
            else if (values[1] instanceof Double)
              return value_0 + ((Number)values[1]).doubleValue();
          }
          else if (values[0] instanceof Long)
          {
            long value_0 = ((Number)values[0]).longValue();
            if (values[1] instanceof Byte)
              return value_0 + ((Number)values[1]).byteValue();
            else if (values[1] instanceof Short)
              return value_0 + ((Number)values[1]).shortValue();
            else if (values[1] instanceof Integer)
              return value_0 + ((Number)values[1]).intValue();
            else if (values[1] instanceof Long)
              return value_0 + ((Number)values[1]).longValue();
            else if (values[1] instanceof Float)
              return value_0 + ((Number)values[1]).floatValue();
            else if (values[1] instanceof Double)
              return value_0 + ((Number)values[1]).doubleValue();
          }
          else if (values[0] instanceof Float)
          {
            float value_0 = ((Number)values[0]).floatValue();
            if (values[1] instanceof Byte)
              return value_0 + ((Number)values[1]).byteValue();
            else if (values[1] instanceof Short)
              return value_0 + ((Number)values[1]).shortValue();
            else if (values[1] instanceof Integer)
              return value_0 + ((Number)values[1]).intValue();
            else if (values[1] instanceof Long)
              return value_0 + ((Number)values[1]).longValue();
            else if (values[1] instanceof Float)
              return value_0 + ((Number)values[1]).floatValue();
            else if (values[1] instanceof Double)
              return value_0 + ((Number)values[1]).doubleValue();
          }
          else if (values[0] instanceof Double)
          {
            double value_0 = ((Number)values[0]).doubleValue();
            if (values[1] instanceof Byte)
              return value_0 + ((Number)values[1]).byteValue();
            else if (values[1] instanceof Short)
              return value_0 + ((Number)values[1]).shortValue();
            else if (values[1] instanceof Integer)
              return value_0 + ((Number)values[1]).intValue();
            else if (values[1] instanceof Long)
              return value_0 + ((Number)values[1]).longValue();
            else if (values[1] instanceof Float)
              return value_0 + ((Number)values[1]).floatValue();
            else if (values[1] instanceof Double)
              return value_0 + ((Number)values[1]).doubleValue();
          }
        }
        else if (values[0] instanceof Date)
        {
          values[0] = ((Date)values[0]).getTime();
          return applyOperation(values);
        }
        else if (values[1] instanceof Date)
        {
          values[1] = ((Date)values[1]).getTime();
          return applyOperation(values);
        }
        return values[0].toString() + values[1].toString();
      }
    };
    Operator sub = new Operator("-")
    {
      @Override
      protected Object applyOperation(Object[] values)
      {
        if (values[0] instanceof Number && values[1] instanceof Number)
        {
          if (values[0] instanceof Byte)
          {
            byte value_0 = ((Number)values[0]).byteValue();
            if (values[1] instanceof Byte)
              return value_0 - ((Number)values[1]).byteValue();
            else if (values[1] instanceof Short)
              return value_0 - ((Number)values[1]).shortValue();
            else if (values[1] instanceof Integer)
              return value_0 - ((Number)values[1]).intValue();
            else if (values[1] instanceof Long)
              return value_0 - ((Number)values[1]).longValue();
            else if (values[1] instanceof Float)
              return value_0 - ((Number)values[1]).floatValue();
            else if (values[1] instanceof Double)
              return value_0 - ((Number)values[1]).doubleValue();
          }
          else if (values[0] instanceof Short)
          {
            short value_0 = ((Number)values[0]).shortValue();
            if (values[1] instanceof Byte)
              return value_0 - ((Number)values[1]).byteValue();
            else if (values[1] instanceof Short)
              return value_0 - ((Number)values[1]).shortValue();
            else if (values[1] instanceof Integer)
              return value_0 - ((Number)values[1]).intValue();
            else if (values[1] instanceof Long)
              return value_0 - ((Number)values[1]).longValue();
            else if (values[1] instanceof Float)
              return value_0 - ((Number)values[1]).floatValue();
            else if (values[1] instanceof Double)
              return value_0 - ((Number)values[1]).doubleValue();
          }
          else if (values[0] instanceof Integer)
          {
            int value_0 = ((Number)values[0]).intValue();
            if (values[1] instanceof Byte)
              return value_0 - ((Number)values[1]).byteValue();
            else if (values[1] instanceof Short)
              return value_0 - ((Number)values[1]).shortValue();
            else if (values[1] instanceof Integer)
              return value_0 - ((Number)values[1]).intValue();
            else if (values[1] instanceof Long)
              return value_0 - ((Number)values[1]).longValue();
            else if (values[1] instanceof Float)
              return value_0 - ((Number)values[1]).floatValue();
            else if (values[1] instanceof Double)
              return value_0 - ((Number)values[1]).doubleValue();
          }
          else if (values[0] instanceof Long)
          {
            long value_0 = ((Number)values[0]).longValue();
            if (values[1] instanceof Byte)
              return value_0 - ((Number)values[1]).byteValue();
            else if (values[1] instanceof Short)
              return value_0 - ((Number)values[1]).shortValue();
            else if (values[1] instanceof Integer)
              return value_0 - ((Number)values[1]).intValue();
            else if (values[1] instanceof Long)
              return value_0 - ((Number)values[1]).longValue();
            else if (values[1] instanceof Float)
              return value_0 - ((Number)values[1]).floatValue();
            else if (values[1] instanceof Double)
              return value_0 - ((Number)values[1]).doubleValue();
          }
          else if (values[0] instanceof Float)
          {
            float value_0 = ((Number)values[0]).floatValue();
            if (values[1] instanceof Byte)
              return value_0 - ((Number)values[1]).byteValue();
            else if (values[1] instanceof Short)
              return value_0 - ((Number)values[1]).shortValue();
            else if (values[1] instanceof Integer)
              return value_0 - ((Number)values[1]).intValue();
            else if (values[1] instanceof Long)
              return value_0 - ((Number)values[1]).longValue();
            else if (values[1] instanceof Float)
              return value_0 - ((Number)values[1]).floatValue();
            else if (values[1] instanceof Double)
              return value_0 - ((Number)values[1]).doubleValue();
          }
          else if (values[0] instanceof Double)
          {
            double value_0 = ((Number)values[0]).doubleValue();
            if (values[1] instanceof Byte)
              return value_0 - ((Number)values[1]).byteValue();
            else if (values[1] instanceof Short)
              return value_0 - ((Number)values[1]).shortValue();
            else if (values[1] instanceof Integer)
              return value_0 - ((Number)values[1]).intValue();
            else if (values[1] instanceof Long)
              return value_0 - ((Number)values[1]).longValue();
            else if (values[1] instanceof Float)
              return value_0 - ((Number)values[1]).floatValue();
            else if (values[1] instanceof Double)
              return value_0 - ((Number)values[1]).doubleValue();
          }
        }
        else if (values[0] instanceof Date)
        {
          values[0] = ((Date)values[0]).getTime();
          return applyOperation(values);
        }
        else if (values[1] instanceof Date)
        {
          values[1] = ((Date)values[1]).getTime();
          return applyOperation(values);
        }
        return null;
      }
    };
    Operator mul = new Operator("*", 3)
    {
      @Override
      protected Object applyOperation(Object[] values)
      {
        if (values[0] instanceof Number && values[1] instanceof Number)
        {
          if (values[0] instanceof Byte)
          {
            byte value_0 = ((Number)values[0]).byteValue();
            if (values[1] instanceof Byte)
              return value_0 * ((Number)values[1]).byteValue();
            else if (values[1] instanceof Short)
              return value_0 * ((Number)values[1]).shortValue();
            else if (values[1] instanceof Integer)
              return value_0 * ((Number)values[1]).intValue();
            else if (values[1] instanceof Long)
              return value_0 * ((Number)values[1]).longValue();
            else if (values[1] instanceof Float)
              return value_0 * ((Number)values[1]).floatValue();
            else if (values[1] instanceof Double)
              return value_0 * ((Number)values[1]).doubleValue();
          }
          else if (values[0] instanceof Short)
          {
            short value_0 = ((Number)values[0]).shortValue();
            if (values[1] instanceof Byte)
              return value_0 * ((Number)values[1]).byteValue();
            else if (values[1] instanceof Short)
              return value_0 * ((Number)values[1]).shortValue();
            else if (values[1] instanceof Integer)
              return value_0 * ((Number)values[1]).intValue();
            else if (values[1] instanceof Long)
              return value_0 * ((Number)values[1]).longValue();
            else if (values[1] instanceof Float)
              return value_0 * ((Number)values[1]).floatValue();
            else if (values[1] instanceof Double)
              return value_0 * ((Number)values[1]).doubleValue();
          }
          else if (values[0] instanceof Integer)
          {
            int value_0 = ((Number)values[0]).intValue();
            if (values[1] instanceof Byte)
              return value_0 * ((Number)values[1]).byteValue();
            else if (values[1] instanceof Short)
              return value_0 * ((Number)values[1]).shortValue();
            else if (values[1] instanceof Integer)
              return value_0 * ((Number)values[1]).intValue();
            else if (values[1] instanceof Long)
              return value_0 * ((Number)values[1]).longValue();
            else if (values[1] instanceof Float)
              return value_0 * ((Number)values[1]).floatValue();
            else if (values[1] instanceof Double)
              return value_0 * ((Number)values[1]).doubleValue();
          }
          else if (values[0] instanceof Long)
          {
            long value_0 = ((Number)values[0]).longValue();
            if (values[1] instanceof Byte)
              return value_0 * ((Number)values[1]).byteValue();
            else if (values[1] instanceof Short)
              return value_0 * ((Number)values[1]).shortValue();
            else if (values[1] instanceof Integer)
              return value_0 * ((Number)values[1]).intValue();
            else if (values[1] instanceof Long)
              return value_0 * ((Number)values[1]).longValue();
            else if (values[1] instanceof Float)
              return value_0 * ((Number)values[1]).floatValue();
            else if (values[1] instanceof Double)
              return value_0 * ((Number)values[1]).doubleValue();
          }
          else if (values[0] instanceof Float)
          {
            float value_0 = ((Number)values[0]).floatValue();
            if (values[1] instanceof Byte)
              return value_0 * ((Number)values[1]).byteValue();
            else if (values[1] instanceof Short)
              return value_0 * ((Number)values[1]).shortValue();
            else if (values[1] instanceof Integer)
              return value_0 * ((Number)values[1]).intValue();
            else if (values[1] instanceof Long)
              return value_0 * ((Number)values[1]).longValue();
            else if (values[1] instanceof Float)
              return value_0 * ((Number)values[1]).floatValue();
            else if (values[1] instanceof Double)
              return value_0 * ((Number)values[1]).doubleValue();
          }
          else if (values[0] instanceof Double)
          {
            double value_0 = ((Number)values[0]).doubleValue();
            if (values[1] instanceof Byte)
              return value_0 * ((Number)values[1]).byteValue();
            else if (values[1] instanceof Short)
              return value_0 * ((Number)values[1]).shortValue();
            else if (values[1] instanceof Integer)
              return value_0 * ((Number)values[1]).intValue();
            else if (values[1] instanceof Long)
              return value_0 * ((Number)values[1]).longValue();
            else if (values[1] instanceof Float)
              return value_0 * ((Number)values[1]).floatValue();
            else if (values[1] instanceof Double)
              return value_0 * ((Number)values[1]).doubleValue();
          }
        }
        else if (values[0] instanceof Date)
        {
          values[0] = ((Date)values[0]).getTime();
          return applyOperation(values);
        }
        else if (values[1] instanceof Date)
        {
          values[1] = ((Date)values[1]).getTime();
          return applyOperation(values);
        }
        return null;
      }
    };
    Operator div = new Operator("/", 3)
    {
      @Override
      protected Object applyOperation(Object[] values)
      {
        if (values[0] instanceof Number && values[1] instanceof Number && ((Number)values[1]).doubleValue() != 0)
        {
          if (values[0] instanceof Byte)
          {
            byte value_0 = ((Number)values[0]).byteValue();
            if (values[1] instanceof Byte)
              return value_0 / ((Number)values[1]).byteValue();
            else if (values[1] instanceof Short)
              return value_0 / ((Number)values[1]).shortValue();
            else if (values[1] instanceof Integer)
              return value_0 / ((Number)values[1]).intValue();
            else if (values[1] instanceof Long)
              return value_0 / ((Number)values[1]).longValue();
            else if (values[1] instanceof Float)
              return value_0 / ((Number)values[1]).floatValue();
            else if (values[1] instanceof Double)
              return value_0 / ((Number)values[1]).doubleValue();
          }
          else if (values[0] instanceof Short)
          {
            short value_0 = ((Number)values[0]).shortValue();
            if (values[1] instanceof Byte)
              return value_0 / ((Number)values[1]).byteValue();
            else if (values[1] instanceof Short)
              return value_0 / ((Number)values[1]).shortValue();
            else if (values[1] instanceof Integer)
              return value_0 / ((Number)values[1]).intValue();
            else if (values[1] instanceof Long)
              return value_0 / ((Number)values[1]).longValue();
            else if (values[1] instanceof Float)
              return value_0 / ((Number)values[1]).floatValue();
            else if (values[1] instanceof Double)
              return value_0 / ((Number)values[1]).doubleValue();
          }
          else if (values[0] instanceof Integer)
          {
            int value_0 = ((Number)values[0]).intValue();
            if (values[1] instanceof Byte)
              return value_0 / ((Number)values[1]).byteValue();
            else if (values[1] instanceof Short)
              return value_0 / ((Number)values[1]).shortValue();
            else if (values[1] instanceof Integer)
              return value_0 / ((Number)values[1]).intValue();
            else if (values[1] instanceof Long)
              return value_0 / ((Number)values[1]).longValue();
            else if (values[1] instanceof Float)
              return value_0 / ((Number)values[1]).floatValue();
            else if (values[1] instanceof Double)
              return value_0 / ((Number)values[1]).doubleValue();
          }
          else if (values[0] instanceof Long)
          {
            long value_0 = ((Number)values[0]).longValue();
            if (values[1] instanceof Byte)
              return value_0 / ((Number)values[1]).byteValue();
            else if (values[1] instanceof Short)
              return value_0 / ((Number)values[1]).shortValue();
            else if (values[1] instanceof Integer)
              return value_0 / ((Number)values[1]).intValue();
            else if (values[1] instanceof Long)
              return value_0 / ((Number)values[1]).longValue();
            else if (values[1] instanceof Float)
              return value_0 / ((Number)values[1]).floatValue();
            else if (values[1] instanceof Double)
              return value_0 / ((Number)values[1]).doubleValue();
          }
          else if (values[0] instanceof Float)
          {
            float value_0 = ((Number)values[0]).floatValue();
            if (values[1] instanceof Byte)
              return value_0 / ((Number)values[1]).byteValue();
            else if (values[1] instanceof Short)
              return value_0 / ((Number)values[1]).shortValue();
            else if (values[1] instanceof Integer)
              return value_0 / ((Number)values[1]).intValue();
            else if (values[1] instanceof Long)
              return value_0 / ((Number)values[1]).longValue();
            else if (values[1] instanceof Float)
              return value_0 / ((Number)values[1]).floatValue();
            else if (values[1] instanceof Double)
              return value_0 / ((Number)values[1]).doubleValue();
          }
          else if (values[0] instanceof Double)
          {
            double value_0 = ((Number)values[0]).doubleValue();
            if (values[1] instanceof Byte)
              return value_0 / ((Number)values[1]).byteValue();
            else if (values[1] instanceof Short)
              return value_0 / ((Number)values[1]).shortValue();
            else if (values[1] instanceof Integer)
              return value_0 / ((Number)values[1]).intValue();
            else if (values[1] instanceof Long)
              return value_0 / ((Number)values[1]).longValue();
            else if (values[1] instanceof Float)
              return value_0 / ((Number)values[1]).floatValue();
            else if (values[1] instanceof Double)
              return value_0 / ((Number)values[1]).doubleValue();
          }
        }
        else if (values[0] instanceof Date)
        {
          values[0] = ((Date)values[0]).getTime();
          return applyOperation(values);
        }
        else if (values[1] instanceof Date)
        {
          values[1] = ((Date)values[1]).getTime();
          return applyOperation(values);
        }
        return null;
      }
    };
    Operator mod = new Operator("%", true, 3)
    {
      @Override
      protected Object applyOperation(Object[] values)
      {
        if (values[0] instanceof Number && values[1] instanceof Number && ((Number)values[1]).doubleValue() != 0)
        {
          if (values[0] instanceof Byte)
          {
            byte value_0 = ((Number)values[0]).byteValue();
            if (values[1] instanceof Byte)
              return value_0 % ((Number)values[1]).byteValue();
            else if (values[1] instanceof Short)
              return value_0 % ((Number)values[1]).shortValue();
            else if (values[1] instanceof Integer)
              return value_0 % ((Number)values[1]).intValue();
            else if (values[1] instanceof Long)
              return value_0 % ((Number)values[1]).longValue();
            else if (values[1] instanceof Float)
              return value_0 % ((Number)values[1]).floatValue();
            else if (values[1] instanceof Double)
              return value_0 % ((Number)values[1]).doubleValue();
          }
          else if (values[0] instanceof Short)
          {
            short value_0 = ((Number)values[0]).shortValue();
            if (values[1] instanceof Byte)
              return value_0 % ((Number)values[1]).byteValue();
            else if (values[1] instanceof Short)
              return value_0 % ((Number)values[1]).shortValue();
            else if (values[1] instanceof Integer)
              return value_0 % ((Number)values[1]).intValue();
            else if (values[1] instanceof Long)
              return value_0 % ((Number)values[1]).longValue();
            else if (values[1] instanceof Float)
              return value_0 % ((Number)values[1]).floatValue();
            else if (values[1] instanceof Double)
              return value_0 % ((Number)values[1]).doubleValue();
          }
          else if (values[0] instanceof Integer)
          {
            int value_0 = ((Number)values[0]).intValue();
            if (values[1] instanceof Byte)
              return value_0 % ((Number)values[1]).byteValue();
            else if (values[1] instanceof Short)
              return value_0 % ((Number)values[1]).shortValue();
            else if (values[1] instanceof Integer)
              return value_0 % ((Number)values[1]).intValue();
            else if (values[1] instanceof Long)
              return value_0 % ((Number)values[1]).longValue();
            else if (values[1] instanceof Float)
              return value_0 % ((Number)values[1]).floatValue();
            else if (values[1] instanceof Double)
              return value_0 % ((Number)values[1]).doubleValue();
          }
          else if (values[0] instanceof Long)
          {
            long value_0 = ((Number)values[0]).longValue();
            if (values[1] instanceof Byte)
              return value_0 % ((Number)values[1]).byteValue();
            else if (values[1] instanceof Short)
              return value_0 % ((Number)values[1]).shortValue();
            else if (values[1] instanceof Integer)
              return value_0 % ((Number)values[1]).intValue();
            else if (values[1] instanceof Long)
              return value_0 % ((Number)values[1]).longValue();
            else if (values[1] instanceof Float)
              return value_0 % ((Number)values[1]).floatValue();
            else if (values[1] instanceof Double)
              return value_0 % ((Number)values[1]).doubleValue();
          }
          else if (values[0] instanceof Float)
          {
            float value_0 = ((Number)values[0]).floatValue();
            if (values[1] instanceof Byte)
              return value_0 % ((Number)values[1]).byteValue();
            else if (values[1] instanceof Short)
              return value_0 % ((Number)values[1]).shortValue();
            else if (values[1] instanceof Integer)
              return value_0 % ((Number)values[1]).intValue();
            else if (values[1] instanceof Long)
              return value_0 % ((Number)values[1]).longValue();
            else if (values[1] instanceof Float)
              return value_0 % ((Number)values[1]).floatValue();
            else if (values[1] instanceof Double)
              return value_0 % ((Number)values[1]).doubleValue();
          }
          else if (values[0] instanceof Double)
          {
            double value_0 = ((Number)values[0]).doubleValue();
            if (values[1] instanceof Byte)
              return value_0 % ((Number)values[1]).byteValue();
            else if (values[1] instanceof Short)
              return value_0 % ((Number)values[1]).shortValue();
            else if (values[1] instanceof Integer)
              return value_0 % ((Number)values[1]).intValue();
            else if (values[1] instanceof Long)
              return value_0 % ((Number)values[1]).longValue();
            else if (values[1] instanceof Float)
              return value_0 % ((Number)values[1]).floatValue();
            else if (values[1] instanceof Double)
              return value_0 % ((Number)values[1]).doubleValue();
          }
        }
        else if (values[0] instanceof Date)
        {
          values[0] = ((Date)values[0]).getTime();
          return applyOperation(values);
        }
        else if (values[1] instanceof Date)
        {
          values[1] = ((Date)values[1]).getTime();
          return applyOperation(values);
        }
        return null;
      }
    };
    Operator umin = new Operator("\"", false, this.highUnaryPrecedence ? 8 : 5, 1)
    {
      @Override
      protected Object applyOperation(Object[] values)
      {
        if (values[0] instanceof Number)
        {
          if (values[0] instanceof Double)
            return -((Number)values[0]).doubleValue();
          else if (values[0] instanceof Float)
            return -((Number)values[0]).floatValue();
          else if (values[0] instanceof Long)
            return -((Number)values[0]).longValue();
          else if (values[0] instanceof Integer)
            return -((Number)values[0]).intValue();
          else if (values[0] instanceof Short)
            return -((Number)values[0]).shortValue();
          else if (values[0] instanceof Byte)
            return -((Number)values[0]).byteValue();
        }
        return values[0];
      }
    };
    Operator pow = new Operator("^", false, 5, 2)
    {
      @Override
      protected Object applyOperation(Object[] values)
      {
        return (values != null && values.length == 2 && values[0] instanceof Number && values[1] instanceof Number) ? Math.pow(((Number)values[0]).doubleValue(), ((Number)values[1]).doubleValue()) : null;
      }
    };
    Operator logicalAND = new Operator("&&", 11)
    {
      @Override
      protected Object applyOperation(Object[] values)
      {
        return (values[0] instanceof Boolean && values[1] instanceof Boolean) ? (Boolean) values[0] && (Boolean) values[1] : false;
      }
    };
    Operator logicalOR = new Operator("||", 12)
    {
      @Override
      protected Object applyOperation(Object[] values)
      {
        return (values[0] instanceof Boolean && values[1] instanceof Boolean) ? (Boolean) values[0] || (Boolean) values[1] : false;
      }
    };
    Operator logicalNOT = new Operator("!", true, 2, 1)
    {
      @Override
      protected Object applyOperation(Object[] values)
      {
        return (values[0] instanceof Boolean) ? !((Boolean)values[0]) : false;
      }
    };
    Operator gt = new Operator(">", false, 6)
    {
      @Override
      protected Object applyOperation(Object[] values)
      {
        if (values[0] instanceof Number && values[1] instanceof Number)
          return ((Number)values[0]).doubleValue() > ((Number)values[1]).doubleValue();
        else if (values[0] instanceof Date && values[1] instanceof Date)
          return ((Date)values[0]).compareTo((Date)values[1]) > 0;
        else if (values[0] instanceof String && values[1] instanceof String)
          return ((String)values[0]).compareTo((String)values[1]) > 0;
        else
          return false;
      }
    };
    Operator gte = new Operator(">=", false, 6)
    {
      @Override
      protected Object applyOperation(Object[] values)
      {
        if (values[0] instanceof Number && values[1] instanceof Number)
          return ((Number)values[0]).doubleValue() >= ((Number)values[1]).doubleValue();
        else if (values[0] instanceof Date && values[1] instanceof Date)
          return ((Date)values[0]).compareTo((Date)values[1]) >= 0;
        else if (values[0] instanceof String && values[1] instanceof String)
            return ((String)values[0]).compareTo((String)values[1]) >= 0;
        else
          return false;
      }
    };
    Operator eq = new Operator("==", false, 7)
    {
      @Override
      protected Object applyOperation(Object[] values)
      {
        if (values[0] instanceof Number && values[1] instanceof Number)
          return ((Number)values[0]).doubleValue() == ((Number)values[1]).doubleValue();
        else if (values[0] instanceof Date && values[1] instanceof Date)
          return ((Date)values[0]).compareTo((Date)values[1]) == 0;
        else if (values[0] instanceof Boolean && values[1] instanceof Boolean)
          return ((Boolean)values[0]).compareTo((Boolean)values[1]) == 0;
        else if (values[0] instanceof String && values[1] instanceof String)
          return ((String)values[0]).compareTo((String)values[1]) == 0;
        else
          return false;
      }
    };
    Operator ne = new Operator("!=", false, 7)
    {
      @Override
      protected Object applyOperation(Object[] values)
      {
        if (values[0] instanceof Number && values[1] instanceof Number)
          return ((Number)values[0]).doubleValue() != ((Number)values[1]).doubleValue();
        else if (values[0] instanceof Date && values[1] instanceof Date)
          return ((Date)values[0]).compareTo((Date)values[1]) != 0;
        else if (values[0] instanceof Boolean && values[1] instanceof Boolean)
          return ((Boolean)values[0]).compareTo((Boolean)values[1]) != 0;
        else if (values[0] instanceof String && values[1] instanceof String)
          return ((String)values[0]).compareTo((String)values[1]) != 0;
        else
          return false;
      }
    };
    Operator lt = new Operator("<", false, 6)
    {
      @Override
      protected Object applyOperation(Object[] values)
      {
        if (values[0] instanceof Number && values[1] instanceof Number)
          return ((Number)values[0]).doubleValue() < ((Number)values[1]).doubleValue();
        else if (values[0] instanceof Date && values[1] instanceof Date)
          return ((Date)values[0]).compareTo((Date)values[1]) < 0;
        else if (values[0] instanceof String && values[1] instanceof String)
          return ((String)values[0]).compareTo((String)values[1]) < 0;
        else
          return false;
      }
    };
    Operator lte = new Operator("<=", false, 6)
    {
      @Override
      protected Object applyOperation(Object[] values)
      {
        if (values[0] instanceof Number && values[1] instanceof Number)
          return ((Number)values[0]).doubleValue() <= ((Number)values[1]).doubleValue();
        else if (values[0] instanceof Date && values[1] instanceof Date)
          return ((Date)values[0]).compareTo((Date)values[1]) <= 0;
        else if (values[0] instanceof String && values[1] instanceof String)
          return ((String)values[0]).compareTo((String)values[1]) <= 0;
        else
          return false;
      }
    };
    
    Map<String, Operator> operations = new HashMap<String, Operator>();
    operations.put("+", add);
    operations.put("-", sub);
    operations.put("*", mul);
    operations.put("/", div);
    operations.put("%", mod);
    operations.put("\"", umin);
    operations.put("^", pow);
    operations.put("&&", logicalAND);
    operations.put("||", logicalOR);
    operations.put("!", logicalNOT);
    operations.put(">", gt);
    operations.put(">=", gte);
    operations.put("==", eq);
    operations.put("!=", ne);
    operations.put("<", lt);
    operations.put("<=", lte);
    return operations;
  }

  private Map<String, Function> getBuiltinFunctions()
  {
    try
    {
      Map<String, Function> functions = new HashMap<String, Function>();
      
      // Math functions
      functions.put("E", new Function("E", 0)
      {
        @Override
        public Object applyFunction(Object... args)
        {
          return Math.E;
        }
      });
      functions.put("PI", new Function("PI", 0)
      {
        @Override
        public Object applyFunction(Object... args)
        {
          return Math.PI;
        }
      });
      functions.put("abs", new Function("abs")
      {
        @Override
        public Object applyFunction(Object... args)
        {
          if (args != null && args.length == 1 && args[0] instanceof Number)
            if (args[0] instanceof Integer || args[0] instanceof Byte || args[0] instanceof Short)
              return Math.abs(((Number)args[0]).intValue());
            else if (args[0] instanceof Long)
              return Math.abs(((Number)args[0]).longValue());
            else if (args[0] instanceof Float)
              return Math.abs(((Number)args[0]).floatValue());
            else if (args[0] instanceof Double)
              return Math.abs(((Number)args[0]).doubleValue());
          return null;
        }
      });
      functions.put("acos", new Function("acos")
      {
        @Override
        public Object applyFunction(Object... args)
        {
          return (args != null && args.length == 1 && args[0] instanceof Number) ? Math.acos(((Number)args[0]).doubleValue()) : null;
        }
      });
      functions.put("asin", new Function("asin")
      {
        @Override
        public Object applyFunction(Object... args)
        {
          return (args != null && args.length == 1 && args[0] instanceof Number) ? Math.asin(((Number)args[0]).doubleValue()) : null;
        }
      });
      functions.put("atan", new Function("atan")
      {
        @Override
        public Object applyFunction(Object... args)
        {
          return (args != null && args.length == 1 && args[0] instanceof Number) ? Math.atan(((Number)args[0]).doubleValue()) : null;
        }
      });
      functions.put("atan2", new Function("atan2", 2)
      {
        @Override
        public Object applyFunction(Object... args)
        {
          return (args != null && args.length == 2 && args[0] instanceof Number && args[1] instanceof Number) ? Math.atan2(((Number)args[0]).doubleValue(), ((Number)args[1]).doubleValue()) : null;
        }
      });
      functions.put("cbrt", new Function("cbrt")
      {
        @Override
        public Object applyFunction(Object... args)
        {
          return (args != null && args.length == 1 && args[0] instanceof Number) ? Math.cbrt(((Number)args[0]).doubleValue()) : null;
        }
      });
      functions.put("ceil", new Function("ceil")
      {
        @Override
        public Object applyFunction(Object... args)
        {
          return (args != null && args.length == 1 && args[0] instanceof Number) ? Math.ceil(((Number)args[0]).doubleValue()) : null;
        }
      });
      functions.put("copySign", new Function("copySign", 2)
      {
        @Override
        public Object applyFunction(Object... args)
        {
          if (args != null && args.length == 2 && args[0] instanceof Number && args[1] instanceof Number)
            if (args[0] instanceof Double || args[1] instanceof Double)
              return Math.copySign(((Number)args[0]).doubleValue(), ((Number)args[1]).doubleValue());
            else
              return Math.copySign(((Number)args[0]).floatValue(), ((Number)args[1]).floatValue());
          return null;
        }
      });
      functions.put("cos", new Function("cos")
      {
        @Override
        public Object applyFunction(Object... args)
        {
          return (args != null && args.length == 1 && args[0] instanceof Number) ? Math.cos(((Number)args[0]).doubleValue()) : null;
        }
      });
      functions.put("cosh", new Function("cosh")
      {
        @Override
        public Object applyFunction(Object... args)
        {
          return (args != null && args.length == 1 && args[0] instanceof Number) ? Math.cosh(((Number)args[0]).doubleValue()) : null;
        }
      });
      functions.put("exp", new Function("exp")
      {
        @Override
        public Object applyFunction(Object... args)
        {
          return (args != null && args.length == 1 && args[0] instanceof Number) ? Math.exp(((Number)args[0]).doubleValue()) : null;
        }
      });
      functions.put("expm1", new Function("expm1")
      {
        @Override
        public Object applyFunction(Object... args)
        {
          return (args != null && args.length == 1 && args[0] instanceof Number) ? Math.expm1(((Number)args[0]).doubleValue()) : null;
        }
      });
      functions.put("floor", new Function("floor")
      {
        @Override
        public Object applyFunction(Object... args)
        {
          return (args != null && args.length == 1 && args[0] instanceof Number) ? Math.floor(((Number)args[0]).doubleValue()) : null;
        }
      });
      functions.put("getExponent", new Function("getExponent")
      {
        @Override
        public Object applyFunction(Object... args)
        {
          if (args != null && args.length == 1 && args[0] instanceof Number)
            if (args[0] instanceof Float || args[0] instanceof Integer || args[0] instanceof Byte || args[0] instanceof Short)
              return Math.getExponent(((Number)args[0]).floatValue());
            else if (args[0] instanceof Double || args[0] instanceof Long)
              return Math.getExponent(((Number)args[0]).doubleValue());
          return null;
        }
      });
      functions.put("hypot", new Function("hypot", 2)
      {
        @Override
        public Object applyFunction(Object... args)
        {
          return (args != null && args.length == 2 && args[0] instanceof Number && args[1] instanceof Number) ? Math.hypot(((Number)args[0]).doubleValue(), ((Number)args[1]).doubleValue()) : null;
        }
      });
      functions.put("IEEEremainder", new Function("IEEEremainder", 2)
      {
        @Override
        public Object applyFunction(Object... args)
        {
          return (args != null && args.length == 2 && args[0] instanceof Number && args[1] instanceof Number) ? Math.IEEEremainder(((Number)args[0]).doubleValue(), ((Number)args[1]).doubleValue()) : null;
        }
      });
      functions.put("log", new Function("log")
      {
        @Override
        public Object applyFunction(Object... args)
        {
          return (args != null && args.length == 1 && args[0] instanceof Number) ? Math.log(((Number)args[0]).doubleValue()) : null;
        }
      });
      functions.put("log10", new Function("log10")
      {
        @Override
        public Object applyFunction(Object... args)
        {
          return (args != null && args.length == 1 && args[0] instanceof Number) ? Math.log10(((Number)args[0]).doubleValue()) : null;
        }
      });
      functions.put("log1p", new Function("log1p")
      {
        @Override
        public Object applyFunction(Object... args)
        {
          return (args != null && args.length == 1 && args[0] instanceof Number) ? Math.log1p(((Number)args[0]).doubleValue()) : null;
        }
      });
      functions.put("max", new Function("max", 2)
      {
        @Override
        public Object applyFunction(Object... args)
        {
          if (args != null && args.length == 2 && args[0] instanceof Number && args[1] instanceof Number)
          {
            boolean isInt0 = args[0] instanceof Integer || args[0] instanceof Byte || args[0] instanceof Short;
            boolean isInt1 = args[1] instanceof Integer || args[1] instanceof Byte || args[1] instanceof Short;
            if (isInt0 && isInt1)
              return Math.max(((Number)args[0]).intValue(), ((Number)args[1]).intValue());
            else if ((args[0] instanceof Long || isInt0) && (args[1] instanceof Long || isInt1))
              return Math.max(((Number)args[0]).longValue(), ((Number)args[1]).longValue());
            else if (!(args[0] instanceof Double) && !(args[1] instanceof Double))
              return Math.max(((Number)args[0]).floatValue(), ((Number)args[1]).floatValue());
            else
              return Math.max(((Number)args[0]).doubleValue(), ((Number)args[1]).doubleValue());
          }
          return null;
        }
      });
      functions.put("min", new Function("min", 2)
      {
        @Override
        public Object applyFunction(Object... args)
        {
          if (args != null && args.length == 2 && args[0] instanceof Number && args[1] instanceof Number)
          {
            boolean isInt0 = args[0] instanceof Integer || args[0] instanceof Byte || args[0] instanceof Short;
            boolean isInt1 = args[1] instanceof Integer || args[1] instanceof Byte || args[1] instanceof Short;
            if (isInt0 && isInt1)
              return Math.min(((Number)args[0]).intValue(), ((Number)args[1]).intValue());
            else if ((args[0] instanceof Long || isInt0) && (args[1] instanceof Long || isInt1))
              return Math.min(((Number)args[0]).longValue(), ((Number)args[1]).longValue());
            else if (!(args[0] instanceof Double) && !(args[1] instanceof Double))
              return Math.min(((Number)args[0]).floatValue(), ((Number)args[1]).floatValue());
            else
              return Math.min(((Number)args[0]).doubleValue(), ((Number)args[1]).doubleValue());
          }
          return null;
        }
      });
      functions.put("nextAfter", new Function("nextAfter", 2)
      {
        @Override
        public Object applyFunction(Object... args)
        {
          if (args != null && args.length == 2 && args[0] instanceof Number && args[1] instanceof Number)
            if (args[0] instanceof Double)
              return Math.nextAfter(((Number)args[0]).doubleValue(), ((Number)args[1]).doubleValue());
            else
              return Math.nextAfter(((Number)args[0]).floatValue(), ((Number)args[1]).doubleValue());
          return null;
        }
      });
      functions.put("nextUp", new Function("nextUp")
      {
        @Override
        public Object applyFunction(Object... args)
        {
          if (args != null && args.length == 1 && args[0] instanceof Number)
            if (args[0] instanceof Double)
              return Math.nextUp(((Number)args[0]).doubleValue());
            else
              return Math.nextUp(((Number)args[0]).floatValue());
          return null;
        }
      });
      functions.put("pow", new Function("pow", 2)
      {
        @Override
        public Object applyFunction(Object... args)
        {
          return (args != null && args.length == 2 && args[0] instanceof Number && args[1] instanceof Number) ? Math.pow(((Number)args[0]).doubleValue(), ((Number)args[1]).doubleValue()) : null;
        }
      });
      functions.put("random", new Function("random", 0)
      {
        @Override
        public Object applyFunction(Object... args)
        {
          return Math.random();
        }
      });
      functions.put("rint", new Function("rint")
      {
        @Override
        public Object applyFunction(Object... args)
        {
          return (args != null && args.length == 1 && args[0] instanceof Number) ? Math.rint(((Number)args[0]).doubleValue()) : null;
        }
      });
      functions.put("round", new Function("round")
      {
        @Override
        public Object applyFunction(Object... args)
        {
          if (args != null && args.length == 1 && args[0] instanceof Number)
            if (args[0] instanceof Double)
              return Math.round(((Number)args[0]).doubleValue());
            else
              return Math.round(((Number)args[0]).floatValue());
          return null;
        }
      });
      functions.put("scalb", new Function("scalb", 2)
      {
        @Override
        public Object applyFunction(Object... args)
        {
          if (args != null && args.length == 2 && args[0] instanceof Number && args[1] instanceof Number)
            if (args[0] instanceof Double)
              return Math.scalb(((Number)args[0]).doubleValue(), ((Number)args[1]).intValue());
            else
              return Math.scalb(((Number)args[0]).floatValue(), ((Number)args[1]).intValue());
          return null;
        }
      });
      functions.put("signum", new Function("signum")
      {
        @Override
        public Object applyFunction(Object... args)
        {
          if (args != null && args.length == 1 && args[0] instanceof Number)
            if (args[0] instanceof Double)
              return Math.signum(((Number)args[0]).doubleValue());
            else
              return Math.signum(((Number)args[0]).floatValue());
          return null;
        }
      });
      functions.put("sin", new Function("sin")
      {
        @Override
        public Object applyFunction(Object... args)
        {
          return (args != null && args.length == 1 && args[0] instanceof Number) ? Math.sin(((Number)args[0]).doubleValue()) : null;
        }
      });
      functions.put("sinh", new Function("sinh")
      {
        @Override
        public Object applyFunction(Object... args)
        {
          return (args != null && args.length == 1 && args[0] instanceof Number) ? Math.sinh(((Number)args[0]).doubleValue()) : null;
        }
      });
      functions.put("sqrt", new Function("sqrt")
      {
        @Override
        public Object applyFunction(Object... args)
        {
          return (args != null && args.length == 1 && args[0] instanceof Number) ? Math.sqrt(((Number)args[0]).doubleValue()) : null;
        }
      });
      functions.put("tan", new Function("tan")
      {
        @Override
        public Object applyFunction(Object... args)
        {
          return (args != null && args.length == 1 && args[0] instanceof Number) ? Math.tan(((Number)args[0]).doubleValue()) : null;
        }
      });
      functions.put("tanh", new Function("tanh")
      {
        @Override
        public Object applyFunction(Object... args)
        {
          return (args != null && args.length == 1 && args[0] instanceof Number) ? Math.tanh(((Number)args[0]).doubleValue()) : null;
        }
      });
      functions.put("toDegrees", new Function("toDegrees")
      {
        @Override
        public Object applyFunction(Object... args)
        {
          return (args != null && args.length == 1 && args[0] instanceof Number) ? Math.toDegrees(((Number)args[0]).doubleValue()) : null;
        }
      });
      functions.put("toRadians", new Function("toRadians")
      {
        @Override
        public Object applyFunction(Object... args)
        {
          return (args != null && args.length == 1 && args[0] instanceof Number) ? Math.toRadians(((Number)args[0]).doubleValue()) : null;
        }
      });
      functions.put("ulp", new Function("ulp")
      {
        @Override
        public Object applyFunction(Object... args)
        {
          if (args != null && args.length == 1 && args[0] instanceof Number)
            if (args[0] instanceof Double)
              return Math.ulp(((Number)args[0]).doubleValue());
            else
              return Math.ulp(((Number)args[0]).floatValue());
          return null;
        }
      });
      
      // String manipulation functions
      functions.put("length", new Function("length")
      {
        @Override
        public Object applyFunction(Object... args)
        {
          return (args != null && args.length == 1) ? String.valueOf(args[0]).length() : null;
        }
      });
      
      functions.put("isEmpty", new Function("isEmpty")
      {
        @Override
        public Object applyFunction(Object... args)
        {
          return (args != null && args.length == 1) ? String.valueOf(args[0]).isEmpty() : null;
        }
      });
      
      functions.put("isNull", new Function("isNull")
      {
        @Override
        public Object applyFunction(Object... args)
        {
          return (args != null && args.length == 1) ? args[0] == null : null;
        }
      });
      
      functions.put("equals", new Function("equals", 2)
      {
        @Override
        public Object applyFunction(Object... args)
        {
          if (args != null && args.length == 2)
          {
            if (args[0] != null && args[1] != null)
            {
              if (args[0] instanceof Number && args[1] instanceof Number)
                return ((Number)args[0]).doubleValue() == ((Number)args[1]).doubleValue();
              else if (args[0] instanceof Date && args[1] instanceof Date)
                return ((Date)args[0]).compareTo((Date)args[1]) == 0;
              else if (args[0] instanceof Boolean && args[1] instanceof Boolean)
                return ((Boolean)args[0]).compareTo((Boolean)args[1]) == 0;
              else if (args[0] instanceof String && args[1] instanceof String)
                return ((String)args[0]).compareTo((String)args[1]) == 0;
            }
            else if(args[0] == null && args[1] == null)
              return true;
          }
          return false;
        }
      });
      
      functions.put("equalsIgnoreCase", new Function("equalsIgnoreCase", 2)
      {
        @Override
        public Object applyFunction(Object... args)
        {
          return (args != null && args.length == 2) ? String.valueOf(args[0]).equalsIgnoreCase(String.valueOf(args[1])) : false;
        }
      });
      
      functions.put("compareTo", new Function("compareTo", 2)
      {
        @Override
        public Object applyFunction(Object... args)
        {
          if (args != null && args.length == 2)
          {
            if (args[0] != null && args[1] != null)
            {
              if (args[0] instanceof Number && args[1] instanceof Number)
                return Double.compare(((Number)args[0]).doubleValue(), ((Number)args[1]).doubleValue());
              else if (args[0] instanceof Date && args[1] instanceof Date)
                return ((Date)args[0]).compareTo((Date)args[1]);
              else if (args[0] instanceof Boolean && args[1] instanceof Boolean)
                return ((Boolean)args[0]).compareTo((Boolean)args[1]);
              else if (args[0] instanceof String && args[1] instanceof String)
                return ((String)args[0]).compareTo((String)args[1]);
            }
            else if(args[0] == null && args[1] == null)
              return 0;
          }
          return null;
        }
      });
      
      functions.put("compareToIgnoreCase", new Function("compareToIgnoreCase", 2)
      {
        @Override
        public Object applyFunction(Object... args)
        {
          return (args != null && args.length == 2) ? String.valueOf(args[0]).compareToIgnoreCase(String.valueOf(args[1])) : null;
        }
      });
      
      functions.put("startsWith", new Function("startsWith", 2)
      {
        @Override
        public Object applyFunction(Object... args)
        {
          return (args != null && args.length == 2) ? String.valueOf(args[0]).startsWith(String.valueOf(args[1])) : null;
        }
      });
      
      functions.put("endsWith", new Function("endsWith", 2)
      {
        @Override
        public Object applyFunction(Object... args)
        {
          return (args != null && args.length == 2) ? String.valueOf(args[0]).endsWith(String.valueOf(args[1])) : null;
        }
      });
      
      functions.put("indexOf", new Function("indexOf", 3)
      {
        @Override
        public Object applyFunction(Object... args)
        {
          if (args != null && args.length == 3)
          {
            String src = null;
            String str = null;
            Integer fromIndex = null;
            if (args[0] != null)
            {
              src = String.valueOf(args[0]);
              str = String.valueOf(args[1]);
              fromIndex = Converter.convertToInteger(args[2]);
            }
            else
            {
              src = String.valueOf(args[1]);
              str = String.valueOf(args[2]);
            }
            try
            {
              return (fromIndex != null) ? src.indexOf(str, fromIndex) : src.indexOf(str);
            }
            catch (IndexOutOfBoundsException e)
            {
              ;
            }
            return src;
          }
          return null;
        }
      });
      
      functions.put("lastIndexOf", new Function("lastIndexOf", 3)
      {
        @Override
        public Object applyFunction(Object... args)
        {
          if (args != null && args.length == 3)
          {
            String src = null;
            String str = null;
            Integer fromIndex = null;
            if (args[0] != null)
            {
              src = String.valueOf(args[0]);
              str = String.valueOf(args[1]);
              fromIndex = Converter.convertToInteger(args[2]);
            }
            else
            {
              src = String.valueOf(args[1]);
              str = String.valueOf(args[2]);
            }
            try
            {
              return (fromIndex != null) ? src.lastIndexOf(str, fromIndex) : src.lastIndexOf(str);
            }
            catch (IndexOutOfBoundsException e)
            {
              ;
            }
            return src;
          }
          return null;
        }
      });
      
      functions.put("substring", new Function("substring", 3)
      {
        @Override
        public Object applyFunction(Object... args)
        {
          if (args != null && args.length == 3)
          {
            String src = null;
            Integer beginIndex = null;
            Integer endIndex = null;
            
            if (args[0] != null)
            {
              src = String.valueOf(args[0]);
              beginIndex = Converter.convertToInteger(args[1]);
              endIndex = Converter.convertToInteger(args[2]);
            }
            else
            {
              src = String.valueOf(args[1]);
              beginIndex = Converter.convertToInteger(args[2]);
            }
            try
            {
              return endIndex != null ? src.substring(beginIndex, endIndex) : src.substring(beginIndex);
            }
            catch (IndexOutOfBoundsException e)
            {
              ;
            }
            return src;
          }
          return null;
        }
      });
      
      functions.put("concat", new Function("concat", 2)
      {
        @Override
        public Object applyFunction(Object... args)
        {
          return (args != null && args.length == 2) ? String.valueOf(args[0]).concat(String.valueOf(args[1])) : null;
        }
      });
      
      functions.put("matches", new Function("matches", 2)
      {
        @Override
        public Object applyFunction(Object... args)
        {
          return (args != null && args.length == 2) ? String.valueOf(args[0]).matches(String.valueOf(args[1])) : null;
        }
      });
      
      functions.put("contains", new Function("contains", 2)
      {
        @Override
        public Object applyFunction(Object... args)
        {
          return (args != null && args.length == 2) ? String.valueOf(args[0]).contains(String.valueOf(args[1])) : null;
        }
      });
      
      functions.put("replaceFirst", new Function("replaceFirst", 3)
      {
        @Override
        public Object applyFunction(Object... args)
        {
          return (args != null && args.length == 3) ? String.valueOf(args[0]).replaceFirst(String.valueOf(args[1]), String.valueOf(args[2])) : null;
        }
      });
      
      functions.put("replaceAll", new Function("replaceAll", 3)
      {
        @Override
        public Object applyFunction(Object... args)
        {
          return (args != null && args.length == 3) ? String.valueOf(args[0]).replaceAll(String.valueOf(args[1]), String.valueOf(args[2])) : null;
        }
      });
      
      functions.put("replace", new Function("replace", 3)
      {
        @Override
        public Object applyFunction(Object... args)
        {
          return (args != null && args.length == 3) ? String.valueOf(args[0]).replace(String.valueOf(args[1]), String.valueOf(args[2])) : null;
        }
      });
      
      functions.put("toLowerCase", new Function("toLowerCase")
      {
        @Override
        public Object applyFunction(Object... args)
        {
          return (args != null && args.length == 1) ? String.valueOf(args[0]).toLowerCase() : null;
        }
      });
      
      functions.put("toUpperCase", new Function("toUpperCase")
      {
        @Override
        public Object applyFunction(Object... args)
        {
          return (args != null && args.length == 1) ? String.valueOf(args[0]).toUpperCase() : null;
        }
      });
      
      functions.put("trim", new Function("trim")
      {
        @Override
        public Object applyFunction(Object... args)
        {
          return (args != null && args.length == 1) ? String.valueOf(args[0]).trim() : null;
        }
      });
      
      functions.put("valueOf", new Function("valueOf")
      {
        @Override
        public Object applyFunction(Object... args)
        {
          return (args != null && args.length == 1) ? String.valueOf(args[0]) : null;
        }
      });
      
      // GeoEvent processor functions
      functions.put("receivedTime", new Function("receivedTime", 0)
      {
        @Override
        public Object applyFunction(Object... args)
        {
          if (geoEvent != null)
            if (geoEvent.hasProperty(GeoEventPropertyName.RECEIVED_TIME))
              return geoEvent.getProperty(GeoEventPropertyName.RECEIVED_TIME);
          return null;
        }
      });
      functions.put("currentTime", new Function("currentTime", 0)
      {
        @Override
        public Object applyFunction(Object... args)
        {
          return new Date();
        }
      });
      functions.put("previousGeoEvent", new Function("previousGeoEvent")
      {
        @Override
        public Object applyFunction(Object... args)
        {
          if (args != null && args.length == 1)
          {
            GeoEvent previousGeoEvent = geoEventCache.getPreviousGeoEvent(geoEvent);
            if (previousGeoEvent != null)
            {
              FieldExpression fe = new FieldExpression(String.valueOf(args[0]));
              if (fe.isValid())
              {
                Field f = previousGeoEvent.getField(fe);
                if (f != null)
                  return f.getValue();
              }
            }
          }
          return null;
        }
      });
      return functions;
    }
    catch (InvalidFunctionException e)
    {
      // this should not happen...
      throw new RuntimeException(e);
    }
  }

  public boolean isCacheRequired()
  {
    try
    {
      for (final Token token : new Tokenizer(null, functions, builtInOperators).getTokens(expression))
      {
        if (token instanceof FunctionToken)
          if ("previousGeoEvent".equals(((FunctionToken)token).getName()))
            return true;
      }
    }
    catch (Exception e)
    {
      ;
    }
    return false;
  }
  /**
   * build a new {@link Calculable} from the expression
   * 
   * @return the {@link Calculable} which can be used to evaluate the expression
   * @throws UnknownFunctionException
   *           when an unrecognized function name is used in the expression
   * @throws UnparsableExpressionException
   *           if the expression could not be parsed
   */
  public Calculable build() throws UnknownFunctionException, UnparsableExpressionException
  {
    for (Operator op : customOperators.values())
      for (int i=0; i < op.symbol.length(); i++)
        if (!validOperatorSymbols.contains(op.symbol.charAt(i)))
          throw new UnparsableExpressionException("" + op.symbol + " is not a valid symbol for an operator please choose from: !,#,§,$,&,;,:,~,<,>,|,=");
    builtInOperators.putAll(customOperators);
    if (geoEvent != null)
    {
      for (FieldDefinition fd : geoEvent.getGeoEventDefinition().getFieldDefinitions())
        checkVariableName(fd.getName());
      return RPNConverter.toRPNExpression(expression, geoEvent, functions, builtInOperators);
    }
    return null;
  }

  private void checkVariableName(String varName) throws UnparsableExpressionException
  {
    char[] name = varName.toCharArray();
    for (int i=0; i < name.length; i++)
    {
      if (i == 0)
      {
        if (!Character.isLetter(name[i]) && name[i] != '_')
          throw new UnparsableExpressionException(varName + " is not a valid variable name: character '" + name[i] + " at " + i);
      }
      else
      {
        if (!Character.isLetter(name[i]) && !Character.isDigit(name[i]) && name[i] != '_')
          throw new UnparsableExpressionException(varName + " is not a valid variable name: character '" + name[i] + " at " + i);
      }
    }
  }

  private static String substituteUnaryOperators(String expr, Map<String, Operator> operators)
  {
    final StringBuilder exprBuilder = new StringBuilder(expr.length());
    final char[] data = expr.toCharArray();
    char lastChar = ' ';
    boolean isLiteral = false;
    StringBuilder lastOperation = new StringBuilder();
    for (int i=0; i < expr.length(); i++)
    {
      if (exprBuilder.length() > 0)
        lastChar = exprBuilder.charAt(exprBuilder.length() - 1);
      final char c = data[i];
      if (i > 0 && isOperatorCharacter(expr.charAt(i - 1), operators))
      {
        if (!operators.containsKey(lastOperation.toString() + expr.charAt(i - 1)))
          lastOperation.delete(0, lastOperation.length());
        lastOperation.append(expr.charAt(i - 1));
      }
      else if (i > 0 && !Character.isWhitespace(expr.charAt(i - 1)))
        lastOperation.delete(0, lastOperation.length());
      switch (c)
      {
        case '+':
          if (i > 0 && lastChar != '(' && lastChar != ',' && operators.get(lastOperation.toString()) == null)
            exprBuilder.append(c);
          break;
        case '-':
          if (i > 0 && lastChar != '(' && lastChar != ',' && operators.get(lastOperation.toString()) == null)
            exprBuilder.append(c);
          else
          {
            isLiteral = false;
            exprBuilder.append('\"');
          }
          break;
        case '\'':
          isLiteral = !isLiteral;
          exprBuilder.append(c);
          break;
        default:
          if (isLiteral)
            exprBuilder.append(c);
          else if (!Character.isWhitespace(c))
            exprBuilder.append(c);
      }
    }
    return exprBuilder.toString();
  }
  private static boolean isOperatorCharacter(char c, Map<String, Operator> operators)
  {
    for (String symbol : operators.keySet())
      if (symbol.indexOf(c) != -1)
        return true;
    return false;
  }
  /**
   * add a custom function instance for the evaluator to recognize
   * 
   * @param function
   *          the {@link Function} to add
   * @return the {@link ExpressionBuilder} instance
   */
  public ExpressionBuilder withCustomFunction(Function function)
  {
    functions.put(function.name, function);
    return this;
  }

  public ExpressionBuilder withCustomFunctions(Collection<Function> functions)
  {
    for (Function f : functions)
      withCustomFunction(f);
    return this;
  }

  /**
   * set a {@link Operator} to be used in the expression
   * 
   * @param operation
   *          the {@link Operator} to be used
   * @return the {@link ExpressionBuilder} instance
   */
  public ExpressionBuilder withOperation(Operator operation)
  {
    customOperators.put(operation.symbol, operation);
    return this;
  }

  /**
   * set a {@link Collection} of {@link Operator} to use in the expression
   * 
   * @param operations
   *          the {@link Collection} of {@link Operator} to use
   * @return the {@link ExpressionBuilder} instance
   */
  public ExpressionBuilder withOperations(Collection<Operator> operations)
  {
    for (Operator op : operations)
      withOperation(op);
    return this;
  }

  /**
   * set the mathematical expression for parsing
   * 
   * @param expression
   *          a mathematical expression
   * @return the {@link ExpressionBuilder} instance
   */
  public ExpressionBuilder withExpression(String expression)
  {
    this.expression = expression;
    return this;
  }
  
  public static void main(String[] args)
  {
    Boolean b = true;
    Integer i = 1;
    Integer j = new Integer(b.toString());
    System.out.println(i+j);
  }
}