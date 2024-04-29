package LPI;

import Tokens.TOKENS;
import Tokens.TOKENS_TYPES;
import Tokens.Token;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer{
    private boolean ERROR = false;
    private static final String lexicalError = "Lexical error at "; //Строка для выведения лексических ошибок
    private static final String divString = "  ||  "; //Строка разделитель при печати токенов
    private StringBuilder undefineToken = new StringBuilder(); //Строка с неопределённым токеном
    private int linesCounter = 0; //Счётчик строк программы
    private int position = 1; //Счётчик символов на строке
    private final String pass; //Путь входного файла с текстом программы
    private final LinkedList<Token> tokens; //Очередь распознанных токенов
    public Lexer(String pass) throws IOException {
        this.pass = pass;
        tokens = new LinkedList<>();
    }
    public LinkedList<Token> getTokens() {
        return new LinkedList<>(tokens);
    }
    public boolean lexicalAnalysis() throws IOException {
        ArrayList<String> programsLines = next();
        linesCounter = 0;
        if(programsLines == null)
            return !ERROR;
        for(String stringBuffer : programsLines){
            linesCounter++;
            position = 1;
            while (stringBuffer.length() != 0)
                stringBuffer = getToken(stringBuffer);
        }
        tokens.add(new Token(TOKENS.T_EOF, "",-1,-1));
        if(!ERROR)
            Printer.lexicalSuccess();
        return !ERROR;
    }
    private ArrayList<String> next() throws IOException {
        ArrayList<String> strings = new ArrayList<>();
        strings.add("{");
        java.util.Scanner scanner = new java.util.Scanner(new File(pass));
        boolean comments = false;
        int commentsLine = 0;
        int commentsPos = 0;

        while (scanner.hasNextLine()){
            linesCounter++;
            String nextLine = scanner.nextLine();
            Matcher commALL = Pattern.compile("/\\*.*\\*/$").matcher(nextLine);
            Matcher commStart = Pattern.compile("/\\*.*$").matcher(nextLine);
            if(!comments){
                if(commALL.find())
                    nextLine = commStart.replaceFirst("");
                else if(commStart.find()) {
                    comments = true;
                    commentsLine = linesCounter;
                    commentsPos = position;
                    nextLine = commStart.replaceFirst("");
                }
                strings.add(nextLine);
            }
            else{
                Matcher commEnd = Pattern.compile("^.*\\*/").matcher(nextLine);
                if(commEnd.find()){
                    comments = false;
                    nextLine = commEnd.replaceFirst("");
                    strings.add(nextLine);
                }
                else strings.add("");
            }
        }
        if(comments){
            addError(commentsLine, commentsPos, "Unclosed comment");
            return null;
        }
        strings.add("}");
        return strings;
    }
    private String getToken(String stringBuffer){
        boolean tokenWasFind = false;
        for(TOKENS_TYPES token : TOKENS_TYPES.values()){
            Matcher matcher = token.getPattern().matcher(stringBuffer);
            if(matcher.find()){
                tokenWasFind = true;
                addUndefToken();
                if(token != TOKENS_TYPES.SPACES) {
                    if(token == TOKENS_TYPES.STRINGCON){
                        Pattern pString = Pattern.compile("\\\\\"");
                        String s = matcher.group(1);
                        Matcher mString = pString.matcher(s);
                        tokens.add(new Token(token.getToken(), mString.replaceAll("\""), linesCounter - 1, position));
                    }
                    else if (token == TOKENS_TYPES.CHARCON)
                        tokens.add(new Token(token.getToken(), matcher.group(1), linesCounter - 1, position));
                    else
                        tokens.add(new Token(token.getToken(), matcher.group(), linesCounter - 1, position));
                }
                position += matcher.end() - matcher.start();
                stringBuffer = stringBuffer.substring(matcher.end());
            }
            if(tokenWasFind)
                break;
        }
        if(!tokenWasFind){
            position++;
            undefineToken.append(stringBuffer.charAt(0));
            stringBuffer = stringBuffer.substring(1);
            if(stringBuffer.length() == 0)
                addUndefToken();
        }
        return stringBuffer;
    }

    private void addUndefToken(){
        if(undefineToken.length() != 0) {
            int posUndef = position - undefineToken.length();
            tokens.add(new Token(TOKENS.T_UNDEF, undefineToken.toString(), linesCounter - 1, posUndef));
            addError(linesCounter - 1, posUndef, "Undefined token \"" + undefineToken.toString() + "\"");
            undefineToken = new StringBuilder();
        }
    }
    private void addError(int line, int pos, String information){
        ERROR = true;
        Printer.putLog(lexicalError + "(" + line + ", " + pos + "): " + information);
    }
    public void printTokens() throws IOException {
        for(int i = 1; i < tokens.size() - 2; i++) {
            Token t = tokens.get(i);
            StringBuilder resultString = new StringBuilder();
            resultString.append(t.getToken().toString()).append(divString).append(t.getValue());
            resultString.append(divString).append(t.getLine());
            resultString.append(divString).append(t.getPosition());
            resultString.append("\n");
            System.out.print(resultString);
        }
    }
}