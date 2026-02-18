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

    static void imprimirRuta(Nodo nodo) {
        if (nodo.padre != null) {
            // Puzzle8.imprimirHijos(new String[]{nodo.padre.estado});
            imprimirRuta(nodo.padre);
        } else {
            System.out.println("Estado inicial: " + nodo.estado);
        }
        System.out.println("Nivel: " + nodo.nivel);
        System.out.println("Costo total: " + nodo.costoTotal);
        Puzzle8.imprimirHijos(new String[]{nodo.estado});
    }
    
}
