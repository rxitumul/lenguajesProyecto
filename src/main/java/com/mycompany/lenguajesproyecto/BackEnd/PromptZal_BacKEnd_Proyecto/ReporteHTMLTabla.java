package com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacEnd.ListaîlaYColas.Listas;
import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacEnd.TokensRegistrados.RegistroDeTokens;

public class ReporteHTMLTabla {

    private Listas<RegistroDeTokens> listaDeTokens = new Listas<>();

    // Método para agregar errores durante el análisis
    public void registroDeTokens(RegistroDeTokens registroDeTokens) {
        listaDeTokens.agregarAlFinal(registroDeTokens);
    }

    // Generar el archivo HTML al terminar la lectura del documento
    public void generarHTMLDeTokens(String rutaDestino) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n<html>\n<head>\n <meta charset=\"UTF-8\">\n");
        html.append("<title>Reporte de Tokens</title>\n");
        html.append(
                "<style>table, th, td { border: 1px solid black; border-collapse: collapse; padding: 8px; }</style>\n");
        html.append("</head>\n<body>\n");
        html.append("<h1>Reporte de Tokens</h1>\n");

        if (listaDeTokens.esVacia()) {
            html.append(
                    "<p style='color: green;'><strong>No se encontraron tokens durante el análisis.</strong></p>\n");
        } else {
            html.append("<table>\n");
            html.append(
                    "<tr><th>#</th><th>Lexema / Carácter</th><th>Token</th><th>Fila</th><th>Columna</th></tr>\n");
            int contador = 1;
            for (int i = 0; i < listaDeTokens.getCapacidad(); i++) {
                try {
                    RegistroDeTokens token = listaDeTokens.obtenerContenido(i);
                    html.append("<tr>")
                            .append("<td>").append(contador++).append("</td>")
                            .append("<td>").append(token.getLexema()).append("</td>")
                            .append("<td>").append(token.getToken()).append("</td>")
                            .append("<td>").append(token.getFila()).append("</td>")
                            .append("<td>").append(token.getColumna()).append("</td>")
                            .append("</tr>\n");
                } catch (Exception e) {
                    System.err.println("Error al leer token de la lista: " + e.getMessage());
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

    public void tablaDeTokensConsola() {
        System.out.println("Tabla de Tokens");
        System.out.println("Lexema\t\tDescripcion\tFila\tColumna");
        for (int i = 0; i < listaDeTokens.getCapacidad(); i++) {
            try {
                RegistroDeTokens token = listaDeTokens.obtenerContenido(i);
                System.out.println(token.getLexema() + "\t\t" + token.getDescripcion() + "\t" + token.getFila() + "\t"
                        + token.getColumna());
            } catch (Exception e) {
                System.err.println("Error al imprimir token: " + e.getMessage());
            }
        }
    }

}
