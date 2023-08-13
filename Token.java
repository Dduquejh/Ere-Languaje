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
}

public class Token {
    TokenType type;
    String value;

    Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static final Map<String, TokenType> KEYWORDS = new HashMap<>();
    static {
        KEYWORDS.put("funcion", TokenType.FUNCTION);
        KEYWORDS.put("si", TokenType.IF);
        KEYWORDS.put("si-no", TokenType.ELSE);
        KEYWORDS.put("constante", TokenType.CONSTANT);
        KEYWORDS.put("variable", TokenType.LET);
    }
}