# Code, Learn & Practice  
**Procesos y Servicios (modo alumno, sin root) — Trabajo en `$HOME/dam` con *user units* de systemd**

> **Importante:** Esta guía está adaptada para **usuarios sin privilegios de root**.  
> Todo se hace **en tu carpeta** `~/dam` usando **systemd --user** (un administrador por usuario), sin tocar `/etc` ni `/usr/local`.  
> Pega **salidas reales** y responde a las preguntas cortas.

---

## Preparación

Crea tu área de trabajo y variables útiles:

```bash
mkdir -p ~/dam/{bin,logs,units}
export DAM=~/dam
echo 'export DAM=~/dam' >> ~/.bashrc
```

Comprobar versión de systemd y que el *user manager* está activo:

```bash
systemctl --user --version | head -n1
systemctl --user status --no-pager | head -n5
```
**Pega salida aquí:**

```text
systemd 255 (255.4-1ubuntu8.10)

● eduglezexp-VirtualBox
    State: running
    Units: 201 loaded (incl. loaded aliases)
     Jobs: 0 queued
   Failed: 0 units
```

**Reflexiona la salida:**

```text
El primer comando me dice que estoy usando systemd en la versión 255, que es bastante actual.  

El segundo comando confirma que el gestor de usuario está encendido y funcionando sin errores.  

Eso significa que ya tengo todo listo para poder crear y manejar mis propios servicios como usuario, sin necesidad de ser administrador. 
```

---

## Bloque 1 — Conceptos (breve + fuentes)

1) ¿Qué es **systemd** y en qué se diferencia de SysV init?  

**Respuesta:**  
Systemd es el sistema de inicialización moderno que reemplaza a SysV init. Permite arranque paralelo, dependencias declarativas mediante units, uso de cgroups y logging integrado. SysV init usaba scripts secuenciales en /etc/init.d y era menos flexible.

_Fuentes:_ https://wiki.archlinux.org/title/Systemd

2) **Servicio** vs **proceso** (ejemplos).  

**Respuesta:**  
Un servicio es un conjunto gestionado por systemd (ejemplo: ssh.service). Un proceso es una instancia de un programa con un PID concreto (ejemplo: un proceso sshd de ese servicio).

_Fuentes:_ https://docs.redhat.com/en/documentation/red_hat_enterprise_linux/7/html/system_administrators_guide/chap-managing_services_with_systemd

3) ¿Qué son los **cgroups** y para qué sirven?  

**Respuesta:**  
Son grupos de control de recursos en Linux. Permiten limitar y monitorizar CPU, memoria, I/O de un conjunto de procesos. Systemd los usa para organizar los procesos de cada unidad.

_Fuentes:_ https://wiki.archlinux.org/title/Cgroups

4) ¿Qué es un **unit file** y tipos (`service`, `timer`, `socket`, `target`)?  

**Respuesta:**  
Un unit file describe cómo systemd gestiona algo: puede ser un service (programa), timer (cron interno), socket (activación por socket) o target (agrupación de units, como multi-user.target).

_Fuentes:_ https://www.digitalocean.com/community/tutorials/understanding-systemd-units-and-unit-files

5) ¿Qué hace `journalctl` y cómo ver logs **de usuario**?  

**Respuesta:**  
Es la herramienta para consultar logs del journal de systemd. Con journalctl --user se ven solo los logs de servicios de usuario.

_Fuentes:_ https://wiki.archlinux.org/title/Systemd/Journal

---

## Bloque 2 — Práctica guiada (todo en tu `$DAM`)

> Si un comando pide permisos que no tienes, usa la **versión `--user`** o consulta el **ayuda** con `--help` para alternativas.

### 2.1 — PIDs básicos

**11.** PID de tu shell y su PPID.

```bash
echo "PID=$$  PPID=$PPID"
```
**Salida:**

```text
PID=3437  PPID=3393
```

**Pregunta:** ¿Qué proceso es el padre (PPID) de tu shell ahora?  

**Respuesta:**
El proceso padre (PPID=3393) de mi shell es probablemente el proceso del terminal o emulador de terminal que estoy usando para ejecutar la shell.

---

**12.** PID del `systemd --user` (manager de usuario) y explicación.

