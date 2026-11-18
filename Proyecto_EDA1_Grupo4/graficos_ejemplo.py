import matplotlib
matplotlib.use("Agg")
import matplotlib.pyplot as plt
pos = {"s": (0, 1), "v1": (1, 2), "v2": (1, 0), "v3": (2.2, 2), "v4": (2.2, 0), "t": (3.2, 1)}
aristas = [("s","v1",16),("s","v2",13),("v2","v1",4),("v1","v3",12),("v3","v2",9),
           ("v2","v4",14),("v4","v3",7),("v3","t",20),("v4","t",4)]
nivel = {"s":0,"v1":1,"v2":1,"v3":2,"v4":2,"t":3}
fig, axs = plt.subplots(1, 2, figsize=(10, 3.4), dpi=200)
for k, ax in enumerate(axs):
    ax.set_xlim(-0.4, 3.6); ax.set_ylim(-0.5, 2.6); ax.axis("off")
    for u, v, c in aristas:
        x1, y1 = pos[u]; x2, y2 = pos[v]
        admisible = nivel[v] == nivel[u] + 1
        if k == 0: col, ls = "#333333", "-"
        else: col, ls = ("#1F4E79", "-") if admisible else ("#BBBBBB", "--")
        ax.annotate("", xy=(x2, y2), xytext=(x1, y1),
                    arrowprops=dict(arrowstyle="-|>", color=col, lw=1.6, ls=ls,
                                    shrinkA=14, shrinkB=14, mutation_scale=14))
        mx, my = (x1 + x2) / 2, (y1 + y2) / 2
        ax.text(mx, my, str(c), fontsize=9, ha="center", va="center",
                bbox=dict(boxstyle="round,pad=0.15", fc="white", ec="none"), color=col)
    for n, (x, y) in pos.items():
        fc = "#7B1FA2" if n in ("s", "t") else "#1F4E79"
        ax.scatter([x], [y], s=650, c=fc, zorder=3)
        ax.text(x, y, n, color="white", ha="center", va="center", fontsize=10, fontweight="bold", zorder=4)
        if k == 1:
            ax.text(x, y - 0.42 if y == 0 else y + 0.36, f"nivel {nivel[n]}", ha="center", fontsize=8, color="#7B1FA2")
axs[0].set_title("(a) Red de flujo con capacidades (flujo máximo = 23)", fontsize=10)
axs[1].set_title("(b) Grafo de niveles de la 1.ª fase de Dinic", fontsize=10)
plt.tight_layout()
plt.savefig("figuras/fig_ejemplo.png", bbox_inches="tight", facecolor="white")
