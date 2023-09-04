import java.util.HashMap;
import java.util.Map;

enum Precedence{
    LOWEST(1),
    EQUALS(2),
    LESSGREATER(3),
    SUM(4),
    PRODUCT(5),
    PREFIX(6),
    CALL(7);
    
    private int value;

    private Precedence(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

class PrecedenceHashMap{
    private static final Map<TokenType, Precedence> PRECEDENCES = new HashMap<TokenType, Precedence>();
    static{
        PRECEDENCES.put(TokenType.EQ, Precedence.EQUALS);
        PRECEDENCES.put(TokenType.DIF, Precedence.EQUALS);
        PRECEDENCES.put(TokenType.LT, Precedence.LESSGREATER);
        PRECEDENCES.put(TokenType.GT, Precedence.LESSGREATER);
        PRECEDENCES.put(TokenType.PLUS, Precedence.SUM);
        PRECEDENCES.put(TokenType.MINUS, Precedence.SUM);
        PRECEDENCES.put(TokenType.DIVIDE, Precedence.PRODUCT);
        PRECEDENCES.put(TokenType.MULTIPLY, Precedence.PRODUCT);
        PRECEDENCES.put(TokenType.LPAREN, Precedence.CALL);
    }
} 

public class Parser {
    
}
