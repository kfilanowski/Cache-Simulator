import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * // TODO
 *
 * @author Kevin Filanowski
 * @author Caleb Tupone
 * @version April 22, 2019
 */
public class Simulator {

    /**
     *
     *
     *
     *
     */
    public void go(String[] args) throws FileNotFoundException {
        parseInput(args[0]);
    }

    /**
     *
     *
     *
     *
     */
    public void parseInput(String args) throws FileNotFoundException {
        File file = new File(args);
        Scanner input = new Scanner(file);

        while (input.hasNextLine()) {
            System.out.println(input.hasNextLine());
        }

    }

}