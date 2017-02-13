package com.leantaas.operation.string_ops;

import java.util.function.BiFunction;

/**
 * Created by boweiliu on 12/11/16.
 */
public class ChooseNotNull implements BiFunction<String, String, String> {

    private static ChooseNotNull singleton;


    @Override
    public String apply(String s1, String s2) {
        if (s1 == null && s2 == null) {
            throw new IllegalArgumentException("s1 and s2 cannot be both null");
        }
        if (s1 != null && s2 != null) {
            throw new IllegalArgumentException("one of s1 and s2 must be null");
        }
        return s1 != null ? s1 : s2;
    }

    public static ChooseNotNull simpleFactoryCreate() {
        if (singleton == null) {
            singleton = new ChooseNotNull();
        }
        return singleton;
    }
}