```bash
pidof systemd --user || pgrep -u "$USER" -x systemd
```

**Salida:**

```text
2584
```

**Pregunta:** ¿Qué hace el *user manager* de systemd para tu sesión?  

**Respuesta:**
El user manager de systemd (PID=2584) gestiona los servicios y unidades específicas para mi usuario, permitiendo iniciar, detener y supervisar procesos sin necesidad de privilegios de root.

---

### 2.2 — Servicios **de usuario** con systemd

Vamos a crear un servicio sencillo y un timer **en tu carpeta** `~/.config/systemd/user` o en `$DAM/units` (usaremos la primera para que `systemctl --user` lo encuentre).

**13.** Prepara directorios y script de práctica.

```bash
mkdir -p ~/.config/systemd/user "$DAM"/{bin,logs}
cat << 'EOF' > "$DAM/bin/fecha_log.sh"
#!/usr/bin/env bash
mkdir -p "$HOME/dam/logs"
echo "$(date --iso-8601=seconds) :: hello from user timer" >> "$HOME/dam/logs/fecha.log"
EOF
chmod +x "$DAM/bin/fecha_log.sh"
```

**14.** Crea el servicio **de usuario** `fecha-log.service` (**Type=simple**, ejecuta tu script).

```bash
cat << 'EOF' > ~/.config/systemd/user/fecha-log.service
[Unit]
Description=Escribe fecha en $HOME/dam/logs/fecha.log

[Service]
Type=simple
ExecStart=%h/dam/bin/fecha_log.sh
EOF

systemctl --user daemon-reload
systemctl --user start fecha-log.service
systemctl --user status fecha-log.service --no-pager -l | sed -n '1,10p'
```
**Salida (pega un extracto):**

```text
○ fecha-log.service - Escribe fecha en $HOME/dam/logs/fecha.log
     Loaded: loaded (/home/eduglezexp/.config/systemd/user/fecha-log.service; static)
     Active: inactive (dead)

sep 24 11:15:17 eduglezexp-VirtualBox systemd[2584]: Started fecha-log.service - Escribe fecha en $HOME/dam/logs/fecha.log.
```
**Pregunta:** ¿Se creó/actualizó `~/dam/logs/fecha.log`? Muestra las últimas líneas:

```bash
tail -n 5 "$DAM/logs/fecha.log"
```

**Salida:**

```text
2025-09-24T11:15:17+01:00 :: hello from user timer
```

**Reflexiona la salida:**

```text
El archivo fecha.log se creó correctamente y contiene la línea con la fecha y el mensaje, lo que indica que el servicio se ejecutó correctamente.
```

---

**15.** Diferencia **enable** vs **start** (modo usuario). Habilita el **timer**.

```bash
cat << 'EOF' > ~/.config/systemd/user/fecha-log.timer
[Unit]
Description=Timer (usuario): ejecuta fecha-log.service cada minuto

[Timer]
OnCalendar=*:0/1
Unit=fecha-log.service
Persistent=true

[Install]
WantedBy=timers.target
EOF

systemctl --user daemon-reload
systemctl --user enable --now fecha-log.timer
systemctl --user list-timers --all | grep fecha-log || true
```

**Salida (recorta):**

```text
Created symlink /home/eduglezexp/.config/systemd/user/timers.target.wants/fecha-log.timer → /home/eduglezexp/.config/systemd/user/fecha-log.timer.
Wed 2025-09-24 11:29:00 WEST  49s -                                    - fecha-log.timer                fecha-log.service
```

**Pregunta:** ¿Qué diferencia hay entre `enable` y `start` cuando usas `systemctl --user`?  

**Respuesta:**
`start` inicia el servicio o timer inmediatamente, mientras que `enable` configura el servicio o timer para que se inicie automáticamente en el futuro, como al iniciar la sesión del usuario.
---

**16.** Logs recientes **del servicio de usuario** con `journalctl --user`.

```bash
journalctl --user -u fecha-log.service -n 10 --no-pager
```

**Salida:**

