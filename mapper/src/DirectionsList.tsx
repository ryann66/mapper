/*
 * Author: Ryan Nelson
 * Provided as is without any warranty
 */

import React, {Component} from 'react';
import MapLine from "./MapLine";
import Direction from "./Direction";
import { compassBearing } from "./Functions";

import "./DirectionsList.css"

interface DirectionsListProps{
    lines: MapLine[]
    origin: string
    terminus: string
}

let key: number = 0

// component that draws a list of directions to follow along the path (props.lines) from
// (props.origin) to (props.terminus)
class DirectionsList extends Component<DirectionsListProps, {}>{
    render(){
        //create list of elements
        let directionsJSX: JSX.Element[] = []
        if(this.props.lines.length !== 0){
            //add starting point direction
            directionsJSX.push(new CustomDirection({text:"Starting at " + this.props.origin}).render())
            //add each direction in the path
            let angle: number = -1//previous line's angle, used as a property
            for(let line of this.props.lines){
                //create directions
                const direction: Direction = new Direction({x1:line.props.x1, y1:line.props.y2, x2:line.props.x2, y2:line.props.y1, prevAngle:angle})
                directionsJSX.push(direction.render())
                angle = compassBearing(line.props.x1, line.props.y2, line.props.x2, line.props.y1)
            }
            //add closing direction
            directionsJSX.push(new CustomDirection({text:"You have reached " + this.props.terminus}).render())
        }
        return(
        <div id="directions">
            <div id="directionsHeader">
                <label>Directions:</label>
            </div>
            <div id="directionsListContainer" key={key}>
                { directionsJSX }
            </div>
        </div>
        )
    }
}

interface CustomDirectionProps{
    text: string
}

//component for making directions that do not fall into straight line between two points
class CustomDirection extends Component<CustomDirectionProps, {}>{
    // see DirectionsList.css for style
    render(){
        return(
            <div id="direction">{this.props.text}</div>
        )
    }
}

export default DirectionsList