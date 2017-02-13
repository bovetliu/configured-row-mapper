package com.leantaas;

import com.leantaas.graph_logic.FilteringStep;
import com.leantaas.graph_logic.MappingStep;
import com.leantaas.graph_logic.GraphBuilder;
import com.leantaas.storage_medium_adapter.SimpleTextFileAdapter;
import com.leantaas.graph_representation.GraphEdge;
import com.leantaas.graph_representation.GraphNode;
import java.util.Optional;
import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * entry point of this experimental project
 */
public class App {

    private static final Logger LOGGER = Logger.getLogger(App.class);

    private static final String COMMA_SEPARATOR = ",";


    public static void main(String[] args) {
        GraphBuilder graphBuilder = new GraphBuilder();
        SimpleTextFileAdapter simpleTextFileAdapter = new SimpleTextFileAdapter((line) -> {
            String[] tokens = line.split(COMMA_SEPARATOR);
            for (int i = 0; i < tokens.length; i++) {
                tokens[i] = tokens[i].trim();
            }
            return tokens;
        });
        Iterator<GraphEdge> graphEdgeIterator = simpleTextFileAdapter.apply("simple_textual_mappper.txt");
        Collection<GraphNode> nodes = graphBuilder.buildFromEdges(graphEdgeIterator);

        for (GraphNode node : nodes) {
            LOGGER.info(node.prettyPrintNode());
        }

        MappingStep mappingStep = new MappingStep(nodes);

        // following code should be in some database fetching row callback method
        LOGGER.info("simulating in some database row fetching callback method");
        Map<String, String> inputRow1 = new HashMap<>();
        inputRow1.put("col_name1", "hello");
        inputRow1.put("col_name2", "world");
        inputRow1.put("col_name3", "jack");
        inputRow1.put("col_name4", "alibaba");

        LOGGER.info("started mapping...\n");

        Optional<Map<String, String>> output = mappingStep.map(inputRow1);

        if (output.isPresent()) {
            LOGGER.info("input is not filtered");
            LOGGER.info("result: " + output.get());
        } else {
            LOGGER.info("input is filtered");
        }

        FilteringStep filterStep = new FilteringStep("col_name4", (val) -> val.equals("baidu"));
        output = filterStep.map(inputRow1);

        if (output.isPresent()) {
            LOGGER.info("input is not filtered");
            LOGGER.info("result: " + output.get());
        } else {
            LOGGER.info("input is filtered");
        }

    }
}
