package CacheSimulator;

import java.io.FileNotFoundException;
import java.util.NoSuchElementException;

/**
 * The driver for a cache simulator. This is the class to be run.
 *
 * @author Kevin Filanowski
 * @author Caleb Tupone
 * @version April 29, 2019
 * CS 350 Project 4
 */
public class Driver {

    /**
     * Created a simulator object and runs it. Input is accepted through
     * re-directing standard input from a file.
     *
     * @param args - Not Supported.
     */
    public static void main(String[] args) {
        try {
            Simulator sim = new Simulator();
            sim.go();
        } catch (NumberFormatException ex) {
            System.out.println("Error in File.");
        } catch (NoSuchElementException ex) {
            System.out.println(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
