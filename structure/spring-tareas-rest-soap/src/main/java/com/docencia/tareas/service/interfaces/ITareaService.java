package com.docencia.tareas.service.interfaces;

import java.util.List;

import com.docencia.tareas.model.Tarea;

public interface ITareaService {
    List<Tarea> listarTodas();
    Tarea buscarPorId(Long id);
    Tarea crearTarea(String titulo, String descripcion);
    Tarea actualizarTarea(Long id, String titulo, String descripcion, Boolean completada);
    boolean eliminarTarea(Long id);
}
