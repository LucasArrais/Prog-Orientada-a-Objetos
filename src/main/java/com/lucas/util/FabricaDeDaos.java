package com.lucas.util;

import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FabricaDeDaos {
    private static final Map<Class<?>, Object> map = new HashMap<>();
    public static <T> T getDAO(Class<T> tipo) {
        Object obj = map.get(tipo);
        if (obj == null) {
            Reflections reflections = new Reflections("com.lucas.dao.impl");
            Set<Class<? extends T>> conjunto = reflections.getSubTypesOf(tipo);
            if (conjunto.size() != 1) {
                throw new RuntimeException(
                        "Deve haver uma e apenas uma classe que implementa a interface " +
                                tipo.getName());
            }
            Class<? extends T> classe = conjunto.iterator().next();
            try {
                obj = classe.getDeclaredConstructor().newInstance();
                map.put(tipo, obj);
            } catch (InstantiationException |
                     IllegalAccessException |
                     InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        return (T) obj;
    }
}