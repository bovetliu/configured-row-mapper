# Configured-Row-Mapper
## Synopsis

This project is created to server as general-purpose row mapper. The reason is, in my work,
I am increasingly facing lots of custom pipeline building and maintenance work. After summarizing
what I did in those custom data pipelines, I found the part which is least reusable
is the "row-mapper".   

What is a "row-mapper"?  
The "row-mapper" is simply one mapper which transform one row of data into another row of 
data of different schema. For example:  

one rotated input row is given below:   
first_name: Bowei  
last_name: Liu  
age: 26  
gender: male  

one transformed output row could be like following:  
full_name: Bowei Liu  
summary: Bowei Liu is a male aged 26.

another transformed output row could be like following:
full_name_in_capital_letter: BOWEI LIU
age: twenty-six
gender: MALE

In my current work setup, I need to create two completely separate but highly similar 
data pipelines to handle these two kinds of output.

So I am thinking of creating one configurable \"row-mapper\".

## Code Example

java code is given below: 
```java
    GraphBuilder graphBuilder = new GraphBuilder();
    SimpleTextFileAdapter simpleTextFileAdapter = new SimpleTextFileAdapter(COMMA_SEPARATOR);  // COMMA_SEPARATOR is ","
    Iterator<GraphEdge> graphEdgeIterator = simpleTextFileAdapter.apply("simple_textual_mappper.txt");
    Collection<GraphNode> nodes = graphBuilder.buildFromEdges(graphEdgeIterator);


    DAGMapper dagMapper = new DAGMapper(nodes);

    // following code should be in some database fetching row callback method
    LOGGER.info("simulating in some database row fetching callback method");
    Map<String, String> inputRow1 = new HashMap<>();
    inputRow1.put("col_name1", "hello");
    inputRow1.put("col_name2", "world");
    inputRow1.put("col_name3", "jack");
    inputRow1.put("col_name4", "alibaba");

    LOGGER.info("started mapping...\n");

    Map<String, String> output = dagMapper.map(inputRow1);  // output is generated
```

map configuration file : simple_textual_mapper.txt 
```text
# I am going to create one map can do following operation
# col1: hello         node1(hello)    node5(hweolrllod)    node7(hweolrllodjackalibaba)
# col2: world         node2(world)
# col3: jack          node3(jack)     node6(jackalibaba)
# col4: alibaba       node4(alibaba)

# col1: hweolrllodjackalibaba

col_name1,5,true,interpolation
col_name2,5

col_name3,6,true,concat
col_name4,6

5,mixed_output,true,concat
6,mixed_output

col_name1, output1, true, chooseNotNull
col_name2, output2, true, chooseNotNull
col_name3, output3, true, chooseNotNull
col_name4, output4, true, chooseNotNull
```

## Setup

$ git clone &lt; this object&gt;  
$ cd configured-row-mapper

import project using Intellij



## Tests

in progress.

## Contributors

Bowei Liu

## Simple Structure
This project is mainly divided into three parts:
1. graph_logic
2. graph_representation
3. operation

For the graph_logic part:  

The first assumption of this project, I can use combination of biFunctionalTransformNodes and 
filterNodes to construct any row-mapper logic. And one action of "row-mapping" is actually
 a BFS traversing of a DAG (directed-acyclic-graph).

one biFunctionalTransformNodes take 0 - 2 inputs, and use java.util.BiFunction to evaluate
output of this node. one transform node involving three inputs can be converted into 
2 biFunctionalTransformNodes cascaded.

The logic of filterNode is also simple, during graph traversing, when the output
of a filterNode is evaluated, it will run `andThen` block to determine whether it should throw
FilterOutException to interrupt mapping of this row.

For the graph_representation part:  
the DAG (directed-acyclic-graph) of "row-mapper" is persisted using directed-edges.
Then one edge-represented graph is equivalently converted into a node-represented graph.
The graphNode is very like the binary tree tree node of some leetcode qestion, but with direction reversed.


For the operation part
The is one part designed to be completely extent-able. None of classes in 
`graph_logic` and `graph_representation` has direct dependency on any classes in 
 `operation`, except the abstract `OperationResolver` class. 
 
 To extent , modify logic and add new operations to project is very simple,
 just add one class to corresponding package, make the class implement `BiFunction`
 and that is it.
 
 
## TODO

In the progress of supporting more type, like all the classes implementing `java.lang.Number`.
Develop more operations.






## License

MIT license