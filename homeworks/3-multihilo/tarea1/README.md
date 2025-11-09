# Code, Learn & Practice(Programación de Servicios y Procesos: "Comenzando con Hilos en Java")

> Los **10 ejercicios** están en este README en **pseudocódigo** con sus **tests** (también en pseudocódigo). Puedes crear sus clases Java bajo `src/main/java` y sus tests en `src/test/java` siguiendo el patrón del ejemplo.

---

## Ejemplo ejecutable: Carrera entre Goku y Vegeta

<div align="center">

<img src=https://cinepremiere.com.mx/wp-content/uploads/2021/01/Goku-vs-Vegeta.jpg width="400">

</div>

### Código Java

```java
import java.util.Random;

public class SaiyanRace implements Runnable {
    private String name;
    private int distance = 0;
    private static final int GOAL = 100;
    private static volatile boolean winnerDeclared = false;

    public SaiyanRace(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        Random random = new Random();
        while (distance < GOAL && !winnerDeclared) {
            int step = random.nextInt(10) + 1; // Avance aleatorio de 1 a 10
            distance += step;
            System.out.println(name + " avanzó " + step + " metros. Distancia total: " + distance + " metros.");

            if (distance >= GOAL && !winnerDeclared) {
                winnerDeclared = true;
                System.out.println(name + " ha ganado la carrera!");
            }

            try {
                Thread.sleep(500); // Pausa entre pasos
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        Thread goku = new Thread(new SaiyanRace("Goku"));
        Thread vegeta = new Thread(new SaiyanRace("Vegeta"));

        goku.start();
        vegeta.start();
    }
}
```

### Test JUnit

```java
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SaiyanRaceTest {

    @Test
    public void testSaiyanRace() throws InterruptedException {
        // Redirigir la salida estándar
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Ejecutar los hilos
        Thread goku = new Thread(new SaiyanRace("Goku"));
        Thread vegeta = new Thread(new SaiyanRace("Vegeta"));

        goku.start();
        vegeta.start();

        goku.join();   // Esperar a que termine Goku
        vegeta.join(); // Esperar a que termine Vegeta

        // Verificar si uno de ellos ganó
        String output = outContent.toString();
        assertTrue(output.contains("Goku ha ganado la carrera!") || output.contains("Vegeta ha ganado la carrera!"));
    }
}
```

---

## Guía de portado del pseudocódigo a Java

- `atomic boolean` → `java.util.concurrent.atomic.AtomicBoolean` o `volatile boolean` según el caso.
- `mutex`/`cond` → `java.util.concurrent.locks.ReentrantLock` y `Condition`.
- `BlockingQueue<T>` → `java.util.concurrent.ArrayBlockingQueue` o `LinkedBlockingQueue`.
- `RandomInt(a,b)` → `ThreadLocalRandom.current().nextInt(a, b+1)`.
- `Sleep(ms)` → `Thread.sleep(ms)` (captura `InterruptedException` y usa `Thread.currentThread().interrupt()` al propagar).
- Para alternancia por turno, usa `Condition.await()` / `signal()` bajo el mismo `Lock`.
- Para tests que dependen de salida estándar, usa `ByteArrayOutputStream` + `System.setOut` como en el ejemplo.

---

## Ejercicios (pseudocódigo + tests)

> Los siguientes ejercicios están en **pseudocódigo** y cada uno incluye un **test de verificación**. Puedes convertirlos a Java siguiendo la guía anterior.

---

### Ejercicio 1: Batalla Pokémon

<div align="center">

<img src=https://wallpapers.com/images/hd/pokemon-battle-background-snpr84zisrk1qj2b.jpg width="400">

</div>

**Pseudocódigo**

