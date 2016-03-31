package com.esri.geoevent.processor.cacheawarefieldcalculator.expression;

import java.util.List;
import java.util.Stack;

import com.esri.ges.core.geoevent.GeoEvent;

public class RPNExpression implements Calculable
{
  final List<Token> tokens;
  final GeoEvent geoEvent;

  public RPNExpression(List<Token> tokens, final GeoEvent geoEvent)
  {
    super();
    this.tokens = tokens;
    this.geoEvent = geoEvent;
  }
  
  /**
   * calculate the result of the expression
   * 
   * @return the result of the calculation
   * @throws IllegalArgumentException
   *           if the variables are invalid
   */
  public Object calculate() throws IllegalArgumentException
  {
    final Stack<Object> stack = new Stack<Object>();
    for (final Token t : tokens)
      ((CalculationToken)t).mutateStackForCalculation(stack, geoEvent);
    return stack.pop();
  }
  
  public String getExpression()
  {
    StringBuilder sb = new StringBuilder("");
    for (Token token : tokens)
      sb.append(token.getValue()).append(" ");
    return sb.toString().trim();
  }
}