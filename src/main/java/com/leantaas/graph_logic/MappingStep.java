package com.leantaas.graph_logic;

import com.google.common.base.Preconditions;
import com.leantaas.graph_representation.GraphNode;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by boweiliu on 12/11/16.
 */
public class MappingStep {

  protected final Collection<GraphNode> graphNodes;

  protected final Map<String, GraphNode> nodeNameVsAllNodes;
  protected final HashMap<String, GraphNode> inputColNameVsInputNode = new HashMap<>();
  protected final Map<String, GraphNode> outputColNameVsOutputNode = new HashMap<>();

  public MappingStep(Collection<GraphNode> graphNodesParam) {
    graphNodes = graphNodesParam;

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
    for (GraphNode graphNode : graphNodesParam) {
      if (isInputNode(graphNode)) {
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

  public HashMap<String, GraphNode> getInputColNameVsInputNode() {
    return inputColNameVsInputNode;
  }

  public Map<String, GraphNode> getOutputColNameVsOutputNode() {
    return outputColNameVsOutputNode;
  }

  /**
   *
   * @param incomingRow a row of data represented by a map, key is column name, value is column value
   * @return outputRow, a row of data represented by a map, key is column name, value is output value
   */
  public Map<String, String> map (Map<String, String> incomingRow) {
    for (GraphNode outputNode : outputColNameVsOutputNode.values()) {
      outputNode.clearOutput();
    }
    for (Map.Entry<String, String> inputFieldOfRow : incomingRow.entrySet()) {
      String colName = inputFieldOfRow.getKey();
      String colFieldValue = inputFieldOfRow.getValue();
      GraphNode inputNode = Preconditions.checkNotNull(inputColNameVsInputNode.get(colName),
          String.format("colName : %s cannot find corresponding input node", colName));
      inputNode.setOutput(colFieldValue);
    }
    // depth first search
    for (GraphNode outputNode : outputColNameVsOutputNode.values()) {
      outputNode.computeOutput();
    }
    return outputColNameVsOutputNode.values().stream().collect(Collectors.toMap(
        node -> node.graphNodeId,
        GraphNode::computeOutput
    ));
  }

  private static boolean isInputNode(GraphNode node) {
    if (node == null) {
      throw new NullPointerException("node param cannot be null");
    }
    return node.getFromNode1() == null && node.getFromNode2() == null;
  }

}
