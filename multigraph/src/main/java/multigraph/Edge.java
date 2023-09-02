package multigraph;

/**
 * This immutable class represents an edge between two nodes.  Consider a unidirectional, labeled connection
 * between two labeled points.
 */
public class Edge<N, E> {
    //RI: (none)
    //AF: Represents a unidirectional, labeled graph edge from
    //      this.parentNode to this.childNode, with the label this.label

    private final E label;
    private final N parentNode, childNode;

    /**
     * Creates a new edge with the given parent and child nodes
     *
     * @param parentNode the parent node of childNode
     * @param childNode  the child node of parentNode
     * @param label      the label of (this)
     */
    public Edge(N parentNode, N childNode, E label) {
        this.parentNode = parentNode;
        this.childNode = childNode;
        this.label = label;
    }

    /**
     * Returns the label of the edge
     *
     * @return the label of the edge
     */
    public E getLabel() {
        return label;
    }

    /**
     * Returns the parent node of the edge
     *
     * @return the parent node of the edge
     */
    public N getParentNode() {
        return parentNode;
    }

    /**
     * Returns the child node of the edge
     *
     * @return the child node of the edge
     */
    public N getChildNode() {
        return childNode;
    }

    /**
     * Checks if (this) is equal to (o)
     *
     * @param o the object to compare equality to
     * @return returns true if this is the same edge as o
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof Edge)) return false;

        Edge other = (Edge) o;
        if (!this.label.equals(other.label)) return false;
        if (!this.childNode.equals(other.childNode)) return false;
        if (!this.parentNode.equals(other.parentNode)) return false;
        return true;
    }

    /**
     * Returns the hash value of (this)
     *
     * @return the hash value of (this)
     */
    public int hashCode() {
        int result = label != null ? label.hashCode() : 0;
        result = 31 * result + (parentNode != null ? parentNode.hashCode() : 0);
        result = 31 * result + (childNode != null ? childNode.hashCode() : 0);
        return result;
    }
}
