package LPI;

import Tokens.TOKENS;
import Tokens.Token;
import Tree.*;
import Tree.ExprTypes.ArithmeticExpr;
import Tree.ExprTypes.LogicExpr;

import java.util.*;

public class Parser {
    private boolean ERROR = false;
    private static final String syntaxError = "Syntax error at "; //Строка для вывода синтаксических ошибок
    private static final Set<TOKENS> logicSign = Set.of(
            TOKENS.T_NOT,
            TOKENS.T_EQUAL,
            TOKENS.T_NEQUAL,
            TOKENS.T_EOGREAT,
            TOKENS.T_GREAT,
            TOKENS.T_EOLESS,
            TOKENS.T_LESS,
            TOKENS.T_AND,
            TOKENS.T_OR
    );
    private static final Set<TOKENS> letter = Set.of(
            TOKENS.T_STRINGCON,
            TOKENS.T_CHARCON
    );
    private static final Set<TOKENS> number = Set.of(
            TOKENS.T_INTCON,
            TOKENS.T_FLOATCON
    );
    private static final Set<TOKENS> sign = Set.of(
            TOKENS.T_ADD,
            TOKENS.T_SUB,
            TOKENS.T_MUL,
            TOKENS.T_DIV,
            TOKENS.T_REM
    );
    private static final Set<TOKENS> type = Set.of(
            TOKENS.T_CHAR,
            TOKENS.T_INT,
            TOKENS.T_FLOAT,
            TOKENS.T_STRING
    );
    private StartBlock startBlock;
    private final Deque<Token> tokensFromLexer;
    private final Stack<Token> tokensBuffer;

    public StartBlock getStartBlock() {
        return startBlock;
    }

    public Parser(Deque<Token> tokensFromLexer) {
        this.tokensFromLexer = tokensFromLexer;
        this.tokensBuffer = new Stack<>();
    }

    private Token getTokenFromLexer(){
        Token token = tokensFromLexer.pollFirst();
        while(token.getToken() == TOKENS.T_UNDEF){
            addError(token.getLine(), token.getPosition(), "Unexpected token " + token.getToken().toString() + ": \"" + token.getValue() + "\"");
            token = tokensFromLexer.pollFirst();
        }
        tokensBuffer.push(token);
        return token;
    }

    public boolean syntaxAnalysis(){
        if(tokensFromLexer == null || tokensFromLexer.size() == 0)
            addError(0, 0, "Undefined token list");
        else
            startBlock = new StartBlock(block());

        if(getTokenFromLexer().getToken() != TOKENS.T_EOF)
            addError(-1, -1, "The program has no end");

        if (!ERROR)
            Printer.syntaxSuccess();
        return !ERROR;
    }

    private BlockNode block(){
        ArrayList<Node> nodes = new ArrayList<>();
        if(getTokenFromLexer().getToken() == TOKENS.T_LBRACE) {
            Token token = null;
            while(token == null || (token.getToken() != TOKENS.T_RBRACE && token.getToken() != TOKENS.T_EOF)){
                Node n = BL();
                if(n == null) {
                    n = NT();
                    if(n == null){
                        token = getTokenFromLexer();
                        switch (token.getToken()){
                            case T_RBRACE -> { return new BlockNode(nodes); }
                            case T_EOF -> {
                                addError(token.getLine(), token.getPosition(), "Unclosed block of actions");
                                return null;
                            }
                            default -> {
                                addError(token.getLine(), token.getPosition(), "Unexpected token " + token.getToken().toString() + ": \"" + token.getValue() + "\"");
                                tokensBuffer.pop();
                            }
                        }
                        continue;
                    }
                }
                nodes.add(n);
            }
        }

        returnTokenFromBuffer();
        return null;
    }

