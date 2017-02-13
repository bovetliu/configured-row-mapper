package com.leantaas.graph_logic;


import com.leantaas.exception.FilteredRowException;
import com.leantaas.graph_representation.GraphNode;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 */
public class MappingStep extends AbstractStep {

    public MappingStep(Collection<GraphNode> graphNodesParam) {
        super(graphNodesParam);
    }

    public Optional<Map<String, String>> map(Map<String, String> incomingRow) {
        Objects.requireNonNull(incomingRow, "incomingRow cannot be empty");
        for (GraphNode outputNode : outputColNameVsOutputNode.values()) {
            outputNode.clearOutput();
        }

        // verify input and initialize inputNodes
        for (Map.Entry<String, GraphNode> inputGraphNodeEntry : inputColNameVsInputNode.entrySet()) {
            String inputColName = inputGraphNodeEntry.getKey();
            if (!incomingRow.containsKey(inputColName)) {
                throw new IllegalStateException(String.format("column : %s is not found in incomingRow: %s",
                        inputColName, incomingRow.toString()));
            }
            GraphNode inputGraphNode = inputGraphNodeEntry.getValue();
            // inputColValue can be null, since a map value can be null
            String inputColValue = incomingRow.get(inputColName);
            inputGraphNode.setOutput(inputColValue);
        }

        try {
            // depth first iteration : computeOutput()
            HashMap<String, String> res = new HashMap<>();
            for (Map.Entry<String, GraphNode> outputNodeEntry : outputColNameVsOutputNode.entrySet()) {
                res.put(outputNodeEntry.getKey(), outputNodeEntry.getValue().computeOutput());
            }
            return Optional.of(res);
        } catch (FilteredRowException filtered) {
            return Optional.<Map<String, String>>empty();
        }
    }
}
