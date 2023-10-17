import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Evaluator {
    private BooleanObject TRUE = new BooleanObject(true);
    private BooleanObject FALSE = new BooleanObject(false);
    private Null NULL = null;
    String typeMismatchMessage = ErrorMessages.TYPE_MISMATCH;
    String unknownPrefixOperatorMessage = ErrorMessages.UNKNOWN_PREFIX_OPERATOR;
    String unknownInfixOperatorMessage = ErrorMessages.UNKNOWN_INFIX_OPERATOR;
    String unknownIdentifierMessage = ErrorMessages.UNKNOWN_IDENTIFIER;

    protected Optional<Object> evaluate(ASTNode node, Environment env) {
        if (node instanceof Program) {
            Program program = (Program) node;
            return Optional.of(evaluateProgram(program, env));
        } else if (node instanceof ExpressionStatement) {
            ExpressionStatement expressionStatement = (ExpressionStatement) node;
            if (expressionStatement.getExpression() == null)
                throw new AssertionError("El token actual es nulo");

            return evaluate(expressionStatement.getExpression(), env);
        } else if (node instanceof IntegerExpression) {
            IntegerExpression integerExpression = (IntegerExpression) node;
            if (integerExpression.getValue() == null)
                throw new AssertionError("El token actual es nulo");

            return Optional.of(new IntegerObject(integerExpression.getValue()));
        } else if (node instanceof BooleanExpression) {
            BooleanExpression booleanExpression = (BooleanExpression) node;
            if (booleanExpression.getValue() == null)
                throw new AssertionError("El token actual es nulo");

            return Optional.of(toBooleanObject(booleanExpression.getValue()));
        } else if (node instanceof Prefix) {
            Prefix prefix = (Prefix) node;
            if (prefix.getRight() == null)
                throw new AssertionError("El token actual es nulo");

            Object right = evaluate(prefix.getRight(), env);
            if (right == null)
                throw new AssertionError("El token actual es nulo");

            return Optional.of(evaluatePrefixExpression(prefix.getOperator(), right));
        } else if (node instanceof Infix) {
            Infix infix = (Infix) node;
            if (infix.getLeft() == null || infix.getRight() == null)
                throw new AssertionError("El token actual es nulo");

            Object left = evaluate(infix.getLeft(), env);
            Object right = evaluate(infix.getRight(), env);
            if (left == null || right == null)
                throw new AssertionError("El token actual es nulo");

            return Optional.of(evaluateInfixExpression(infix.getOperator(), left, right));
        } else if (node instanceof Block) {
            Block block = (Block) node;
            return Optional.of(evaluateBlockStatement(block, env));
        } else if (node instanceof If) {
            If condition = (If) node;
            return Optional.of(evaluateIfExpression(condition, env));
        } else if (node instanceof ReturnStatement) {
            ReturnStatement returnStatement = (ReturnStatement) node;
            if (returnStatement.getReturnValue() == null)
                throw new AssertionError("El token actual es nulo");
            Object value = evaluate(returnStatement.getReturnValue(), env);
            return Optional.of((ReturnObject) value);
        } else if (node instanceof LetStatement) {
            LetStatement letStatement = (LetStatement) node;
            if (letStatement.getValue() == null)
                throw new AssertionError("El token actual es nulo");
            Object value = evaluate(letStatement.getValue(), env);
            if (letStatement.getName() == null)
                throw new AssertionError("El token actual es nulo");
            env.set(letStatement.name.value, value);
            return Optional.empty();
        } else if (node instanceof Identifier) {
            Identifier identifier = (Identifier) node;
            return Optional.of(evaluateIdentifier(identifier, env));
        } else
            return Optional.of(NULL);
    }

    private Optional<Object> evaluateProgram(Program program, Environment env) {
        Optional<Object> result = Optional.empty();

        for (Statement statement : program.getStatements()) {
            result = evaluate(statement, env);

            if (result.isPresent()) {
                Object resultValue = result.get();
                if (resultValue instanceof ReturnObject) {
                    return Optional.of(((ReturnObject) resultValue).getValue());
                } else if (resultValue instanceof Error) {
                    return Optional.of(resultValue);
                }
            }
        }
        return result;
    }

    private Object evaluateBangOperatorExpression(Object right) {
        if (right == TRUE)
            return FALSE;
        else if (right == FALSE)
            return TRUE;
        else if (right == NULL)
            return TRUE;
        else
            return FALSE;
    }

    private Optional<Object> evaluateBlockStatement(Block block, Environment env) {
        Optional<Object> result = Optional.empty();
        for (Statement statement : block.getStatements()) {
            result = Optional.of(evaluate(statement, env));

            if (result.isPresent()) {
                Object resultValue = result.get();
                if (resultValue instanceof ReturnObject || resultValue instanceof Error)
                    return Optional.of(resultValue);
            }
        }
        return result;
    }

    private Object evaluateIdentifier(Identifier node, Environment env) {
        Object value = env.get(node.getValue());
    if (value != null) {
        return value;
    } else {
        return newError(ErrorMessages.UNKNOWN_IDENTIFIER, Arrays.asList(node.getValue()));
    }
    }

    private Object evaluateIfExpression(If ifExpression, Environment env) {
        if (ifExpression == null)
            throw new AssertionError("El token actual es nulo");
        Object condition = evaluate(ifExpression.getCondition(), env);

        if (condition == null)
            throw new AssertionError("El token actual es nulo");
        if (isTruthy(condition)) {
            if (ifExpression.getConsequense() == null)
                throw new AssertionError("El token actual es nulo");
            return evaluate(ifExpression.getConsequense(), env);
        } else if (ifExpression.getAlternative() != null)
            return evaluate(ifExpression.getAlternative(), env);
        else
            return NULL;
    }

    private boolean isTruthy(Object obj) {
        if (obj == NULL)
            return false;
        else if (obj == TRUE)
            return true;
        else if (obj == FALSE)
            return false;
        else
            return true;
    }

    private Object evaluateInfixExpression(String operator, Object left, Object right) {
        if (left instanceof IntegerObject && right instanceof IntegerObject)
            return evaluateIntegerInfixExpression(operator, left, right);
        else if (left instanceof IntegerObject && right instanceof IntegerObject) {
            Integer leftValue = ((IntegerObject) left).getValue();
            Integer rightValue = ((IntegerObject) right).getValue();

            switch (operator) {
                case "+":
                    return new IntegerObject(leftValue + rightValue);
                case "==":
                    return toBooleanObject(leftValue.equals(rightValue));
                case "!=":
                    return toBooleanObject(!leftValue.equals(rightValue));
                default:
                    return NULL;
            }
        } else if (operator.equals("=="))
            return toBooleanObject(left == right);
        else if (operator.equals("!="))
            return toBooleanObject(left != right);
        else if (!left.getClass().equals(right.getClass()))
            return newError(ErrorMessages.TYPE_MISMATCH, Arrays.asList(left.getClass().getSimpleName(), operator, right.getClass().getSimpleName()));
        else
            return newError(ErrorMessages.UNKNOWN_INFIX_OPERATOR, Arrays.asList(left.getClass().getSimpleName(), operator, right.getClass().getSimpleName()));
    }

    private Object evaluateIntegerInfixExpression(String operator, Object left, Object right) {
        int leftValue = ((IntegerObject) left).getValue();
        int rightValue = ((IntegerObject) right).getValue();

        switch (operator) {
            case "+":
                return new IntegerObject(leftValue + rightValue);
            case "-":
                return new IntegerObject(leftValue - rightValue);
            case "*":
                return new IntegerObject(leftValue * rightValue);
            case "/":
                return new IntegerObject(leftValue / rightValue);
            case "<":
                return toBooleanObject(leftValue < rightValue);
            case "<=":
                return toBooleanObject(leftValue <= rightValue);
            case ">":
                return toBooleanObject(leftValue > rightValue);
            case ">=":
                return toBooleanObject(leftValue >= rightValue);
            case "==":
                return toBooleanObject(leftValue == rightValue);
            case "!=":
                return toBooleanObject(leftValue != rightValue);
            default:
                return newError(ErrorMessages.UNKNOWN_INFIX_OPERATOR, Arrays.asList(left.getClass().getSimpleName(), operator, right.getClass().getSimpleName()));
        }
    }

    private Object evaluateMinusOperatorExpression(Object right) {
        if (!(right instanceof IntegerObject))
            return newError(ErrorMessages.UNKNOWN_PREFIX_OPERATOR, Arrays.asList('-', right.getClass().getSimpleName()));
        IntegerObject integerRight = (IntegerObject) right;
        return new IntegerObject(-integerRight.getValue());
    }

    private Object evaluatePrefixExpression(String operator, Object right) {
        switch (operator) {
            case "!":
                return evaluateBangOperatorExpression(right);
            case "-":
                return evaluateMinusOperatorExpression(right);
            default:
                return newError(ErrorMessages.UNKNOWN_PREFIX_OPERATOR, Arrays.asList(operator, right.getClass().getSimpleName()));
        }
    }

    private BooleanObject toBooleanObject(boolean value) {
        return value == true ? TRUE : FALSE;
    }

    

    

    private Error newError(String message, List<Object> args) {
        return new Error(String.format(message, args.toArray()));
    }
}

class ErrorMessages {
    public static final String TYPE_MISMATCH = "Discrepancia de tipos: {} {} {}";
    public static final String UNKNOWN_PREFIX_OPERATOR = "Operador desconocido: {}{}";
    public static final String UNKNOWN_INFIX_OPERATOR = "Operador desconocido: {} {} {}";
    public static final String UNKNOWN_IDENTIFIER = "Identificador no encontrado: {}";
}

