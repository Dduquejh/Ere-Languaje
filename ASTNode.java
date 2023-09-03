import java.util.List;

public abstract class ASTNode{
    abstract String tokenLiteral();
    public abstract String toString();
}


class Statement extends ASTNode{
    private Token token;

    public Statement(Token token) {
        this.token = token;
    }

    @Override
    String tokenLiteral() {
        return token.tokenLiteral;
    }

    @Override
    public String toString() {
        return null;
    } 
}


class Expression extends ASTNode{
    private Token token;

    public Expression(Token token) {
        this.token = token;
    }
    
    @Override
    String tokenLiteral() {
        return token.TokenLiteral();
    }

    @Override
    public String toString() {
        return null;
    }

}


class Program extends ASTNode{
    private List<Statement> statements;

    public Program(List<Statement> statements) {
        this.statements = statements;
    }
    
    @Override
    String tokenLiteral() {
        if (statements.size() > 0)
            return statements.get(0).tokenLiteral();
        return "";
    }
    
    @Override
    public String toString() {
        return null;
    }
}


class Identifier extends Expression{
    private String value;

    public Identifier(Token token, String value) {
        super(token);
        this.value = value;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return value;
    }
}


class LetStatement extends Statement{
    private Identifier name;
    private Expression value;
    
    public LetStatement(Token token, Identifier name, Expression value) {
        super(token);
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return tokenLiteral() + " " + name != null ? name.toString() : "null" + " = " + value != null ? value.toString() : "null";
    }  
}


class ReturnStatement extends Statement{
    private Expression returnValue;

    public ReturnStatement(Token token, Expression returnValue) {
        super(token);
        this.returnValue = returnValue;
    }

    @Override
    public String toString() {
        return tokenLiteral() + " " + returnValue != null ? returnValue.toString() : "null";
    }
}


class ExpressionStatement extends Statement{
    private Expression expression;

    public ExpressionStatement(Token token, Expression expression) {
        super(token);
        this.expression = expression;
    }

    @Override
    public String toString() {
        return expression != null ? expression.toString() : "null";
    } 
}


class Integer extends Expression{
    private Integer value;

    public Integer(Token token, Integer value) {
        super(token);
        this.value = value;
    }

    @Override
    public String toString() {
        return value != null ? value.toString() : "null";
    }   
}


class Prefix extends Expression{
    private String operator;
    private Expression right;
    
    public Prefix(Token token, String operator, Expression right) {
        super(token);
        this.operator = operator;
        this.right = right;
    }

    @Override
    public String toString() {
        return operator + " " + right != null ? right.toString() : "null";
    }   
}


class Infix extends Expression{
    private Expression left;
    private String operator;
    private Expression right;
    
    public Infix(Token token, Expression left, String operator, Expression right) {
        super(token);
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public String toString() {
        return left.toString() + " " + operator + " " + right != null ? right.toString(): "null";
    }
}


class Boolean extends Expression{
    private Boolean value;

    public Boolean(Token token, Boolean value) {
        super(token);
        this.value = value;
    }

    @Override
    public String toString() {
        return tokenLiteral() + value != null ? value.toString(): "null";
    }   
}


class Block extends Statement{
    private List <Statement> statements;

    public Block(Token token, List<Statement> statements) {
        super(token);
        this.statements = statements;
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        for(Statement statement: statements)
            out.append(statement);
        return out.toString();
    }
}


class If extends Expression{
    private Expression condition;
    private Block consequense;
    private Block alternative;

    public If(Token token, Expression condition, Block consequense, Block alternative) {
        super(token);
        this.condition = condition;
        this.consequense = consequense;
        this.alternative = alternative;
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("si" + (condition != null ? condition : "null") + " " + (consequense != null ? consequense : "null"));
        
        if(alternative != null)
            out.append(" sino " + alternative);
        
        return out.toString();
    }
}


class Function extends Expression{
    private List <Identifier> parameters;
    private Block body;
    
    public Function(Token token, List<Identifier> parameters, Block body) {
        super(token);
        this.parameters = parameters;
        this.body = body;
    }

    @Override
    public String toString() {
        StringBuilder params = new StringBuilder();
        boolean firstParameter = true;
        for(Identifier parameter: parameters){
            if(firstParameter)
                firstParameter = false;
            else
                params.append(", ");
            params.append(parameter);
        }
        return tokenLiteral() + "(" + params +")" + (body != null ? body.toString() : "null");
    }  
}


class Call extends Expression{
    private Expression function;
    private List <Expression> arguments;
    
    public Call(Token token, Expression function, List<Expression> arguments) {
        super(token);
        this.function = function;
        this.arguments = arguments;
    }

    @Override
    public String toString() {
        if (arguments == null)
            throw new AssertionError("Argumentos nulos");
        
        StringBuilder args = new StringBuilder();
        boolean firstParameter = true;
        for(Expression argument: arguments){
            if(firstParameter)
                firstParameter = false;
            else
                args.append(", ");
            args.append(argument);
        }
        return function.toString() + args;
    }
}