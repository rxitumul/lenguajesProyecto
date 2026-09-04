package com.mycompany.lenguajesproyecto.FrontEnd.PromptZal_FrontEnd;

public class MenuDeSelecion {
    private Configuraciones confi = new Configuraciones();

    public void menuPrincipal() {
        confi.delayThread();
        confi.limpiadorDeLineas();
        confi.separadorDeLineas();
        System.out.println(confi.formatear("PromptZal"));
        confi.separadorDeLineas();
        System.out.println(confi.formatear("Porfavor de ingresar la ruta del archivo a analizar"));
        System.out.println(confi.formatear("Ejemplo: /Users/ricardocastillo/Documents/Prueba.pz"));
        confi.separadorDeLineas();
    }

    public void analizando() {
        confi.delayThread();
        confi.limpiadorDeLineas();
        confi.separadorDeLineas();
        System.out.println(confi.formatear("analizando en archivo"));
        confi.separadorDeLineas();
    }

    public void finDelAnalisis() {
        System.out.println("fin del analisis corectamente ");
        System.out.println("Desea continuar con otro archivo escriba 's' sino presione enter");
    }

    public void archivoNoValido(){
        System.out.println("archivo no valido porfavor ingrese un archivo .pz ");
        System.out.println("Desea continuar con otro archivo escriba 's' sino presione enter");
    }

    public void errorDePrograma() {
        System.out.println("No se pudo encontrar el archivo vuelva a intentar ");
    }

}
