/*
 * Author: Ryan Nelson
 * Provided as is without any warranty
 */

package pathfinder;

import multigraph.NodeMultigraph;
import multigraph.Edge;
import multigraph.Multigraph;
import pathfinder.datastructures.Path;
import pathfinder.datastructures.Point;
import pathfinder.parser.CampusBuilding;
import pathfinder.parser.CampusPath;
import pathfinder.parser.CampusPathsParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Represents a map of the campus with capabilities for pathfinding as well as working with shortened building
 * names
 */
public class CampusMap implements ModelAPI {

    /**
     * Constructs a new campus map with standard building and path sets
     */
    public CampusMap() {
        this.pathGraph = new NodeMultigraph<>();
        this.buildings = new HashMap<>();

        List<CampusBuilding> buildingsList = CampusPathsParser.parseCampusBuildings("campus_buildings.csv");
        List<CampusPath> paths = CampusPathsParser.parseCampusPaths("campus_paths.csv");

        for(CampusBuilding building : buildingsList){
            pathGraph.addNode(new Point(building.getX(), building.getY()));
            buildings.put(building.getShortName().toUpperCase(), building);
        }
        for(CampusPath path : paths){
            try{
                Point start = new Point(path.getX1(), path.getY1()), end = new Point(path.getX2(), path.getY2());
                pathGraph.addNode(start);
                pathGraph.addNode(end);
                pathGraph.addEdge(new Edge<>(start, end, path.getDistance()));
                pathGraph.addEdge(new Edge<>(end, start, path.getDistance()));
            }catch(NoSuchElementException nsee){
                //node point does not exist
            }
        }
    }

    //graph with nodes of points and edges as cost to travel between them
    private Multigraph<Point, Double> pathGraph;
    //maps building short name to building
    private Map<String, CampusBuilding> buildings;

    @Override
    public boolean shortNameExists(String shortName) {
        shortName = shortName.toUpperCase();
        return buildings.containsKey(shortName);
    }

    @Override
    public String longNameForShort(String shortName) {
        shortName = shortName.toUpperCase();
        CampusBuilding ret = buildings.get(shortName);
        if(ret == null) throw new IllegalArgumentException("Short name does not exist");
        return ret.getLongName();
    }

    @Override
    public Map<String, String> buildingNames() {
        Map<String, String> ret = new HashMap<>();
        for(String shortName : buildings.keySet()){
            ret.put(shortName, buildings.get(shortName).getLongName());
        }
        return ret;
    }

    @Override
    public Path findShortestPath(String startShortName, String endShortName) {
        startShortName = startShortName.toUpperCase();
        endShortName = endShortName.toUpperCase();
        CampusBuilding startBuilding = buildings.get(startShortName), endBuilding = buildings.get(endShortName);
        Point startPoint = new Point(startBuilding.getX(), startBuilding.getY()),
                endPoint = new Point(endBuilding.getX(), endBuilding.getY());
        GraphUtils.Path<Point> gPath = GraphUtils.shortestPath(pathGraph, startPoint, endPoint);
        return convertGenericPath(gPath);
    }

    /**
     * Converts a generic path of point type to an equivalent path from package datastructures.path
     * @param gPath generic path of type point to convert
     * @return non-generic copy of the given path
     */
    public static Path convertGenericPath(GraphUtils.Path<Point> gPath){
        Path path = new Path(gPath.getStart());
        for(GraphUtils.Path.Segment<Point> gSeg : gPath){
            path = path.extend(gSeg.getEnd(), gSeg.getCost());
        }
        return path;
    }
}