    private Node NT(){
        Node n = null;
        for(int i = 0; i < 5 && n == null; i++){
            switch (i){
                case 0 -> n = init();
                case 1 -> n = decl();
                case 2 -> n = assgn();
                case 3 -> n = printf();
                case 4 -> n = scanf();
            }
        }
        if(n != null) {
            Token token = getTokenFromLexer();
            if (token.getToken() != TOKENS.T_SEMICOLON) {
                addError(token.getLine(), token.getPosition(), "A semicolon was expected");
                returnTokenFromBuffer();
            } else
                deleteTokenFromBuffer();
        }
        return n;
    }

    private Node BL(){
        Node n = null;
        for(int i = 0; i < 5 && n == null; i++){
            switch (i){
                case 0 -> n = main();
                case 1 -> n = ifState();
                case 2 -> n = whileState();
                case 3 -> n = forState();
                case 4 -> n = block();
            }
        }
        return n;
    }

    private DeclNode decl(){
        int startToken = stackSize();
        if(type.contains(getTokenFromLexer().getToken()))
            if(getTokenFromLexer().getToken() == TOKENS.T_IDENT){
                ArrayList<Token> tokens = deleteTokenFromBuffer(2);
                Token typeIdent = tokens.get(0);
                String nameIdent = tokens.get(1).getValue();
                return new DeclNode(typeIdent, nameIdent);
            }

        returnTokenFromBuffer(stackSize() - startToken);
        return null;
    }

    private InitNode init(){
        AssignNode assignNode = null;
        if(type.contains(getTokenFromLexer().getToken())) {
            assignNode = assgn();
            if (assignNode != null)
                return new InitNode(deleteTokenFromBuffer(), assignNode);
        }

        returnTokenFromBuffer();
        return null;
    }

    private AssignNode assgn(){
        int startToken = stackSize();
        if(getTokenFromLexer().getToken() == TOKENS.T_IDENT)
            if(getTokenFromLexer().getToken() == TOKENS.T_ASSGN) {
                ArrayList<Token> tokens = null;
                if (letter.contains(getTokenFromLexer().getToken())) {
                    tokens = deleteTokenFromBuffer(3);
                    return new AssignNode(tokens.get(0), tokens.get(2));
                }
                else{
                    returnTokenFromBuffer();
                    int startExpr = stackSize();
                    if(expression()) {
                        ArithmeticExpr expr = new ArithmeticExpr(deleteTokenFromBuffer(stackSize() - startExpr));
                        tokens = deleteTokenFromBuffer(2);
                        return new AssignNode(tokens.get(0), expr);
                    }
                }
            }
        returnTokenFromBuffer(stackSize() - startToken);
        return null;
    }

    private PrintfNode printf(){
        int startToken = stackSize();
        if(getTokenFromLexer().getToken() == TOKENS.T_PRINTF)
            if(getTokenFromLexer().getToken() == TOKENS.T_LPAREN) {
                Token formatString = getTokenFromLexer();
                if (formatString.getToken() == TOKENS.T_STRINGCON) {
                    ArrayList<Token> args = new ArrayList<>();
                    for (Token arg = arguments(); arg != null; arg = arguments())
                        args.add(arg);

                    Token token = getTokenFromLexer();
                    if (token.getToken() == TOKENS.T_RPAREN) {
                        deleteTokenFromBuffer(stackSize() - startToken);
                        return new PrintfNode(args, formatString);
                    } else {
                        addError(token.getLine(), token.getPosition(), "A right parenthesis was expected");
                        returnTokenFromBuffer();
                    }
                }
            }

        returnTokenFromBuffer(stackSize() - startToken);
        return null;
    }

    private ScanfNode scanf(){
        int startToken = stackSize();
        if(getTokenFromLexer().getToken() == TOKENS.T_SCANF)
            if(getTokenFromLexer().getToken() == TOKENS.T_LPAREN) {
                Token formatString = getTokenFromLexer();
                if (formatString.getToken() == TOKENS.T_STRINGCON) {
                    ArrayList<Token> args = new ArrayList<>();
                    Token arg = arguments();
                    if(arg != null) {
                        args.add(arg);
                        for (arg = arguments(); arg != null; arg = arguments())
                            args.add(arg);

                        Token token = getTokenFromLexer();
                        if (token.getToken() == TOKENS.T_RPAREN) {
                            deleteTokenFromBuffer(stackSize() - startToken);
                            return new ScanfNode(args, formatString);
                        } else {
                            addError(token.getLine(), token.getPosition(), "A right parenthesis was expected");
                            returnTokenFromBuffer();
                        }
                    }
                }
            }

        returnTokenFromBuffer(stackSize() - startToken);
        return null;
    }

