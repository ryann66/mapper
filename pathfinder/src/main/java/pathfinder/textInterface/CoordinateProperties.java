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

package pathfinder.textInterface;

/**
 * Represents the different possible basis arrangements for a two-dimensional cartesian
 * coordinate space.
 */
public enum CoordinateProperties {

    /**
     * The x-coordinate increases in value in the rightward direction.
     * The y-coordinate increases in value in the upward direction.
     * <p>
     * This is the "standard" cartesian coordinate space in general mathematics.
     */
    INCREASING_UP_RIGHT,

    /**
     * The x-coordinate increases in value in the leftward direction.
     * The y-coordinate increases in value in the upward direction.
     */
    INCREASING_UP_LEFT,

    /**
     * The x-coordinate increases in value in the rightward direction.
     * The y-coordinate increases in value in the downward direction.
     * <p>
     * This is a commonly-used space for graphical operations where coordinates are measured from
     * the upper-left corner of some bounding box.
     */
    INCREASING_DOWN_RIGHT,

    /**
     * The x-coordinate increases in value in the leftward direction.
     * The y-coordinate increases in value in the downward direction.
     */
    INCREASING_DOWN_LEFT

}
