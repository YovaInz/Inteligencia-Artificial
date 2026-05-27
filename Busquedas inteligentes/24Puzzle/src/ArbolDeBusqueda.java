import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

public class ArbolDeBusqueda {

    public enum Heuristica {
        MANHATTAN,
        CONFLICTO_LINEAL
    }

    Nodo raiz;

    int  nodosExpandidos = 0;   // Nodos expandidos durante la búsqueda
    int  totalCosto      = 0;   // Longitud del camino solución (número de movimientos)
    long tiempoMs        = 0;   // Tiempo de ejecución en milisegundos
    HashSet<String> estadosVisitados = new HashSet<>(); // Estados únicos generados

    public ArbolDeBusqueda(Nodo raiz) {
        this.raiz = raiz;
    }

    private void reiniciarEstadisticas() {
        nodosExpandidos  = 0;
        totalCosto       = 0;
        tiempoMs         = 0;
        estadosVisitados = new HashSet<>();
    }


    Nodo busquedaPrimeroAnchura(String estadoObjetivo) {
        if (raiz == null) return null;
        reiniciarEstadisticas();

        HashSet<String> visitados = new HashSet<>();
        Queue<Nodo> cola = new LinkedList<>();
        Nodo actual;
        cola.add(raiz);
        visitados.add(raiz.estado);
        estadosVisitados.add(raiz.estado);

        while (!cola.isEmpty()) {
            actual = cola.poll();
            nodosExpandidos++;
            if (actual.estado.equals(estadoObjetivo)) {
                totalCosto = actual.nivel;
                return actual;
            } else {
                List<Nodo> sucesores = actual.getSucesores();
                for (Nodo sucesor : sucesores) {
                    if (!visitados.contains(sucesor.estado)) {
                        cola.add(sucesor);
                        visitados.add(sucesor.estado);
                        estadosVisitados.add(sucesor.estado);
                    }
                }
            }
        }
        return null;
    }


    Nodo busquedaEnProfundidad(String estadoObjetivo) {
        if (raiz == null) return null;
        reiniciarEstadisticas();

        HashSet<String> visitados = new HashSet<>();
        Stack<Nodo> pila = new Stack<>();
        Nodo actual;
        pila.add(raiz);

        while (!pila.isEmpty()) {
            actual = pila.pop();
            if (visitados.contains(actual.estado)) continue;
            visitados.add(actual.estado);
            estadosVisitados.add(actual.estado);
            nodosExpandidos++;
            if (actual.estado.equals(estadoObjetivo)) {
                totalCosto = actual.nivel;
                return actual;
            } else {
                List<Nodo> sucesores = actual.getSucesores();
                for (Nodo sucesor : sucesores) {
                    if (!visitados.contains(sucesor.estado)) {
                        pila.add(sucesor);
                    }
                }
            }
        }
        return null;
    }

    
    Nodo busquedaPorCostoUniforme(String estadoObjetivo) {
        if (raiz == null) return null;
        reiniciarEstadisticas();

        NodoComparadorPorPrioridad comparador = new NodoComparadorPorPrioridad();
        PriorityQueue<Nodo> cola = new PriorityQueue<>(10, comparador);
        HashSet<String> visitados = new HashSet<>();
        Nodo actual;
        cola.add(raiz);

        while (!cola.isEmpty()) {
            actual = cola.poll();
            if (actual.estado.equals(estadoObjetivo)) {
                totalCosto = actual.getCostoTotal();
                return actual;
            } else {
                if (visitados.contains(actual.estado)) continue;
                visitados.add(actual.estado);
                estadosVisitados.add(actual.estado);
                nodosExpandidos++;
                List<Nodo> sucesores = actual.getSucesores();
                for (Nodo sucesor : sucesores) {
                    if (!visitados.contains(sucesor.estado)) {
                        sucesor.setCostoTotal(actual.getCostoTotal() + Character.getNumericValue(sucesor.estado.charAt(actual.estado.indexOf('0'))));
                        cola.add(sucesor);
                    }
                }
            }
        }
        return null;
    }

