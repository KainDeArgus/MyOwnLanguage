import java.io.File;
import java.io.IOException;

public class GrammarExecuter {
    public static void main(String[] args) {
        File file = new File("inputGrammar.txt");

        try {
            Grammar grammar = new Grammar(file);
            if(!grammar.syntaxAnalys())
                System.out.println("ERROR");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}