package com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto;

public class Automata {

    private Reportes reportes;
    public String ejecutorDeAutomataBuscadorDePalabras(char caracterInicial, String texto, int columna) {

        if (esLetra(caracterInicial) || caracterInicial == '_') {
            
            StringBuilder palabraEncontrada = new StringBuilder();
            int colInicio = columna;
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

    public void ejecutorFlechaAutomata(char caracterInicial, String texto, int columna) {
        if (caracterInicial == '-') {
            if (columna + 1 < texto.length() && texto.charAt(columna + 1) == '>') {
                
            }
        }
    }

    public String ejecutorDeAutomataBuscadorDeDirectivas(char caracterInicial, String texto, int columna) {

        if (esDirectiva(caracterInicial)) {
            StringBuilder palabraEncontrada = new StringBuilder();

            int colInicio = columna;
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

    public void ejecutorDeOperadoresLiterales(char caracterInicial, String texto, int columna) {
        if (caracterInicial == '+' || caracterInicial == '-' || caracterInicial == '*' || caracterInicial == '/' || caracterInicial == '%') {
            // Aquí puedes agregar la lógica para manejar los operadores literales
        }
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
