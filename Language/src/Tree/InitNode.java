package Tree;

import Tokens.TOKENS;
import Tokens.Token;

public class InitNode extends Node{
    private final TOKENS typeIdent;
    private final AssignNode assignNode;

    public InitNode(Token typeIdent, AssignNode assignNode) {
        setLine(typeIdent.getLine());
        this.typeIdent = typeIdent.getToken();
        this.assignNode = assignNode;
    }
    @Override
    public void setParent(BlockNode parent){
        this.parent = parent;
        assignNode.setParent(this.parent);
    }

    @Override
    public boolean run() {
        boolean interpretSuccess = false;

        if(typeIdent != assignNode.getTypeValue() && !(assignNode.getTypeValue() == TOKENS.T_FLOAT && typeIdent == TOKENS.T_INT)){
            //Тип инициализации не соответствует типу присвоения
            Node.addError(line,"The type of the initialized variable does not match the type of the assigned value");
        }
        else interpretSuccess = assignNode.init(typeIdent);

        return interpretSuccess;
    }

    @Override
    protected void print(int level) {
        for(int i = 0; i < level; i++)
            System.out.print("\t");
        System.out.println("Init");
        assignNode.print(level + 1);
    }
}
