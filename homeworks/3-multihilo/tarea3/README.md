# Code, Learn & Practice(Programación de Servicios y Procesos: "Cazadores de Monstruos")

En esta tarea vamos a aprender **thread pools en Java** simulando un **servidor de videojuegos** con varios escenarios:

1. 🏰 **Servidor de Mazmorras Online** → `ExecutorService` + `Runnable`  
2. ⚔️ **Calculadora de Daño Crítico** → `Callable` + `Future`  
3. 👹 **Spawns de Enemigos en un Mundo Abierto** → `ScheduledExecutorService`  

La idea es que veas **para qué sirven los pools de hilos** y cómo usarlos de forma correcta, pero disfrazado de cosas que te suenen a MMORPG, D&D o similar.

> __IMPORTANTE__: Debes de crear el proyecto en java, con las dependencias necesarias, y realizar los test necesarios para verificar el correcto funcionamiento.

---

## 1. Objetivos de la tarea

Al terminar deberías ser capaz de:

- Entender qué es un **thread pool** y por qué es mejor que crear hilos a lo loco.
- Usar `ExecutorService` con distintos tipos de pool:
  - `newFixedThreadPool(...)`
  - `newScheduledThreadPool(...)`
- Enviar tareas:
  - `Runnable` (no devuelven valor, solo hacen cosas).
  - `Callable<V>` (devuelven un resultado).
- Usar `Future<V>` para recuperar resultados de tareas.
- Cerrar correctamente un pool:
  - `shutdown()`, `awaitTermination(...)`, `shutdownNow()`.

---


Estructura sugerida:

```text
src/main/org/docencia/hilos/
├── ServidorMazmorras.java
├── CalculadoraDanoCritico.java
└── SpawnsMundoAbierto.java
```

---

## 2. Ejercicio 1 – 🏰 Servidor de Mazmorras Online (Fixed Thread Pool + Runnable)

### 2.1. Historia

Tienes un **servidor de mazmorras online**. Cada vez que un jugador entra a una mazmorra, el servidor debe:

- Validar al jugador.
- Preparar la instancia.
- Cargar enemigos, loot, etc.

En vez de crear un hilo por jugador, tienes un **equipo de 3 “GM bots”** (hilos del pool) que se encargan de procesar todas las peticiones de forma ordenada.

### 2.2. Qué vas a implementar

- Una tarea `Runnable` que representa una **petición de entrada a mazmorra**.
- Un pool de hilos fijo (`newFixedThreadPool(3)`) que actuará como los GM bots.
- Un `main` que simula varios jugadores intentando entrar a mazmorras distintas.

### 2.3. Código base 

```java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServidorMazmorras {

    static class PeticionMazmorra implements Runnable {
        private final String nombreJugador;
        private final String mazmorra;

        public PeticionMazmorra(String nombreJugador, String mazmorra) {
            this.nombreJugador = nombreJugador;
            this.mazmorra = mazmorra;
        }

        @Override
        public void run() {
            String hilo = Thread.currentThread().getName();
            System.out.println("[" + hilo + "] Preparando mazmorra '" + mazmorra +
                    "' para el jugador " + nombreJugador);
            try {
                Thread.sleep(1000 + (int)(Math.random() * 1000));
            } catch (InterruptedException e) {
                System.out.println("[" + hilo + "] Petición de " + nombreJugador + " interrumpida");
                Thread.currentThread().interrupt();
                return;
            }
            System.out.println("[" + hilo + "] Mazmorra '" + mazmorra +
                    "' lista para " + nombreJugador + " 🎮");
        }
    }

    public static void main(String[] args) {

        ExecutorService gmBots = Executors.newFixedThreadPool(3); // Creamos un pool de 3

        // Simulamos 10 jugadores que quieren entrar a mazmorras
        String[] jugadores = {
                "Link", "Zelda", "Geralt", "Yennefer", "Gandalf",
                "Frodo", "Aragorn", "Leia", "Luke", "DarthVader"
        };
        String[] mazmorras = {
                "Catacumbas de Hyrule", "Torre Oscura", "Moria",
                "Estrella de la Muerte", "Nido de Dragón"
        };

        for (int i = 0; i < jugadores.length; i++) {
            String jugador = jugadores[i];
            String dungeon = mazmorras[i % mazmorras.length];
            gmBots.execute(new PeticionMazmorra(jugador, dungeon));
        }

        gmBots.shutdown();
        System.out.println("Servidor: todas las peticiones han sido enviadas a los GM bots.");
    }
}
```