```text
sep 24 11:15:17 eduglezexp-VirtualBox systemd[2584]: Started fecha-log.service - Escribe fecha en $HOME/dam/logs/fecha.log.
sep 24 11:29:38 eduglezexp-VirtualBox systemd[2584]: Started fecha-log.service - Escribe fecha en $HOME/dam/logs/fecha.log.
sep 24 11:30:38 eduglezexp-VirtualBox systemd[2584]: Started fecha-log.service - Escribe fecha en $HOME/dam/logs/fecha.log.
sep 24 11:31:38 eduglezexp-VirtualBox systemd[2584]: Started fecha-log.service - Escribe fecha en $HOME/dam/logs/fecha.log.
sep 24 11:32:38 eduglezexp-VirtualBox systemd[2584]: Started fecha-log.service - Escribe fecha en $HOME/dam/logs/fecha.log.
```
**Pregunta:** ¿Ves ejecuciones activadas por el timer? ¿Cuándo fue la última?  

**Respuesta:**
No, la última ejecución fue a las 11:32:38

---

### 2.3 — Observación de procesos sin root

**17.** Puertos en escucha (lo que puedas ver como usuario).

```bash
lsof -i -P -n | grep LISTEN || ss -lntp
```
**Salida:**

```text
State    Recv-Q   Send-Q     Local Address:Port     Peer Address:Port  Process  
LISTEN   0        4096           127.0.0.1:631           0.0.0.0:*              
LISTEN   0        4096       127.0.0.53%lo:53            0.0.0.0:*              
LISTEN   0        4096          127.0.0.54:53            0.0.0.0:*              
LISTEN   0        4096               [::1]:631              [::]:* 
```
**Pregunta:** ¿Qué procesos *tuyos* están escuchando? (si no hay, explica por qué)  

**Respuesta:**
No hay procesos míos escuchando porque no he iniciado ningún servicio o aplicación que abra puertos de red. Los puertos que se ven pertenecen a servicios del sistema.

---

**18.** Ejecuta un proceso bajo **cgroup de usuario** con límite de memoria.

```bash
systemd-run --user --scope -p MemoryMax=50M sleep 200
ps -eo pid,ppid,cmd,stat | grep "[s]leep 200"
```

**Salida:**

```text
Running as unit: run-r9e4f698c859b4e03bc4fb5c3830333d2.scope; invocation ID: a72493f83c0c443194ce8fd5584f0d0d
```

**Pregunta:** ¿Qué ventaja tiene lanzar con `systemd-run --user` respecto a ejecutarlo “a pelo”?  

**Respuesta:**
Usar `systemd-run --user` permite gestionar el proceso dentro de un cgroup con límites específicos (como memoria), facilitando el control y monitoreo del recurso que consume, algo que no se logra al ejecutarlo directamente.

---

**19.** Observa CPU en tiempo real con `top` (si tienes `htop`, también vale).

```bash
top -b -n 1 | head -n 15
```
**Salida (resumen):**

```text
PID USUARIO   PR  NI    VIRT    RES    SHR S  %CPU  %MEM     HORA+ ORDEN
3900 eduglez+  20   0   17224   5740   3564 R   6,1   0,1   0:00.02 top
   1 root      20   0   22448  13204   9236 S   0,0   0,2   0:03.70 systemd
   2 root      20   0       0      0      0 S   0,0   0,0   0:00.08 kthreadd
   3 root      20   0       0      0      0 S   0,0   0,0   0:00.00 pool_wo+
   4 root       0 -20       0      0      0 I   0,0   0,0   0:00.00 kworker+
   5 root       0 -20       0      0      0 I   0,0   0,0   0:00.00 kworker+
   6 root       0 -20       0      0      0 I   0,0   0,0   0:00.00 kworker+
   7 root       0 -20       0      0      0 I   0,0   0,0   0:00.00 kworker+

```
**Pregunta:** ¿Cuál es tu proceso con mayor `%CPU` en ese momento?  

**Respuesta:**
Mi proceso con mayor %CPU en ese momento es el proceso 'top' con un 6.1% de uso de CPU.
---

**20.** Traza syscalls de **tu propio proceso** (p. ej., el `sleep` anterior).
> Nota: Sin root, no podrás adjuntarte a procesos de otros usuarios ni a algunos del sistema.

```bash
pid=$(pgrep -u "$USER" -x sleep | head -n1)
strace -p "$pid" -e trace=nanosleep -tt -c -f 2>&1 | sed -n '1,10p'
```

