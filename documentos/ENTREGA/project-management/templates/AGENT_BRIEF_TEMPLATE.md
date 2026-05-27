# Plantilla Breve Para Delegar a un Agente

Usar esta plantilla cuando el agente principal delegue trabajo a un agente auxiliar. El objetivo es mantener el contexto minimo y evitar prompts largos.

```text
Rol:
[Agente A / Agente B / Agente C / Revisor]

Tarea concreta:
[Una frase accionable]

Contexto minimo:
[Resumen breve. No copiar documentos completos.]

Leer solo:
- documentos/ENTREGA/project-management/AGENTS.md
- documentos/ENTREGA/project-management/TASKS.md
- documentos/ENTREGA/project-management/agents/AGENT_*.md
- [otros archivos estrictamente necesarios]

Puede modificar:
- [rutas concretas, o "ningun archivo; solo investigar"]

No tocar:
- [archivos compartidos o ajenos al rol]

Salida esperada:
Resultado:
- [hallazgos o implementacion]

Archivos relevantes:
- ruta:linea

Cambios realizados:
- [si aplica]

Riesgos o dudas:
- [si aplica]
```

Reglas:

- No copiar contenido completo de `documentos/ENTREGA/project-management/`.
- No rehacer analisis ya registrado en `SCRATCHPAD.md`.
- No modificar archivos compartidos sin autorizacion humana.
- Si la tarea requiere una decision nueva, parar y pedir confirmacion.