    // ==================================================================
    //  IDA* — Iterative Deepening A*
    // ==================================================================
    Nodo busquedaIDAStar(String estadoObjetivo, Heuristica heuristica) {
        if (raiz == null) return null;
        reiniciarEstadisticas();

        String[] tokens   = Puzzle24.parsear(estadoObjetivo);
        int[]    objetivo = new int[25];
        for (int i = 0; i < 25; i++) objetivo[i] = Integer.parseInt(tokens[i]);

        int umbral = calcularHeuristica(raiz.estado, objetivo, heuristica);
        List<Nodo> camino = new java.util.ArrayList<>();
        camino.add(raiz);
        estadosVisitados.add(raiz.estado);

        long inicio = System.currentTimeMillis();
        while (true) {
            int resultado = buscarIDA(camino, 0, umbral, objetivo, heuristica);
            if (resultado == -1) {
                tiempoMs   = System.currentTimeMillis() - inicio;
                Nodo meta  = camino.get(camino.size() - 1);
                totalCosto = meta.nivel; // g(n) = número de movimientos
                return meta;
            }
            if (resultado == Integer.MAX_VALUE) {
                tiempoMs = System.currentTimeMillis() - inicio;
                return null; // Sin solución
            }
            umbral = resultado;
            System.out.println("  [IDA*] Nuevo umbral f = " + umbral
                + " | Nodos expandidos: " + nodosExpandidos
                + " | Estados visitados: " + estadosVisitados.size());
        }
    }

    // Devuelve -1 si encontró solución, Integer.MAX_VALUE si no hay,
    // o el próximo umbral mínimo que superó el corte.
    private int buscarIDA(List<Nodo> camino, int g, int umbral,
                          int[] objetivo, Heuristica heuristica) {
        Nodo actual = camino.get(camino.size() - 1);
        int  h      = calcularHeuristica(actual.estado, objetivo, heuristica);
        int  f      = g + h;

        if (f > umbral) return f;   // Poda por umbral
        if (h == 0)     return -1;  // ¡Solución!

        nodosExpandidos++;
        int minimo = Integer.MAX_VALUE;

        for (Nodo sucesor : actual.getSucesores()) {
            if (actual.padre != null && sucesor.estado.equals(actual.padre.estado)) continue;

            estadosVisitados.add(sucesor.estado);
            sucesor.padre = actual;
            sucesor.nivel = actual.nivel + 1;
            sucesor.costoTotal = sucesor.nivel + calcularHeuristica(sucesor.estado, objetivo, heuristica); // f(n) = g(n) + h(n) = nivel + heurística
            camino.add(sucesor);

            int resultado = buscarIDA(camino, g + 1, umbral, objetivo, heuristica);
            if (resultado == -1)    return -1;
            if (resultado < minimo) minimo = resultado;

            camino.remove(camino.size() - 1);
        }

        return minimo;
    }

    // Dispatch de heurística según el enum
    private int calcularHeuristica(String estado, int[] objetivo, Heuristica heuristica) {
        return switch (heuristica) {
            case MANHATTAN        -> heuristicaManhattan(estado, objetivo);
            case CONFLICTO_LINEAL -> heuristicaConflictoLineal(estado, objetivo);
        };
    }

    // ══════════════════════════════════════════════════════════════════
    //  Heurísticas
    // ══════════════════════════════════════════════════════════════════

    // Índices del tablero 5×5:
    // [ 0][ 1][ 2][ 3][ 4]
    // [ 5][ 6][ 7][ 8][ 9]
    // [10][11][12][13][14]
    // [15][16][17][18][19]
    // [20][21][22][23][24]

