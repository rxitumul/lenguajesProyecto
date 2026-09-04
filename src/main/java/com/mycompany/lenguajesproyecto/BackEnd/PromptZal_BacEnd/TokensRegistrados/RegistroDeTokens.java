package com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacEnd.TokensRegistrados;

public class RegistroDeTokens {
    private String lexemaLocal;
    private String descripcionLocal;
    private int filaLocal;
    private int columnaLocal;
    private String tokenLocal;

    public RegistroDeTokens(String lexema, String descripcion, int linea, int columna, String token) {
        lexemaLocal = lexema;
        descripcionLocal = descripcion;
        filaLocal = linea;
        columnaLocal = columna;
        tokenLocal = token;
    }

    public String getToken() {
        return tokenLocal;
    }

    public String getLexema() {
        return lexemaLocal;
    }

    public String getDescripcion() {
        return descripcionLocal;
    }

    public int getFila() {
        return filaLocal;
    }

    public int getColumna() {
        return columnaLocal;
    }

}