```text
class BatallaPokemon {
    atomic boolean juegoTerminado = false
    int hpPikachu = 100
    int hpCharmander = 100
    String turno = "Pikachu"        // alternancia estricta
    mutex m
    cond turnoCambio

    procedure atacar(atacante, ref hpObjetivo):
        daño = RandomInt(5, 20)
        hpObjetivo = hpObjetivo - daño
        print(atacante + " ataca con " + daño + " de daño. HP rival: " + hpObjetivo)
        if hpObjetivo <= 0 and not juegoTerminado:
            juegoTerminado = true
            print(atacante + " ha ganado la batalla!")
        Sleep(RandomInt(200, 600))

    runnable HiloPikachu:
        while not juegoTerminado:
            lock(m)
            while turno != "Pikachu" and not juegoTerminado:
                wait(turnoCambio, m)
            if juegoTerminado: unlock(m); break
            atacar("Pikachu", hpCharmander)
            turno = "Charmander"
            signal(turnoCambio)
            unlock(m)

    runnable HiloCharmander:
        while not juegoTerminado:
            lock(m)
            while turno != "Charmander" and not juegoTerminado:
                wait(turnoCambio, m)
            if juegoTerminado: unlock(m); break
            atacar("Charmander", hpPikachu)
            turno = "Pikachu"
            signal(turnoCambio)
            unlock(m)

    procedure main():
        t1 = Thread.start(HiloPikachu)
        t2 = Thread.start(HiloCharmander)
        t1.join(); t2.join()
}
```

**Test**

```text
test BatallaPokemon_debeHaberGanador():
    capturarSalida()
    juego = new BatallaPokemon()
    juego.main()
    salida = obtenerSalida()
    assert( salida.contains("ha ganado la batalla!") )
    assert( juego.juegoTerminado == true )
    assert( juego.hpPikachu <= 0 or juego.hpCharmander <= 0 )
```

---

### Ejercicio 2: Caza de Horrocruxes

<div align="center">

<img src=https://static1.colliderimages.com/wordpress/wp-content/uploads/2021/11/harry-potter-horcruxes.jpg width="400">

</div>

**Pseudocódigo**

```text
class CazaHorrocruxes {
    atomic boolean encontrado = false
    atomic String ganador = null

    runnable Buscador(nombre, ubicacion):
        tiempo = RandomInt(500, 2000)
        Sleep(tiempo)
        if not encontrado:
            encontrado = true
            ganador = nombre
            print(nombre + " encontró un Horrocrux en " + ubicacion + ". ¡Búsqueda terminada!")

    procedure main():
        t1 = Thread.start(Buscador("Harry", "Bosque Prohibido"))
        t2 = Thread.start(Buscador("Hermione", "Biblioteca Antigua"))
        t3 = Thread.start(Buscador("Ron", "Mazmorras del castillo"))
        t1.join(); t2.join(); t3.join()
}
```

**Test**

```text
test CazaHorrocruxesUnGanadorYUnSoloHallazgo():
    capturarSalida()
    sim = new CazaHorrocruxes()
    sim.main()
    salida = obtenerSalida()
    assert( sim.encontrado == true )
    assert( sim.ganador in ["Harry","Hermione","Ron"] )
    assert( salida.count("encontró un Horrocrux") == 1 )
```

---

### Ejercicio 3: Fábrica de Droids

<div align="center">

<img src=https://i.ytimg.com/vi/z3r2dBzs6B4/maxresdefault.jpg width="400">

</div>

**Pseudocódigo**

```text
class FabricaDroids {
    BlockingQueue<String> ensamblados = new BlockingQueue()
    int N = 10
    atomic int activados = 0

    runnable Ensamblador:
        for i in 1..N:
            Sleep(RandomInt(100, 300))
            d = "Droid-" + i
            print("Ensamblado " + d)
            ensamblados.put(d)

    runnable Activador:
        local int cuenta = 0
        while cuenta < N:
            d = ensamblados.take()
            print("Activado " + d)
            activados = activados + 1
            cuenta = cuenta + 1
            Sleep(RandomInt(50, 150))

    procedure main():
        tE = Thread.start(Ensamblador)
        tA = Thread.start(Activador)
        tE.join(); tA.join()
}
```

