package com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacEnd.Errores.ErrorLexico;
import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacEnd.ListaîlaYColas.Listas;

public class ReporteDeError {

    private Listas<ErrorLexico> listaDeErrores = new Listas<>();

    // Método para agregar errores durante el análisis
    public void registrarError(ErrorLexico errorLexico) {
        listaDeErrores.agregarAlFinal(errorLexico);
    }

    // Generar el archivo HTML al terminar la lectura del documento
    public void generarHTMLDeError(String rutaDestino) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n<html>\n<head>\n <meta charset=\"UTF-8\">\n");
        html.append("<title>Reporte de Errores Léxicos</title>\n");
        html.append(
                "<style>table, th, td { border: 1px solid black; border-collapse: collapse; padding: 8px; }</style>\n");
        html.append("</head>\n<body>\n");
        html.append("<h1>Reporte de Errores Léxicos</h1>\n");

        if (listaDeErrores.esVacia()) {
            html.append(
                    "<p style='color: green;'><strong>No se encontraron errores léxicos durante el análisis.</strong></p>\n");
        } else {
            html.append("<table>\n");
            html.append(
                    "<tr><th>#</th><th>Lexema / Carácter</th><th>Descripción del Error</th><th>Fila</th><th>Columna</th></tr>\n");
            int contador = 1;
            for (int i = 0; i < listaDeErrores.getCapacidad(); i++) {
                try {
                    ErrorLexico err = listaDeErrores.obtenerContenido(i);
                    html.append("<tr>")
                            .append("<td>").append(contador++).append("</td>")
                            .append("<td>").append(err.getLexema()).append("</td>")
                            .append("<td>").append(err.getDescripcion()).append("</td>")
                            .append("<td>").append(err.getFila()).append("</td>")
                            .append("<td>").append(err.getColumna()).append("</td>")
                            .append("</tr>\n");
                } catch (Exception e) {
                    System.err.println("Error al leer elemento de la lista: " + e.getMessage());
                }
            }
            html.append("</table>\n");
        }

        html.append("</body>\n</html>");

        File destFile = new File(rutaDestino);
        if (destFile.getParentFile() != null) {
            destFile.getParentFile().mkdirs();
        }

        try (FileWriter writer = new FileWriter(destFile)) {
            writer.write(html.toString());
        } catch (IOException e) {
            System.err.println("Error al escribir el reporte HTML: " + e.getMessage());
        }
    }

}
