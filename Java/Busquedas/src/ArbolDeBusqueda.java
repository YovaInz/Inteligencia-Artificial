import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class ArbolDeBusqueda {
    Nodo raiz = new Nodo("1238 4765"); // Estado inicial del puzzle8

    public ArbolDeBusqueda(Nodo raiz) {
        this.raiz = raiz;
    }

    Nodo busquedaPrimeroAnchura(String estadoObjetivo) {
        if (raiz == null) 
            return null;

        HashSet<String> visitados = new HashSet<>();
        Queue<Nodo> cola = new LinkedList<>();
        Nodo actual;
        cola.add(raiz);
        while(!cola.isEmpty()) {
            actual = cola.poll();
            if(actual.estado.equals(estadoObjetivo)) {
                return actual;
            } else {
                visitados.add(actual.estado);
                List<Nodo> sucesores = actual.getSucesores();
                for (Nodo sucesor : sucesores) {
                    if (!visitados.contains(sucesor.estado)) {
                        cola.add(sucesor);
                        visitados.add(sucesor.estado);
                    }
                }
            }
        }
        return null;
    }

    Nodo busquedaEnProfundidad(String estadoObjetivo) {
        if (raiz == null) 
            return null;

        HashSet<String> visitados = new HashSet<>();
        Stack<Nodo> pila = new Stack<>();
        Nodo actual;
        pila.add(raiz);
        while(!pila.isEmpty()) {
            actual = pila.pop();
            if(actual.estado.equals(estadoObjetivo)) {
                return actual;
            } else {
                visitados.add(actual.estado);
                List<Nodo> sucesores = actual.getSucesores();
                for (Nodo sucesor : sucesores) {
                    if (!visitados.contains(sucesor.estado)) {
                        pila.add(sucesor);
                        visitados.add(sucesor.estado);
                    }
                }
            }
        }
        return null;
    }

}
