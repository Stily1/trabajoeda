import java.util.Random;

/**
 * Generadores reproducibles (por semilla) de las cuatro familias de redes
 * usadas en los experimentos. En todas, s = 0 y t = n - 1.
 *
 * Responsable: Integrante 3
 */
public class Generadores {

    /**
     * Red aleatoria dispersa: n vértices, cada vértice intermedio tiene
     * 'grado' aristas de salida a destinos aleatorios; s y t se conectan
     * con n/10 vértices cada uno. Capacidades uniformes en [1, capMax].
     * E ≈ (grado + 0,2) · n.
     */
    public static RedFlujo dispersa(int n, int grado, int capMax, long semilla) {
        Random r = new Random(semilla);
        int k = Math.max(1, n / 10);
        RedFlujo g = new RedFlujo(n, n * grado + 2 * k);
        int s = 0, t = n - 1;
        for (int u = 1; u < n - 1; u++)
            for (int j = 0; j < grado; j++) {
                int v = 1 + r.nextInt(n - 2);
                if (v != u) g.agregarArista(u, v, 1 + r.nextInt(capMax));
            }
        for (int j = 0; j < k; j++) {
            g.agregarArista(s, 1 + r.nextInt(n - 2), 1 + r.nextInt(capMax));
            g.agregarArista(1 + r.nextInt(n - 2), t, 1 + r.nextInt(capMax));
        }
        g.congelar();
        return g;
    }

    /**
     * Red aleatoria densa: cada par ordenado (u, v), u ≠ v, es arista con
     * probabilidad p. E ≈ p · n². Capacidades uniformes en [1, capMax].
     */
    public static RedFlujo densa(int n, double p, int capMax, long semilla) {
        Random r = new Random(semilla);
        RedFlujo g = new RedFlujo(n, (int) (p * n * n * 1.1) + 16);
        for (int u = 0; u < n; u++) {
            if (u == n - 1) continue;              // t no tiene aristas de salida
            for (int v = 1; v < n; v++)            // s no tiene aristas de entrada
                if (u != v && r.nextDouble() < p) g.agregarArista(u, v, 1 + r.nextInt(capMax));
        }
        g.congelar();
        return g;
    }

    /**
     * Emparejamiento bipartito: k trabajadores (izquierda) y k tareas (derecha);
     * cada trabajador es compatible con 'grado' tareas aleatorias.
     * Todas las capacidades son 1. n = 2k + 2, E = k·grado + 2k.
     */
    public static RedFlujo bipartita(int k, int grado, long semilla) {
        Random r = new Random(semilla);
        int n = 2 * k + 2, s = 0, t = n - 1;
        RedFlujo g = new RedFlujo(n, k * grado + 2 * k);
        for (int i = 1; i <= k; i++) g.agregarArista(s, i, 1);
        for (int i = 1; i <= k; i++)
            for (int j = 0; j < grado; j++)
                g.agregarArista(i, k + 1 + r.nextInt(k), 1);
        for (int j = k + 1; j <= 2 * k; j++) g.agregarArista(j, t, 1);
        g.congelar();
        return g;
    }

    /**
     * Malla (grilla) de lado L: L × L celdas con aristas a los 4 vecinos
     * (capacidad [1, capMax]); s se conecta a la primera columna y la última
     * columna a t. Modela redes viales o de distribución y los grafos usados
     * en visión por computador. n = L² + 2, E ≈ 4L².
     */
    public static RedFlujo malla(int L, int capMax, long semilla) {
        Random r = new Random(semilla);
        int n = L * L + 2, s = 0, t = n - 1;
        RedFlujo g = new RedFlujo(n, 4 * L * L + 2 * L);
        int[] dr = {-1, 0, 1, 0}, dc = {0, 1, 0, -1};
        for (int i = 0; i < L; i++)
            for (int j = 0; j < L; j++) {
                int u = 1 + i * L + j;
                for (int d = 0; d < 4; d++) {
                    int ni = i + dr[d], nj = j + dc[d];
                    if (ni >= 0 && ni < L && nj >= 0 && nj < L)
                        g.agregarArista(u, 1 + ni * L + nj, 1 + r.nextInt(capMax));
                }
            }
        for (int i = 0; i < L; i++) {
            g.agregarArista(s, 1 + i * L, capMax * 4);
            g.agregarArista(1 + i * L + (L - 1), t, capMax * 4);
        }
        g.congelar();
        return g;
    }
}
