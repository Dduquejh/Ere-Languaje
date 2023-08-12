
public class Lexer {
    private int position;

    Lexer() {
        this.position = 0;
    }

    protected void nextToken(String input) {
        while (position < input.length()) {
            char currentChar = input.charAt(position);
            if (currentChar == ' ') {
                position++;
            } else if (currentChar == '=') {
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
            } else if (currentChar == '>') {
                if (position + 1 < input.length() && input.charAt(position + 1) == '=') {
                    System.out.println("(" + TokenType.GTE + "): " + currentChar + "=");
                    position += 2;
                } else {
                    System.out.println("(" + TokenType.GT + "): " + currentChar);
                    position++;
                }
            } else if (currentChar == '<') {
                if (position + 1 < input.length() && input.charAt(position + 1) == '=') {
                    System.out.println("(" + TokenType.LTE + "): " + currentChar + "=");
                    position += 2;
                } else {
                    System.out.println("(" + TokenType.LT + "): " + currentChar);
                    position++;
                }
            } else if (Character.isLetter(currentChar)) {
                String cadena = readCharacter(input, position);
                // Verificar si la cadena es una palabra clave
                if (Token.KEYWORDS.containsKey(cadena)) {
                    TokenType keywordType = Token.KEYWORDS.get(cadena);
                    System.out.println("(" + keywordType + "): " + cadena);
                } else {
                    System.out.println("(" + TokenType.IDENTIFIER + "): " + cadena);
                }
                position += cadena.length();
            } else if (Character.isDigit(currentChar)) {
                String cadena = readNumber(input, position);
                // Verificar si la cadena es un número clave
                if (Token.KEYWORDS.containsKey(cadena)) {
                    TokenType keywordType = Token.KEYWORDS.get(cadena);
                    System.out.println("(" + keywordType + "): " + cadena);
                } else {
                    System.out.println("(" + TokenType.INTEGER + "): " + cadena);
                }
                position += cadena.length();
            } else {
                System.out.println("(" + TokenType.ILLEGAL + "): " + currentChar);
                position++;
            }
        }
    }

    protected String readCharacter(String input, int position) {
        String cadena = "";
        while (position < input.length() && Character.isLetter(input.charAt(position))) {
            cadena += input.charAt(position);
            position++;
        }
        return cadena;
    }

    protected String readNumber(String input, int position) {
        String cadena = "";
        while (position < input.length() && Character.isDigit(input.charAt(position))) {
            cadena += input.charAt(position);
            position++;
        }
        return cadena;
    }
}
