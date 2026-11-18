/**
 * Pruebas de corrección:
 *  1) Ejemplo clásico de Cormen et al. (flujo máximo conocido = 23).
 *  2) 2 000 redes aleatorias pequeñas: ambos algoritmos deben dar el mismo
 *     valor, el flujo debe ser válido (capacidades y conservación) y debe
 *     coincidir con la capacidad del corte mínimo (teorema de Ford-Fulkerson).
 *
 * Ejecutar:  java PruebasCorreccion
 * Responsable: Integrante 4
 */
public class PruebasCorreccion {
    public static void main(String[] args) {
        // --- 1) Ejemplo de libro (CLRS, fig. 26.1): s=0, v1..v4 = 1..4, t=5 ---
        RedFlujo g = new RedFlujo(6, 10);
        g.agregarArista(0, 1, 16); g.agregarArista(0, 2, 13);
        g.agregarArista(2, 1, 4);  g.agregarArista(1, 3, 12);
        g.agregarArista(3, 2, 9);  g.agregarArista(2, 4, 14);
        g.agregarArista(4, 3, 7);  g.agregarArista(3, 5, 20);
        g.agregarArista(4, 5, 4);
        g.congelar();
        long ek = new EdmondsKarp(g).flujoMaximo(0, 5);
        g.reiniciar();
        long di = new Dinic(g).flujoMaximo(0, 5);
        System.out.printf("Ejemplo CLRS: Edmonds-Karp = %d, Dinic = %d (esperado 23)%n", ek, di);
        if (ek != 23 || di != 23) throw new AssertionError("Ejemplo CLRS incorrecto");

        // --- 2) Pruebas aleatorias ---
        int casos = 0;
        for (long semilla = 1; semilla <= 500; semilla++) {
            RedFlujo[] redes = {
                Generadores.dispersa(10 + (int) (semilla % 60), 3, 50, semilla),
                Generadores.densa(5 + (int) (semilla % 30), 0.3, 20, semilla),
                Generadores.bipartita(3 + (int) (semilla % 40), 3, semilla),
                Generadores.malla(2 + (int) (semilla % 9), 10, semilla)
            };
            for (RedFlujo red : redes) {
                int s = 0, t = red.n - 1;
                red.reiniciar();
                long a = new EdmondsKarp(red).flujoMaximo(s, t);
                long fa = red.verificarFlujo(s, t);
                long ca = red.capacidadCorteMinimo(s);
                red.reiniciar();
                long b = new Dinic(red).flujoMaximo(s, t);
                long fb = red.verificarFlujo(s, t);
                long cb = red.capacidadCorteMinimo(s);
                if (a != b || a != fa || b != fb || a != ca || b != cb)
                    throw new AssertionError("Discrepancia en semilla " + semilla +
                        ": EK=" + a + " Dinic=" + b + " corteEK=" + ca + " corteDinic=" + cb);
                casos++;
            }
        }
        System.out.println("Pruebas aleatorias superadas: " + casos + " redes "
            + "(mismo flujo, flujo válido y flujo = corte mínimo).");
    }
}
