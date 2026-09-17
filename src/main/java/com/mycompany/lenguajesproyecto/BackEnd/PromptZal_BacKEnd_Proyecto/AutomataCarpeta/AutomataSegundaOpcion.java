package com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.AutomataCarpeta;

import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.BibliotecaCarpeta.BibliotecaDeDot;
import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.ReportesCarpeta.BibliotecaDeReporteDeUsoDeTokensHtml;
import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.ReportesCarpeta.RegistroDeTokens;
import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.ReportesCarpeta.ReporteDeError;
import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.ReportesCarpeta.ReporteHTMLTabla;

public class AutomataSegundaOpcion extends AutomataPadre {
    private AutomataComandoIA IA;
    private AutomataPalabrasEstructura estructura;
    private AutomataDirectivas directiva;
    private AutomataConectores conectores;

    /*
     * private int ejecutorDeAutomataBuscadorDePalabras(char caracterInicial, String
     * texto, int columna, int linea) {
     * if (texto == null || columna < 0 || columna >= texto.length()) {
     * return columna;
     * }
     * 
     * int colInicio = columna;
     * if (!esLetra(caracterInicial) && caracterInicial != '_') {
     * return columna;
     * }
     * 
     * StringBuilder palabraEncontrada = new StringBuilder();
     * while (columna < texto.length() && (esLetra(texto.charAt(columna)) ||
     * Character.isDigit(texto.charAt(columna))
     * || texto.charAt(columna) == '_')) {
     * palabraEncontrada.append(texto.charAt(columna));
     * columna++;
     * }
     * 
     * String lexema = palabraEncontrada.toString();
     * 
     * if (bibliotecaDeTokens.existeEnLosTokens(lexema)) {
     * String tipo = bibliotecaDeTokens.mapeadorDeTokens(lexema);
     * String desc = bibliotecaDeTokens.getDescripcion(lexema);
     * registrarToken(new RegistroDeTokens(lexema, tipo, desc, linea, colInicio,
     * tipo));
     * } else {
     * registrarToken(new RegistroDeTokens(
     * lexema, "IDENTIFICADOR", "Identificador alfanumérico válido", linea,
     * colInicio, "IDENTIFICADOR"));
     * }
     * 
     * return columna;
     * }
     */

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

            // Transición: q0 -> q1 (lee primer carácter: letra, '@' o '_')
            dotBiblioteca.agregarTransicion("q0", "q1", "letra / @ / _");
            int estado = 1; // Estado q1: Leyendo cuerpo de la palabra
            columna++;

            while (columna < texto.length() && estado == 1) {
                char caracterActual = texto.charAt(columna);

                if (esLetra(caracterActual) || Character.isDigit(caracterActual) || caracterActual == '_') {
                    // Transición: q1 -> q1 (bucle en q1: acumula letras, dígitos o guion bajo)
                    dotBiblioteca.agregarTransicion("q1", "q1", "letra / digito / _");
                    palabraEncontrada.append(caracterActual);
                    columna++;
                } else if (caracterActual == '=' || caracterActual == '\n' || caracterActual == '"'
                        || caracterActual == ' ' || caracterActual == '-' || caracterActual == '+') {
                    dotBiblioteca.agregarTransicion("q1", "q2", "separador");
                    // Transición: q1 -> q2 (delimitador o símbolo de corte)
                    estado = 2;
                } else {
                    // Transición: q1 -> q2 (corte por fin de palabra)
                    dotBiblioteca.agregarTransicion("q1", "q2", "separador");
                    estado = 2;
                    break;
                }
            }

            if (estado == 1) {
                // Transición: q1 -> q2 (fin del texto)
                dotBiblioteca.agregarTransicion("q1", "q2", "fin de texto");
                estado = 2;
            }

