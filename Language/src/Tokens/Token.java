package Tokens;

public class Token {
    private final TOKENS token;
    private final String value;
    private final int position;
    private final int line;
    public Token(TOKENS token, String value, int line, int position) {
        this.token = token;
        this.value = value;
        this.line = line;
        this.position = position;
    }
    public int getLine() {
        return line;
    }
    public TOKENS getToken() {
        return token;
    }
    public String getValue() {
        return value;
    }
    public int getPosition() {
        return position;
    }
}
