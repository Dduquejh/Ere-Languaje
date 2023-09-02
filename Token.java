import java.util.HashMap;
import java.util.Map;

enum TokenType {
    ASSING,
    COMMA,
    CONSTANT,
    DIF,
    ELSE,
    EOF,
    EQ,
    FUNCTION,
    IDENTIFIER,
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
    RPAREN,
    SEMICOLON,
    DOT,
    DIVIDE,
    MULTIPLY
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
        KEYWORDS.put("constante", TokenType.CONSTANT);
        KEYWORDS.put("variable", TokenType.LET);
    }

    @Override
    public String toString() {
        return "(" + type + "): " + tokenLiteral;
    }
}