package multigraph;

import java.util.*;

/**
 * This class represents a multigraph.  Consider a collection of labeled nodes that are connected with
 * labeled edges between them.  These edges are unidirectional, where the origin node is considered
 * to be the parent, and the termination node is the child.  Each node can have 0, 1, or more edges
 * between them.  Edges may originate and terminate at the same node, however edges may not be
 * identical (they must have different labels)
 */
public class LinearMultigraph<N, E> implements Multigraph<N, E> {

    /**
     * Creates a new, empty multigraph.
     */
    public LinearMultigraph(){
        nodes = new HashSet<>();
        edges = new HashSet<>();
        checkRep();
    }

    /**
     * Creates a new multigraph that is a copy of graph
     * @spec.requires graph != null
     * @param graph the graph to clone
     * @throws NullPointerException if graph == null
     */
    public LinearMultigraph(LinearMultigraph<N, E> graph){
        if(graph == null) throw new NullPointerException();
        this.nodes = new HashSet<N>(graph.nodes);
        this.edges = new HashSet<Edge<N, E>>(graph.edges);
        checkRep();
    }

    /**
     * Creates a new multigraph that is a copy of graph
     * @spec.requires graph != null
     * @param graph the graph to clone
     * @throws NullPointerException if graph == null
     */
    public LinearMultigraph(Multigraph<N, E> graph){
        this();
        if(graph == null) throw new NullPointerException();
        for(N node : graph.listNodes())
            this.addNode(node);
        for(Edge<N, E> node : graph.listEdges())
            this.addEdge(node);
        checkRep();
    }

    //RI: this.nodes != null, this.edges != null, if nodes.isEmpty() then edges.isEmpty() must be true
    //  for(Edge e : edges)
    //      this.nodes.contains(e.childNode)
    //      this.nodes.contains(e.parentNode)
    //      e.label != null
    //AF: A graph with nodes this.nodes.  Edge represents the connections between nodes.  Each edge
    //      connects from its parent node to its child node.

    Set<N> nodes;
    Set<Edge<N, E>> edges;
    boolean debug = false;

    /**
     * Returns the number of nodes in the graph
     * @return the number of nodes in the graph
     */
    public int size(){
        checkRep();
        return nodes.size();
    }

    /**
     * Returns a sorted list containing the unique label of all the nodes in the graph
     * Note that the returned list will have at most one of each element (no duplicates)
     * @return the sorted list of nodes in the graph
     */
    public List<N> listNodes(){
        checkRep();
        return new ArrayList<>(this.nodes);
    }

    /**
     * Returns a list of edges that terminate at childNode
     * @param childNode the node that all returned edges should terminate at
     * @spec.requires this.containsNode(childNode)
     * @return the list of edges that terminate at childNode
     * @throws NoSuchElementException if this does not contain childNode
     */
    public List<Edge<N, E>> listParentEdges(N childNode){
        checkRep();
        if(!nodes.contains(childNode)) throw new NoSuchElementException();
        List<Edge<N, E>> ret = new ArrayList<>();
        for(Edge<N, E> e : edges){
            if(e.getChildNode().equals(childNode)){
                ret.add(e);
            }
        }
        checkRep();
        return ret;
    }

    /**
     * Returns the list of edges that originate at parentNode
     * @param parentNode the node that all returned edges should originate at
     * @spec.requires this.containsNose(parentNode)
     * @return the list of edges that originate at parentNode
     * @throws NoSuchElementException if this does not contain parentNode
     */
    public List<Edge<N, E>> listChildEdges(N parentNode){
        checkRep();
        if(!nodes.contains(parentNode)) throw new NoSuchElementException();
        List<Edge<N, E>> ret = new ArrayList<>();
        for(Edge<N, E> e : edges){
            if(e.getParentNode().equals(parentNode)){
                ret.add(e);
            }
        }
        checkRep();
        return ret;
    }

    /**
     * Returns a list of all the edges in the graph
     * @return a list of all the edges in the graph
     */
    public List<Edge<N, E>> listEdges(){
        checkRep();
        return new ArrayList<>(edges);
    }

    /**
     * Checks if the graph contains the node label
     * @param label the label of the node to check for
     * @spec.requires label != null
     * @return true if the graph contains the node label
     */
    public boolean containsNode(N label){
        checkRep();
        return nodes.contains(label);
    }

    /**
     * Checks if the graph contains the given edge
     * The graph contains edge if this.ListEdges().contains(edge)
     * @param edge the edge to check for
     * @spec.requires edge != null
     * @return true if the graph contains edge
     */
    public boolean containsEdge(Edge<N, E> edge){
        checkRep();
        return edges.contains(edge);
    }

