package com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacEnd.TokensAnalizadores;

import java.io.File;
import java.io.IOException;
import com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacEnd.Errores.ErrorLexico;

public class ConectoresAnalizador extends Analizador {

    @Override
    protected int condicion(int columna, int fila, String linea, File file) throws IOException {
        columna = comando.saltarEspacios(linea, columna);
        int inicio = columna;
        columna = movedorDeColumnasHastaFinDeEsprecion(linea, columna, fila);
        if (columna == inicio) {
            error = new ErrorLexico("Fin de linea", "Se esperaba un complemento después del conector revisa", fila,
                    columna);
            reportesError.registrarError(error);
            return columna;
        }

        // Si el conector es "->", registramos la variable asignada
        if (contadorDeIndices == 4) {
            String varName = comando.extraerTextoLimpio(linea, inicio, columna);
            // verificar nombre de variable
            if (!varName.isEmpty() && Character.isLetter(varName.charAt(0))) {
                // verificar que exista la variable
                variablesDeclaradas.agregarAlFinal(varName);
            }
        }

        return columna;
    }

    protected String[] token() {
        return tokens.getCONECTORES();
    }
}
