package com.leantaas.operation.manager;

import java.util.NoSuchElementException;
import java.util.TreeMap;
import java.util.function.BiFunction;

/**
 * Created by boweiliu on 12/11/16.
 */
public abstract class OperationResolver {
  protected final TreeMap<String, BiFunction<String, String, String>> stringOperationLookup;
  protected final TreeMap<String, BiFunction<Number, Number, Number>> numberOperationLookup;

  public OperationResolver() {
    stringOperationLookup = new TreeMap<>();
    numberOperationLookup = new TreeMap<>();
  }

  public TreeMap<String, BiFunction<String, String, String>> getStringOperationLookup() {
    return stringOperationLookup;
  }

  public TreeMap<String, BiFunction<Number, Number, Number>> getNumberOperationLookup() {
    return numberOperationLookup;
  }

  public BiFunction<String, String, String> resolveStringOperation(String op) {
    if (op == null || op.isEmpty()) {
      throw new IllegalArgumentException("argument cannot be null or empty");
    }
    BiFunction<String, String, String> res = stringOperationLookup.get(op);
    if (res == null) {
      throw new NoSuchElementException(String.format("\"%s\" cannot be resolved as an string operation", op));
    }
    return res;
  }

  public BiFunction<Number, Number, Number> resolveNumberOperation(String op) {
    if (op == null || op.isEmpty()) {
      throw new IllegalArgumentException("argument cannot be null or empty");
    }
    BiFunction<Number, Number, Number> res = numberOperationLookup.get(op);
    if (res == null) {
      throw new NoSuchElementException(String.format("\"%s\" cannot be resolved as an number operation", op));
    }
    return res;
  }
}
