package com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto;

import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacEnd.Errores.ErrorLexico;
import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacEnd.TokensRegistrados.RegistroDeTokens;

public class Automata {
    private BibliotecaDeTokens bibliotecaDeTokens = new BibliotecaDeTokens();
    private Reportes reportes = new Reportes();

    public int ejecutorDeAutomataBuscadorDePalabras(char caracterInicial, String texto, int columna, int linea) {

        if (esLetra(caracterInicial) || caracterInicial == '_') {

            StringBuilder palabraEncontrada = new StringBuilder();
            int contador = 0;

            while (contador < texto.length() && (esLetra(texto.charAt(columna)))) {

                palabraEncontrada.append(texto.charAt(columna));
                contador++;
                columna++;

            }

        }
        return columna;
    }

    public String ejecutorFlechaAutomata(char caracterInicial, String texto, int columna) {
        if (caracterInicial == '-') {
            if (columna + 1 < texto.length() && texto.charAt(columna + 1) == '>') {
                return "->";
            }
        }
        return null;
    }

    public int ejecutorDeAutomataBuscadorDeDirectivas(char caracterInicial, String texto, int columna, int linea) {

        if (esDirectiva(caracterInicial)) {
            StringBuilder palabraEncontrada = new StringBuilder();

            int contador = 0;

            while (contador < texto.length() && (esLetra(texto.charAt(contador)))) {

                palabraEncontrada.append(texto.charAt(contador));

                contador++;

                columna++;

            }
            if (palabraEncontrada != null && !palabraEncontrada.toString().isEmpty()) {

                if (bibliotecaDeTokens.existeEnLosTokens(texto)) {
                    String tipo = bibliotecaDeTokens.mapeadorDeTokens(palabraEncontrada.toString());
                    reportes.agregarReporteValido(new RegistroDeTokens(palabraEncontrada.toString(),
                            bibliotecaDeTokens.getDescripcion(palabraEncontrada.toString()), columna, linea, tipo));
                } else {
                    reportes.agregarReporteNoValido(
                            new ErrorLexico(palabraEncontrada.toString(), "Token no reconocido", linea, columna));
                }
            }
        }
        return columna;
    }

    public String ejecutorDeOperadoresLiterales(char caracterInicial, String texto, int columna) {
        if (caracterInicial == '+' || caracterInicial == '-' || caracterInicial == '*' || caracterInicial == '/'
                || caracterInicial == '%') {
            return String.valueOf(caracterInicial);
        }
        return null;
    }

    private boolean esDirectiva(char caracterInicial) {
        if (caracterInicial == '@') {
            return true;
        }
        return false;
    }

    private boolean esLetra(char c) {
        return Character.isLetter(c);
    }
}
