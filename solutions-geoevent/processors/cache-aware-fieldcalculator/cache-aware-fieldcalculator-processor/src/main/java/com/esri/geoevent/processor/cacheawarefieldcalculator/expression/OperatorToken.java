package com.esri.geoevent.processor.cacheawarefieldcalculator.expression;

import java.util.List;
import java.util.Stack;

import com.esri.ges.core.geoevent.GeoEvent;

/**
 * {@link Token} for Operations like +,-,*,/,% and ^
 * 
 */
public class OperatorToken extends CalculationToken
{
  private Operator operator;

  /**
   * construct a new {@link OperatorToken}
   * 
   * @param value
   *          the symbol (e.g.: '+')
   * @param operator
   *          the {@link Operator} of this {@link Token}
   */
  OperatorToken(String value, Operator operator)
  {
    super(value);
    this.operator = operator;
  }

  /**
   * apply the {@link Operator}
   * 
   * @param values
   *          the Objects to operate on
   * @return the result of the {@link Operator}
   */
  Object applyOperation(Object... values)
  {
    return operator.applyOperation(values);
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof OperatorToken)
    {
      final OperatorToken t = (OperatorToken) obj;
      return t.getValue().equals(this.getValue());
    }
    return false;
  }

  @Override
  public int hashCode()
  {
    return getValue().hashCode();
  }

  @Override
  void mutateStackForCalculation(Stack<Object> stack, GeoEvent geoEvent)
  {
    final Object[] operands = new Object[operator.operandCount];
    for (int i=0; i < operator.operandCount; i++)
      operands[operator.operandCount-i-1] = stack.pop();
    stack.push(operator.applyOperation(operands));
  }

  @Override
  void mutateStackForInfixTranslation(Stack<Token> operatorStack, List<Token> tokens)
  {
    Token before;
    while (!operatorStack.isEmpty() && (before = operatorStack.peek()) != null && (before instanceof OperatorToken || before instanceof FunctionToken))
    {
      if (before instanceof FunctionToken)
      {
        operatorStack.pop();
        tokens.add(before);
      }
      else
      {
        final OperatorToken stackOperator = (OperatorToken) before;
        if ((this.isLeftAssociative() && this.getPrecedence() <= stackOperator.getPrecedence()) || (!this.isLeftAssociative() && this.getPrecedence() < stackOperator.getPrecedence()))
          tokens.add(operatorStack.pop());
        else
          break;
      }
    }
    operatorStack.push(this);
  }

  private boolean isLeftAssociative()
  {
    return operator.leftAssociative;
  }

  private int getPrecedence()
  {
    return operator.precedence;
  }
}