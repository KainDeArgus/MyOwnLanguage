package Tree;

import java.util.Iterator;

public class StartBlock extends Node{
    private final BlockNode block;
    public StartBlock(BlockNode block) {
        this.block = block;
        this.parent = null;
    }
    @Override
    public boolean run(){
        int main = 0;

        Iterator<Node> nodeIterator = block.getNodes();
        if(nodeIterator != null)
            while(nodeIterator.hasNext())
                if(nodeIterator.next().getClass() == MainNode.class)
                    main++;

        if(main > 1){
            //Множественная функция main
            Node.addError(-1,": StartProgram: Multiple declaration of the main function");
            return false;
        }
        if(main == 0){
            //Нет объявления функции main
            Node.addError(-1,": StartProgram: The main function declaration is missing");
            return false;
        }
        return block.run();
    }

    public void print(){
        print(0);
    }

    @Override
    protected void print(int level) {
        System.out.println("Program");
        block.print(level);
    }
}
