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

import React, { Component } from "react";
import { Polyline } from "react-leaflet";
import{
    xToLon,
    yToLat
} from "./Functions"

interface MapLineProps {
  color: string; // color of line
  x1: number; // x coordinate of start point
  y1: number; // y coordinate of start point
  x2: number; // x coordinate of end point
  y2: number; // y coordinate of end point
}

/**
 * A component that will render a line on the React Leaflet map of color from
 * point x1,y1 to x2,y2. This line will convert from the assignment's coordinate
 * system (where 0,0 is the top-left of the UW campus) to latitude and
 * longitude, which the React Leaflet map uses
 */
class MapLine extends Component<MapLineProps, {}> {
  constructor(props: any) {
    super(props);
    this.state = {
      edgeText: "",
    };
  }

  render() {
    return (
      <Polyline
        // Path options includes color, among a variety of line customizations
        pathOptions={{ color: this.props.color }}
        // Positions are a list of latitude,longitude pairs that consist of the
        // points on the line we draw on the map
        positions={[
          [yToLat(this.props.y1), xToLon(this.props.x1)],
          [yToLat(this.props.y2), xToLon(this.props.x2)],
        ]}
      />
    );
  }
}

export default MapLine;