            if (estado == 2) {
                // Estado de aceptación inicial: q2 (Palabra completa identificada)
                String palabra = palabraEncontrada.toString();
                boolean reportada = false;
                int verificador = 0;

                while (!reportada && verificador <= 4) {
                    int columnaTemporal = columna;

                    switch (verificador) {
                        case 0:
                            // Transición: q2 -> Sub-autómata Directivas
                            if (palabra.startsWith("@") || palabra.equalsIgnoreCase("modelo")
                                    || palabra.equalsIgnoreCase("rol") || palabra.equalsIgnoreCase("formato")
                                    || bibliotecaDeTokens.mapeadorDeTokens(palabra).equals("DIRECTIVA")) {
                                dotBiblioteca.agregarTransicion("q2", "q0_dir", "Palabra Directiva");
                                int colAntes = columnaTemporal;
                                int colDespues = directiva.ejecutorDeAutomataDirectivas(palabra, texto, columnaTemporal,
                                        linea);
                                if (palabra.startsWith("@") || colDespues > colAntes) {
                                    columna = colDespues;
                                    reportada = true;
                                    break;
                                }
                            }
                            verificador++;
                            break;

                        case 1:
                            // Transición: q2 -> Sub-autómata Palabras de Estructura
                            if (esPalabraEstructura(palabra)
                                    || bibliotecaDeTokens.mapeadorDeTokens(palabra).equals("PALABRA_RESERVADA")) {
                                dotBiblioteca.agregarTransicion("q2", "q0_pal", "Palabra Estructura|Palabra Reservada");
                                columna = estructura.ejecutorDeAutomataPalabrasEstructura(palabra, texto,
                                        columnaTemporal, linea);
                                reportada = true;
                            } else {
                                verificador++;
                            }
                            break;

                        case 2:
                            // Transición: q2 -> Sub-autómata Conectores IA
                            if (esConectorIA(palabra)
                                    || bibliotecaDeTokens.mapeadorDeTokens(palabra).equals("CONECTORES")) {
                                dotBiblioteca.agregarTransicion("q2", "q0_conIA", "Conector IA");
                                columna = IA.ejecutorDeAutomataConectoresIA(palabra, texto, columnaTemporal, linea);
                                reportada = true;
                            } else {
                                verificador++;
                            }
                            break;

                        case 3:
                            // Transición: q2 -> Token reservado en BibliotecaDeTokens (Comandos IA, etc.)
                            if (bibliotecaDeTokens.existeEnLosTokens(palabra)) {
                                dotBiblioteca.agregarTransicion("q2", "q2", "Lexema");
                                String tipo = bibliotecaDeTokens.mapeadorDeTokens(palabra);
                                String desc = bibliotecaDeTokens.getDescripcion(palabra);
                                registrarToken(new RegistroDeTokens(palabra, tipo, desc, linea, colInicio, tipo));
                                if (tipo.equals("OPERADORES_LITERALES_COMENTARIOS_IDENTIFICADORES") || palabra.equals("analista")) {
                                    graficaHtml.setIdentificadores(graficaHtml.getIdentificadores() + 1);
                                }
                                reportada = true;
                            } else {
                                verificador++;
                            }
                            break;

                        case 4:
                            dotBiblioteca.agregarTransicion("q2", "q2", "Identificador");
                            // Transición: q2 -> Identificador alfanumérico válido
                            graficaHtml.setIdentificadores(graficaHtml.getIdentificadores()+1);
                            registrarToken(new RegistroDeTokens(
                                    palabra, "OPERADORES_LITERALES_COMENTARIOS_IDENTIFICADORES", "Identificador alfanumérico", linea, colInicio,
                                    "OPERADORES_LITERALES_COMENTARIOS_IDENTIFICADORES"));
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

        dotBiblioteca.agregarTransicion("q0", "q0_con", "Conectores/Símbolos");
        return conectores.ejecutorDeAutomataConectoresCompleto("", caracterInicial, texto, columna, linea);
    }

    public AutomataSegundaOpcion() {
        super();
        IA = new AutomataComandoIA();
        estructura = new AutomataPalabrasEstructura();
        directiva = new AutomataDirectivas();
        conectores = new AutomataConectores();
        propagarDotBiblioteca();
    }

    private void propagarDotBiblioteca() {
        if (IA != null)
            IA.setDotBiblioteca(this.dotBiblioteca);
        if (estructura != null)
            estructura.setDotBiblioteca(this.dotBiblioteca);
        if (directiva != null)
            directiva.setDotBiblioteca(this.dotBiblioteca);
        if (conectores != null)
            conectores.setDotBiblioteca(this.dotBiblioteca);
    }

    @Override
    public void setDotBiblioteca(BibliotecaDeDot dotBiblioteca) {
        super.setDotBiblioteca(dotBiblioteca);
        propagarDotBiblioteca();
    }

    @Override
    public void setReportesCompartidos(ReporteHTMLTabla tablaTokens, ReporteDeError tablaErrores,
            BibliotecaDeReporteDeUsoDeTokensHtml graficaHtml) {
        super.setReportesCompartidos(tablaTokens, tablaErrores, graficaHtml);
        IA.setReportesCompartidos(tablaTokens, tablaErrores, graficaHtml);
        estructura.setReportesCompartidos(tablaTokens, tablaErrores, graficaHtml);
        directiva.setReportesCompartidos(tablaTokens, tablaErrores, graficaHtml);
        conectores.setReportesCompartidos(tablaTokens, tablaErrores, graficaHtml);
        propagarDotBiblioteca();
    }

}
