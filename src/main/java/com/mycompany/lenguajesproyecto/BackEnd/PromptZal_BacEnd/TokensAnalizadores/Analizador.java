package com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacEnd.TokensAnalizadores;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacEnd.BibliotecaDeTokens;
import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacEnd.ReporteDeError;
import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacEnd.ReporteHTMLTabla;
import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacEnd.Errores.ErrorLexico;
import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacEnd.TokensRegistrados.RegistroDeTokens;

import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacEnd.ListaîlaYColas.Listas;

public abstract class Analizador {
    protected BibliotecaDeTokens tokens = new BibliotecaDeTokens();
    protected ReporteDeError reportesError;
    protected ReporteHTMLTabla reporteHTMLTabla;
    protected ErrorLexico error;
    protected int contadorDeIndices = 0;
    protected String[] palabrasResultadoLocal;
    private File fileLocal;
    protected ComandosMultimedia comando = new ComandosMultimedia();

    // Listas estáticas compartidas para validación semántica usando la lista
    // doblemente enlazada propia
    protected static Listas<String> variablesDeclaradas = new Listas<>();
    protected static Listas<String> agentesDeclarados = new Listas<>();

    public static void limpiarDeclaraciones() {
        variablesDeclaradas.limpiar();
        agentesDeclarados.limpiar();
    }

    public int analizador(int columna, int fila, String linea, ReporteDeError reportesErrorEntrante,
            ReporteHTMLTabla reportesTabla, File fileEntrante, boolean verdadError) throws IOException {

        reporteHTMLTabla = reportesTabla;
        reportesError = reportesErrorEntrante;
        fileLocal = fileEntrante;
        columna = comando.saltarEspacios(linea, columna);
        if (columna >= linea.length()) {
            return columna;
        }
        String palabra = comando.leerSiguienteToken(linea, columna);

        columna = columna + palabra.length();
        buscarIndiceDeToken(palabra);
        return condicion(columna, fila, linea, fileLocal);
    }

    // pos leemos toda la exprecion hasta terminar jejeje
    protected int movedorDeColumnasHastaFinDeEsprecion(String linea, int columna, int fila) {
        columna = comando.saltarEspacios(linea, columna);

        if (columna >= linea.length()) {
            return columna;
        }

        char c = linea.charAt(columna);

        // lee la palabra que esta entre comillas
        if (c == '"') {
            columna = lectorDeComillas(columna, fila, linea);
        }
        // lee la palabra
        else if (Character.isLetterOrDigit(c) || c == '_') {

            String palabra = comando.leerPalabra(linea, columna);
            columna = columna + palabra.length();

            // Verifica si es una función / una funcion que lleva un dato entre parentesis
            int columnaTemp = comando.saltarEspacios(linea, columna);
            if (columnaTemp < linea.length() && linea.charAt(columnaTemp) == '(') {

                reporteHTMLTabla
                        .registroDeTokens(new RegistroDeTokens("CARGAR", "Reconocido", fila, columna, "Comandos IA"));
                columna = columnaTemp + 1;
                int parentesisAbiertos = 1;

                while (columna < linea.length() && parentesisAbiertos > 0) {

                    char ch = linea.charAt(columna);
                    if (ch == '(') {
                        parentesisAbiertos++;
                    } else if (ch == ')') {
                        parentesisAbiertos--;
                    }
                    columna++;
                }
            }
        } else {
            columna++;
        }
        // lee la siguiente palabra
        int columnaSig = comando.saltarEspacios(linea, columna);
        if (columnaSig < linea.length()) {
            char sigC = linea.charAt(columnaSig);
            if (Character.isLetterOrDigit(sigC) || sigC == '_') {
                String sigPalabra = comando.leerPalabra(linea, columnaSig);
                if (!esPalabraClave(sigPalabra, fila, columna)) {
                    columna = columnaSig;
                    columna = movedorDeColumnasHastaFinDeEsprecion(linea, columna, fila);
                }
            }
        }

        // verifica la concatenacion con '+'
        int columnaMas = comando.saltarEspacios(linea, columna);
        if (columnaMas < linea.length() && linea.charAt(columnaMas) == '+') {
            reporteHTMLTabla.registroDeTokens(new RegistroDeTokens("+", "Reconocido", fila, columna, "operadores"));
            columna = columnaMas + 1;
            columna = movedorDeColumnasHastaFinDeEsprecion(linea, columna, fila);
        }

        return columna;
    }

