package multigraph;

import java.util.List;
import java.util.NoSuchElementException;

public interface Multigraph<N, E> {
    /**
     * Returns the number of nodes in the graph
     * @return the number of nodes in the graph
     */
    public int size();

    /**
     * Returns a sorted list containing the unique label of all the nodes in the graph
     * Note that the returned list will have at most one of each element (no duplicates)
     * @return the sorted list of nodes in the graph
     */
    public List<N> listNodes();

    /**
     * Returns a list of edges that terminate at childNode
     * @param childNode the node that all returned edges should terminate at
     * @spec.requires this.containsNode(childNode)
     * @return the list of edges that terminate at childNode
     * @throws NoSuchElementException if this does not contain childNode
     */
    public List<Edge<N, E>> listParentEdges(N childNode);

    /**
     * Returns the list of edges that originate at parentNode
     * @param parentNode the node that all returned edges should originate at
     * @spec.requires this.containsNose(parentNode)
     * @return the list of edges that originate at parentNode
     * @throws NoSuchElementException if this does not contain parentNode
     */
    public List<Edge<N, E>> listChildEdges(N parentNode);

    /**
     * Returns a list of all the edges in the graph
     * @return a list of all the edges in the graph
     */
    public List<Edge<N, E>> listEdges();

    /**
     * Checks if the graph contains the node label
     * @param label the label of the node to check for
     * @spec.requires label != null
     * @return true if the graph contains the node label
     */
    public boolean containsNode(N label);

    /**
     * Checks if the graph contains the given edge
     * The graph contains edge if this.ListEdges().contains(edge)
     * @param edge the edge to check for
     * @spec.requires edge != null
     * @return true if the graph contains edge
     */
    public boolean containsEdge(Edge<N, E> edge);

    /**
     * Checks if the graph contains any edge with the given label.  The graph may contain any positive number
     * of edges label.
     * @param label the edge to check for
     * @spec.requires label != null
     * @return true if the graph contains at least one edge such that edge.label.equals(label)
     */
    public boolean containsEdge(E label);

    /**
     * Adds the given node label to the set of nodes
     * If the node already exists in the set of nodes, no action will be taken
     * @param label the node to add to the set
     * @spec.requires label != null
     * @spec.modifies this
     * @spec.effects adds 1 or 0 instances of the node label to (this)
     */
    public void addNode(N label);

    /**
     * Adds the given edge to the graph
     * @param edge the edge to add to the graph
     * @spec.requires edge != null and edge.label != null and
     *                this.containsNode(edge.parentNode) and this.containsNode(edge.childNode)
     * @spec.modifies this
     * @spec.effects Connects edge.parentNode to edge.childNode with a new edge label
     * @throws NoSuchElementException if this does not contain edge.parentNode or edge.childNode
     */
    public void addEdge(Edge<N, E> edge);

    /**
     * Removes the given node label from the graph. If the graph does not contain the node, no action is taken.
     * Removes all edges that are connected to label
     * @param label the node to remove from the graph
     * @spec.requires label != null
     * @spec.modifies this
     * @spec.effects Removes 1 or 0 instances of the node label from (this).
     *               Deletes any edges that are connected to the node label
     */
    public void deleteNode(N label);

    /**
     * Removes all edges that have the given label
     * An adjacent call to containsEdge(label) is guaranteed to return false
     * @param label the label to remove all edges that have edge.label.equals(label)
     * @spec.requires label != null
     * @spec.modifies this
     * @spec.effects Removes all edges that are labelled with label from the graph
     */
    public void deleteEdge(E label);

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
    public void deleteEdge(Edge<N, E> edge);
}
