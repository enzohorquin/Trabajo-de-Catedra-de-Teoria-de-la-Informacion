package com.company;

/*
 *      TODO abstraer un poco el código porque hay mucha duplicación.
 *      TODO avisar Enzo para que de el OK en la descripción de los métodos del Run-Length.
 */

/**
 * Clase encargada de lanzar la ejecución de la aplicación.
 * @author Horquin Enzo, Serrano Francisco
 */
@SuppressWarnings("unused")
public class PracticoEspecial {

    /**
     * Inicio de la ejecución.
     * @param args Argumentos del programa al ejecutarse.
     */
    public static void main(String[] args)
    {
        // Lanza la ventana principal
        MainWindow window = new MainWindow(new ConcatenadorImagenes());

    }
}
