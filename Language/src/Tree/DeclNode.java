package Tree;

import Tokens.TOKENS;
import Tokens.Token;

public class DeclNode extends Node{
    private final TOKENS typeIdent;
    private final String nameIdent;
    public DeclNode(Token typeIdent, String nameIdent) {
        this.typeIdent = typeIdent.getToken();
        setLine(typeIdent.getLine());
        this.nameIdent = nameIdent;
    }

    @Override
    public boolean run() {
        if(parent.findIdent(nameIdent) != null){
            //Переменная с таким именем уже существует
            Node.addError(line,"The variable with the \"" + nameIdent + "\" identifier has already been declared");
            return false;
        }
        else{
             switch (typeIdent){
                 case T_INT -> parent.makeIdent(nameIdent, 0);
                 case T_FLOAT -> parent.makeIdent(nameIdent, 0F);
                 case T_CHAR -> parent.makeIdent(nameIdent, Character.MAX_VALUE);
                 case T_STRING -> parent.makeIdent(nameIdent, "");
             }
            return true;
        }
    }

    @Override
    protected void print(int level) {
        for(int i = 0; i < level; i++)
            System.out.print("\t");
        System.out.println("Decl");
    }
}
