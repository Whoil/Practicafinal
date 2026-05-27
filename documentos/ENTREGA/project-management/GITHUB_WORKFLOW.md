# Flujo de Trabajo con GitHub

## 1. Conceptos basicos

`main` es la rama estable del proyecto. Debe contener solo codigo revisado y aceptado.

Una Pull Request, o PR, es una solicitud para incorporar cambios desde una rama de trabajo hacia otra rama. En este proyecto normalmente sera:

```text
feature/a-estructuras -> main
feature/b-logica -> main
feature/c-javafx-json-docs -> main
```

Una PR permite:

- Ver archivos modificados.
- Comentar cambios.
- Revisar posibles errores.
- Pedir correcciones.
- Ejecutar checklist.
- Hacer merge solo cuando el grupo este de acuerdo.

## 2. Ramas oficiales

Rama estable:

```text
main
```

Ramas de trabajo:

```text
feature/a-estructuras
feature/b-logica
feature/c-javafx-json-docs
```

Cada integrante y su agente trabajan en su rama.

Asignacion:

```text
Alvaro + Codex-A Estructuras -> feature/a-estructuras
Guille + Agente B Logica -> feature/b-logica
Hector + Agente C JavaFX/JSON/Docs -> feature/c-javafx-json-docs
```

## 3. Regla principal

Nadie, humano o agente, debe hacer push directo a `main`.

Todo cambio importante debe entrar mediante Pull Request revisada.

## 4. Flujo normal de una tarea

1. El humano se identifica e indica su parte: A, B, C o revision.
2. El agente confirma rol, area permitida y tarea.
3. El agente comprueba si hay cambios remotos en GitHub.
4. El agente lee los documentos de coordinacion actualizados.
5. El agente confirma que esta en la rama correspondiente a su parte.
6. Si no esta en su rama, pide autorizacion para cambiar a ella.
7. El agente actualiza su rama con `origin/main` si no hay conflictos.
8. Si hay conflictos al actualizar, el agente se detiene y pide ayuda humana.
9. El humano asigna una tarea en `TASKS.md`.
10. El agente trabaja solo en su rama.
11. El agente modifica solo archivos permitidos.
12. Si necesita tocar un archivo compartido, pide permiso.
13. El agente actualiza `SCRATCHPAD.md`, `TASKS.md` e `IA_DIARY.md`.
14. El agente revisa si debe actualizar `DECISIONS.md`, `PRD.md`, `ARCHITECTURE.md` o su archivo de agente.
15. El agente ejecuta pruebas relevantes si es posible.
16. El agente muestra resumen de sesion.
17. El humano autoriza o rechaza commit.
18. El humano autoriza o rechaza push.
19. Se crea PR hacia `main`.
20. Un humano revisa la PR.
21. Si la PR contiene codigo, el Agente Revisor independiente la revisa obligatoriamente.
22. Si se aprueba, se hace merge a `main`.

## 4.1 Documentacion como fuente de verdad

Los archivos de `documentos/ENTREGA/project-management/` deben mantenerse actualizados en GitHub.

Cada agente debe:

- Leerlos al inicio de sesion.
- Comprobar si otros agentes los han modificado.
- Actualizarlos cuando haga cambios relevantes.
- Pedir autorizacion explicita para commit/push de esos cambios.
- Avisar si detecta una contradiccion entre documentos y codigo.

No se debe trabajar con documentos desactualizados.

Regla adicional:

```text
Si un agente modifica documentacion o realiza un cambio significativo,
debe terminar preguntando si se autoriza commit y push.
```

Tambien debe registrar un cierre de sesion en `SCRATCHPAD.md` antes de terminar.

### Que cuenta como cambio significativo

Cuenta como cambio significativo cualquier cambio que afecte a:

- La logica del juego.
- La estructura o arquitectura del proyecto.
- La documentacion.
- El trabajo de otros companeros.
- Otras ramas, especialmente `main`.

Estos cambios siempre requieren resumen, documentacion actualizada si procede, y autorizacion explicita para commit/push.

## 4.2 Actualizacion automatica de ramas

Cada agente debe mantener su rama al dia con `origin/main`.

Al inicio de sesion:

```text
1. Hacer fetch de GitHub.
2. Cambiar a su rama de trabajo.
3. Integrar la ultima version de origin/main.
4. Si no hay conflictos, continuar.
5. Si hay conflictos, parar y pedir ayuda humana.
```

Si tras actualizar la rama local hace falta subir la rama remota, el agente debe pedir autorizacion explicita antes de hacer push.