**Salida (fragmento):**

```text
strace: -t/--absolute-timestamps has no effect with -c/--summary-only
strace: attach: ptrace(PTRACE_SEIZE, 3920): Operación no permitida
```
**Pregunta:** Explica brevemente la syscall que observaste.  

**Respuesta:**
El strace no pudo adjuntarse al proceso sleep debido a restricciones de permisos, pero la syscall nanosleep es utilizada para pausar la ejecución de un proceso durante un tiempo especificado sin consumir CPU activamente.
---

### 2.4 — Estados y jerarquía (sin root)

**21.** Árbol de procesos con PIDs.

```bash
pstree -p | head -n 50
```

**Salida (recorta):**

```text
systemd(1)-+-ModemManager(814)-+-{ModemManager}(883)
           |                   |-{ModemManager}(891)
           |                   `-{ModemManager}(893)
           |-NetworkManager(734)-+-{NetworkManager}(803)
           |                     |-{NetworkManager}(812)
           |                     `-{NetworkManager}(815)
           |-VBoxClient(2755)---VBoxClient(2756)-+-{VBoxClient}(2757)
           |                                     |-{VBoxClient}(2758)
           |                                     `-{VBoxClient}(2759)
           |-VBoxClient(2770)---VBoxClient(2771)-+-{VBoxClient}(2773)
           |                                     |-{VBoxClient}(2774)
           |                                     `-{VBoxClient}(2775)
           |-VBoxClient(2778)---VBoxClient(2779)-+-{VBoxClient}(2786)
           |                                     |-{VBoxClient}(2787)
           |                                     |-{VBoxClient}(2788)
           |                                     `-{VBoxClient}(2792)
           |-VBoxClient(2783)---VBoxClient(2784)-+-{VBoxClient}(2789)
           |                                     |-{VBoxClient}(2790)
           |                                     `-{VBoxClient}(2806)
           |-VBoxDRMClient(1929)-+-{VBoxDRMClient}(1952)
           |                     |-{VBoxDRMClient}(1953)
           |                     |-{VBoxDRMClient}(1954)
           |                     `-{VBoxDRMClient}(2796)
           |-VBoxService(1932)-+-{VBoxService}(1935)
           |                   |-{VBoxService}(1936)
           |                   |-{VBoxService}(1937)
           |                   |-{VBoxService}(1940)
           |                   |-{VBoxService}(1942)
           |                   |-{VBoxService}(1943)
           |                   |-{VBoxService}(1944)
           |                   `-{VBoxService}(1945)
           |-accounts-daemon(645)-+-{accounts-daemon}(674)
           |                      |-{accounts-daemon}(675)
           |                      `-{accounts-daemon}(730)
           |-agetty(2438)
           |-at-spi2-registr(2975)-+-{at-spi2-registr}(2986)
           |                       |-{at-spi2-registr}(2987)
           |                       `-{at-spi2-registr}(2989)
           |-avahi-daemon(647)---avahi-daemon(696)
           |-colord(2992)-+-{colord}(3011)
           |              |-{colord}(3012)
           |              `-{colord}(3016)
           |-cron(649)
           |-csd-printer(2982)-+-{csd-printer}(2996)
           |                   |-{csd-printer}(2997)
           |                   `-{csd-printer}(2998)
           |-cups-browsed(1091)-+-{cups-browsed}(1114)
           |                    |-{cups-browsed}(1115)
           |                    `-{cups-browsed}(1116)
           |-cupsd(1078)-+-dbus(1088)
