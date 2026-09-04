package com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacEnd.TokensAnalizadores;

public class ComandosMultimedia {

    // Recorre Caracter por Caracter saltando los espacios en blanco
    public int saltarEspacios(String linea, int columna) {
        while (columna < linea.length() && (linea.charAt(columna) == ' ' || linea.charAt(columna) == '\t')) {
            columna++;
        }
        return columna;
    }

    // Extrae una subcadena de forma segura
    public String extraerSubcadena(String linea, int inicio, int fin) {
        if (linea == null || inicio < 0 || fin > linea.length() || inicio >= fin) {
            return "";
        }
        return linea.substring(inicio, fin);
    }

    // Extrae y elimina espacios al inicio y final (trim)
    public String extraerTextoLimpio(String linea, int inicio, int fin) {
        return extraerSubcadena(linea, inicio, fin).trim();
    }

    // Lee un identificador, número o comando Caracter por Caracter
    public String leerPalabra(String linea, int columna) {
        int inicio = columna;
        while (columna < linea.length()) {
            char columnasLocales = linea.charAt(columna);
            if (Character.isLetterOrDigit(columnasLocales) || columnasLocales == '_' || columnasLocales == '-') {
                columna++;
            } else {
                break;
            }
        }
        return extraerSubcadena(linea, inicio, columna);
    }

    // Lee una directiva que inicia con @ o el conector -> Caracter por Caracter
    public String leerDirectivaOConector(String linea, int columna) {
        int inicio = columna;
        if (columna < linea.length() && linea.charAt(columna) == '@') {
            columna++;
            while (columna < linea.length()
                    && (Character.isLetterOrDigit(linea.charAt(columna)) || linea.charAt(columna) == '_')) {
                columna++;
            }
        }
        return extraerSubcadena(linea, inicio, columna);
    }

    // Lee el siguiente token
    public String leerSiguienteToken(String linea, int columna) {
        if (columna >= linea.length()) {
            return "";
        }
        char c = linea.charAt(columna);
        if (c == '@') {
            return leerDirectivaOConector(linea, columna);
        } else if (c == '-' && columna + 1 < linea.length() && linea.charAt(columna + 1) == '>') {
            return "->";
        }
        return leerPalabra(linea, columna);
    }

}
