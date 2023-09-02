/*
 * Author: Ryan Nelson
 * Provided as is without any warranty
 */

import React, {Component} from 'react';
import Selector from './Selector';

interface ColorListProps{
    onChange(color: string): any
    color: string
}

interface ColorListState{
    colors: string[]
}

//component selectable combo box of colors
class ColorList extends Component<ColorListProps, ColorListState>{
    constructor(props: ColorListProps){
        super(props)
        //fill state list of colors
        this.state = { colors:[
            "Aqua",
            "Black",
            "Blue",
            "Fuchsia",
            "Gray",
            "Green",
            "Lime",
            "Maroon",
            "Navy",
            "Olive",
            "Purple",
            "Red",
            "Silver",
            "Teal",
            "White",
            "Yellow"
        ]}
    }

    render(){
        //create list of elements, one for each color in state
        let buildingsJSX: JSX.Element[] = []
        for(let color of this.state.colors){
            let selector = new Selector({name:color})
            buildingsJSX.push(selector.render())
        }
        //render the select combo box with each color
        return(
            <div>
                <label>Color:</label>
                <select name="Color"
                        value={this.props.color}
                        onChange={(event) => {
                            this.props.onChange(event.target.value)
                        }}>
                    { buildingsJSX }
                </select>
            </div>
        )
    }
}

export default ColorList;