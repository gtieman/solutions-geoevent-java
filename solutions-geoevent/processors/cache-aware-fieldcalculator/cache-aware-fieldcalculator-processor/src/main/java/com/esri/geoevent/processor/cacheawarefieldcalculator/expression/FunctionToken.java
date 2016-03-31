package com.esri.geoevent.processor.cacheawarefieldcalculator.expression;

import java.util.List;
import java.util.Stack;

import com.esri.ges.core.geoevent.GeoEvent;

public class FunctionToken extends CalculationToken
{
  final String functionName;
  final Function function;

  FunctionToken(String value, Function function) throws UnknownFunctionException
  {
    super(value);
    if (value == null)
      throw new UnknownFunctionException(value);
    try
    {
      this.functionName = function.name;
      this.function = function;
    }
    catch (IllegalArgumentException e)
    {
      throw new UnknownFunctionException(value);
    }
  }

  String getName()
  {
    return functionName;
  }

  @Override
  public boolean equals(Object obj)
  {
    return (obj instanceof FunctionToken) ? functionName.equals(((FunctionToken) obj).functionName) : false;
  }

  @Override
  public int hashCode()
  {
    return functionName.hashCode();
  }

  @Override
  void mutateStackForCalculation(Stack<Object> stack, GeoEvent geoEvent)
  {
    Object[] args = new Object[function.argc];
    for (int i=0; i < function.argc; i++)
      args[i] = stack.isEmpty() ? null : stack.pop();
    stack.push(function.applyFunction(ArrayUtil.reverse(args)));
  }
  
  @Override
  void mutateStackForInfixTranslation(Stack<Token> operatorStack, List<Token> tokens)
  {
    operatorStack.push(this);
  }
}