package com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto;

public class Automata {

    public String ejecutorDeAutomataBuscadorDePalabras(char caracterInicial, String texto, int columna) {

        if (esLetra(caracterInicial) || caracterInicial == '_') {
            
            StringBuilder palabraEncontrada = new StringBuilder();
            int contador = 0;

            while (contador < texto.length() && (esLetra(texto.charAt(contador)))) {

                palabraEncontrada.append(texto.charAt(contador));

                contador++;

                columna++;

            }
            if (contador >= texto.length()) {
                return palabraEncontrada.toString();
            }
        }
        return null;
    }

    public String ejecutorFlechaAutomata(char caracterInicial, String texto, int columna) {
        if (caracterInicial == '-') {
            if (columna + 1 < texto.length() && texto.charAt(columna + 1) == '>') {
                return "->";
            }
        }
        return null;
    }

    public String ejecutorDeAutomataBuscadorDeDirectivas(char caracterInicial, String texto, int columna) {

        if (esDirectiva(caracterInicial)) {
            StringBuilder palabraEncontrada = new StringBuilder();

            int contador = 0;

            while (contador < texto.length() && (esLetra(texto.charAt(contador)))) {

                palabraEncontrada.append(texto.charAt(contador));

                contador++;

                columna++;

            }
            return palabraEncontrada.toString();
        }
        return null;    
    }

    public String ejecutorDeOperadoresLiterales(char caracterInicial, String texto, int columna) {
        if (caracterInicial == '+' || caracterInicial == '-' || caracterInicial == '*' || caracterInicial == '/' || caracterInicial == '%') {
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
