package com.mycompany.lenguajesproyecto.FrontEnd.PromptZal_FrontEnd;

public class Configuraciones {

    private final int TIEMPO = 1100;
    private final static int ANCHO = 92;
    private final static String LIMPIADOR_DE_PANTALLA = "\033[H\033[2J";
    private final static String SEPARADOR_DE_PANTALLA = "|" + "-".repeat(ANCHO - 2) + "|";

    public void separadorDeLineas() {
        System.out.println(SEPARADOR_DE_PANTALLA);
    }

    public void limpiadorDeLineas() {
        System.out.print(LIMPIADOR_DE_PANTALLA);
    }

    public String formatear(String texto) {
        int interior = ANCHO - 2;

        if (texto.length() > interior) {
            texto = texto.substring(0, interior);
        }

        return "|" + String.format("%-" + interior + "s", texto) + "|";
    }

    public void delayThread() {
        try {
            Thread.sleep(TIEMPO);
        } catch (InterruptedException ex) {
        }

        System.out.flush();
    }

    public void pantallaDeError() {
        delayThread();
        System.out.print("\033[38;5;208m");
        limpiadorDeLineas();
        separadorDeLineas();
        System.out.printf("%-10s%s%9s%n%s%s%s%n", "| ", "Error: Opción inválida ", " |", "|",
                "Por favor, seleccione una opción válida ", "|");
        separadorDeLineas();
        System.out.print("\033[0m");
    }
}
