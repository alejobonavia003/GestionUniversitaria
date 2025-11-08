package com.is1.proyecto.utils;

import com.google.gson.Gson;

/**
 * Utilidad para manejo de JSON en la aplicación.
 * esta clases es para transformar objetos java a json y viceversa
 * la usamos para responder peticiones api rest con json
 */
public class JsonUtil {
    private static final Gson gson = new Gson();

    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    public static <T> T fromJson(String json, Class<T> type) {
        return gson.fromJson(json, type);
    }
}