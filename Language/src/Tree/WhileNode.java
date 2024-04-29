package Tree;

import Tree.ExprTypes.LogicExpr;

public class WhileNode extends Node{
    private final LogicExpr logicExpr;
    private final BlockNode block;
    private long countLoop = 0;
    private static final long MAX_COUNT = Long.MAX_VALUE;

    public WhileNode(int line, LogicExpr logicExpr, BlockNode block) {
        setLine(line);
        this.logicExpr = logicExpr;
        this.block = block;
    }

    @Override
    public void setParent(BlockNode parent) {
        this.parent = parent;
        block.setParent(parent);
        logicExpr.setParent(parent);
    }

    @Override
    public boolean run() {
        boolean interpretSuccess = true;

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
        System.out.println("While");
        logicExpr.print(level + 1);
        block.print(level + 1);
    }
}
