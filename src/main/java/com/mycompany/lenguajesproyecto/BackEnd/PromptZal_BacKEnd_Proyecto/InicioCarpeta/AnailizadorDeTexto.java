package com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.InicioCarpeta;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import javax.swing.JTextArea;

import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.ComandosMultimedia;
import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.AutomataCarpeta.AutomataSegundaOpcion;
import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.BibliotecaCarpeta.BibliotecaDeDot;
import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.ReportesCarpeta.RegistroDeTokens;
import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.ReportesCarpeta.ReporteDeError;
import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.ReportesCarpeta.ReporteHTMLTabla;
import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.ReportesCarpeta.ListasEnlazadas.ErrorLexico;

public class AnailizadorDeTexto {
    private AutomataSegundaOpcion automatas;
    private ReporteDeError reportesError;
    private ReporteHTMLTabla reporteHTMLTabla;
    private ComandosMultimedia comando;

    public AnailizadorDeTexto() {
        comando = new ComandosMultimedia();
        reporteHTMLTabla = new ReporteHTMLTabla();
        automatas = new AutomataSegundaOpcion();
        reportesError = new ReporteDeError();
    }
    public BibliotecaDeDot getBibliotecaDot(){
        return automatas.getDotBiblioteca();
    }

    public AutomataSegundaOpcion getAutomatas() {
        return automatas;
    }

    public ReporteHTMLTabla getReporteHTMLTabla() {
        return reporteHTMLTabla;
    }

    public ReporteDeError getReportesError() {
        return reportesError;
    }

    public boolean lector(JTextArea areaAnalizar, int contadorDeAarchivosAnalizados) throws IOException {

        try (BufferedReader lectorPrincipal = new BufferedReader(new StringReader(areaAnalizar.getText()))) {
            // ── CLAVE: inyectar los reportes compartidos a todos los sub-autómatas ──
            // Así cada token/error detectado internamente llega directo a reporteHTMLTabla
            // y reportesError en lugar de quedarse atrapado en los objetos internos.
            automatas.setReportesCompartidos(reporteHTMLTabla, reportesError);

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
                        String tipo = automatas.getBibliotecaDeTokens().mapeadorDeTokens("//");
                        String desc = automatas.getBibliotecaDeTokens().getDescripcion("//");
                        reporteHTMLTabla.registroDeTokens(new RegistroDeTokens("//", tipo, desc, contadorDeFilas,
                                contadorDeColumnas, tipo));
                        break;
                    }

                    if (letra == '/' && contadorDeColumnas + 1 < lineaLeida.length()
                            && lineaLeida.charAt(contadorDeColumnas + 1) == '*') {
                        String tipo = automatas.getBibliotecaDeTokens().mapeadorDeTokens("/* */");
                        String desc = automatas.getBibliotecaDeTokens().getDescripcion("/* */");
                        reporteHTMLTabla.registroDeTokens(new RegistroDeTokens("/*..*/", tipo, desc,
                                contadorDeFilas, contadorDeColumnas, tipo));
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
                        String s = String.valueOf(letra);
                        String tipo = automatas.getBibliotecaDeTokens().mapeadorDeTokens(s);
                        String desc = automatas.getBibliotecaDeTokens().getDescripcion(s);
                        reporteHTMLTabla.registroDeTokens(new RegistroDeTokens(
                                s, tipo, desc, contadorDeFilas, contadorDeColumnas,
                                tipo));
                        contadorDeColumnas++;
                        continue;
                    }

                    int columnaAnterior = contadorDeColumnas;
                    contadorDeColumnas = automatas.ejecutorDeAutomataInicial(letra, lineaLeida, contadorDeColumnas,
                            contadorDeFilas);

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

}
