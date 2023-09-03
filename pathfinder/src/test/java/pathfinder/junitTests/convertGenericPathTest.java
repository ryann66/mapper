/*
 * Author: Ryan Nelson
 * Provided as is without any warranty
 */

package pathfinder.junitTests;

import org.junit.Test;
import pathfinder.CampusMap;
import pathfinder.GraphUtils;
import pathfinder.datastructures.Path;
import pathfinder.datastructures.Point;

import static org.junit.Assert.assertEquals;

public class convertGenericPathTest {
    @Test
    public void testConvertGenericPathTest(){
        Point A = new Point(1, 1);
        Point B = new Point(2, 2);
        Point C = new Point(3, 3);

        Path nPath = new Path(A);
        GraphUtils.Path<Point> gPath = new GraphUtils.Path<>(A);

        assertEquals(nPath, CampusMap.convertGenericPath(gPath));

        nPath.extend(B, 5);
        gPath.extend(B, 5);

        assertEquals(nPath, CampusMap.convertGenericPath(gPath));

        nPath.extend(C, 3);
        gPath.extend(C, 3);

        assertEquals(nPath, CampusMap.convertGenericPath(gPath));

        nPath.extend(B, 1);
        gPath.extend(B, 1);

        assertEquals(nPath, CampusMap.convertGenericPath(gPath));
    }
}
