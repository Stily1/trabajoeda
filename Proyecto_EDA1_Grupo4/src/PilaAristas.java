/**
 * Pila LIFO de enteros (índices de aristas) sobre un arreglo.
 * Se usa en el DFS iterativo de Dinic para guardar el camino actual
 * desde s; evita la recursión profunda (que en mallas grandes
 * desbordaría la pila de llamadas de Java).
 *
 * Responsable: Integrante 1
 */
public class PilaAristas {
    private final int[] datos;
    private int tope;

    public PilaAristas(int capacidad) { datos = new int[Math.max(1, capacidad)]; }

    public void apilar(int e) { datos[tope++] = e; }

    public int desapilar() { return datos[--tope]; }

    public int cima() { return datos[tope - 1]; }

    public boolean estaVacia() { return tope == 0; }

    public int tamano() { return tope; }

    /** Acceso de solo lectura a la posición i (0 = fondo). */
    public int get(int i) { return datos[i]; }

    /** Recorta la pila para que queden solo las primeras k aristas. */
    public void recortar(int k) { tope = k; }

    public void vaciar() { tope = 0; }
}
