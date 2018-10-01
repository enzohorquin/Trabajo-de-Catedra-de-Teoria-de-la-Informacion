package com.company;

/**
 * Clase que define a un nodo interno en un árbol de Huffman, es decir un nodo que no
 * sea hoja.
 * @author Horquin Enzo, Serrano Francisco
 */
public class NodoInterno extends ArbolHuffman {

    private final ArbolHuffman nodo_izq, nodo_der;

    /**
     * Genera un nodo interno a partir de sus hijos.
     * La frecuencia del nodo será el resultado de la suma de las frecuencias de sus nodos
     * hijos.
     * @param nodo_izq Nodo hijo izquierdo.
     * @param nodo_der Nodo hijo derecho.
     */
    public NodoInterno(ArbolHuffman nodo_izq, ArbolHuffman nodo_der) {
        super(nodo_izq.getFrecuencia() + nodo_der.getFrecuencia());
        this.nodo_izq = nodo_izq;
        this.nodo_der = nodo_der;
    }

    /**
     * Retorna el nodo izquierdo.
     * @return Nodo izquierdo.
     */
    public ArbolHuffman getNodo_izq() {
        return nodo_izq;
    }

    /**
     * Retorna el nodo derecho.
     * @return Nodo derecho.
     */
    public ArbolHuffman getNodo_der() {
        return nodo_der;
    }

    /**
     * Informa si el presente nodo es interno al árbol.
     * El retorno es siempre false, siempre que se trate de un NodoInterno. Esto se implementó de ésta
     * manera para evitar el posterior uso del 'instanceof', que es poco elegante.
     * @return Retorna siempre falso.
     */
    @Override
    public boolean esHoja() {
        return false;
    }

    /**
     * Método utilizado para imprimir el nodo por salida estándar.
     * @return String que representa el nodo.
     */
    @Override
    public String toString() {
        return "NodoInterno{" +
                "nodo_izq=" + nodo_izq +
                ", nodo_der=" + nodo_der +
                '}';
    }
}
