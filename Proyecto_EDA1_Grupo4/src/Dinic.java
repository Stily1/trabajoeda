/**
 * Algoritmo de Dinic (Dinitz, 1970) en la versión de Even e Itai.
 * Trabaja por fases. En cada fase:
 *   1) Un BFS desde s construye el grafo de niveles (level[v] = distancia a s).
 *   2) Un DFS con "puntero de arista actual" (it[]) encuentra un flujo
 *      bloqueante usando solo aristas u->v con level[v] = level[u] + 1.
 * La distancia de s a t en la red residual crece en cada fase, por lo que
 * hay a lo sumo V - 1 fases.
 *
 * Complejidad temporal: O(V^2 * E) en general
 *   - ≤ V - 1 fases; cada flujo bloqueante cuesta O(V * E).
 *   - Con capacidades unitarias: O(E * min(V^(2/3), E^(1/2))) (Even y Tarjan, 1975);
 *     en emparejamiento bipartito: O(E * sqrt(V)) (equivale a Hopcroft-Karp).
 * Complejidad espacial: O(V + E).
 *
 * El DFS se implementa de forma iterativa con una pila explícita de aristas
 * (PilaAristas) para no depender de la recursión.
 *
 * Responsable: Integrante 3
 */
public class Dinic {
    private final RedFlujo red;
    private final int[] level;     // nivel (distancia BFS) de cada vértice
    private final int[] it;        // puntero de arista actual por vértice
    private final ColaEnteros cola;
    private final PilaAristas camino;

    /** Contadores para el análisis experimental. */
    public long fases;
    public long aumentos;
    public long aristasExaminadas;

    public Dinic(RedFlujo red) {
        this.red = red;
        this.level = new int[red.n];
        this.it = new int[red.n];
        this.cola = new ColaEnteros(red.n);
        this.camino = new PilaAristas(red.n);
    }

    /** Construye el grafo de niveles. Devuelve true si t es alcanzable. */
    private boolean bfs(int s, int t) {
        int[] head = red.head, next = red.next, to = red.to, cap = red.cap;
        java.util.Arrays.fill(level, -1);
        level[s] = 0;
        cola.vaciar();
        cola.encolar(s);
        while (!cola.estaVacia()) {
            int u = cola.desencolar();
            for (int e = head[u]; e != -1; e = next[e]) {
                aristasExaminadas++;
                int v = to[e];
                if (cap[e] > 0 && level[v] < 0) {
                    level[v] = level[u] + 1;
                    cola.encolar(v);
                }
            }
        }
        return level[t] >= 0;
    }

    /** Encuentra un flujo bloqueante en el grafo de niveles (DFS iterativo). */
    private long flujoBloqueante(int s, int t) {
        int[] next = red.next, to = red.to, cap = red.cap;
        long total = 0;
        camino.vaciar();
        int u = s;
        while (true) {
            if (u == t) {
                // Se encontró un camino s -> t: calcular cuello de botella y aumentar
                int cuello = Integer.MAX_VALUE;
                for (int i = 0; i < camino.tamano(); i++)
                    cuello = Math.min(cuello, cap[camino.get(i)]);
                int primeraSaturada = -1;
                for (int i = 0; i < camino.tamano(); i++) {
                    int e = camino.get(i);
                    cap[e] -= cuello;
                    cap[e ^ 1] += cuello;
                    if (cap[e] == 0 && primeraSaturada < 0) primeraSaturada = i;
                }
                total += cuello;
                aumentos++;
                // Retroceder hasta el origen de la primera arista saturada
                int eSat = camino.get(primeraSaturada);
                camino.recortar(primeraSaturada);
                u = to[eSat ^ 1];
                continue;
            }
            // Avanzar: buscar una arista admisible desde u
            int e = it[u];
            while (e != -1) {
                aristasExaminadas++;
                if (cap[e] > 0 && level[to[e]] == level[u] + 1) break;
                e = next[e];
            }
            it[u] = e;
            if (e != -1) {
                camino.apilar(e);
                u = to[e];
            } else {
                // Callejón sin salida: u ya no sirve en esta fase
                if (u == s) break;
                level[u] = -1;
                int ePadre = camino.desapilar();
                u = to[ePadre ^ 1];
                it[u] = next[it[u]];   // descartar la arista que llevó al callejón
            }
        }
        return total;
    }

    public long flujoMaximo(int s, int t) {
        fases = 0; aumentos = 0; aristasExaminadas = 0;
        long flujo = 0;
        while (bfs(s, t)) {
            System.arraycopy(red.head, 0, it, 0, red.n);   // reiniciar punteros
            flujo += flujoBloqueante(s, t);
            fases++;
        }
        return flujo;
    }
}
