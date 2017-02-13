package com.leantaas.storage_medium_adapter;

import com.leantaas.graph_representation.GraphEdge;

import java.util.Iterator;
import java.util.function.Function;

/**
 * Created by boweiliu on 12/11/16.
 */
public interface MediumAdapter<T> extends Function<T, Iterator<GraphEdge>> {

    @Override
    Iterator<GraphEdge> apply(T t);
}
