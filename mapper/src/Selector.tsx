/*
 * Author: Ryan Nelson
 * Provided as is without any warranty
 */

import React, {Component} from 'react';

interface SelectorProps{
    name: string
}

let index = 0//key for ensuring unique keys

//react component for creating selectable options
class Selector extends Component<SelectorProps, {}>{
    render(){
        index++
        return(
            <option key={index} value={this.props.name}>{this.props.name}</option>
        )
    }
}

export default Selector;