    private Token arguments(){
        int startToken = stackSize();
        if(getTokenFromLexer().getToken() == TOKENS.T_COMMA) {
            Token elem = getTokenFromLexer();
            TOKENS token = elem.getToken();
            if (letter.contains(token) || number.contains(token) || token == TOKENS.T_IDENT) {
                deleteTokenFromBuffer(2);
                return elem;
            }
        }
        returnTokenFromBuffer(stackSize() - startToken);
        return null;
    }

    private MainNode main(){
        int startToken = stackSize();
        if(getTokenFromLexer().getToken() == TOKENS.T_VOID)
            if(getTokenFromLexer().getToken() == TOKENS.T_MAIN)
                if(getTokenFromLexer().getToken() == TOKENS.T_LPAREN)
                    if(getTokenFromLexer().getToken() == TOKENS.T_RPAREN) {
                        BlockNode blockNode = block();
                        if (blockNode != null) {
                            deleteTokenFromBuffer(stackSize() - startToken);
                            return new MainNode(blockNode);
                        }
                    }

        returnTokenFromBuffer(stackSize() - startToken);
        return null;
    }

    private ConditionNode ifState(){
        int startToken = stackSize();
        if(getTokenFromLexer().getToken() == TOKENS.T_IF)
            if(getTokenFromLexer().getToken() == TOKENS.T_LPAREN) {
                int startLE = stackSize();
                if (logicExpr()) {
                    ArrayList<Token> tokens = getTokenFromBuffer(stackSize() - startLE);
                    if (getTokenFromLexer().getToken() == TOKENS.T_RPAREN) {
                        BlockNode trueBlock = block();
                        if (trueBlock != null) {
                            BlockNode elseBlock = elseState();
                            deleteTokenFromBuffer(stackSize() - startToken);
                            LogicExpr LE = new LogicExpr(tokens);
                            if(elseBlock == null)
                                return new ConditionNode(LE, trueBlock);
                            else
                                return new ConditionNode(LE, trueBlock, elseBlock);
                        }
                    }
                }
            }

        returnTokenFromBuffer(stackSize() - startToken);
        return null;
    }

    private BlockNode elseState(){
        int startToken = stackSize();
        if(getTokenFromLexer().getToken() == TOKENS.T_ELSE) {
            BlockNode elseBlock = block();
            if (elseBlock != null)
                return elseBlock;
        }

        returnTokenFromBuffer(stackSize() - startToken);
        return null;
    }

    private WhileNode whileState(){
        int startToken = stackSize();
        Token tokenWhile = getTokenFromLexer();
        if(tokenWhile.getToken() == TOKENS.T_WHILE) {
            int line = tokenWhile.getLine();
            if (getTokenFromLexer().getToken() == TOKENS.T_LPAREN) {
                int startLE = stackSize();
                if (logicExpr()) {
                    ArrayList<Token> tokens = getTokenFromBuffer(stackSize() - startLE);
                    if (getTokenFromLexer().getToken() == TOKENS.T_RPAREN) {
                        BlockNode blockNode = block();
                        if (blockNode != null) {
                            deleteTokenFromBuffer(stackSize() - startToken);
                            LogicExpr LE = new LogicExpr(tokens);
                            return new WhileNode(line, LE, blockNode);
                        }
                    }
                }
            }
        }

        returnTokenFromBuffer(stackSize() - startToken);
        return null;
    }

