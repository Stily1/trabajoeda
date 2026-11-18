import matplotlib
matplotlib.use("Agg")
import matplotlib.pyplot as plt
from matplotlib.patches import FancyBboxPatch, FancyArrowPatch

pasos = [
    ("1. Generar\nredes", "4 familias × 5–7\ntamaños × 5 semillas"),
    ("2. Construir red\nresidual", "aristas e y e^1\n(arreglos)"),
    ("3. Calentar\nla JVM", "30 rondas sobre\nredes pequeñas"),
    ("4. Ejecutar EK\ny Dinic", "mín. de 3 corridas\nSystem.nanoTime"),
    ("5. Validar", "flujo EK = flujo Dinic\n(+ pruebas de corrección)"),
    ("6. Analizar", "promedios, log-log,\npendientes vs O(·)"),
]
fig, ax = plt.subplots(figsize=(11, 2.6), dpi=200)
ax.set_xlim(0, 6 * 1.85); ax.set_ylim(0, 2.4); ax.axis("off")
w, h = 1.55, 1.05
for i, (tit, sub) in enumerate(pasos):
    x = 0.1 + i * 1.85
    col = "#1F4E79" if i not in (3,) else "#7B1FA2"
    ax.add_patch(FancyBboxPatch((x, 1.15), w, h, boxstyle="round,pad=0.02,rounding_size=0.08",
                                fc=col, ec="none"))
    ax.text(x + w / 2, 1.15 + h / 2, tit, ha="center", va="center", color="white",
            fontsize=9, fontweight="bold", family="DejaVu Sans")
    ax.text(x + w / 2, 0.75, sub, ha="center", va="center", fontsize=8.5, color="#333333")
    if i < len(pasos) - 1:
        ax.add_patch(FancyArrowPatch((x + w + 0.02, 1.15 + h / 2), (x + 1.85 - 0.02, 1.15 + h / 2),
                                     arrowstyle="-|>", mutation_scale=14, color="#555555", lw=1.4))
ax.text(5.55, 0.2, "Si los flujos de ambos algoritmos difieren, el experimento se detiene (AssertionError).",
        ha="center", fontsize=8, style="italic", color="#555555")
plt.savefig("figuras/fig_metodo.png", bbox_inches="tight", facecolor="white")
