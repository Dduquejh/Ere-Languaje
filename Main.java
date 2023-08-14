public class Main {
    public static void main(String[] args) {
        pantallaBienvenida();
        Repl.starRepel();
    }

    public static void pantallaBienvenida() {
        System.out.println("Bienvenido al lenguaje");
        String[] letras = {
            "*****", "*    ", "*****", "*    ", "*    ", "*****",   // E
            "**** ", "*   *", "**** ", "*  * ", "*   *", "*   *"    // R
        };

        String mensaje = "ERE";
        for (int fila = 0; fila < 6; fila++) {
            for (char letra : mensaje.toCharArray()) {
                int indice;
                if (letra == 'E') {
                    indice = 0;
                } else if (letra == 'R') {
                    indice = 1;
                } else {
                    continue; // Caracter no reconocido, omite
                }
                System.out.print(letras[indice * 6 + fila] + "  ");
            }
            System.out.println();
        }
    }
}
