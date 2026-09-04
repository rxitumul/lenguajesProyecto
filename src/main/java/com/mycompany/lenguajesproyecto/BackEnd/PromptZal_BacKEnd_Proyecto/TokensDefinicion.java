package com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto;

public class TokensDefinicion {
    private String tipo;
    private String descripcion;

    public TokensDefinicion(String tipo, String descripcion) {
        this.tipo = tipo;
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
