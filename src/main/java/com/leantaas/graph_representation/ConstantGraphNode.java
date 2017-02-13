package com.leantaas.graph_representation;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by boweiliu on 12/11/16.
 */
public class ConstantGraphNode extends GraphNode {

    private static final Map<String, ConstantGraphNode> internalConstantNodes = new HashMap<>();
    private static AtomicInteger constantNodeCounter = new AtomicInteger();


    public ConstantGraphNode(String constantValue) {
        super("constant_" + String.valueOf(constantNodeCounter.incrementAndGet()));
        output = constantValue;
    }

    @Override
    public void setOutput(String outputParam) {
        throw new UnsupportedOperationException("set output of constant graph node is now supported");
    }

    @Override
    public void clearOutput() {

    }

    @Override
    public void addFromNode(GraphNode oneFromNode) {
        throw new UnsupportedOperationException("add from node to constant node is not supportec");
    }
}