    // Heurística 1 — Distancia de Hamming (piezas fuera de lugar)
    // Se cuentan las piesas distintas entre el estado actual y el objetivo.
    private int heuristicaHamming(String estadoActual, int[] objetivo) {
        String[] tokens = Puzzle24.parsear(estadoActual);
        int diferencia = 0;
        for (int i = 0; i < 25; i++) {
            int val = Integer.parseInt(tokens[i]);
            if (val != 0 && val != objetivo[i]) diferencia++;
        }
        return diferencia;
    }

    // Heurística 2 — Distancia de Manhattan
    // Para cada pieza calcula cuántos pasos (horizontal + vertical) le
    // faltan para llegar a su posición objetivo. Es admisible porque
    // cada movimiento solo desplaza una pieza un paso.
    private int heuristicaManhattan(String estadoActual, int[] objetivo) {
        // Precalcular: posicionObjetivo[valor] = índice donde debe estar ese valor
        int[] posObjetivo = new int[25]; // valores 1-24; índice 0 = blanco (ignorado)
        for (int i = 0; i < 25; i++) {
            if (objetivo[i] != 0) posObjetivo[objetivo[i]] = i;
        }

        String[] tokens = Puzzle24.parsear(estadoActual);
        int suma = 0;
        for (int i = 0; i < 25; i++) {
            int val = Integer.parseInt(tokens[i]);
            if (val == 0) continue; // El blanco no suma
            int filaActual  = i              / 5;
            int colActual   = i              % 5;
            int filaMeta    = posObjetivo[val] / 5;
            int colMeta     = posObjetivo[val] % 5;
            suma += Math.abs(filaActual - filaMeta) + Math.abs(colActual - colMeta);
        }
        return suma;
    }

    // Heurística 3 — Conflicto Lineal + Manhattan
    // Si dos piezas están en su fila/columna objetivo pero en orden
    // inverso, al menos una debe salir y regresar (+2 movimientos).
    private int heuristicaConflictoLineal(String estadoActual, int[] objetivo) {
        int[] posObjetivo = new int[25];
        for (int i = 0; i < 25; i++) {
            if (objetivo[i] != 0) posObjetivo[objetivo[i]] = i;
        }

        String[] tokens = Puzzle24.parsear(estadoActual);
        int[] estado = new int[25];
        for (int i = 0; i < 25; i++) estado[i] = Integer.parseInt(tokens[i]);

        int suma = heuristicaManhattan(estadoActual, objetivo);

        // Conflictos en filas
        for (int fila = 0; fila < 5; fila++) {
            for (int c1 = 0; c1 < 4; c1++) {
                int idx1 = fila * 5 + c1;
                int val1 = estado[idx1];
                if (val1 == 0) continue;
                if (posObjetivo[val1] / 5 != fila) continue; // val1 no pertenece a esta fila
                for (int c2 = c1 + 1; c2 < 5; c2++) {
                    int idx2 = fila * 5 + c2;
                    int val2 = estado[idx2];
                    if (val2 == 0) continue;
                    if (posObjetivo[val2] / 5 != fila) continue;
                    // Ambas en su fila objetivo: ¿están invertidas?
                    if (posObjetivo[val1] % 5 > posObjetivo[val2] % 5) suma += 2;
                }
            }
        }

        // Conflictos en columnas
        for (int col = 0; col < 5; col++) {
            for (int r1 = 0; r1 < 4; r1++) {
                int idx1 = r1 * 5 + col;
                int val1 = estado[idx1];
                if (val1 == 0) continue;
                if (posObjetivo[val1] % 5 != col) continue; // val1 no pertenece a esta columna
                for (int r2 = r1 + 1; r2 < 5; r2++) {
                    int idx2 = r2 * 5 + col;
                    int val2 = estado[idx2];
                    if (val2 == 0) continue;
                    if (posObjetivo[val2] % 5 != col) continue;
                    if (posObjetivo[val1] / 5 > posObjetivo[val2] / 5) suma += 2;
                }
            }
        }

        return suma;
    }
}
