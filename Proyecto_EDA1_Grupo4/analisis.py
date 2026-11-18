"""Análisis de resultados: tablas resumen y figuras.
Lee resultados/resultados_raw.csv y escribe resultados/resumen.csv,
resultados/pendientes.csv y las figuras en figuras/.
Responsable: Integrante 4
"""
import numpy as np
import pandas as pd
import matplotlib
matplotlib.use("Agg")
import matplotlib.pyplot as plt

COL = {"Edmonds-Karp": "#D9731E", "Dinic": "#2A6FBB"}
MARK = {"Edmonds-Karp": "s", "Dinic": "o"}
FAM = ["dispersa", "densa", "bipartita", "malla"]
TIT = {"dispersa": "(a) Aleatoria dispersa", "densa": "(b) Aleatoria densa",
       "bipartita": "(c) Emparejamiento bipartito", "malla": "(d) Malla"}
plt.rcParams.update({"font.family": "DejaVu Sans", "font.size": 9,
                     "axes.spines.top": False, "axes.spines.right": False,
                     "axes.edgecolor": "#888888", "axes.grid": True,
                     "grid.color": "#E5E5E5", "grid.linewidth": 0.6})

d = pd.read_csv("resultados/resultados_raw.csv")

# ---------- comprobaciones ----------
piv = d.pivot_table(index=["familia", "tam", "semilla"], columns="algoritmo",
                    values=["flujo", "aumentos"])
ambos = piv.dropna()
assert (ambos["flujo"]["Dinic"] == ambos["flujo"]["Edmonds-Karp"]).all()
aum_iguales = (ambos["aumentos"]["Dinic"] == ambos["aumentos"]["Edmonds-Karp"]).mean()
print(f"Instancias con ambos algoritmos: {len(ambos)}; aumentos iguales en {aum_iguales:.0%}")

# ---------- resumen ----------
g = d.groupby(["familia", "tam", "algoritmo"])
res = g.agg(V=("V", "mean"), E=("E", "mean"), t_med=("tiempo_ms", "mean"),
            t_sd=("tiempo_ms", "std"), aumentos=("aumentos", "mean"),
            fases=("fases", "mean"), examinadas=("aristas_examinadas", "mean"),
            n=("semilla", "count")).reset_index()
res.to_csv("resultados/resumen.csv", index=False)

# ---------- pendientes log-log (tiempo vs E y operaciones vs E) ----------
filas = []
for fam in FAM:
    for alg in COL:
        r = res[(res.familia == fam) & (res.algoritmo == alg) & (res.t_med >= 1.0)]
        if len(r) >= 3:
            pt = np.polyfit(np.log(r.E), np.log(r.t_med), 1)[0]
            po = np.polyfit(np.log(r.E), np.log(r.examinadas), 1)[0]
            pa = np.polyfit(np.log(r.E), np.log(r.aumentos), 1)[0]
            filas.append(dict(familia=fam, algoritmo=alg, puntos=len(r),
                              pendiente_tiempo=round(pt, 2), pendiente_operaciones=round(po, 2),
                              pendiente_aumentos=round(pa, 2)))
pend = pd.DataFrame(filas)
pend.to_csv("resultados/pendientes.csv", index=False)
print(pend.to_string(index=False))

# ---------- Figura 3: tiempo vs E ----------
fig, axs = plt.subplots(2, 2, figsize=(10, 7), dpi=200)
for ax, fam in zip(axs.flat, FAM):
    for alg in ["Edmonds-Karp", "Dinic"]:
        r = res[(res.familia == fam) & (res.algoritmo == alg)]
        ax.errorbar(r.E, r.t_med, yerr=r.t_sd, color=COL[alg], marker=MARK[alg], ms=5,
                    lw=2, capsize=2, label=alg)
        last = r.iloc[-1]
        off, ha = ((-8, 8), "right") if (fam == "malla" and alg == "Edmonds-Karp") else ((5, 0), "left")
        ax.annotate(alg, (last.E, last.t_med), xytext=off, textcoords="offset points",
                    fontsize=8, color="#333333", va="center", ha=ha)
    ax.set_xscale("log"); ax.set_yscale("log")
    ax.set_title(TIT[fam], loc="left", fontsize=10)
    ax.set_xlabel("Número de aristas E (escala log)")
    ax.set_ylabel("Tiempo (ms, escala log)")
    ax.margins(x=0.15)
