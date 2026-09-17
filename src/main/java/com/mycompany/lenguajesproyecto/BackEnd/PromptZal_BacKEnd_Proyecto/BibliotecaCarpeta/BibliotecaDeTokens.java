package com.mycompany.lenguajesproyecto.BackEnd.PromptZal_BacKEnd_Proyecto.BibliotecaCarpeta;

import java.util.HashMap;
import java.util.Map;

public class BibliotecaDeTokens {

        private Map<String, TokensDefinicion> reservadas = new HashMap<>();
        {
                reservadas.put("@modelo", new TokensDefinicion("DIRECTIVA",
                                "Indica la herramienta o modelo de IA para el que se redacta el prompt (por ejemplo, claude-sonnet-4-6)."));
                reservadas.put("@rol", new TokensDefinicion("DIRECTIVA",
                                "Define el rol general que se le pedirá asumir a la IA en el encabezado del  prompt."));
                reservadas.put("@formato", new TokensDefinicion("DIRECTIVA",
                                "Indica el formato en que se desea la respuesta (por ejemplo, markdown, texto  plano o lista)."));
                reservadas.put("AGENTE", new TokensDefinicion("PALABRA_RESERVADA",
                                "Declara un agente: la unidad que agrupa un contexto, variables y comandos."));
                reservadas.put("contexto", new TokensDefinicion("PALABRA_RESERVADA",
                                "Define el rol que asumirá la IA dentro del agente."));
                reservadas.put("variable", new TokensDefinicion("PALABRA_RESERVADA",
                                "Declara una variable que almacena un valor o el resultado de un comando."));
                reservadas.put("EJECUTAR",
                                new TokensDefinicion("PALABRA_RESERVADA", "Corre un agente previamente definido."));
                reservadas.put("EXPORTAR", new TokensDefinicion("PALABRA_RESERVADA",
                                "Indica qué resultados se incluyen en el prompt final."));

                reservadas.put("PREGUNTAR", new TokensDefinicion("COMANDO_IA",
                                "Formula una pregunta a la IA sobre unos datos o un contexto."));
                reservadas.put("GENERAR", new TokensDefinicion("COMANDO_IA",
                                "Solicita a la IA que genere contenido nuevo (código, texto, ideas)."));
                reservadas.put("RESUMIR", new TokensDefinicion("COMANDO_IA",
                                "Pide un resumen de un contenido, opcionalmente con un límite de palabras."));
                reservadas.put("ANALIZAR", new TokensDefinicion("COMANDO_IA",
                                "Solicita un análisis de un dato bajo cierto criterio."));
                reservadas.put("TRADUCIR", new TokensDefinicion("COMANDO_IA", "Traduce un texto a otro idioma."));
                reservadas.put("CLASIFICAR", new TokensDefinicion("COMANDO_IA",
                                "Clasifica un contenido dentro de un conjunto de categorías."));
                reservadas.put("EXTRAER",
                                new TokensDefinicion("COMANDO_IA", "Extrae información específica de un texto."));

                reservadas.put("CARGAR",
                                new TokensDefinicion("FUNCION_DE_SISTEMA",
                                                "Carga el contenido de un archivo y lo entrega como valor. La ejecuta el  programa, no la IA."));
                reservadas.put("CODIFICAR",
                                new TokensDefinicion("COMANDO_IA", "Comando de IA para codificar información"));
                reservadas.put("SOBRE",
                                new TokensDefinicion("CONECTORES",
                                                "Indica los datos sobre los que opera un comando."));
                reservadas.put("DESDE",
                                new TokensDefinicion("CONECTORES",
                                                "Indica la especificación o fuente a partir de la cual se genera algo."));
                reservadas.put("EN", new TokensDefinicion("CONECTORES",
                                "Indica un parámetro del comando (un idioma, una cantidad de palabras)."));
                reservadas.put("COMO",
                                new TokensDefinicion("CONECTORES", "Indica el formato deseado para el resultado."));
                reservadas.put("->", new TokensDefinicion("CONECTORES",
                                "Indica la variable donde se almacena el resultado."));
                reservadas.put(",", new TokensDefinicion("CONECTORES", "Separador de elementos"));
                reservadas.put("(", new TokensDefinicion("CONECTORES", "Paréntesis de apertura"));
                reservadas.put(")", new TokensDefinicion("CONECTORES", "Paréntesis de cierre"));

                reservadas.put("{", new TokensDefinicion("OPERADORES_LITERALES_COMENTARIOS_IDENTIFICADORES",
                                "Llave de apertura de bloque"));
                reservadas.put("}",
                                new TokensDefinicion("OPERADORES_LITERALES_COMENTARIOS_IDENTIFICADORES",
                                                "Llave de cierre de bloque"));
                reservadas.put("=", new TokensDefinicion("OPERADORES_LITERALES_COMENTARIOS_IDENTIFICADORES",
                                "Operador de asignación: guarda un valor en una variable."));
                reservadas.put("+", new TokensDefinicion("OPERADORES_LITERALES_COMENTARIOS_IDENTIFICADORES",
                                "Operador de concatenación: une dos cadenas de texto."));
                reservadas.put("...",
                                new TokensDefinicion("OPERADORES_LITERALES_COMENTARIOS_IDENTIFICADORES",
                                                "Operador de tres puntos"));
                reservadas.put("analista",
                                new TokensDefinicion("OPERADORES_LITERALES_COMENTARIOS_IDENTIFICADORES",
                                                "Palabra reservada para definir un analista"));
                reservadas.put("//",
                                new TokensDefinicion("OPERADORES_LITERALES_COMENTARIOS_IDENTIFICADORES",
                                                "Comentario de una línea"));
                reservadas.put("/* */",
                                new TokensDefinicion("OPERADORES_LITERALES_COMENTARIOS_IDENTIFICADORES",
                                                "Comentario de múltiples líneas"));
        }

        public String mapeadorDeTokens(String token) {
                return reservadas.getOrDefault(token, new TokensDefinicion("DESCONOCIDO", "Token no reconocido"))
                                .getTipo();
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
