/*
 * Author: Ryan Nelson
 * Provided as is without any warranty
 */

import React, {Component} from 'react';
import{
    direction,
    distance,
    compassBearing,
    expandedDirection
} from "./Functions"

interface DirectionProps{
    x1: number; // x coordinate of start point
    y1: number; // y coordinate of start point
    x2: number; // x coordinate of end point
    y2: number; // y coordinate of end point
    prevAngle: number; // direction of previous Direction object, 0-360 (exclusive), set to negative to indicate no previous angle
}

let key: number = 0

//component: a box with a text instruction to follow in it
class Direction extends Component<DirectionProps, {}>{
    render(){
        key++
        //no angle given for last direction
        if(this.props.prevAngle < 0){
            return(
                <div id="direction" key={key}>Go {expandedDirection(this.props.x1, this.props.y1, this.props.x2, this.props.y2)} for {distance(this.props.x1, this.props.y1, this.props.x2, this.props.y2)}.</div>
            )
        }
        //get this angle and compare it with previous path's angle
        const angle: number = compassBearing(this.props.x1, this.props.y1, this.props.x2, this.props.y2)
        let command: string = ""
        let turnAngle = (angle - this.props.prevAngle) % 360
        while(turnAngle < 0) turnAngle += 360
        //build turn/straight instruction
        if(turnAngle < 10 || turnAngle > 350) command = "Go straight"
        else{
            command = "Turn "
            if(turnAngle < 25 || turnAngle > 335) command += "slightly "
            if(turnAngle < 160) command += "right"
            else if(turnAngle > 200) command += "left"
            else command += "around"
        }
        return(
            <div id="direction" key={key}>{command} ({direction(this.props.x2, this.props.y1, this.props.x1, this.props.y2)}) and walk {distance(this.props.x1, this.props.y1, this.props.x2, this.props.y2)}.</div>
        )
    }
}

export default Direction;