**Test**

```text
test FabricaDroidsNoSeActivaAntesDeEnsamblaryCuentaCorrecta():
    capturarSalida()
    fab = new FabricaDroids()
    fab.main()
    salida = obtenerSalida()
    for k in 1..fab.N:
        idxE = salida.indexOf("Ensamblado Droid-" + k)
        idxA = salida.indexOf("Activado Droid-" + k)
        assert( idxE != -1 and idxA != -1 and idxE < idxA )
    assert( fab.activados == fab.N )
```

---

### Ejercicio 4: Competencia de Quidditch

<div align="center">

<img src=https://www.aventurasnerd.com/wp-content/uploads/2024/07/unnamed-4-1.jpg width="400">

</div>

**Pseudocódigo**

```text
class Quidditch {
    atomic boolean snitchAtrapada = false
    int puntosEquipoA = 0
    int puntosEquipoB = 0
    mutex m

    runnable CazadorA:
        while not snitchAtrapada:
            Sleep(RandomInt(200, 500))
            g = RandomInt(0,1) * 10
            if g > 0:
                lock(m); puntosEquipoA += g; unlock(m)
                print("Equipo A anota 10. Total A=" + puntosEquipoA)

    runnable CazadorB:
        while not snitchAtrapada:
            Sleep(RandomInt(200, 500))
            g = RandomInt(0,1) * 10
            if g > 0:
                lock(m); puntosEquipoB += g; unlock(m)
                print("Equipo B anota 10. Total B=" + puntosEquipoB)

    runnable Buscador:
        while not snitchAtrapada:
            Sleep(RandomInt(300, 700))
            if RandomInt(1,100) <= 15:
                snitchAtrapada = true
                print("¡Snitch dorada atrapada!")

    procedure main():
        t1 = Thread.start(CazadorA)
        t2 = Thread.start(CazadorB)
        t3 = Thread.start(Buscador)
        t1.join(); t2.join(); t3.join()
        print("Marcador final: A=" + puntosEquipoA + " B=" + puntosEquipoB)
}
```

**Test**

```text
test Quidditch_terminaCuandoSnitchAtrapada():
    capturarSalida()
    q = new Quidditch()
    q.main()
    salida = obtenerSalida()
    assert( salida.contains("¡Snitch dorada atrapada!") )
    assert( salida.contains("Marcador final:") )
```

---

### Ejercicio 5: Exploradores Jedi

<div align="center">

<img src=https://cdn1.forevergeek.com/uploads/33/2022/11/123-1140x641.jpg width="400">

</div>

**Pseudocódigo**

```text
class ExploradoresJedi {
    atomic boolean pistaEncontrada = false
    atomic String ganador = null

    runnable Jedi(nombre, planeta):
        Sleep(RandomInt(400, 1500))
        if not pistaEncontrada:
            pistaEncontrada = true
            ganador = nombre
            print(nombre + " halló una pista en " + planeta + ". Fin de búsqueda.")

    procedure main():
        t1 = Thread.start(Jedi("Kenobi", "Tatooine"))
        t2 = Thread.start(Jedi("Skywalker", "Dagobah"))
        t1.join(); t2.join()
}
```

**Test**

```text
test ExploradoresJediUnSoloGanador():
    capturarSalida()
    e = new ExploradoresJedi()
    e.main()
    salida = obtenerSalida()
    assert( e.pistaEncontrada == true )
    assert( salida.count("halló una pista") == 1 )
```

---

### Ejercicio 6: Simulación de la Tardis

<div align="center">

<img src=https://static1.srcdn.com/wordpress/wp-content/uploads/2023/03/doctor-who-tardis-rooms.jpg width="400">

</div>


**Pseudocódigo**

