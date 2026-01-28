public class Arbol {
    private Nodo raiz;

    public Arbol() {
        raiz = null;
    }

    public boolean vacio() {
        return raiz == null;
    }

    public void insertar(String nombre) {
        raiz = insertarRec(raiz, nombre);
    }

    private Nodo insertarRec(Nodo actual, String nombre) {
        if (actual == null) {
            return new Nodo(nombre);
        }

        if (nombre.compareTo(actual.nombre) < 0) {
            actual.izquierdo = insertarRec(actual.izquierdo, nombre);
        } else if (nombre.compareTo(actual.nombre) > 0) {
            actual.derecho = insertarRec(actual.derecho, nombre);
        }
        return actual;
    }

    public Nodo buscarNodo(String nombre) {
        return buscarNodoRec(raiz, nombre);
    }

    private Nodo buscarNodoRec(Nodo actual, String nombre) {
        if (actual == null || actual.nombre.equals(nombre)) {
            return actual;
        }

        if (nombre.compareTo(actual.nombre) < 0) {
            return buscarNodoRec(actual.izquierdo, nombre);
        } else {
            return buscarNodoRec(actual.derecho, nombre);
        }
    }

    public void imprimirArbol() {
        System.out.println("Contenido del árbol:");
        recorridoInorden(raiz, "RAIZ");
        System.out.println(); 
    }

    private void recorridoInorden(Nodo actual, String posicion) {
        if (actual != null) {
            recorridoInorden(actual.izquierdo, "IZQ"); 
            System.out.println(posicion +"\t[" + actual.nombre + "] ");
            recorridoInorden(actual.derecho, "DER");  
        }
    }
}
