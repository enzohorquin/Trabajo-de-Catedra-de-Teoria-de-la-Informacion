package com.company;


import java.io.*;
import java.util.*;

/**
 * Define al árbol de Huffman que se utilizará para hacer las tareas tanto de codificación como de
 * decodificación, además de proveer la posibilidad de interacción con archivos binarios.
 * @author Horquin Enzo, Serrano Francisco
 */
public class CodificadorHuffmanNuevo {

    private static final int CANTIDAD_DIGITOS = 16;
    private static final int UNO = 1;

    private PriorityQueue<ArbolHuffman> arbol = new PriorityQueue<>(new Comparator<ArbolHuffman>() {
        @Override
        public int compare(ArbolHuffman o1, ArbolHuffman o2) {
            return o1.getFrecuencia() > o2.getFrecuencia() ? 1 : o1.getFrecuencia() == o2.getFrecuencia() ? 0 : -1;
        }
    });

    /**
     * Genera el árbol a partir de una lista (no necesariamente ordenada) de nodos.
     * @param nodos Lista de nodos sobre los que se construirá el codificador
     */
    public CodificadorHuffmanNuevo(List<NodoHoja> nodos)
    {
        arbol.addAll(nodos);
        reducirArbol();
    }

    /**
     * Genera el árbol a partir de un archivo con la probabilidad de ocurrencia para cada símbolo.
     * El formato del archivo debe ser "simbolo" : "probabilidad"
     * Teniendo dicho archivo, una entrada por línea.
     * @param frecuencias Archivo de texto que contiene la probabilidad de ocurrencia para cada símbolo.
     */
    public CodificadorHuffmanNuevo(File frecuencias)
    {
        Map<String, Double> mapa_frecs = Utils.generar_mapeo_simbolo_frecuencia(frecuencias);

        for (String simbolo : mapa_frecs.keySet())
            arbol.add(new NodoHoja(mapa_frecs.get(simbolo), simbolo));

        reducirArbol();
    }

    /**
     * Toma el árbol con todos los nodos hoja insertados, para así reducirlo a un solo nodo, de
     * frecuencia 1 (uno), sobre el cuál posteriormente se iniciará el recorrido.
     *
     * La dinámica consiste en generar un nuevo nodo a partir de la extracción de dos nodos, siendo
     * cada uno los de menor frecuencia. De esta manera termina quedando sólo un nodo al final de
     * la reducción.
     */
    private void reducirArbol()
    {
        while (arbol.size() > 1)
            arbol.add(new NodoInterno(arbol.poll(), arbol.poll()));
    }

    /**
     * Retorna el árbol, que siempre consistirá de un solo nodo interno, con sus respectivos nodos
     * hijos tanto a izquierda como a derecha.
     * @return Nodo padre del árbol generado.
     */
    public ArbolHuffman getArbol()
    {
        return arbol.peek();
    }

    /**
     * Método estático recursivo en el que el que se obtiene la codificación correspondiente a cada símbolo, en
     * forma de mapa.
     * @param arbol Arbol sobre el que se efectuará la lectura
     * @param str Variable auxiliar para obtener la codificación de un símbolo. Siempre debe ser un String vacío.
     * @param codificaciones Mapa sobre el cuál se depositarán los pares clave-valor. Debe estar previamente instanciado.
     */
    public static void getCodificacion(ArbolHuffman arbol, StringBuilder str, Map<String, String> codificaciones)
    {
        if (arbol.esHoja()) {
            NodoHoja hoja = (NodoHoja) arbol;

            codificaciones.put(hoja.getSimbolo(), str.toString());
        } else {

            NodoInterno interno = (NodoInterno) arbol;

            str.append('0');
            getCodificacion(interno.getNodo_izq(), str, codificaciones);
            str.deleteCharAt(str.length() - 1);

            str.append('1');
            getCodificacion(interno.getNodo_der(), str, codificaciones);
            str.deleteCharAt(str.length() - 1);
        }
    }

    /**
     * Método estático en el que dado un árbol, se obtiene el símbolo correspondiente a una cadena
     * de texto con 0s y 1s.
     * @param arbol Arbol sobre el cuál se extrae el símbolo.
     * @param codigo Cadena de texto a traducir con 0s y 1s.
     * @return Símbolo asociado a la cadena pasada por parámetro.
     */
    private static String getSimbolo(ArbolHuffman arbol, StringBuilder codigo)
    {
        if (arbol.esHoja()){
            if (codigo.length() == 0) {
                NodoHoja hoja = (NodoHoja) arbol;
                return hoja.getSimbolo();
            } else {
                return null;
            }
        } else {
            if (codigo.length() == 0)
                return null;
            NodoInterno interno = (NodoInterno) arbol;
            if (codigo.charAt(0) == '1')
                return getSimbolo(interno.getNodo_der(), codigo.deleteCharAt(0));
            if (codigo.charAt(0) == '0')
                return getSimbolo(interno.getNodo_izq(), codigo.deleteCharAt(0));
        }

        return null;
    }

