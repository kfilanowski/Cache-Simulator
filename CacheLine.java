
import java.util.Date;

/**
 *
 *
 */
public class CacheLine {
    /** */
    private boolean validBit;
    /** */
    private Date timeStamp;
    /** */
    private int tag;
    /** */
    private boolean dirtyBit;

    /**
     * This is the contructor of a Cacheline
     */
    public CacheLine() {
        validBit = false;
        timeStamp = new Date();
    }

    /**
     * This method converts the Cachline into a readable string
     * @return the readable string
     */
    public String toString() {
        return "cachie";
    }

    /**
     * This method refreshes the time stamp, so that it is no longer LRU
     */
    public void resetTimeStamp() {
        timeStamp = new Date();
    }

    /**
     * This method gets the current time stamp of the Cacheline in milliseconds.
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
     * This method gets the current tag of the Cacheline
     * @return the current tag of the Cacheline
     */
    public int getTag() {
        return tag;
    }

    /**
     * This method sets the tag of the Cacheline
     * @param tag the new tag of the CacheLine
     */
    public void setTag(int tag) {
        this.tag = tag;
    }

    public boolean getDirtyBit(){
        return this.dirtyBit;
    }

    public void setDirtyBit(boolean bool){
        this.dirtyBit = bool;
    }

}
