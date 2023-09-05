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

            Token tokens = lexer.nextToken(); // Se obtiene el token

            System.out.println(tokens); // Se imprime el token
        }

        scanner.close();
    }
}