```
**Pregunta:** ¿Bajo qué proceso aparece tu `systemd --user`?  

**Respuesta:**
No aparece directamente en el árbol mostrado, pero generalmente el `systemd --user` se ejecuta bajo el proceso del gestor de sesiones o del terminal que inició la sesión del usuario.

---

**22.** Estados y relación PID/PPID.

```bash
ps -eo pid,ppid,stat,cmd | head -n 20
```
**Salida:**

```text
PID    PPID STAT CMD
  1       0 Ss   /sbin/init splash
  2       0 S    [kthreadd]
  3       2 S    [pool_workqueue_release]
  4       2 I<   [kworker/R-rcu_gp]
  5       2 I<   [kworker/R-sync_wq]
  6       2 I<   [kworker/R-kvfree_rcu_reclaim]
  7       2 I<   [kworker/R-slub_flushwq]
  8       2 I<   [kworker/R-netns]
  11       2 I<   [kworker/0:0H-events_highpri]
  13       2 I<   [kworker/R-mm_percpu_wq]
  14       2 I    [rcu_tasks_kthread]
  15       2 I    [rcu_tasks_rude_kthread]
  16       2 I    [rcu_tasks_trace_kthread]
  17       2 S    [ksoftirqd/0]
  18       2 I    [rcu_preempt]
  19       2 S    [rcu_exp_par_gp_kthread_worker/0]
  20       2 S    [rcu_exp_gp_kthread_worker]
  21       2 S    [migration/0]
  22       2 S    [idle_inject/0]
```
**Pregunta:** Explica 3 flags de `STAT` que veas (ej.: `R`, `S`, `T`, `Z`, `+`).  

**Respuesta:**
- S: Sleeping - El proceso está inactivo, esperando un evento.
- Ss: Interruptible Sleep - El proceso está durmiendo pero puede ser interrumpido por señales.
- I: Idle - El proceso está inactivo, generalmente un hilo del kernel que no está haciendo nada.

---

**23.** Suspende y reanuda **uno de tus procesos** (no crítico).

```bash
# Crea un proceso propio en segundo plano
sleep 120 &
pid=$!
# Suspende
kill -STOP "$pid"
# Estado
ps -o pid,stat,cmd -p "$pid"
# Reanuda
kill -CONT "$pid"
# Estado
ps -o pid,stat,cmd -p "$pid"
```
**Pega los dos estados (antes/después):**

```text
# Estado
ps -o pid,stat,cmd -p "$pid"
[2] 3998
    PID STAT CMD
   3998 T    bash

[2]+  Detenido                120 > sleep

# Estado
ps -o pid,stat,cmd -p "$pid"
    PID STAT CMD
   3998 S    bash
```
**Pregunta:** ¿Qué flag indicó la suspensión?  

**Respuesta:**
El flag que indicó la suspensión fue "T", que significa que el proceso está detenido (suspended).
---

**24. (Opcional)** Genera un **zombie** controlado (no requiere root).

```bash
cat << 'EOF' > "$DAM/bin/zombie.c"
#include <stdlib.h>
#include <unistd.h>
int main() {
  if (fork() == 0) { exit(0); } // hijo termina
  sleep(60); // padre no hace wait(), hijo queda zombie hasta que padre termine
  return 0;
}
EOF
gcc "$DAM/bin/zombie.c" -o "$DAM/bin/zombie" && "$DAM/bin/zombie" &
ps -el | grep ' Z '
```
**Salida (recorta):**

```text
1 Z  1000    4069    4068  0  80   0 -     0 -      pts/0    00:00:00 zombie
```
**Pregunta:** ¿Por qué el estado `Z` y qué lo limpia finalmente?  

**Respuesta:**
El estado "Z" indica que el proceso es un zombie, lo que significa que ha terminado su ejecución pero su entrada en la tabla de procesos aún no ha sido eliminada porque el proceso padre no ha llamado a wait() para recoger su estado. Finalmente, el proceso padre limpiará el zombie cuando termine o llame a wait().

---

### 2.5 — Limpieza (solo tu usuario)

Detén y deshabilita tu **timer/servicio de usuario** y borra artefactos si quieres.

```bash
systemctl --user disable --now fecha-log.timer
systemctl --user stop fecha-log.service
rm -f ~/.config/systemd/user/fecha-log.{service,timer}
systemctl --user daemon-reload
rm -rf "$DAM/bin" "$DAM/logs" "$DAM/units"
```

---

## ¿Qué estás prácticando?
- [ ] Pegaste **salidas reales**.  
- [ ] Explicaste **qué significan**.  
- [ ] Usaste **systemd --user** y **journalctl --user**.  
- [ ] Probaste `systemd-run --user` con límites de memoria.  
- [ ] Practicaste señales (`STOP`/`CONT`), `pstree`, `ps` y `strace` **sobre tus procesos**.