axs[0, 0].legend(frameon=False, loc="upper left")
plt.tight_layout()
plt.savefig("figuras/fig_tiempos.png", bbox_inches="tight", facecolor="white")

# ---------- Figura 4: aceleración EK/Dinic ----------
fig, ax = plt.subplots(figsize=(7.5, 3.6), dpi=200)
colfam = {"dispersa": "#2A6FBB", "densa": "#D9731E", "bipartita": "#2E7D32", "malla": "#7B1FA2"}
mk = {"dispersa": "o", "densa": "s", "bipartita": "^", "malla": "D"}
for fam in FAM:
    e = res[(res.familia == fam) & (res.algoritmo == "Edmonds-Karp")].set_index("tam")
    di = res[(res.familia == fam) & (res.algoritmo == "Dinic")].set_index("tam")
    idx = e.index.intersection(di.index)
    sp = e.loc[idx, "t_med"] / di.loc[idx, "t_med"]
    ax.plot(e.loc[idx, "E"], sp, marker=mk[fam], color=colfam[fam], lw=2, ms=5,
            label=TIT[fam][4:])
ax.set_xscale("log"); ax.set_yscale("log")
ax.set_xlabel("Número de aristas E (escala log)")
ax.set_ylabel("Tiempo EK / tiempo Dinic")
ax.axhline(1, color="#888888", lw=1, ls="--")
ax.legend(frameon=False, fontsize=8)
plt.tight_layout()
plt.savefig("figuras/fig_aceleracion.png", bbox_inches="tight", facecolor="white")

# ---------- Figura 5: aumentos vs fases, y aristas examinadas ----------
fig, axs = plt.subplots(1, 2, figsize=(10, 3.6), dpi=200)
for fam in FAM:
    di = res[(res.familia == fam) & (res.algoritmo == "Dinic")]
    axs[0].plot(di.V, di.aumentos, marker=mk[fam], color=colfam[fam], lw=2, ms=5, label=f"Aumentos – {TIT[fam][4:]}")
    axs[0].plot(di.V, di.fases, marker=mk[fam], color=colfam[fam], lw=1.2, ms=4, ls="--", mfc="white")
axs[0].set_xscale("log"); axs[0].set_yscale("log")
axs[0].set_xlabel("Número de vértices V (escala log)")
axs[0].set_ylabel("Cantidad (escala log)")
axs[0].set_title("(a) Caminos de aumento (—) vs. fases de Dinic (- -)", loc="left", fontsize=10)
axs[0].legend(frameon=False, fontsize=7)
for fam in FAM:
    e = res[(res.familia == fam) & (res.algoritmo == "Edmonds-Karp")].set_index("tam")
    di = res[(res.familia == fam) & (res.algoritmo == "Dinic")].set_index("tam")
    idx = e.index.intersection(di.index)
    axs[1].plot(e.loc[idx, "E"], e.loc[idx, "examinadas"] / di.loc[idx, "examinadas"],
                marker=mk[fam], color=colfam[fam], lw=2, ms=5, label=TIT[fam][4:])
axs[1].set_xscale("log"); axs[1].set_yscale("log")
axs[1].set_xlabel("Número de aristas E (escala log)")
axs[1].set_ylabel("Aristas examinadas EK / Dinic")
axs[1].set_title("(b) Razón de operaciones representativas", loc="left", fontsize=10)
axs[1].legend(frameon=False, fontsize=7)
plt.tight_layout()
plt.savefig("figuras/fig_operaciones.png", bbox_inches="tight", facecolor="white")
print(res.round(2).to_string())