    // Verifica si una palabra es parte del vocabulario reservado
    protected boolean esPalabraClave(String token, int fila, int columna) {
        for (String directivaAnalizar : tokens.getDIRECTIVAS()) {
            if (directivaAnalizar.equals(token))
                reporteHTMLTabla
                        .registroDeTokens(new RegistroDeTokens(token, "Reconocido", fila, columna, "Directiva"));
            return true;
        }
        for (String palabraReservadaAnalizar : tokens.getPALABRAS_RESERVADAS()) {
            if (palabraReservadaAnalizar.equals(token))
                reporteHTMLTabla.registroDeTokens(
                        new RegistroDeTokens(token, "Reconocido", fila, columna, "Palabras Reservadas"));
            return true;
        }
        for (String comandoDeIaAnalizar : tokens.getCOMANDOS_DE_IA()) {
            if (comandoDeIaAnalizar.equals(token))
                reporteHTMLTabla
                        .registroDeTokens(new RegistroDeTokens(token, "Reconocido", fila, columna, "Comandos De IA"));
            return true;
        }
        for (String conectorAnalizar : tokens.getCONECTORES()) {
            if (conectorAnalizar.equals(token))
                reporteHTMLTabla.registroDeTokens(new RegistroDeTokens(token, "Reconocido", fila, columna, "Conector"));
            return true;
        }
        return false;
    }

    // Busca de forma manual en el archivo la llave de cierre '}'
    protected int verificadorDeCierre(int columna, int fila, char analizarCierreDe) throws IOException {
        int filaArchivo = 0;
        int columnaArchivo = 0;
        columna++;
        try (BufferedReader lector = new BufferedReader(new FileReader(fileLocal))) {
            while (true) {
                if (filaArchivo != fila) {
                    lector.readLine();
                    filaArchivo++;
                } else {
                    break;
                }
            }
            String lineaLeida;
            while ((lineaLeida = lector.readLine()) != null) {
                columnaArchivo = 0;
                while (columnaArchivo < lineaLeida.length()) {
                    if (lineaLeida.charAt(columnaArchivo) == analizarCierreDe) {
                        return columna;
                    }
                    columnaArchivo++;
                }
            }
            reportesError.registrarError(
                    new ErrorLexico("Fin de linea",
                            "Se esperaba " + analizarCierreDe + " después de abrir un bloque revisa",
                            fila, columna));
            return columna;
        }
    }

    protected int lectorDeComillas(int columna, int fila, String linea) {
        String texto = leerTextoEntreComillas(linea, columna);
        // Si encontró el texto con sus dos comillas ("...")
        if (!texto.isEmpty() && texto.startsWith("\"") && texto.endsWith("\"") && texto.length() >= 2) {
            reporteHTMLTabla.registroDeTokens(
                    new RegistroDeTokens("\"...\"", "Reconocido", fila, columna, "Literales"));
            return columna + texto.length();
        } else {
            if (texto.isEmpty()) {
                error = new ErrorLexico("\"", "Falta el cierre de comillas en la directiva revisa", fila, columna);
            } else {
                error = new ErrorLexico(texto, "Falta el cierre de comillas en la directiva revisa", fila, columna);
            }
            reportesError.registrarError(error);
            return columna + texto.length();
        }
    }

    private String leerTextoEntreComillas(String linea, int columna) {
        if (columna < linea.length() && linea.charAt(columna) == '"') {
            int inicio = columna;
            columna++;
            while (columna < linea.length() && linea.charAt(columna) != '"') {
                columna++;
            }
            if (columna < linea.length()) {
                columna++; // Incluir la comilla de cierre
            }
            return comando.extraerSubcadena(linea, inicio, columna);
        }
        return "";
    }

    private void buscarIndiceDeToken(String palabra) {
        contadorDeIndices = 0;
        String[] palabrasDirectivas = token();
        for (String d : palabrasDirectivas) {
            if (d.equals(palabra)) {
                return;
            }
            contadorDeIndices++;
        }
    }

    protected abstract String[] token();

    protected abstract int condicion(int columna, int fila, String linea, File file) throws IOException;
}
