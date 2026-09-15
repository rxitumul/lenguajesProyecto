package com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.AutomataCarpeta;

import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.ReportesCarpeta.RegistroDeTokens;
import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.ReportesCarpeta.ListasEnlazadas.ErrorLexico;

public class AutomataDirectivas extends AutomataPadre {

    protected int ejecutorDeAutomataDirectivas(String palabra, String texto, int columna, int linea) {
        int colInicio = Math.max(0, columna - (palabra != null ? palabra.length() : 0));
        StringBuilder directivaCompleta = new StringBuilder(palabra != null ? palabra : "");
        StringBuilder valor = new StringBuilder();

        // Estado q1 -> q2: Se recibe la directiva inicial (@modelo, @rol, @formato)

        dotBiblioteca.agregarTransicion("q0_dir", "q1_dir", "espacio");

        int estado = 2; // Estado q2: Esperando espacios o comilla de apertura

        while (columna < texto.length() && estado != 4 && estado != 0) {
            char caracterActual = texto.charAt(columna);

            switch (estado) {
                case 2: // Estado q2
                    if (caracterActual == ' ') {
                        // Transición: q2 -> q2 (consume espacios en blanco)
                        directivaCompleta.append(caracterActual);
                        columna++;
                    } else if (caracterActual == '"') {
                        dotBiblioteca.agregarTransicion("q1_dir", "q2_dir", "Comillas apertura");
                        // Transición: q2 -> q3 (encuentra comilla de apertura)
                        directivaCompleta.append(caracterActual);
                        columna++;
                        estado = 3;
                    } else {
                        dotBiblioteca.agregarTransicion("q1_dir", "q0_dir", "Caracter no esperado");
                        // Transición: q2 -> q0 (carácter no esperado, estado de error)
                        estado = 0;
                    }
                    break;

                case 3: // Estado q3
                    if (caracterActual == '"') {
                        // Transición: q3 -> q4 (encuentra comilla de cierre)
                        dotBiblioteca.agregarTransicion("q2_dir", "q3_dir", "Comillas Cierre");
                        directivaCompleta.append(caracterActual);
                        columna++;
                        estado = 4;
                    } else if (caracterActual == '\n' || caracterActual == '\r') {
                        dotBiblioteca.agregarTransicion("q2_dir", "q0_dir", "No se cerro comilla");
                        // Transición: q3 -> q0 (salto de línea sin cerrar comilla, estado de error)
                        estado = 0;
                    } else {
                        dotBiblioteca.agregarTransicion("q2_dir", "q2_dir", "Letras");
                        // Transición: q3 -> q3 (lee contenido dentro de las comillas)
                        directivaCompleta.append(caracterActual);
                        valor.append(caracterActual);
                        columna++;
                    }
                    break;
            }
        }

        // Si la palabra no empieza con '@' pero se estructuró como directiva (ej:
        // modelo "contenido")
        if (palabra == null || !palabra.startsWith("@")) {
            if (estado == 4) {
                registrarError(new ErrorLexico(
                        directivaCompleta.toString(),
                        "Directiva inválida '" + palabra + "': debe iniciar con '@' (ej: @"
                                + (palabra != null ? palabra : "") + ")",
                        linea, colInicio));
                return columna;
            } else {
                // No tuvo estructura de directiva con comillas completas
                return colInicio + (palabra != null ? palabra.length() : 0);
            }
        }

        if (!bibliotecaDeTokens.existeEnLosTokens(palabra)) {
            registrarError(new ErrorLexico(
                    directivaCompleta.toString(),
                    "Directiva no reconocida: " + palabra,
                    linea, colInicio));
            return columna;
        }

        // Evaluación de estado final
        if (estado == 4) {
            // Estado de aceptación: q4 (Directiva y argumento de cadena válidos)
            String tipoBiblioteca = bibliotecaDeTokens.mapeadorDeTokens(palabra);
            String descBiblioteca = bibliotecaDeTokens.getDescripcion(palabra);
            registrarToken(new RegistroDeTokens(
                    directivaCompleta.toString(), tipoBiblioteca, descBiblioteca, linea, colInicio, tipoBiblioteca));
        } else if (estado == 3) {
            // Error en q3: Cadena sin comilla de cierre
            registrarError(new ErrorLexico(
                    directivaCompleta.toString(), "No se encontró cierre de comillas en la directiva: " + palabra,
                    linea, colInicio));
        } else {
            // Error en q0 / q2: Directiva incompleta
            registrarError(new ErrorLexico(
                    directivaCompleta.toString(),
                    "Directiva incompleta (se esperaba '\"valor\"' después de " + palabra + ")", linea, colInicio));
        }

        return columna;
    }

}
