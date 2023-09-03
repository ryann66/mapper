/*
 * Author: Ryan Nelson
 * Provided as is without any warranty
 */

package multigraph.junitTests;

import multigraph.Edge;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Clear box test of Multigraph.Edge
 */
public class EdgeTest {

    /**
     * Tests getters
     */
    @Test
    public void testBasicFunctions(){
        Edge<String, String> nullEdge = new Edge<>(null, null, null);
        assertNull(nullEdge.getParentNode());
        assertNull(nullEdge.getChildNode());
        assertNull(nullEdge.getLabel());

        Edge<String, String> normalEdge = new Edge<>("pNode", "cNode", "name1");
        assertEquals("pNode", normalEdge.getParentNode());
        assertEquals("cNode", normalEdge.getChildNode());
        assertEquals("name1", normalEdge.getLabel());
    }

    /**
     * Test equals
     */
    @Test
    public void testEquals(){
        Edge<String, String> norm1 = new Edge<>("pNode", "cNode", "name1");
        Edge<String, String> norm2 = new Edge<>("pNode", "cNode", "name1");
        Edge<String, String> bad1 = new Edge<>("foo", "cNode", "name1");
        Edge<String, String> bad2 = new Edge<>("pNode", "bar", "name1");
        Edge<String, String> bad3 = new Edge<>("pNode", "cNode", "foobar");
        Edge<String, String> bad4 = new Edge<>("fizz", "buzz", "fizzbuzz");

        assertEquals(norm2, norm1);
        assertNotEquals(null, norm1);
        assertNotEquals(bad1, norm1);
        assertNotEquals(bad2, norm1);
        assertNotEquals(bad3, norm1);
        assertNotEquals(bad4, norm1);
    }

    /**
     * Test consistency with equals
     */
    @Test
    public void testHashcode(){
        Edge<String, String> norm = new Edge<>("pNode", "cNode", "name1");
        List<Edge<String, String>> variants = new ArrayList<>();
        variants.add(new Edge<>("pNode", "cNode", "name1"));
        variants.add(new Edge<>("foo", "cNode", "name1"));
        variants.add(new Edge<>("pNode", "bar", "name1"));
        variants.add(new Edge<>("pNode", "cNode", "foobar"));
        variants.add(new Edge<>("fizz", "buzz", "fizzbuzz"));
        //if equals, then they must have the same hash
        for(Edge<String, String> edge : variants){
            if(norm.equals(edge)) {
                assertTrue(norm.hashCode() == edge.hashCode());
            }
        }
    }
}
