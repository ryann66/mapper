/*
 * Author: Ryan Nelson
 * Provided as is without any warranty
 */

import React, {Component} from 'react';
import Selector from './Selector';

interface BuildingListProps{
    listName: string
    value: string
    onChange(building: string): any
    buildings: string[]
}

//component selectable combo box of buildings, where buildings is given in the props
class BuildingList extends Component<BuildingListProps, {}>{
    render(){
        //create list of elements for each building
        let buildingsJSX: JSX.Element[] = []
        for(let building of this.props.buildings){
            let buildingSelector = new Selector({name:building})
            buildingsJSX.push(buildingSelector.render())
        }

        //render a select combo box with one option for each building
        return(
            <div>
                <label>{this.props.listName}:</label>
                <select name={this.props.listName}
                        value={this.props.value}
                        onChange={(event) => {
                            this.props.onChange(event.target.value)
                        }}>
                    <option value="" hidden>--select an option--</option>
                    { buildingsJSX }
                </select>
            </div>
        )
    }
}

export default BuildingList;