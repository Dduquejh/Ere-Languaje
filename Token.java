import java.util.HashMap;
import java.util.Map;

enum TokenType {
    ASSING,
    COMMA,
    DIF,
    ELSE,
    EOF,
    EQ,
    FALSE,
    FUNCTION,
    IDENTIFIER,
    IDENT,
    IF,
    GT,
    GTE,
    ILLEGAL,
    INTEGER,
    FLOAT,
    LBRACE,
    LET,
    LPAREN,
    LT,
    LTE,
    MINUS,
    NEGATION,
    PLUS,
    RBRACE,
    RETURN,
    RPAREN,
    SEMICOLON,
    DOT,
    DIVIDE,
    MULTIPLY,
    TRUE
}

public class Token {
    TokenType type;
    String tokenLiteral;

    Token(TokenType type, String tokenLiteral) {
        this.type = type;
        this.tokenLiteral = tokenLiteral;
    }

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public String TokenLiteral() {
        return tokenLiteral;
    }

    public void setValue(String tokenLiteral) {
        this.tokenLiteral = tokenLiteral;
    }

    public static final Map<String, TokenType> KEYWORDS = new HashMap<>();
    static {
        KEYWORDS.put("funcion", TokenType.FUNCTION);
        KEYWORDS.put("si", TokenType.IF);
        KEYWORDS.put("sino", TokenType.ELSE);
        KEYWORDS.put("variable", TokenType.LET);
        KEYWORDS.put("vuelve", TokenType.RETURN);
        KEYWORDS.put("cierto", TokenType.TRUE);
        KEYWORDS.put("mentira", TokenType.FALSE);
    }

    @Override
    public String toString() {
        return "(" + type + "): " + tokenLiteral;
    }
}