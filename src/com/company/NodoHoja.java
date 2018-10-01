package com.company;

/**
 * Clase que define a una hoja del árbol de Huffman, siendo una hoja el final de uno de los recorridos
 * en el árbol, definiendo así una codificación.
 * @author Horquin Enzo, Serrano Francisco
 */
public class NodoHoja extends ArbolHuffman {

    private final String simbolo;

    /**
     * Crea un nodo hoja a partir de las características requeridas por el árbol de Huffman.
     * @param frecuencia Probabilidad de ocurrencia del símbolo.
     * @param simbolo Cadena de texto que representa el símbolo de la hoja.
     */
    public NodoHoja(double frecuencia, String simbolo) {
        super(frecuencia);
        this.simbolo = simbolo;
    }

    /**
     * Obtiene el símbolo del nodo.
     * @return Símbolo contenido en el nodo.
     */
    public String getSimbolo() {
        return simbolo;
    }

    /**
     * Informa si el presente nodo es hoja del árbol.
     * El retorno es siempre verdadero, siempre que se trate de un NodoHoja. Esto se implementó de ésta
     * manera para evitar el posterior uso del 'instanceof', que es poco elegante.
     * @return Retorna siempre verdadero.
     */
    @Override
    public boolean esHoja() {
        return true;
    }

    /**
     * Método utilizado para imprimir el nodo por salida estándar.
     * @return String que representa el nodo.
     */
    @Override
    public String toString() {
        return "NodoHoja{" +
                "simbolo='" + simbolo + '\'' +
                '}';
    }
}
