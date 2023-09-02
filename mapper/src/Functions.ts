/*
 * Author: Ryan Nelson
 * Provided as is without any warranty
 */

import {
  UW_LATITUDE,
  UW_LATITUDE_OFFSET,
  UW_LATITUDE_SCALE,
  UW_LONGITUDE,
  UW_LONGITUDE_OFFSET,
  UW_LONGITUDE_SCALE,
  EARTH_RADIUS_FEET
} from "./Constants";

/**
 * Converts x coordinate to longitude
 */
export const xToLon = (x: number): number =>  {
    return UW_LONGITUDE + (x - UW_LONGITUDE_OFFSET) * UW_LONGITUDE_SCALE;
}

/**
 * Converts y coordinate to latitude
 */
export const yToLat = (y: number): number =>  {
    return UW_LATITUDE + (y - UW_LATITUDE_OFFSET) * UW_LATITUDE_SCALE;
}

/**
 * returns the bearing in degrees (centered at N, clockwise) of the line from (x1, y1) to (x2, y2)
 */
export const compassBearing = (x1: number, y1: number, x2: number, y2: number): number => {
    let diffX: number = x2 - x1
    let diffY: number = y2 - y1
    if(diffY === 0){
        if(diffX > 0) return 90
        if(diffX < 0) return 270
        return NaN
    }
    let theta: number = Math.atan(diffX / diffY) * 180 / Math.PI
    if(diffY < 0){
        theta += 180
    }
    while(theta < 0) theta += 360
    theta %= 360
    return theta
}

/*
 * Gets the compass bearing of travel of the given line as a string (1-3 characters)
 */
export const direction = (x1: number, y1: number, x2: number, y2: number): string => {
    const bearing: number = compassBearing(x1, y1, x2, y2)
    if(bearing < 11.25) return "N"
    if(bearing < 33.75) return "NNE"
    if(bearing < 56.25) return "NE"
    if(bearing < 78.75) return "ENE"
    if(bearing < 101.25) return "E"
    if(bearing < 123.75) return "ESE"
    if(bearing < 146.25) return "SE"
    if(bearing < 168.75) return "SSE"
    if(bearing < 191.25) return "S"
    if(bearing < 213.75) return "SSW"
    if(bearing < 236.25) return "SW"
    if(bearing < 258.75) return "WSW"
    if(bearing < 281.25) return "W"
    if(bearing < 303.75) return "WNW"
    if(bearing < 326.25) return "NW"
    if(bearing < 348.75) return "NNW"
    return "N"
}

/*
 * Gets the english word cardinal direction of the line
 * e.g. "east northeast"
 */
export const expandedDirection = (x1: number, y1: number, x2: number, y2: number): string => {
    let dir: string = direction(x1, y1, x2, y2)
    let ret: string = ""
    if(dir.length === 1 || dir.length === 3){
        if(dir.charAt(0) === "N") ret += "north"
        else if(dir.charAt(0) === "E") ret += "east"
        else if(dir.charAt(0) === "S") ret += "south"
        else if(dir.charAt(0) === "W") ret += "west"
        if(dir.length === 3) ret += "-"
        dir = dir.substring(1)
    }
    if(dir.length === 2){
        if(dir === "NW") ret += "northwest"
        else if(dir === "SW") ret += "southwest"
        else if(dir === "NE") ret += "northeast"
        else if(dir === "SE") ret += "southeast"
    }
    return ret
}

/*
 * Gets the distance of the line as a string with unit
 */
export const distance = (x1: number, y1: number, x2: number, y2: number): string => {
    x1 = xToLon(x1)
    y1 = yToLat(y1)
    x2 = xToLon(x2)
    y2 = yToLat(y2)

    const thetaX: number = Math.abs(x2 - x1)
    const thetaY: number = Math.abs(y2 - y1)

    const arcX: number = EARTH_RADIUS_FEET * thetaX * Math.PI / 180
    const arcY: number = EARTH_RADIUS_FEET * thetaY * Math.PI / 180

    let dist: number = Math.sqrt(Math.pow(arcX, 2) + Math.pow(arcY, 2))
    dist = Math.round(dist)
    return dist.toString() + " ft"
}