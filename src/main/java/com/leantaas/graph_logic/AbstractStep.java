package com.leantaas.graph_logic;

import com.leantaas.exception.FilteredRowException;
import com.leantaas.graph_representation.GraphNode;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 */
public abstract class AbstractStep {

    protected final Collection<GraphNode> graphNodes;

    protected final Map<String, GraphNode> nodeNameVsAllNodes;

    protected final Map<String, GraphNode> inputColNameVsInputNode;

    protected final Map<String, GraphNode> outputColNameVsOutputNode;

    public AbstractStep(Collection<GraphNode> graphNodesParam) {
        graphNodes = graphNodesParam;
        graphNodesParam.forEach(node -> {
            if (node == null) {
                throw new NullPointerException("graphNodesParam collection contains one null node");
            }
        });
        nodeNameVsAllNodes = graphNodesParam.stream().collect(Collectors.toMap(
                graphNode -> graphNode.graphNodeId,
                graphNode -> graphNode
        ));

        Set<String> outputColumnNames = graphNodesParam.stream().map(graphNode -> graphNode.graphNodeId)
                .collect(Collectors.toSet());

        for (GraphNode graphNode : graphNodesParam) {
            if (graphNode.getFromNode1() != null) {
                outputColumnNames.remove(graphNode.getFromNode1().graphNodeId);
            }
            if (graphNode.getFromNode2() != null) {
                outputColumnNames.remove(graphNode.getFromNode2().graphNodeId);
            }
        }
        inputColNameVsInputNode = new HashMap<>();
        outputColNameVsOutputNode = new HashMap<>();

        for (GraphNode graphNode : graphNodesParam) {
            if (graphNode.getFromNode1() == null && graphNode.getFromNode2() == null) {
                inputColNameVsInputNode.put(graphNode.graphNodeId, graphNode);
            }
            if (outputColumnNames.contains(graphNode.graphNodeId)) {
                outputColNameVsOutputNode.put(graphNode.graphNodeId, graphNode);
            }
        }
    }


    public Collection<GraphNode> getGraphNodes() {
        return graphNodes;
    }

    public Map<String, GraphNode> getNodeNameVsAllNodes() {
        return nodeNameVsAllNodes;
    }

    public Map<String, GraphNode> getInputColNameVsInputNode() {
        return inputColNameVsInputNode;
    }

    public Map<String, GraphNode> getOutputColNameVsOutputNode() {
        return outputColNameVsOutputNode;
    }

    /**
     * Give one incoming row of data represented by one map. The mapped input column names must be a subset of incoming
     * row keyset.
     *
     * <p> For example, if incoming row contains following column names: {"COLUMN1", "COLUMN2", "COLUMN3"}, the keySet
     * of inputColNameVsInputNode must be either {"COLUMN1", "COLUMN2", "COLUMN3"} or {"COLUMN1", "COLUMN2"} or {
     * "COLUMN2", "COLUMN3"} or {"COLUMN1"}.
     *
     * <p> If the keySet of inputColNameVsInputNode is like {"COLUMN1", "COLUMN3", "COLUMN4"}, it will be one invalid
     * one.
     *
     * @param incomingRow a row of data represented by a map, key is column name, value is column value
     * @return outputRow, a row of data represented by a map, key is column name, value is output value
     * @throws FilteredRowException when this row needs to be filtered out.
     */
    public abstract Optional<Map<String, String>> map(Map<String, String> incomingRow);
}
