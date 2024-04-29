package Tree;

import LPI.Printer;
import Tokens.TOKENS;
import Tokens.Token;

import java.util.*;

public class PrintfNode extends Node{

    private final ArrayList<Token> arguments;
    private final String formatString;

    public PrintfNode(ArrayList<Token> arguments, Token formatString) {
        setLine(formatString.getLine());
        this.arguments = arguments;
        this.formatString = formatString.getValue();
    }

    public ArrayList<Object> parseArgs(){
        ArrayList<Object> args = new ArrayList<>();
        for(Token elem: arguments){
            TOKENS token = elem.getToken();
            if(token == TOKENS.T_IDENT) {
                String arg = elem.getValue();
                Integer argInt = null;
                Float argFloat = null;
                String argString = null;
                Character argChar = null;
                for (int i = 0; i < 5 && (argInt == null && argFloat == null && argChar == null && argString == null); i++) {
                    switch (i) {
                        case 0 -> {
                            argInt = parent.getIdent(arg, Integer.MAX_VALUE);
                            if (argInt != null)
                                args.add(argInt);
                        }
                        case 1 -> {
                            argFloat = parent.getIdent(arg, Float.MAX_VALUE);
                            if (argFloat != null)
                                args.add(argFloat);
                        }
                        case 2 -> {
                            argString = parent.getIdent(arg, "");
                            if (argString != null)
                                args.add(argString);
                        }
                        case 3 -> {
                            argChar = parent.getIdent(arg, Character.MAX_VALUE);
                            if (argChar != null)
                                args.add(argChar);
                        }
                        case 4 -> {
                            //Переменная не найдена
                            Node.addError(line,": printf(): There is no variable with the \"" + arg + "\" identifier");
                            return null;
                        }
                    }
                }
            }
            else {
                for(int i = 0; i < 5; i++){
                    switch (i){
                        case 0 -> { if(token == TOKENS.T_INTCON) args.add(Integer.valueOf(elem.getValue())); }
                        case 1 -> { if(token == TOKENS.T_FLOATCON) args.add(Float.valueOf(elem.getValue())); }
                        case 2 -> { if(token == TOKENS.T_CHARCON) args.add(elem.getValue().charAt(0)); }
                        case 3 -> { if(token == TOKENS.T_STRINGCON) args.add(elem.getValue()); }
                    }
                }
            }
        }
        return args;
    }

    @Override
    public boolean run() {
        boolean interpretSuccess = true;

        if(arguments == null)
            return false;

        ArrayList<Object> args = parseArgs();
        if(args != null) {
            try {
                Printer.put(String.format(formatString, args.toArray()));
            } catch (MissingFormatArgumentException e) {
                //Нет идентификатора под модификатор
                addError("An empty modifier is present");
                interpretSuccess = false;
            } catch (IllegalFormatConversionException e) {
                //Неправильный тип идентификатора под модификатор
                addError("The type of the argument does not match the type of the modifier");
                interpretSuccess = false;
            } catch (UnknownFormatConversionException e) {
                //Неизвестный тип модификатора
                addError("Unknown modifier");
                interpretSuccess = false;
            } catch (UnknownFormatFlagsException e) {
                //Неизвестный тип флага
                addError("Unknown flag");
                interpretSuccess = false;
            } catch (IllegalFormatWidthException e) {
                //Неправильная длина формата
                addError("Illegal format width");
                interpretSuccess = false;
            } catch (IllegalFormatPrecisionException e) {
                //Неправильная точность формата
                addError("Illegal format precision");
                interpretSuccess = false;
            } catch (IllegalFormatException e) {
                addError("Illegal format index");
                interpretSuccess = false;
            }
        }

        return interpretSuccess;
    }

    private void addError(String information){
        Node.addError(line,": printf(): " + information);
    }

    @Override
    protected void print(int level) {
        for(int i = 0; i < level; i++)
            System.out.print("\t");
        System.out.println("Printf");
    }
}
