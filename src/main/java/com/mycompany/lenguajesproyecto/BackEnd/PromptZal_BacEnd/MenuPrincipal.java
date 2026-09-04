package com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacEnd;

import java.io.IOException;
import java.util.Scanner;

import com.mycompany.lenguajesproyecto.FrontEnd.PromptZal_FrontEnd.MenuDeSelecion;

public class MenuPrincipal {
    private Scanner scanner = new Scanner(System.in);

    public void menuPrincipalInicio() {
        int contadorDeAarchivosAnalizados = 0;
        while (true) {
            try {
                String phat;
                MenuDeSelecion front = new MenuDeSelecion();
                AnailizadorDeTexto analizador = new AnailizadorDeTexto();
                front.menuPrincipal();
                phat = scanner.nextLine();
                boolean todoCorecto=analizador.lector(phat, contadorDeAarchivosAnalizados);
                if(todoCorecto){
                    contadorDeAarchivosAnalizados++;
                    front.finDelAnalisis();
                }else{
                    front.archivoNoValido();
                }
                if (!scanner.nextLine().equals("s")) {
                    break;
                }

            } catch (IOException e) {
                MenuDeSelecion front = new MenuDeSelecion();
                front.errorDePrograma();
            }
        }

    }
}
