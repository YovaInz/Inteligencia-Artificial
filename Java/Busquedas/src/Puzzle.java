public class Puzzle {
    public static void main(String[] args) {
        String estadoInicial = "42567813 ";
        System.err.println("\nEstado Inicial: " + estadoInicial);
        Puzzle8.imprimirHijos(new String[]{estadoInicial});

        String estadoObjetivo = "12345678 ";
        System.err.println("Estado Inicial: " + estadoObjetivo);
        Puzzle8.imprimirHijos(new String[]{estadoObjetivo});

        ArbolDeBusqueda arbol = new ArbolDeBusqueda(new Nodo(estadoInicial));
        Nodo n = arbol.busquedaPrimeroAnchura(estadoObjetivo);

        Puzzle8.imprimirHijos(new String[]{n.estado});
        System.out.println("Nivel: " + n.nivel);
        System.out.println("Ruta desde el estado inicial hasta estado objetivo:");
        imprimirRuta(n);

    }

    static void imprimirRuta(Nodo nodo) {
        if (nodo.padre != null) {
            imprimirRuta(nodo.padre);
        } else {
            System.out.println("Estado inicial: " + nodo.estado);
        }
        System.out.println("Nivel: " + nodo.nivel);
        Puzzle8.imprimirHijos(new String[]{nodo.estado});
    }
    
}
