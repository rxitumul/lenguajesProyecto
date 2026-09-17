package com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.AutomataCarpeta;

import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.ReportesCarpeta.RegistroDeTokens;
import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.ReportesCarpeta.ListasEnlazadas.ErrorLexico;

public class AutomataComandoIA extends AutomataPadre {

    protected int ejecutorDeAutomataConectoresIA(String conectorInicial, String texto, int columna, int linea) {

        int colInicio = Math.max(0, columna - (conectorInicial != null ? conectorInicial.length() : 0));

        StringBuilder cadenaAcumulada = new StringBuilder(conectorInicial != null ? conectorInicial : "");

        // Transición: q0 -> q1 (recibe conector IA: SOBRE, DESDE, EN, COMO)
        dotBiblioteca.agregarTransicion("q0_conIA", "q1_conIA","recibe conector IA: SOBRE, DESDE, EN, COMO");
        int estado = 1; // Estado q1: Esperando espacios, comillas o identificador

        while (columna < texto.length() && estado != 4 && estado != 0) {
            char caracterActual = texto.charAt(columna);

            switch (estado) {
                case 1: // Estado q1
                    if (caracterActual == ' ') {
                        dotBiblioteca.agregarTransicion("q1_conIA", "q1_conIA", "Espacio");
                        // Transición: q1 -> q1 (consume espacios en blanco)
                        cadenaAcumulada.append(caracterActual);
                        columna++;
                    } else if (caracterActual == '"') {
                        dotBiblioteca.agregarTransicion("q1_conIA", "q2_conIA", "Comillas apertura");
                        // Transición: q1 -> q2 (encuentra comilla de apertura para argumento literal)
                        cadenaAcumulada.append(caracterActual);
                        columna++;
                        estado = 2;
                    } else if (esLetra(caracterActual) || Character.isDigit(caracterActual) || caracterActual == '_') {
                        dotBiblioteca.agregarTransicion("q1_conIA", "q3_conIA", "letra");
                        // Transición: q1 -> q3 (inicio de argumento identificador)
                        cadenaAcumulada.append(caracterActual);
                        columna++;
                        estado = 3;
                    } else {
                        dotBiblioteca.agregarTransicion("q1_conIA", "q4_conIA", "delimitador");
                        // Transición: q1 -> q4 (delimitador o carácter de corte)
                        estado = 4;
                    }
                    break;

                case 2: // Estado q2: Leyendo contenido entre comillas
                    if (caracterActual == '"') {
                        // Transición: q2 -> q4 (comilla de cierre de argumento)
                        dotBiblioteca.agregarTransicion("q2_conIA", "q4_conIA", "Cierre de comillas");
                        cadenaAcumulada.append(caracterActual);
                        columna++;
                        estado = 4;
                    } else if (caracterActual == '\n' || caracterActual == '\r') {
                        dotBiblioteca.agregarTransicion("q2_conIA", "q0_conIA", "Error sin comillas");
                        // Transición: q2 -> q0 (error: salto de línea sin cerrar comilla)
                        estado = 0;
                    } else {
                        // Transición: q2 -> q2 (acumula caracteres de la cadena)
                        dotBiblioteca.agregarTransicion("q2_conIA", "q2_conIA", "Letra");
                        cadenaAcumulada.append(caracterActual);
                        columna++;
                    }
                    break;

                case 3: // Estado q3: Leyendo identificador
                    if (esLetra(caracterActual) || Character.isDigit(caracterActual) || caracterActual == '_') {
                        // Transición: q3 -> q3 (continúa leyendo identificador)
                        dotBiblioteca.agregarTransicion("q3_conIA", "q3_conIA", "Identificador");
                        cadenaAcumulada.append(caracterActual);
                        columna++;
                    } else if (caracterActual == ' ') {
                        // Transición: q3 -> q4 (espacio tras identificador)
                        dotBiblioteca.agregarTransicion("q3_conIA", "q4_conIA", "Identificador/Delimitador");
                        cadenaAcumulada.append(caracterActual);
                        columna++;
                        estado = 4;
                    } else {
                        dotBiblioteca.agregarTransicion("q3_conIA", "q4_conIA", "Identificador/Delimitador");
                        // Transición: q3 -> q4 (delimitador tras identificador)
                        estado = 4;
                    }
                    break;
            }
        }

        // Evaluación de estados de aceptación
        switch (estado) {
            case 4:
            case 3:
                // Estados de aceptación: q4, q3 (Conector IA con su argumento válido)
                graficaHtml.setComandoIA(graficaHtml.getComandoIA()+1);
                String tokenCompleto = cadenaAcumulada.toString().trim();
                String tipoBiblioteca = conectorInicial != null && bibliotecaDeTokens.existeEnLosTokens(conectorInicial)
                        ? bibliotecaDeTokens.mapeadorDeTokens(conectorInicial)
                        : "CONECTORES";
                String descBiblioteca = conectorInicial != null && bibliotecaDeTokens.existeEnLosTokens(conectorInicial)
                        ? bibliotecaDeTokens.getDescripcion(conectorInicial)
                        : "Conector de Inteligencia Artificial";
                registrarToken(new RegistroDeTokens(
                        tokenCompleto, tipoBiblioteca, descBiblioteca, linea, colInicio, tipoBiblioteca));
                break;
            default:
                // Estado de error: q0 (estructura mal cerrada o incompleta)
                registrarError(new ErrorLexico(
                        cadenaAcumulada.toString(), "Estructura de conector mal cerrada o incompleta", linea,
                        colInicio));
                break;
        }

        return columna;
    }

}
