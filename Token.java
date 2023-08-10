enum TokenType{
    ASSING,
    COMMA,
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


public class Token{
    TokenType type;
    String value;

    Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }
}