public class App {
    public static void main(String[] args) {
        Arbol miArbol = new Arbol();

        System.out.println("¿El árbol está vacío? " + miArbol.vacio());

        miArbol.insertar("Mario");
        miArbol.insertar("Isra");
        miArbol.insertar("René");
        miArbol.insertar("Yovanni");
        miArbol.insertar("Ana");
        miArbol.insertar("Yockzari");
        miArbol.insertar("Amaury");

        System.out.println("¿El árbol está vacío? " + miArbol.vacio());

        String nombreABuscar = "Tilin";
        Nodo resultado = miArbol.buscarNodo(nombreABuscar);

        if (resultado != null) {
            System.out.println("Nodo encontrado: " + resultado.nombre);
        } else {
            System.out.println("El nombre '" + nombreABuscar + "' no existe en el árbol.");
        }

        miArbol.imprimirArbol();
    }
}
