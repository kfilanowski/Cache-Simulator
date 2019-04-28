
import java.util.Arrays;
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;


/**
 * The main cache simulator. Uses a set-associative cache.
 * NOTE: Does not actually store any data.
 *
 * @author Kevin Filanowski
 * @author Caleb Tupone
 * @version April 28, 2019
 */
public class Simulator {
    /** The size of the bit address. */
    private static final int ADDRESS_SIZE = 32;
    /** The Scanner taking in input. */
    private Scanner input;
    /** The total number of hits by the data searching into the cache. */
    private int totalHits;
    /** The total number of misses by the data searching into the cache. */
    private int totalMisses;
    /** The total number of memory accesses. */
    private int totalAccesses;
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
     * @throws FileNotFoundException - Thrown if the file the user entered
     *                               - is not found or valid.
     */
    public Simulator() throws FileNotFoundException {
        //input = new Scanner(System.in);
        input = new Scanner(new File("fin1.dt"));
    }

    /**
     * Begins the simulation.
     * This method starts the whole process.
     * It builds the cache, prints the configuration, prints the results,
     * and prints the statistics of the results.
     */
    public void go() {
        buildCache();
        printCacheConfiguration();
        printResultTableHeader();
        parseRow();
        printStatistics();
    }

    /**
     * This method creates the structure of a cache based on the redirected
     * input.
     */
    private void buildCache() {
        // Must not exceed 8192.
        numSets = Integer.parseInt(input.nextLine().replaceAll("\\D*", ""));

        // Must not exceed 8.
        setSize = Integer.parseInt(input.nextLine().replaceAll("\\D*", ""));

        // At least 4, and a power of 2.
        lineSize = Integer.parseInt(input.nextLine().replaceAll("\\D*", ""));

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
            // TODO: checkInputForErrors(line);
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
        String access = accessType(line);

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
    * @param index - //TODO:
    * @return if the row required 0, 1, or 2 memory references.
    */
    private int totalMemrefs(boolean result, String access, int index){
        int memrefs = 0;
        for(int i = 0; i < cache[index].length; i++){

            if(result ==  true) {
                //hit
                return memrefs;
            }
            else if(access.equals("write") && result == false){
                cache[index][i].setDirtyBit(true);
                //write miss and nothing to update
                if (totalAccesses <= 1 )
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
    *
    */
    private String accessType(String[] line){
        //If read or write
        if (line[1].equals("R")) {
            return "read";
        } else {
            return "write";
        }
    }

    /**
    *
    *
    */
    private boolean searchCache(int tag, int index) {
        int oldest = 0;

        for (int i = 0; i < cache[index].length; i++) {
            if (cache[index][i] == null) {
                cache[index][i] = new CacheLine();
                cache[index][i].setValidBit(true);
                cache[index][i].setTag(tag);
                totalAccesses++;
                totalMisses++;
                return false;
            } else if (cache[index][i].getTag() == tag) {
                totalAccesses++;
                totalHits++;
                return true;
            }
        }

        for (int i = 1; i < cache[index].length; i++) {
            if (cache[index][i].getTimeStamp() < cache[index][oldest].getTimeStamp()) {
                oldest = i;
            }
        }

        cache[index][oldest].resetTimeStamp();
        cache[index][oldest].setTag(tag);
        totalAccesses++;
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
     * Type of access, The address, The value for the tag,
     */
    private void printRow(String access, String address, int tag, int index, int offset, boolean result, int memref) {
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
