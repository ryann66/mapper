/*
 * Copyright (C) 2023 Soham Pardeshi.  All rights reserved.
 */

package pathfinder.scriptTestRunner;

import multigraph.LinearMultigraph;
import multigraph.Edge;
import pathfinder.GraphUtils;

import java.io.*;
import java.util.*;

/**
 * This class implements a test driver that uses a script file format
 * to test an implementation of Dijkstra's algorithm on a graph.
 */
public class PathfinderTestDriver {

    //RI: graphs != null, all keys and values of graphs must also be non-null
    //  output != null, input != null
    // String -> Graph: maps the names of graphs to the actual graph
    private final Map<String, LinearMultigraph<String, Double>> graphs = new HashMap<>();
    private final PrintWriter output;
    private final BufferedReader input;

    /**
     * Creates a new PathfinderTestDriver
     * @spec.requires r != null, w != null
     * @param r input stream for test driver
     * @param w output stream for all output
     */
    public PathfinderTestDriver(Reader r, Writer w) {
        input = new BufferedReader(r);
        output = new PrintWriter(w);
    }

    /**
     * @throws IOException if the input or output sources encounter an IOException
     * @spec.effects Executes the commands read from the input and writes results to the output
     **/
    // Leave this method public
    public void runTests() throws IOException {
        String inputLine;
        while((inputLine = input.readLine()) != null) {
            if((inputLine.trim().length() == 0) ||
                    (inputLine.charAt(0) == '#')) {
                // echo blank and comment lines
                output.println(inputLine);
            } else {
                // separate the input line on white space
                StringTokenizer st = new StringTokenizer(inputLine);
                if(st.hasMoreTokens()) {
                    String command = st.nextToken();

                    List<String> arguments = new ArrayList<>();
                    while(st.hasMoreTokens()) {
                        arguments.add(st.nextToken());
                    }

                    executeCommand(command, arguments);
                }
            }
            output.flush();
        }
    }

    /**
     * executes the given command
     * @spec.modifies this.graphs, this.output
     * @spec.effects prints command results to this.output
     * @spec.requires command != null, arguments != null, all strings in arguments != null
     * @param command the command to be executed
     * @param arguments the arguments to the command
     */
    private void executeCommand(String command, List<String> arguments) {
        try {
            switch(command) {
                case "CreateGraph":
                    createGraph(arguments);
                    break;
                case "AddNode":
                    addNode(arguments);
                    break;
                case "AddEdge":
                    addEdge(arguments);
                    break;
                case "ListNodes":
                    listNodes(arguments);
                    break;
                case "ListChildren":
                    listChildren(arguments);
                    break;
                case "FindPath":
                    findPath(arguments);
                    break;
                default:
                    output.println("Unrecognized command: " + command);
                    break;
            }
        } catch(Exception e) {
            String formattedCommand = command;
            formattedCommand += arguments.stream().reduce("", (a, b) -> a + " " + b);
            output.println("Exception while running command: " + formattedCommand);
            e.printStackTrace(output);
        }
    }

    /**
     * creates a new graph to test with
     * @spec.modifies this.graphs, this.output
     * @spec.effects prints command results to this.output
     * @spec.requires arguments != null, all strings in arguments != null
     * @param arguments list of length 1 with the first element being the graph name
     */
    private void createGraph(List<String> arguments) {
        if(arguments.size() != 1) {
            throw new CommandException("Bad arguments to CreateGraph: " + arguments);
        }

        String graphName = arguments.get(0);
        LinearMultigraph<String, Double> graph = new LinearMultigraph<>();
        graphs.put(graphName, graph);
        output.println("created graph " + graphName);
    }

    /**
     * Adds a new node to the given graph
     * @spec.modifies this.graphs, this.output
     * @spec.effects prints command results to this.output
     * @spec.requires arguments != null, all strings in arguments != null
     * @param arguments list of length 2 with the first element being the graph name
     *                  and the second element being the node name
     */
    private void addNode(List<String> arguments) {
        if(arguments.size() != 2) {
            throw new CommandException("Bad arguments to AddNode: " + arguments);
        }

        String graphName = arguments.get(0);
        String nodeName = arguments.get(1);
        LinearMultigraph<String, Double> graph = graphs.get(graphName);
        graph.addNode(nodeName);
        output.println("added node " + nodeName + " to " + graphName);
    }

