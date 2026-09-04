package com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacEnd.TokensAnalizadores;

import java.io.File;
import java.io.IOException;

import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacEnd.Errores.ErrorLexico;

public class ComandosDeIaAnalizador extends Analizador {

    @Override
    protected int condicion(int contadorDeColumnas, int contadorDeFilas, String lineaLeida, File archivo)
            throws IOException {
        contadorDeColumnas = comando.saltarEspacios(lineaLeida, contadorDeColumnas);

        int posicionAntes = contadorDeColumnas;

        // Consumimos la expresión completa que sigue al comando
        contadorDeColumnas = movedorDeColumnasHastaFinDeEsprecion(lineaLeida, contadorDeColumnas, contadorDeFilas);

        if (contadorDeColumnas == posicionAntes) {
            error = new ErrorLexico("Fin de linea", "Se esperaba un argumento despues del comando de IA revisa",
                    contadorDeFilas, contadorDeColumnas);
            reportesError.registrarError(error);
        }

        return contadorDeColumnas;
    }

    @Override
    protected String[] token() {
        return tokens.getCOMANDOS_DE_IA();
    }
}
