package com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.AutomataCarpeta;

import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.ReportesCarpeta.ErrorLexico;
import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.ReportesCarpeta.RegistroDeTokens;

public class AutomataConectores extends AutomataPadre{
    
    protected  int ejecutorDeAutomataConectoresCompleto(String conectorInicial, char caracterInicial, String texto,
            int columna, int linea) {
        if (columna >= texto.length()) {
            return columna;
        }

        int colInicio = columna;
        caracterInicial = texto.charAt(columna);

        // Rama A: Operador Compuesto '->'
        if (caracterInicial == '-' && columna + 1 < texto.length() && texto.charAt(columna + 1) == '>') {
            columna += 2;
            reportes.agregarReporteValido(new RegistroDeTokens(
                    "->", bibliotecaDeTokens.getDescripcion("->"), linea, colInicio, "CONECTORES"));
            return columna;
        }

        // Rama B: Literales de cadena ("...")
        if (caracterInicial == '"') {
            StringBuilder cadena = new StringBuilder();
            cadena.append(caracterInicial);
            columna++;
            int estado = 1;
            while (columna < texto.length() && estado == 1) {
                char c = texto.charAt(columna);
                if (c == '"') {
                    cadena.append(c);
                    columna++;
                    estado = 2;
                } else if (c == '\n' || c == '\r') {
                    estado = 0;
                } else {
                    cadena.append(c);
                    columna++;
                }
            }

            if (estado == 2) {
                reportes.agregarReporteValido(new RegistroDeTokens(
                        cadena.toString(), "Literal de Cadena Válido", linea, colInicio, "LITERAL_CADENA"));
            } else {
                reportes.agregarReporteNoValido(new ErrorLexico(
                        cadena.toString(), "Literal de cadena sin comilla de cierre", linea, colInicio));
            }
            return columna;
        }

        if (Character.isDigit(caracterInicial)) {
            StringBuilder num = new StringBuilder();
            int estado = 1;

            while (columna < texto.length() && estado != 0 && estado != 4) {
                char c = texto.charAt(columna);
                switch (estado) {
                    case 1:
                        if (Character.isDigit(c)) {
                            num.append(c);
                            columna++;
                        } else if (c == '.' && columna + 1 < texto.length()
                                && Character.isDigit(texto.charAt(columna + 1))) {
                            num.append(c);
                            columna++;
                            estado = 2;
                        } else {
                            estado = 4;
                        }
                        break;
                    case 2:
                        if (Character.isDigit(c)) {
                            num.append(c);
                            columna++;
                        } else {
                            estado = 4;
                        }
                        break;
                }
            }

            String lex = num.toString();
            String tipo = lex.contains(".") ? "NUMERO_DECIMAL" : "NUMERO_ENTERO";
            reportes.agregarReporteValido(
                    new RegistroDeTokens(lex, "Constante numérica válida", linea, colInicio, tipo));
            return columna;
        }

        String op = String.valueOf(caracterInicial);
        if (bibliotecaDeTokens.existeEnLosTokens(op)) {
            columna++;
            reportes.agregarReporteValido(new RegistroDeTokens(
                    op, bibliotecaDeTokens.getDescripcion(op), linea, colInicio,
                    bibliotecaDeTokens.mapeadorDeTokens(op)));
            return columna;
        }

        reportes.agregarReporteNoValido(new ErrorLexico(
                op, "Caracter no reconocido por el analizador", linea, colInicio));
        return columna + 1;
    }

}
