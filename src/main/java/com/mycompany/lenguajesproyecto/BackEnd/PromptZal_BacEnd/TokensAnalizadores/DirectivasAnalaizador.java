package com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacEnd.TokensAnalizadores;

import java.io.File;
import java.io.IOException;

public class DirectivasAnalaizador extends Analizador {

    @Override
    protected int condicion(int columna, int fila, String linea, File file) throws IOException {
        columna = comando.saltarEspacios(linea, columna);
        return lectorDeComillas(columna, fila, linea);
    }

    protected String[] token() {
        return tokens.getDIRECTIVAS();
    }
}
