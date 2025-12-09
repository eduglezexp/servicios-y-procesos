package com.docencia.tareas.repository.interfaces;

import java.util.List;

import com.docencia.tareas.model.Tarea;

public interface ITareaRepository {
    public Tarea add(Tarea tarea);
    public boolean delete(Tarea tarea);
    public Tarea findBy(Tarea tarea);
    public Tarea update(Tarea tarea);
    public List<Tarea> all();
}
