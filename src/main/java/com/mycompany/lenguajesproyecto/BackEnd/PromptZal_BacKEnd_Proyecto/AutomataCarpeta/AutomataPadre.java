package com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.AutomataCarpeta;

import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.BibliotecaCarpeta.BibliotecaDeDot;
import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.BibliotecaCarpeta.BibliotecaDeTokens;
import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.ReportesCarpeta.Reportes;
import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.ReportesCarpeta.ListasEnlazadas.ErrorLexico;
import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.ReportesCarpeta.ReporteHTMLTabla;
import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.ReportesCarpeta.RegistroDeTokens;
import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.ReportesCarpeta.ReporteDeError;

public class AutomataPadre {

    protected BibliotecaDeTokens bibliotecaDeTokens;
    protected Reportes reportes;

    protected ReporteHTMLTabla reporteHTMLTablaCompartido;
    protected ReporteDeError reporteDeErrorCompartido;
    protected BibliotecaDeDot dotBiblioteca;

    public AutomataPadre() {
        bibliotecaDeTokens = new BibliotecaDeTokens();
        reportes = new Reportes();
        dotBiblioteca = new BibliotecaDeDot();
    }

    public BibliotecaDeDot getDotBiblioteca() {
        return dotBiblioteca;
    }

    public void setDotBiblioteca(BibliotecaDeDot dotBiblioteca) {
        this.dotBiblioteca = dotBiblioteca;
    }

    public Reportes getReportes() {
        return reportes;
    }

    public BibliotecaDeTokens getBibliotecaDeTokens() {
        return bibliotecaDeTokens;
    }

    /**
     * Inyecta los reportes compartidos del AnailizadorDeTexto.
     * Cuando están seteados, todos los tokens/errores se escriben directo ahí.
     */

    // Registra un token válido en el reporte compartido si existe, sino en el
    // interno
    protected void registrarToken(RegistroDeTokens token) {
        if (reporteHTMLTablaCompartido != null) {
            reporteHTMLTablaCompartido.registroDeTokens(token);
        } else {
            reportes.agregarReporteValido(token);
        }
    }

    // Registra un error en el reporte compartido si existe, sino en el interno
    protected void registrarError(ErrorLexico error) {
        if (reporteDeErrorCompartido != null) {
            reporteDeErrorCompartido.registrarError(error);
        } else {
            reportes.agregarReporteNoValido(error);
        }
    }

    public void setReportesCompartidos(ReporteHTMLTabla tablaTokens, ReporteDeError tablaErrores) {
        this.reporteHTMLTablaCompartido = tablaTokens;
        this.reporteDeErrorCompartido = tablaErrores;
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
