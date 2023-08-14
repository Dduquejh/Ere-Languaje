import java.util.List;
import java.util.Scanner;

public class Repl {
    public static void starRepel() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print(">> ");
            String source = scanner.nextLine();

            if (source.equals("salir()")) {
                break;
            }

            Lexer lexer = new Lexer(source);

            List<Token> tokens = lexer.nextToken(); // Obtener la lista de tokens

            for (Token token : tokens) {
                if (token.getType() == TokenType.EOF) { // Verificar si es el token de EOF
                    break;
                }
            System.out.println(token);
            }
        }

        scanner.close();
    }
}
