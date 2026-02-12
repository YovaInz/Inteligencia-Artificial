
import java.util.LinkedList;
import java.util.Queue;



public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");

        Queue<String> cola = new LinkedList<>();
        cola.add("1238 4765");
        cola.add("Mijiruto");
        cola.add("Marvin");

        System.out.println(cola.poll());
        System.out.println(cola);
        System.out.println(cola.poll());
        System.out.println(cola);

    }
}
