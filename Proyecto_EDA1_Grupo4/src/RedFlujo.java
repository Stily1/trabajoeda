/**
 * Red de flujo representada como red residual con listas de adyacencia
 * almacenadas en arreglos ("estrella hacia adelante").
 *
 * Cada arista (u, v, c) se guarda junto con su arista reversa (v, u, 0)
 * en posiciones consecutivas e y e^1, de modo que la reversa de la arista e
 * es simplemente e ^ 1 (operación XOR). Esto permite actualizar el flujo
 * en O(1) durante el aumento.
 *
 * Responsable: Integrante 1
 */
public class RedFlujo {
    public final int n;          // número de vértices
    public int m;                // número de aristas almacenadas (incluye reversas)
    public int[] head;           // head[u] = primera arista que sale de u (-1 si no hay)
    public int[] next;           // next[e] = siguiente arista en la lista de u
    public int[] to;             // to[e]   = vértice destino de la arista e
    public int[] cap;            // cap[e]  = capacidad residual de la arista e
    private int[] capOriginal;   // copia para reiniciar la red entre corridas

    public RedFlujo(int n, int maxAristas) {
        this.n = n;
        this.head = new int[n];
        java.util.Arrays.fill(head, -1);
        int capacidad = 2 * maxAristas;
        next = new int[capacidad];
        to = new int[capacidad];
        cap = new int[capacidad];
        m = 0;
    }

    /** Agrega la arista dirigida u -> v con capacidad c (y su reversa con capacidad 0). */
    public void agregarArista(int u, int v, int c) {
        if (m + 2 > to.length) crecer();
        to[m] = v; cap[m] = c; next[m] = head[u]; head[u] = m++;
        to[m] = u; cap[m] = 0; next[m] = head[v]; head[v] = m++;
    }

    private void crecer() {
        int nueva = Math.max(16, to.length * 2);
        next = java.util.Arrays.copyOf(next, nueva);
        to = java.util.Arrays.copyOf(to, nueva);
        cap = java.util.Arrays.copyOf(cap, nueva);
    }

    /** Número de aristas originales (sin contar reversas). */
    public int numAristas() { return m / 2; }

    /** Guarda las capacidades actuales para poder reiniciar la red. */
    public void congelar() { capOriginal = java.util.Arrays.copyOf(cap, m); }

    /** Restablece las capacidades originales (flujo cero). */
    public void reiniciar() { System.arraycopy(capOriginal, 0, cap, 0, m); }

    /** Origen de la arista e (el destino de su reversa). */
    public int origen(int e) { return to[e ^ 1]; }

    /**
     * Verifica que el flujo actual sea válido: respeta capacidades y conserva
     * el flujo en todo vértice distinto de s y t. Devuelve el valor del flujo.
     */
    public long verificarFlujo(int s, int t) {
        long[] balance = new long[n];
        for (int e = 0; e < m; e += 2) {
            int flujo = capOriginal[e] - cap[e];
            if (flujo < 0 || flujo > capOriginal[e]) throw new IllegalStateException("Capacidad violada en arista " + e);
            balance[origen(e)] -= flujo;
            balance[to[e]] += flujo;
        }
        for (int v = 0; v < n; v++)
            if (v != s && v != t && balance[v] != 0)
                throw new IllegalStateException("Conservación violada en vértice " + v);
        return balance[t];
    }

    /**
     * Calcula la capacidad del corte (S, V\S) donde S son los vértices alcanzables
     * desde s en la red residual. Por el teorema flujo máximo-corte mínimo,
     * si el flujo es máximo este valor coincide con el flujo.
     */
    public long capacidadCorteMinimo(int s) {
        boolean[] enS = new boolean[n];
        ColaEnteros cola = new ColaEnteros(n);
        enS[s] = true; cola.encolar(s);
        while (!cola.estaVacia()) {
            int u = cola.desencolar();
            for (int e = head[u]; e != -1; e = next[e])
                if (cap[e] > 0 && !enS[to[e]]) { enS[to[e]] = true; cola.encolar(to[e]); }
        }
        long corte = 0;
        for (int e = 0; e < m; e += 2)
            if (enS[origen(e)] && !enS[to[e]]) corte += capOriginal[e];
        return corte;
    }
}
