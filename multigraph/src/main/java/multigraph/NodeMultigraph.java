package multigraph;

import java.util.*;

public class NodeMultigraph<N, E> implements Multigraph<N, E> {

    /**
     * Creates a new, empty multigraph.
     */
    public NodeMultigraph(){
        graphMap = new HashMap<>();
        checkRep();
    }

    /**
     * Creates a new multigraph that is a copy of graph
     * @spec.requires graph != null
     * @param graph the graph to clone
     * @throws NullPointerException if graph == null
     */
    public NodeMultigraph(NodeMultigraph<N, E> graph){
        this();
        if(graph == null) throw new NullPointerException();
        for(N node : graph.graphMap.keySet()){
            graphMap.put(node, new HashSet<>(graph.graphMap.get(node)));
        }
        checkRep();
    }

    /**
     * Creates a new multigraph that is a copy of graph
     * @spec.requires graph != null
     * @param graph the graph to clone
     * @throws NullPointerException if graph == null
     */
    public NodeMultigraph(Multigraph<N, E> graph){
        this();
        if(graph == null) throw new NullPointerException();
        for(N node : graph.listNodes())
            this.addNode(node);
        for(Edge<N, E> node : graph.listEdges())
            this.addEdge(node);
        checkRep();
    }

    //AF: (this) is a graph made up of nodes graphMap.keySet() and each node has child edges
    //      graphMap.get(Node)
    //RI: graphMap != null
    //      all nodes in graphMap.keySet() are not null
    //      all sets in graphMap.get(N) are not null
    //          all edges in these sets are not null
    //              childNode, parentNode, and label in these edges are not null
    //              graphMap.containsKey(childNode) must be true
    //              the node that this is mapped from in graphMap must be parentNode
    HashMap<N, Set<Edge<N,E>>> graphMap;
    boolean debug = false;

    /**
     * Returns the number of nodes in the graph
     * @return the number of nodes in the graph
     */
    @Override
    public int size() {
        return graphMap.size();
    }

    /**
     * Returns a sorted list containing the unique label of all the nodes in the graph
     * Note that the returned list will have at most one of each element (no duplicates)
     * @return the sorted list of nodes in the graph
     */
    @Override
    public List<N> listNodes() {
        return new ArrayList<>(graphMap.keySet());
    }

    /**
     * Returns a list of edges that terminate at childNode
     * @param childNode the node that all returned edges should terminate at
     * @spec.requires this.containsNode(childNode)
     * @return the list of edges that terminate at childNode
     * @throws NoSuchElementException if this does not contain childNode
     */
    @Override
    public List<Edge<N, E>> listParentEdges(N childNode) {
        List<Edge<N, E>> edgesList = new ArrayList<>();
        if(!graphMap.containsKey(childNode)) throw new NoSuchElementException();
        //iterate nodes
        for(N node : graphMap.keySet()){
            //iterate edges
            for(Edge<N, E> edge : graphMap.get(node)){
                if(edge.getChildNode().equals(childNode)){
                    edgesList.add(edge);
                }
            }
        }
        return edgesList;
    }

    /**
     * Returns the list of edges that originate at parentNode
     * @param parentNode the node that all returned edges should originate at
     * @spec.requires this.containsNose(parentNode)
     * @return the list of edges that originate at parentNode
     * @throws NoSuchElementException if this does not contain parentNode
     */
    @Override
    public List<Edge<N, E>> listChildEdges(N parentNode) {
        if(!graphMap.containsKey(parentNode)) throw new NoSuchElementException();
        return new ArrayList<>(graphMap.get(parentNode));
    }

    /**
     * Returns a list of all the edges in the graph
     * @return a list of all the edges in the graph
     */
    @Override
    public List<Edge<N, E>> listEdges() {
        List<Edge<N, E>> ret = new LinkedList<>();
        //iterate nodes
        for(N node : graphMap.keySet()){
            //iterate edges
            for(Edge<N, E> edge : graphMap.get(node)){
                ret.add(edge);
            }
        }
        return ret;
    }

