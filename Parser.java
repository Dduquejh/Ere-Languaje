import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@FunctionalInterface
interface PrefixParseFn {
    Expression prefixParseFn(Parser parser);
}

@FunctionalInterface
interface InfixParseFn {
    Expression infixParseFn(Parser parser, Expression left);
}

enum Precedence {
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

class PrecedenceHashMap {
    static final Map<TokenType, Precedence> PRECEDENCES = new HashMap<TokenType, Precedence>();
    static {
        PRECEDENCES.put(TokenType.EQ, Precedence.EQUALS);
        PRECEDENCES.put(TokenType.DIF, Precedence.EQUALS);
        PRECEDENCES.put(TokenType.LT, Precedence.LESSGREATER);
        PRECEDENCES.put(TokenType.LTE, Precedence.LESSGREATER);
        PRECEDENCES.put(TokenType.GT, Precedence.LESSGREATER);
        PRECEDENCES.put(TokenType.GTE, Precedence.LESSGREATER);
        PRECEDENCES.put(TokenType.PLUS, Precedence.SUM);
        PRECEDENCES.put(TokenType.MINUS, Precedence.SUM);
        PRECEDENCES.put(TokenType.DIVIDE, Precedence.PRODUCT);
        PRECEDENCES.put(TokenType.MULTIPLY, Precedence.PRODUCT);
        PRECEDENCES.put(TokenType.LPAREN, Precedence.CALL);
        PRECEDENCES.put(TokenType.INTEGER, Precedence.LOWEST);
        PRECEDENCES.put(TokenType.EOF, Precedence.LOWEST);
    }
}

public class Parser {
    private Lexer lexer;
    private Token currentToken;
    private Token peekToken;
    private List<String> errors;

    private Map<TokenType, PrefixParseFn> _prefixFns;
    private Map<TokenType, InfixParseFn> _infixFns;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        this.errors = new ArrayList<>();

        _infixFns = new HashMap<>();
        _infixFns.put(TokenType.PLUS, (InfixParseFn) this::parseInfixExpression);
        _infixFns.put(TokenType.MINUS, (InfixParseFn) this::parseInfixExpression);
        _infixFns.put(TokenType.DIVIDE, (InfixParseFn) this::parseInfixExpression);
        _infixFns.put(TokenType.MULTIPLY, (InfixParseFn) this::parseInfixExpression);
        _infixFns.put(TokenType.EQ, (InfixParseFn) this::parseInfixExpression);
        _infixFns.put(TokenType.DIF, (InfixParseFn) this::parseInfixExpression);
        _infixFns.put(TokenType.LT, (InfixParseFn) this::parseInfixExpression);
        _infixFns.put(TokenType.LTE, (InfixParseFn) this::parseInfixExpression);
        _infixFns.put(TokenType.GT, (InfixParseFn) this::parseInfixExpression);
        _infixFns.put(TokenType.GTE, (InfixParseFn) this::parseInfixExpression);
        _infixFns.put(TokenType.LPAREN, (InfixParseFn) this::parseCall);

        _prefixFns = new HashMap<>();
        _prefixFns.put(TokenType.FALSE, (PrefixParseFn) this::parseBoolean);
        _prefixFns.put(TokenType.FUNCTION, (PrefixParseFn) this::parseFunction);
        _prefixFns.put(TokenType.IDENTIFIER, (PrefixParseFn) this::parseIdentifier);
        _prefixFns.put(TokenType.IF, (PrefixParseFn) this::parseIf);
        _prefixFns.put(TokenType.INTEGER, (PrefixParseFn) this::parseInteger);
        _prefixFns.put(TokenType.LPAREN, (PrefixParseFn) this::parseGroupedExpression);
        _prefixFns.put(TokenType.MINUS, (PrefixParseFn) this::parsePrefixExpression);
        _prefixFns.put(TokenType.NEGATION, (PrefixParseFn) this::parsePrefixExpression);
        _prefixFns.put(TokenType.TRUE, (PrefixParseFn) this::parseBoolean);

        advanceTokens();
        advanceTokens();
    }

    public List<String> getErrors() {
        return errors;
    }

    public Program parseProgram(Parser parser) {
        Program program = new Program(new ArrayList<>());

        while (currentToken.getType() != null && currentToken.getType() != TokenType.EOF) {
            Statement statement = parseStatement(parser);
            if (statement != null) {
                program.getStatements().add(statement);
            }
            advanceTokens();
        }
        return program;
    }

    public void advanceTokens() {
        currentToken = peekToken;
        peekToken = lexer.nextToken();
        if (peekToken == null) {
            peekToken = new Token(TokenType.EOF, "");
        }
    }

    public Precedence currentPrecedence() {
        if (currentToken == null)
            throw new AssertionError("El token actual es nulo");
        try {
            return PrecedenceHashMap.PRECEDENCES.get(currentToken.getType());
        } catch (NullPointerException e) {
            return Precedence.LOWEST;
        }
    }

