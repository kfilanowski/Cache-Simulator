package CacheSimulator;

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

        Simulator sim = new Simulator();
        try {

            sim.go(args);
        } catch (FileNotFoundException ex) {
            System.out.println("FUCK");
        }
    }
}
