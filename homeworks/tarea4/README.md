# <img src=../../../../../images/computer.png width="40"> Code, Learn & Practice(Programación de Servicios y Procesos: "Ejecución de procesos del sistema con Spring Boot")

## Objetivo

Construir una aplicación **Spring Boot (CLI)**  que **lanza procesos del sistema operativo** (Linux/Windows), captura **stdout/stderr** en tiempo real, aplica **timeout**, y **persiste** el historial de ejecuciones en **ficheros JSONL**, todo usando una **arquitectura por interfaces** con **@Component, @Service y @Repository**.

## ¿Qué puedes hacer?

- Ejecutar tareas y portables por SO:
  - `PING` — `ping -c/-n`
  - `LIST_DIR` — `ls -la` / `cmd /c dir`
  - `HASH_SHA256` — `sha256sum` / `certutil -hashfile ... SHA256`
- Ver la salida en **tiempo real** (con prefijos `[OUT]`/`[ERR]`).
- Definir **timeout** (ms); si expira, se marca `TIMEOUT` y se mata el proceso.
- Guardar **stdout/stderr** en ficheros (`app.logs-dir`).
- Consultar el **resumen** (PID, exit code, estado) al finalizar cada ejecución.
- REPL que **permanece** corriendo hasta **Ctrl+C**.

## Arquitectura (propuesta)

- **domain/**
  - `Job`, `JobStatus`, `JobType`.
- **interfaces**:
  - `ICommandFactory` (**@Component**) — traduce `JobType`+params a **List<String>** según SO.
  - `IProcessExecutor` (**@Service**) — ejecuta el proceso, captura streams y devuelve `Result`.
  - `ILogStorage` (**@Component**) — resuelve rutas de logs, utilidades (tail)/ etc.
  - `IJobRepository` (**@Repository**) — persiste/lee `Job` en almacenamiento **fichero (JSON)**.
- **services/**
  - `JobService` (**@Service**) — orquesta: prepara logs, invoca `ProcessExecutor`, actualiza y persiste `Job`.
- **cli/**
  - `ConsoleRunner` (**@Component**, `CommandLineRunner`) — REPL (`run ...`) y gestión de Ctrl+C.

Diagrama resumido:

```console
ConsoleRunner --> JobService --> (CommandFactory + ProcessExecutor + LogStorage + JobRepository)
```

## Ejecutar

```bash
mvn clean spring-boot:run
```

Prompt propuesto:

```console
=== Lanzador de Procesos (CLI) Linux/Windows ===
Comandos:
  run PING host=8.8.8.8 count=4 timeoutMs=15000
  run LIST_DIR path=.
  run HASH_SHA256 file=README.md
  help | os | exit
```

### Ejemplos

```console
run PING host=8.8.8.8 count=4
run LIST_DIR path=.
run HASH_SHA256 file=README.md timeoutMs=5000
```

## Configuración

- `app.logs-dir` (por defecto `logs/`)
- `app.data-dir` (por defecto `data/`)
- `app.default-timeout-ms` (por defecto `10000`)

## Detalles de implementación

- `ProcessExecutor` usa `ProcessBuilder`, consume **stdout**/**stderr**.
- `JobRepository` escribe `Job` como **JSON** (`data/jobs.json`).
- `LogStorage` organiza logs por día (`logs/yyyy-MM-dd/`) y ofrece `tail()` para mostrar las últimas N líneas en el resumen.

## Cerrar la app

Pulsa **Ctrl+C** o escribe `exit`.

## Arquitectura de paquetes propuesta

```text
org.formacion.procesos/
├─ domain/                 # Modelo de dominio (POJOs, enums, DTOs)
│  ├─ JobId.java
│  ├─ JobType.java         # PING | LIST_DIR | HASH_SHA256
│  ├─ JobStatus.java       # PENDING | RUNNING | SUCCESS | ERROR | TIMEOUT | KILLED
│  ├─ Job.java
│  ├─ Result.java
│  └─ RunRequest.java
│
├─ interfaces/             # Contratos (arquitectura por interfaces)
│  ├─ ICommandFactory.java   @Component
│  ├─ IProcessExecutor.java  @Service
│  ├─ ILogStorage.java       @Component
│  ├─ IJobRepository.java    @Repository
│  └─ IConsole.java          @Component (opcional, para testear REPL)
│
├─ cli/                    # Entrada y REPL
│  ├─ ConsoleRunner.java     @Component implements CommandLineRunner
│  └─ CommandParser.java
│
├─ services/               # Orquestación y lógica de aplicación
│  ├─ JobService.java        @Service (usa interfaces)
│  └─ ProcessExecutor.java   @Service (impl de IProcessExecutor)
│
├─ component/              # Componentes auxiliares
│  ├─ CommandFactory.java    @Component (impl de ICommandFactory; dependiente de SO)
│  └─ LogStorage.java        @Component (impl de ILogStorage; rutas y tail)
│
├─ repository/             # Persistencia (ficheros JSONL)
│  └─ JobRepository.java     @Repository (impl de IJobRepository; data/jobs.jsonl)
│
├─ config/                 # Configuración tipada
│  ├─ AppProperties.java     @ConfigurationProperties(prefix="app")
│  └─ AppConfig.java         @Configuration @EnableConfigurationProperties
│
└─ util/                   # Utilidades (opcional)
   ├─ OsUtils.java
   ├─ JsonlUtils.java
   └─ IoUtils.java
```

---

## Rol de cada paquete

- **domain/**: núcleo del negocio sin dependencias de Spring.  
- **interfaces/**: contratos que desacoplan la orquestación de las implementaciones.  
- **cli/**: REPL y parsing de comandos; mantiene viva la app hasta `Ctrl+C`.  
- **services/**: orquesta una ejecución (`JobService`) y ejecuta procesos del SO (`ProcessExecutor`).  
- **component/**: utilidades de comandos por SO y gestión de logs (rutas `logs/yyyy-MM-dd/`, `tail(N)`).  
- **repository/**: persistencia **JSONL** (`data/jobs.jsonl`), operaciones `append/find`.  
- **config/**: propiedades `app.logs-dir`, `app.data-dir`, `app.default-timeout-ms`.  
- **util/**: helpers transversales (opcional).  

---

## Diagrama lógico

```text
ConsoleRunner (CLI)
   └─ JobService (@Service)
        ├─ ICommandFactory (@Component) -> CommandFactory
        ├─ IProcessExecutor (@Service)  -> ProcessExecutor
        ├─ ILogStorage (@Component)     -> LogStorage
        └─ IJobRepository (@Repository) -> JobRepository (JSONL)
```

---

## Propiedades recomendadas (application.properties)

```properties
app.logs-dir=logs/
app.data-dir=data/
```

## Licencia 📄

Este proyecto está bajo la Licencia (Apache 2.0) - mira el archivo [LICENSE.md]([../../../LICENSE.md](https://github.com/jpexposito/code-learn-practice/blob/main/LICENSE)) para detalles.