    public boolean expectedToken(TokenType tokenType) {
        if (peekToken == null)
            throw new AssertionError("El token es nulo");
        if (peekToken.getType() == tokenType) {
            advanceTokens();
            return true;
        }
        expectedTokenError(tokenType);
        return false;
    }

    public void expectedTokenError(TokenType tokenType) {
        if (peekToken == null)
            throw new AssertionError("El token es nulo");
        String error = "Se esperaba que el siguiente token fuera " + tokenType + "\n" + "pero se obtuvo "
                + peekToken.getType();
        errors.add(error);
    }

    public Block parseBlock(Parser parser) {
        if (currentToken == null)
            throw new AssertionError("El token actual es nulo");
        Block blockStatement = new Block(currentToken, new ArrayList<>());
        advanceTokens();
        while (!(currentToken.getType() == TokenType.RBRACE || currentToken.getType() == TokenType.EOF)) {
            Statement statement = parseStatement(parser);

            if (statement != null)
                blockStatement.getStatements().add(statement);
            advanceTokens();
        }
        return blockStatement;
    }

    public BooleanExpression parseBoolean(Parser parser) {
        if (currentToken == null)
            throw new AssertionError("El token actual es nulo");
        return new BooleanExpression(currentToken, currentToken.getType() == TokenType.TRUE);
    }

    public Call parseCall(Parser parser, Expression function) {
        if (currentToken == null)
            throw new AssertionError("El token actual es nulo");
        Call call = new Call(currentToken, function, parseCallArguments());
        return call;
    }

    public List<Expression> parseCallArguments() {
        List<Expression> arguments = new ArrayList<>();
        if (peekToken == null)
            throw new AssertionError("El token es nulo");
        if (peekToken.getType() == TokenType.RPAREN) {
            advanceTokens();
            return arguments;
        }
        advanceTokens();
        Expression expression = parseExpression(Precedence.LOWEST);
        if (expression != null)
            arguments.add(expression);
        while (peekToken.getType() == TokenType.COMMA) {
            advanceTokens();
            advanceTokens();
            expression = parseExpression(Precedence.LOWEST);
            if (expression != null)
                arguments.add(expression);
        }
        if (!expectedToken(TokenType.RPAREN))
            return null;
        return arguments;
    }

    public Expression parseExpression(Precedence precedence) {
        if (currentToken == null)
            throw new AssertionError("El token actual es nulo");
        PrefixParseFn prefixParseFn = _prefixFns.get(currentToken.getType());

        if (prefixParseFn == null) {
            String message = "No se encontró ninguna función para analizar " + currentToken.TokenLiteral();
            errors.add(message);
            return null;
        }

        Expression leftExpression = prefixParseFn.prefixParseFn(this);

        if (peekToken == null)
            throw new AssertionError("El token es nulo");

        while (!(peekToken.getType() == TokenType.SEMICOLON) && precedence.getValue() < peekPrecedence().getValue()) {
            InfixParseFn infixParseFn = _infixFns.get(peekToken.getType());

            if (infixParseFn == null) {
                return leftExpression;
            }

            advanceTokens();

            if (leftExpression == null)
                throw new AssertionError("leftExpression es nulo");

            leftExpression = infixParseFn.infixParseFn(this, leftExpression);

        }

        return leftExpression;
    }

    public ExpressionStatement parseExpressionStatement() {
        if (currentToken == null)
            throw new AssertionError("El token actual es nulo");
        ExpressionStatement expressionStatement = new ExpressionStatement(currentToken, null);
        expressionStatement.setExpression(parseExpression(Precedence.LOWEST));
        if (peekToken == null)
            throw new AssertionError("El token es nulo");
        if (peekToken.getType() == TokenType.SEMICOLON) {
            advanceTokens();
        }
        return expressionStatement;
    }

    public Expression parseGroupedExpression(Parser parser) {
        advanceTokens();
        Expression expression = parseExpression(Precedence.LOWEST);
        if (!expectedToken(TokenType.RPAREN))
            return null;
        return expression;
    }

    public Function parseFunction(Parser parser) {
        if (currentToken == null)
            throw new AssertionError("El token actual es nulo");
        Function function = new Function(currentToken, null, null);
        if (!expectedToken(TokenType.LPAREN))
            return null;
        function.setParameters(parseFunctionParameters());
        if (!expectedToken(TokenType.LBRACE))
            return null;
        function.setBody(parseBlock(parser));
        return function;
    }

