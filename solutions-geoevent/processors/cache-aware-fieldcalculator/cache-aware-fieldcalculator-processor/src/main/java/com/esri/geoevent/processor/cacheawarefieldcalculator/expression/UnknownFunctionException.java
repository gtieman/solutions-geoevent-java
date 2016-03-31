package com.esri.geoevent.processor.cacheawarefieldcalculator.expression;

/**
 * Exception for handling unknown Functions.
 * 
 * @see FunctionToken
 */
public class UnknownFunctionException extends Exception
{
  private static final long serialVersionUID = 1L;

  /**
   * construct a new {@link UnknownFunctionException}
   * 
   * @param functionName
   *          the function name which could not be found
   */
  public UnknownFunctionException(String functionName)
  {
    super("Unknown function: " + functionName);
  }
}