### 2.3. Responde y comenta la salida ejecutando los cambios que se proponen.

- Solo se usan **3 hilos** (3 GM bots) para atender a todos los jugadores, que esta sucediendo.
- Los mismos hilos procesan varias peticiones → **reutilización de hilos**. ¿Qué significa esto?
- ¿Qué pasa si cambias el tamaño del pool a 1? ¿Y a 10?

---

## 3. Ejercicio 2 – ⚔️ Calculadora de Daño Crítico (Callable + Future)

### 3.1. Historia

Ahora estás en el **servidor de combate**.  
Cada jugador de la raid lanza ataques y el servidor tiene que calcular:

- Daño base.
- Si hay crítico o no (según probabilidad).
- Multiplicador de crítico.

Cada cálculo de ataque es una tarea que **devuelve el daño total**.  
Se ejecutan en paralelo usando un pool, y luego se suman los daños para ver el **DPS total de la raid**.

### 3.2. Qué vas a implementar

- Una clase `Ataque` con:
  - nombre del atacante
  - daño base
  - probabilidad de crítico
  - multiplicador crítico
- Una tarea `Callable<Integer>` que calcula el daño de un ataque.
- Un pool fijo (`newFixedThreadPool(4)`).
- Uso de `Future<Integer>` para leer el daño de cada ataque y sumarlo.

### 3.3. Código base

```java
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class CalculadoraDanoCritico {

    static class Ataque {
        final String atacante;
        final int danoBase;
        final double probCritico;          // Por ejemplo 0.25 = 25%
        final double multiplicadorCritico; // Por ejemplo 2.0 = x2

        Ataque(String atacante, int danoBase, double probCritico, double multiplicadorCritico) {
            this.atacante = atacante;
            this.danoBase = danoBase;
            this.probCritico = probCritico;
            this.multiplicadorCritico = multiplicadorCritico;
        }
    }

    static class TareaCalcularDano implements Callable<Integer> {
        private final Ataque ataque;

        TareaCalcularDano(Ataque ataque) {
            this.ataque = ataque;
        }

        @Override
        public Integer call() throws Exception {
            String hilo = Thread.currentThread().getName();
            System.out.println("[" + hilo + "] Calculando daño para " + ataque.atacante);

            boolean esCritico = Math.random() < ataque.probCritico;
            double multiplicador = esCritico ? ataque.multiplicadorCritico : 1.0;

            Thread.sleep(500 + (int)(Math.random() * 500));

            int danoFinal = (int) (ataque.danoBase * multiplicador);
            System.out.println("[" + hilo + "] " + ataque.atacante +
                    (esCritico ? " ¡CRÍTICO!" : " golpe normal") +
                    " -> daño: " + danoFinal);

            return danoFinal;
        }
    }

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(4);
        List<Future<Integer>> futuros = new ArrayList<>();

        // Simulamos una raid
        Ataque[] ataques = {
                new Ataque("Mago del Fuego", 120, 0.30, 2.5),
                new Ataque("Guerrero", 150, 0.15, 2.0),
                new Ataque("Pícaro", 90, 0.50, 3.0),
                new Ataque("Arquera Élfica", 110, 0.35, 2.2),
                new Ataque("Invocador", 80, 0.40, 2.8),
                new Ataque("Paladín", 130, 0.10, 1.8),
                new Ataque("Bárbaro", 160, 0.20, 2.1),
                new Ataque("Nigromante", 100, 0.25, 2.3),
        };

        // Enviamos todas las tareas al pool
        for (Ataque ataque : ataques) {
            Future<Integer> futuro = pool.submit(new TareaCalcularDano(ataque));
            futuros.add(futuro);
        }

        // Recogemos resultados
        int totalRaid = 0;
        for (int i = 0; i < ataques.length; i++) {
            try {
                int dano = futuros.get(i).get(); 
                totalRaid += dano;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Lectura de resultado interrumpida");
            } catch (ExecutionException e) {
                System.out.println("Error calculando daño: " + e.getCause());
            }
        }

        System.out.println("Daño total de la raid: " + totalRaid);
        pool.shutdown();
    }
}
```

