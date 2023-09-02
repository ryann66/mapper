/*
 * Author: Ryan Nelson
 * Provided as is without any warranty
 */

import React, {Component} from 'react';
import MapLine from "./MapLine";
import BuildingList from "./BuildingList"
import ColorList from "./ColorList"

interface PathFinderProps{
    onChange(edges: MapLine[], origin: string, terminus: string): any
}

interface PathFinderState{
    buildings: string[]
    origin: string
    terminus: string
    color: string
}

//set of buttons with the ability to select buildings and create a path between them
//calls onChange() with correct path (etc.) upon update
class PathFinder extends Component<PathFinderProps, PathFinderState>{

    constructor(props: PathFinderProps){
        super(props);
        //base state with no lines, origin, or terminus
        this.state = {
            buildings: [],
            origin: "",
            terminus: "",
            color: "Red"
        }

        this.getBuildingList()
    }

    //updates this.state to have the full list of buildings
    getBuildingList = async () => {
        let localbuildings: string[] = []
        try{
            let response = await fetch("http://localhost:4567/buildings")
            localbuildings = (await response.json())
        } catch(e) {
            alert("Failure getting building list")
            console.log("Error getting building list")
            console.log(e)
        }
        this.setState({buildings: localbuildings})
    }

    //finds a path (if applicable/possible) and then calls on change to display path
    //failure will result in an empty path, with no origin or terminus
    findPath = async () => {
        //get segments from remote database
        let segments: string[][] = []
        if(this.state.origin !== "" && this.state.terminus !== "") try{
            let response = await fetch("http://localhost:4567/path?origin=" + this.state.origin + "&terminus=" + this.state.terminus)
            if(!response.ok){
                console.log("Server error " + response.status)
            } else{
                segments = await response.json()
            }
        } catch(e) {
            console.log("Error getting path from " + this.state.origin + " to " + this.state.terminus)
            console.log(e)
        }
        //create mapLines from segments
        let mapLines: MapLine[] = []
        let key = 0
        for(let index in segments){
            const segment: string[] = segments[index]
            key++
            const mapLine: MapLine = new MapLine({x1:segment[0], y1:segment[1], x2:segment[2], y2:segment[3], color:this.state.color, key:key})
            mapLines.push(mapLine)
        }
        this.props.onChange(mapLines, this.state.origin, this.state.terminus)
    }
    
    render(){
        return(
            <div>
                <BuildingList
                    listName="Origin"
                    value={this.state.origin}
                    buildings={this.state.buildings}
                    onChange={(building: string) => {
                        this.setState({origin:building})
                    }} />
                <br/>
                <BuildingList
                    listName="Terminus"
                    value={this.state.terminus}
                    buildings={this.state.buildings}
                    onChange={(building: string) => {
                        this.setState({terminus:building})
                    }} />
                <br/>
                <ColorList
                    color={this.state.color}
                    onChange={(color: string) => {
                        this.setState({color:color})
                    }} />
                <br/>
                <button type="button" onClick={(event) => {
                    this.setState({origin:"", terminus:"", color:"Red"})
                    this.props.onChange([], "", "")
                }}>Reset</button>
                <button type="button" onClick={(event) => {
                    this.findPath()
                }}>Draw</button>
            </div>
        )
    }
}

export default PathFinder;