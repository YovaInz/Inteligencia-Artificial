public class Puzzle {
    public static void main(String[] args) {
        Nodo n;
        String estadoInicial = "64215378 ";
        System.err.println("\nEstado Inicial: " + estadoInicial);
        Puzzle8.imprimirHijos(new String[]{estadoInicial});

        String estadoObjetivo = "12345678 ";
        System.err.println("Estado Objetivo: " + estadoObjetivo);
        Puzzle8.imprimirHijos(new String[]{estadoObjetivo});

        ArbolDeBusqueda arbol = new ArbolDeBusqueda(new Nodo(estadoInicial));
        // n = arbol.busquedaPrimeroAnchura(estadoObjetivo);
        // n = arbol.busquedaEnProfundidad(estadoObjetivo);
        n = arbol.busquedaPorCostoUniforme(estadoObjetivo);

        Puzzle8.imprimirHijos(new String[]{n.estado});
        System.out.println("Nivel: " + n.nivel);
        System.out.println("Ruta desde el estado inicial hasta estado objetivo:");
        imprimirRuta(n);

    }

    // static void imprimirRuta(Nodo nodo) {
    //     if (nodo.padre != null) {
    //         // Puzzle8.imprimirHijos(new String[]{nodo.padre.estado});
    //         imprimirRuta(nodo.padre);
    //     } else {
    //         System.out.println("Estado inicial: " + nodo.estado);
    //     }
    //     System.out.println("Nivel: " + nodo.nivel);
    //     System.out.println("Costo total: " + nodo.costoTotal);
    //     Puzzle8.imprimirHijos(new String[]{nodo.estado});
    // }

    static void imprimirRuta(Nodo nodo) { // Se cambió por un método que imprima mediante ciclos para evitar que la recursividad desborde la pila de llamadas
        java.util.Stack<Nodo> pila = new java.util.Stack<>();
        Nodo actual = nodo;

        while (actual != null) {
            pila.push(actual);
            actual = actual.padre;
        }

        while (!pila.isEmpty()) {
            Nodo paso = pila.pop();
            
            if (paso.padre == null) {
                System.out.println("Estado inicial: " + paso.estado);
            }
            
            System.out.println("Nivel: " + paso.nivel);
            System.out.println("Costo total: " + paso.costoTotal);
            Puzzle8.imprimirHijos(new String[]{paso.estado});
        }
    }
    
}
