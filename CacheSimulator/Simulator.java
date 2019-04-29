package CacheSimulator;

import java.util.Arrays;
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.lang.IllegalArgumentException;
import java.util.NoSuchElementException;

/**
 * The main cache simulator. Uses a set-associative cache.
 * NOTE: Does not actually store any data.
 *
 * @author Kevin Filanowski
 * @author Caleb Tupone
 * @version April 29, 2019
 * CS 350 Project 4
 */
public class Simulator {
    /** The size of the bit address. */
    private static final int ADDRESS_SIZE = 32;
    /** The max size of the number of sets allowed. */
    private static final int MAX_SET_NUM = 8192;
    /** The max size of the number of lines in a set. */
    private static final int MAX_SET_SIZE = 8;
    /** The min size of the number of bytes in a line. */
    private static final int MIN_LINE_SIZE = 4;
    /** The Scanner taking in input. */
    private Scanner input;
    /** The total number of hits by the data searching into the cache. */
    private int totalHits;
    /** The total number of misses by the data searching into the cache. */
    private int totalMisses;
    /** The total number of sets. */
    private int numSets;
    /** The number of cacheLines per set. */
    private int setSize;
    /** The size, in bytes, of a cacheLine. */
    private int lineSize;
    /** The cache itself. */
    CacheLine[][] cache;

    /**
     * Constructs a new simular, and creates a Scanner taking in standard input.
     *
     * @throws IllegalArgumentException - Thrown if the file contains invalid
     *                                  - arguments for the setSize, numSets,
     *                                  - and lineSize.
     */
    public Simulator() throws IllegalArgumentException {
        input = new Scanner(System.in);
    }

    /**
     * Begins the simulation.
     * This method starts the whole process.
     * It builds the cache, prints the configuration, prints the results,
     * and prints the statistics of the results.
     * @throws IllegalArgumentException - Thrown if the file contains invalid
     *                                   - arguments for the setSize, numSets,
     *                                   - and lineSize.
     */
    public void go() throws IllegalArgumentException, NoSuchElementException {
        buildCache();
        printCacheConfiguration();
        printResultTableHeader();
        parseRow();
        printStatistics();
    }

    /**
     * This method creates the structure of a cache based on the redirected
     * input.
     * @throws NoSuchElementException - Throws if the file does
     */
    private void buildCache() throws NoSuchElementException {
        if (!input.hasNextLine())
             throw new NoSuchElementException("Error in file");
        // Must not exceed 8192.
        numSets = Integer.parseInt(input.nextLine().replaceAll("\\D*", ""));
        double result = Math.log(numSets)/Math.log(2);
        if (numSets > MAX_SET_NUM){
            throw new IllegalArgumentException("Number of sets is greater than "
            + "8192, please use fewer");
        } else if (result != Math.round(result)){
            throw new IllegalArgumentException("Number of sets must be "
            + "a power of 2");
        }

        if (!input.hasNextLine())
             throw new NoSuchElementException("Error in file");
        // Must not exceed 8.
        setSize = Integer.parseInt(input.nextLine().replaceAll("\\D*", ""));
        if (setSize > MAX_SET_SIZE){
            throw new IllegalArgumentException("Set size is greater than "
            + "8, please use fewer");
        }

        if (!input.hasNextLine())
             throw new NoSuchElementException("Error in file");
        // At least 4, and a power of 2.
        lineSize = Integer.parseInt(input.nextLine().replaceAll("\\D*", ""));
        result = Math.log(lineSize)/Math.log(2);
        if (lineSize < MIN_LINE_SIZE) {
            throw new IllegalArgumentException("Line size needs to at least 4");
        } else if (result != Math.round(result)){
            throw new IllegalArgumentException("Line size must be "
            + "a power of 2");
        }

        // The cache itself is a 2D array of CacheLine objects.
        cache = new CacheLine[numSets][setSize];
    }

    /**
     * Parses a single row in the file.
     */
    private void parseRow() {
        while (input.hasNextLine()) {
            // One line of data.
            String[] line = input.nextLine().split(":");
            processRow(line);
        }
    }

   /**
    * Processes a single row by determining it's offset, index, and tag, as
    * well as determining if it is in the cache.
    *
    * @param line - The line read by parseRow, a line in the file.
    * Example of a line: 58:R:4, where 58 is the address, R is the access type,
    * and 4 is the size of a cache line in byes.
    */
    private void processRow(String[] line) {
        String access = accessType(line[1]);

        String binary = String.format("%32s",
                        Integer.toBinaryString(Integer.parseInt(line[0], 16)))
                        .replace(' ', '0');

        // The length of the binary string.
        int size = binary.length();

        //offset for this data
        int offset = Integer.parseInt(
                            binary.substring(size - getOffsetBits(), size), 2);
        //index for this data
        int index = Integer.parseInt(binary.substring(
          size - getOffsetBits() - getIndexBits(), size - getOffsetBits()), 2);
        //tag for this data
        int tag = Integer.parseInt(
              binary.substring(0, size - getOffsetBits() - getIndexBits()), 2);

        // Determine if this data will result in a hit or miss.
        boolean result = searchCache(tag, index);

        int memrefs = totalMemrefs(result, access, index);

        printRow(access, line[0], tag, index, offset, result, memrefs);
    }

