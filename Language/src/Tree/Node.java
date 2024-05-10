package Tree;

import LPI.Printer;

public abstract class Node {
    protected BlockNode parent;
    protected int line;
    protected static final String interpretationError = "Interpretation error at "; //Строка для вывода ошибок интерпретации

    protected void setLine(int line){
        this.line = line;
    }

    protected static void addError(int line, String information){
        Printer.putLog(interpretationError + line + " line: " + information);
    }
    protected abstract boolean run();
    protected abstract void print(int level);
    protected void setParent(BlockNode parent){
        this.parent = parent;
    }
}
