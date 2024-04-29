package LPI;

import Tree.StartBlock;

public class Interpreter {
    private final StartBlock startBlock;

    public Interpreter(StartBlock startBlock) {
        this.startBlock = startBlock;
    }

    public boolean interpret(){
        if(startBlock == null) {
            Printer.putLog("Interpretation error at " + 0 + "line: Undefined start block");
            return false;
        }

        boolean interpretSuccess = startBlock.run();
        if(interpretSuccess)
            Printer.interpretSuccess();
        return interpretSuccess;
    }

    public void printAST(){
        startBlock.print();
    }
}
