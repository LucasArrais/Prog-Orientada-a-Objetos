package com.lucas.dao;

import java.util.List;
import java.util.Map;

public interface DAOGenerico<V> {
    Map<Integer, V> getMap();
    void setMap(Map<Integer, V> map);
    Integer getContador();
    void setContador(Integer contador);
    V incluir(V obj);
    V alterar(V obj);
    V remover(Integer id);
    V recuperarPorId(Integer id);
    List<V> recuperarTodos();
}
