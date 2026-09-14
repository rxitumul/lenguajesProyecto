package com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.ReportesCarpeta;

public class RegistroDeTokens {
    private String lexemaLocal;
    private String descripcionLocal;
    private int filaLocal;
    private int columnaLocal;
    private String tokenLocal;

    public RegistroDeTokens(String lexema, String descripcion, int linea, int columna, String token) {
        this.lexemaLocal = lexema;
        this.descripcionLocal = descripcion;
        this.filaLocal = linea;
        this.columnaLocal = columna;
        this.tokenLocal = token;
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

    @Override
    public String toString() {
        return "RegistroDeTokens [Token: " + tokenLocal + ", Lexema: '" + lexemaLocal + "', Fila: " + filaLocal + ", Columna: " + columnaLocal + ", Descripcion: '" + descripcionLocal + "']";
    }
}
