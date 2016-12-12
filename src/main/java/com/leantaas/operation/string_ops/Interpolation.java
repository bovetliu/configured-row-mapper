package com.leantaas.operation.string_ops;

import java.util.function.BiFunction;

/**
 * input1: "abc"
 * input2: "ghj"
 *
 * return "agbhcj"
 * Created by boweiliu on 12/11/16.
 */
public class Interpolation implements BiFunction<String, String, String> {
  private static Interpolation singleton;

  @Override
  public String apply(String s1, String s2) {
    if (s1 == null || s2 == null) {
      throw new NullPointerException("s1 or s2 is null");
    }
    char[] temp = new char[s1.length() + s2.length()];
    for (int i = 0; i < s1.length(); i ++) {
      temp[2 * i] = s1.charAt(i);
    }
    for (int i = 0; i < s2.length(); i++) {
      temp[2 * i + 1] = s2.charAt(i);
    }
    return new String(temp);
  }

  public static Interpolation simpleFactoryCreate() {
    if (singleton == null) {
      singleton = new Interpolation();
    }
    return singleton;
  }
}