```text
class Tardis {
    atomic boolean destinoAlcanzado = false
    atomic String eraGanadora = null

    runnable Viaje(era):
        tiempo = RandomInt(500, 2000)
        Sleep(tiempo)
        if not destinoAlcanzado:
            destinoAlcanzado = true
            eraGanadora = era
            print("La TARDIS llegó primero a " + era)

    procedure main():
        eras = ["Roma Antigua", "Futuro Lejano", "Era Victoriana", "Año 3000"]
        hilos = []
        for e in eras: hilos.add(Thread.start(Viaje(e)))
        for t in hilos: t.join()
}
```

**Test**

```text
test TardisExisteUnaEraGanadora():
    capturarSalida()
    t = new TARDIS()
    t.main()
    salida = obtenerSalida()
    assert( t.destinoAlcanzado == true )
    assert( t.eraGanadora != null )
    assert( salida.count("llegó primero") == 1 )
```

---

### Ejercicio 7: Superhéroes salvando la ciudad

<div align="center">

<img src=https://img.freepik.com/fotos-premium/grupo-diversos-superheroes-unen-horizonte-ciudad_14117-766641.jpg width="400">

</div>

**Pseudocódigo**

```text
class CiudadEnPeligro {
    atomic boolean amenazaNeutralizada = false
    list zonasA = ["Norte","Centro","Este"]
    list zonasB = ["Oeste","Sur"]
    atomic String ganador = null

    runnable Superman:
        for zona in zonasA:
            if amenazaNeutralizada: break
            Sleep(RandomInt(200, 500))
            print("Superman salvó " + zona)
        if not amenazaNeutralizada:
            amenazaNeutralizada = true
            ganador = "Superman"
            print("Amenaza neutralizada por Superman")

    runnable Batman:
        for zona in zonasB:
            if amenazaNeutralizada: break
            Sleep(RandomInt(300, 600))
            print("Batman salvó " + zona)
        if not amenazaNeutralizada:
            amenazaNeutralizada = true
            ganador = "Batman"
            print("Amenaza neutralizada por Batman")

    procedure main():
        t1 = Thread.start(Superman)
        t2 = Thread.start(Batman)
        t1.join(); t2.join()
}
```

**Test**

```text
test CiudadEnPeligroSoloNeutralizaElOtroSeDetiene():
    capturarSalida()
    c = new CiudadEnPeligro()
    c.main()
    salida = obtenerSalida()
    assert( c.amenazaNeutralizada == true )
    assert( c.ganador in ["Superman","Batman"] )
    assert( salida.count("Amenaza neutralizada") == 1 )
```
---

### Ejercicio 8: Thor vs Hulk - Competencia de fuerza

<div align="center">

<img src=https://images4.alphacoders.com/874/874960.jpg width="400">

</div>

**Pseudocódigo**

```text
class FuerzaThorHulk {
    durationMS = 5000
    atomic boolean tiempoTerminado = false
    atomic int totalThor = 0
    atomic int totalHulk = 0

    runnable Temporizador:
        Sleep(durationMS)
        tiempoTerminado = true
        print("¡Tiempo!")

    runnable Thor:
        while not tiempoTerminado:
            peso = RandomInt(5, 20)
            totalThor += peso
            Sleep(RandomInt(50, 120))

    runnable Hulk:
        while not tiempoTerminado:
            peso = RandomInt(5, 20)
            totalHulk += peso
            Sleep(RandomInt(50, 120))

    procedure main():
        t0 = Thread.start(Temporizador)
        t1 = Thread.start(Thor)
        t2 = Thread.start(Hulk)
        t0.join(); t1.join(); t2.join()
        if totalThor > totalHulk:
            print("Thor gana con " + totalThor + " vs " + totalHulk)
        else if totalHulk > totalThor:
            print("Hulk gana con " + totalHulk + " vs " + totalThor)
        else:
            print("Empate: " + totalThor)
}
```

**Test**

```text
test FuerzaThorHulk_terminaPorTiempo_yDeclaraResultado():
    capturarSalida()
    f = new FuerzaThorHulk()
    f.main()
    salida = obtenerSalida()
    assert( salida.contains("¡Tiempo!") )
    assert( salida.contains("gana") or salida.contains("Empate") )
```

