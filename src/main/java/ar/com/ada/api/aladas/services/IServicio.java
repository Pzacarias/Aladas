package ar.com.ada.api.aladas.services;

import java.util.List;

//
public interface IServicio<T, ID> {
    
    void crear(T t); //insert
    void grabar(T t); //update
    void delete(T t);
    List<T> traerTodos();
    T buscarPorId(ID id);
    boolean esValido(T t);
    boolean existe(T t);

}
