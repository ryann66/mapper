/*
 * Author: Ryan Nelson
 * Provided as is without any warranty
 */

package multigraph.junitTests;

import multigraph.LinearMultigraph;
import multigraph.Edge;

import org.junit.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

/**
 * Black box test suite for multigraph
 */
public class LinearMultigraphTest {

    /**
     * helper method for testing if two multigraphs are equal
     * @param m1 first multigraph, must not be null
     * @param m2 second multigraph, must not be null
     * @return true if the two multigraphs are equal, else false
     */
    private static <N, E> boolean mgEquals(LinearMultigraph<N, E> m1, LinearMultigraph<N, E> m2){
        if(!m1.listNodes().equals(m2.listNodes())) return false;
        return m1.listEdges().equals(m2.listEdges());
    }

    /**
     * test cloning
     */
    @Test
    public void copyTest(){
        assertThrows(NullPointerException.class, ()->new LinearMultigraph<String, String>(null));
        LinearMultigraph<String, String> mg = new LinearMultigraph<>();
        assertTrue(mgEquals(mg, new LinearMultigraph<>(mg)));
        mg.addNode("node1");
        mg.addEdge(new Edge<>("node1", "node1", "edge1"));
        assertTrue(mgEquals(mg, new LinearMultigraph<String, String>(mg)));
        mg.addNode("node2");
        mg.addEdge(new Edge<>("node2", "node1", "edge2"));
        mg.addNode("island");
        assertTrue(mgEquals(mg, new LinearMultigraph<String, String>(mg)));

        //check deep copy
        LinearMultigraph<String, String> addN = new LinearMultigraph<>(mg);
        addN.addNode("foo");
        LinearMultigraph<String, String> addE = new LinearMultigraph<>(mg);
        addE.addEdge(new Edge<>("node1", "node2", "bar"));
        LinearMultigraph<String, String> delN = new LinearMultigraph<>(mg);
        delN.deleteNode("node1");
        LinearMultigraph<String, String> delN2 = new LinearMultigraph<>(mg);
        delN2.deleteNode("island");
        LinearMultigraph<String, String> delE = new LinearMultigraph<>(mg);
        delE.deleteEdge("edge1");
        assertNotSame(mg, new LinearMultigraph<>(mg));
        assertFalse(mgEquals(mg, addN));
        assertFalse(mgEquals(mg, addE));
        assertFalse(mgEquals(mg, delN));
        assertFalse(mgEquals(mg, delN2));
        assertFalse(mgEquals(mg, delE));
    }

    /**
     * test size
     */
    @Test
    public void sizeTest(){
        LinearMultigraph<String, String> mg = new LinearMultigraph<>();
        assertEquals(0, mg.size());
        mg.addNode("node1");
        assertEquals(1, mg.size());
        mg.addEdge(new Edge<>("node1", "node1", "edge"));
        assertEquals(1, mg.size());
        mg.addNode("node2");
        assertEquals(2, mg.size());
        mg.addEdge(new Edge<>("node1", "node2", "edge"));
        assertEquals(2, mg.size());
    }

    /**
     * test listParentEdges
     */
    @Test
    public void listParentEdgesTest(){
        assertThrows(NoSuchElementException.class, ()->new LinearMultigraph<String, String>().listParentEdges("notanode"));

        LinearMultigraph<String, String> mg = new LinearMultigraph<>();
        List<Edge<String, String>> edges;

        //no parents
        mg.addNode("parent");
        edges = mg.listParentEdges("parent");
        assertEquals(0, edges.size());

        //self parent
        Edge<String, String> e1 = new Edge<>("parent", "parent", "e1");
        mg.addEdge(e1);
        edges = mg.listParentEdges("parent");
        assertEquals(1, edges.size());
        assertTrue(edges.contains(e1));

        mg.addNode("child");
        Edge<String, String> e2 = new Edge<>("parent", "child", "e2");
        mg.addEdge(e2);
        edges = mg.listParentEdges("child");
        assertEquals(1, edges.size());
        assertTrue(edges.contains(e2));

        Edge<String, String> e3 = new Edge<>("parent", "child", "e3");
        mg.addEdge(e3);
        edges = mg.listParentEdges("child");
        assertEquals(2, edges.size());
        assertTrue(edges.contains(e2));
        assertTrue(edges.contains(e3));
    }

