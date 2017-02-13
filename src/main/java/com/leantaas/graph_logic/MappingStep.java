package com.leantaas.graph_logic;


import com.google.common.base.Preconditions;
import com.leantaas.exception.FilteredRowException;
import com.leantaas.graph_representation.GraphNode;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 */
public class MappingStep extends AbstractStep{

  public MappingStep(Collection<GraphNode> graphNodesParam) {
    super(graphNodesParam);
  }

  public Optional<Map<String, String>> map (Map<String, String> incomingRow) {
    Objects.requireNonNull(incomingRow, "incomingRow cannot be empty");
    for (GraphNode outputNode : outputColNameVsOutputNode.values()) {
      outputNode.clearOutput();
    }

    // verify input
    for (Map.Entry<String, GraphNode> inputGraphNodeEntry : inputColNameVsInputNode.entrySet()) {
      String inputColName = inputGraphNodeEntry.getKey();
      GraphNode inputGraphNode = inputGraphNodeEntry.getValue();
      String inputColValue = Preconditions.checkNotNull(incomingRow.get(inputColName),
              String.format("key : %s is not given in incomingRow", inputColName));
      inputGraphNode.setOutput(inputColValue);
    }

    try {
      // depth first search
      HashMap<String, String > res = new HashMap<>();
      for (Map.Entry<String, GraphNode>  outputNodeEntry : outputColNameVsOutputNode.entrySet()) {
        res.put(outputNodeEntry.getKey(), outputNodeEntry.getValue().computeOutput());
      }
      return Optional.of(res);
    } catch (FilteredRowException filtered) {
      return Optional.<Map<String, String>>empty();
    }
  }
}
