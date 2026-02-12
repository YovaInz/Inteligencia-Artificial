import java.util.ArrayList;
import java.util.List;

public class Nodo {
    String estado;
    Nodo padre;
    int nivel;
    // int costo;


    public Nodo(String estado) {
        this.estado = estado;
    }


    public String getEstado() {
        return estado;
    }


    public Nodo getPadre() {
        return padre;
    }


    public int getNivel() {
        return nivel;
    }

    public List<Nodo> getSucesores() {
        List<Nodo> sucesores = new ArrayList<>();
        String[] estadosHijos = Puzzle8.generaSucesores(this.estado);
        for (String estadoHijo : estadosHijos) {
            Nodo nodoHijo = new Nodo(estadoHijo);
            nodoHijo.padre = this;
            nodoHijo.nivel = this.nivel + 1;
            sucesores.add(nodoHijo);
        }
        return sucesores;
    }

}
