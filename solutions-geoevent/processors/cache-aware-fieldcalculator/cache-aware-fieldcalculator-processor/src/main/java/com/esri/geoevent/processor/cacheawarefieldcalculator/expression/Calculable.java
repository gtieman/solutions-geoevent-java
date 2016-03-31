package com.esri.geoevent.processor.cacheawarefieldcalculator.expression;

/**
 * This is the basic result class of the expression {@link ExpressionBuilder}
 */
public interface Calculable
{
  /**
   * calculate the result of the expression
   * 
   * @return the result of the calculation
   */
  public Object calculate();
  /**
   * return the expression in reverse polish postfix notation
   * 
   * @return the expression used to construct this {@link Calculable}
   */
  public String getExpression();
}