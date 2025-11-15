# Code, Learn & Practice(Programación de Servicios y Procesos: "Hilos y Semáforos en Java")

> Los **ejercicios** que se proponen es para la prática de uso de semáforos en java
---

Para resolver la carrera entre `Goku` y `Vegeta` usando **semáforos** (`java.util.concurrent.Semaphore`).  
Aprenderás a:

- Evitar bloqueos tras declarar un ganador (liberación de permisos).
- Diseñar un test robusto con JUnit 5.

---

## 🎯 Objetivo

1. Declarar **un único ganador** de manera segura.
2. Asegurar que **ningún hilo quede bloqueado** cuando la carrera termina.
3. Recuerda el código de partida

```java
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

public class SaiyanRace implements Runnable {
    private final String name;
    private int distance = 0;
    private static final int GOAL = 100;
    private static final AtomicBoolean WINNER_DECLARED = new AtomicBoolean(false);

    public SaiyanRace(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        while (distance < GOAL && !WINNER_DECLARED.get()) {
            int step = ThreadLocalRandom.current().nextInt(1, 11); 
            distance += step;
            System.out.println(name + " avanzó " + step + " metros. Distancia total: " + distance + " metros.");

            if (distance >= GOAL) {
                if (WINNER_DECLARED.compareAndSet(false, true)) {
                    System.out.println(name + " ha ganado la carrera!");
                }
            }

            try {
                Thread.sleep(500); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    public static void resetWinner() {
        WINNER_DECLARED.set(false);
    }

    public static void main(String[] args) {
        Thread goku = new Thread(new SaiyanRace("Goku"));
        Thread vegeta = new Thread(new SaiyanRace("Vegeta"));

        goku.start();
        vegeta.start();
    }
}
```

---

## 🧩 Conceptos clave de `Semaphore`

- Un **Semaphore** controla el número de **permisos** disponibles.
  - **Binario** (`new Semaphore(1)`) ≈ *mutex*: a la vez solo entra uno.
  - **Contador** (`new Semaphore(k)`) ≈ *k recursos*: a la vez pueden entrar k.
- `acquire()` **bloquea** si no hay permisos; `release()` **devuelve** un permiso.
- El semáforo **no guarda “de quién es el turno”**: lo define tu **protocolo**.

---

## Diseñando la solución

- **Alternancia por turnos** con **dos semáforos** (Goku/Vegeta).
- **Ganador único** con `AtomicBoolean.compareAndSet(false, true)`.
- **Salida limpia**: si un hilo detecta que la carrera ya acabó, hace `release()` del semáforo del rival para que **no se quede bloqueado**.

---

## Código Java (Semáforos)

### `SaiyanRaceSemaphore.java`

```java
package com.docencia.semaforo;

import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

public class SaiyanRaceSemaphoreOne implements Runnable {
    private final String name;
    private int distance = 0;

    private static final int GOAL = 100;

    private static final Semaphore semaphore = new Semaphore(1, true);

    private static final AtomicBoolean winnerDeclared = new AtomicBoolean(false);

    public SaiyanRaceSemaphoreOne(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        try {
            while (!winnerDeclared.get() && distance < GOAL) {

                semaphore.acquire();
                try {
                    if (winnerDeclared.get()) {
                        break;
                    }
                    int step = ThreadLocalRandom.current().nextInt(1, 11);
                    distance += step;
                    System.out.println(name + " avanzó " + step + " metros. Distancia total: " + distance + " metros.");

                    if (distance >= GOAL) {
                        if (winnerDeclared.compareAndSet(false, true)) {
                            System.out.println(name + " ha ganado la carrera!");
                        }
                    }
                } finally {
                    semaphore.release();
                }
                try {
                    Thread.sleep(ThreadLocalRandom.current().nextInt(200, 401));
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread goku = new Thread(new SaiyanRaceSemaphoreOne("Goku"));
        Thread vegeta = new Thread(new SaiyanRaceSemaphoreOne("Vegeta"));

        goku.start();
        vegeta.start();

        goku.join();
        vegeta.join();
    }
}
```

## Posible salida en consola

```console
Goku avanzó 6 metros. Distancia total: 39 metros.
Vegeta avanzó 9 metros. Distancia total: 45 metros.
Goku avanzó 10 metros. Distancia total: 49 metros.
Vegeta avanzó 7 metros. Distancia total: 52 metros.
Goku avanzó 10 metros. Distancia total: 59 metros.
Vegeta avanzó 6 metros. Distancia total: 58 metros.
Goku avanzó 3 metros. Distancia total: 62 metros.
Vegeta avanzó 7 metros. Distancia total: 65 metros.
Goku avanzó 10 metros. Distancia total: 72 metros.
Vegeta avanzó 7 metros. Distancia total: 72 metros.
Goku avanzó 3 metros. Distancia total: 75 metros.
Vegeta avanzó 6 metros. Distancia total: 78 metros.
Goku avanzó 2 metros. Distancia total: 77 metros.
Vegeta avanzó 1 metros. Distancia total: 79 metros.
Goku avanzó 8 metros. Distancia total: 85 metros.
Vegeta avanzó 3 metros. Distancia total: 82 metros.
Goku avanzó 2 metros. Distancia total: 87 metros.
Vegeta avanzó 3 metros. Distancia total: 85 metros.
Goku avanzó 1 metros. Distancia total: 88 metros.
Vegeta avanzó 4 metros. Distancia total: 89 metros.
Goku avanzó 4 metros. Distancia total: 92 metros.
Vegeta avanzó 2 metros. Distancia total: 91 metros.
Goku avanzó 5 metros. Distancia total: 97 metros.
Vegeta avanzó 8 metros. Distancia total: 99 metros.
Vegeta avanzó 4 metros. Distancia total: 103 metros.
Vegeta ha ganado la carrera!
```