    /**
     * Adds a new edge to the given graph
     * @spec.modifies this.graphs, this.output
     * @spec.effects prints command results to this.output
     * @spec.requires arguments != null, all strings in arguments != null
     * @param arguments list of length 4 with the first element being the graph name,
     *                  the second element being the origin node,
     *                  the third element being the terminating node,
     *                  and the fourth element being the edge label, which must parse to a double
     */
    private void addEdge(List<String> arguments) {
        if(arguments.size() != 4) {
            throw new CommandException("Bad arguments to AddEdge: " + arguments);
        }
        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        String childName = arguments.get(2);
        Double edgeLabel = Double.parseDouble(arguments.get(3));
        LinearMultigraph<String, Double> graph = graphs.get(graphName);
        graph.addEdge(new Edge<>(parentName, childName, edgeLabel));
        output.println(String.format("added edge %.3f from " + parentName + " to " + childName
                + " in " + graphName, edgeLabel));
    }

    /**
     * lists all the nodes in the given graph
     * @spec.modifies this.output
     * @spec.effects prints command results to this.output
     * @spec.requires arguments != null, all strings in arguments != null
     * @param arguments list of length 1 with the first element being the graph name
     */
    private void listNodes(List<String> arguments) {
        if(arguments.size() != 1) {
            throw new CommandException("Bad arguments to ListNodes: " + arguments);
        }

        String graphName = arguments.get(0);
        LinearMultigraph<String, Double> graph = graphs.get(graphName);
        List<String> nodes = graph.listNodes();
        nodes.sort(Comparator.naturalOrder());
        output.print(graphName + " contains:");
        for(String s : nodes) output.print(" " + s);
        output.println();
    }

    /**
     * lists all the children of the parent node in the given graph
     * @spec.modifies this.output
     * @spec.effects prints command results to this.output
     * @spec.requires arguments != null, all strings in arguments != null
     * @param arguments list of length 2 with the first element being the graph name
     *                  and the second element being the parent node
     */
    private void listChildren(List<String> arguments) {
        if(arguments.size() != 2) {
            throw new CommandException("Bad arguments to ListChildren: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);


        LinearMultigraph<String, Double> graph = graphs.get(graphName);
        List<Edge<String, Double>> edges = graph.listChildEdges(parentName);

        //sort edges by node name then by label
        for(int i = 0; i < edges.size(); i++){
            for(int j = i + 1; j < edges.size(); j++){
                int r = edges.get(i).getChildNode().compareTo(edges.get(j).getChildNode());
                if(r == 0) r = edges.get(i).getLabel().compareTo(edges.get(j).getLabel());
                if(r > 0){
                    //swap
                    Edge<String, Double> tmp = edges.get(i);
                    edges.set(i, edges.get(j));
                    edges.set(j, tmp);
                }
            }
        }

        output.print("the children of " + parentName + " in " + graphName + " are:");
        for(Edge<String, Double> e : edges) output.print(" " + e.getChildNode() + "(" + String.format("%.3f", e.getLabel()) + ")");
        output.println();
    }

    /**
     * Finds the shortest path between two nodes
     * @spec.modifies this.output
     * @spec.effects prints command results to this.output
     * @spec.requires arguments != null, all strings in arguments != null
     * @param arguments list of length 3 with the first element being the graph name,
     *                  the second element being the origin node,
     *                  the third element being the terminating node,
     */
    private void findPath(List<String> arguments){
        if(arguments.size() != 3) {
            throw new CommandException("Bad arguments to ListChildren: " + arguments);
        }

        String graphName = arguments.get(0);
        String originName = arguments.get(1);
        String terminatingName = arguments.get(2);
        LinearMultigraph<String, Double> graph = graphs.get(graphName);

        if(!graph.containsNode(originName)){
            output.println("unknown: " + originName);
            if(!graph.containsNode(terminatingName)){
                output.println("unknown: " + terminatingName);
            }
            return;
        }
        if(!graph.containsNode(terminatingName)){
            output.println("unknown: " + terminatingName);
            return;
        }

        output.println("path from " + originName + " to " + terminatingName + ":");
        try{
            GraphUtils.Path<String> path = GraphUtils.shortestPath(graph, originName, terminatingName);
            for(GraphUtils.Path.Segment<String> seg : path){
                output.println(String.format(seg.getStart() + " to " + seg.getEnd() + " with weight %.3f", seg.getCost()));
            }
            output.println(String.format("total cost: %.3f", path.getCost()));
        }catch(IllegalArgumentException iae){
            output.println("no path found");
        }
    }


    /**
     * This exception results when the input file cannot be parsed properly
     **/
    static class CommandException extends RuntimeException {

        public CommandException() {
            super();
        }

        public CommandException(String s) {
            super(s);
        }

        public static final long serialVersionUID = 3495;
    }
}
