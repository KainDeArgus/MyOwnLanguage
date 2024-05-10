package Tree.ExprTypes;

import Tokens.TOKENS;
import Tokens.Token;
import Tree.Node;
import com.udojava.evalex.Expression;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Objects;


public class LogicExpr extends Node implements ExpressionInterface<Boolean> {
        private boolean value;
    private final ArrayList<Token> expression;

    public LogicExpr(ArrayList<Token> expression) {
        setLine(expression.get(0).getLine());
        this.expression = expression;
    }

    @Override
    public boolean run() {
        StringBuilder expr = new StringBuilder();
        for(Token elem : expression){
            TOKENS token = elem.getToken();
            if(token == TOKENS.T_IDENT){
                String nameIdent = elem.getValue();
                Float number = parent.getIdent(nameIdent, Float.MAX_VALUE);
                if(number == null) {
                    Integer numberI = parent.getIdent(nameIdent, Integer.MAX_VALUE);
                    if (numberI == null) {
                        //Несуществующий идентификатор
                        Node.addError(line,"There is no variable with the \"" + nameIdent + "\" identifier of the numeric type");
                        return false;
                    }
                    number = numberI.floatValue();
                }
                expr.append(number);
            }
            else if(token == TOKENS.T_NOT)
                expr.append("not");
            else
                expr.append(elem.getValue());
        }
        value = Objects.equals(new Expression(expr.toString()).eval(), BigDecimal.ONE);
        return true;
    }

    @Override
    public Boolean getValue() {
        return value;
    }

    @Override
    public void print(int level) {
        for(int i = 0; i < level; i++)
            System.out.print("\t");
        System.out.println("LogicExpr");
    }
}
