package Tree;

import LPI.Scanner;
import Tokens.TOKENS;
import Tokens.TOKENS_TYPES;
import Tokens.Token;
import Tree.ExprTypes.ArithmeticExpr;

import java.util.*;

public class ScanfNode extends Node{
    private final ArrayList<Token> arguments;
    private final String formatString;

    public ScanfNode(ArrayList<Token> arguments, Token formatString) {
        setLine(formatString.getLine());
        this.arguments = arguments;
        this.formatString = formatString.getValue();
    }

    @Override
    public boolean run() {
        boolean interpretSuccess = true;

        if(arguments == null || arguments.size() == 0)
            return false;

        String [] formats = formatString.split(" ?%");
        if(formats.length <= 1) {
            addError("There are not modifiers");
            interpretSuccess = false;
        }

        for(int i = 1; i <= arguments.size() && interpretSuccess; i++){
            String mod = formats[i];
            StringBuilder line = new StringBuilder(Scanner.nextLine());

            ArrayList<Token> token = new ArrayList<>();
            Token ident = new Token(TOKENS.T_IDENT, arguments.get(i - 1).getValue(), this.line, -1);

            switch (mod){
                case "c" -> {
                    Token letter = new Token(TOKENS.T_CHARCON, String.valueOf(line.charAt(0)), this.line, -1);
                    AssignNode assignNode = new AssignNode(ident, letter);
                    assignNode.setParent(parent);
                    interpretSuccess = assignNode.run();
                }
                case "s" -> {
                    Token letter = new Token(TOKENS.T_STRINGCON, line.toString(), this.line, -1);
                    AssignNode assignNode = new AssignNode(ident, letter);
                    assignNode.setParent(parent);
                    interpretSuccess = assignNode.run();
                }
                case "d" -> {
                    if(TOKENS_TYPES.INTCON.getPattern().matcher(line).matches()) {
                        token.add(new Token(TOKENS.T_INTCON, line.toString(), this.line, -1));
                        ArithmeticExpr expr = new ArithmeticExpr(token);
                        AssignNode assignNode = new AssignNode(ident, expr);
                        assignNode.setParent(parent);
                        interpretSuccess = assignNode.run();
                    }
                    else {
                        addError("Attempt to record illegal data");
                        interpretSuccess = false;
                    }
                }
                case "f" -> {
                    if(TOKENS_TYPES.FLOATCON.getPattern().matcher(line).matches()) {
                        token.add(new Token(TOKENS.T_FLOATCON, line.toString(), this.line, -1));
                        ArithmeticExpr expr = new ArithmeticExpr(token);
                        AssignNode assignNode = new AssignNode(ident, expr);
                        assignNode.setParent(parent);
                        interpretSuccess = assignNode.run();
                    }
                    else {
                        addError("Attempt to record illegal data");
                        interpretSuccess = false;
                    }
                }
                default -> {
                    addError("Unknown modifier");
                    interpretSuccess = false;
                }
            }
        }

        return interpretSuccess;
    }

    private void addError(String information){
        Node.addError(line,": scanf(): " + information);
    }

    @Override
    protected void print(int level) {
        for(int i = 0; i < level; i++)
            System.out.print("\t");
        System.out.println("Scanf");
    }
}
