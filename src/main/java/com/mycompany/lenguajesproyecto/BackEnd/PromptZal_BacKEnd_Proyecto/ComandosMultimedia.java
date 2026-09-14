package com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto;

public class ComandosMultimedia {


    public int saltarEspacios(String linea, int columna) {
        while (columna < linea.length() && (linea.charAt(columna) == ' ' || linea.charAt(columna) == '\t'|| linea.charAt(columna) == '\n'|| linea.charAt(columna) == '\r')) {
            columna++;
        }
        return columna;
    }


    public String extraerSubcadena(String linea, int inicio, int fin) {
        if (linea == null || inicio < 0 || fin > linea.length() || inicio >= fin) {
            return "";
        }
        return linea.substring(inicio, fin);
    }


    public String extraerTextoLimpio(String linea, int inicio, int fin) {
        return extraerSubcadena(linea, inicio, fin).trim();
    }


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
