package com.esri.geoevent.processor.cacheawarefieldcalculator.expression;

import java.util.List;
import java.util.Stack;

public abstract class Token
{
  protected final Object value;

  /**
   * construct a new {@link Token}
   * 
   * @param value
   *          the value of the {@link Token}
   */
  Token(Object value)
  {
    super();
    this.value = value;
  }

  /**
   * get the value (String representation) of the token
   * 
   * @return the value
   */
  String getValue()
  {
    return value.toString();
  }

  abstract void mutateStackForInfixTranslation(Stack<Token> operatorStack, List<Token> tokens);
}