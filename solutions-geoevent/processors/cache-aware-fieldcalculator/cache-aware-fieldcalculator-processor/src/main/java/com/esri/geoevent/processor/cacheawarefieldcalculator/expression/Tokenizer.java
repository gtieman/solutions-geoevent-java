package com.esri.geoevent.processor.cacheawarefieldcalculator.expression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.esri.ges.core.geoevent.FieldExpression;
import com.esri.ges.core.geoevent.GeoEventDefinition;
import com.esri.ges.util.Validator;

public class Tokenizer
{
  private final GeoEventDefinition geoEventDefinition;
  private final Map<String, Function> functions;
  private final Map<String, Operator> operators;

  Tokenizer(GeoEventDefinition geoEventDefinition, Map<String, Function> functions, Map<String, Operator> operators)
  {
    super();
    this.geoEventDefinition = geoEventDefinition;
    this.functions = (functions != null) ? functions : new HashMap<String, Function>();
    this.operators = (operators != null) ? operators : new HashMap<String, Operator>();
  }
  
  List<Token> getTokens(final String expression) throws UnparsableExpressionException, UnknownFunctionException
  {
    final List<Token> tokens = new ArrayList<Token>();
    final char[] chars = expression.toCharArray();
    // iterate over the chars and fork on different types of input
    Token lastToken;
    for (int i = 0; i < chars.length; i++)
    {
      char c = chars[i];
      if (c == ' ')
        continue;
      if (c == '\'')
      {
        final StringBuilder strBuilder = new StringBuilder(1);
        // handle the literals of the expression
        strBuilder.append(c);
        int strLen = 1;
        while (chars.length > i + strLen && chars[i + strLen] != '\'')
        {
          strBuilder.append(chars[i + strLen]);
          strLen++;
        }
        i += strLen;
        strBuilder.append('\'');
        lastToken = new ObjectToken(parseValue(strBuilder.toString()));
      }
      else if (Character.isDigit(c))
      {
        final StringBuilder valueBuilder = new StringBuilder(1);
        // handle the numbers of the expression
        valueBuilder.append(c);
        int numberLen = 1;
        while (chars.length > i + numberLen && isDigitOrDecimalSeparator(chars[i + numberLen]))
        {
          valueBuilder.append(chars[i + numberLen]);
          numberLen++;
        }
        i += numberLen - 1;
        lastToken = new ObjectToken(parseValue(valueBuilder.toString()));
      }
      else if (Character.isLetter(c) || c == '_')
      {
        // can be a variable or function
        final StringBuilder nameBuilder = new StringBuilder();
        nameBuilder.append(c);
        int offset = 1;
        char prev = c;
        while (chars.length > i + offset && isVariableOrFunctionCharacter(prev, chars[i + offset]))
        {
          nameBuilder.append(chars[i + offset++]);
          prev = nameBuilder.charAt(nameBuilder.length()-1);
        }
        i += offset-1;
        String name = nameBuilder.toString();
          lastToken = isFunction(name) ? new FunctionToken(name, functions.get(name)) : createVariableToken(name);
      }
      else if (c == ',')
        lastToken = new FunctionSeparatorToken();
      else if (isOperatorCharacter(c))
      {
        // might be an operation
        StringBuilder symbolBuilder = new StringBuilder();
        symbolBuilder.append(c);
        int offset = 1;
        while (chars.length > i + offset && (isOperatorCharacter(chars[i + offset])) && isOperatorStart(symbolBuilder.toString() + chars[i + offset]))
        {
          symbolBuilder.append(chars[i + offset]);
          offset++;
        }
        String symbol = symbolBuilder.toString();
        if (operators.containsKey(symbol))
        {
          i += offset-1;
          lastToken = new OperatorToken(symbol, operators.get(symbol));
        }
        else
          throw new UnparsableExpressionException(c, i);
      }
      else if (c == '(' || c == ')' || c == '[' || c == ']' || c == '{' || c == '}')
        lastToken = new ParenthesisToken(String.valueOf(c));
      else
        throw new UnparsableExpressionException(c, i);
      tokens.add(lastToken);
    }
    return validateTokens(tokens);
  }
  
  private List<Token> validateTokens(final List<Token> tokens) throws UnparsableExpressionException
  {
    final List<Token> validatedTokens = new ArrayList<Token>();
    for (Iterator<Token> i = tokens.iterator(); i.hasNext();)
    {
      Token token = i.next();
      if (token instanceof FunctionToken)
      {
        if (i.hasNext())
        {
          Token nextToken = i.next();
          if (nextToken instanceof ParenthesisToken && ((ParenthesisToken)nextToken).isOpen())
          {
            validatedTokens.add(token);
            validatedTokens.add(nextToken);
            continue;
          }
          else
          {
            validatedTokens.add(createVariableToken(token.getValue()));
            validatedTokens.add(nextToken);
            continue;
          }
        }
        else
        {
          validatedTokens.add(createVariableToken(token.getValue()));
          continue;
        }
      }
      validatedTokens.add(token);
    }
    return validatedTokens;
  }
  
  private VariableToken createVariableToken(String name) throws UnparsableExpressionException
  {
    FieldExpression fe = new FieldExpression(name);
    if (fe.isValid())
      if (geoEventDefinition == null || geoEventDefinition.isFieldExist(fe))
        return new VariableToken(fe);
      else
        throw new UnparsableExpressionException("Field '" + name + "' doesn't exist.");
    else
      throw new UnparsableExpressionException(fe.getErrorMessage());
  }
  
  private Object parseValue(String s)
  {
    s = Validator.compactSpaces(s);
    if (s.startsWith("'") && s.endsWith("'"))
      return s;
    try
    {
      return Short.parseShort(s);
    }
    catch (NumberFormatException e)
    {
      ;
    }
    try
    {
      return Integer.parseInt(s);
    }
    catch (NumberFormatException e)
    {
      ;
    }
    try
    {
      return Long.parseLong(s);
    }
    catch (NumberFormatException e)
    {
      ;
    }
    try
    {
      return Double.parseDouble(s);
    }
    catch (NumberFormatException e)
    {
      ;
    }
    return s;
  }
  
  private boolean isDigitOrDecimalSeparator(char c)
  {
    return Character.isDigit(c) || c == '.';
  }
  
  private boolean isFunction(String name)
  {
    return functions.containsKey(name);
  }
  
  private boolean isVariableOrFunctionCharacter(char prev, char cur)
  {
    return Character.isLetter(cur) || Character.isDigit(cur) || cur == '_' || cur == '[' || cur == ']' || (cur == '?' && prev == '[') || (cur == '*' && prev == '[') || cur == '.';
  }
  
  private boolean isOperatorCharacter(char c)
  {
    for (String symbol : operators.keySet())
      if (symbol.indexOf(c) != -1)
        return true;
    return false;
  }
  
  private boolean isOperatorStart(String op)
  {
    for (String operatorName : operators.keySet())
      if (operatorName.startsWith(op))
        return true;
    return false;
  }
}