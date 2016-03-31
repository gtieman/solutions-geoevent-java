package com.esri.geoevent.processor.cacheawarefieldcalculator.expression;

public abstract class ArrayUtil
{
  public static Object[] reverse(Object[] data)
  {
    int left = 0;
    int right = data.length - 1;
    while (left < right)
    {
      // swap the values at the left and right indices
      Object temp = data[left];
      data[left] = data[right];
      data[right] = temp;
      // move the left and right index pointers in toward the center
      left++;
      right--;
    }
    return data;
  }
}