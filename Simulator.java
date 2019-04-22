package CacheSimulator;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
     * This method parses a file
     * @param args the file to be parsed
     * @throws FileNotFoundException
     */
    public void parseInput(String args) throws FileNotFoundException {
        File file = new File(args);
        Scanner input = new Scanner(file);
        Pattern p = Pattern.compile("\\d+");

        while (input.hasNextLine()) {
            Matcher m = p.matcher(input.nextLine());
            System.out.print("\n");
            while(m.find()){
                System.out.print(m.group());
            }


        }

    }

}
