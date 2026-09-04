package com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacEnd.Errores;

public class ErrorLexico {
    private String lexemaLocal;
    private String descripcionLocal;
    private int filaLocal;
    private int columnaLocal;

    public ErrorLexico(String lexema, String descripcion, int fila, int columna) {
        lexemaLocal = lexema;
        descripcionLocal = descripcion;
        filaLocal = fila;
        columnaLocal = columna;
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
