package com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto;

import java.util.HashMap;
import java.util.Map;

public class BibliotecaDeTokens {

    private Map<String, TokensDefinicion> reservadas = new HashMap<>();
    {
        reservadas.put("@modelo", new TokensDefinicion("DIRECTIVA", "Directiva para definir el modelo"));
        reservadas.put("@rol", new TokensDefinicion("DIRECTIVA", "Directiva para definir el rol"));
        reservadas.put("@formato", new TokensDefinicion("DIRECTIVA", "Directiva para definir el formato"));
        reservadas.put("AGENTE", new TokensDefinicion("PALABRA_RESERVADA", "Palabra reservada para definir un agente") );
        reservadas.put("EJECUTAR", new TokensDefinicion("PALABRA_RESERVADA", "Palabra reservada para ejecutar una acción"));
        reservadas.put("contexto", new TokensDefinicion("PALABRA_RESERVADA", "Palabra reservada para definir el contexto"));
        reservadas.put("variable", new TokensDefinicion("PALABRA_RESERVADA", "Palabra reservada para definir una variable"));
        reservadas.put("EXPORTAR", new TokensDefinicion("PALABRA_RESERVADA", "Palabra reservada para exportar datos"));
        reservadas.put("PREGUNTAR", new TokensDefinicion("COMANDO_IA", "Comando de IA para hacer preguntas"));
        reservadas.put("GENERAR", new TokensDefinicion("COMANDO_IA", "Comando de IA para generar contenido"));
        reservadas.put("RESUMIR", new TokensDefinicion("COMANDO_IA", "Comando de IA para resumir información"));
        reservadas.put("ANALIZAR", new TokensDefinicion("COMANDO_IA", "Comando de IA para analizar datos"));
        reservadas.put("TRADUCIR", new TokensDefinicion("COMANDO_IA", "Comando de IA para traducir texto"));
        reservadas.put("CLASIFICAR", new TokensDefinicion("COMANDO_IA", "Comando de IA para clasificar información"));
        reservadas.put("EXTRAER", new TokensDefinicion("COMANDO_IA", "Comando de IA para extraer información"));
        reservadas.put("CARGAR", new TokensDefinicion("COMANDO_IA", "Comando de IA para cargar datos"));
        reservadas.put("CODIFICAR", new TokensDefinicion("COMANDO_IA", "Comando de IA para codificar información"));
        reservadas.put("SOBRE", new TokensDefinicion("CONECTORES", "Conector para definir sobre qué se aplica una acción"));
        reservadas.put("DESDE", new TokensDefinicion("CONECTORES", "Conector para definir desde dónde se aplica una acción"));
        reservadas.put("EN", new TokensDefinicion("CONECTORES", "Conector para definir en dónde se aplica una acción"));
        reservadas.put("COMO", new TokensDefinicion("CONECTORES", "Conector para definir cómo se aplica una acción"));
        reservadas.put("->", new TokensDefinicion("CONECTORES", "Conector para definir la relación entre elementos"));
        reservadas.put("=", new TokensDefinicion("OPERADORES_LITERALES_COMENTARIOS", "Operador de asignación"));
        reservadas.put("+", new TokensDefinicion("OPERADORES_LITERALES_COMENTARIOS", "Operador de suma"));
        reservadas.put("...", new TokensDefinicion("OPERADORES_LITERALES_COMENTARIOS", "Operador de tres puntos"));
        reservadas.put("analista", new TokensDefinicion("OPERADORES_LITERALES_COMENTARIOS", "Palabra reservada para definir un analista"));
        reservadas.put("//", new TokensDefinicion("OPERADORES_LITERALES_COMENTARIOS", "Comentario de una línea"));
        reservadas.put("/* */", new TokensDefinicion("OPERADORES_LITERALES_COMENTARIOS", "Comentario de múltiples líneas"));
    }

    public String mapeadorDeTokens(String token) {
        return reservadas.getOrDefault(token, new TokensDefinicion("DESCONOCIDO", "Token no reconocido")).getTipo();
    }

    public boolean existeEnLosTokens(String palabra) {
        return reservadas.containsKey(palabra);
    }

    public String getDescripcion(String token) {
        TokensDefinicion definicion = reservadas.get(token);
        if (definicion != null) {
            return definicion.getDescripcion();
        }
        return "Token no reconocido";
    }

}
