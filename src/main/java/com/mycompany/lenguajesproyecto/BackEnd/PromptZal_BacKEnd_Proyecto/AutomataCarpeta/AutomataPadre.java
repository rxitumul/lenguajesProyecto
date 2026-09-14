package com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.AutomataCarpeta;

import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.BibliotecaCarpeta.BibliotecaDeTokens;
import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.ReportesCarpeta.Reportes;

public class AutomataPadre {

    protected BibliotecaDeTokens bibliotecaDeTokens;
    protected Reportes reportes;

    public Reportes getReportes() {
        return reportes;
    }

    public BibliotecaDeTokens getBibliotecaDeTokens() {
        return bibliotecaDeTokens;
    }

    public AutomataPadre() {
        bibliotecaDeTokens = new BibliotecaDeTokens();
        reportes = new Reportes();
    }

    protected boolean esPalabraEstructura(String palabra) {
        return palabra.equals("AGENTE") || palabra.equals("contexto") || palabra.equals("variable")
                || palabra.equals("EJECUTAR") || palabra.equals("EXPORTAR");
    }

    protected boolean esConectorIA(String palabra) {
        return palabra.equals("SOBRE") || palabra.equals("DESDE") || palabra.equals("EN")
                || palabra.equals("COMO");
    }

    protected boolean esLetra(char c) {
        return Character.isLetter(c);
    }
}