    /**
     * test listChildEdges
     * NOTE: limited test, most functionality is tested with script testing
     */
    @Test
    public void listChildEdgesTest(){
        assertThrows(NoSuchElementException.class, ()->new LinearMultigraph<String, String>().listChildEdges("notanode"));
    }

    /**
     * test listEdges
     */
    @Test
    public void listEdgesTest(){
        LinearMultigraph<String, String> mg = new LinearMultigraph<>();
        List<Edge<String, String>> edges;

        //no edges
        mg.addNode("parent");
        edges = mg.listEdges();
        assertEquals(0, edges.size());

        //1 edge (self-directional)
        Edge<String, String> e1 = new Edge<>("parent", "parent", "e1");
        mg.addEdge(e1);
        edges = mg.listEdges();
        assertEquals(1, edges.size());
        assertTrue(edges.contains(e1));

        //2 edges (unidirectional)
        mg.addNode("child");
        Edge<String, String> e2 = new Edge<>("parent", "child", "e2");
        mg.addEdge(e2);
        edges = mg.listEdges();
        assertEquals(2, edges.size());
        assertTrue(edges.contains(e1));
        assertTrue(edges.contains(e2));

        //3 edges (bidirectional)
        Edge<String, String> e3 = new Edge<>("parent", "child", "e3");
        mg.addEdge(e3);
        edges = mg.listEdges();
        assertEquals(3, edges.size());
        assertTrue(edges.contains(e1));
        assertTrue(edges.contains(e2));
        assertTrue(edges.contains(e3));
    }

    /**
     * test containsNode
     */
    @Test
    public void containsNodeTest(){
        LinearMultigraph<String, String> mg = new LinearMultigraph<>();

        //no contains
        assertFalse(mg.containsNode("node1"));
        mg.addNode("foo");
        assertFalse(mg.containsNode("node1"));
        mg.addNode("bar");
        assertFalse(mg.containsNode("node1"));

        //contains
        mg = new LinearMultigraph<>();
        mg.addNode("node1");
        assertTrue(mg.containsNode("node1"));
        mg.addNode("foobar");
        assertTrue(mg.containsNode("node1"));
    }

    /**
     * test containsEdge
     * Tests for all overloaded methods
     */
    @Test
    public void containsEdgeTest(){
        LinearMultigraph<String, String> mg = new LinearMultigraph<>();
        LinearMultigraph<String, String> mg2 = new LinearMultigraph<>();
        Edge<String, String> e = new Edge<>("node1", "node2", "e");
        Edge<String, String> e2 = new Edge<>("node1", "node3", "e");
        Edge<String, String> e3 = new Edge<>("node1", "node3", "foobar");
        Edge<String, String> e4 = new Edge<>("node2", "node3", "fizzbuzz");
        mg.addNode("node1");
        mg.addNode("node2");
        mg.addNode("node3");
        mg2.addNode("node1");
        mg2.addNode("node2");
        mg2.addNode("node3");

        //contains - 1 element
        mg.addEdge(e);
        assertTrue(mg.containsEdge(e));
        assertTrue(mg.containsEdge("e"));

        //2 elements
        mg.addEdge(e2);
        assertTrue(mg.containsEdge(e2));
        assertTrue(mg.containsEdge("e"));

        //no contains - 0 elements
        assertFalse(mg2.containsEdge(e));
        assertFalse(mg2.containsEdge("e"));

        //1 element
        mg2.addEdge(e3);
        assertFalse(mg2.containsEdge(e));
        assertFalse(mg2.containsEdge("e"));

        //2 elements
        mg2.addEdge(e4);
        assertFalse(mg2.containsEdge(e));
        assertFalse(mg2.containsEdge("e"));
    }

    /**
     * test addEdge
     * NOTE: limited test, most functionality is tested with script testing
     */
    @Test
    public void addEdgeTest(){
        assertThrows(NoSuchElementException.class, ()-> {
            LinearMultigraph<String, String> mg = new LinearMultigraph<>();
            mg.addNode("node1");
            mg.addEdge(new Edge<>("node1", "node2", "edge"));
        });
        assertThrows(NoSuchElementException.class, ()-> {
            LinearMultigraph<String, String> mg = new LinearMultigraph<>();
            mg.addNode("node2");
            mg.addEdge(new Edge<>("node1", "node2", "edge"));
        });
    }

