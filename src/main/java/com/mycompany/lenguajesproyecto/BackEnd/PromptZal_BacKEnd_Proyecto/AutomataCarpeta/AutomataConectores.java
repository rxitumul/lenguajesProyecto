package com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.AutomataCarpeta;

import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.ReportesCarpeta.RegistroDeTokens;
import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.ReportesCarpeta.ListasEnlazadas.ErrorLexico;

public class AutomataConectores extends AutomataPadre {

    public int ejecutorDeAutomataConectoresCompleto(String conectorInicial, char caracterInicial, String texto,
            int columna, int linea) {
        if (columna >= texto.length()) {
            return columna;
        }

        int colInicio = columna;
        caracterInicial = texto.charAt(columna);

        // Rama A: Operador Compuesto '->'
        if (caracterInicial == '-' && columna + 1 < texto.length() && texto.charAt(columna + 1) == '>') {
            dotBiblioteca.agregarTransicion("q0_con", "q4_con", "Operador ->");
            columna += 2;
            String tipo = bibliotecaDeTokens.mapeadorDeTokens("->");
            String desc = bibliotecaDeTokens.getDescripcion("->");
            registrarToken(new RegistroDeTokens("->", tipo, desc, linea, colInicio, tipo));
            graficaHtml.setConectores(graficaHtml.getConectores() + 1);
            return columna;
        }

        // Rama B: Literales de cadena ("...")
        if (caracterInicial == '"') {
            StringBuilder cadena = new StringBuilder();
            cadena.append(caracterInicial);
            columna++;
            dotBiblioteca.agregarTransicion("q0_con", "q1_con", "Comillas");
            int estado = 1;

            while (columna < texto.length() && estado == 1) {
                char c = texto.charAt(columna);
                if (c == '"') {
                    columna++;
                    dotBiblioteca.agregarTransicion("q1_con", "q4_con", "Comillas");
                    cadena.append(c);
                    estado = 2;
                } else if (c == '\n' || c == '\r') {
                    dotBiblioteca.agregarTransicion("q1_con", "q0_con", "Sin cierre");
                    estado = 0;
                } else {
                    columna++;
                    dotBiblioteca.agregarTransicion("q1_con", "q1_con", "Letra");
                    cadena.append(c);
                }
            }

            if (estado == 2) {
            graficaHtml.setIdentificadores(graficaHtml.getIdentificadores() + 1);
                registrarToken(new RegistroDeTokens(
                        cadena.toString(), "OPERADORES_LITERALES_COMENTARIOS", "Literal de cadena válido", linea,
                        colInicio, "LITERAL_CADENA"));
            } else {
                registrarError(new ErrorLexico(
                        cadena.toString(), "Literal de cadena sin comilla de cierre", linea, colInicio));
            }
            return columna;
        }

        // Rama C: Constantes numéricas (Enteras y Decimales)
        if (Character.isDigit(caracterInicial)) {
            StringBuilder num = new StringBuilder();
            num.append(caracterInicial); // <-- CORREGIDO: Se añade el primer dígito que activó la rama
            columna++;

            dotBiblioteca.agregarTransicion("q0_con", "q1_con", "Digito");
            int estado = 1;

            while (columna < texto.length() && estado != 0 && estado != 4) {
                char c = texto.charAt(columna);
                switch (estado) {
                    case 1:
                        if (Character.isDigit(c)) {
                            dotBiblioteca.agregarTransicion("q1_con", "q1_con", "Numero");
                            num.append(c);
                            columna++;
                        } else if (c == '.' && columna + 1 < texto.length()
                                && Character.isDigit(texto.charAt(columna + 1))) {
                            dotBiblioteca.agregarTransicion("q1_con", "q2_con", "Decimal");
                            num.append(c);
                            columna++;
                            estado = 2;
                        } else {
                            dotBiblioteca.agregarTransicion("q1_con", "q4_con", "Fin Numero");
                            estado = 4;
                        }
                        break;
                    case 2:
                        if (Character.isDigit(c)) {
                            dotBiblioteca.agregarTransicion("q2_con", "q2_con", "Decimal");
                            num.append(c);
                            columna++;
                        } else {
                            dotBiblioteca.agregarTransicion("q2_con", "q4_con", "Fin Numero Decimal");
                            estado = 4;
                        }
                        break;
                }
            }

            String lex = num.toString();
            String tipo = lex.contains(".") ? "NUMERO_DECIMAL" : "NUMERO_ENTERO";
            registrarToken(new RegistroDeTokens(
                    lex, "OPERADORES_LITERALES_COMENTARIOS", "Constante numérica válida", linea, colInicio, tipo));
            graficaHtml.setIdentificadores(graficaHtml.getIdentificadores() + 1);
            return columna;
        }

        // Rama D: Operadores y símbolos de un solo carácter
        String op = String.valueOf(caracterInicial);
        if (bibliotecaDeTokens.existeEnLosTokens(op)) {
            dotBiblioteca.agregarTransicion("q0_con", "q4_con", op);
            columna++;
            String tipo = bibliotecaDeTokens.mapeadorDeTokens(op);
            String desc = bibliotecaDeTokens.getDescripcion(op);
            registrarToken(new RegistroDeTokens(op, tipo, desc, linea, colInicio, tipo));
            if (tipo.equals("CONECTORES")) {
                graficaHtml.setConectores(graficaHtml.getConectores() + 1);
            } else {
                graficaHtml.setIdentificadores(graficaHtml.getIdentificadores() + 1);
            }
            return columna;
        }

        // Estado de error: carácter desconocido
        registrarError(new ErrorLexico(
                op, "Caracter no reconocido por el analizador", linea, colInicio));
        return columna + 1;
    }
}
