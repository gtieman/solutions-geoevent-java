package com.esri.geoevent.processor.cacheawarefieldcalculator.expression;

import java.util.List;
import java.util.Stack;

public class ParenthesisToken extends Token
{
  ParenthesisToken(String value)
  {
    super(value);
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof ParenthesisToken)
    {
      final ParenthesisToken t = (ParenthesisToken) obj;
      return t.getValue().equals(this.getValue());
    }
    return false;
  }

  @Override
  public int hashCode()
  {
    return getValue().hashCode();
  }

  /**
   * check the direction of the parenthesis
   * 
   * @return true if it's a left parenthesis (open) false if it is a right
   *         parenthesis (closed)
   */
  boolean isOpen()
  {
    return getValue().equals("(") || getValue().equals("[") || getValue().equals("{");
  }

  @Override
  void mutateStackForInfixTranslation(Stack<Token> operatorStack, List<Token> tokens)
  {
    if (this.isOpen())
      operatorStack.push(this);
    else
    {
      Token next;
      while ((next = operatorStack.peek()) instanceof OperatorToken || next instanceof FunctionToken ||(next instanceof ParenthesisToken && !((ParenthesisToken) next).isOpen()))
        tokens.add(operatorStack.pop());
      operatorStack.pop();
    }
  }
}