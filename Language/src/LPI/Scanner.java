package LPI;

import java.io.File;
import java.io.FileNotFoundException;

public class Scanner {
    private static final String inputPath = "LANGUAGE_INPUT.txt";
    private static final java.util.Scanner scanner;

    static {
        try {
            scanner = new java.util.Scanner(new File(inputPath));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static String nextLine(){
        return scanner.nextLine();
    }
}