    /**
     * Checks if the graph contains any edge with the given label.  The graph may contain any positive number
     * of edges label.
     * @param label the edge to check for
     * @spec.requires label != null
     * @return true if the graph contains at least one edge such that edge.label.equals(label)
     */
    public boolean containsEdge(E label){
        checkRep();
        for(Edge<N, E> e : edges){
            if(e.getLabel().equals(label)) return true;
        }
        return false;
    }

    /**
     * Adds the given node label to the set of nodes
     * If the node already exists in the set of nodes, no action will be taken
     * @param label the node to add to the set
     * @spec.requires label != null
     * @spec.modifies this
     * @spec.effects adds 1 or 0 instances of the node label to (this)
     */
    public void addNode(N label){
        checkRep();
        if(label == null) throw new IllegalArgumentException();
        nodes.add(label);
        checkRep();
    }

    /**
     * Adds the given edge to the graph
     * @param edge the edge to add to the graph
     * @spec.requires edge != null and edge.label != null and
     *                this.containsNode(edge.parentNode) and this.containsNode(edge.childNode)
     * @spec.modifies this
     * @spec.effects Connects edge.parentNode to edge.childNode with a new edge label
     * @throws NoSuchElementException if this does not contain edge.parentNode or edge.childNode
     */
    public void addEdge(Edge<N, E> edge){
        if(edge == null) throw new NullPointerException();
        if(edge.getLabel() == null) throw new IllegalArgumentException();
        if(!nodes.contains(edge.getChildNode()) || !nodes.contains(edge.getParentNode()))
            throw new NoSuchElementException();
        edges.add(edge);
    }

    /**
     * Removes the given node label from the graph. If the graph does not contain the node, no action is taken.
     * Removes all edges that are connected to label
     * @param label the node to remove from the graph
     * @spec.requires label != null
     * @spec.modifies this
     * @spec.effects Removes 1 or 0 instances of the node label from (this).
     *               Deletes any edges that are connected to the node label
     */
    public void deleteNode(N label){
        checkRep();
        if(label == null) throw new IllegalArgumentException();
        if(nodes.remove(label)){
            Iterator<Edge<N, E>> edgeIterator = edges.iterator();
            while(edgeIterator.hasNext()){
                Edge<N, E> e = edgeIterator.next();
                if(e.getParentNode().equals(label) || e.getChildNode().equals(label)){
                    edgeIterator.remove();
                }
            }
        }
        checkRep();
    }

    /**
     * Removes all edges that have the given label
     * An adjacent call to containsEdge(label) is guaranteed to return false
     * @param label the label to remove all edges that have edge.label.equals(label)
     * @spec.requires label != null
     * @spec.modifies this
     * @spec.effects Removes all edges that are labelled with label from the graph
     */
    public void deleteEdge(E label){
        checkRep();
        if(label == null) throw new IllegalArgumentException();
        Iterator<Edge<N, E>> edgeIterator = edges.iterator();
        while(edgeIterator.hasNext()){
            Edge<N, E> e = edgeIterator.next();
            if(e.getLabel().equals(label)){
                edgeIterator.remove();
            }
        }
        checkRep();
    }

    /**
     * Removes edge that matches the given edge. If the edge is not in the graph, will do nothing, provided
     * both edge.parentNode and edge.childNode are valid
     * An adjacent call to containsEdge(edge) is guaranteed to return false
     * @param edge the edge to remove all instances of
     * @spec.requires edge != null and this.containsNode(edge.parentNode) and this.containsNode(edge.childNode)
     * @spec.modifies this
     * @spec.effects Removes edge from the graph
     * @throws NoSuchElementException if this does not contain edge.parentNode or edge.childNode
     */
    public void deleteEdge(Edge<N, E> edge){
        checkRep();
        if(edge == null) throw new NullPointerException();
        if(edge.getLabel() == null) throw new IllegalArgumentException();
        if(!nodes.contains(edge.getChildNode()) || !nodes.contains(edge.getParentNode()))
            throw new NoSuchElementException();
        edges.remove(edge);
        checkRep();
    }

    /**
     * Checks if the representation invariant is currently met
     */
    private void checkRep(){
        assert edges != null;
        assert nodes != null;
        if(nodes.isEmpty()){
            assert edges.isEmpty();
        }
        if(debug) for(Edge<N, E> e : edges){
            assert e.getLabel() != null;
            assert nodes.contains(e.getParentNode());
            assert nodes.contains(e.getChildNode());
        }
    }
}
