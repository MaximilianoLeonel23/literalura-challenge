package com.example.literalura.service;

public interface IConvertirDatos {
    public <T> T obtenerDatos(String json, Class<T> clase);
}