Las ramas son:

```text
Parte A -> feature/a-estructuras
Parte B -> feature/b-logica
Parte C -> feature/c-javafx-json-docs
```

## 4.3 Tests JUnit obligatorios

Todo agente que cree o modifique codigo no visual debe crear o actualizar tests unitarios con JUnit.

Aplica especialmente a:

- Estructuras propias.
- Matriz y celdas.
- Grafo de cuevas.
- BFS y caminos minimos.
- Personajes.
- Objetos e inventario.
- Turnos.
- Combate.
- Victoria y derrota.
- Carga y guardado JSON.

No se considera terminada una tarea de codigo no visual si no incluye tests JUnit razonables o una explicacion aprobada de por que no se pueden crear todavia.

Cada resumen de sesion debe indicar:

```text
Tests creados o actualizados:
Tests ejecutados:
Resultado:
Si no se ejecutaron, motivo:
```

## 5. Archivos compartidos

Requieren permiso previo:

```text
documentos/ENTREGA/project-management/PRD.md
documentos/ENTREGA/project-management/ARCHITECTURE.md
documentos/ENTREGA/project-management/DECISIONS.md
documentos/ENTREGA/project-management/TASKS.md
diagrama_inicial_juego.puml
src/modelo/juego/Partida.java
src/modelo/juego/Mazmorra.java
src/modelo/mapa/Cueva.java
src/modelo/mapa/Celda.java
src/modelo/personajes/Jugador.java
src/modelo/objetos/Objeto.java
```

Motivo:

- Son archivos que afectan a varios integrantes.
- Pueden causar conflictos.
- Pueden cambiar reglas o arquitectura.

## 6. Que puede hacer un agente en GitHub

Permitido con autorizacion humana cuando corresponda:

- Ver estado del repositorio.
- Ver diferencias.
- Consultar cambios remotos.
- Comparar su rama con `origin/main`.
- Crear rama asignada.
- Preparar cambios.
- Proponer commit con mensaje en espanol, claro y descriptivo.
- Hacer commit autorizado.
- Hacer push autorizado.
- Preparar texto de PR.

No permitido sin autorizacion explicita:

- Push directo a `main`.
- Merge.
- Rebase.
- Reset.
- Force push.
- Borrar ramas.
- Cambiar protecciones del repositorio.
- Resolver conflictos complejos sin consultar.

## 7. Pull Request

Cada PR debe incluir:

```text
Resumen:
- Que cambia.
- Por que cambia.
- Archivos tocados.
- Pruebas ejecutadas.
- Riesgos.
- Tareas relacionadas.
- Checklist de restricciones.
```

Checklist minimo:

- [ ] No usa colecciones prohibidas.
- [ ] No usa arrays para matriz de cuevas.
- [ ] No toca archivos ajenos sin permiso.
- [ ] Actualiza documentos de coordinacion.
- [ ] El codigo nuevo esta comentado en profundidad.
- [ ] Compila o indica por que no se pudo comprobar.
- [ ] Incluye pruebas si aplica.

## 7.1 Mensajes de commit

A partir de ahora, los mensajes de commit deben estar en espanol y describir claramente el cambio.

Ejemplos validos:

```text
docs: aclarar reglas de sincronizacion de agentes
feat: crear matriz propia de cuevas
test: anadir pruebas de movimiento en cueva
fix: corregir calculo de celdas alcanzables
```

Evitar mensajes vagos como:

```text
cambios
arreglos
update
cosas
```

## 7.2 Donde se hacen los commits

Los commits deben hacerse en la rama de trabajo de la parte correspondiente:

```text
Parte A: feature/a-estructuras
Parte B: feature/b-logica
Parte C: feature/c-javafx-json-docs
```

`main` solo debe recibir cambios mediante Pull Request revisada, salvo cambios de coordinacion excepcionalmente autorizados por el grupo.

## 8. Conflictos

Si aparece un conflicto entre ramas:

1. No resolver automaticamente.
2. Avisar al grupo.
3. Identificar archivos afectados.
4. Decidir quien es propietario del archivo.
5. Resolver con supervision humana.
6. Actualizar `SCRATCHPAD.md` con lo ocurrido.

## 9. Integracion recomendada

Integrar con PR pequenas:

- Matriz propia.
- Grafo de cuevas.
- Personajes base.
- Objetos base.
- Turnos y combate.
- JSON inicial.
- JavaFX minima.

Evitar PR grandes que mezclen muchas areas.
