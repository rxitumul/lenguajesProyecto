package com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacEnd.TokensAnalizadores;

import java.io.File;
import java.io.IOException;
import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacEnd.Errores.ErrorLexico;

public class PalabrasReservadasDeEstructuraAnalizador extends Analizador {

    @Override
    protected int condicion(int columna, int fila, String linea, File file) throws IOException {
        columna = comando.saltarEspacios(linea, columna);

        switch (contadorDeIndices) {
            case 0: // AGENTE - espera un identificador a continuación
                columna = leerPalabraAgente(columna, fila, linea, "identificador de agente");
                break;
            case 1: // contexto - espera un bloque de contenido
                columna = leerPalabraContexto(columna, fila, linea, "valor de contexto");
                break;
            case 2: // variable - espera nombre y valor
                columna = leerPalabraVariable(columna, fila, linea, "nombre de variable", file);
                break;
            case 3: // EJECUTAR - espera un comando o bloque
                columna = leerPalabraEjecucion(columna, fila, linea, "bloque de ejecución");
                break;
            case 4: // EXPORTAR - espera un formato o destino
                columna = leerPalabraExportacion(columna, fila, linea, "destino de exportación");
                break;
            default:
                break;
        }

        return columna;
    }

    private int leerPalabraAgente(int columna, int fila, String linea, String contexto) throws IOException {
        columna = comando.saltarEspacios(linea, columna);

        String nombreAgente = comando.leerPalabra(linea, columna);
        // verifica a agente
        if (nombreAgente.isEmpty() || !Character.isLetter(nombreAgente.charAt(0))) {
            if (nombreAgente.isEmpty()) {
                reportesError.registrarError(
                        new ErrorLexico("Fin de linea", "Se esperaba " + contexto + " después de AGENTE revisa", fila,
                                columna));
            } else {
                reportesError.registrarError(
                        new ErrorLexico(nombreAgente, "Se esperaba " + contexto + " después de AGENTE revisa", fila,
                                columna));
            }
            return columna;
        }

        columna = columna + nombreAgente.length();
        agentesDeclarados.agregarAlFinal(nombreAgente);
        columna = comando.saltarEspacios(linea, columna);
        // verifica que despues del agente sigue una llave de apertura
        if (columna >= linea.length() || linea.charAt(columna) != '{') {
            String charDetectado;
            if (columna < linea.length()) {
                charDetectado = String.valueOf(linea.charAt(columna));
            } else {
                charDetectado = "Fin de linea";
            }
            reportesError.registrarError(
                    new ErrorLexico(charDetectado, "Se esperaba '{' después del identificador de agente", fila,
                            columna));
            return columna;
        }
        columna++;
        // verifica cierre
        columna = verificadorDeCierre(columna - 1, fila, '}');
        return columna;
    }

    private int leerPalabraContexto(int columna, int fila, String linea, String contexto) throws IOException {
        columna = comando.saltarEspacios(linea, columna);
        // verificar signo de igual
        if (columna >= linea.length() || linea.charAt(columna) != '=') {
            String charDetectado;
            if (columna < linea.length()) {
                charDetectado = String.valueOf(linea.charAt(columna));
            } else {
                charDetectado = "Fin de linea";
            }
            reportesError.registrarError(
                    new ErrorLexico(charDetectado, "Se esperaba '=' después de 'contexto' revisa", fila, columna));
            return columna;
        }

        columna++;
        columna = comando.saltarEspacios(linea, columna);
        // verificar comillas
        return lectorDeComillas(columna, fila, linea);
    }

