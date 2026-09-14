package com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.AutomataCarpeta;

import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.ReportesCarpeta.RegistroDeTokens;
import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.ReportesCarpeta.ListasEnlazadas.ErrorLexico;

public class AutomataComandoIA extends AutomataPadre {

    protected int ejecutorDeAutomataConectoresIA(String conectorInicial, String texto, int columna, int linea) {
        int colInicio = Math.max(0, columna - (conectorInicial != null ? conectorInicial.length() : 0));
        StringBuilder cadenaAcumulada = new StringBuilder(conectorInicial != null ? conectorInicial : "");

        int estado = 1;

        while (columna < texto.length() && estado != 4 && estado != 0) {
            char caracterActual = texto.charAt(columna);

            switch (estado) {
                case 1:
                    if (caracterActual == ' ') {
                        cadenaAcumulada.append(caracterActual);
                        columna++;
                    } else if (caracterActual == '"') {
                        cadenaAcumulada.append(caracterActual);
                        columna++;
                        estado = 2;
                    } else if (esLetra(caracterActual) || Character.isDigit(caracterActual) || caracterActual == '_') {
                        cadenaAcumulada.append(caracterActual);
                        columna++;
                        estado = 3;
                    } else {

                        estado = 4;
                    }
                    break;

                case 2:
                    if (caracterActual == '"') {
                        cadenaAcumulada.append(caracterActual);
                        columna++;
                        estado = 4;
                    } else if (caracterActual == '\n' || caracterActual == '\r') {
                        estado = 0;
                    } else {
                        cadenaAcumulada.append(caracterActual);
                        columna++;
                    }
                    break;

                case 3:
                    if (esLetra(caracterActual) || Character.isDigit(caracterActual) || caracterActual == '_') {
                        cadenaAcumulada.append(caracterActual);
                        columna++; // Bucle en q3
                    } else if (caracterActual == ' ') {
                        cadenaAcumulada.append(caracterActual);
                        columna++;
                        estado = 4;
                    } else {
                        estado = 4;
                    }
                    break;
            }
        }

        switch (estado) {
            case 4:
            case 3:
                String tokenCompleto = cadenaAcumulada.toString().trim();
                reportes.agregarReporteValido(new RegistroDeTokens(
                        tokenCompleto, "Conector de Inteligencia Artificial Válido", linea, colInicio, "CONECTOR_IA"));
                break;
            default:
                reportes.agregarReporteNoValido(new ErrorLexico(
                        cadenaAcumulada.toString(), "Estructura de conector mal cerrada o incompleta", linea,
                        colInicio));
                break;
        }

        return columna;
    }

}
