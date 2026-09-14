package com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.AutomataCarpeta;

import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.ReportesCarpeta.ErrorLexico;
import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.ReportesCarpeta.RegistroDeTokens;
import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.BibliotecaCarpeta.BibliotecaDeTokens;
import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.ReportesCarpeta.Reportes;

public class AutomataSegundaOpcion extends AutomataPadre {
    private AutomataComandoIA IA;
    private AutomataPalabrasEstructura estructura;
    private AutomataDirectivas directiva;
    private AutomataConectores conectores;

    public AutomataSegundaOpcion() {
        super();
        IA = new AutomataComandoIA();
        estructura = new AutomataPalabrasEstructura();
        directiva = new AutomataDirectivas();
        conectores = new AutomataConectores();
    }

    public int ejecutorDeAutomataBuscadorDePalabras(char caracterInicial, String texto, int columna, int linea) {
        if (texto == null || columna < 0 || columna >= texto.length()) {
            return columna;
        }

        int colInicio = columna;
        if (!esLetra(caracterInicial) && caracterInicial != '_') {
            return columna;
        }

        StringBuilder palabraEncontrada = new StringBuilder();
        while (columna < texto.length() && (esLetra(texto.charAt(columna)) || Character.isDigit(texto.charAt(columna))
                || texto.charAt(columna) == '_')) {
            palabraEncontrada.append(texto.charAt(columna));
            columna++;
        }

        String lexema = palabraEncontrada.toString();
        if (bibliotecaDeTokens.existeEnLosTokens(lexema)) {
            String tipo = bibliotecaDeTokens.mapeadorDeTokens(lexema);
            String desc = bibliotecaDeTokens.getDescripcion(lexema);
            reportes.agregarReporteValido(new RegistroDeTokens(lexema, desc, linea, colInicio, tipo));
        } else {
            reportes.agregarReporteValido(new RegistroDeTokens(
                    lexema, "Identificador alfanumérico válido", linea, colInicio, "IDENTIFICADOR"));
        }

        return columna;
    }

    public int ejecutorDeAutomataInicial(char caracterInicial, String texto, int columna, int linea) {
        if (texto == null || columna < 0 || columna >= texto.length()) {
            return columna;
        }

        caracterInicial = texto.charAt(columna);

        if (caracterInicial == ' ' || caracterInicial == '\t' || caracterInicial == '\r') {
            return columna + 1;
        }

        if (esLetra(caracterInicial) || caracterInicial == '@' || caracterInicial == '_') {
            int colInicio = columna;
            StringBuilder palabraEncontrada = new StringBuilder();
            palabraEncontrada.append(caracterInicial);

            int estado = 1;
            columna++;

            while (columna < texto.length() && estado == 1) {
                char caracterActual = texto.charAt(columna);

                if (esLetra(caracterActual) || Character.isDigit(caracterActual) || caracterActual == '_') {
                    palabraEncontrada.append(caracterActual);
                    columna++;
                } else if (caracterActual == '=' || caracterActual == '\n' || caracterActual == '"'
                        || caracterActual == ' ' || caracterActual == '-' || caracterActual == '+') {

                    estado = 2;
                } else {
                    estado = 2;
                    break;
                }
            }

            if (estado == 1) {
                estado = 2;
            }

            if (estado == 2) {
                String palabra = palabraEncontrada.toString();
                boolean reportada = false;
                int verificador = 0;

                while (!reportada && verificador <= 4) {
                    int columnaTemporal = columna;

                    switch (verificador) {
                        case 0:

                            if (palabra.startsWith("@")
                                    || bibliotecaDeTokens.mapeadorDeTokens(palabra).equals("DIRECTIVA")) {
                                columna = directiva.ejecutorDeAutomataDirectivas(palabra, texto, columnaTemporal,
                                        linea);
                                reportada = true;
                            } else {
                                verificador++;
                            }
                            break;

                        case 1:

                            if (esPalabraEstructura(palabra)
                                    || bibliotecaDeTokens.mapeadorDeTokens(palabra).equals("PALABRA_RESERVADA")) {
                                columna = estructura.ejecutorDeAutomataPalabrasEstructura(palabra, texto,
                                        columnaTemporal, linea);
                                reportada = true;
                            } else {
                                verificador++;
                            }
                            break;

                        case 2:

                            if (esConectorIA(palabra)
                                    || bibliotecaDeTokens.mapeadorDeTokens(palabra).equals("CONECTORES")) {
                                columna = IA.ejecutorDeAutomataConectoresIA(palabra, texto, columnaTemporal, linea);
                                reportada = true;
                            } else {
                                verificador++;
                            }
                            break;

                        case 3:

                            if (bibliotecaDeTokens.existeEnLosTokens(palabra)) {
                                String tipo = bibliotecaDeTokens.mapeadorDeTokens(palabra);
                                String desc = bibliotecaDeTokens.getDescripcion(palabra);
                                reportes.agregarReporteValido(
                                        new RegistroDeTokens(palabra, desc, linea, colInicio, tipo));
                                reportada = true;
                            } else {
                                verificador++;
                            }
                            break;

                        case 4:

                            reportes.agregarReporteValido(new RegistroDeTokens(
                                    palabra, "Identificador alfanumérico", linea, colInicio, "IDENTIFICADOR"));
                            reportada = true;
                            break;

                        default:
                            reportada = true;
                            break;
                    }
                }
                return columna;
            }
        }

        return conectores.ejecutorDeAutomataConectoresCompleto("", caracterInicial, texto, columna, linea);
    }

}
