package com.esri.geoevent.processor.cacheawarefieldcalculator.expression;

import java.util.List;
import java.util.Stack;

public class FunctionSeparatorToken extends Token
{
  FunctionSeparatorToken()
  {
    super(",");
  }

  @Override
  void mutateStackForInfixTranslation(Stack<Token> operatorStack, List<Token> tokens)
  {
    Token token;
    while (!((token = operatorStack.peek()) instanceof ParenthesisToken) && !token.getValue().equals("("))
      tokens.add(operatorStack.pop());
  }
}