    private ForNode forState(){
        int startToken = stackSize();
        Token tokenFor = getTokenFromLexer();
        if(tokenFor.getToken() == TOKENS.T_FOR) {
            int line = tokenFor.getLine();
            if (getTokenFromLexer().getToken() == TOKENS.T_LPAREN) {
                Node declCounter = init();
                if (declCounter == null)
                    declCounter = assgn();
                if (getTokenFromLexer().getToken() == TOKENS.T_SEMICOLON) {
                    int startLE = stackSize();
                    if (logicExpr()) {
                        ArrayList<Token> tokens = getTokenFromBuffer(stackSize() - startLE);
                        if (getTokenFromLexer().getToken() == TOKENS.T_SEMICOLON) {
                            AssignNode assignNode = assgn();
                            if (getTokenFromLexer().getToken() == TOKENS.T_RPAREN) {
                                BlockNode blockNode = block();
                                if (blockNode != null) {
                                    deleteTokenFromBuffer(stackSize() - startToken);
                                    LogicExpr LE = new LogicExpr(tokens);
                                    return new ForNode(line, declCounter, LE, assignNode, blockNode);
                                }
                            }
                        }
                    }
                }
            }
        }

        returnTokenFromBuffer(stackSize() - startToken);
        return null;
    }

    private boolean expression(){
        int startToken = stackSize();
        Token token = null;
        if(getTokenFromLexer().getToken() == TOKENS.T_LPAREN)
            if(expression())
                if(getTokenFromLexer().getToken() == TOKENS.T_RPAREN)
                    return true;

        returnTokenFromBuffer(stackSize() - startToken);

        if(getTokenFromLexer().getToken() != TOKENS.T_SUB)
            returnTokenFromBuffer();

        token = getTokenFromLexer();
        if(token.getToken() == TOKENS.T_IDENT || number.contains(token.getToken())) {
            while (nextAE());
            return true;
        }

        returnTokenFromBuffer(stackSize() - startToken);
        return false;
    }

    private boolean nextAE(){
        if (sign.contains(getTokenFromLexer().getToken()))
            if (expression())
                return true;

        returnTokenFromBuffer();
        return false;
    }

    private boolean logicExpr(){
        int startToken = stackSize();
        if(expression())
            if(logicSign.contains(getTokenFromLexer().getToken()))
                if(expression()){
                    while (nextLE());
                    return true;
                }

        if(getTokenFromLexer().getToken() != TOKENS.T_NOT)
            returnTokenFromBuffer();
        if(getTokenFromLexer().getToken() == TOKENS.T_LPAREN)
            if(logicExpr())
                if(getTokenFromLexer().getToken() == TOKENS.T_RPAREN) {
                    while (nextLE());
                    return true;
                }

        returnTokenFromBuffer(stackSize() - startToken);
        return false;
    }

    private boolean nextLE(){
        int startToken = stackSize();
        if(logicSign.contains(getTokenFromLexer().getToken()))
            if(logicExpr())
                return true;

        returnTokenFromBuffer(stackSize() - startToken);
        return false;
    }

    private int stackSize(){
        return tokensBuffer.size();
    }

    private void addError(int line, int pos, String information){
        ERROR = true;
        Printer.putLog(syntaxError + "(" + line + ", " + pos + "): " + information);
    }

    private Token deleteTokenFromBuffer(){
        return deleteTokenFromBuffer(1).get(0);
    }

    private ArrayList<Token> deleteTokenFromBuffer(int count){
        ArrayList<Token> tokens = new ArrayList<>();
        for(int i = 0; i < count; i++)
            tokens.add(tokensBuffer.pop());
        Collections.reverse(tokens);
        return tokens;
    }
    private ArrayList<Token> getTokenFromBuffer(int count){
        ArrayList<Token> tokens = new ArrayList<>();
        for(int i = 0; i < count; i++)
            tokens.add(tokensBuffer.pop());
        Collections.reverse(tokens);

        for(int i = 0; i < count; i++)
            tokensBuffer.push(tokens.get(i));

        return tokens;
    }

    private void returnTokenFromBuffer(){
        returnTokenFromBuffer(1);
    }

    private void returnTokenFromBuffer(int count){
        for(int i = 0; i < count; i++)
            tokensFromLexer.addFirst(tokensBuffer.pop());
    }
}