    private int leerPalabraVariable(int columna, int fila, String linea, String contexto, File file)
            throws IOException {
        columna = comando.saltarEspacios(linea, columna);
        String nombreVar = comando.leerPalabra(linea, columna);
        // verificar nombre de variable
        if (nombreVar.isEmpty() || !Character.isLetter(nombreVar.charAt(0))) {
            if (nombreVar.isEmpty()) {
                reportesError.registrarError(
                        new ErrorLexico("Fin de linea", "Se esperaba " + contexto + " después de 'variable' revisa",
                                fila, columna));
            } else {
                reportesError.registrarError(
                        new ErrorLexico(nombreVar, "Se esperaba " + contexto + " después de 'variable' revisa", fila,
                                columna));
            }
            return columna;
        }

        columna = columna + nombreVar.length();
        // agrega la variable a las variables declaradas
        variablesDeclaradas.agregarAlFinal(nombreVar);
        columna = comando.saltarEspacios(linea, columna);
        // verifica signo de igual
        if (columna >= linea.length() || linea.charAt(columna) != '=') {
            String charDetectado;
            if (columna < linea.length()) {
                charDetectado = String.valueOf(linea.charAt(columna));
            } else {
                charDetectado = "Fin de linea";
            }
            reportesError.registrarError(
                    new ErrorLexico(charDetectado, "Se esperaba '=' después del nombre de la variable revisa", fila,
                            columna));
            return columna;
        }

        columna++;
        columna = comando.saltarEspacios(linea, columna);

        int inicioValor = columna;
        // mueve la columna hasta el fin de la expresion
        columna = movedorDeColumnasHastaFinDeEsprecion(linea, columna, fila);
        if (columna == inicioValor) {
            reportesError.registrarError(
                    new ErrorLexico("Fin de linea", "Se esperaba una expresión de valor para la variable revisa", fila,
                            columna));
        }
        return columna;
    }

    private int leerPalabraEjecucion(int columna, int fila, String linea, String contexto) throws IOException {
        columna = comando.saltarEspacios(linea, columna);
        String agenteAEjecutar = comando.leerPalabra(linea, columna);
        if (agenteAEjecutar.isEmpty()) {
            reportesError.registrarError(
                    new ErrorLexico("Fin de linea", "Se esperaba el nombre de un agente a ejecutar", fila, columna));
            return columna;
        }

        columna = columna + agenteAEjecutar.length();
        // verificar que exista un agente
        if (!agentesDeclarados.contiene(agenteAEjecutar)) {
            reportesError.registrarError(
                    new ErrorLexico(agenteAEjecutar, "El agente '" + agenteAEjecutar + "' no ha sido declarado", fila,
                            columna - agenteAEjecutar.length()));
        }
        return columna;
    }

    private int leerPalabraExportacion(int columna, int fila, String linea, String contexto) throws IOException {
        columna = comando.saltarEspacios(linea, columna);

        while (columna < linea.length()) {
            columna = comando.saltarEspacios(linea, columna);
            if (columna >= linea.length()) {
                break;
            }

            // Si es un comentario, terminamos de leer la exportación
            if (linea.charAt(columna) == '/' && columna + 1 < linea.length() &&
                    (linea.charAt(columna + 1) == '/' || linea.charAt(columna + 1) == '*')) {
                break;
            }

            String token = comando.leerPalabra(linea, columna);
            if (token.isEmpty()) {
                char ch = linea.charAt(columna);
                if (ch != ',') {
                    reportesError.registrarError(
                            new ErrorLexico("unn", "Se esperaba un nombre de variable para exportar", fila,
                                    columna));
                    columna++;
                    continue;
                }
            } else {
                // verificar nombre de variable
                if (!Character.isLetter(token.charAt(0))) {
                    reportesError.registrarError(
                            new ErrorLexico(token, "Se esperaba un nombre de variable valido para exportar", fila,
                                    columna));
                }
                // verificar que exista la variable
                else if (!variablesDeclaradas.contiene(token)) {
                    reportesError.registrarError(
                            new ErrorLexico(token, "La variable '" + token + "' no ha sido declarada", fila, columna));
                }
                columna = columna + token.length();
            }

            columna = comando.saltarEspacios(linea, columna);
            if (columna < linea.length() && linea.charAt(columna) == ',') {
                columna++;
            }
        }
        return columna;
    }

    protected String[] token() {
        return tokens.getPALABRAS_RESERVADAS();
    }
}
