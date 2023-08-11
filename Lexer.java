
public class Lexer {
    private int position;

    Lexer() {
        this.position = 0;
    }

    protected void nextToken(String input) {
        while (position < input.length()) {
            char currentChar = input.charAt(position);
            if (currentChar == '=') {
                System.out.println("(" + TokenType.ASSING + "): " + currentChar);
                position++;
            } else if (currentChar == '!') {
                System.out.println("(" + TokenType.NEGATION + "): " + currentChar);
                position++;
            } else if (currentChar == '+') {
                System.out.println("(" + TokenType.PLUS + "): " + currentChar);
                position++;
            } else if (currentChar == '-') {
                System.out.println("(" + TokenType.PLUS + "): " + currentChar);
                position++;
            } else if (currentChar == ';') {
                System.out.println("(" + TokenType.EOF + "): " + currentChar);
                position++;
            } else if (currentChar == '!') {
                System.out.println("(" + TokenType.NEGATION + "): " + currentChar);
                position++;
            } else if (Character.isLetter(currentChar)) {
                System.out.println("(" + TokenType.IDENTIFIER + "): " + currentChar);
                position++;
            } else if (Character.isDigit(currentChar)) {
                System.out.println("(" + TokenType.INTEGER + "): " + currentChar);
                position++;
            } else if (currentChar == '>') {
                System.out.println("(" + TokenType.GT + "): " + currentChar);
                position++;
            } else if (currentChar == '<') {
                System.out.println("(" + TokenType.LTE + "): " + currentChar);
                position++;
            } else {
                System.out.println("(" + TokenType.ILLEGAL + "): " + currentChar);
                position++;
            }
        }
    }

    public static void main(String[] args) {
        Lexer manin = new Lexer();
        manin.nextToken("asnkandf354684!#$%#$&//><<");
    }
}
