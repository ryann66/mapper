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

import { LatLngExpression } from "leaflet";
import React, { Component } from "react";
import { MapContainer, TileLayer } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import MapLine from "./MapLine";
import {
  UW_LATITUDE_CENTER,
  UW_LONGITUDE_CENTER
} from "./Constants";
import{
    xToLon,
    yToLat
} from "./Functions"

interface MapProps {
  lines: MapLine[]
}

//count used to create unique new keys and ensure new map on redraw
let index: number = 0

//a component that draws a leaflet map with all the lines given in MapProps drawn upon it
class Map extends Component<MapProps, {}> {

  render() {
    //default position and scale
    let position: LatLngExpression = [UW_LATITUDE_CENTER, UW_LONGITUDE_CENTER]
    let scale: number = 15
    let lineElements: JSX.Element[] = []

    //if there are lines, render and update map pos
    if(this.props.lines.length !== 0){
        //set initial min/max bounds
        let minX: number = this.props.lines[0].props.x1
        let minY: number = this.props.lines[0].props.y1
        let maxX: number = this.props.lines[0].props.x2
        let maxY: number = this.props.lines[0].props.y2
        for(let line of this.props.lines){
            //create render lines
            lineElements.push(line.render())
            //update min/max bounds
            minX = Math.min(line.props.x1, minX)
            minY = Math.min(line.props.y1, minY)
            maxX = Math.max(line.props.x2, maxX)
            maxY = Math.max(line.props.y2, maxY)
        }
        //center map using calculated min/max bounds of path
        position = [yToLat(minY + (maxY - minY) / 2),
                    xToLon(minX + (maxX - minX) / 2)]

        //scale map using the width/height
        let diffX = Math.abs(xToLon(maxX) - xToLon(minX))
        let diffY = Math.abs(yToLat(maxY) - yToLat(minY))
        //leaflet logarithmic map scale
        scale = Math.min(Math.log2(360 / diffX), Math.log2(360 / diffY))
    }

    index++//increase index to ensure new map element on every click of draw button

    return (
      <div id="map">
        <MapContainer
          key={index}//always redraws
          center={position}
          zoom={scale}
          scrollWheelZoom={true}
        >
          <TileLayer
            attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          />
          { lineElements }
        </MapContainer>
      </div>
    );
  }
}

export default Map;
