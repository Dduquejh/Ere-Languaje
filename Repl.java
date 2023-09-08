import java.util.Scanner;

public class Repl {
    public static void startRepl() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print(">> ");
            String source = scanner.nextLine();

            if (source.equals("salir()")) {
                break;
            }

            Lexer lexer = new Lexer(source);
            Parser parser = new Parser(lexer);

            Program program = parser.parseProgram(parser);
            Token token;
            do {
                token = lexer.nextToken();
                if (token != null) {
                    System.out.println(token);
                    System.out.println(program);
                }
                if (!parser.getErrors().isEmpty()) {
                    for (String error : parser.getErrors()) {
                        System.out.println(error);
                    }
                } else {
                    for (Statement statement : program.getStatements()) {
                        System.out.println(statement.toString());
                    }
                }
            } while (token != null && token.getType() != TokenType.EOF);
        }

        scanner.close();
    }
}

