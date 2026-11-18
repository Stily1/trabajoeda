import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

/**
 * Experimento principal. Para cada familia de redes y cada tamaño genera
 * SEMILLAS instancias; cada algoritmo se ejecuta REPETICIONES veces sobre la
 * misma instancia (reiniciando la red) y se registra el menor tiempo.
 * Antes de medir se hace un calentamiento para estabilizar el compilador JIT.
 * Las corridas que superan 1 s no se repiten. Si el tiempo medio de
 * Edmonds-Karp en un tamaño supera LIMITE_MS (5 s), se omiten
 * para él los tamaños mayores de esa familia (Dinic sí continúa).
 *
 * Salida: resultados/resultados_raw.csv
 * Ejecutar:  java -Xmx4g Experimento [rápido]
 *
 * Responsable: Integrante 4
 */
public class Experimento {
    static int SEMILLAS = 5;
    static int REPETICIONES = 3;
    static final double LIMITE_MS = 5_000;

    static PrintWriter out;

    interface Fabrica { RedFlujo crear(int tam, long semilla); }

    public static void main(String[] args) throws IOException {
        Locale.setDefault(Locale.US);
        boolean rapido = args.length > 0;
        if (rapido) { SEMILLAS = 2; REPETICIONES = 1; }
        new java.io.File("../resultados").mkdirs();
        out = new PrintWriter(new FileWriter("../resultados/resultados_raw.csv"));
        out.println("familia,tam,V,E,semilla,algoritmo,tiempo_ms,flujo,aumentos,fases,aristas_examinadas");

        calentar();

        correrFamilia("dispersa", new int[]{1000, 2000, 4000, 8000, 16000, 32000, 64000},
            (n, sem) -> Generadores.dispersa(n, 5, 1000, sem));
        correrFamilia("densa", new int[]{100, 200, 400, 800, 1600},
            (n, sem) -> Generadores.densa(n, 0.25, 1000, sem));
        correrFamilia("bipartita", new int[]{1000, 2000, 4000, 8000, 16000, 32000, 64000},
            (k, sem) -> Generadores.bipartita(k, 4, sem));
        correrFamilia("malla", new int[]{25, 50, 100, 200, 400},
            (L, sem) -> Generadores.malla(L, 100, sem));

        out.close();
        System.out.println("Listo: resultados/resultados_raw.csv");
    }

    static void calentar() {
        for (int i = 0; i < 30; i++) {
            RedFlujo[] redes = {
                Generadores.dispersa(2000, 5, 1000, 1000 + i),
                Generadores.bipartita(1000, 4, 1000 + i),
                Generadores.malla(30, 100, 1000 + i),
                Generadores.densa(100, 0.25, 1000, 1000 + i)
            };
            for (RedFlujo g : redes) {
                g.reiniciar(); new EdmondsKarp(g).flujoMaximo(0, g.n - 1);
                g.reiniciar(); new Dinic(g).flujoMaximo(0, g.n - 1);
            }
        }
    }

    static void correrFamilia(String familia, int[] tamanos, Fabrica fab) {
        boolean ekActivo = true;
        for (int tam : tamanos) {
            double sumaEK = 0; int corridasEK = 0;
            for (int sem = 1; sem <= SEMILLAS; sem++) {
                RedFlujo g = fab.crear(tam, sem);
                int s = 0, t = g.n - 1;

                // Dinic
                long mejor = Long.MAX_VALUE, flujoD = 0; Dinic d = null;
                for (int r = 0; r < REPETICIONES; r++) {
                    g.reiniciar();
                    d = new Dinic(g);
                    long t0 = System.nanoTime();
                    flujoD = d.flujoMaximo(s, t);
                    mejor = Math.min(mejor, System.nanoTime() - t0);
                    if (mejor / 1e6 > 1000) break;            // no repetir corridas largas
                }
                registrar(familia, tam, g, sem, "Dinic", mejor, flujoD, d.aumentos, d.fases, d.aristasExaminadas);

                // Edmonds-Karp
                if (ekActivo) {
                    mejor = Long.MAX_VALUE; long flujoE = 0; EdmondsKarp ek = null;
                    for (int r = 0; r < REPETICIONES; r++) {
                        g.reiniciar();
                        ek = new EdmondsKarp(g);
                        long t0 = System.nanoTime();
                        flujoE = ek.flujoMaximo(s, t);
                        mejor = Math.min(mejor, System.nanoTime() - t0);
                        if (mejor / 1e6 > 1000) break;            // no repetir corridas largas
                    }
                    if (flujoE != flujoD)
                        throw new AssertionError("Flujos distintos en " + familia + " tam=" + tam + " sem=" + sem);
                    registrar(familia, tam, g, sem, "Edmonds-Karp", mejor, flujoE, ek.aumentos, ek.aumentos, ek.aristasExaminadas);
                    sumaEK += mejor / 1e6; corridasEK++;
                }
            }
            System.out.printf("%-10s tam=%-6d  EK medio=%.1f ms%n", familia, tam, corridasEK > 0 ? sumaEK / corridasEK : -1);
            if (ekActivo && corridasEK > 0 && sumaEK / corridasEK > LIMITE_MS) ekActivo = false;
        }
    }

    static void registrar(String fam, int tam, RedFlujo g, int sem, String alg, long ns,
                          long flujo, long aumentos, long fases, long examinadas) {
        out.printf("%s,%d,%d,%d,%d,%s,%.4f,%d,%d,%d,%d%n", fam, tam, g.n, g.numAristas(), sem, alg,
            ns / 1e6, flujo, aumentos, fases, examinadas);
        out.flush();
    }
}
