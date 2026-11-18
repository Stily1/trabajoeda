/**
 * Algoritmo de Edmonds-Karp (1972): método de Ford-Fulkerson en el que cada
 * camino de aumento se busca con BFS, es decir, siempre se aumenta por el
 * camino más corto (en número de aristas) de la red residual.
 *
 * Complejidad temporal: O(V * E^2)
 *   - Hay a lo sumo O(V * E) aumentos (cada arista puede ser "crítica"
 *     O(V/2) veces porque la distancia de s a su origen crece en ≥ 2).
 *   - Cada BFS cuesta O(V + E) = O(E).
 * Complejidad espacial: O(V + E).
 *
 * Responsable: Integrante 2
 */
public class EdmondsKarp {
    private final RedFlujo red;
    private final int[] aristaPadre;   // arista por la que se llegó a cada vértice en el BFS
    private final ColaEnteros cola;

    /** Contadores para el análisis experimental. */
    public long aumentos;              // número de caminos de aumento
    public long aristasExaminadas;     // operación representativa (aristas revisadas en BFS)

    public EdmondsKarp(RedFlujo red) {
        this.red = red;
        this.aristaPadre = new int[red.n];
        this.cola = new ColaEnteros(red.n);
    }

    public long flujoMaximo(int s, int t) {
        aumentos = 0; aristasExaminadas = 0;
        long flujo = 0;
        int[] head = red.head, next = red.next, to = red.to, cap = red.cap;

        while (true) {
            // 1) BFS desde s en la red residual
            java.util.Arrays.fill(aristaPadre, -1);
            aristaPadre[s] = -2;                 // marca de visitado para la fuente
            cola.vaciar();
            cola.encolar(s);
            boolean llego = false;
            while (!cola.estaVacia() && !llego) {
                int u = cola.desencolar();
                for (int e = head[u]; e != -1; e = next[e]) {
                    aristasExaminadas++;
                    int v = to[e];
                    if (cap[e] > 0 && aristaPadre[v] == -1) {
                        aristaPadre[v] = e;
                        if (v == t) { llego = true; break; }
                        cola.encolar(v);
                    }
                }
            }
            if (!llego) break;                   // no hay camino de aumento: flujo máximo

            // 2) Capacidad cuello de botella del camino
            int cuello = Integer.MAX_VALUE;
            for (int v = t; v != s; v = to[aristaPadre[v] ^ 1])
                cuello = Math.min(cuello, cap[aristaPadre[v]]);

            // 3) Aumentar el flujo a lo largo del camino
            for (int v = t; v != s; v = to[aristaPadre[v] ^ 1]) {
                int e = aristaPadre[v];
                cap[e] -= cuello;
                cap[e ^ 1] += cuello;
            }
            flujo += cuello;
            aumentos++;
        }
        return flujo;
    }
}