    /**
     * Extensión del método getSimbolo() para varios símbolos. Se obtiene la traducción de una cadena de
     * texto con 0s y 1s a su correspondiente representación en símbolos, dado un árbol sobre el cual leer
     * dicha correspondencia.
     * @param arbol Arbol sobre el cuál se analiza la codificación en cuestión.
     * @param codificacion Cadena de texto a traducir, con 0s y 1s
     * @param ancho Número entero que indica el ancho de la imagen a descomprimir
     * @param alto Número entero que indica el alto de la imagen a comprimir
     * @return Lista de cadenas de texto que representa la decodificación.
     */
    public static List<String> getCadena(ArbolHuffman arbol, StringBuilder codificacion, int ancho, int alto)
    {
        List<String> aux = new ArrayList<>();
        int i = 0, j;

        while (i < codificacion.length() && aux.size() < ancho * alto) {
            String simbolo = null;
            j = i + 1;

            while (j <= codificacion.length() && (simbolo = getSimbolo(arbol, new StringBuilder(codificacion.substring(i, j)))) == null)
                j++;

            if (simbolo != null)
                aux.add(simbolo);
            else
                System.out.println("quedó null");

            i = j;
        }

        return aux;
    }

    /**
     * Método estático que se encarga de bajar una cadena de texto a un archivo binario, con el nombre deseado.
     * Además se guardan en el mismo binario, las dimensiones de la imagen.
     * @param cadena Cadena de texto a guardar en disco.
     * @param nombre_archivo Nombre deseado del archivo a generar.
     * @param ancho_img Ancho de la imagen.
     * @param alto_img Alto de la imagen.
     * @throws IOException En caso de que se produzcan errores de entrada/salida
     */
    public static void generarComprimido(String cadena, String nombre_archivo, int ancho_img, int alto_img) throws IOException
    {
        char buffer = 0;
        int cant_digitos = 0;
        int n = cadena.length();

        char[] dimensiones = {(char) ancho_img, (char) alto_img};

        FileOutputStream fos = new FileOutputStream(new File(nombre_archivo + ".huff"));

        for (int i = 0; i < dimensiones.length; i++) {
            byte lsb = (byte)(dimensiones[i] & 0x00FF);
            byte msb = (byte)((dimensiones[i] & 0xFF00) >> 8);

            byte[] arr_dim = {msb, lsb};

            fos.write(arr_dim);
        }

        while (n > 0){
            buffer = (char) (buffer << UNO);
            if (cadena.charAt(cadena.length() - n) == '1')
                buffer = (char) (buffer | UNO);
            cant_digitos++;
            if (cant_digitos == CANTIDAD_DIGITOS){

                // Como se guarda de a 8 bits, tengo que separar el buffer de 16 en dos partes de 8 bits
                byte lsb = (byte)(buffer & 0xFF); // Least Significant Bits
                byte msb = (byte)((buffer & 0xFF00) >> 8); // Most Significant Bits
                byte[] byte_buffer_arr = {msb, lsb};

                fos.write(byte_buffer_arr);

                buffer = 0;
                cant_digitos = 0;
            }
            n--;
        }
        if((cant_digitos < CANTIDAD_DIGITOS) && (cant_digitos != 0)){
            buffer = (char) (buffer << (CANTIDAD_DIGITOS - cant_digitos));

            // Como se guarda de a 8 bits, tengo que separar el buffer en dos partes
            byte lsb = (byte)(buffer & 0xFF); // Least Significant Bits
            byte msb = (byte)((buffer & 0xFF00) >> 8); // Most Significant Bits
            byte[] byte_buffer_arr = {msb, lsb};

            fos.write(byte_buffer_arr);
        }

        fos.close();
    }

    /**
     * Método estático que se encarga de levantar un archivo binario y transformarlo a
     * cadena de texto.
     * @param archivo Archivo binario a leer.
     * @return Lista de tres elementos, en el siguiente orden: ANCHO, ALTO, COD_HUFFMAN
     * @throws IOException En caso de que se produzcan errores de entrada/salida.
     */
    public static List<String> leerArchivo(File archivo) throws IOException
    {
        List<String> lista_retornar = new ArrayList<>();
        FileInputStream fis = new FileInputStream(archivo);

        for (int i = 0; i < 2; i++) {
            StringBuilder str = new StringBuilder();
            for (int j = 0; j < 2; j++) {
                int content = fis.read();

                for (int k = 7; k >= 0; k--) {
                    if (((content >> k) & 0x1) == 1)
                        str.append('1');
                    else
                        str.append('0');
                }

            }

            lista_retornar.add(String.valueOf(Integer.parseInt(str.toString(), 2)));
        }

        ArrayList<Integer> byteArrayList = new ArrayList<>();

        int content;
        while((content = fis.read()) != -1)
            byteArrayList.add(content);

        fis.close();

        StringBuilder stringBuilder = new StringBuilder();
        for (Integer bait : byteArrayList) {
            int aux = bait;
            for (int j = 7; j >= 0; j--) {
                if (((aux >> j) & 0x1) == 1)
                    stringBuilder.append('1');
                else
                    stringBuilder.append('0');
            }
        }

        lista_retornar.add(stringBuilder.toString());

//        // Para debuggear
//        PrintWriter pw = new PrintWriter(new FileWriter(new File("cadena_decodificacion.txt")));
//        pw.print(stringBuilder.toString());
//        pw.close();

        return lista_retornar;
    }

    /**
     * Método auxiliar para imprimir el árbol por salida estándar. Utilizado para hacer debug.
     * @param tree Arbol a leer
     */
    public static void printArbol(ArbolHuffman tree)
    {
        if (tree.esHoja()) {
            System.out.println(tree);
            return;
        }
        else {
            NodoInterno aux = (NodoInterno) tree;

            printArbol(aux.getNodo_izq());
            printArbol(aux.getNodo_der());
        }
    }

}