    /**
     * test deleteNode
     */
    @Test
    public void deleteNodeTest(){
        LinearMultigraph<String, String> mg;

        LinearMultigraph<String, String> emp = new LinearMultigraph<>();
        LinearMultigraph<String, String> mg1 = new LinearMultigraph<>();
        LinearMultigraph<String, String> mg1e = new LinearMultigraph<>();
        LinearMultigraph<String, String> mg2 = new LinearMultigraph<>();
        LinearMultigraph<String, String> mg2e = new LinearMultigraph<>();

        Edge<String, String> self = new Edge<>("node1", "node1", "edge");
        Edge<String, String> selfE = new Edge<>("foobar", "foobar", "edge");
        Edge<String, String> bi1 = new Edge<>("node1", "foobar", "edge");
        Edge<String, String> bi2 = new Edge<>("foobar", "node1", "edge");
        Edge<String, String> bi1e = new Edge<>("fizzbuzz", "foobar", "edge");
        Edge<String, String> bi2e = new Edge<>("foobar", "fizzbuzz", "edge");

        mg1.addNode("node1");
        mg1.addEdge(self);
        mg1e.addNode("foobar");
        mg1e.addEdge(selfE);
        mg2.addNode("node1");
        mg2.addNode("foobar");
        mg2.addNode("fizzbuzz");
        mg2.addEdge(selfE);
        mg2.addEdge(bi1);
        mg2.addEdge(bi2);
        mg2e.addNode("foobar");
        mg2e.addNode("fizzbuzz");
        mg2.addEdge(bi1e);
        mg2.addEdge(bi2e);

        //delete from empty
        mg = new LinearMultigraph<>(emp);
        mg.deleteNode("node1");
        assertTrue(mgEquals(mg, emp));

        //delete only node
        mg = new LinearMultigraph<>(mg1);
        mg.deleteNode("node1");
        mg.deleteNode("fizzbuzz");
        assertTrue(mgEquals(mg, emp));
        assertFalse(mg.listEdges().contains(self));

        //do nothing with singleton
        mg = new LinearMultigraph<>(mg1e);
        mg.deleteNode("node1");
        assertTrue(mgEquals(mg, mg1e));
        assertTrue(mg.listEdges().contains(selfE));

        //delete from complex graph
        mg = new LinearMultigraph<>(mg2);
        mg.deleteNode("node1");
        mg.deleteNode("fizzbuzz");
        assertTrue(mgEquals(mg, mg1e));
        assertFalse(mg.listEdges().contains(bi1));
        assertFalse(mg.listEdges().contains(bi2));

        //do nothing with complex graph
        mg = new LinearMultigraph<>(mg2e);
        mg.deleteNode("node1");
        assertTrue(mgEquals(mg, mg2e));
        assertTrue(mgEquals(mg, mg2e));
    }