    /**
    * This method determines the total number of memory references a row would
    * require.
    *
    * @param result whether or not there was a hit or miss
    * @param access whether it was a read or write
    * @param index the set the instruction is referencing
    * @return if the row required 0, 1, or 2 memory references.
    */
    private int totalMemrefs(boolean result, String access, int index){
        int memrefs = 0;
        for(int i = 0; i < cache[index].length; i++){

            if(result ==  true) {
                if(access.equals("write"))
                    cache[index][i].setDirtyBit(true);
                //hit
                return memrefs;
            }
            else if(access.equals("write") && result == false){
                cache[index][i].setDirtyBit(true);
                //write miss and nothing to update
                if (totalHits + totalMisses <= 1 )
                    return memrefs = 1;
                //write miss
                return memrefs = 2;
            }else{
                if(cache[index][i].getDirtyBit() == true)
                    return memrefs = 2;
                //miss
                return memrefs = 1;
            }
        }
        return memrefs;
    }

    /**
    * Determines the access type based on the character.
    * 'R' is for read, and 'W' is for write. It will default to
    * write if it is not 'R'.
    *
    * @param line - The string to analyze.
    * @return - 'Read' if line is 'R', and 'write' if the line is 'W'.
    */
    private String accessType(String line){
        //If read or write
        if (line.equals("R")) {
            return "read";
        } else {
            return "write";
        }
    }

    /**
    * This method is what is used to search through the cache, and place tags
    * at the appropriate indicies. It also sets the valid bit for that line,
    * and correctly implements Least Recently Used replacement algorithm.
    *
    * @param tag the tag of the data
    * @param index the set the instruction is referencing
    */
    private boolean searchCache(int tag, int index) {
        int oldest = 0;

        // Searchs through a set in a cache
        for (int i = 0; i < cache[index].length; i++) {
            // Models a miss.
            if (cache[index][i] == null) {
                cache[index][i] = new CacheLine();
                cache[index][i].setValidBit(true);
                cache[index][i].setTag(tag);
                totalMisses++;
                return false;
                // Models a hit.
            } else if (cache[index][i].getTag() == tag) {
                totalHits++;
                cache[index][i].resetTimeStamp();
                return true;
            }
        }

        // Models a replacement.
        for (int i = 1; i < cache[index].length; i++) {
            if (cache[index][i].getTimeStamp() < cache[index][oldest]
                                                            .getTimeStamp()) {
                oldest = i;
            }
        }
        cache[index][oldest].resetTimeStamp();
        cache[index][oldest].setTag(tag);
        totalMisses++;
        return false;
    }

    /**
    * This method prints the configuration of the current cache.
    */
    private void printCacheConfiguration() {
        System.out.println("Cache Configuration\n");

        System.out.println("   " + numSets + " " + setSize +
        "-way set associative " +  "entries\n   of line size " + lineSize +
        " bytes\n\n" + "Results for " + "Each Reference\n");
    }

    /**
     * This method prints the attributes of the table
     */
    private void printResultTableHeader() {
        System.out.println("Access Address" +
                  "    Tag   Index Offset Result Memrefs\n"
                + "------ " + "-------- ------- ----- ------ ------ -------");
    }

    /**
     * This method prints each row of the table. The data includes the following
     * information about each row in the file:
     * Type of access, the address, the tag value, the index value, the offset
     * value, the boolean result, and the memory references values.
     *
     * @param access - The type of access, "read" or "write".
     * @param address - The address of the data.
     * @param tag - The tag of the data.
     * @param index - The index of the cacheline.
     * @param offset - The offset into the data.
     * @param result - The hit or miss result.
     * @param memref - The amount of memory references.
     */
    private void printRow(String access, String address, int tag, int index,
                                    int offset, boolean result, int memref) {
        String hitOrMiss;
        if (result == true) {
            hitOrMiss = "hit";
        } else {
            hitOrMiss = "miss";
        }
        System.out.printf("%6s %8s %7x %5d %6d %6s %7d %n",
                        access, address, tag, index, offset, hitOrMiss, memref);
    }

    /**
     * This method prints the statistics of the cache such as: Total hits,
     * total misses, total accesses, hit ratio, and miss ratio.
     */
    private void printStatistics() {
        int totalAccesses = totalHits + totalMisses;
        System.out.println("\nSimulation Summary Statistics\n" +
                           "-----------------------------");

        System.out.printf("%-17s: %-10d%n", "Total hits", totalHits);
        System.out.printf("%-17s: %-10d%n", "Total misses", totalMisses);
        System.out.printf("%-17s: %-10d%n", "Total accesses", totalAccesses);
        System.out.printf("%-17s: %-10f%n", "Hit ratio",
                                        (double) totalHits/totalAccesses);
        System.out.printf("%-17s: %-10f%n", "Miss ratio",
                                        (double) totalMisses/totalAccesses);
    }

    /**
     *  This method gives the number of bits needed to represent the line size.
     *
     * @return - The number of bits needed to represent the offset.
     */
    private int getOffsetBits() {
        return (int) (Math.log(lineSize) / Math.log(2));
    }

    /**
     * This method gives the number of bits needed to
     * represent the number of sets in the cache.
     *
     * @return - The number of bits representing the number of sets in a cache.
     */
    private int getIndexBits() {
        return (int) (Math.log(numSets) / Math.log(2));
    }

    /**
     * This method gives the remaining bits that are used to represent the tag.
     *
     * @return - The number of bits representing a tag.
     */
     private int getTagBits() {
         return ADDRESS_SIZE - (getOffsetBits() + getIndexBits());
     }
}
