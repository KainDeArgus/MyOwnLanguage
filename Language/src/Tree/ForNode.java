package Tree;

import Tree.ExprTypes.LogicExpr;

public class ForNode extends Node{
    private final Node declCounter;
    private final LogicExpr logicExpr;
    private final Node assgn;
    private final BlockNode block;
    private long countLoop = 0;
    private static final long MAX_COUNT = Long.MAX_VALUE;

    public ForNode(int line, Node declCounter, LogicExpr logicExpr, Node assgn, BlockNode block) {
        setLine(line);
        this.declCounter = declCounter;
        this.logicExpr = logicExpr;
        this.assgn = assgn;
        this.block = block;
    }

    @Override
    public void setParent(BlockNode parent) {
        this.parent = parent;
        logicExpr.setParent(block);
        block.setParent(parent);
        declCounter.setParent(block);
        assgn.setParent(block);
    }

    @Override
    public boolean run() {
        boolean interpretSuccess = declCounter.run();

        if(logicExpr == null)
            return false;

        while(interpretSuccess) {
            if (logicExpr.run()) {
                if(logicExpr.getValue()) {
                    countLoop++;
                    if(countLoop == MAX_COUNT){
                        //Бесконечный цикл
                        Node.addError(line,"Infinite loop");
                        interpretSuccess = false;
                        break;
                    }
                    interpretSuccess = block.run();
                    if(interpretSuccess)
                        interpretSuccess = assgn.run();
                }
                else break;
            } else {
                //Логическое выражение не вычисляется
                interpretSuccess = false;
                break;
            }
        }
        return interpretSuccess;
    }

    @Override
    protected void print(int level) {
        for(int i = 0; i < level; i++)
            System.out.print("\t");
        System.out.println("For");
        declCounter.print(level + 1);
        logicExpr.print(level + 1);
        assgn.print(level + 1);
        block.print(level + 1);
    }
}
