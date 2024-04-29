package Tree;

import Tree.ExprTypes.ArithmeticExpr;
import Tokens.TOKENS;
import Tokens.Token;


public class AssignNode extends Node{
    private final String nameIdent;
    private final ArithmeticExpr valueNumber;
    private final Token valueLetter;
    public AssignNode(Token ident, ArithmeticExpr valueNumber) {
        setLine(ident.getLine());
        this.nameIdent = ident.getValue();
        this.valueNumber = valueNumber;
        valueLetter = null;
    }
    public AssignNode(Token ident, Token valueLetter) {
        setLine(ident.getLine());
        this.nameIdent = ident.getValue();
        this.valueLetter = valueLetter;
        valueNumber = null;
    }

    @Override
    public void setParent(BlockNode parent) {
        this.parent = parent;
        if(valueNumber != null)
            valueNumber.setParent(parent);
    }

    @Override
    public boolean run() {
        boolean interpretSuccess = false;
        TOKENS typeIdent = parent.findIdent(nameIdent);
        if(typeIdent != null) {
            if (getTypeValue() != TOKENS.T_FLOAT) {
                if(valueLetter.getToken() == TOKENS.T_CHARCON){
                    if(parent.setIdent(nameIdent, valueLetter.getValue().charAt(0)))
                        interpretSuccess = true;
                    else
                        Node.addError(line,"Attempt to record illegal data in \"" + nameIdent + "\" identifier");
                }
                else if(parent.setIdent(nameIdent, valueLetter.getValue()))
                    interpretSuccess = true;
                else
                    Node.addError(line,"Attempt to record illegal data in \"" + nameIdent + "\" identifier");
            } else if (valueNumber.run()) {
                switch (typeIdent){
                    case T_INT -> {
                        if(parent.setIdent(nameIdent, valueNumber.getValue().intValue()))
                            interpretSuccess = true;
                        else
                            Node.addError(line,"Attempt to record illegal data in \"" + nameIdent + "\" identifier");
                    }
                    case T_FLOAT -> {
                        if(parent.setIdent(nameIdent, valueNumber.getValue()))
                            interpretSuccess = true;
                        Node.addError(line,"Attempt to record illegal data in \"" + nameIdent + "\" identifier");
                    }
                }
            }
        }
        else {
            //Переменной не существует
            Node.addError(line,"There is no variable with the \"" + nameIdent + "\" identifier");
        }
        return interpretSuccess;
    }


    public TOKENS getTypeValue(){
        if(valueLetter != null) {
            if (valueLetter.getToken() == TOKENS.T_STRINGCON)
                return TOKENS.T_STRING;
            else
                return TOKENS.T_CHAR;
        }
        else return TOKENS.T_FLOAT;
    }

    public boolean init(TOKENS typeIdent){
        boolean interpretSuccess = false;
        if(parent.findIdent(nameIdent) == null) {
            if (valueLetter != null) {
                if(valueLetter.getToken() == TOKENS.T_CHARCON)
                    parent.makeIdent(nameIdent, valueLetter.getValue().charAt(0));
                else
                    parent.makeIdent(nameIdent, valueLetter.getValue());
                interpretSuccess = true;
            } else if (valueNumber.run()) {
                switch (typeIdent){
                    case T_INT -> parent.makeIdent(nameIdent, valueNumber.getValue().intValue());
                    case T_FLOAT -> parent.makeIdent(nameIdent, valueNumber.getValue());
                }
                interpretSuccess = true;
            }
        }
        else {
            //Переменная с таким именем уже существует
            Node.addError(line,"The variable with the \"" + nameIdent + "\" identifier has already been declared");
        }
        return interpretSuccess;
    }

    @Override
    protected void print(int level) {
        for(int i = 0; i < level; i++)
            System.out.print("\t");
        System.out.println("Assign");
        if(valueLetter == null)
            valueNumber.print(level + 1);
        else {
            for(int i = 0; i < level + 1; i++)
                System.out.print("\t");
            System.out.println("Letter");
        }
    }
}
