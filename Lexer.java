
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
                String cadena = readCharacter(input, position);
                System.out.println("(" + TokenType.IDENTIFIER + "): " + cadena);
                position += cadena.length();
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

    protected String readCharacter (String input, int position){
        String cadena = "";
        while (position < input.length() && Character.isLetter(input.charAt(position))) {
            cadena += input.charAt(position);
            position++;
        }
        return cadena;
    }
}
