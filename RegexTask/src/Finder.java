import java.io.*;

public class Finder {
    private enum STATE{
        Q0, Q1, Q2, ERROR
    }
    private char curr_char;
    private STATE state;
    private String word;

    Finder() {
        state = STATE.Q0;
        word = "";
        curr_char = (char) 0;
    }
    public void find(File file) throws IOException {
        if(!file.exists())
            printResult("file is not existed");
        else {
            FileReader fileReader = new FileReader(file);

            curr_char = (char) fileReader.read();
            nextState();
            action();

            while (state != STATE.ERROR){
                curr_char = (char) fileReader.read();
                if(curr_char == (char) -1)
                    break;
                nextState();
                action();
            }

            if(state == STATE.Q2)
                printResult("yes");
            else
                printResult("no");
        }
    }
    private void nextState(){
        switch (state) {
            case Q0:
                if(curr_char == 'c' || curr_char == 'd') {
                    state = STATE.Q1;
                    break;
                }
            case Q1:
                if(curr_char == 'e' || curr_char == 'f')
                    break;
                if(curr_char == 'g' || curr_char == 'h' || curr_char == 'k'){
                    state = STATE.Q2;
                    break;
                }
            default: state = STATE.ERROR;
        }
    }
    private void action() throws IOException {
        if(state == STATE.ERROR)
            printResult("no");
        else
            word += curr_char;
    }
    private void printResult(String information) throws IOException {
        File file = new File("output.txt");

        file.delete();
        file.createNewFile();

        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(information.getBytes());
    }
}
