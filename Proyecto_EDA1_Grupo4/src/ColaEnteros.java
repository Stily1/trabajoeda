/**
 * Cola FIFO de enteros implementada con un arreglo circular.
 * Todas las operaciones son O(1). Se usa en el BFS de Edmonds-Karp
 * y en la construcción del grafo de niveles de Dinic.
 *
 * Responsable: Integrante 1
 */
public class ColaEnteros {
    private final int[] datos;
    private int frente, fin, tam;

    public ColaEnteros(int capacidad) { datos = new int[Math.max(1, capacidad)]; }

    public void encolar(int x) {
        if (tam == datos.length) throw new IllegalStateException("Cola llena");
        datos[fin] = x;
        fin = (fin + 1 == datos.length) ? 0 : fin + 1;
        tam++;
    }

    public int desencolar() {
        if (tam == 0) throw new IllegalStateException("Cola vacía");
        int x = datos[frente];
        frente = (frente + 1 == datos.length) ? 0 : frente + 1;
        tam--;
        return x;
    }

    public boolean estaVacia() { return tam == 0; }

    public int tamano() { return tam; }

    public void vaciar() { frente = fin = tam = 0; }
}
