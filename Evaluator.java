import java.util.List;

public class Evaluator {
    private BooleanObject TRUE = new BooleanObject(true);
    private BooleanObject FALSE = new BooleanObject(false);
    private Null NULL = null;

    protected Object evaluateBangOperatorExpression(Object right){
        if (right == TRUE)
            return FALSE;
        else if (right == FALSE)
            return TRUE;
        else if (right == NULL)
            return TRUE;
        else
            return FALSE;
    }

    protected BooleanObject toBooleanObject(boolean value){
        return value == true ? TRUE : FALSE;
    }

    protected Object evaluateMinusOperatorExpression(Object right){
        if (!(right instanceof IntegerObject))
            return NULL;
        IntegerObject integerRight = (IntegerObject)right;
        return new IntegerObject(-integerRight.getValue());
    }

    protected Object evaluatePrefixExpression(String operator, Object right){
        switch(operator){
            case "!":
                return evaluateBangOperatorExpression(right);
            case "-":
                return evaluateMinusOperatorExpression(right);
            default:
                return NULL;
        }
    }

    protected Object evaluateIntegerInfixExpression(String operator, Object left, Object right){
        int leftValue = ((IntegerObject)left).getValue();
        int rightValue = ((IntegerObject)right).getValue();

        switch (operator){
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
                return NULL;
        }
    }

    protected Object evaluateInfixExpression(String operator, Object left, Object right){
        if (left instanceof IntegerObject && right instanceof IntegerObject)
            return evaluateIntegerInfixExpression(operator, left, right);
        else if (operator == "==")
            return toBooleanObject(left == right);
        else if (operator == "!=")
            return toBooleanObject(left != right);
        else
            return NULL;
    }

    protected Object evaluate(ASTNode node){
        if (node instanceof Program){
            Program program = (Program)node;
            return evaluateStatements(program.getStatements());
        }else if (node instanceof ExpressionStatement){
            ExpressionStatement expressionStatement = (ExpressionStatement)node;
            if (expressionStatement.getExpression() == null)
                throw new AssertionError("El token actual es nulo");
                
            return evaluate(expressionStatement.getExpression());
        }else if (node instanceof IntegerExpression){
            IntegerExpression integerExpression = (IntegerExpression)node;
            if (integerExpression.getValue() == null)
                throw new AssertionError("El token actual es nulo");

            return new IntegerObject(integerExpression.getValue());
        }else if (node instanceof BooleanExpression){
            BooleanExpression booleanExpression = (BooleanExpression)node;
            if (booleanExpression.getValue() == null)
                throw new AssertionError("El token actual es nulo");

            return toBooleanObject(booleanExpression.getValue());
        }else if (node instanceof Prefix){
            Prefix prefix = (Prefix)node;
            if (prefix.getRight() == null)
                throw new AssertionError("El token actual es nulo");

            Object right = evaluate(prefix.getRight());
            if (right == null)
                throw new AssertionError("El token actual es nulo");

            return evaluatePrefixExpression(prefix.getOperator(), right);
        }else if (node instanceof Infix){
            Infix infix = (Infix)node;
            if (infix.getLeft() == null || infix.getRight() == null)
                throw new AssertionError("El token actual es nulo");

            Object left = evaluate(infix.getLeft());
            Object right = evaluate(infix.getRight());
            if (left == null || right == null)
                throw new AssertionError("El token actual es nulo");

            return evaluateInfixExpression(infix.getOperator(), left, right);
        }else
            return NULL;
    }

    protected Object evaluateStatements(List <Statement> statements){
        Object result = null;

        for(Statement statement: statements)
            result = evaluate(statement);

        return result;
    }
}
