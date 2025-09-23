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
    
    - **Proceso:** instancia de un programa en ejecución, con su propio espacio de memoria y recursos asignados.
    
    - **Programa:** conjunto de instrucciones almacenadas en disco que aún no se ejecuta.

    - **Diferencia:** el programa es estático, el proceso es dinámico.

    **Fuente:** [GeeksforGeeks - Difference between Program and Process (2025)](https://www.geeksforgeeks.org/operating-systems/difference-between-program-and-process/)
    </details>

</br>

2. Explica qué es el **kernel** y su papel en la gestión de procesos.  

    <details>
    <summary>Respuesta</summary>

    - El **kernel** es el núcleo del sistema operativo y se encarga de gestionar los recursos del sistema, como la CPU, la memoria y los dispositivos.

    - En la gestión de procesos, el **kernel** es responsable de:

      - Crear y destruir procesos.

      - Asignar tiempo de CPU a los procesos.

      - Administrar la memoria utilizada por cada proceso.

      - Coordinar la comunicación y sincronización entre procesos.

    **Fuente:** [GeeksforGeeks — Kernel in Operating System](https://www.geeksforgeeks.org/operating-systems/kernel-in-operating-system/)
    </details>

</br>

3. ¿Qué son **PID** y **PPID**? Explica con un ejemplo.  

    <details>
    <summary>Respuesta</summary>
    
    - **PID** (Process ID): Es un identificador único asignado a cada proceso en ejecución en el sistema. Este número permite al sistema operativo gestionar y controlar los procesos de manera eficiente.

    - **PPID** (Parent Process ID): Es el identificador del proceso padre que creó al proceso en cuestión. Cada proceso en Linux tiene un proceso padre, excepto el proceso inicial (generalmente init o systemd), que tiene un **PPID** de 0.

    **Ejemplo:**

    _Si ejecutamos el siguiente comando en la terminal:_

    ```bash
    ps -f
    ```

    _Podriamos ver una salida similar a esta:_

    ```
    UID        PID  PPID  C STIME TTY          TIME CMD
    user     12345     1  0 10:00 pts/0    00:00:00 bash
    user     12346 12345  0 10:01 pts/0    00:00:00 ps -f
    ```

    _En este caso:_

    - El proceso **bash** tiene **PID=12345** y su proceso padre es **init** o **systemd**, con **PPID=1.**

    - El proceso **ps** tiene **PID=12346** y su proceso padre es **bash**, con **PPID=12345**.

    **Fuente:** [LinuxHint — How to Find Parent Process ID (PPID) in Linux](https://linuxhint.com/find-parent-process-linux/)

    </details>

</br>

4. Describe qué es un **cambio de contexto** y por qué es costoso.  

    <details>
    <summary>Respuesta</summary>

    - Un **cambio de contexto** ocurre cuando la CPU guarda el estado de un proceso y carga otro.

    - Es costoso porque implica acceder a memoria y registros sin hacer trabajo útil directamente.

    **Fuente:** [GeeksforGeeks - Context Switching in OS (2025)](https://www.geeksforgeeks.org/context-switching-in-operating-system/)
    </details>

</br>

5. Explica qué es un **PCB (Process Control Block)** y qué información almacena.  

    <details>
    <summary>Respuesta</summary>

    El **PCB** es una estructura del kernel que guarda toda la información necesaria de un proceso, como:

    - Identificación (PID, PPID).

    - Estado del proceso.

    - Registros de CPU.

    - Información de memoria.

    - Recursos como archivos abiertos.

    **Fuente:** [GeeksforGeeks — Process Control Block (PCB) in OS (2025)](https://www.geeksforgeeks.org/operating-systems/process-control-block-in-os/)
    </details>

</br>

6. Diferencia entre **proceso padre** y **proceso hijo**.  

    <details>
    <summary>Respuesta</summary>
    
    - **Proceso padre:** es el proceso que crea a otro proceso mediante una llamada al sistema como fork().

    - **Proceso hijo:** es el proceso creado por el proceso padre, que hereda recursos como variables de entorno y descriptores de archivos.

    **Ejemplo:** la shell bash (proceso padre) crea un proceso hijo al ejecutar un comando como ls.

    **Fuente:** [Delightly Linux — What are PID and PPID?](https://delightlylinux.wordpress.com/2012/06/25/what-is-pid-and-ppid/)
    </details>

</br>

7. Explica qué ocurre cuando un proceso queda **huérfano** en Linux.  

    <details>
    <summary>Respuesta</summary>
    
    - Un proceso se considera huérfano cuando su proceso padre termina antes que él.

    - En sistemas Linux modernos, el proceso huérfano es adoptado por el proceso init o systemd (PID 1), que se encarga de gestionar la finalización del proceso huérfano.

    **Fuente:** [GeeksforGeeks — Orphan Processes in Linux](https://www.geeksforgeeks.org/operating-systems/orphan-processes/)
    </details>

</br>

8. ¿Qué es un proceso **zombie**? Da un ejemplo de cómo puede ocurrir.  

    <details>
    <summary>Respuesta</summary>

    - Un zombie es un proceso que ha terminado su ejecución, pero su entrada sigue presente en la tabla de procesos porque su proceso padre no ha leído su estado de salida mediante la llamada al sistema wait().

    - Esto ocurre cuando el proceso hijo termina, pero el proceso padre no recoge su estado de salida.

    - Los procesos zombie pueden identificarse en la salida del comando ps con un estado Z.

    **Fuente:** [Wikipedia — Zombie Process](https://en.wikipedia.org/wiki/Zombie_process)
    </details>

</br>

9. Diferencia entre **concurrencia** y **paralelismo**.  

    <details>
    <summary>Respuesta</summary>
    
    - **Concurrencia:** varias tareas avanzan intercaladas en el tiempo, usando un mismo CPU. Se logra con multitarea y planificación de procesos. No significa que se ejecuten exactamente al mismo tiempo, sino que progresan de manera que parece simultáneo.

    - **Paralelismo:** varias tareas se ejecutan realmente al mismo tiempo, en distintos núcleos o procesadores. Es posible solo si hay hardware con múltiples núcleos o procesadores.

    **Ejemplo:**

    - **Concurrencia:** un único núcleo alterna entre reproducir música y descargar un archivo.

    - **Paralelismo:** dos núcleos ejecutan música y descarga simultáneamente.

    **Fuente:** [GeeksforGeeks — Difference Between Concurrency and Parallelism](https://www.geeksforgeeks.org/operating-systems/difference-between-concurrency-and-parallelism/)
    </details>

</br>

10. Explica qué es un **hilo (thread)** y en qué se diferencia de un proceso.  

    <details>
    <summary>Respuesta</summary>
    
    - **Hilo (thread):** unidad de ejecución dentro de un proceso, que comparte memoria con otros hilos del mismo proceso.

    - **Diferencia con proceso:**

        - El proceso es independiente, con memoria propia.

        - El hilo es más ligero y comparte recursos.

    **Fuente:** [GeeksforGeeks - Difference between Process and Thread (2025)](https://www.geeksforgeeks.org/operating-systems/difference-between-process-and-thread/)

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

    2191

</br>

12. Usa echo $PPID para mostrar el PID del proceso padre.
    
    2165

</br>

13. Ejecuta pidof systemd y explica el resultado.
    
    1484 Nos muestra la cantidad de procesos asociados a systemd

</br>

14. Abre un programa gráfico (ejemplo: gedit) y usa pidof para obtener sus PID.

    Se usa xeyes como programa gráfico de ejemplo:
    ```bash
    xeyes &
    ```

    Y luego se obtiene su **PID** con pidof:
    ```bash
    pidof xeyes
    2356
    ```

</br>

15. Ejecuta ps -e y explica qué significan sus columnas.

    **PID:** identificador del proceso.

    **TTY:** terminal asociado al proceso (? si no hay).

    **TIME:** tiempo total de CPU usado.

    **CMD:** comando que ejecuta el proceso.
    
```bash
    PID TTY          TIME CMD
      1 ?        00:00:00 systemd
      2 ?        00:00:00 kthreadd
      3 ?        00:00:00 rcu_gp
      4 ?        00:00:00 rcu_par_gp
      5 ?        00:00:00 slub_flushwq
      6 ?        00:00:00 netns
      8 ?        00:00:00 kworker/0:0H-kblockd
      9 ?        00:00:01 kworker/u6:0-events_unbound
     10 ?        00:00:00 mm_percpu_wq
     11 ?        00:00:00 rcu_tasks_rude_
     12 ?        00:00:00 rcu_tasks_trace
     13 ?        00:00:00 ksoftirqd/0
     14 ?        00:00:00 rcu_sched
     15 ?        00:00:00 migration/0
     16 ?        00:00:00 idle_inject/0
     17 ?        00:00:00 kworker/0:1-cgroup_destroy
     18 ?        00:00:00 cpuhp/0
     19 ?        00:00:00 cpuhp/1
     20 ?        00:00:00 idle_inject/1
     21 ?        00:00:00 migration/1
     22 ?        00:00:00 ksoftirqd/1
     24 ?        00:00:00 kworker/1:0H-events_highpri
     25 ?        00:00:00 cpuhp/2
     26 ?        00:00:00 idle_inject/2
     27 ?        00:00:00 migration/2
     28 ?        00:00:00 ksoftirqd/2
     30 ?        00:00:00 kworker/2:0H-events_highpri
     31 ?        00:00:00 kdevtmpfs
     32 ?        00:00:00 inet_frag_wq
     33 ?        00:00:00 kauditd
     34 ?        00:00:00 khungtaskd
     35 ?        00:00:00 oom_reaper
     36 ?        00:00:00 writeback
     37 ?        00:00:00 kcompactd0
     38 ?        00:00:00 ksmd
     39 ?        00:00:00 khugepaged
     45 ?        00:00:00 kworker/2:1-events
     62 ?        00:00:00 kworker/1:1-events
     87 ?        00:00:00 kintegrityd
     88 ?        00:00:00 kblockd
     89 ?        00:00:00 blkcg_punt_bio
     90 ?        00:00:00 tpm_dev_wq
     91 ?        00:00:00 ata_sff
     92 ?        00:00:00 md
     93 ?        00:00:00 edac-poller
     94 ?        00:00:00 devfreq_wq
     95 ?        00:00:00 watchdogd
     97 ?        00:00:00 kworker/1:1H-kblockd
     99 ?        00:00:00 kswapd0
    100 ?        00:00:00 ecryptfs-kthrea
    102 ?        00:00:00 kthrotld
    103 ?        00:00:00 acpi_thermal_pm
    105 ?        00:00:00 scsi_eh_0
    106 ?        00:00:00 scsi_tmf_0
    107 ?        00:00:00 scsi_eh_1
    108 ?        00:00:00 scsi_tmf_1
    110 ?        00:00:00 vfio-irqfd-clea
    111 ?        00:00:00 mld
    112 ?        00:00:00 kworker/2:1H-kblockd
    113 ?        00:00:00 ipv6_addrconf
    116 ?        00:00:00 kworker/u6:5-ext4-rsv-conversion
    117 ?        00:00:00 kworker/0:1H-kblockd
    125 ?        00:00:00 kstrp
    128 ?        00:00:00 zswap-shrink
    129 ?        00:00:00 kworker/u7:0
    134 ?        00:00:00 charger_manager
    170 ?        00:00:00 kworker/1:2-events
    178 ?        00:00:00 scsi_eh_2
    179 ?        00:00:00 scsi_tmf_2
    186 ?        00:00:00 cryptd
    196 ?        00:00:00 kworker/0:2-events
    219 ?        00:00:00 ttm_swap
    222 ?        00:00:00 irq/18-vmwgfx
    223 ?        00:00:00 card0-crtc0
    224 ?        00:00:00 card0-crtc1
    225 ?        00:00:00 card0-crtc2
    226 ?        00:00:00 card0-crtc3
    227 ?        00:00:00 card0-crtc4
    228 ?        00:00:00 card0-crtc5
    229 ?        00:00:00 card0-crtc6
    230 ?        00:00:00 card0-crtc7
    295 ?        00:00:00 jbd2/sda3-8
    296 ?        00:00:00 ext4-rsv-conver
    346 ?        00:00:00 systemd-journal
    365 ?        00:00:00 kworker/2:2-mm_percpu_wq
    384 ?        00:00:00 systemd-udevd
    533 ?        00:00:00 spl_system_task
    534 ?        00:00:00 spl_delay_taskq
    535 ?        00:00:00 spl_dynamic_tas
    536 ?        00:00:00 spl_kmem_cache
    537 ?        00:00:00 zvol
    538 ?        00:00:00 arc_prune
    539 ?        00:00:00 arc_evict
    540 ?        00:00:00 arc_reap
    541 ?        00:00:00 dbu_evict
    542 ?        00:00:00 dbuf_evict
    543 ?        00:00:00 z_vdev_file
    544 ?        00:00:00 l2arc_feed
    575 ?        00:00:00 systemd-resolve
    576 ?        00:00:00 systemd-timesyn
    590 ?        00:00:00 accounts-daemon
    591 ?        00:00:00 acpid
    592 ?        00:00:00 anacron
    593 ?        00:00:00 avahi-daemon
    595 ?        00:00:00 cron
    596 ?        00:00:00 dbus-daemon
    597 ?        00:00:00 NetworkManager
    602 ?        00:00:00 irqbalance
    610 ?        00:00:00 networkd-dispat
    614 ?        00:00:00 polkitd
    615 ?        00:00:00 rsyslogd
    617 ?        00:00:00 switcheroo-cont
    618 ?        00:00:00 systemd-logind
    619 ?        00:00:00 touchegg
    624 ?        00:00:00 udisksd
    630 ?        00:00:00 wpa_supplicant
    639 ?        00:00:00 zed
    664 ?        00:00:00 avahi-daemon
    733 ?        00:00:00 ModemManager
    767 ?        00:00:00 cupsd
    798 ?        00:00:00 cups-browsed
    806 ?        00:00:00 kerneloops
    811 ?        00:00:00 kerneloops
   1347 ?        00:00:00 lightdm
   1362 ?        00:00:00 VBoxService
   1372 tty7     00:00:08 Xorg
   1374 tty1     00:00:00 agetty
   1403 ?        00:00:00 rtkit-daemon
   1453 ?        00:00:00 upowerd
   1464 ?        00:00:00 lightdm
   1484 ?        00:00:00 systemd
   1485 ?        00:00:00 (sd-pam)
   1492 ?        00:00:00 pipewire
   1493 ?        00:00:00 pulseaudio
   1495 ?        00:00:00 dbus-daemon
   1497 ?        00:00:00 gnome-keyring-d
   1501 ?        00:00:00 cinnamon-sessio
   1634 ?        00:00:00 VBoxClient
   1636 ?        00:00:00 VBoxClient
   1644 ?        00:00:00 VBoxClient
   1646 ?        00:00:00 VBoxClient
   1650 ?        00:00:00 VBoxClient
   1651 ?        00:00:01 VBoxClient
   1656 ?        00:00:00 VBoxClient
   1658 ?        00:00:00 VBoxClient
   1742 ?        00:00:00 gvfsd
   1747 ?        00:00:00 gvfsd-fuse
   1752 ?        00:00:00 at-spi-bus-laun
   1759 ?        00:00:00 dbus-daemon
   1763 ?        00:00:00 at-spi2-registr
   1774 ?        00:00:00 csd-clipboard
   1775 ?        00:00:00 csd-media-keys
   1776 ?        00:00:00 csd-print-notif
   1777 ?        00:00:00 csd-keyboard
   1778 ?        00:00:00 csd-a11y-settin
   1779 ?        00:00:00 csd-background
   1780 ?        00:00:00 csd-wacom
   1781 ?        00:00:00 csd-color
   1791 ?        00:00:00 csd-screensaver
   1794 ?        00:00:00 csd-housekeepin
   1798 ?        00:00:00 csd-power
   1804 ?        00:00:00 csd-xsettings
   1813 ?        00:00:00 csd-automount
   1829 ?        00:00:00 colord
   1830 ?        00:00:00 dconf-service
   1850 ?        00:00:00 csd-printer
   1853 ?        00:00:00 gvfs-udisks2-vo
   1860 ?        00:00:00 gvfs-goa-volume
   1867 ?        00:00:00 goa-daemon
   1881 ?        00:00:00 goa-identity-se
   1888 ?        00:00:00 gvfs-gphoto2-vo
   1893 ?        00:00:00 gvfs-mtp-volume
   1897 ?        00:00:00 gvfs-afc-volume
   1906 ?        00:00:00 cinnamon-launch
   1911 ?        00:00:09 cinnamon
   1937 ?        00:00:00 xapp-sn-watcher
   1953 ?        00:00:00 evolution-alarm
   1954 ?        00:00:00 blueman-applet
   1955 ?        00:00:01 nemo-desktop
   1961 ?        00:00:00 agent
   1962 ?        00:00:00 polkit-gnome-au
   1965 ?        00:00:00 nm-applet
   1975 ?        00:00:00 cinnamon-killer
   2012 ?        00:00:00 evolution-sourc
   2052 ?        00:00:00 evolution-calen
   2070 ?        00:00:00 evolution-addre
   2098 ?        00:00:00 obexd
   2146 ?        00:00:00 gvfsd-trash
   2157 ?        00:00:00 gvfsd-metadata
   2165 ?        00:00:01 gnome-terminal-
   2191 pts/0    00:00:00 bash
   2220 ?        00:00:00 mintUpdate
   2268 ?        00:00:00 applet.py
   2271 ?        00:00:00 mintreport-tray
   2353 pts/0    00:00:00 xeyes
   2356 pts/0    00:00:00 xeyes
   2366 ?        00:00:00 kworker/u6:1-events_unbound
   2706 ?        00:00:00 packagekitd
   2760 ?        00:00:00 kworker/u6:2-ext4-rsv-conversion
   2761 pts/0    00:00:00 ps
```

</br>

16. Ejecuta ps -f y observa la relación entre procesos padre e hijo.

```bash
    UID          PID    PPID  C STIME TTY          TIME CMD
    eduglez+    2191    2165  0 23:05 pts/0    00:00:00 bash
    eduglez+    2353    2191  0 23:10 pts/0    00:00:00 xeyes
    eduglez+    2775    2191  0 23:27 pts/0    00:00:00 ps -f
```

</br>

17. Usa ps -axf o pstree para mostrar el árbol de procesos y dibújalo.

```bash
    PID TTY      STAT   TIME COMMAND
      2 ?        S      0:00 [kthreadd]
      3 ?        I<     0:00  \_ [rcu_gp]
      4 ?        I<     0:00  \_ [rcu_par_gp]
      5 ?        I<     0:00  \_ [slub_flushwq]
      6 ?        I<     0:00  \_ [netns]
      8 ?        I<     0:00  \_ [kworker/0:0H-kblockd]
      9 ?        I      0:01  \_ [kworker/u6:0-events_unbound]
     10 ?        I<     0:00  \_ [mm_percpu_wq]
     11 ?        S      0:00  \_ [rcu_tasks_rude_]
     12 ?        S      0:00  \_ [rcu_tasks_trace]
     13 ?        S      0:00  \_ [ksoftirqd/0]
     14 ?        I      0:00  \_ [rcu_sched]
     15 ?        S      0:00  \_ [migration/0]
     16 ?        S      0:00  \_ [idle_inject/0]
     17 ?        I      0:00  \_ [kworker/0:1-cgroup_destroy]
     18 ?        S      0:00  \_ [cpuhp/0]
     19 ?        S      0:00  \_ [cpuhp/1]
     20 ?        S      0:00  \_ [idle_inject/1]
     21 ?        S      0:00  \_ [migration/1]
     22 ?        S      0:00  \_ [ksoftirqd/1]
     24 ?        I<     0:00  \_ [kworker/1:0H-events_highpri]
     25 ?        S      0:00  \_ [cpuhp/2]
     26 ?        S      0:00  \_ [idle_inject/2]
     27 ?        S      0:00  \_ [migration/2]
     28 ?        S      0:00  \_ [ksoftirqd/2]
     30 ?        I<     0:00  \_ [kworker/2:0H-events_highpri]
     31 ?        S      0:00  \_ [kdevtmpfs]
     32 ?        I<     0:00  \_ [inet_frag_wq]
     33 ?        S      0:00  \_ [kauditd]
     34 ?        S      0:00  \_ [khungtaskd]
     35 ?        S      0:00  \_ [oom_reaper]
     36 ?        I<     0:00  \_ [writeback]
     37 ?        S      0:00  \_ [kcompactd0]
     38 ?        SN     0:00  \_ [ksmd]
     39 ?        SN     0:00  \_ [khugepaged]
     45 ?        I      0:00  \_ [kworker/2:1-events]
     62 ?        I      0:00  \_ [kworker/1:1-events]
     87 ?        I<     0:00  \_ [kintegrityd]
     88 ?        I<     0:00  \_ [kblockd]
     89 ?        I<     0:00  \_ [blkcg_punt_bio]
     90 ?        I<     0:00  \_ [tpm_dev_wq]
     91 ?        I<     0:00  \_ [ata_sff]
     92 ?        I<     0:00  \_ [md]
     93 ?        I<     0:00  \_ [edac-poller]
     94 ?        I<     0:00  \_ [devfreq_wq]
     95 ?        S      0:00  \_ [watchdogd]
     97 ?        I<     0:00  \_ [kworker/1:1H-kblockd]
     99 ?        S      0:00  \_ [kswapd0]
    100 ?        S      0:00  \_ [ecryptfs-kthrea]
    102 ?        I<     0:00  \_ [kthrotld]
    103 ?        I<     0:00  \_ [acpi_thermal_pm]
    105 ?        S      0:00  \_ [scsi_eh_0]
    106 ?        I<     0:00  \_ [scsi_tmf_0]
    107 ?        S      0:00  \_ [scsi_eh_1]
    108 ?        I<     0:00  \_ [scsi_tmf_1]
    110 ?        I<     0:00  \_ [vfio-irqfd-clea]
    111 ?        I<     0:00  \_ [mld]
    112 ?        I<     0:00  \_ [kworker/2:1H-kblockd]
    113 ?        I<     0:00  \_ [ipv6_addrconf]
    117 ?        I<     0:00  \_ [kworker/0:1H-kblockd]
    125 ?        I<     0:00  \_ [kstrp]
    128 ?        I<     0:00  \_ [zswap-shrink]
    129 ?        I<     0:00  \_ [kworker/u7:0]
    134 ?        I<     0:00  \_ [charger_manager]
    170 ?        I      0:00  \_ [kworker/1:2-events]
    178 ?        S      0:00  \_ [scsi_eh_2]
    179 ?        I<     0:00  \_ [scsi_tmf_2]
    186 ?        I<     0:00  \_ [cryptd]
    196 ?        I      0:00  \_ [kworker/0:2-events]
    219 ?        I<     0:00  \_ [ttm_swap]
    222 ?        S      0:00  \_ [irq/18-vmwgfx]
    223 ?        S      0:00  \_ [card0-crtc0]
    224 ?        S      0:00  \_ [card0-crtc1]
    225 ?        S      0:00  \_ [card0-crtc2]
    226 ?        S      0:00  \_ [card0-crtc3]
    227 ?        S      0:00  \_ [card0-crtc4]
    228 ?        S      0:00  \_ [card0-crtc5]
    229 ?        S      0:00  \_ [card0-crtc6]
    230 ?        S      0:00  \_ [card0-crtc7]
    295 ?        S      0:00  \_ [jbd2/sda3-8]
    296 ?        I<     0:00  \_ [ext4-rsv-conver]
    365 ?        I      0:00  \_ [kworker/2:2-events]
    533 ?        S<     0:00  \_ [spl_system_task]
    534 ?        S<     0:00  \_ [spl_delay_taskq]
    535 ?        S<     0:00  \_ [spl_dynamic_tas]
    536 ?        S<     0:00  \_ [spl_kmem_cache]
    537 ?        S<     0:00  \_ [zvol]
    538 ?        S      0:00  \_ [arc_prune]
    539 ?        S      0:00  \_ [arc_evict]
    540 ?        SN     0:00  \_ [arc_reap]
    541 ?        S      0:00  \_ [dbu_evict]
    542 ?        SN     0:00  \_ [dbuf_evict]
    543 ?        SN     0:00  \_ [z_vdev_file]
    544 ?        S      0:00  \_ [l2arc_feed]
   2760 ?        I      0:00  \_ [kworker/u6:2-events_unbound]
   2776 ?        I      0:00  \_ [kworker/u6:1-flush-8:0]
      1 ?        Ss     0:00 /sbin/init splash
    346 ?        S<s    0:00 /lib/systemd/systemd-journald
    384 ?        Ss     0:00 /lib/systemd/systemd-udevd
    575 ?        Ss     0:00 /lib/systemd/systemd-resolved
    576 ?        Ssl    0:00 /lib/systemd/systemd-timesyncd
    590 ?        Ssl    0:00 /usr/libexec/accounts-daemon
    591 ?        Ss     0:00 /usr/sbin/acpid
    593 ?        Ss     0:00 avahi-daemon: running [eduglezexp-VirtualBox.local]
    664 ?        S      0:00  \_ avahi-daemon: chroot helper
    595 ?        Ss     0:00 /usr/sbin/cron -f -P
    596 ?        Ss     0:00 @dbus-daemon --system --address=systemd: --nofork -
    597 ?        Ssl    0:00 /usr/sbin/NetworkManager --no-daemon
    602 ?        Ssl    0:00 /usr/sbin/irqbalance --foreground
    610 ?        Ss     0:00 /usr/bin/python3 /usr/bin/networkd-dispatcher --run
    614 ?        Ssl    0:00 /usr/libexec/polkitd --no-debug
    615 ?        Ssl    0:00 /usr/sbin/rsyslogd -n -iNONE
    617 ?        Ssl    0:00 /usr/libexec/switcheroo-control
    618 ?        Ss     0:00 /lib/systemd/systemd-logind
    619 ?        Ssl    0:01 /usr/bin/touchegg --daemon
    624 ?        Ssl    0:00 /usr/libexec/udisks2/udisksd
    630 ?        Ss     0:00 /sbin/wpa_supplicant -u -s -O /run/wpa_supplicant
    639 ?        Ssl    0:00 /usr/sbin/zed -F
    733 ?        Ssl    0:00 /usr/sbin/ModemManager
    767 ?        Ss     0:00 /usr/sbin/cupsd -l
    798 ?        Ssl    0:00 /usr/sbin/cups-browsed
    806 ?        Ss     0:00 /usr/sbin/kerneloops --test
    811 ?        Ss     0:00 /usr/sbin/kerneloops
   1347 ?        SLsl   0:00 /usr/sbin/lightdm
   1372 tty7     Ssl+   0:11  \_ /usr/lib/xorg/Xorg -core :0 -seat seat0 -auth /
   1464 ?        Sl     0:00  \_ lightdm --session-child 12 19
   1501 ?        Ssl    0:00      \_ cinnamon-session --session cinnamon
   1774 ?        Sl     0:00          \_ csd-clipboard
   1775 ?        Sl     0:00          \_ csd-media-keys
   1776 ?        Sl     0:00          \_ csd-print-notifications
   1777 ?        Sl     0:00          \_ csd-keyboard
   1778 ?        Sl     0:00          \_ csd-a11y-settings
   1779 ?        Sl     0:00          \_ csd-background
   1780 ?        Sl     0:00          \_ csd-wacom
   1781 ?        Sl     0:00          \_ csd-color
   1791 ?        Sl     0:00          \_ csd-screensaver-proxy
   1794 ?        Sl     0:00          \_ csd-housekeeping
   1798 ?        Sl     0:00          \_ csd-power
   1804 ?        Sl     0:00          \_ csd-xsettings
   1813 ?        Sl     0:00          \_ csd-automount
   1906 ?        Sl     0:00          \_ cinnamon-launcher
   1911 ?        Sl     0:13          |   \_ cinnamon --replace
   1937 ?        Sl     0:00          \_ /usr/lib/x86_64-linux-gnu/xapps/xapp-sn
   1953 ?        Sl     0:00          \_ /usr/libexec/evolution-data-server/evol
   1954 ?        Sl     0:00          \_ /usr/bin/python3 /usr/bin/blueman-apple
   1955 ?        Sl     0:01          \_ nemo-desktop
   1961 ?        Sl     0:00          \_ /usr/libexec/geoclue-2.0/demos/agent
   1962 ?        Sl     0:00          \_ /usr/lib/policykit-1-gnome/polkit-gnome
   1965 ?        Sl     0:00          \_ nm-applet
   1975 ?        Sl     0:00          \_ cinnamon-killer-daemon
   2268 ?        S      0:00          \_ /usr/bin/python3 /usr/share/system-conf
   1362 ?        Sl     0:00 /usr/sbin/VBoxService --pidfile /var/run/vboxadd-se
   1374 tty1     Ss+    0:00 /sbin/agetty -o -p -- \u --noclear tty1 linux
   1403 ?        SNsl   0:00 /usr/libexec/rtkit-daemon
   1453 ?        Ssl    0:00 /usr/libexec/upowerd
   1484 ?        Ss     0:00 /lib/systemd/systemd --user
   1485 ?        S      0:00  \_ (sd-pam)
   1492 ?        S<sl   0:00  \_ /usr/bin/pipewire
   1493 ?        S<sl   0:00  \_ /usr/bin/pulseaudio --daemonize=no --log-target
   1495 ?        Ss     0:00  \_ /usr/bin/dbus-daemon --session --address=system
   1742 ?        Ssl    0:00  \_ /usr/libexec/gvfsd
   2146 ?        Sl     0:00  |   \_ /usr/libexec/gvfsd-trash --spawner :1.8 /or
   1747 ?        Sl     0:00  \_ /usr/libexec/gvfsd-fuse /run/user/1000/gvfs -f
   1752 ?        Ssl    0:00  \_ /usr/libexec/at-spi-bus-launcher
   1759 ?        S      0:00  |   \_ /usr/bin/dbus-daemon --config-file=/usr/sha
   1763 ?        Sl     0:00  \_ /usr/libexec/at-spi2-registryd --use-gnome-sess
   1830 ?        Ssl    0:00  \_ /usr/libexec/dconf-service
   1853 ?        Ssl    0:00  \_ /usr/libexec/gvfs-udisks2-volume-monitor
   1860 ?        Ssl    0:00  \_ /usr/libexec/gvfs-goa-volume-monitor
   1867 ?        Sl     0:00  \_ /usr/libexec/goa-daemon
   1881 ?        Sl     0:00  \_ /usr/libexec/goa-identity-service
   1888 ?        Ssl    0:00  \_ /usr/libexec/gvfs-gphoto2-volume-monitor
   1893 ?        Ssl    0:00  \_ /usr/libexec/gvfs-mtp-volume-monitor
   1897 ?        Ssl    0:00  \_ /usr/libexec/gvfs-afc-volume-monitor
   2012 ?        Ssl    0:00  \_ /usr/libexec/evolution-source-registry
   2052 ?        Ssl    0:00  \_ /usr/libexec/evolution-calendar-factory
   2070 ?        Ssl    0:00  \_ /usr/libexec/evolution-addressbook-factory
   2098 ?        Ss     0:00  \_ /usr/lib/bluetooth/obexd
   2157 ?        Ssl    0:00  \_ /usr/libexec/gvfsd-metadata
   2165 ?        Ssl    0:01  \_ /usr/libexec/gnome-terminal-server
   2191 pts/0    Ss     0:00      \_ bash
   2353 pts/0    T      0:00          \_ xeyes
   2777 pts/0    R+     0:00          \_ ps -axf
   1497 ?        Sl     0:00 /usr/bin/gnome-keyring-daemon --daemonize --login
   1634 ?        S      0:00 /usr/bin/VBoxClient --clipboard
   1636 ?        Sl     0:00  \_ /usr/bin/VBoxClient --clipboard
   1644 ?        S      0:00 /usr/bin/VBoxClient --seamless
   1646 ?        Sl     0:00  \_ /usr/bin/VBoxClient --seamless
   1650 ?        S      0:00 /usr/bin/VBoxClient --draganddrop
   1651 ?        Sl     0:03  \_ /usr/bin/VBoxClient --draganddrop
   1656 ?        S      0:00 /usr/bin/VBoxClient --vmsvga
   1658 ?        Sl     0:00  \_ /usr/bin/VBoxClient --vmsvga
   1829 ?        Ssl    0:00 /usr/libexec/colord
   1850 ?        Sl     0:00 /usr/libexec/csd-printer
   2220 ?        Sl     0:00 mintUpdate
   2271 ?        Sl     0:01 mintreport-tray
   2706 ?        Ssl    0:00 /usr/libexec/packagekitd
```

</br>

18. Ejecuta top o htop y localiza el proceso con mayor uso de CPU.

```bash
    PID USUARIO   PR  NI    VIRT    RES    SHR S  %CPU  %MEM     HORA+ ORDEN
    1372 root      20   0  434824 144112  89204 S  14,6   3,7   0:16.56 Xorg
```

</br>

19. Ejecuta sleep 100 en segundo plano y busca su PID con ps.

```bash
    sleep 100 &

    ps aux | grep 'sleep 100'
    eduglez+    2786  0.0  0.0  10984  1020 pts/0    S    23:35   0:00 sleep 100
    eduglez+    2789  0.0  0.0  11712  2316 pts/0    S+   23:36   0:00 grep --color=auto sleep 100
```

</br>

20. Finaliza un proceso con kill <PID> y comprueba con ps que ya no está.

```bash
    kill 2786
    [2]-  Hecho                   sleep 100
```

</br>

### Ejercicio 0 (ejemplo resuelto)  

**Pregunta:** ¿Qué comando muestra el árbol de procesos en Linux?

**Respuesta:**  

```bash
pstree
```

## Bloque 3: Procesos y jerarquía

21. Identifica el **PID del proceso init/systemd** y explica su función.

    El PID es 1 y es el primer proceso que se inicia en el sistema, encargado de iniciar y gestionar otros procesos del sistema operativo.

```bash
    ps -p 1
    PID TTY          TIME CMD
    1 ?        00:00:00 systemd
```

</br>

22. Explica qué ocurre con el **PPID** de un proceso hijo si su padre termina antes.

    Ese proceso se le delega a init

</br>

23. Ejecuta un programa que genere varios procesos hijos y observa sus PIDs con `ps`.

```bash
    bash -c 'sleep 100 & sleep 100 & sleep 100 & wait'
    
    ps -f --ppid $!
    UID          PID    PPID  C STIME TTY          TIME CMD
    eduglez+    2801   2795  0 23:40 pts/0    00:00:00 sleep 100
    eduglez+    2802   2795  0 23:40 pts/0    00:00:00 sleep 100
    eduglez+    2803   2795  0 23:40 pts/0    00:00:00 sleep 100
    eduglez+    2795   2191  0 23:40 pts/0    00:00:00 bash -c sleep 100 & sleep 100 & sleep 100 & wait
    eduglez+    2804   2795  0 23:40 pts/0    00:00:00 wait
    eduglez+    2805   2191  0 23:40 pts/0    00:00:00 ps -f --ppid <PID_del_bash>
```

</br>

24. Haz que un proceso quede en **estado suspendido** con `Ctrl+Z` y réanúdalo con `fg`.

```bash
    sleep 100
    ^Z
    [1]+  Detenido                 sleep 100
    fg
    sleep 100
```

</br>

25. Lanza un proceso en **segundo plano** con `&` y obsérvalo con `jobs`.

```bash
    sleep 100 &
    [1] 2810
    jobs
    [1]+  Ejecutando                 sleep 100 &
```

</br>

26. Explica la diferencia entre los estados de un proceso: **Running, Sleeping, Zombie, Stopped**.

    - Running: El proceso está en ejecución
    - Sleeping: El proceso está inactivo, esperando un evento o recurso
    - Zombie: El proceso ha terminado pero su entrada en la tabla de procesos aún existe porque el padre no ha leído su estado de salida
    - Stopped: El proceso ha sido detenido, generalmente por una señal (como Ctrl+Z)

</br>

27. Usa `ps -eo pid,ppid,stat,cmd` para mostrar los estados de varios procesos.

```bash
        PID    PPID STAT CMD
      1       0 Ss   /sbin/init splash
      2       0 S    [kthreadd]
      3       2 I<   [rcu_gp]
      4       2 I<   [rcu_par_gp]
      5       2 I<   [slub_flushwq]
      6       2 I<   [netns]
      8       2 I<   [kworker/0:0H-kblockd]
     10       2 I<   [mm_percpu_wq]
     11       2 S    [rcu_tasks_rude_]
     12       2 S    [rcu_tasks_trace]
     13       2 S    [ksoftirqd/0]
     14       2 I    [rcu_sched]
     15       2 S    [migration/0]
     16       2 S    [idle_inject/0]
     18       2 S    [cpuhp/0]
     19       2 S    [cpuhp/1]
     20       2 S    [idle_inject/1]
     21       2 S    [migration/1]
     22       2 S    [ksoftirqd/1]
     24       2 I<   [kworker/1:0H-events_highpri]
     25       2 S    [cpuhp/2]
     26       2 S    [idle_inject/2]
     27       2 S    [migration/2]
     28       2 S    [ksoftirqd/2]
     30       2 I<   [kworker/2:0H-events_highpri]
     31       2 S    [kdevtmpfs]
     32       2 I<   [inet_frag_wq]
     33       2 S    [kauditd]
     34       2 S    [khungtaskd]
     35       2 S    [oom_reaper]
     36       2 I<   [writeback]
     37       2 S    [kcompactd0]
     38       2 SN   [ksmd]
     39       2 SN   [khugepaged]
     45       2 I    [kworker/2:1-events]
     62       2 I    [kworker/1:1-mm_percpu_wq]
     87       2 I<   [kintegrityd]
     88       2 I<   [kblockd]
     89       2 I<   [blkcg_punt_bio]
     90       2 I<   [tpm_dev_wq]
     91       2 I<   [ata_sff]
     92       2 I<   [md]
     93       2 I<   [edac-poller]
     94       2 I<   [devfreq_wq]
     95       2 S    [watchdogd]
     97       2 I<   [kworker/1:1H-kblockd]
     99       2 S    [kswapd0]
    100       2 S    [ecryptfs-kthrea]
    102       2 I<   [kthrotld]
    103       2 I<   [acpi_thermal_pm]
    105       2 S    [scsi_eh_0]
    106       2 I<   [scsi_tmf_0]
    107       2 S    [scsi_eh_1]
    108       2 I<   [scsi_tmf_1]
    110       2 I<   [vfio-irqfd-clea]
    111       2 I<   [mld]
    112       2 I<   [kworker/2:1H-kblockd]
    113       2 I<   [ipv6_addrconf]
    117       2 I<   [kworker/0:1H-kblockd]
    125       2 I<   [kstrp]
    128       2 I<   [zswap-shrink]
    129       2 I<   [kworker/u7:0]
    134       2 I<   [charger_manager]
    170       2 I    [kworker/1:2-events]
    178       2 S    [scsi_eh_2]
    179       2 I<   [scsi_tmf_2]
    186       2 I<   [cryptd]
    196       2 I    [kworker/0:2-events]
    219       2 I<   [ttm_swap]
    222       2 S    [irq/18-vmwgfx]
    223       2 S    [card0-crtc0]
    224       2 S    [card0-crtc1]
    225       2 S    [card0-crtc2]
    226       2 S    [card0-crtc3]
    227       2 S    [card0-crtc4]
    228       2 S    [card0-crtc5]
    229       2 S    [card0-crtc6]
    230       2 S    [card0-crtc7]
    295       2 S    [jbd2/sda3-8]
    296       2 I<   [ext4-rsv-conver]
    346       1 S<s  /lib/systemd/systemd-journald
    365       2 I    [kworker/2:2-events]
    384       1 Ss   /lib/systemd/systemd-udevd
    533       2 S<   [spl_system_task]
    534       2 S<   [spl_delay_taskq]
    535       2 S<   [spl_dynamic_tas]
    536       2 S<   [spl_kmem_cache]
    537       2 S<   [zvol]
    538       2 S    [arc_prune]
    539       2 S    [arc_evict]
    540       2 SN   [arc_reap]
    541       2 S    [dbu_evict]
    542       2 SN   [dbuf_evict]
    543       2 SN   [z_vdev_file]
    544       2 S    [l2arc_feed]
    575       1 Ss   /lib/systemd/systemd-resolved
    576       1 Ssl  /lib/systemd/systemd-timesyncd
    590       1 Ssl  /usr/libexec/accounts-daemon
    591       1 Ss   /usr/sbin/acpid
    593       1 Ss   avahi-daemon: running [eduglezexp-VirtualBox.local]
    595       1 Ss   /usr/sbin/cron -f -P
    596       1 Ss   @dbus-daemon --system --address=systemd: --nofork --nopidfile --systemd-activation --syslog-only
    597       1 Ssl  /usr/sbin/NetworkManager --no-daemon
    602       1 Ssl  /usr/sbin/irqbalance --foreground
    610       1 Ss   /usr/bin/python3 /usr/bin/networkd-dispatcher --run-startup-triggers
    614       1 Ssl  /usr/libexec/polkitd --no-debug
    615       1 Ssl  /usr/sbin/rsyslogd -n -iNONE
    617       1 Ssl  /usr/libexec/switcheroo-control
    618       1 Ss   /lib/systemd/systemd-logind
    619       1 Ssl  /usr/bin/touchegg --daemon
    624       1 Ssl  /usr/libexec/udisks2/udisksd
    630       1 Ss   /sbin/wpa_supplicant -u -s -O /run/wpa_supplicant
    639       1 Ssl  /usr/sbin/zed -F
    664     593 S    avahi-daemon: chroot helper
    733       1 Ssl  /usr/sbin/ModemManager
    767       1 Ss   /usr/sbin/cupsd -l
    798       1 Ssl  /usr/sbin/cups-browsed
    806       1 Ss   /usr/sbin/kerneloops --test
    811       1 Ss   /usr/sbin/kerneloops
   1347       1 SLsl /usr/sbin/lightdm
   1362       1 Sl   /usr/sbin/VBoxService --pidfile /var/run/vboxadd-service.sh
   1372    1347 Ssl+ /usr/lib/xorg/Xorg -core :0 -seat seat0 -auth /var/run/lightdm/root/:0 -nolisten tcp vt7 -novtswitch
   1374       1 Ss+  /sbin/agetty -o -p -- \u --noclear tty1 linux
   1403       1 SNsl /usr/libexec/rtkit-daemon
   1453       1 Ssl  /usr/libexec/upowerd
   1464    1347 Sl   lightdm --session-child 12 19
   1484       1 Ss   /lib/systemd/systemd --user
   1485    1484 S    (sd-pam)
   1492    1484 S<sl /usr/bin/pipewire
   1493    1484 S<sl /usr/bin/pulseaudio --daemonize=no --log-target=journal
   1495    1484 Ss   /usr/bin/dbus-daemon --session --address=systemd: --nofork --nopidfile --systemd-activation --syslog-only
   1497       1 Sl   /usr/bin/gnome-keyring-daemon --daemonize --login
   1501    1464 Ssl  cinnamon-session --session cinnamon
   1634       1 S    /usr/bin/VBoxClient --clipboard
   1636    1634 Sl   /usr/bin/VBoxClient --clipboard
   1644       1 S    /usr/bin/VBoxClient --seamless
   1646    1644 Sl   /usr/bin/VBoxClient --seamless
   1650       1 S    /usr/bin/VBoxClient --draganddrop
   1651    1650 Sl   /usr/bin/VBoxClient --draganddrop
   1656       1 S    /usr/bin/VBoxClient --vmsvga
   1658    1656 Sl   /usr/bin/VBoxClient --vmsvga
   1742    1484 Ssl  /usr/libexec/gvfsd
   1747    1484 Sl   /usr/libexec/gvfsd-fuse /run/user/1000/gvfs -f
   1752    1484 Ssl  /usr/libexec/at-spi-bus-launcher
   1759    1752 S    /usr/bin/dbus-daemon --config-file=/usr/share/defaults/at-spi2/accessibility.conf --nofork --print-address 11 --address=unix:path=/run/user/1000/at-spi/bus_0
   1763    1484 Sl   /usr/libexec/at-spi2-registryd --use-gnome-session
   1774    1501 Sl   csd-clipboard
   1775    1501 Sl   csd-media-keys
   1776    1501 Sl   csd-print-notifications
   1777    1501 Sl   csd-keyboard
   1778    1501 Sl   csd-a11y-settings
   1779    1501 Sl   csd-background
   1780    1501 Sl   csd-wacom
   1781    1501 Sl   csd-color
   1791    1501 Sl   csd-screensaver-proxy
   1794    1501 Sl   csd-housekeeping
   1798    1501 Sl   csd-power
   1804    1501 Sl   csd-xsettings
   1813    1501 Sl   csd-automount
   1829       1 Ssl  /usr/libexec/colord
   1830    1484 Ssl  /usr/libexec/dconf-service
   1850       1 Sl   /usr/libexec/csd-printer
   1853    1484 Ssl  /usr/libexec/gvfs-udisks2-volume-monitor
   1860    1484 Ssl  /usr/libexec/gvfs-goa-volume-monitor
   1867    1484 Sl   /usr/libexec/goa-daemon
   1881    1484 Sl   /usr/libexec/goa-identity-service
   1888    1484 Ssl  /usr/libexec/gvfs-gphoto2-volume-monitor
   1893    1484 Ssl  /usr/libexec/gvfs-mtp-volume-monitor
   1897    1484 Ssl  /usr/libexec/gvfs-afc-volume-monitor
   1906    1501 Sl   cinnamon-launcher
   1911    1906 Sl   cinnamon --replace
   1937    1501 Sl   /usr/lib/x86_64-linux-gnu/xapps/xapp-sn-watcher
   1953    1501 Sl   /usr/libexec/evolution-data-server/evolution-alarm-notify
   1954    1501 Sl   /usr/bin/python3 /usr/bin/blueman-applet
   1955    1501 Sl   nemo-desktop
   1961    1501 Sl   /usr/libexec/geoclue-2.0/demos/agent
   1962    1501 Sl   /usr/lib/policykit-1-gnome/polkit-gnome-authentication-agent-1
   1965    1501 Sl   nm-applet
   1975    1501 Sl   cinnamon-killer-daemon
   2012    1484 Ssl  /usr/libexec/evolution-source-registry
   2052    1484 Ssl  /usr/libexec/evolution-calendar-factory
   2070    1484 Ssl  /usr/libexec/evolution-addressbook-factory
   2098    1484 Ss   /usr/lib/bluetooth/obexd
   2146    1742 Sl   /usr/libexec/gvfsd-trash --spawner :1.8 /org/gtk/gvfs/exec_spaw/0
   2157    1484 Ssl  /usr/libexec/gvfsd-metadata
   2165    1484 Dsl  /usr/libexec/gnome-terminal-server
   2191    2165 Ss   bash
   2220       1 Sl   mintUpdate
   2268    1501 S    /usr/bin/python3 /usr/share/system-config-printer/applet.py
   2271       1 Sl   mintreport-tray
   2353    2191 T    xeyes
   2706       1 Ssl  /usr/libexec/packagekitd
   2760       2 R    [kworker/u6:2-events_unbound]
   2776       2 I    [kworker/u6:1-events_unbound]
   2815       2 I    [kworker/0:0-events]
   2876       2 I    [kworker/u6:0-events_power_efficient]
   2877    2191 R+   ps -eo pid,ppid,stat,cmd
```

</br>

28. Ejecuta `watch -n 1 ps -e` y observa cómo cambian los procesos en tiempo real.

```bash
        PID TTY          TIME CMD
      1 ?        00:00:00 systemd
      2 ?        00:00:00 kthreadd
      3 ?        00:00:00 rcu_gp
      4 ?        00:00:00 rcu_par_gp
      5 ?        00:00:00 slub_flushwq
      6 ?        00:00:00 netns
      8 ?        00:00:00 kworker/0:0H-kblockd
     10 ?        00:00:00 mm_percpu_wq
     11 ?        00:00:00 rcu_tasks_rude_
     12 ?        00:00:00 rcu_tasks_trace
     13 ?        00:00:00 ksoftirqd/0
     14 ?        00:00:00 rcu_sched
     15 ?        00:00:00 migration/0
     16 ?        00:00:00 idle_inject/0
     18 ?        00:00:00 cpuhp/0
     19 ?        00:00:00 cpuhp/1
     20 ?        00:00:00 idle_inject/1
     21 ?        00:00:00 migration/1
     22 ?        00:00:00 ksoftirqd/1
     24 ?        00:00:00 kworker/1:0H-events_highpri
     25 ?        00:00:00 cpuhp/2
     26 ?        00:00:00 idle_inject/2
     27 ?        00:00:00 migration/2
     28 ?        00:00:00 ksoftirqd/2
     30 ?        00:00:00 kworker/2:0H-events_highpri
     31 ?        00:00:00 kdevtmpfs
     32 ?        00:00:00 inet_frag_wq
     33 ?        00:00:00 kauditd
     34 ?        00:00:00 khungtaskd
     35 ?        00:00:00 oom_reaper
     36 ?        00:00:00 writeback
     37 ?        00:00:00 kcompactd0
     38 ?        00:00:00 ksmd
     39 ?        00:00:00 khugepaged
     45 ?        00:00:00 kworker/2:1-events
     62 ?        00:00:00 kworker/1:1-events
     87 ?        00:00:00 kintegrityd
     88 ?        00:00:00 kblockd
     89 ?        00:00:00 blkcg_punt_bio
     90 ?        00:00:00 tpm_dev_wq
     91 ?        00:00:00 ata_sff
     92 ?        00:00:00 md
     93 ?        00:00:00 edac-poller
     94 ?        00:00:00 devfreq_wq
     95 ?        00:00:00 watchdogd
     97 ?        00:00:00 kworker/1:1H-kblockd
     99 ?        00:00:00 kswapd0
    100 ?        00:00:00 ecryptfs-kthrea
```

</br>

29. Explica la diferencia entre ejecutar un proceso con `&` y con `nohup`.

    `&` ejecuta el proceso en segundo plano, pero si cierras la terminal, el proceso se termina. `nohup` permite que el proceso continúe ejecutándose incluso después de cerrar la terminal.

</br>

30. Usa `ulimit -a` para ver los límites de recursos de procesos en tu sistema.

```bash
    real-time non-blocking time  (microseconds, -R) unlimited
    core file size              (blocks, -c) 0
    data seg size               (kbytes, -d) unlimited
    scheduling priority                 (-e) 0
    file size                   (blocks, -f) unlimited
    pending signals                     (-i) 14745
    max locked memory           (kbytes, -l) 488420
    max memory size             (kbytes, -m) unlimited
    open files                          (-n) 1024
    pipe size                (512 bytes, -p) 8
    POSIX message queues         (bytes, -q) 819200
    real-time priority                  (-r) 0
    stack size                  (kbytes, -s) 8192
    cpu time                   (seconds, -t) unlimited
    max user processes                  (-u) 14745
    virtual memory              (kbytes, -v) unlimited
    file locks                          (-x) unlimited
```
