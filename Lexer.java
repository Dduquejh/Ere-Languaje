
public class Lexer {
    private String input;
    private int position;

    Lexer(String input) {
        this.input = input;
        this.position = 0;
    }

    protected void nextToken() {
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
}
