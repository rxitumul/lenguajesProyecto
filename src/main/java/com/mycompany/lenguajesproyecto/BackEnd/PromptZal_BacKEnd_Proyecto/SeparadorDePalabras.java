package com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class SeparadorDePalabras {

    private ComandosMultimedia comando = new ComandosMultimedia();
    private Automata automata = new Automata();

    public boolean inicio(String paht) {

        File file = new File(paht);
        String nombreDelArchivo = file.getName();

        if (!verificadorDeArchivoValido(nombreDelArchivo)) {
            return false;
        }

        try (BufferedReader lectorPrincipal = new BufferedReader(new FileReader(file))) {
            String lineaLeinda;
            int columnaActual = 0;
            while ((lineaLeinda = lectorPrincipal.readLine()) != null) {
                ArrayList<int[][]> posicionesDePalabras = separadorDePalabras(lineaLeinda);
                for (int[][] posicion : posicionesDePalabras) {
                    int inicio = posicion[0][0];
                    int fin = posicion[0][1];
                    String palabra = comando.extraerSubcadena(lineaLeinda, inicio, fin);
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
        return true;
    }

    private boolean verificadorDeArchivoValido(String nombreDelArchivo) {
        boolean punto = false;
        String estencionPZ = "";
        for (int i = 0; i < nombreDelArchivo.length(); i++) {
            if (nombreDelArchivo.charAt(i) == '.' || punto) {
                estencionPZ = estencionPZ + nombreDelArchivo.charAt(i);
                punto = true;
            }
        }
        return estencionPZ.equals(".pz");
    }

    private ArrayList<int[][]> separadorDePalabras(String linea) {

        ArrayList<int[][]> posicionesDePalabras = new ArrayList<>();

        int contadorInicio = 0;
        int contadorFinal = 0;

        for (int i = 0; i < linea.length(); i++) {
            if (linea.charAt(i) == ' ') {
                posicionesDePalabras.add(new int[][] { { contadorInicio, contadorFinal } });
                contadorInicio = contadorFinal + 1;
                contadorFinal = contadorInicio;
            }
            contadorFinal++;
        }

        return posicionesDePalabras;
    }
}
