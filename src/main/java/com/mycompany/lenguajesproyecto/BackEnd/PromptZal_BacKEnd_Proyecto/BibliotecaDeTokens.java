package com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto;

public class BibliotecaDeTokens {

    private final String[] DIRECTIVAS = { "@modelo", "@rol", "@formato" };
    private final String[] PALABRAS_RESERVADAS = { "AGENTE", "contexto", "variable", "EJECUTAR", "EXPORTAR" };
    private final String[] COMANDOS_DE_IA = { "PREGUNTAR", "GENERAR", "RESUMIR", "ANALIZAR", "TRADUCIR", "CLASIFICAR",
            "EXTRAER", "CARGAR" ,"CODIFICAR"};
    private final String[] CONECTORES = { "SOBRE", "DESDE", "EN", "COMO", "->" };
    private final String[] IDENTIFICADORES = { "_", "( ", ")" };
    private final String[] OPERADORES_LITERALES_COMENTARIOS = { "=", "+", "...", "analista", "//", "/* */"};

    public String[] getDIRECTIVAS() {
        return DIRECTIVAS;
    }

    public String[] getPALABRAS_RESERVADAS() {
        return PALABRAS_RESERVADAS;
    }

    public String[] getCOMANDOS_DE_IA() {
        return COMANDOS_DE_IA;
    }

    public String[] getCONECTORES() {
        return CONECTORES;
    }

    public String[] getIDENTIFICADORES() {
        return IDENTIFICADORES;
    }

    public String[] getOPERADORES_LITERALES_COMENTARIOS() {
        return OPERADORES_LITERALES_COMENTARIOS;
    }

}
