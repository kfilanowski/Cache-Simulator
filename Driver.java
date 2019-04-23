//package CacheSimulator;

import java.io.FileNotFoundException;

/**
 * // TODO
 *
 * @author Kevin Filanowski
 * @author Caleb Tupone
 * @version April 22, 2019
 */
public class Driver {

    /**
     * // TODO
     *
     * @param args -
     */
    public static void main(String[] args) {
        String[] test = {"trace.dt"};
        Simulator sim = new Simulator();
        try {
            sim.go(test);
        } catch (FileNotFoundException ex) {
            System.out.println("File not found");
        } catch (NumberFormatException ex) {
            System.out.println("Error in file.");
        }
    }
}
