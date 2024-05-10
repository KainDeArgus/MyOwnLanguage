package LPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Printer {
    private static final ArrayList<String> program = new ArrayList<>();
    private static final ArrayList<String> logs = new ArrayList<>();

    public static void print(String ErrorPass, String OutputPass) throws IOException {
        new FileOutputStream(ErrorPass).close();
        new File(ErrorPass).createNewFile();

        new FileOutputStream(OutputPass).close();
        new File(OutputPass).createNewFile();

        FileOutputStream errorStream = new FileOutputStream(ErrorPass, true);
        FileOutputStream outputStream = new FileOutputStream(OutputPass, true);

        for(int i = 0; i < logs.size(); i++)
            errorStream.write((logs.get(i) + "\n").getBytes());
        for(int i = 0; i < program.size(); i++)
            outputStream.write((program.get(i) + "\n").getBytes());
    }

    public static void put(String errorString){
        program.add(errorString);
    }

    public static void putLog(String errorString){
        logs.add(errorString);
    }

    protected static void lexicalSuccess(){
        logs.add("The lexical analysis was successful");
    }

    protected static void syntaxSuccess(){
        logs.add("The parsing was successful");
    }

    protected static void interpretSuccess(){
        logs.add("The interpreting was successful");
    }
}
