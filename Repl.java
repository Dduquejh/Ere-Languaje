import java.util.Scanner;
import java.util.List;
import java.util.Optional;

public class Repl {
    public static void startRepl() {
        Scanner scanner = new Scanner(System.in);
        Evaluator evaluator = new Evaluator();

        while (true) {
            System.out.print(">> ");
            String source = scanner.nextLine();

            if (source.equals("salir()")) {
                break;
            }

            Lexer lexer = new Lexer(source);
            Parser parser = new Parser(lexer);

            Program program = parser.parseProgram(parser);
            Environment env = new Environment();

            if(parser.getErrors().size() > 0){
                printParseErrors(parser.getErrors());
                continue;
            }

            Optional<Object> result = evaluator.evaluate(program, env);
            if (result.isPresent()){
                CustomObjects evaluated = (CustomObjects) result.get();
                if (evaluated != null)
                    System.out.println(evaluated.inspect());
            }
        }

        scanner.close();
    }

    private static void printParseErrors(List<String> errors){
        for (String error: errors)
            System.out.println(error);
    }
}
