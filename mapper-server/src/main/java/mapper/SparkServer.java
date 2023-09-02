/*
 * Copyright (C) 2023 Soham Pardeshi.  All rights reserved.  ***REMOVED***
 * ***REMOVED***
 * CSE 331 for use solely during Autumn Quarter 2022 for purposes of
 * ***REMOVED***
 * ***REMOVED***
 * ***REMOVED***
 * ***REMOVED***
 * ***REMOVED***
 */

package mapper;

import com.google.gson.JsonSyntaxException;
import pathfinder.CampusMap;

import mapper.utils.CORSFilter;
import com.google.gson.Gson;
import pathfinder.datastructures.Path;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static spark.Spark.*;

public class SparkServer {

    public static void main(String[] args) {
        CORSFilter corsFilter = new CORSFilter();
        corsFilter.apply();
        // The above two lines help set up some settings that allow the
        // React application to make requests to the Spark server, even though it
        // comes from a different server.
        // You should leave these two lines at the very beginning of main().

        CampusMap map = new CampusMap();
        Gson gson = new Gson();

        //get a list of building short names
        get("/buildings", ((request, response) -> {
            response.body(gson.toJson(new ArrayList<>(map.buildingNames().keySet())));
            response.status(200);
            return response;
        }));

        //get long names for each short name given
        //use ?name=<short name> to get a single long name
        //  status 200 for success, 400 for failure
        //use ?names=[<s1>,<s2>] to get multiple names
        //  status 200 for total success, 207 for some successful conversions, 400 for total failure
        get("/longname", (request, response) -> {
            if(request.queryParams("name") != null){
                try{
                    response.body(map.longNameForShort(request.queryParams("name").toUpperCase()));
                    response.status(200);
                }
                catch(IllegalArgumentException iae){
                    response.body(request.queryParams("name") + " is not a short name");
                    response.status(400);
                }
            }
            else if(request.queryParams("names") != null){
                try{
                    String[] names = gson.fromJson(request.queryParams("names"), String[].class);
                    String[] longNames = new String[names.length];
                    int numResolved = 0;
                    for(int i = 0; i < names.length; i++){
                        try{
                            longNames[i] = map.longNameForShort(names[i].toUpperCase());
                            numResolved++;
                        }
                        catch(IllegalArgumentException iae){
                            longNames[i] = null;
                        }
                    }
                    //return results
                    if(numResolved == 0) response.status(400);
                    else if(numResolved == names.length) response.status(200);
                    else response.status(207);
                    response.body(gson.toJson(longNames));
                }
                catch(JsonSyntaxException jse){
                    response.status(400);
                    response.body("json parsing exception");
                }
            }
            else{
                response.status(400);
                response.body("invalid query params");
            }
            return response;
        });

        //get path from origin to terminus
        //use query params origin=<short name> and terminus=<short name>
        get("/path", (request, response) -> {
            String origin = request.queryParams("origin");
            String terminus = request.queryParams("terminus");
            if(origin == null || terminus == null){
                halt(400, "Origin or terminus not present");
                return response;
            }
            origin = origin.toUpperCase();
            terminus = terminus.toUpperCase();
            if(!map.shortNameExists(origin) || !map.shortNameExists(terminus)){
                halt(400, "Origin or terminus invalid");
                return response;
            }
            try{
                Path path = map.findShortestPath(origin, terminus);
                List<String[]> segments = new LinkedList<>();
                for(Path.Segment segment : path){
                    String[] segmentPoints = new String[4];
                    segmentPoints[0] = ((Double)segment.getStart().getX()).toString();
                    segmentPoints[1] = ((Double)segment.getStart().getY()).toString();
                    segmentPoints[2] = ((Double)segment.getEnd().getX()).toString();
                    segmentPoints[3] = ((Double)segment.getEnd().getY()).toString();
                    segments.add(segmentPoints);
                }
                response.status(200);
                response.body(gson.toJson(segments));
            }catch(IllegalArgumentException iae){
                halt(422, "No path between origin and terminus");
            }
            return response;
        });

        //418 joke endpoint
        get("/coffee", ((request, response) -> {
            halt(418, "I'm a teapot!");
            return response;
        }));

        //404 page not found handler
        notFound((request, response) -> {
            halt(404, "Page not found");
            return response;
        });
    }

}
