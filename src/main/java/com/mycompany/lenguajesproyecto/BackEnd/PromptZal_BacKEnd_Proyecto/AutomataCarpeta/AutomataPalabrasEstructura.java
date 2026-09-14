package com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.AutomataCarpeta;

import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.ReportesCarpeta.ErrorLexico;
import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.ReportesCarpeta.RegistroDeTokens;

public class AutomataPalabrasEstructura extends AutomataPadre {

    protected  int ejecutorDeAutomataPalabrasEstructura(String palabraInicial, String texto, int columna, int linea) {
        int colInicio = Math.max(0, columna - (palabraInicial != null ? palabraInicial.length() : 0));
        StringBuilder cadenaAcumulada = new StringBuilder(palabraInicial != null ? palabraInicial : "");

        int estado = 1;

        while (columna < texto.length() && estado != 0 && estado != 6 && estado != 7) {
            char caracterActual = texto.charAt(columna);

            switch (estado) {
                case 1:
                    if (caracterActual == ' ') {
                        cadenaAcumulada.append(caracterActual);
                        columna++;
                    } else if (caracterActual == '=') {
                        cadenaAcumulada.append(caracterActual);
                        columna++;
                        estado = 2;
                    } else if (esLetra(caracterActual) || caracterActual == '_') {
                        cadenaAcumulada.append(caracterActual);
                        columna++;
                        estado = 5;
                    } else {

                        estado = 6;
                    }
                    break;

                case 5:
                    if (esLetra(caracterActual) || Character.isDigit(caracterActual) || caracterActual == '_') {
                        cadenaAcumulada.append(caracterActual);
                        columna++;
                    } else if (caracterActual == ' ') {
                        cadenaAcumulada.append(caracterActual);
                        columna++;
                    } else if (caracterActual == '=') {
                        cadenaAcumulada.append(caracterActual);
                        columna++;
                        estado = 2;
                    } else {
                        estado = 6;
                    }
                    break;

                case 2:
                    if (caracterActual == ' ') {
                        cadenaAcumulada.append(caracterActual);
                        columna++;
                    } else if (caracterActual == '"') {
                        cadenaAcumulada.append(caracterActual);
                        columna++;
                        estado = 4;
                    } else if (esLetra(caracterActual) || caracterActual == '_') {
                        cadenaAcumulada.append(caracterActual);
                        columna++;
                        estado = 3;
                    } else if (Character.isDigit(caracterActual)) {
                        estado = 6;
                    } else {
                        estado = 0;
                    }
                    break;

                case 3:
                    if (esLetra(caracterActual) || Character.isDigit(caracterActual) || caracterActual == '_') {
                        cadenaAcumulada.append(caracterActual);
                        columna++;
                    } else {
                        estado = 6;
                    }
                    break;

                case 4:
                    if (caracterActual == '"') {
                        cadenaAcumulada.append(caracterActual);
                        columna++;
                        estado = 7;
                    } else if (caracterActual == '\n' || caracterActual == '\r') {
                        estado = 0;
                    } else {
                        cadenaAcumulada.append(caracterActual);
                        columna++;
                    }
                    break;
            }
        }

        switch (estado) {
            case 1:
            case 3:
            case 5:
            case 6:
            case 7:
                String tokenCompleto = cadenaAcumulada.toString().trim();
                reportes.agregarReporteValido(new RegistroDeTokens(
                        tokenCompleto, "Palabra Reservada de Estructura Válida", linea, colInicio, "ESTRUCTURA"));
                break;
            default:
                reportes.agregarReporteNoValido(new ErrorLexico(
                        cadenaAcumulada.toString(), "Sintaxis de estructura incompleta o errónea", linea, colInicio));
                break;
        }

        return columna;
    }

}
