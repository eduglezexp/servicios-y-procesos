# Code, Learn & Practice (Programación de Servicios y Procesos)

## Instrucciones de la práctica

En esta práctica se trabajará en **dos fases complementarias**:

1. **Parte teórica:** Responde a los conceptos solicitados mediante la **búsqueda de información confiable**, citando siempre las **fuentes consultadas** al final de cada respuesta.  
2. **Parte práctica:** Ejecuta en Linux los **comandos indicados** y muestra la **salida obtenida** junto con una breve explicación de su significado.  

El objetivo es afianzar la comprensión de los **procesos en sistemas operativos**, tanto desde el punto de vista conceptual como práctico.  

## Bloque 1: Conceptos básicos (teoría)

### Ejercicio 0 (ejemplo resuelto)  

**Pregunta:** Explica la diferencia entre hardware, sistema operativo y aplicación.  

**Respuesta:**  

- **Hardware**: parte física (CPU, memoria, disco, etc.).  
- **Sistema Operativo (SO)**: software que gestiona el hardware y ofrece servicios a las aplicaciones (ejemplo: Linux, Windows).  
- **Aplicación**: programas que usa el usuario y que se apoyan en el SO (ejemplo: navegador, editor de texto).  

---

1. Define qué es un **proceso** y en qué se diferencia de un **programa**.

    <details>
    <summary>Respuesta</summary>

    </details>

</br>

2. Explica qué es el **kernel** y su papel en la gestión de procesos.  

    <details>
    <summary>Respuesta</summary>
    
    </details>

</br>

3. ¿Qué son **PID** y **PPID**? Explica con un ejemplo.  

    <details>
    <summary>Respuesta</summary>
    
    </details>

</br>

4. Describe qué es un **cambio de contexto** y por qué es costoso.  

    <details>
    <summary>Respuesta</summary>
    
    </details>

</br>

5. Explica qué es un **PCB (Process Control Block)** y qué información almacena.  

    <details>
    <summary>Respuesta</summary>
    
    </details>

</br>

6. Diferencia entre **proceso padre** y **proceso hijo**.  

    <details>
    <summary>Respuesta</summary>
    
    </details>

</br>

7. Explica qué ocurre cuando un proceso queda **huérfano** en Linux.  

    <details>
    <summary>Respuesta</summary>
    
    </details>

</br>

8. ¿Qué es un proceso **zombie**? Da un ejemplo de cómo puede ocurrir.  

    <details>
    <summary>Respuesta</summary>
    
    </details>

</br>

9. Diferencia entre **concurrencia** y **paralelismo**.  

    <details>
    <summary>Respuesta</summary>
    
    </details>

</br>

10. Explica qué es un **hilo (thread)** y en qué se diferencia de un proceso.  

    <details>
    <summary>Respuesta</summary>
    
    </details>

</br>

---

## Bloque 2: Práctica con comandos en Linux

### Ejercicio 0 (ejemplo resuelto)  

**Pregunta:** ¿Qué comando muestra el directorio actual?  

**Respuesta:**  

```bash
pwd
```

11. Usa echo $$ para mostrar el PID del proceso actual.

    <details>
    <summary>Respuesta</summary>
    
    </details>

</br>

12. Usa echo $PPID para mostrar el PID del proceso padre.

    <details>
    <summary>Respuesta</summary>
    
    </details>

</br>

13. Ejecuta pidof systemd y explica el resultado.

    <details>
    <summary>Respuesta</summary>
    
    </details>

</br>

14. Abre un programa gráfico (ejemplo: gedit) y usa pidof para obtener sus PID.

    <details>
    <summary>Respuesta</summary>
    
    </details>

</br>

15. Ejecuta ps -e y explica qué significan sus columnas.

    <details>
    <summary>Respuesta</summary>
    
    </details>

</br>

16. Ejecuta ps -f y observa la relación entre procesos padre e hijo.

    <details>
    <summary>Respuesta</summary>
    
    </details>

</br>

17. Usa ps -axf o pstree para mostrar el árbol de procesos y dibújalo.

    <details>
    <summary>Respuesta</summary>
    
    </details>

</br>

18. Ejecuta top o htop y localiza el proceso con mayor uso de CPU.

    <details>
    <summary>Respuesta</summary>
    
    </details>

</br>

19. Ejecuta sleep 100 en segundo plano y busca su PID con ps.

    <details>
    <summary>Respuesta</summary>
    
    </details>

</br>

20. Finaliza un proceso con kill <PID> y comprueba con ps que ya no está.

    <details>
    <summary>Respuesta</summary>
    
    </details>

</br>

### Ejercicio 0 (ejemplo resuelto)  

**Pregunta:** ¿Qué comando muestra el árbol de procesos en Linux?

**Respuesta:**  

```bash
pstree
```

## Bloque 3: Procesos y jerarquía

21. Identifica el **PID del proceso init/systemd** y explica su función.

    <details>
    <summary>Respuesta</summary>
    
    </details>

</br>

22. Explica qué ocurre con el **PPID** de un proceso hijo si su padre termina antes.

    <details>
    <summary>Respuesta</summary>
    
    </details>

</br>

23. Ejecuta un programa que genere varios procesos hijos y observa sus PIDs con `ps`.

    <details>
    <summary>Respuesta</summary>
    
    </details>

</br>

24. Haz que un proceso quede en **estado suspendido** con `Ctrl+Z` y réanúdalo con `fg`.

    <details>
    <summary>Respuesta</summary>
    
    </details>

</br>

25. Lanza un proceso en **segundo plano** con `&` y obsérvalo con `jobs`.

    <details>
    <summary>Respuesta</summary>
    
    </details>

</br>

26. Explica la diferencia entre los estados de un proceso: **Running, Sleeping, Zombie, Stopped**.

    <details>
    <summary>Respuesta</summary>
    
    </details>

</br>

27. Usa `ps -eo pid,ppid,stat,cmd` para mostrar los estados de varios procesos.

    <details>
    <summary>Respuesta</summary>
    
    </details>

</br>

28. Ejecuta `watch -n 1 ps -e` y observa cómo cambian los procesos en tiempo real.

    <details>
    <summary>Respuesta</summary>
    
    </details>

</br>

29. Explica la diferencia entre ejecutar un proceso con `&` y con `nohup`.

    <details>
    <summary>Respuesta</summary>
    
    </details>

</br>

30. Usa `ulimit -a` para ver los límites de recursos de procesos en tu sistema.

    <details>
    <summary>Respuesta</summary>
    
    </details>

</br>