package com.lucas.dao.impl;

import com.lucas.dao.DAOGenerico;
import com.lucas.util.Id;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DAOGenericoimpl<V> implements DAOGenerico<V> {

    protected Map<Integer, V> map = new LinkedHashMap<>(16);
    private int contador;

    public Map<Integer, V> getMap() {
        return map;
    }

    public void setMap(Map<Integer, V> map) {
        this.map = map;
    }

    @Override
    public Integer getContador() {
        return contador;
    }

    @Override
    public void setContador(Integer contador) {
        this.contador = contador;
    }

    public V incluir(V obj) {
        Field campo = recuperarCampoIdentificador(obj);
        atribuirContadorACampo(obj, campo);
        return map.put(contador, obj);
    }

    private void atribuirContadorACampo(V obj, Field campo) {
        try {
            campo.setAccessible(true);
            campo.set(obj, ++contador);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private Field recuperarCampoIdentificador(V obj) {
        for (Field campo : obj.getClass().getDeclaredFields()) {
            if (campo.isAnnotationPresent(Id.class)) {
                return campo;
            }
        }
        throw new RuntimeException("Deve haver um campo anotado com @Id");
    }
    public V alterar(V obj) {
        Field campo = recuperarCampoIdentificador(obj);
        atribuirContadorACampo(obj, campo);
        return map.put(contador, obj);
    }

    public V remover(Integer id) {
        return map.remove(id);
    }

    public V recuperarPorId(Integer id) {
        return map.get(id);
    }

    public List<V> recuperarTodos() {
        return new ArrayList<>(map.values()); // .stream().toList();
    }
}
