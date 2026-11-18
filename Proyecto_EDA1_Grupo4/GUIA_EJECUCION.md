# Guía de ejecución: Windows y Linux

**Proyecto EDA1 · Grupo 4**

Comparación de Edmonds-Karp y Dinic para el problema de flujo máximo.

Manual paso a paso para compilar, ejecutar las pruebas, medir el rendimiento y comprender las tablas y gráficas del proyecto.

## Contenido

1. [Qué hace el programa](#1-qué-hace-el-programa)
2. [Requisitos y estructura](#2-requisitos-y-estructura)
3. [Linux: compilación y ejecución](#3-linux-compilación-y-ejecución)
4. [Windows: ejecución con PowerShell](#4-windows-ejecución-con-powershell)
5. [Python: tablas e imágenes](#5-python-tablas-e-imágenes)
6. [Interpretar los mensajes de Java](#6-interpretar-los-mensajes-de-java)
7. [Interpretar los CSV](#7-interpretar-los-csv)
8. [Gráficas y crecimiento](#8-gráficas-y-crecimiento)
9. [Errores habituales y comprobación final](#9-errores-habituales-y-comprobación-final)

## 1. Qué hace el programa

Calcula la mayor cantidad de flujo que puede viajar desde un origen hasta un destino sin superar la capacidad de cada conexión. Una analogía es la cantidad de agua que puede circular por una red de tuberías.

Los dos algoritmos deben obtener el mismo flujo máximo sobre la misma red. La comparación estudia cuánto tiempo y cuánto trabajo necesita cada uno. Es un programa de consola: no tiene menú gráfico ni solicita datos por teclado.

**Validación indicada en la guía original:** el proyecto se compiló con OpenJDK 25 en Linux y superó el ejemplo conocido de flujo 23 y sus 2.000 pruebas aleatorias. Los comandos de Windows están adaptados a PowerShell; no se han ejecutado en Windows. Los tiempos de rendimiento dependerán del equipo.

## 2. Requisitos y estructura

### Software necesario

| Herramienta | Para qué sirve |
| --- | --- |
| JDK 17 o superior | Incluye `java` (ejecutar) y `javac` (compilar). Instala un JDK, no solo un entorno de ejecución. |
| Python 3 | Genera las tablas y las imágenes con los scripts del proyecto. |
| `numpy`, `pandas`, `matplotlib` | Dependencias de Python; se instalarán en un entorno virtual. |

**Windows:** instala un JDK 17+ (por ejemplo, Temurin) y Python 3 desde sus distribuidores oficiales. Habilita la incorporación al `PATH` cuando el instalador la ofrezca y abre una nueva PowerShell después de instalar.

**Linux:** instala el JDK, Python 3 y el módulo de entornos virtuales usando el gestor de paquetes de tu distribución. En Ubuntu/Debian, por ejemplo:

```bash
sudo apt update
sudo apt install openjdk-17-jdk python3 python3-venv python3-pip
```

### Estructura de referencia

Conserva esta estructura al copiar el proyecto a otro equipo:

```text
Codigo_EDA1_Grupo4/
  bin/                          # Clases compiladas; se crea al compilar
  Proyecto_EDA1_Grupo4/
    src/                        # Fuentes Java
    resultados/                 # CSV
    figuras/                    # Imágenes PNG
    analisis.py
    graficos_ejemplo.py
    graficos_metodo.py
    GUIA_EJECUCION.md
```

En este manual, **carpeta principal** es `Codigo_EDA1_Grupo4` y **carpeta interna** es `Proyecto_EDA1_Grupo4`.

Las rutas son ejemplos genéricos: reemplázalas por la ubicación real de tu proyecto. Si la carpeta principal tiene otro nombre —por ejemplo, `Codigo_EDA1_Grupo4 (1)`—, utiliza ese nombre. Pon las rutas entre comillas cuando contengan espacios.

### Por qué importa la carpeta actual

`Experimento.java` escribe en `../resultados/resultados_raw.csv`, una ruta relativa a la carpeta de la terminal. Por eso ejecutaremos el experimento desde la carpeta interna `src`. Los scripts Python deben ejecutarse desde la carpeta interna, donde está `analisis.py`.

> **Resultados anteriores:** cada experimento sobrescribe `resultados_raw.csv`; el análisis sobrescribe sus tablas y gráficas. Copia los archivos anteriores si quieres conservarlos. El experimento no genera automáticamente una copia de seguridad.

## 3. Linux: compilación y ejecución

### Paso 1. Verificar las herramientas

```bash
java -version
javac -version
python3 --version
```

`java` y `javac` deben indicar una versión 17 o superior. La validación documentada utilizó OpenJDK 25.

### Paso 2. Entrar en la carpeta principal y compilar

```bash
cd "/ruta/al/proyecto/Codigo_EDA1_Grupo4"
mkdir -p bin
javac -encoding UTF-8 -d bin Proyecto_EDA1_Grupo4/src/*.java
```

Sustituye la ruta del comando `cd` por la real. Una compilación correcta normalmente no imprime mensajes.

- `javac` compila.
- `-encoding UTF-8` permite leer los caracteres del código.
- `-d bin` guarda las clases compiladas.
- `*.java` selecciona todos los archivos fuente Java.

### Paso 3. Ejecutar las pruebas de corrección

```bash
java -cp bin PruebasCorreccion
```

El parámetro `-cp` indica dónde están las clases. Deben aparecer el flujo 23 para ambos algoritmos y las 2.000 redes superadas. Consulta la [interpretación de las pruebas](#6-interpretar-los-mensajes-de-java).

### Paso 4. Ejecutar el modo rápido

```bash
cd Proyecto_EDA1_Grupo4/src
java -Xmx4g -cp ../../bin Experimento rapido
```

Usa dos semillas por tamaño y una repetición por algoritmo. No reduce los tamaños máximos; por ello también puede tardar. Al principio realiza un calentamiento sin mensajes de progreso.

### Paso 5. Experimento completo

Desde la misma carpeta `src`, cuando quieras medir más muestras:

```bash
java -Xmx4g -cp ../../bin Experimento
```

Usa cinco semillas por tamaño y hasta tres repeticiones por algoritmo. Guarda el menor tiempo por instancia. Las corridas que superan un segundo no se repiten. El `LEEME` estima unos 10 minutos; el tiempo real depende del equipo.

`-Xmx4g` permite hasta 4 GiB de heap para Java. No reserva necesariamente toda esa memoria al comenzar. Tanto la ejecución rápida como la completa reemplazan el mismo CSV. **No necesitas ejecutar ambos modos** para generar las gráficas.

Siguiente paso: [generar las tablas y gráficas](#5-python-tablas-e-imágenes).

## 4. Windows: ejecución con PowerShell

Copia la carpeta completa a tu equipo. Este ejemplo supone que está en `C:\Proyectos\Codigo_EDA1_Grupo4`. Cambia esa ruta por la real. Usa **PowerShell**, no el símbolo del sistema CMD, porque algunos comandos son específicos de PowerShell.

### Paso 1. Comprobar las herramientas

```powershell
java -version
javac -version
py -3 --version
```

Si el comando `py` no existe pero `python` sí funciona, utiliza `python` en lugar de `py -3` al crear el entorno virtual de la sección de Python.

### Paso 2. Entrar en la carpeta principal y compilar

```powershell
Set-Location "C:\Proyectos\Codigo_EDA1_Grupo4"
New-Item -ItemType Directory -Force -Path bin | Out-Null
$fuentes = Get-ChildItem .\Proyecto_EDA1_Grupo4\src\*.java
javac -encoding UTF-8 -d bin $fuentes.FullName
```

Se obtiene primero la lista de archivos Java y se pasan sus rutas a `javac`. Esto evita depender de cómo se expanden los comodines en distintas consolas de Windows. Si no hay errores, la compilación normalmente no muestra nada.

### Paso 3. Ejecutar las pruebas

```powershell
java -cp .\bin PruebasCorreccion
```

Espera el ejemplo con flujo 23 y el mensaje de las 2.000 redes superadas.

### Paso 4. Ejecutar el experimento rápido

```powershell
Set-Location .\Proyecto_EDA1_Grupo4\src
java -Xmx4g -cp ..\..\bin Experimento rapido
```

### Paso 5. Ejecutar el experimento completo

Desde la misma carpeta `src`:

```powershell
java -Xmx4g -cp ..\..\bin Experimento
```

Elige rápido para una primera prueba o completo para obtener más muestras. Ejecutarlos uno tras otro reemplaza los datos del primero con los del segundo. No necesitas ejecutar ambos para generar las gráficas.

El modo completo usa cinco semillas y hasta tres repeticiones; el rápido usa dos semillas y una repetición. Los dos escriben en la carpeta interna `resultados`. Consulta la [explicación del progreso y del valor `-1.0 ms`](#6-interpretar-los-mensajes-de-java).

## 5. Python: tablas e imágenes

Realiza estos pasos una vez que el experimento haya terminado. También puedes analizar los CSV ya incluidos, pero en ese caso las gráficas reflejarán esos datos anteriores.

### Linux

```bash
cd "/ruta/al/proyecto/Codigo_EDA1_Grupo4/Proyecto_EDA1_Grupo4"
python3 -m venv .venv
.venv/bin/python -m pip install numpy pandas matplotlib
mkdir -p resultados figuras
.venv/bin/python analisis.py
.venv/bin/python graficos_ejemplo.py
.venv/bin/python graficos_metodo.py
xdg-open figuras
```

### Windows PowerShell

```powershell
Set-Location "C:\Proyectos\Codigo_EDA1_Grupo4\Proyecto_EDA1_Grupo4"
py -3 -m venv .venv
.\.venv\Scripts\python.exe -m pip install numpy pandas matplotlib
New-Item -ItemType Directory -Force -Path resultados,figuras | Out-Null
.\.venv\Scripts\python.exe analisis.py
.\.venv\Scripts\python.exe graficos_ejemplo.py
.\.venv\Scripts\python.exe graficos_metodo.py
Invoke-Item .\figuras
```

Estos comandos llaman directamente al Python del entorno virtual: no hace falta activarlo ni cambiar la política de ejecución de PowerShell. Si `.venv` ya existe y tiene las dependencias, no necesitas recrearlo ni reinstalarlas. Si copias el proyecto entre Windows y Linux, crea un entorno nuevo para el sistema de destino.

### Archivos generados

| Archivo | Contenido |
| --- | --- |
| `resultados/resumen.csv` | Promedios y variación por familia, tamaño y algoritmo. |
| `resultados/pendientes.csv` | Estimaciones de crecimiento con el número de aristas. |
| `figuras/fig_tiempos.png` | Tiempo de ambos algoritmos frente al tamaño de la red. |
| `figuras/fig_aceleracion.png` | Razón tiempo de Edmonds-Karp / tiempo de Dinic. |
| `figuras/fig_operaciones.png` | Aumentos, fases y razón de aristas examinadas. |
| `figuras/fig_ejemplo.png` | Red de flujo 23 y niveles iniciales de Dinic; ilustración fija. |
| `figuras/fig_metodo.png` | Diagrama del procedimiento; ilustración del modo completo. |

Los scripts guardan imágenes PNG: no abren ventanas automáticamente. `analisis.py` además imprime las pendientes y la tabla de resumen en la terminal.

## 6. Interpretar los mensajes de Java

### Resultado de las pruebas de corrección

```text
Ejemplo CLRS: Edmonds-Karp = 23, Dinic = 23 (esperado 23)
Pruebas aleatorias superadas: 2000 redes
(mismo flujo, flujo valido y flujo = corte minimo).
```

CLRS es el ejemplo de referencia usado por el proyecto. Ambos algoritmos deben producir 23. Las otras comprobaciones verifican igualdad de resultados, capacidades, conservación del flujo en los nodos intermedios y coincidencia con la capacidad del corte mínimo. Esta última igualdad certifica la optimalidad del flujo obtenido para las redes probadas.

Si aparece `AssertionError`, alguna comprobación falló: revisa el texto completo. En el experimento también se comprueba la igualdad del flujo cuando se ejecutan los dos algoritmos.

### Progreso del experimento: ejemplo ilustrativo

```text
dispersa      tam=1000       EK medio=12.4 ms
```

`dispersa` es la familia; `tam` es su parámetro de tamaño; `EK medio` es la media de los tiempos registrados para Edmonds-Karp en ese tamaño. No es el tiempo total del programa. Los tiempos de Dinic se guardan en el CSV, aunque no aparezcan en esta línea. 1.000 ms equivalen a un segundo.

| Familia | Qué significa `tam` |
| --- | --- |
| `dispersa` | Número de vértices. Red con relativamente pocas conexiones. |
| `densa` | Número de vértices. Red con muchas conexiones. |
| `bipartita` | Elementos de cada grupo: trabajadores y tareas. `V = 2 × tam + 2`. El flujo máximo representa el número de emparejamientos. |
| `malla` | Lado de una cuadrícula. `V = tam² + 2`. Una malla de `tam=100` tiene 10.002 vértices. |

### Qué significa `EK medio=-1.0 ms`

No es un tiempo negativo. Si la media de Edmonds-Karp supera 5.000 ms en un tamaño, el programa omite ese algoritmo en los tamaños mayores de esa familia; Dinic continúa. El valor `-1.0` indica que no hubo mediciones de Edmonds-Karp en ese tamaño.

El umbral de cinco segundos no cancela una corrida en curso: se aplica después de terminar las mediciones del tamaño actual. Una corrida individual puede durar más.

### Fin normal

```text
Listo: resultados/resultados_raw.csv
```

Indica que terminó de escribir el CSV. Si interrumpes el proceso con `Ctrl+C`, puede quedar un archivo incompleto. Para comparar tiempos, evita ejecutar simultáneamente otros trabajos pesados.

## 7. Interpretar los CSV

Abre los archivos con Excel, LibreOffice Calc o un visor de texto. Selecciona coma como separador; los decimales usan punto. En Excel, si todo aparece en una columna, importa el archivo desde **Datos → Desde texto o CSV**.

### `resultados_raw.csv`: mediciones por instancia

| Columna | Significado |
| --- | --- |
| `familia` / `tam` | Tipo de red y parámetro de tamaño. |
| `V` / `E` | Número de vértices y de aristas originales dirigidas. |
| `semilla` | Identifica la red aleatoria y permite reproducirla. |
| `algoritmo` | Dinic o Edmonds-Karp. |
| `tiempo_ms` | Tiempo registrado en milisegundos; menor es mejor. En el modo completo se conserva el menor de hasta tres intentos. |
| `flujo` | Valor del flujo máximo; debe coincidir en ambos algoritmos para la misma red. |
| `aumentos` | Cantidad de caminos utilizados para aumentar el flujo. |
| `fases` | Fases de Dinic. Para Edmonds-Karp, este programa registra aquí el número de aumentos. |
| `aristas_examinadas` | Exploraciones de aristas contadas por el código. Una misma arista puede examinarse muchas veces. |

### Cómo comparar dos filas

Busca iguales **`familia` + `tam` + `semilla`**. Así comparas ambos algoritmos sobre la misma red.

Ejemplo inventado: Edmonds-Karp tarda 100 ms y Dinic 10 ms, ambos con flujo 500. Dinic es diez veces más rápido en esa instancia porque `100 / 10 = 10`. Un flujo mayor no hace mejor al algoritmo: los resultados deben coincidir.

### `resumen.csv`: agregación de las mediciones

| Columna | Significado |
| --- | --- |
| `V` / `E` | Promedios del número de vértices y aristas del grupo. |
| `t_med` | Media aritmética del tiempo registrado; pese al nombre, no es una mediana. |
| `t_sd` | Desviación estándar del tiempo entre muestras. Cuanto mayor, más varían los tiempos. |
| `aumentos` / `fases` | Promedios de los contadores correspondientes. |
| `examinadas` | Promedio de aristas examinadas. |
| `n` | Número de muestras del grupo; normalmente 2 en rápido o 5 en completo. No es el número de repeticiones internas. |

Al analizar, el mensaje «aumentos iguales en ...%» indica en cuántas instancias coincidió ese contador. No tiene por qué ser 100%: distintos caminos pueden alcanzar el mismo flujo máximo.

## 8. Gráficas y crecimiento

### `fig_tiempos.png`: ¿cuál tarda menos?

Eje horizontal: número de aristas `E`. Eje vertical: milisegundos. Edmonds-Karp aparece en naranja y Dinic en azul. La curva más baja es la más rápida. Las barras de error muestran la desviación estándar entre muestras.

Los ejes son logarítmicos: intervalos iguales representan factores iguales, por ejemplo de 10 a 100 y de 100 a 1.000. Cada panel corresponde a una familia de redes. Compara algoritmos dentro del mismo panel y tamaño.

### `fig_aceleracion.png`: ¿cuántas veces es más rápido?

```text
aceleracion = tiempo de Edmonds-Karp / tiempo de Dinic
```

| Valor | Lectura |
| --- | --- |
| 1 | Ambos tardan lo mismo. |
| 2 | Dinic es dos veces más rápido. |
| 10 | Dinic es diez veces más rápido. |
| 0,5 | Edmonds-Karp es dos veces más rápido. |

### `fig_operaciones.png`: ¿cuánto trabajo realizan?

**Panel izquierdo:** líneas continuas para aumentos de Dinic y discontinuas para sus fases. Una fase puede agrupar varios aumentos.

**Panel derecho:** razón de aristas examinadas por Edmonds-Karp frente a Dinic. Un valor de 10 indica diez veces tantas exploraciones; no implica necesariamente diez veces más tiempo, porque los algoritmos tienen otras operaciones y costes.

### `pendientes.csv`: cómo crece el coste al crecer `E`

- `puntos`: tamaños usados en el ajuste.
- `pendiente_tiempo`: crecimiento observado del tiempo.
- `pendiente_operaciones`: crecimiento de las aristas examinadas.
- `pendiente_aumentos`: crecimiento del número de aumentos.

Los ajustes se hacen sobre escalas logarítmicas.

| Pendiente del tiempo | Al duplicar `E`, el tiempo se multiplica aproximadamente por |
| --- | --- |
| 1 | 2 |
| 2 | 4 |
| 3 | 8 |

El ajuste usa solo tamaños con tiempo medio de al menos 1 ms y necesita tres puntos o más. Si faltan combinaciones en `pendientes.csv`, puede ser por ese filtro. Una pendiente es una estimación empírica, no una demostración de la complejidad teórica del algoritmo.

### Qué escribir como conclusión

Indica la familia y el tamaño; comprueba que coincide el flujo; compara los tiempos y calcula su razón; apóyate en las operaciones y fases para explicar la diferencia. No generalices un único resultado a todas las redes.

## 9. Errores habituales y comprobación final

| Problema | Qué revisar o hacer |
| --- | --- |
| `java` o `javac` no se reconoce | Instala un JDK 17+ y configura su carpeta `bin` en `PATH`. Cierra y abre de nuevo la terminal. Tener `java` no garantiza tener `javac`. |
| `UnsupportedClassVersionError` | La versión que ejecuta es más antigua que la usada para compilar. Usa un JDK compatible para ambas operaciones y recompila. |
| `Could not find or load main class` | Comprueba la carpeta actual, que existan los `.class` en `bin` y la ruta de `-cp`. Las clases se llaman `PruebasCorreccion` y `Experimento`, sin `.java` ni `.class` al ejecutarlas. |
| No aparecen resultados donde esperabas | El experimento debe ejecutarse desde `Proyecto_EDA1_Grupo4/src`. Escribe en `../resultados` respecto a la carpeta actual. |
| `FileNotFoundError` en `analisis.py` | Ejecuta Python desde la carpeta interna que contiene `analisis.py` y comprueba que exista `resultados/resultados_raw.csv`. |
| `ModuleNotFoundError` | Instala `numpy`, `pandas` y `matplotlib` con el mismo Python del entorno virtual que utilizas para ejecutar los scripts. |
| Linux: falla `python3 -m venv` | Instala el paquete de entornos virtuales de tu distribución; en Ubuntu/Debian, `python3-venv`. |
| Windows: `py` no se reconoce | Prueba `python --version`. Si funciona, sustituye `py -3` por `python` al crear el entorno. Si no, instala Python y revisa `PATH`. |
| `OutOfMemoryError` | Cierra aplicaciones para liberar recursos. Puedes aumentar `-Xmx` solo si tu equipo tiene RAM suficiente. Bajar ese límite reduce la memoria disponible para el experimento. |
| No aparecen ventanas con gráficas | Es normal: los scripts guardan PNG en `figuras`. Abre la carpeta para verlos. |
| `AssertionError` o análisis con datos incompletos | Revisa el mensaje completo. Comprueba primero las pruebas de corrección y que el experimento haya finalizado antes de analizar el CSV. |

### Lista final para una ejecución completa

- [ ] Java, `javac` y Python disponibles.
- [ ] Fuentes compiladas sin errores.
- [ ] Ejemplo de flujo 23 y 2.000 redes superadas.
- [ ] Experimento finalizado con el mensaje «Listo».
- [ ] CSV de mediciones generado en la carpeta correcta.
- [ ] `analisis.py` ejecutado con las dependencias instaladas.
- [ ] Tablas y gráficas abiertas e interpretadas.
- [ ] Resultados guardados antes de iniciar otra medición.

**Regla principal:** mismo flujo máximo; compara tiempo, aumentos, fases y exploraciones. Las mediciones rápidas sirven para familiarizarte; el modo completo aporta más muestras para el informe.
