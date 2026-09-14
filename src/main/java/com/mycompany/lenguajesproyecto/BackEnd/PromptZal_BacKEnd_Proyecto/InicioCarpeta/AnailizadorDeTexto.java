package com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.InicioCarpeta;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.AutomataCarpeta.AutomataSegundaOpcion;
import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.ComandosMultimedia;
import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.ReportesCarpeta.ErrorLexico;
import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.ReportesCarpeta.RegistroDeTokens;
import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.ReportesCarpeta.ReporteDeError;
import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.ReportesCarpeta.ReporteHTMLTabla;

public class AnailizadorDeTexto {
    private AutomataSegundaOpcion automatas = new AutomataSegundaOpcion();
    private ReporteDeError reportesError = new ReporteDeError();
    private ReporteHTMLTabla reporteHTMLTabla = new ReporteHTMLTabla();
    private ComandosMultimedia comando = new ComandosMultimedia();

    public AutomataSegundaOpcion getAutomatas() {
        return automatas;
    }

    public ReporteHTMLTabla getReporteHTMLTabla() {
        return reporteHTMLTabla;
    }

    public ReporteDeError getReportesError() {
        return reportesError;
    }

    public boolean lector(String paht, int contadorDeAarchivosAnalizados) throws IOException {
        File file = new File(paht);
        String nombreDelArchivo = file.getName();

        if (!verificadorDeArchivoValido(nombreDelArchivo)) {
            return false;
        }

        try (BufferedReader lectorPrincipal = new BufferedReader(new FileReader(file))) {
            int contadorDeFilas = 1;
            String lineaLeida;
            boolean dentroDeComentarioBloque = false;

            while ((lineaLeida = lectorPrincipal.readLine()) != null) {
                int contadorDeColumnas = 0;

                while (contadorDeColumnas < lineaLeida.length()) {


                    if (dentroDeComentarioBloque) {
                        int posCierre = lineaLeida.indexOf("*/", contadorDeColumnas);
                        if (posCierre != -1) {
                            dentroDeComentarioBloque = false;
                            contadorDeColumnas = posCierre + 2;
                        } else {
                            break;
                        }
                    }

                    contadorDeColumnas = comando.saltarEspacios(lineaLeida, contadorDeColumnas);

                    if (contadorDeColumnas >= lineaLeida.length()) {
                        break;
                    }

                    char letra = lineaLeida.charAt(contadorDeColumnas);


                    if (letra == '/' && contadorDeColumnas + 1 < lineaLeida.length()
                            && lineaLeida.charAt(contadorDeColumnas + 1) == '/') {
                        reporteHTMLTabla.registroDeTokens(new RegistroDeTokens("//", "Reconocido", contadorDeFilas,
                                contadorDeColumnas, "Comentarios"));
                        break; 
                    }


                    if (letra == '/' && contadorDeColumnas + 1 < lineaLeida.length()
                            && lineaLeida.charAt(contadorDeColumnas + 1) == '*') {
                        reporteHTMLTabla.registroDeTokens(new RegistroDeTokens("/*..*/", "Reconocido",
                                contadorDeFilas, contadorDeColumnas, "Comentarios"));
                        int posCierre = lineaLeida.indexOf("*/", contadorDeColumnas + 2);
                        if (posCierre != -1) {
                            contadorDeColumnas = posCierre + 2;
                            continue;
                        } else {
                            dentroDeComentarioBloque = true;
                            break;
                        }
                    }


                    if (letra == '{' || letra == '}') {
                        reporteHTMLTabla.registroDeTokens(new RegistroDeTokens(
                                String.valueOf(letra), "Delimitador", contadorDeFilas, contadorDeColumnas, "DELIMITADOR"));
                        contadorDeColumnas++;
                        continue;
                    }


                    int columnaAnterior = contadorDeColumnas;
                    contadorDeColumnas = automatas.ejecutorDeAutomataInicial(letra, lineaLeida, contadorDeColumnas, contadorDeFilas);

                    if (contadorDeColumnas <= columnaAnterior) {
                        contadorDeColumnas++;
                    }
                }

                contadorDeFilas++;
            }

            if (dentroDeComentarioBloque) {
                reportesError.registrarError(new ErrorLexico("/*..*/",
                        "Se esperaba el cierre del bloque de comentarios '*/'", contadorDeFilas, 0));
            }

        } catch (Exception e) {
            reportesError.registrarError(new ErrorLexico("Lector",
                    "Error al leer el archivo: " + e.getMessage(), 0, 0));
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
}
