package com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.AutomataCarpeta;

import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.ReportesCarpeta.ErrorLexico;
import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.ReportesCarpeta.RegistroDeTokens;

public class AutomataDirectivas  extends AutomataPadre {
    protected int ejecutorDeAutomataDirectivas(String palabra, String texto, int columna, int linea) {
        int colInicio = Math.max(0, columna - (palabra != null ? palabra.length() : 0));
        StringBuilder directivaCompleta = new StringBuilder(palabra != null ? palabra : "");
        StringBuilder valor = new StringBuilder();

        int estado = 2;

        while (columna < texto.length() && estado != 4 && estado != 0) {
            char caracterActual = texto.charAt(columna);

            switch (estado) {
                case 2:
                    if (caracterActual == ' ') {
                        directivaCompleta.append(caracterActual);
                        columna++;
                    } else if (caracterActual == '"') {
                        directivaCompleta.append(caracterActual);
                        columna++;
                        estado = 3;
                    } else {
                        estado = 0;
                    }
                    break;

                case 3:
                    if (caracterActual == '"') {
                        directivaCompleta.append(caracterActual);
                        columna++;
                        estado = 4;
                    } else if (caracterActual == '\n' || caracterActual == '\r') {
                        estado = 0;
                    } else {
                        directivaCompleta.append(caracterActual);
                        valor.append(caracterActual);
                        columna++;
                    }
                    break;
            }
        }

        if (estado == 4) {
            reportes.agregarReporteValido(new RegistroDeTokens(
                    directivaCompleta.toString(), "Directiva declarada correctamente", linea, colInicio, "DIRECTIVA"));
        } else if (estado == 3) {
            reportes.agregarReporteNoValido(new ErrorLexico(
                    directivaCompleta.toString(), "No se encontró cierre de comillas en directiva", linea, colInicio));
        } else {
            reportes.agregarReporteNoValido(new ErrorLexico(
                    directivaCompleta.toString(), "Estructura de directiva incompleta", linea, colInicio));
        }

        return columna;
    }

}
