package com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.AutomataCarpeta;

import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.ReportesCarpeta.RegistroDeTokens;
import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.ReportesCarpeta.ListasEnlazadas.ErrorLexico;

public class AutomataPalabrasEstructura extends AutomataPadre {

    protected int ejecutorDeAutomataPalabrasEstructura(String palabraInicial, String texto, int columna, int linea) {
        int colInicio = Math.max(0, columna - (palabraInicial != null ? palabraInicial.length() : 0));
        StringBuilder cadenaAcumulada = new StringBuilder(palabraInicial != null ? palabraInicial : "");

        // Transición: q0 -> q1 (recibe palabra reservada de estructura: AGENTE,
        // variable, contexto, etc.)
        dotBiblioteca.agregarTransicion("q0_pal", "q1_pal", "AGENTE, variable, contexto, etc");
        int estado = 1; // Estado q1: Esperando '=', espacios o identificador

        while (columna < texto.length() && estado != 0 && estado != 6 && estado != 7) {
            char caracterActual = texto.charAt(columna);

            switch (estado) {
                case 1: // Estado q1
                    if (caracterActual == ' ') {
                        dotBiblioteca.agregarTransicion("q1_pal", "q1_pal", "espacios en blanco");
                        // Transición: q1 -> q1 (consume espacios en blanco)
                        cadenaAcumulada.append(caracterActual);
                        columna++;
                    } else if (caracterActual == '=') {
                        dotBiblioteca.agregarTransicion("q1_pal", "q2_pal", "operador '='");
                        // Transición: q1 -> q2 (encuentra operador '=')
                        cadenaAcumulada.append(caracterActual);
                        columna++;
                        estado = 2;
                    } else if (esLetra(caracterActual) || caracterActual == '_') {
                        // Transición: q1 -> q5 (inicio de identificador tras palabra reservada)
                        dotBiblioteca.agregarTransicion("q1_pal", "q5_pal", " identificador");
                        cadenaAcumulada.append(caracterActual);
                        columna++;
                        estado = 5;
                    } else {
                        // Transición: q1 -> q6 (corte de palabra/delimitador)
                        dotBiblioteca.agregarTransicion("q1_pal", "q6_pal", " Delimitador/Palabra");
                        estado = 6;
                    }
                    break;

                case 5: // Estado q5: Leyendo identificador
                    if (esLetra(caracterActual) || Character.isDigit(caracterActual) || caracterActual == '_') {
                        // Transición: q5 -> q5 (continúa leyendo identificador)
                        dotBiblioteca.agregarTransicion("q5_pal", "q5_pal", " caracter");
                        cadenaAcumulada.append(caracterActual);
                        columna++;
                    } else if (caracterActual == ' ') {
                        // Transición: q5 -> q5 (consume espacios tras identificador)
                        dotBiblioteca.agregarTransicion("q5_pal", "q5_pal", " caracter");
                        cadenaAcumulada.append(caracterActual);
                        columna++;
                    } else if (caracterActual == '=') {
                        // Transición: q5 -> q2 (encuentra operador '=')
                        dotBiblioteca.agregarTransicion("q5_pal", "q2_pal", "operador '='");
                        cadenaAcumulada.append(caracterActual);
                        columna++;
                        estado = 2;
                    } else {
                        // Transición: q5 -> q6 (corte hacia estado de aceptación)
                        dotBiblioteca.agregarTransicion("q5_pal", "q6_pal", "Carcater");
                        estado = 6;
                    }
                    break;

                case 2: // Estado q2: Esperando valor tras '='
                    if (caracterActual == ' ') {
                        dotBiblioteca.agregarTransicion("q2_pal", "q2_pal", " caracter");
                        // Transición: q2 -> q2 (consume espacios)
                        cadenaAcumulada.append(caracterActual);
                        columna++;
                    } else if (caracterActual == '"') {
                        // Transición: q2 -> q4 (apertura de literal de cadena)
                        dotBiblioteca.agregarTransicion("q2_pal", "q4_pal", " Comillas");
                        cadenaAcumulada.append(caracterActual);
                        columna++;
                        estado = 4;
                    } else if (esLetra(caracterActual) || caracterActual == '_') {
                        // Transición: q2 -> q3 (inicio de identificador asignado)
                        dotBiblioteca.agregarTransicion("q2_pal", "q3_pal", " caracter");
                        cadenaAcumulada.append(caracterActual);
                        columna++;
                        estado = 3;
                    } else if (Character.isDigit(caracterActual)) {
                        // Transición: q2 -> q6 (valor numérico asignado)
                        dotBiblioteca.agregarTransicion("q2_pal", "q6_pal", " Numero");
                        estado = 6;
                    } else {
                        // Transición: q2 -> q0 (carácter inválido tras '=')
                        dotBiblioteca.agregarTransicion("q2_pal", "q0_pal", " caracter Invalido");
                        estado = 0;
                    }
                    break;

                case 3: // Estado q3: Leyendo identificador asignado tras '='
                    if (esLetra(caracterActual) || Character.isDigit(caracterActual) || caracterActual == '_') {
                        // Transición: q3 -> q3 (continúa leyendo identificador)
                        dotBiblioteca.agregarTransicion("q3_pal", "q3_pal", " Identificador");
                        cadenaAcumulada.append(caracterActual);
                        columna++;
                    } else {
                        // Transición: q3 -> q6 (corte hacia estado de aceptación)
                        dotBiblioteca.agregarTransicion("q3_pal", "q6_pal", " Aceptacion");
                        estado = 6;
                    }
                    break;

                case 4: // Estado q4: Leyendo contenido de cadena tras '"'
                    if (caracterActual == '"') {
                        // Transición: q4 -> q7 (cierre de comillas de cadena)
                        dotBiblioteca.agregarTransicion("q4_pal", "q7_pal", " Cierre de comillas");
                        cadenaAcumulada.append(caracterActual);
                        columna++;
                        estado = 7;
                    } else if (caracterActual == '\n' || caracterActual == '\r') {
                        // Transición: q4 -> q0 (salto de línea sin cerrar comillas, estado de error)
                        dotBiblioteca.agregarTransicion("q4_pal", "q0_pal", " Sin comillas");
                        estado = 0;
                    } else {
                        // Transición: q4 -> q4 (continúa acumulando caracteres de la cadena)
                        dotBiblioteca.agregarTransicion("q4_pal", "q4_pal", " caracter");
                        cadenaAcumulada.append(caracterActual);
                        columna++;
                    }
                    break;
            }
        }

        // Evaluación de estados de aceptación
        switch (estado) {
            case 1:
            case 3:
            case 5:
            case 6:
            case 7:
                // Estados de aceptación: q1, q3, q5, q6, q7 (Estructura sintáctica válida)
                String tokenCompleto = cadenaAcumulada.toString().trim();

                String tipoBiblioteca = bibliotecaDeTokens.mapeadorDeTokens(palabraInicial);
                String descBiblioteca = bibliotecaDeTokens.getDescripcion(palabraInicial);

                registrarToken(new RegistroDeTokens(
                        tokenCompleto, tipoBiblioteca, descBiblioteca, linea, colInicio, tipoBiblioteca));
                break;
            default:
                // Estado de error: q0 (estructura incompleta o mal formada)
                registrarError(new ErrorLexico(
                        cadenaAcumulada.toString(), "Sintaxis de estructura incompleta o errónea", linea, colInicio));
                break;
        }

        return columna;
    }

}
