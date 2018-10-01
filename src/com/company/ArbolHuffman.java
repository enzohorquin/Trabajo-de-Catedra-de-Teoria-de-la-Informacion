package com.company;

/**
 * Clase abstracta que sirve de base para extensión de los nodos que integrarán
 * el árbol de Huffman.
 * @author Horquin Enzo, Serrano Francisco
 */
public abstract class ArbolHuffman{

    private final double frecuencia;

    /**
     * Crea un nodo con la frecuencia deseada.
     * @param frecuencia valor decimal con la frecuencia que represente al nodo.
     */
    public ArbolHuffman(double frecuencia) {
        this.frecuencia = frecuencia;
    }

    /**
     * Retorna la frecuencia que representa al nodo.
     * @return Frecuencia del nodo.
     */
    public double getFrecuencia() {
        return frecuencia;
    }

    /**
     * Método utilizado para imprimir el nodo por salida estándar.
     * @return String que representa el nodo.
     */
    @Override
    public String toString() {
        return "ArbolHuffman{" +
                "frecuencia=" + frecuencia +
                '}';
    }

    /**
     * Método abstracto que permite saber si un nodo es hoja (o no) del árbol.
     * @return Booleano que indica si el nodo es hoja.
     */
    public abstract boolean esHoja();
}
