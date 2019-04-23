//package CacheSimulator;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Arrays;


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
    public void parseInput(String args) throws FileNotFoundException, NumberFormatException {
        // index 0 holds the number of sets.
        // index 1 holds the number of elements in each set. 
        // index 2 holds the size of a cache line, in bytes. 
        int[] data = new int[3];
        // Read file
        File file = new File(args);
        Scanner input = new Scanner(file);

        // OPTION 1:
        // Regex the information.
        // Pattern p = Pattern.compile("\\d+");
        // Matcher m;

        // for (int i = 0; i < 3; i++) {
        //     if (input.hasNextLine()) {
        //         m = p.matcher(input.nextLine());
        //         if (m.find()) {
        //             data[i] = Integer.parseInt(m.group());
        //         }
        //     }
        // }

        

        // OPTION 2:
        for (int i = 0; i < 3; i++) {
            if (input.hasNextLine()) {
                data[i] = Integer.parseInt(input.nextLine().replaceAll("\\D*", ""));
            }
        }

        // prints the first 3 line's integer data.
        System.out.println(Arrays.toString(data));


        // while (input.hasNextLine()) {
        //     m = p.matcher(input.nextLine());
        //     System.out.print("\n");
        //     while(m.find()){
        //         System.out.print(m.group());
        //     }
        // }
    }
}