### 3.3. Responde y comenta la salida ejecutando los cambios que se proponen.

- Diferencia con:
  - `execute(Runnable)` → no hay valor de retorno. → Muestra las salidas que demuestran esto.
  - `submit(Callable<V>)` → devuelve un `Future<V>` del que puedes sacar el resultado. → Muestra las salidas que demuestran esto.
- Cómo se pueden lanzar muchos cálculos de daño en paralelo y luego recogerlos todos.
- Probar a cambiar la probabilidad de crítico y ver cómo sube/baja el daño total.

---

## 4. Ejercicio 3 – 👹 Spawns de Enemigos en Mundo Abierto (ScheduledExecutorService)

### 4.1. Historia

Ahora tienes un **mundo abierto** tipo sandbox. Cada pocos segundos, el juego debe:

- Lanzar spawns de enemigos en distintas zonas.
- Loguear qué aparece y dónde.

Usarás un `ScheduledExecutorService` como si fuera el motor que programa los spawns.

### 4.2. Qué vas a implementar

- Una tarea `Runnable` que genera:
  - Una zona aleatoria.
  - Un enemigo aleatorio.
- Un `ScheduledExecutorService` que ejecuta esta tarea **cada 2 segundos**.
- Un `main` que deja el sistema funcionando unos segundos y luego apaga el scheduler.

### 4.3. Código base sugerido

```java
import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SpawnsMundoAbierto {

    static class SpawnTarea implements Runnable {

        private final String[] zonas = {
                "Bosque Maldito",
                "Ruinas Antiguas",
                "Pantano Radiactivo",
                "Ciudad Cibernética",
                "Templo Prohibido"
        };

        private final String[] enemigos = {
                "Slime Mutante",
                "Esqueleto Guerrero",
                "Mecha-Dragón",
                "Bandido del Desierto",
                "Lich Supremo"
        };

        @Override
        public void run() {
            String hilo = Thread.currentThread().getName();
            String zona = zonas[(int)(Math.random() * zonas.length)];
            String enemigo = enemigos[(int)(Math.random() * enemigos.length)];
            System.out.println("[" + LocalTime.now() + "][" + hilo + "] Spawn de " +
                    enemigo + " en " + zona);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

        // Ejecutar la tarea cada 2 segundos, sin retraso inicial
        scheduler.scheduleAtFixedRate(new SpawnTarea(), 0, 2, TimeUnit.SECONDS);

        // Dejamos que el mundo “viva” durante 12 segundos
        Thread.sleep(12000);

        // Apagado ordenado
        System.out.println("Deteniendo spawns...");
        scheduler.shutdown();
        if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
            System.out.println("Forzando parada de spawns.");
            scheduler.shutdownNow();
        }
        System.out.println("Servidor de mundo abierto detenido.");
    }
}
```

### 4.3. Responde y comenta la salida ejecutando los cambios que se proponen.

- `ScheduledExecutorService` permite:
  - `schedule(...)` → una vez en el futuro. ¿Qué significa esto?
  - `scheduleAtFixedRate(...)` → repetidamente, cada X tiempo. ¿Qué significa esto?
- Cómo se comporta el sistema si la tarea tarda más que el período. Modifca, muestra el resultado y comenta.
- Probar a cambiar el período (1s, 3s…) y la duración del `sleep` del `main`.  Modifca, muestra el resultado y comenta.

---

## 5. Retos opcionales para subir de nivel 🧙‍♂️

- **Reto 1:**  
  Añade tiempo de cola / prioridad de jugadores en el servidor de mazmorras.
- **Reto 2:**  
  Haz que cada ataque pueda fallar (daño 0) y calcula la **media** de daño.
- **Reto 3:**  
  Añade tipos de spawn “raro” (enemigo épico) con menos probabilidad.
- **Reto 4:**  
  Usa constantes y enums (`enum`) para las zonas, enemigos, clases de personaje, etc.
- **Reto 5:**  
  Escribe tus propias trazas de log con formato tipo:
  `[TIMESTAMP] [SISTEMA] mensaje`.

## Licencia 📄

Este proyecto está bajo la Licencia (Apache 2.0) - mira el archivo [LICENSE.md]([../../../LICENSE.md](https://github.com/jpexposito/code-learn-practice/blob/main/LICENSE)) para detalles.