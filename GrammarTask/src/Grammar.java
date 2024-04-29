import java.io.*;

public class Grammar {
    private static final File outputFile = new File("output.txt");
    private FileReader fileReader;
    private static char EOF = (char) -1;
    private int positionChar;
    private char currChar;
    private boolean error;
    private StringBuilder resultString;

    public Grammar() throws IOException {
        outputFile.delete();
        outputFile.createNewFile();
        error = false;
        fileReader = null;
        positionChar = 0;
        resultString = new StringBuilder();
    }
    public Grammar(File file) throws IOException {
        outputFile.delete();
        outputFile.createNewFile();
        error = false;
        fileReader = new FileReader(file);
        positionChar = 0;
        resultString = new StringBuilder();
    }
    public void setFileReader(FileReader fileReader) {
        this.fileReader = fileReader;
    }
    public boolean syntaxAnalys(){
        boolean find = false;
        if(fileReader == null)
            return false;
        getChar();
        for(int i = 0; !find && !error; i++){
            switch (i){
                case 0 -> find = itString();
                case 1 -> find = itInt();
                case 2 -> find = itList();
                case 3 -> find = itDictionary();
                default -> error = true;
            }
        }
        printResult();
        return true;
    }

    private boolean itString(){
        if(error)
            return false;
        if(Character.isDigit(currChar)) {
            StringBuilder digit = new StringBuilder("0");
            if (currChar != '0')
                digit = getInt();
            else
                getChar();
            if (currChar != ':')
                error = true;
            else {
                getChar();
                resultString.append("'").append(getString(Integer.parseInt(digit.toString()))).append("'");
                return !error;
            }
        }
        return false;
    }
    private boolean itInt(){
        if(error)
            return false;
        if(currChar == 'i'){
            getChar();
            StringBuilder digit = new StringBuilder("0");
            if (currChar != '0')
                digit = getInt();
            else
                getChar();
            if(currChar != 'e')
                error = true;
            else {
                getChar();
                resultString.append(digit);
                return true;
            }
        }
        return false;
    }
    private boolean itList(){
        if(error)
            return false;
        if(currChar == 'l'){
            resultString.append("[ ");
            getChar();
            if(currChar == 'e'){
                resultString.append("]");
                return true;
            }
            else{
                itElements();
                if(error)
                    return false;
                if(currChar != 'e'){
                    error = true;
                    return false;
                }
                getChar();
                resultString.append("]");
                return true;
            }
        }
        return false;
    }
    private boolean itDictionary(){
        if(error)
            return false;
        if(currChar == 'd'){
            resultString.append("[ ");
            getChar();
            if(currChar == 'e'){
                resultString.append("]");
                return true;
            }
            else{
                itKeyAndValue();
                if(error)
                    return false;
                if(currChar != 'e'){
                    error = true;
                    return false;
                }
                getChar();
                resultString.append("]");
                return true;
            }
        }
        return false;
    }
    private void itElements(){
        boolean end = false;
        boolean find = false;
        while(!end) {
            for (int i = 0; !find && !end && !error; i++) {
                switch (i) {
                    case 0 -> find = itString();
                    case 1 -> find = itInt();
                    case 2 -> find = itList();
                    case 3 -> find = itDictionary();
                    default -> end = true;
                }
            }
            if(error)
                break;
            if(!end)
                resultString.append(", ");
            find = false;
        }
        if(!error)
            resultString.delete(resultString.length() - 2, resultString.length() - 1);
    }
    private void itKeyAndValue(){
        boolean end = false;
        boolean find = false;
        while(!end) {
            for (int i = 0; !error && !find && !end; i++) {
                switch (i) {
                    case 0 -> find = itString();
                    case 1 -> find = itInt();
                    case 2 -> find = itList();
                    case 3 -> find = itDictionary();
                    default -> end = true;
                }
            }
            if(error)
                break;
            find = false;
            if(!end)
                resultString.append(" => ");

            for (int i = 0; !error && !find && !end; i++) {
                switch (i) {
                    case 0 -> find = itString();
                    case 1 -> find = itInt();
                    case 2 -> find = itList();
                    case 3 -> find = itDictionary();
                    default -> error = true;
                }
            }
            if(!end)
                resultString.append(", ");
            find = false;
        }
        resultString.delete(resultString.length() - 2, resultString.length() - 1);
    }
    private StringBuilder getString(int size){
        StringBuilder line = new StringBuilder();
        for(int i = 0; i < size; i++){
            if(currChar == EOF) {
                error = true;
                break;
            }
            line.append(currChar);
            getChar();
        }
        return line;
    }
    private StringBuilder getInt(){
        StringBuilder number = new StringBuilder();
        if(!Character.isDigit(currChar)) {
            error = true;
            return number;
        }
        do {
            number.append(currChar);
            getChar();
        }while(Character.isDigit(currChar));
        return number;
    }
    private void getChar(){
        try {
            positionChar++;
            currChar = (char) fileReader.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void printResult() {
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(outputFile, true);
            if(error)
                outputStream.write(("Error at position " + positionChar) .getBytes());
            else
                outputStream.write(("OK\n" + resultString.toString()).getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
