package com.esri.geoevent.processor.cacheawarefieldcalculator.expression;

import com.esri.ges.util.Validator;

public abstract class Function
{
  final int argc;
  final String name;

  /**
   * create a new single value input CustomFunction with a set name
   * 
   * @param value
   *          the name of the function (e.g. foo)
   */
  protected Function(String name) throws InvalidFunctionException
  {
    this.argc = 1;
    this.name = Validator.compactSpaces(name).trim();
    if (this.name.isEmpty())
      throw new InvalidFunctionException("function name cannot be empty.");
    int firstChar = (int) name.charAt(0);
    if ((firstChar < 65 || firstChar > 90) && (firstChar < 97 || firstChar > 122))
      throw new InvalidFunctionException("function name have to start with a lowercase or uppercase character.");
  }

  /**
   * create a new single value input CustomFunction with a set name
   * 
   * @param value
   *          the name of the function (e.g. foo)
   */
  protected Function(String name, int argumentCount) throws InvalidFunctionException
  {
    if (argumentCount < 0)
      throw new InvalidFunctionException("function argument count cannot be negative number.");
    this.argc = argumentCount;
    this.name = Validator.compactSpaces(name).trim();
    if (this.name.isEmpty())
      throw new InvalidFunctionException("function name cannot be empty.");
    int firstChar = (int) name.charAt(0);
    if ((firstChar < 65 || firstChar > 90) && (firstChar < 97 || firstChar > 122))
      throw new InvalidFunctionException("function name have to start with a lowercase or uppercase character.");
  }
  
  public abstract Object applyFunction(Object... args);
}