package multigraph.junitTests;

import multigraph.Edge;
import multigraph.LinearMultigraph;
import multigraph.Multigraph;
import multigraph.NodeMultigraph;
import org.junit.Test;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Not for grading;
 * Fun little idea I had for testing
 */
public class RandomMultigraphTest {
    private static final int NUM_ACTIONS = 100;
    private static final PrintStream out = System.err;
    private static final Random r = new Random();

    @Test
    public void randomMultigraphTest(){
        Multigraph<String, String> lm = new LinearMultigraph<>(),
                    nm = new NodeMultigraph<>();
        for(int i = 0; i < NUM_ACTIONS; i++){
            switch(r.nextInt(6)){
                case 0://add node
                    String node = randomString();
                    lm.addNode(node);
                    nm.addNode(node);
                    out.println("Added node: " + node);
                    break;
                case 1://add edge
                    if(lm.size() == 0){
                        i--;
                        continue;
                    }
                    String origin = randomNode(lm);
                    String terminus = randomNode(nm);
                    String label = randomString();
                    Edge<String, String> edge = new Edge<>(origin, terminus, label);
                    nm.addEdge(edge);
                    lm.addEdge(edge);
                    out.println("Added edge: " + label + " from " + origin + " to " + terminus);
                    break;
                case 2://clone
                    switch(r.nextInt(2)){
                        case 0:
                            Multigraph<String, String> tmp = lm;
                            lm = new LinearMultigraph<>(nm);
                            nm = new NodeMultigraph<>(tmp);
                            out.println("Switch cloned both objects");
                            break;
                        case 1:
                            lm = new LinearMultigraph<>(lm);
                            nm = new NodeMultigraph<>(nm);
                            out.println("Straight cloned both objects");
                            break;
                    }
                    break;
                case 3://delete node
                    if(lm.size() == 0){
                        i--;
                        continue;
                    }
                    String del = randomNode(lm);
                    lm.deleteNode(del);
                    nm.deleteNode(del);
                    out.println("Deleted node: " + del);
                    break;
                case 4://delete edge
                    if(lm.size() == 0 || lm.listEdges().isEmpty()){
                        i--;
                        continue;
                    }
                    Edge<String, String> delEdge = randomEdge(lm);
                    lm.deleteEdge(delEdge);
                    nm.deleteEdge(delEdge);
                    out.println("Deleted edge " + delEdge.getLabel() + " from " + delEdge.getParentNode() + " to " + delEdge.getChildNode());
                    break;
                case 5://delete edge label
                    if(lm.size() == 0 || lm.listEdges().isEmpty()){
                        i--;
                        continue;
                    }
                    String delEdgeLabel = randomEdge(lm).getLabel();
                    lm.deleteEdge(delEdgeLabel);
                    nm.deleteEdge(delEdgeLabel);
                    out.println("Deleted edges labelled: " + delEdgeLabel);
                    break;
            }
            assertTrue(mgEquals(lm, nm));
        }
    }

    private static String randomString(){
        StringBuilder stringBuilder = new StringBuilder(256);
        for(int i = 0; i < 256; i++){
            stringBuilder.append((char)r.nextInt(97, 122));//"a...z"
        }
        return stringBuilder.toString();
    }

    private static <N> N randomNode(Multigraph<N, ?> graph){
        List<N> nodes = graph.listNodes();
        return nodes.get(r.nextInt(nodes.size()));
    }

    private static <N, E> Edge<N, E> randomEdge(Multigraph<N, E> graph){
        List<Edge<N, E>> edges = graph.listEdges();
        return edges.get(r.nextInt(edges.size()));
    }

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
}
