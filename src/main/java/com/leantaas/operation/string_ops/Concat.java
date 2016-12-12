package com.leantaas.operation.string_ops;

import java.util.function.BiFunction;

/**
 * input1: "abc"
 * input2: "ghj"
 * return: "abcghj"
 * Created by boweiliu on 12/11/16.
 */
public class Concat implements BiFunction<String, String, String> {

  private static Concat singleton;

  @Override
  public String apply(String s, String s2) {
    if (s == null || s2 == null) {
      throw new NullPointerException("concatenation operation require both input present");
    }
    return s + s2;
  }

  public static Concat simpleFactoryCreate() {
    if (singleton == null) {
      singleton = new Concat();
    }
    return singleton;
  }
}
