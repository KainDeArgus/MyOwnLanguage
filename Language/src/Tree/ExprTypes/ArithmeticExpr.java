package Tree.ExprTypes;

import Tokens.TOKENS;
import Tokens.Token;
import Tree.Node;
import com.udojava.evalex.Expression;

import java.util.ArrayList;

public class ArithmeticExpr extends Node implements ExpressionInterface<Float> {
    private Float value;
    private final ArrayList<Token> expression;

    public ArithmeticExpr(ArrayList<Token> expression) {
        setLine(expression.get(0).getLine());
        this.expression = expression;
    }

    @Override
    public Float getValue() {
        return value;
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
            else
                expr.append(elem.getValue());
        }
        try{ value = new Expression(expr.toString()).eval().floatValue(); }
        catch (ArithmeticException e){
            //Ошибка в арифметическом выражении
            Node.addError(line,"The arithmetic expression cannot be calculated");
            return false;
        }
        return true;
    }

    @Override
    public void print(int level) {
        for(int i = 0; i < level; i++)
            System.out.print("\t");
        System.out.println("ArithmeticExpr");
    }
}