---

### Ejercicio 9: Simulación del Millenium Falcon

<div align="center">

<img src=https://i.ytimg.com/vi/ONmXxHHgxYw/maxresdefault.jpg width="400">

</div>

**Pseudocódigo**

```text
class MilleniumFalcon {
    atomic boolean fin = false
    atomic boolean destruida = false
    int tiempoMisionMS = 4000
    long inicio

    atomic int velocidad = 0
    atomic int escudos = 100

    runnable HanSolo:
        while not fin:
            velocidad += RandomInt(5, 15)
            if RandomInt(1,100) <= 5:
                destruida = true; fin = true
                print("Fallo de hiperimpulsor. ¡La nave se destruye!")
            Sleep(150)
            if now() - inicio >= tiempoMisionMS: fin = true

    runnable Chewbacca:
        while not fin:
            escudos += RandomInt(-10, +5)
            if escudos <= 0:
                destruida = true; fin = true
                print("¡Escudos agotados! La nave se destruye!")
            Sleep(150)
            if now() - inicio >= tiempoMisionMS: fin = true

    procedure main():
        inicio = now()
        t1 = Thread.start(HanSolo)
        t2 = Thread.start(Chewbacca)
        t1.join(); t2.join()
        if not destruida:
            print("¡Han y Chewie escapan! Vel=" + velocidad + ", Escudos=" + escudos)
}
```

**Test**

```text
test MilleniumFalcon_finalizaConEscapeODestruccion():
    capturarSalida()
    m = new MilleniumFalcon()
    m.main()
    salida = obtenerSalida()
    assert( salida.contains("se destruye") xor salida.contains("escapan") )
```

---

### Ejercicio 10: Batalla entre magos

<div align="center">

<img src=https://www.divulgadoresdelmisterio.net/wp-content/uploads/2020/09/batalla-de-magos.jpg width="400">

</div>

**Pseudocódigo**

```text
class BatallaMagos {
    int energiaGandalf = 120
    int energiaSaruman = 120
    atomic boolean combateTerminado = false
    mutex m

    procedure lanzarHechizo(atacante, ref energiaRival):
        daño = RandomInt(8, 25)
        energiaRival = energiaRival - daño
        print(atacante + " lanza hechizo por " + daño + ". Energía rival: " + energiaRival)
        if energiaRival <= 0 and not combateTerminado:
            combateTerminado = true
            print(atacante + " gana la batalla mágica.")

    runnable Gandalf:
        while not combateTerminado:
            lock(m)
            if not combateTerminado: lanzarHechizo("Gandalf", energiaSaruman)
            unlock(m)
            Sleep(RandomInt(200, 600))

    runnable Saruman:
        while not combateTerminado:
            lock(m)
            if not combateTerminado: lanzarHechizo("Saruman", energiaGandalf)
            unlock(m)
            Sleep(RandomInt(200, 600))

    procedure main():
        t1 = Thread.start(Gandalf)
        t2 = Thread.start(Saruman)
        t1.join(); t2.join()
}
```

**Test**
```text
test BatallaMagos_debeHaberGanadorYTerminar():
    capturarSalida()
    b = new BatallaMagos()
    b.main()
    salida = obtenerSalida()
    assert( salida.contains("gana la batalla mágica.") )
    assert( b.combateTerminado == true )
    assert( b.energiaGandalf <= 0 or b.energiaSaruman <= 0 )
```

---

## Licencia

Este proyecto está bajo **Apache 2.0** — consulta el archivo `LICENSE` del repositorio original:
- https://github.com/jpexposito/code-learn-practice/blob/main/LICENSE

> Realiza __TESTING__ para verificar el correcto funcionamiento.

## Licencia 📄

Este proyecto está bajo la Licencia (Apache 2.0) - mira el archivo [LICENSE.md]([../../../LICENSE.md](https://github.com/jpexposito/code-learn-practice/blob/main/LICENSE)) para detalles.