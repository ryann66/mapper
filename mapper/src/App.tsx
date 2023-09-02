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

import React, {Component} from 'react';
import Map from "./Map";
import MapLine from "./MapLine";
import PathFinder from "./PathFinder";
import DirectionsList from "./DirectionsList"

// Allows us to write CSS styles inside App.css, any styles will apply to all components inside <App />
import "./App.css";

interface AppState {
    lines: MapLine[]
    origin: string
    terminus: string
}

//Component app that does pathfinding routes between buildings on the UW campus and
//displays results on a map as well as using a list of directions to follow
class App extends Component<{}, AppState> { // <- {} means no props.

    constructor(props: any) {
        super(props);
        this.state = {
            lines: [],
            origin: "",
            terminus: ""
        };
    }

    render() {
        //three main components, the map and directions list in columns at the top, with
        //the buttons arranged below them
        return (
            <div id="app">
                <h1 id="app-title">Pathfinder!</h1>
                <div className="float-container">
                    <div className="float-child map">
                        <div id="mapContainer">
                            <Map lines={this.state.lines} />
                        </div>
                    </div>
                    <div className="float-child directions">
                        <div id="listContainer">
                            <DirectionsList lines={this.state.lines}
                                origin={this.state.origin} terminus={this.state.terminus}/>
                        </div>
                    </div>
                </div>
                <div id="buttons">
                    <PathFinder
                        onChange={(value: MapLine[], origin: string, terminus: string) => {
                            this.setState({lines: value, origin: origin, terminus: terminus})
                        }}
                    />
                </div>
            </div>
        );
    }
}

export default App;

