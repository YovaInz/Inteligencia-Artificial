public class Puzzle {

    public static void main(String[] args) {
        Nodo n;

        // ==============================================================
        //  ESTADO INICIAL - Descomenta el caso que quieras probar
        // ==============================================================
        //
        //  Tablero de referencia (índices):
        //  [ 0][ 1][ 2][ 3][ 4]
        //  [ 5][ 6][ 7][ 8][ 9]
        //  [10][11][12][13][14]
        //  [15][16][17][18][19]
        //  [20][21][22][23][24]

        // EASY - Blanco cerca del objetivo (~10 movimientos)
        // | 1| 2| 3| 4| 5|       | 1| 2| 3| 4| 5|
        // | 6| 7| 8| 9|10|  -->  | 6| 7| 8| 9|10|
        // |11|12|13|14|15|       |11|12|13|14|15|
        // |16|17|18|19|20|       |16|17|18|19|20|
        // |21|22|23|  |24|       |21|22|23|24|  |
        // String estadoInicial = "1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,0,24";

        // MEDIUM - Segunda fila y parte de la tercera desordenadas (~30 movimientos)
        // | 1| 2| 3| 4| 5|
        // | 6|  | 8| 7|10|
        // |11| 9|12|13|14|
        // |16|17|18|19|20|
        // |21|22|23|24|15|
        String estadoInicial = "1,2,3,4,5,6,0,8,7,10,11,9,12,13,14,16,17,18,19,20,21,22,23,24,15";

        // HARD - Estado muy desordenado (~50+ movimientos)
        // | 5| 1| 3|10| 4|
        // | 2| 7| 8| 9| 6|
        // |11|12|  |13|14|
        // |16|17|15|18|20|
        // |21|22|23|19|24|
        // String estadoInicial = "5,1,3,10,4,2,7,8,9,6,11,12,0,13,14,16,17,15,18,20,21,22,23,19,24";
        // String estadoInicial = "7,12,4,6,21,11,9,16,15,10,13,20,19,2,14,23,3,17,1,8,5,22,24,18,0";

        // ==============================================================
        //  ESTADO OBJETIVO (fijo)
        // ==============================================================
        String estadoObjetivo = Puzzle24.estadoFinal;

        // --- Imprimir estados de referencia ---------------------------
        System.out.println("\nEstado Inicial:");
        Puzzle24.imprimirHijos(new String[]{estadoInicial});

        System.out.println("Estado Objetivo:");
        Puzzle24.imprimirHijos(new String[]{estadoObjetivo});

        // ==============================================================
        //  SELECCIÓN DE ALGORITMO
        // ==============================================================
        // ArbolDeBusqueda arbol = new ArbolDeBusqueda(new Nodo(estadoInicial));
        
        // n = arbol.busquedaPrimeroAnchura(estadoObjetivo);
        // // n = arbol.busquedaEnProfundidad(estadoObjetivo);
        // // n = arbol.busquedaPorCostoUniforme(estadoObjetivo);

        // Puzzle24.imprimirHijos(new String[]{n.estado});
        // System.out.println("Ruta desde el estado inicial hasta estado objetivo:");
        // System.out.println("Nivel: " + n.nivel);
        // printSolution(n, arbol.estadosVisitados, arbol.nodosExpandidos, new Nodo(estadoInicial), arbol.tiempoMs);


        // IDA* con una sola heurística (comentar/descomentar):
        // ArbolDeBusqueda arbol = new ArbolDeBusqueda(new Nodo(estadoInicial));
        // n = arbol.busquedaIDAStar(estadoObjetivo, ArbolDeBusqueda.Heuristica.MANHATTAN);
        // printSolution(n, arbol.estadosVisitados, arbol.nodosExpandidos, new Nodo(estadoInicial), arbol.tiempoMs);

        // ArbolDeBusqueda arbol = new ArbolDeBusqueda(new Nodo(estadoInicial));
        // n = arbol.busquedaIDAStar(estadoObjetivo, ArbolDeBusqueda.Heuristica.CONFLICTO_LINEAL);
        // printSolution(n, arbol.estadosVisitados, arbol.nodosExpandidos, new Nodo(estadoInicial), arbol.tiempoMs);

        // ==============================================================
        //  TABLA COMPARATIVA - Correr ambas heurísticas automáticamente
        // ==============================================================
        compararHeuristicas(estadoInicial, estadoObjetivo);
    }

    static void printSolution(Nodo goalNode, java.util.Set<String> visitedNodes, int expandidos,
                              Nodo root, long time) {
        if (goalNode == null) {
            System.out.println("No se encontró solución.");
            return;
        }

        // Reconstruir el camino con un Stack (evita desbordamiento recursivo)
        java.util.Stack<Nodo> solutionStack = new java.util.Stack<>();
        Nodo temp = goalNode;
        while (temp != null && !temp.getEstado().equals(root.getEstado())) {
            solutionStack.push(temp);
            temp = temp.getPadre();
        }
        solutionStack.push(root);

        String sourceState      = root.getEstado();
        String destinationState;
        int cost      = 0;
        int totalCost = 0;

        for (int i = solutionStack.size() - 1; i >= 0; i--) {
            System.out.println("════════════════════════════════════════════════");
            destinationState = solutionStack.get(i).getEstado();

            if (!sourceState.equals(destinationState)) {
                String piezaMovida = encontrarPiezaMovida(sourceState, destinationState);
                System.out.println("Movimiento: pieza " + piezaMovida
                    + "  ->  " + findTransition(destinationState, sourceState));
                cost = 1;
                totalCost += cost;
            }
            System.out.println("Costo del movimiento: " + cost);
            System.out.println("Costo acumulado: " + totalCost);
            Puzzle24.imprimirHijos(new String[]{destinationState});

            sourceState = destinationState;
            cost = 0;
        }

        System.out.println("════════════════════════════════════════════════");
        System.out.println("** Numero de estados visitados:  " + visitedNodes.size());
        System.out.println("** Costo total de la solucion:   " + goalNode.getCostoTotal());
        System.out.println("** Longitud de la solución (número de movimientos): " + goalNode.getNivel());
        System.out.println("** Nodos expandidos:             " + expandidos);
        System.out.println("** Tiempo de ejecucion:          " + time + " ms");
        System.out.println("════════════════════════════════════════════════");
    }

    static String findTransition(String source, String destination) {
        String[] src  = Puzzle24.parsear(source);
        String[] dest = Puzzle24.parsear(destination);

        int blancoSrc  = -1;
        int blancoDest = -1;
        for (int i = 0; i < 25; i++) {
            if (src[i].equals("0"))  blancoSrc  = i;
            if (dest[i].equals("0")) blancoDest = i;
        }
        if ((blancoSrc/5) < (blancoDest/5)) {
            return "Abajo     (fila " + (blancoSrc/5) + " -> " + (blancoDest/5) + ")";
        } 
        else if ((blancoSrc/5) > (blancoDest/5)) {
            return "Arriba    (fila " + (blancoSrc/5) + " -> " + (blancoDest/5) + ")";
        }
        else if ((blancoSrc%5) < (blancoDest%5)) {
            return "Derecha   (col "  + (blancoSrc%5) + " -> " + (blancoDest%5) + ")";
        }
        else if ((blancoSrc%5) > (blancoDest%5)) {
            return "Izquierda (col "  + (blancoSrc%5) + " -> " + (blancoDest%5) + ")";
        }

        int diff = blancoDest - blancoSrc;
        return "Desconocido (diff=" + diff + ")";
    }

    // Devuelve el valor de la pieza que se desplazó entre dos estados
    static String encontrarPiezaMovida(String source, String destination) {
        String[] src  = Puzzle24.parsear(source);
        String[] dest = Puzzle24.parsear(destination);
        for (int i = 0; i < 25; i++) {
            if (dest[i].equals("0")) return src[i]; // La pieza que ocupaba el blanco en dest
        }
        return "?";
    }

    static void compararHeuristicas(String estadoInicial, String estadoObjetivo) {
        ArbolDeBusqueda.Heuristica[] heuristicas = {
            ArbolDeBusqueda.Heuristica.MANHATTAN,
            ArbolDeBusqueda.Heuristica.CONFLICTO_LINEAL
        };

        String[]          nombres    = {"Manhattan", "Conflicto Lineal"};
        int[]             movs       = new int[2];
        int[]             visitados  = new int[2];
        int[]             expandidos = new int[2];
        long[]            tiempos    = new long[2];
        Nodo[]            soluciones = new Nodo[2];
        ArbolDeBusqueda[] arboles    = new ArbolDeBusqueda[2];

        for (int i = 0; i < 2; i++) {
            System.out.println("\n==================================================");
            System.out.println("  Ejecutando IDA* con heuristica: " + nombres[i]);
            System.out.println("==================================================");
            arboles[i]    = new ArbolDeBusqueda(new Nodo(estadoInicial));
            soluciones[i] = arboles[i].busquedaIDAStar(estadoObjetivo, heuristicas[i]);

            if (soluciones[i] != null) {
                movs[i]       = soluciones[i].nivel;
                visitados[i]  = arboles[i].estadosVisitados.size();
                expandidos[i] = arboles[i].nodosExpandidos;
                tiempos[i]    = arboles[i].tiempoMs;
            }
        }

        for (int i = 0; i < 2; i++) {
            System.out.println("\n\n==================================================");
            System.out.println("  SOLUCION - Heuristica: " + nombres[i]);
            System.out.println("==================================================");
            printSolution(soluciones[i], arboles[i].estadosVisitados, expandidos[i],
                          new Nodo(estadoInicial), tiempos[i]);
        }

        System.out.println("\n");
        System.out.println("================================================================");
        System.out.println("         TABLA COMPARATIVA DE HEURISTICAS - IDA*               ");
        System.out.println("================================================================");
        System.out.printf("%-20s | %-13s | %-14s | %-11s%n",
            "Heuristica", "Movimientos", "Nodos exp.", "Tiempo (ms)");
        System.out.println("----------------------------------------------------------------");
        for (int i = 0; i < 2; i++) {
            if (soluciones[i] != null) {
                System.out.printf("%-20s | %-13d | %-14d | %-11d%n",
                    nombres[i], movs[i], expandidos[i], tiempos[i]);
            } else {
                System.out.printf("%-20s | %-13s | %-14s | %-11s%n",
                    nombres[i], "N/A", "N/A", "N/A");
            }
        }
        System.out.println("================================================================");

        if (soluciones[0] != null && soluciones[1] != null) {
            System.out.println();
            if (expandidos[1] < expandidos[0]) {
                System.out.printf("-> Conflicto Lineal expandio %.1f%% menos nodos que Manhattan.%n",
                    100.0 * (expandidos[0] - expandidos[1]) / expandidos[0]);
            } else {
                System.out.printf("-> Manhattan expandio %.1f%% menos nodos que Conflicto Lineal.%n",
                    100.0 * (expandidos[1] - expandidos[0]) / expandidos[1]);
            }
            if (movs[0] < movs[1]) {
                System.out.println("-> Manhattan encontró una solución más corta en " + movs[0] + " movimientos.");
            } else if (movs[1] < movs[0]) {
                System.out.println("-> Conflicto Lineal encontró una solución más corta que Manhattan en " + movs[1]+ " movimientos.");
            } else {
                System.out.println("-> Ambas encontraron la solucion optima en " + movs[1] + " movimientos.");
            }
        }
    }
}