    /**
     * test deleteEdge
     * Tests for all overloaded methods
     */
    @Test
    public void deleteEdgeTest(){
        assertThrows(NoSuchElementException.class, ()->{
            LinearMultigraph<String, String> mg = new LinearMultigraph<>();
            mg.addNode("node1");
            mg.deleteEdge(new Edge<>("node1", "foobar", "edge"));
        });
        assertThrows(NoSuchElementException.class, ()->{
            LinearMultigraph<String, String> mg = new LinearMultigraph<>();
            mg.addNode("node1");
            mg.deleteEdge(new Edge<>("foobar", "node1", "edge"));
        });

        LinearMultigraph<String, String> selfMono = new LinearMultigraph<>();
        LinearMultigraph<String, String> bicon = new LinearMultigraph<>();
        LinearMultigraph<String, String> doubleUni = new LinearMultigraph<>();

        //add nodes
        selfMono.addNode("node1");
        bicon.addNode("node1");
        bicon.addNode("node2");
        doubleUni.addNode("node1");
        doubleUni.addNode("node2");

        //duplicate
        LinearMultigraph<String, String> selfMonoNop = new LinearMultigraph<>(selfMono);
        LinearMultigraph<String, String> biconNop = new LinearMultigraph<>(bicon);
        LinearMultigraph<String, String> doubleUniNop = new LinearMultigraph<>(doubleUni);

        //create edges
        Edge<String, String> self = new Edge<>("node1", "node1", "self");
        Edge<String, String> selfNop = new Edge<>("node1", "node1", "selfnop");
        Edge<String, String> bicon1 = new Edge<>("node1", "node2", "bicon1");
        Edge<String, String> bicon2 = new Edge<>("node2", "node1", "bicon2");
        Edge<String, String> bicon1Nop = new Edge<>("node1", "node2", "bicon1nop");
        Edge<String, String> double1 = new Edge<>("node1", "node2", "double1");
        Edge<String, String> double2 = new Edge<>("node1", "node2", "double2");
        Edge<String, String> doubleNop = new Edge<>("node1", "node2", "doublenop");

        //add edges
        selfMono.addEdge(self);
        selfMonoNop.addEdge(selfNop);
        bicon.addEdge(bicon1);
        bicon.addEdge(bicon2);
        biconNop.addEdge(bicon1Nop);
        biconNop.addEdge(bicon2);
        doubleUni.addEdge(double1);
        doubleUni.addEdge(double2);
        doubleUniNop.addEdge(doubleNop);
        doubleUniNop.addEdge(double2);

        LinearMultigraph<String, String> mg;

        //test self
        mg = new LinearMultigraph<>(selfMono);
        mg.deleteEdge(self);
        assertEquals(0, mg.listEdges().size());
        mg = new LinearMultigraph<>(selfMono);
        mg.deleteEdge("self");
        assertEquals(0, mg.listEdges().size());
        // (no action)
        mg = new LinearMultigraph<>(selfMonoNop);
        mg.deleteEdge(self);
        assertEquals(1, mg.listEdges().size());
        mg = new LinearMultigraph<>(selfMonoNop);
        mg.deleteEdge("self");
        assertEquals(1, mg.listEdges().size());

        //test biconditional
        mg = new LinearMultigraph<>(bicon);
        mg.deleteEdge(bicon1);
        assertEquals(1, mg.listEdges().size());
        assertTrue(mg.containsEdge(bicon2));
        mg = new LinearMultigraph<>(bicon);
        mg.deleteEdge("bicon1");
        assertEquals(1, mg.listEdges().size());
        assertTrue(mg.containsEdge("bicon2"));
        // (no action)
        mg = new LinearMultigraph<>(biconNop);
        mg.deleteEdge(bicon1);
        assertEquals(2, mg.listEdges().size());
        mg = new LinearMultigraph<>(biconNop);
        mg.deleteEdge("bicon1");
        assertEquals(2, mg.listEdges().size());

        //test double unidirectional
        mg = new LinearMultigraph<>(doubleUni);
        mg.deleteEdge(double1);
        assertEquals(1, mg.listEdges().size());
        assertTrue(mg.containsEdge(double2));
        mg = new LinearMultigraph<>(doubleUni);
        mg.deleteEdge("double1");
        assertEquals(1, mg.listEdges().size());
        assertTrue(mg.containsEdge("double2"));
        // (no action)
        mg = new LinearMultigraph<>(doubleUniNop);
        mg.deleteEdge(double1);
        assertEquals(2, mg.listEdges().size());
        mg = new LinearMultigraph<>(doubleUniNop);
        mg.deleteEdge("double1");
        assertEquals(2, mg.listEdges().size());

        //test multiple of same label all getting deleted
        LinearMultigraph<String, String> doubleLabel = new LinearMultigraph<>();
        doubleLabel.addNode("n1");
        doubleLabel.addNode("n2");
        doubleLabel.addNode("n3");
        doubleLabel.addEdge(new Edge<>("n3", "n1", "nopEdge"));
        LinearMultigraph<String, String> expected = new LinearMultigraph<>(doubleLabel);
        doubleLabel.addEdge(new Edge<>("n1", "n2", "comEdge"));
        doubleLabel.addEdge(new Edge<>("n1", "n3", "comEdge"));

        mg = new LinearMultigraph<>(doubleLabel);
        mg.deleteEdge("comEdge");
        assertEquals(expected.listEdges(), mg.listEdges());
    }
}
