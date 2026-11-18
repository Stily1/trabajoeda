# EDA1 · Grupo 4: Edmonds-Karp vs. Dinic

Comparación de dos algoritmos para el problema de flujo máximo, implementados en Java, con análisis y gráficas en Python.

## Estructura

- `Proyecto_EDA1_Grupo4/src/`: fuentes Java.
- `Proyecto_EDA1_Grupo4/analisis.py`: tablas y gráficas de rendimiento.
- `Proyecto_EDA1_Grupo4/graficos_ejemplo.py`: ilustración de la red de ejemplo.
- `Proyecto_EDA1_Grupo4/graficos_metodo.py`: ilustración del procedimiento.
- [Guía de ejecución para Windows y Linux](Proyecto_EDA1_Grupo4/GUIA_EJECUCION.md).

## Requisitos

- JDK 17 o superior.
- Python 3 con `numpy`, `pandas` y `matplotlib`.

Sigue la guía para compilar, superar las pruebas, ejecutar el experimento y generar las imágenes.
Las rutas de la guía son ejemplos: utiliza la ubicación donde descomprimas el proyecto.

## Carpetas generadas

El paquete limpio no incluye resultados, figuras, clases compiladas ni entornos virtuales.

- `bin/` se crea al seguir los comandos de compilación.
- Java crea `Proyecto_EDA1_Grupo4/resultados/` al ejecutar el experimento desde `src/`.
- Crea `Proyecto_EDA1_Grupo4/figuras/` antes de ejecutar los scripts Python, como indica la guía.
- En Colab, la celda de preparación crea las carpetas `resultados/` y `figuras/`.

Para analizar desde cero, primero termina el experimento y después ejecuta `analisis.py`.
Cada nuevo experimento reemplaza su CSV y el análisis reemplaza sus tablas y gráficas.



