package com.leantaas.graph_representation;

import com.sun.istack.internal.Nullable;
import java.util.function.BiFunction;

/**
 * Graph node is not stored in DB
 * By default they all handles integer
 * Created by boweiliu on 12/11/16.
 */
public class GraphNode {

    public final String graphNodeId;

    @Nullable protected GraphNode fromNode1;

    @Nullable protected GraphNode fromNode2;

    @Nullable protected String output;

    public BiFunction<String, String, String> biFunction;

    public GraphNode(String graphNodeIdParam) {
        if (graphNodeIdParam == null || graphNodeIdParam.isEmpty()) {
            throw new IllegalArgumentException("graphNodeIdParam cannot be null or empty");
        }
        graphNodeId = graphNodeIdParam;
    }


    public void addFromNode(GraphNode oneFromNode) {
        if (fromNode1 == null) {
            fromNode1 = oneFromNode;
            return;
        }
        if (fromNode2 == null) {
            fromNode2 = oneFromNode;
            return;
        }
        throw new IllegalStateException(
                String.format("graph node %s has more than two incoming nodes, which is illegal",
                        graphNodeId));
    }

    public BiFunction<String, String, String> getBiFunction() {
        return biFunction;
    }

    public void setBiFunction(BiFunction<String, String, String> biFunction) {
        if (this.biFunction != null) {
            throw new IllegalStateException("one node cannot have two descriptive edge");
        }
        this.biFunction = biFunction;
    }

    /**
     * Compute output of this node using DFS.
     * @return nullable computed string of output of this node
     */
    public @Nullable String computeOutput() {
        if (fromNode1 == null && fromNode2 == null && output == null) {
            throw new IllegalStateException(String.format("input-node: %s has no initial value when doing row mapping",
                    graphNodeId));
        }
        if (output == null) {
            String outputFromNode1 = fromNode1 != null ? fromNode1.computeOutput() : null;
            String outputFromNode2 = fromNode2 != null ? fromNode2.computeOutput() : null;
            output = biFunction.apply(outputFromNode1, outputFromNode2);
        }
        return output;
    }

    // directed-first-search clear output
    public void clearOutput() {
        if (output != null) {
            output = null;
            if (fromNode1 != null && fromNode1.output != null) {
                fromNode1.clearOutput();
            }
            if (fromNode2 != null && fromNode2.output != null) {
                fromNode2.clearOutput();
            }
        }
    }

    public void setOutput(@Nullable String nodeOutputParam) {
        output = nodeOutputParam;
    }

    public String prettyPrintNode() {
        if (fromNode1 == null && fromNode2 == null) {
            return String.format("id %s, input-node", graphNodeId);
        }
        return String.format("id: %s, fromNodeId1: %s, fromNodeId2: %s",
                graphNodeId,
                fromNode1 != null ? fromNode1.graphNodeId : "null",
                fromNode2 != null ? fromNode2.graphNodeId : "null");
    }

    public @Nullable GraphNode getFromNode1() {
        return fromNode1;
    }

    public void setFromNode1(GraphNode fromNode1) {
        this.fromNode1 = fromNode1;
    }

    public @Nullable GraphNode getFromNode2() {
        return fromNode2;
    }

    public void setFromNode2(GraphNode fromNode2) {
        this.fromNode2 = fromNode2;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GraphNode)) {
            return false;
        }

        GraphNode graphNode = (GraphNode) o;

        return graphNodeId.equals(graphNode.graphNodeId);
    }

    @Override
    public int hashCode() {
        return graphNodeId.hashCode();
    }
}
