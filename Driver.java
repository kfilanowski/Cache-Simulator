import java.io.FileNotFoundException;

/**
 * The driver for a cache simulator.
 *
 * @author Kevin Filanowski
 * @author Caleb Tupone
 * @version April 28, 2019
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
        } catch (FileNotFoundException ex) {
            System.out.println("File not found");
        } catch (NumberFormatException ex) {
            System.out.println("Error in file.");
        }
    }
}
