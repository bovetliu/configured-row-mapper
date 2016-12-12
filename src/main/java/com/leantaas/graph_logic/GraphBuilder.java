package com.leantaas.graph_logic;

import com.google.common.annotations.VisibleForTesting;
import com.leantaas.graph_representation.GraphEdge;
import com.leantaas.graph_representation.GraphNode;
import com.leantaas.operation.manager.OperationGuavaClassPathResolver;
import com.leantaas.operation.manager.OperationResolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The logic of this class is simple, just take one iterable of GraphEdge, then output a List of GraphNodes
 * Created by boweiliu on 12/11/16.
 */
public class GraphBuilder {
  public static String STRING_OPS_PACKAGE_NAME = "com.leantaas.operation.string_ops";
  public static String NUMBER_OPS_PACKAGE_NAME = "com.leantaas.operation.number_ops";

  private final OperationResolver resolver;

  public GraphBuilder () {
    // TODO following line has a string coupling with OperationGuavaClassPathResolver
    // TODO I need to use Guice Injector to resolve it
    resolver = new OperationGuavaClassPathResolver(STRING_OPS_PACKAGE_NAME, NUMBER_OPS_PACKAGE_NAME);
  }

  /**
   *
   * @param edges Iterable graphEdge
   * @return a collection of graphNodes, in the collection, every graphNode has fromNode1 , and possibly fromNode2
   *         assigned value.
   * @throws IllegalStateException when one graphNode have more than 2 fromNodes.
   *
   */
  public Collection<GraphNode> buildFromEdges(Iterator<GraphEdge> edges) {
    List<GraphEdge> edgeList = new ArrayList<>();
    while (edges.hasNext()) {
      edgeList.add(edges.next());
    }
    HashMap<String, GraphNode> nodeIdVsNodeMap = new HashMap<>();
    for (GraphEdge graphEdge : edgeList) {
      GraphNode tempNode = nodeIdVsNodeMap.get(graphEdge.getFromNodeId());
      if (tempNode == null) {
        nodeIdVsNodeMap.put(graphEdge.getFromNodeId(), new GraphNode(graphEdge.getFromNodeId()));
      }
      tempNode = nodeIdVsNodeMap.get(graphEdge.getToNodeId());
      if (tempNode == null) {
        nodeIdVsNodeMap.put(graphEdge.getToNodeId(), new GraphNode(graphEdge.getToNodeId()));
      }
    }

    for (GraphEdge graphEdge : edgeList) {
      GraphNode toNode = nodeIdVsNodeMap.get(graphEdge.getToNodeId());
      GraphNode fromNode =  nodeIdVsNodeMap.get(graphEdge.getFromNodeId()) ;
      toNode.addFromNode(fromNode);
      if (graphEdge.isDescriptiveEdge()) {
        toNode.biFunction = checkNotNull(resolver.resolveStringOperation(graphEdge.getOperationName().toLowerCase()),
            String.format("operation \"%s\" cannot be resolved as a string operation", graphEdge.getOperationName()));
      }
    }

    if (hasCycle(nodeIdVsNodeMap)) {
      throw new IllegalStateException("Cycle found");
    }

    return nodeIdVsNodeMap.values();
  }

  public boolean hasCycle(HashMap<String, GraphNode> nodeIdVsNodeMap) {
    HashMap<String, Integer> statusMap = new HashMap<>();
    for (Map.Entry<String, GraphNode> idNodeEntry : nodeIdVsNodeMap.entrySet()) {
      GraphNode node = idNodeEntry.getValue();
      String id = idNodeEntry.getKey();
      if (statusMap.get(id) == null) {
        if (hasCycle(node, statusMap)) {
          return true;
        }
      }
    }
    return false;
  }

  @VisibleForTesting
  boolean hasCycle(GraphNode root, HashMap<String, Integer> statusMap) {
    if (root == null) {
      return false;
    }
    Integer status = statusMap.get(root.graphNodeId);
    if (status != null) {
      return status == 1;
    }
    statusMap.put(root.graphNodeId, 1); // mark this node as being visited.

    if (hasCycle(root.getFromNode1(), statusMap)) {
      return true;
    }
    if (hasCycle(root.getFromNode2(), statusMap)) {
      return true;
    }
    statusMap.put(root.graphNodeId, 2); // mark this node has been visited.
    return false;
  }
}
