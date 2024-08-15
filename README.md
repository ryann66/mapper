# Introduction
Full stack application for finding the shortest path between buildings on the University of Washington Seattle campus.
This was originally a class project which was extended and modified for public viewing.  The application uses an outdated dataset of campus pathways, so it should not be used for actual pathfinding.

# Features
* Dijkstra's Shortest Path algorithm for finding the shortest path between any buildings on campus
* Fully interactive map
* Automatically generated directions to reach destination

# Organization
The program consists of two servers running in conjunction.  The front-end uses React to handle user input and display an interactive map and generated directions.  The back-end maintains the databases of buildings and pathways around the campus as well as handling pathfinding requests from the front-end.

# Dependencies
Java 8 or later
NPM 10 or later

# Running
The repository root contains a powershell script for starting the program.
Alternatively, the two servers can be launched individually.  The back-end server is launched using the Gradle task 'runSpark'. This can be done by running `./gradlew runSpark` from the repository root.  The front-end server is launched from the `./mapper/` subdirectory.  This can be launched using the commands `npm install` and `npm start` (npm install only has to be run the first time the server is launched).
The front-end server is hosted on port 3000.  The back-end server is hosted on port 4567.  The front-end port can be changed in the NPM config files without consequence, however the back-end port number should not be changed.