    /**
     * Checks if the graph contains the node label
     * @param label the label of the node to check for
     * @spec.requires label != null
     * @return true if the graph contains the node label
     */
    @Override
    public boolean containsNode(N label) {
        return graphMap.containsKey(label);
    }

    /**
     * Checks if the graph contains the given edge
     * The graph contains edge if this.ListEdges().contains(edge)
     * @param edge the edge to check for
     * @spec.requires edge != null
     * @return true if the graph contains edge
     */
    @Override
    public boolean containsEdge(Edge<N, E> edge) {
        //iterate nodes
        for(N node : graphMap.keySet()){
            //iterate edges
            for(Edge<N, E> e : graphMap.get(node)){
                if(e.equals(edge)) return true;
            }
        }
        return false;
    }

    /**
     * Checks if the graph contains any edge with the given label.  The graph may contain any positive number
     * of edges label.
     * @param label the edge to check for
     * @spec.requires label != null
     * @return true if the graph contains at least one edge such that edge.label.equals(label)
     */
    @Override
    public boolean containsEdge(E label) {
        //iterate nodes
        for(N node : graphMap.keySet()){
            //iterate edges
            for(Edge<N, E> edge : graphMap.get(node)){
                if(edge.getLabel().equals(label)) return true;
            }
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
    @Override
    public void addNode(N label) {
        if(graphMap.containsKey(label)) return;
        graphMap.put(label, new HashSet<>());
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
    @Override
    public void addEdge(Edge<N, E> edge) {
        if(!graphMap.containsKey(edge.getChildNode()) || !graphMap.containsKey(edge.getParentNode()))
            throw new NoSuchElementException();
        graphMap.get(edge.getParentNode()).add(edge);
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
    @Override
    public void deleteNode(N label) {
        if(graphMap.remove(label) == null) return;
        //iterate nodes
        for(N node : graphMap.keySet()){
            //iterate edges
            Iterator<Edge<N, E>> iter = graphMap.get(node).iterator();
            while(iter.hasNext()){
                //remove edge if child is removed node
                if(iter.next().getChildNode().equals(label)) iter.remove();
            }
        }
    }

    /**
     * Removes all edges that have the given label
     * An adjacent call to containsEdge(label) is guaranteed to return false
     * @param label the label to remove all edges that have edge.label.equals(label)
     * @spec.requires label != null
     * @spec.modifies this
     * @spec.effects Removes all edges that are labelled with label from the graph
     */
    @Override
    public void deleteEdge(E label) {
        //iterate nodes
        for(N node : graphMap.keySet()){
            //iterate edges
            Iterator<Edge<N, E>> iter = graphMap.get(node).iterator();
            while(iter.hasNext()){
                //remove edge if child is removed node
                if(iter.next().getLabel().equals(label)) iter.remove();
            }
        }
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
    @Override
    public void deleteEdge(Edge<N, E> edge) {
        if(!graphMap.containsKey(edge.getChildNode()) || !graphMap.containsKey(edge.getParentNode()))
            throw new NoSuchElementException();
        //iterate nodes
        for(N node : graphMap.keySet()){
            //iterate edges
            Iterator<Edge<N, E>> iter = graphMap.get(node).iterator();
            while(iter.hasNext()){
                //remove edge if child is removed node
                if(iter.next().equals(edge)) iter.remove();
            }
        }
    }

    /**
     * Checks if the representation invariant is currently met
     */
    private void checkRep(){
        assert graphMap != null;
        if(debug){
            for(N node : graphMap.keySet()){
                assert node != null;
                Set<Edge<N, E>> edgeSet = graphMap.get(node);
                assert edgeSet != null;
                for(Edge<N, E> edge : edgeSet){
                    assert edge != null;
                    assert edge.getLabel() != null;
                    assert edge.getParentNode() == node;
                    assert edge.getChildNode() != null;
                    assert graphMap.containsKey(edge.getChildNode());
                }
            }
        }
    }
}