    public List<Identifier> parseFunctionParameters() {
        List<Identifier> params = new ArrayList<>();
        if (peekToken == null)
            throw new AssertionError("El token es nulo");
        if (peekToken.getType() == TokenType.RPAREN) {
            advanceTokens();
            return params;
        }
        advanceTokens();
        if (currentToken == null)
            throw new AssertionError("El token actual es nulo");
        Identifier identifier = new Identifier(currentToken, currentToken.TokenLiteral());
        params.add(identifier);
        while (peekToken.getType() == TokenType.COMMA) {
            advanceTokens();
            advanceTokens();
            identifier = new Identifier(currentToken, currentToken.TokenLiteral());
            params.add(identifier);
        }
        if (!expectedToken(TokenType.RPAREN))
            return new ArrayList<>();
        return params;
    }

    public Identifier parseIdentifier(Parser parser) {
        if (currentToken == null)
            throw new AssertionError("El token actual es nulo");
        return new Identifier(currentToken, currentToken.TokenLiteral());
    }

    public If parseIf(Parser parser) {
        if (currentToken == null)
            throw new AssertionError("El token actual es nulo");
        If ifExpression = new If(currentToken, null, null, null);
        if (!expectedToken(TokenType.LPAREN))
            return null;
        advanceTokens();
        Expression expression = parseExpression(Precedence.LOWEST);
        ifExpression.setCondition(expression);
        if (!expectedToken(TokenType.RPAREN))
            return null;
        if (!expectedToken(TokenType.LBRACE))
            return null;
        ifExpression.setConsequense(parseBlock(parser));
        if (peekToken == null)
            throw new AssertionError("El token es nulo");
        if (peekToken.getType() == TokenType.ELSE) {
            advanceTokens();
            if (!expectedToken(TokenType.LBRACE))
                return null;
            ifExpression.setAlternative(parseBlock(parser));
        }
        return ifExpression;
    }

    public Infix parseInfixExpression(Parser parser, Expression left) {
        if (currentToken == null)
            throw new AssertionError("El token actual es nulo");
        String operator = currentToken.TokenLiteral(); // Guarda el operador actual.
        Precedence precedence = currentPrecedence();
        advanceTokens();
        Expression right = parseExpression(precedence);

        // Agrega paréntesis alrededor de la expresión binaria.
        String infixExpr = "(" + left.toString() + " " + operator + " " + right.toString() + ")";

        return new Infix(currentToken, left, operator, right, infixExpr);
    }

    public IntegerExpression parseInteger(Parser parser) {
        if (currentToken == null)
            throw new AssertionError("El token actual es nulo");
        IntegerExpression integer = new IntegerExpression(currentToken, null);
        try {
            integer.setValue(Integer.parseInt(currentToken.TokenLiteral()));
        } catch (NullPointerException e) {
            String message = "No se ha podido parsear " + currentToken.TokenLiteral() + " como entero.";
            errors.add(message);
        }
        return integer;
    }

    public LetStatement parseLetStatement(Parser parser) {
        if (currentToken == null)
            throw new AssertionError("El token actual es nulo");
        LetStatement letStatement = new LetStatement(currentToken, null, null);
        if (!expectedToken(TokenType.IDENTIFIER))
            return null;
        letStatement.setName(parseIdentifier(parser));
        if (!expectedToken(TokenType.ASSING))
            return null;
        advanceTokens();
        letStatement.setValue(parseExpression(Precedence.LOWEST));
        if (peekToken == null)
            throw new AssertionError("El token es nulo");
        if (peekToken.getType() == TokenType.SEMICOLON)
            advanceTokens();
        return letStatement;
    }

    public Prefix parsePrefixExpression(Parser parser) {
        if (currentToken == null)
            throw new AssertionError("El token actual es nulo");
        Prefix prefixExpression = new Prefix(currentToken, currentToken.TokenLiteral(), null);
        advanceTokens();
        prefixExpression.setRight(parseExpression(Precedence.PREFIX));
        return prefixExpression;
    }

    public ReturnStatement parseReturnStatement() {
        if (currentToken == null)
            throw new AssertionError("El token actual es nulo");
        ReturnStatement returnStatement = new ReturnStatement(currentToken, null);
        advanceTokens();
        returnStatement.setReturnValue(parseExpression(Precedence.LOWEST));
        if (peekToken == null)
            throw new AssertionError("El token es nulo");
        if (peekToken.getType() == TokenType.SEMICOLON)
            advanceTokens();
        return returnStatement;
    }

    public Statement parseStatement(Parser parser) {
        if (currentToken == null)
            throw new AssertionError("El token actual es nulo");
        if (currentToken.getType() == TokenType.LET)
            return parseLetStatement(parser);
        else if (currentToken.getType() == TokenType.RETURN)
            return parseReturnStatement();
        else
            return parseExpressionStatement();
    }

    public Precedence peekPrecedence() {
        if (peekToken == null || peekToken.getType() == null) {
            return Precedence.LOWEST;
        }
        Precedence precedence = PrecedenceHashMap.PRECEDENCES.get(peekToken.getType());
        if (precedence != null) {
            return precedence;
        } else {
            return Precedence.LOWEST;
        }
    }

}
