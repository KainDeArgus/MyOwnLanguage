import LPI.*;

import java.io.IOException;

public class DSL {
    public static void main(String[] args) {
        String passInput = "LANGUAGE_PROGRAM.txt";
        String passOutput = "LANGUAGE_CONSOLE.txt";
        String passError = "LANGUAGE_CONSOLE.txt";
        try {
            Lexer lexer = new Lexer(passInput);
            lexer.lexicalAnalysis();
            //lexer.printTokens();

            Parser parser = new Parser(lexer.getTokens());

            if(parser.syntaxAnalysis()) {
                Interpreter interpreter = new Interpreter(parser.getStartBlock());
                //interpreter.printAST();
                interpreter.interpret();
            }

            Printer.print(passError, passOutput);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}