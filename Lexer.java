public class Lexer {
    private String input;
    private int position;

    Lexer(String input) {
        this.input = input;
        this.position = 0;
    }

    protected Token nextToken() {
        Token token = null;
        while (position < input.length()) {
            char currentChar = input.charAt(position);
    
            if (currentChar == ' ') {
                position++;
            } else if (currentChar == '=') {
                token = new Token(TokenType.ASSING, String.valueOf(currentChar));
                position++;
            } else if (currentChar == '!') {
                token = new Token(TokenType.NEGATION, String.valueOf(currentChar));
                position++;
            } else if (currentChar == '+') {
                token = new Token(TokenType.PLUS, String.valueOf(currentChar));
                position++;
            } else if (currentChar == '-') {
                token = new Token(TokenType.MINUS, String.valueOf(currentChar));
                position++;
            } else if (currentChar == '/') {
                token = new Token(TokenType.DIVIDE, String.valueOf(currentChar));
                position++;
            } else if (currentChar == '*') {
                token = new Token(TokenType.MULTIPLY, String.valueOf(currentChar));
                position++;
            } else if (currentChar == '.') {
                token = new Token(TokenType.DOT, String.valueOf(currentChar));
                position++;
            } else if (currentChar == ',') {
                token = new Token(TokenType.COMMA, String.valueOf(currentChar));
                position++;
            } else if (currentChar == ';') {
                token = new Token(TokenType.SEMICOLON, String.valueOf(currentChar));
                position++;
            } else if (currentChar == '>') {
                if (position + 1 < input.length() && input.charAt(position + 1) == '=') {
                    token = new Token(TokenType.GTE,
                            String.valueOf(currentChar) + String.valueOf(input.charAt(position + 1)));
                    position += 2;
                } else {
                    token = new Token(TokenType.GT, String.valueOf(currentChar));
                    position++;
                }
            } else if (currentChar == '<') {
                if (position + 1 < input.length() && input.charAt(position + 1) == '=') {
                    token = new Token(TokenType.LTE,
                            String.valueOf(currentChar) + String.valueOf(input.charAt(position + 1)));
                    position += 2;
                } else {
                    token = new Token(TokenType.LT, String.valueOf(currentChar));
                    position++;
                }
            } else if (currentChar == '(') {
                token = new Token(TokenType.LPAREN, String.valueOf(currentChar));
                position++;
            } else if (currentChar == ')') {
                token = new Token(TokenType.RPAREN, String.valueOf(currentChar));
                position++;
            } else if (currentChar == '{') {
                token = new Token(TokenType.LBRACE, String.valueOf(currentChar));
                position++;
            } else if (currentChar == '}') {
                token = new Token(TokenType.RBRACE, String.valueOf(currentChar));
                position++;
            } else if (Character.isLetter(currentChar)) {
                String cadena = readCharacter(input, position);
                // Verificar si la cadena es una palabra clave
                if (Token.KEYWORDS.containsKey(cadena)) {
                    TokenType keywordType = Token.KEYWORDS.get(cadena);
                    token = new Token(keywordType, cadena);
                } else {
                    token = new Token(TokenType.IDENTIFIER, cadena);
                }
                position += cadena.length();
            } else if (Character.isDigit(currentChar)) {
                String cadena = readNumber(input, position);
                // Verificar si la cadena contiene un punto decimal para determinar si es un
                // float
                if (cadena.contains(".")) {
                    token = new Token(TokenType.FLOAT, cadena);
                } else {
                    token = new Token(TokenType.INTEGER, cadena);
                }
                position += cadena.length();
            } else {
                token = new Token(TokenType.ILLEGAL, String.valueOf(currentChar));
                position++;
            }
    
            if (token != null) {
                return token;
            }
        }
        return token;
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
        boolean decimalPointEncountered = false;
        boolean digitBeforeDecimal = false;

        while (position < input.length()) {
            char currentChar = input.charAt(position);
            if (Character.isDigit(currentChar)) {
                cadena += currentChar;
                if (!decimalPointEncountered) {
                    digitBeforeDecimal = true;
                }
            } else if (currentChar == '.' && !decimalPointEncountered && digitBeforeDecimal
                    && position != input.length() - 1) {
                if (Character.isDigit(input.charAt(position + 1))) {
                    cadena += currentChar;
                    decimalPointEncountered = true;
                } else {
                    // Si el punto no está entre dos dígitos paramos de leer;
                    break;
                }
            } else {
                // Si encontramos un carácter que no es dígito ni punto decimal, paramos de
                // leer.
                break;
            }
            position++;
        }
        return cadena;
    }
}
