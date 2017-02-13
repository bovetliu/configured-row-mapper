package com.leantaas.graph_representation;


/**
 * GraphEdge is what will be persisted in database.
 * Created by boweiliu on 12/11/16.
 */
public class GraphEdge {

    private String fromNodeId;

    private String toNodeId;

    private String operationName;

    // among all the edges pointing to one node, it must have one and only one edge, which is descriptive edge.
    private boolean descriptiveEdge;

    public String getFromNodeId() {
        return fromNodeId;
    }

    public void setFromNodeId(String fromNodeId) {
        this.fromNodeId = fromNodeId;
    }

    public String getToNodeId() {
        return toNodeId;
    }

    public void setToNodeId(String toNodeId) {
        this.toNodeId = toNodeId;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public boolean isDescriptiveEdge() {
        return descriptiveEdge;
    }

    public void setDescriptiveEdge(boolean descriptiveEdge) {
        this.descriptiveEdge = descriptiveEdge;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GraphEdge)) {
            return false;
        }

        GraphEdge graphEdge = (GraphEdge) o;

        if (!fromNodeId.equals(graphEdge.fromNodeId)) {
            return false;
        }
        return toNodeId.equals(graphEdge.toNodeId);
    }

    @Override
    public int hashCode() {
        if (fromNodeId == null) {
            throw new NullPointerException("fromNodeId cannot be null");
        }
        if (toNodeId == null) {
            throw new NullPointerException("toNodeId cannot be null");
        }
        int result = fromNodeId.hashCode();
        result = 31 * result + toNodeId.hashCode();
        return result;
    }
}
