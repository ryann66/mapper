/*
 * Author: Ryan Nelson
 * Provided as is without any warranty
 */

package multigraph.junitTests;

import multigraph.Edge;
import multigraph.LinearMultigraph;
import multigraph.Multigraph;
import multigraph.NodeMultigraph;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class MultigraphConstructorsTest {

    /**
     * helper method for testing if two multigraphs are equal
     * @param m1 first multigraph, must not be null
     * @param m2 second multigraph, must not be null
     * @return true if the two multigraphs are equal, else false
     */
    private static <N, E> boolean mgEquals(Multigraph<N, E> m1, Multigraph<N, E> m2){
        Set<N> m1Nodes = new HashSet<>(m1.listNodes()),
                m2Nodes = new HashSet<>(m2.listNodes());
        if(!m1Nodes.equals(m2Nodes)) return false;

        Set<Edge<N, E>> m1edges = new HashSet<>(m1.listEdges()),
                m2edges = new HashSet<>(m2.listEdges());
        return m1edges.equals(m2edges);
    }

    /**
     * Tests the generic multigraph constructor of linear multigraph
     */
    @Test
    public void TestLinearMultigraph(){
        LinearMultigraph<String, String> lm = new LinearMultigraph<>();
        Multigraph<String, String> nm = new NodeMultigraph<>();
        assertTrue(mgEquals(nm, lm));
        assertTrue(mgEquals(nm, new NodeMultigraph<>(lm)));

        nm.addNode("node1");
        lm.addNode("node1");
        assertTrue(mgEquals(nm, lm));
        assertTrue(mgEquals(nm, new NodeMultigraph<>(lm)));

        nm.addEdge(new Edge<>("node1", "node1", "self"));
        lm.addEdge(new Edge<>("node1", "node1", "self"));
        assertTrue(mgEquals(nm, lm));
        assertTrue(mgEquals(nm, new NodeMultigraph<>(lm)));

        nm.addNode("node2");
        lm.addNode("node2");
        nm.addNode("node3");
        lm.addNode("node3");
        assertTrue(mgEquals(nm, lm));
        assertTrue(mgEquals(nm, new NodeMultigraph<>(lm)));

        nm.addEdge(new Edge<>("node2", "node3", "bi"));
        lm.addEdge(new Edge<>("node2", "node3", "bi"));
        assertTrue(mgEquals(nm, lm));
        assertTrue(mgEquals(nm, new NodeMultigraph<>(lm)));

        nm.addEdge(new Edge<>("node3", "node2", "bi"));
        lm.addEdge(new Edge<>("node3", "node2", "bi"));
        assertTrue(mgEquals(nm, lm));
        assertTrue(mgEquals(nm, new NodeMultigraph<>(lm)));

        nm.addEdge(new Edge<>("node2", "node3", "mono"));
        lm.addEdge(new Edge<>("node2", "node3", "mono"));
        assertTrue(mgEquals(nm, lm));
        assertTrue(mgEquals(nm, new NodeMultigraph<>(lm)));
    }
    
    /**
     * Tests the generic multigraph constructor of node multigraph
     */
    @Test
    public void TestNodeMultigraph(){
        LinearMultigraph<String, String> lm = new LinearMultigraph<>();
        Multigraph<String, String> nm = new NodeMultigraph<>();
        assertTrue(mgEquals(nm, lm));
        assertTrue(mgEquals(nm, new NodeMultigraph<>(lm)));

        nm.addNode("node1");
        lm.addNode("node1");
        assertTrue(mgEquals(nm, lm));
        assertTrue(mgEquals(nm, new NodeMultigraph<>(lm)));

        nm.addEdge(new Edge<>("node1", "node1", "self"));
        lm.addEdge(new Edge<>("node1", "node1", "self"));
        assertTrue(mgEquals(nm, lm));
        assertTrue(mgEquals(nm, new NodeMultigraph<>(lm)));

        nm.addNode("node2");
        lm.addNode("node2");
        nm.addNode("node3");
        lm.addNode("node3");
        assertTrue(mgEquals(nm, lm));
        assertTrue(mgEquals(nm, new NodeMultigraph<>(lm)));

        nm.addEdge(new Edge<>("node2", "node3", "bi"));
        lm.addEdge(new Edge<>("node2", "node3", "bi"));
        assertTrue(mgEquals(nm, lm));
        assertTrue(mgEquals(nm, new NodeMultigraph<>(lm)));

        nm.addEdge(new Edge<>("node3", "node2", "bi"));
        lm.addEdge(new Edge<>("node3", "node2", "bi"));
        assertTrue(mgEquals(nm, lm));
        assertTrue(mgEquals(nm, new NodeMultigraph<>(lm)));

        nm.addEdge(new Edge<>("node2", "node3", "mono"));
        lm.addEdge(new Edge<>("node2", "node3", "mono"));
        assertTrue(mgEquals(nm, lm));
        assertTrue(mgEquals(nm, new NodeMultigraph<>(lm)));
    }
}
