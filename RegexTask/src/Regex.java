import java.io.File;
import java.io.IOException;

public class Regex {
    public static void main(String[] args) {
        Finder finder = new Finder();
        File file = new File("input.txt");

        try {
            finder.find(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}