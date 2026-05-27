public class Puzzle24 {
    // Estado objetivo del Puzzle 24:
    // | 1| 2| 3| 4| 5|
    // | 6| 7| 8| 9|10|
    // |11|12|13|14|15|
    // |16|17|18|19|20|
    // |21|22|23|24|  |
    static final String estadoFinal = "1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,0";


    static String[] parsear(String estado) {
        return estado.split(",");
    }

    static String unir(String[] tokens) {
        return String.join(",", tokens);
    }

    // Intercambia dos posiciones del arreglo y devuelve el nuevo estado como String
    static String intercambiar(String[] tokens, int i, int j) {
        String[] copia = tokens.clone();
        String temp = copia[i];
        copia[i]    = copia[j];
        copia[j]    = temp;
        return unir(copia);
    }

    // Devuelve el índice (0-24) donde está el espacio vacío ("0")
    static int encontrarBlanco(String[] tokens) {
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].equals("0")) return i;
        }
        throw new IllegalStateException("Estado inválido: no se encontró el espacio vacío (0)");
    }

    // ─────────────────────────────────────────────
    //  Generación de sucesores
    // ─────────────────────────────────────────────

    // Genera los estados sucesores del estado actual.
    // Movimientos válidos del espacio vacío: arriba (-5), abajo (+5), izquierda (-1), derecha (+1).
    // La cantidad de sucesores depende de la posición del blanco en el tablero (2, 3 o 4).
    static String[] generaSucesores(String estadoActual) {
        String[] tokens = parsear(estadoActual);
        int indice = encontrarBlanco(tokens);
        int fila   = indice / 5;
        int col    = indice % 5;

        java.util.List<String> hijos = new java.util.ArrayList<>(4);

        if (fila > 0)   hijos.add(intercambiar(tokens, indice, indice - 5)); // Arriba
        if (fila < 4)   hijos.add(intercambiar(tokens, indice, indice + 5)); // Abajo
        if (col  > 0)   hijos.add(intercambiar(tokens, indice, indice - 1)); // Izquierda
        if (col  < 4)   hijos.add(intercambiar(tokens, indice, indice + 1)); // Derecha

        return hijos.toArray(new String[0]);
    }

    // ─────────────────────────────────────────────
    //  Impresión
    // ─────────────────────────────────────────────

    static void imprimirHijos(String[] hijos) {
        for (String hijo : hijos) {
            String[] tokens = parsear(hijo);
            System.out.println("+----+----+----+----+----+");
            for (int i = 0; i < 25; i++) {
                if (tokens[i].equals("0"))
                    System.out.print("|    ");
                else
                    System.out.printf("| %2s ", tokens[i]);
                if ((i + 1) % 5 == 0) {
                    System.out.println("|");
                    System.out.println("+----+----+----+----+----+");
                }
            }
            System.out.println(hijo + "\n");
        }
    }
}
