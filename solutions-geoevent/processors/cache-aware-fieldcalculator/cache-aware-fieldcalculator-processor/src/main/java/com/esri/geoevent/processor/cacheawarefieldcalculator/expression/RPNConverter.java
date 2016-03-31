package com.esri.geoevent.processor.cacheawarefieldcalculator.expression;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.esri.ges.core.geoevent.GeoEvent;

public abstract class RPNConverter
{

  static RPNExpression toRPNExpression(String infix, GeoEvent geoEvent, Map<String, Function> customFunctions, Map<String, Operator> operators) throws UnknownFunctionException, UnparsableExpressionException
  {
    final List<Token> tokens = new ArrayList<Token>();
    final Stack<Token> operatorStack = new Stack<Token>();
    for (final Token token : new Tokenizer(geoEvent.getGeoEventDefinition(), customFunctions, operators).getTokens(infix))
      token.mutateStackForInfixTranslation(operatorStack, tokens);
    // all tokens read, put the rest of the operations on the output;
    while (operatorStack.size() > 0)
      tokens.add(operatorStack.pop());
    return new RPNExpression(tokens, geoEvent);
  }

}