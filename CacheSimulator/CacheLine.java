package CacheSimulator;

import java.util.Date;

/**
 * A single cache line in a set. Does not actually contain 'Data', other
 * than what is necessary for the simulator.
 *
 * @author Kevin Filanowski
 * @author Caleb Tupone
 * @version April 29, 2019
 * CS 350 Project 4
 */
public class CacheLine {
    /** Determines whether or not the data in a line is valid*/
    private boolean validBit;
    /** The time the cacheline was created. */
    private Date timeStamp;
    /** The tag of the data*/
    private int tag;
    /** The bit that determines if the data has been written to memory*/
    private boolean dirtyBit;

    /**
     * This is the contructor of a Cacheline
     */
    public CacheLine() {
        validBit = false;
        timeStamp = new Date();
    }

    /**
     * This method refreshes the time stamp, so that it is no longer LRU
     */
    public void resetTimeStamp() {
        timeStamp = new Date();
    }

    /**
     * This method converts the Cachline into a readable string.
     * @return - A formatted string with information regarding the fields.
     */
     @Override
    public String toString() {
        return "validBit: " + validBit + ", tag: " + tag + ", dirtyBit: "
                                + dirtyBit + ", time: " + getTimeStamp();
    }

    /**
     * This method gets the current time stamp of the Cacheline in milliseconds.
     *
     * @return - The time this object was created in milliseconds.
     */
    public long getTimeStamp() {
        return timeStamp.getTime();
    }

    /**
     * This method sets the valid bit of the Cacheline
     * @param validBit whether or not the data is valid
     */
    public void setValidBit(boolean validBit) {
        this.validBit = validBit;
    }

    /**
     * This method gets the current valid bit of the Cacheline
     * @return the current valid bit of the Cacheline
     */
    public boolean getValidBit() {
        return validBit;
    }

    /**
     * This method gets the current tag of the Cacheline.
     * @return the current tag of the Cacheline.
     */
    public int getTag() {
        return tag;
    }

    /**
     * This method sets the tag of the Cacheline.
     *
     * @param tag the new tag of the CacheLine.
     */
    public void setTag(int tag) {
        this.tag = tag;
    }

    /**
     * Fetches the dirty bit boolean value.
     *
     * @return - The dirty bit boolean value.
     */
    public boolean getDirtyBit(){
        return this.dirtyBit;
    }

    /**
     * Sets the dirty bit value to the boolean value passed in.
     *
     * @param bool - The boolean value to set the dirty bit to.
     */
    public void setDirtyBit(boolean bool){
        this.dirtyBit = bool;
    }
}
