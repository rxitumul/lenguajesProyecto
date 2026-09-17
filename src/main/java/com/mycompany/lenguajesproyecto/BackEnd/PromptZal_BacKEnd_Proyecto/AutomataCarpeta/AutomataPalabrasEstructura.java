package com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.AutomataCarpeta;

import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.ReportesCarpeta.RegistroDeTokens;
import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.ReportesCarpeta.ListasEnlazadas.ErrorLexico;

public class AutomataPalabrasEstructura extends AutomataPadre {

    protected int ejecutorDeAutomataPalabrasEstructura(String palabraInicial, String texto, int columna, int linea) {
        int colInicio = Math.max(0, columna - (palabraInicial != null ? palabraInicial.length() : 0));
        StringBuilder cadenaAcumulada = new StringBuilder(palabraInicial != null ? palabraInicial : "");
        StringBuilder valor = new StringBuilder("");
        StringBuilder valorDos = new StringBuilder("");
        boolean parentesis = false;

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
                        cadenaAcumulada.append(caracterActual);
                        columna++;
                    } else if (caracterActual == '"') {
                        dotBiblioteca.agregarTransicion("q2_pal", "q4_pal", " Comillas");
                        cadenaAcumulada.append(caracterActual);
                        columna++;
                        estado = 4;
                    } else if (esLetra(caracterActual) || caracterActual == '_') {
                        // Única ruta: Manda al estado 8 unificado (identificador o función)
                        dotBiblioteca.agregarTransicion("q2_pal", "q8_pal", " inicio identificador/funcion");
                        cadenaAcumulada.append(caracterActual);
                        valorDos.append(caracterActual);
                        columna++;
                        estado = 8;
                    } else if (Character.isDigit(caracterActual)) {
                        dotBiblioteca.agregarTransicion("q2_pal", "q6_pal", " Numero");
                        estado = 6;
                    } else {
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
                        registrarToken(new RegistroDeTokens("\"" + valor + "\"", "Operadores, literales y comentarios",
                                "Literal de cadena: texto entre comillas dobles", linea, columna, "\"...\""));
                        columna++;
                        if (parentesis) {
                            estado = 9;
                        } else {
                            estado = 7;
                        }
                    } else if (caracterActual == '\n' || caracterActual == '\r') {
                        // Transición: q4 -> q0 (salto de línea sin cerrar comillas, estado de error)
                        dotBiblioteca.agregarTransicion("q4_pal", "q0_pal", " Sin comillas");
                        estado = 0;
                    } else {
                        // Transición: q4 -> q4 (continúa acumulando caracteres de la cadena)
                        dotBiblioteca.agregarTransicion("q4_pal", "q4_pal", " caracter");
                        cadenaAcumulada.append(caracterActual);
                        valor.append(caracterActual);
                        columna++;
                    }
                    break;

                case 8:// Estado q8 unificado: Lee letras/números y decide si es función o
                    if (esLetra(caracterActual) || Character.isDigit(caracterActual) || caracterActual == '_') {
                        dotBiblioteca.agregarTransicion("q8_pal", "q8_pal", " caracter");
                        cadenaAcumulada.append(caracterActual);
                        valorDos.append(caracterActual);
                        columna++;
                    } else if (caracterActual == ' ') {
                        dotBiblioteca.agregarTransicion("q8_pal", "q8_pal", " espacio");
                        cadenaAcumulada.append(caracterActual);
                        columna++;
                    } else if (caracterActual == '(') {
                        // ¡Era una función! (ej. CARGAR)
                        parentesis = true;
                        dotBiblioteca.agregarTransicion("q8_pal", "q9_pal", "parentesis apertura");

                        // Validación de tu token reservado
                        String palabraLeida = valorDos.toString().trim();
                        if (bibliotecaDeTokens.existeEnLosTokens(palabraLeida)) {
                            String tipoBiblioteca = bibliotecaDeTokens.mapeadorDeTokens(palabraLeida);
                            String descBiblioteca = bibliotecaDeTokens.getDescripcion(palabraLeida);
                            graficaHtml.setReservadas(graficaHtml.getReservadas() + 1);
                            registrarToken(new RegistroDeTokens(palabraLeida, tipoBiblioteca, descBiblioteca, linea,
                                    colInicio, tipoBiblioteca));
                        } else {
                            registrarError(new ErrorLexico(
                                    cadenaAcumulada.toString(), "Sintaxis de estructura incompleta o errónea", linea,
                                    columna));
                        }
                        cadenaAcumulada.append(caracterActual);

                        columna++;
                        estado = 9; // Se va al estado 9 para procesar el interior
                    } else {
                        // ¡Era un identificador normal! (ej. ventas = otra_variable)
                        // Aquí absorbe lo que antes hacía el estado 3
                        dotBiblioteca.agregarTransicion("q8_pal", "q6_pal", " Aceptacion identificador");
                        estado = 6;
                    }
                    break;

                case 9: // Estado q9: Procesando contenido dentro de CARGAR(...)
                    if (caracterActual == ' ') {
                        cadenaAcumulada.append(caracterActual);
                        columna++;
                    } else if (caracterActual == '"') {
                        // Abre comillas para la ruta del archivo ("ventas.csv")
                        dotBiblioteca.agregarTransicion("q9_pal", "q4_pal", "comillas archivo");
                        cadenaAcumulada.append(caracterActual);
                        columna++;
                        estado = 4; // Reutilizamos el estado 4 para leer el texto de adentro
                    } else if (caracterActual == ')') {
                        parentesis = false;
                        // Cierre de paréntesis final de la función
                        dotBiblioteca.agregarTransicion("q9_pal", "q10_pal", "parentesis cierre");
                        cadenaAcumulada.append(caracterActual);
                        columna++;
                        estado = 6; // O un estado de aceptación final para la instrucción completa
                    } else {
                        // Acumulando caracteres de la ruta si no usa comillas estrictas
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
                graficaHtml.setReservadas(graficaHtml.getReservadas() + 1);
                registrarToken(new RegistroDeTokens(
                        tokenCompleto, tipoBiblioteca, descBiblioteca, linea, colInicio, tipoBiblioteca));
                break;
            default:
                // Estado de error: q0 (estructura incompleta o mal formada)
                registrarError(new ErrorLexico(
                        cadenaAcumulada.toString(),
                        "Sintaxis de estructura incompleta o errónea para palabra reservada", linea, colInicio));
                break;
        }

        return columna;
    }

}
