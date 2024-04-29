package Tokens;

import java.util.regex.Pattern;

public enum TOKENS_TYPES{ //Перечисление всех токенов и регулярных выражений, по которым их распознают
    SPACES(Pattern.compile("^ +"), TOKENS.T_UNDEF),
    STRINGCON(Pattern.compile("^\"([^\"\\\\]*(\\\\.[^\"\\\\]*)*)\""), TOKENS.T_STRINGCON),
    FLOATCON(Pattern.compile("^\\d*\\.\\d+((e|E)(-|\\+)?\\d+)?"), TOKENS.T_FLOATCON),
    INTCON(Pattern.compile("^(0|(-?[1-9]\\d*))"), TOKENS.T_INTCON),
    CHARCON(Pattern.compile("^'([^'])'"), TOKENS.T_CHARCON),
    CHAR(Pattern.compile("^char"), TOKENS.T_CHAR),
    INT(Pattern.compile("^int"), TOKENS.T_INT),
    FLOAT(Pattern.compile("^float"), TOKENS.T_FLOAT),
    STRING(Pattern.compile("^string"), TOKENS.T_STRING),
    VOID(Pattern.compile("^void"), TOKENS.T_VOID),
    IF(Pattern.compile("^if"), TOKENS.T_IF),
    ELSE(Pattern.compile("^else"), TOKENS.T_ELSE),
    WHILE(Pattern.compile("^while"), TOKENS.T_WHILE),
    FOR(Pattern.compile("^for"), TOKENS.T_FOR),
    PRINTF(Pattern.compile("^printf"), TOKENS.T_PRINTF),
    SCANF(Pattern.compile("^scanf"), TOKENS.T_SCANF),
    MAIN(Pattern.compile("^main"), TOKENS.T_MAIN),
    IDENT(Pattern.compile("^[a-zA-Z]([_a-zA-Z\\d]){0,64}"), TOKENS.T_IDENT),
    ADD(Pattern.compile("^\\+"), TOKENS.T_ADD),
    SUB(Pattern.compile("^-"), TOKENS.T_SUB),
    MUL(Pattern.compile("^\\*"), TOKENS.T_MUL),
    DIV(Pattern.compile("^/"), TOKENS.T_DIV),
    REM(Pattern.compile("^%"), TOKENS.T_REM),
    NOT(Pattern.compile("^!"), TOKENS.T_NOT),
    EQUAL(Pattern.compile("^=="), TOKENS.T_EQUAL),
    NEQUAL(Pattern.compile("^!="), TOKENS.T_NEQUAL),
    EOGREAT(Pattern.compile("^>="), TOKENS.T_EOGREAT),
    GREAT(Pattern.compile("^>"), TOKENS.T_GREAT),
    EOLESS(Pattern.compile("^<="), TOKENS.T_EOLESS),
    LESS(Pattern.compile("^<"), TOKENS.T_LESS),
    ASSGN(Pattern.compile("^="), TOKENS.T_ASSGN),
    AND(Pattern.compile("^&&"), TOKENS.T_AND),
    OR(Pattern.compile("^\\|\\|"), TOKENS.T_OR),
    LPAREN(Pattern.compile("^\\("), TOKENS.T_LPAREN),
    RPAREN(Pattern.compile("^\\)"), TOKENS.T_RPAREN),
    LBRACE(Pattern.compile("^\\{"), TOKENS.T_LBRACE),
    RBRACE(Pattern.compile("^}"), TOKENS.T_RBRACE),
    LBRACKET(Pattern.compile("^\\["), TOKENS.T_LBRACKET),
    RBRACKET(Pattern.compile("^]"), TOKENS.T_RBRACKET),
    SEMICOLON(Pattern.compile("^;"), TOKENS.T_SEMICOLON),
    COMMA(Pattern.compile("^,"), TOKENS.T_COMMA);
    private final Pattern pattern;
    private final TOKENS token;
    public Pattern getPattern() {
        return pattern;
    }
    public TOKENS getToken() {
        return token;
    }
    TOKENS_TYPES(Pattern pattern, TOKENS tokenS) {
        this.pattern = pattern;
        this.token = tokenS;
    }
}