## 🎯 Mejorando la solución

Modelar una carrera con dos hilos (`Goku`, `Vegeta`) que **se alternan por turnos**.  
  Para alternar Goku/Vegeta:
  - `turnGoku = new Semaphore(1)`  → Goku puede empezar.
  - `turnVegeta = new Semaphore(0)` → Vegeta espera a que Goku libere.

```java
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

public class SaiyanRaceSemaphore implements Runnable {
    private final String name;
    private int distance = 0;

    private static final int GOAL = 100;

    private static final Semaphore turnGoku = new Semaphore(1);  
    private static final Semaphore turnVegeta = new Semaphore(0); 

    private static final AtomicBoolean winnerDeclared = new AtomicBoolean(false);

    public SaiyanRaceSemaphore(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        final boolean isGoku = "Goku".equals(name);
        final Semaphore myTurn = isGoku ? turnGoku : turnVegeta;
        final Semaphore otherTurn = isGoku ? turnVegeta : turnGoku;

        try {
            while (!winnerDeclared.get() && distance < GOAL) {
                myTurn.acquire();
                if (winnerDeclared.get()) {
                    otherTurn.release(); 
                    break;
                }
                int step = ThreadLocalRandom.current().nextInt(1, 11);
                distance += step;
                System.out.println(name + " avanzó " + step + " metros. Distancia total: " + distance + " metros.");

                if (distance >= GOAL) {
                    if (winnerDeclared.compareAndSet(false, true)) {
                        System.out.println(name + " ha ganado la carrera!");
                    }
                }
                otherTurn.release();

                try {
                    Thread.sleep(ThreadLocalRandom.current().nextInt(200, 401));
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    otherTurn.release();
                    break;
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            (isGoku ? turnVegeta : turnGoku).release();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread goku = new Thread(new SaiyanRaceSemaphore("Goku"));
        Thread vegeta = new Thread(new SaiyanRaceSemaphore("Vegeta"));

        goku.start();
        vegeta.start();

        goku.join();
        vegeta.join();
    }
}
```

---

## Realiza los siguientes ejercicios:

### Haciendo funcionar un semáforo casero

<img src="https://3.bp.blogspot.com/-UcSwiKmEhqM/V1pJzcos47I/AAAAAAAABrk/9bjVkzf0oh44V0QmHXnU52Wxg1mhWkV_gCKgB/s1600/Imagen1SEMA%2BJ.png" width="100px">

_Simula un semáforo de tráfico con tres estados_: __ROJO, ÁMBAR, VERDE__.

Crea un programa que:

- Muestra en consola el color actual.
- Espera un tiempo según el color (__ROJO 3s, VERDE 3s, ÁMBAR 1s__).
- Cambia al siguiente color y repite en bucle.
-El programa principal _(main)_ debe poder parar la simulación después de __20 segundos__.

Objetivo:

- Trabajar con Thread.sleep() y controlar la vida de un hilo.

Clase _ColorSemaforo_:

- color: __ROJO, AMBAR, VERDE__.
- semaforo: para permitir cambiar de estado de color.

### 🎯 Mejorando la solución

Implementa una alternancia con semaforos donde el orden siempre sea __ROJO, VERDE, ÁMBAR,ROJO,...__. Para ello crea la solución en la clase _ColorSemaforoMejorado_.

### Laboratorio de Construcción

Disponemos de un laboratorio con _4 equipos_ para desarrollar, pero tenemos _6 estudiantes_ que necesitan usar los equipos para realizar el ejercicio que le ha propuesto el profesor, pudiendo sólo _4 hacerlo al mismo tiempo_ por la limitación de equipos. Implementa la clase _Estudiante_ haciendo uso de semáforos. para controlar el acceso a los equipos disponibles. Cada estudiante debe de imprimir un mensaje cuando comienza a utilizar el equipo: _El estudiante_ __1__ _ha comenzado a utilizar el equipo_, y cuando finaliza: _El estudiante_ __1__ _ha finalizado con el equipo_. Cada estudiante hace uso del equipo entre __3__ y __5__ _segundos_.

```java
class Estudiante extends Thread {

    private String nombre;
    private Semaphore semaforo;

...
}
```

### 🎯 Mejorando la solución

Mejora la solución anterior en la clase _EstudianteMejorado_, donde se indique el equipo informático que ha usado. Para ello has uso de la función _availablePermits() + 1_.

## Licencia

Este proyecto está bajo **Apache 2.0** — consulta el archivo `LICENSE` del repositorio original:
- https://github.com/jpexposito/code-learn-practice/blob/main/LICENSE

> Realiza __TESTING__ para verificar el correcto funcionamiento.

## Licencia 📄

Este proyecto está bajo la Licencia (Apache 2.0) - mira el archivo [LICENSE.md]([../../../LICENSE.md](https://github.com/jpexposito/code-learn-practice/blob/main/LICENSE)) para detalles.