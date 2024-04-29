package Tree;

import Tree.ExprTypes.LogicExpr;

public class ConditionNode extends Node{
    private final LogicExpr logicExpr;
    private final BlockNode blockTrue;
    private final BlockNode blockElse;

    public ConditionNode(LogicExpr logicExpr, BlockNode blockTrue, BlockNode blockElse) {
        this.logicExpr = logicExpr;
        this.blockTrue = blockTrue;
        this.blockElse = blockElse;
    }
    public ConditionNode(LogicExpr logicExpr, BlockNode blockTrue) {
        this.logicExpr = logicExpr;
        this.blockTrue = blockTrue;
        this.blockElse = null;
    }
    @Override
    public void setParent(BlockNode parent) {
        this.parent = parent;
        logicExpr.setParent(parent);
        blockTrue.setParent(this.parent);
        if(blockElse != null)
            blockElse.setParent(this.parent);
    }

    @Override
    public boolean run() {
        if(logicExpr == null)
            return false;

        if(logicExpr.run()){
            if(logicExpr.getValue())
                blockTrue.run();
            else if(blockElse != null)
                blockElse.run();
        }
        else //Логическое выражение не может быть вычислено
            return false;

        return true;
    }

    @Override
    protected void print(int level) {
        for(int i = 0; i < level; i++)
            System.out.print("\t");
        System.out.println("Condition");
        logicExpr.print(level + 1);
        blockTrue.print(level + 1);
        if(blockElse != null)
            blockElse.print(level + 1);
    }
}
