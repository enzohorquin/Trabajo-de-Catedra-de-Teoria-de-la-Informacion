package com.company;

import com.google.common.base.Splitter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.List;

/**
 * Clase que define varios métodos estáticos útiles para la resolución del enunciado.
 * @author Horquin Enzo, Serrano Francisco
 */
public class Utils {

    private static final int CANTIDAD_COLUMNAS = 50;

    /**
     * Método estático que imprime un arreglo por salida estándar, de forma legible.
     * Usado para debuggear.
     * @param array Arreglo a imprimir.
     */
    public static void printArray(int[] array) {
        for (int i = 0; i < array.length; i++)
            System.out.print(array[i] + " ");
    }

    /**
     * Método estático que imprime una matriz por salida estándar, de forma legible.
     * Usado para debuggear de forma más gráfica.
     * @param matrix Matriz 2D a imprimir.
     */
    public static void printMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    /**
     * Método estático que retorna la correlación entre dos cadenas de enteros.
     * @param cadena_1 Arreglo de enteros 1.
     * @param cadena_2 Arreglo de enteros 2.
     * @return Correlación entre las dos cadenas expresado con doble precisión.
     */
    public static double getCorrelacion(int[] cadena_1, int[] cadena_2) {
        double numerador = 0;
        double denominador = 0;
        double aux_1 = 0, aux_2 = 0;

        for (int i = 0; i < cadena_1.length; i++)
            numerador += cadena_1[i] * cadena_2[i];

        for (int i = 0; i < cadena_1.length; i++) {
            aux_1 += Math.pow(cadena_1[i], 2.0d);
            aux_2 += Math.pow(cadena_2[i], 2.0d);
        }

        denominador = Math.sqrt(aux_1 * aux_2);

        return numerador / denominador;
    }

    /**
     * Método estático que retorna la columna en donde arranca la máxima correlación entre dos matrices.
     * @param img1 Matriz que representa la fuente 1 al evaluar correlación.
     * @param img2 Matriz que representa la fuente 2 al evaluar correlación.
     * @param maximo_columnas_comparar Ancho máximo de las submatrices a comparar.
     * @param comparar_en_orden TRUE: compara img1 con img2; FALSE: compara img2 con img1.
     * @return La columna donde arranca la cadena con la máxima correlación.
     */
    public static int getColumnasCoincidentes(int[][] img1, int[][] img2, int maximo_columnas_comparar, boolean comparar_en_orden) {
        double max_corr = 0.d;
        int columna = 0;
        for (int i = maximo_columnas_comparar; i > 0; i--) {
            int[] submat_1 = Utils.getSubmatriz(img1, i, !comparar_en_orden);
            int[] submat_2 = Utils.getSubmatriz(img2, i, comparar_en_orden);
            double corr = Utils.getCorrelacion(submat_1, submat_2);

            if (corr > max_corr) {
                max_corr=corr;
                columna=i;

                if( corr == 1.0d)
                    return i;
            }
        }

        return columna;
    }

    /**
     * Método estático que extrae una submatriz, serializada en un arreglo unidimensional.
     * @param matriz Matriz de la cual extraer la submatriz.
     * @param cant_columnas Ancho máximo de la submatriz a extraer.
     * @param desde_principio TRUE: extrae de izquierda a derecha; FALSE: extrae de derecha a izquierda.
     * @return Submatriz aplanada en un arreglo unidimensional.
     */
    public static int[] getSubmatriz(int[][] matriz, int cant_columnas, boolean desde_principio) {
        int[] aux = new int[matriz.length * cant_columnas];

        int col_inicial = 0;
        int max_columnas = cant_columnas;
        if (!desde_principio) {
            col_inicial = matriz[0].length - cant_columnas;
            max_columnas = matriz[0].length;
        }

        int k = 0;
        for (int i = 0; i < matriz.length; i++) {
            for (int j = col_inicial; j < max_columnas; j++) {
                aux[k] = matriz[i][j];
                k++;
            }
        }

        return aux;
    }

    /**
     * Método estático que une tres matrices una al lado de la otra, en el orden en que son pasadas
     * por parámetro.
     * @param mat_1 Matriz fuente 1.
     * @param mat_2 Matriz fuente 2.
     * @param mat_3 Matriz fuente 3.
     * @return Matriz resultante de unir las tres pasadas por parámetro.
     */
    public static int[][] juntarMatrices(int[][] mat_1, int[][] mat_2, int[][] mat_3) {
        int[][] matriz = new int[mat_1.length][mat_1[0].length + mat_2[0].length + mat_3[0].length];

        int m = 0, n = 0;
        for (int i = 0; i < mat_1.length; i++) {
            for (int j = 0; j < mat_1[0].length; j++) {
                matriz[m][n] = mat_1[i][j];
                n++;
            }
            m++;
            n = 0;
        }

        m = 0;
        n = mat_1[0].length;

        for (int i = 0; i < mat_2.length; i++) {
            for (int j = 0; j < mat_2[0].length; j++) {
                matriz[m][n] = mat_2[i][j];
                n++;
            }
            m++;
            n = mat_1[0].length;
        }

        m = 0;
        n = mat_1[0].length + mat_2[0].length;

        for (int i = 0; i < mat_3.length; i++) {
            for (int j = 0; j < mat_3[0].length; j++) {
                matriz[m][n] = mat_3[i][j];
                n++;
            }
            m++;
            n = mat_1[0].length + mat_2[0].length;
        }

        return matriz;
    }

    /**
     * Método estático que "aplana" una matriz, es decir, expresándola en un arreglo unidimensional.
     * @param matriz Matriz 2D a redudir de dimensión.
     * @return Arreglo 1D, resultante de leer la matriz de izquierda a derecha, de arriba a abajo.
     */
    public static int[] serializarMatriz(int[][] matriz) {
        int[] aux = new int[matriz.length * matriz[0].length];
        int k = 0;

        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[0].length; j++) {
                aux[k] = matriz[i][j];
                k++;
            }
        }

        return aux;
    }

    /**
     * Método estático que genera una matriz 2D a partir de un arreglo 1D, dadas las dimensiones
     * deseadas de la matriz bidimensional.
     * @param arr Arreglo 1D a expandir.
     * @param ancho Ancho de la matriz 2D resultante.
     * @param alto Alto de la matriz 2D resultante.
     * @return Matriz 2D generada.
     */
    public static int[][] expandirArregloMatriz(int[] arr, int ancho, int alto) {
        int[][] matriz = new int[alto][ancho];
        int k = 0;
        for (int i = 0; i < alto; i++) {
            for (int j = 0; j < ancho; j++) {
                matriz[i][j] = arr[k];
                k++;
            }
        }

        return matriz;
    }

    /**
     * Método estático que se encarga de convertir un arreglo de valores RGB a su
     * correspondiente valor en escala de grises.
     * @param arreglo_rgb Arreglo de valores RGB.
     * @return Arreglo de valores en escala de grises.
     */
    public static int[] convertirArregloRGBaGrises(int[] arreglo_rgb)
    {
        int[] arr = new int[arreglo_rgb.length];

        for (int i = 0; i < arr.length; i++)
            arr[i] = new Color(arreglo_rgb[i]).getRed();

        return arr;
    }

    /**
     * Método estático que convierte una imagen a su correspondiente representación matricial, con la posibilidad
     * de expresarla en escala de grises.
     * @param img Imagen fuente a representar matricialmente.
     * @param convertir_a_grises TRUE: matriz expresada en escala de grises; FALSE: matriz expresada en valores RGB.
     * @return Matriz 2D que representa la imagen deseada.
     */
    public static int[][] generarMatrizRGB(BufferedImage img, boolean convertir_a_grises)
    {
        int[] pixeles = img.getRGB(0, 0, img.getWidth(), img.getHeight(), null, 0, img.getWidth());

        if (convertir_a_grises) {
            int[] pixeles_gris = new int[pixeles.length];
            for (int i = 0; i < pixeles.length; i++)
                pixeles_gris[i] = new Color(pixeles[i]).getRed();
            return Utils.expandirArregloMatriz(pixeles_gris, img.getWidth(), img.getHeight());
        }

        return Utils.expandirArregloMatriz(pixeles, img.getWidth(), img.getHeight());
    }

    /**
     * Método estático que devuelve el promedio de un arreglo de enteros.
     * @param arreglo Arreglo de enteros fuente.
     * @return Promedio del arreglo.
     */
    public static double getPromedio(int[] arreglo)
    {
        double suma = 0.d;

        for (int k = 0; k < arreglo.length; k++)
            suma+=arreglo[k];

        return suma / arreglo.length;
    }

    /**
     * Método estático que devuelve el desvío estándar de un arreglo de enteros.
     * @param arreglo Arreglo de enteros fuente.
     * @param h Mapa con las probabilidades de cada símbolo.
     * @return Desvió estándar del arreglo.
     */
    public static double getDesviacion(int[] arreglo, HashMap<Integer, Double> h)
    {
        double acum = 0.d;
        double n = arreglo.length - 1;
        double prom = getPromedio(arreglo);

        for (int i = 0; i < 256; i++)
          if(h.containsKey(i))
            acum += Math.pow(i - prom, 2) * h.get(i);

        return Math.sqrt(acum);
    }

    /**
     * Método estático que genera un mapa con la probabilidad de ocurrencia de cada símbolo en una
     * tira de símbolos, expresada en forma de arreglo.
     * @param arreglo Cadena de símbolos a analizar.
     * @return Mapeo (símbolo): (probabilidad).
     */
    public static HashMap<Integer,Double> getProbabilidades(int[] arreglo)
    {
        HashMap<Integer,Double> resultado = new HashMap<>();

        for (int k = 0; k < arreglo.length; k++){
            if (resultado.containsKey(arreglo[k]))
                resultado.put(arreglo[k], resultado.get(arreglo[k]) + 1);
            else
                resultado.put(arreglo[k], 1.0d);
        }

        for (Integer I: resultado.keySet())
            resultado.put(I, resultado.get(I) / arreglo.length);

        return resultado;
    }

    /**
     * Método estático que transforma una matriz 2D de valores RGB a su equivalente en escala de grises.
     * @param matriz_rgb Matriz 2D expresada en valores RGB.
     * @return Matriz 2D expresada en valores de escala de grises.
     */
    public static int[][] convertirMatrizRGBaGrises(int[][] matriz_rgb)
    {
        int[][] matriz_grises = new int[matriz_rgb.length][matriz_rgb[0].length];

        for (int i = 0; i < matriz_rgb.length; i++)
            for (int j = 0; j < matriz_rgb[0].length; j++)
                matriz_grises[i][j] = new Color(matriz_rgb[i][j]).getRed();

        return matriz_grises;
    }

    /**
     * Método estático que transforma una matriz 2D de valores en escala de grises a su equivalente
     * en valores RGB.
     * @param matriz_grises Matriz 2D expresada en valores en escala de grises.
     * @return Matriz 2D expresada en valores RGB.
     */
    public static int[][] convertirMatrizGrisesRGB(int[][] matriz_grises)
    {
        int[][] matriz_rgb = new int[matriz_grises.length][matriz_grises[0].length];

        for (int i = 0; i < matriz_grises.length; i++) {
            for (int j = 0; j < matriz_grises[0].length; j++) {
                matriz_rgb[i][j] = new Color(matriz_grises[i][j], matriz_grises[i][j], matriz_grises[i][j]).getRGB();
            }
        }

        return matriz_rgb;
    }

    public static void modificarLista(int matriz1[][], int matriz2[][], int matriz3[][], List<int [] []>  resultado){
        resultado.clear();
        resultado.add(matriz1);
        resultado.add(matriz2);
        resultado.add(matriz3);
    }

    /**
     * Método estático que se encarga de unir de la mejor manera tres matrices (expresadas en escalas de grises)
     * que representen una cadena de símbolos, en función al cálculo de la correlación.
     * @param matriz1 Matriz fuente 1.
     * @param matriz2 Matriz fuente 2.
     * @param matriz3 Matriz fuente 3.
     * @return Lista de matrices dispuestas en orden, de izquierda a derecha.
     */
    public static List<int[][]> unir(int[][] matriz1, int[][] matriz2, int[][] matriz3) {


        java.util.List<int[][]> resultado = new ArrayList<>();
        double max_corr = 0;

        // Secuencia 3 1 2
        double corrIzq1 = Utils.getMaxCorrelacion(matriz1, matriz2, CANTIDAD_COLUMNAS, true);
        double corrDer1 = Utils.getMaxCorrelacion(matriz3, matriz1, CANTIDAD_COLUMNAS, true);

        double suma1 = corrIzq1 + corrDer1;

        // Secuencia 2 1 3

        double corrIzq1b = Utils.getMaxCorrelacion(matriz1, matriz3, CANTIDAD_COLUMNAS, true);
        double corrDer1b = Utils.getMaxCorrelacion(matriz2, matriz1, CANTIDAD_COLUMNAS, true);

        double suma1b = corrIzq1b + corrDer1b;

        if (suma1b >= suma1) {
            max_corr = suma1b;
            modificarLista(matriz3, matriz1, matriz2, resultado);


        } else {
            max_corr = suma1;
            modificarLista(matriz2, matriz1, matriz3, resultado);

        }

        // Secuencia 1 2 3
        double corrIzq2 = Utils.getMaxCorrelacion(matriz2, matriz3, CANTIDAD_COLUMNAS, true);
        double corrDer2 = Utils.getMaxCorrelacion(matriz1, matriz2, CANTIDAD_COLUMNAS, true);

        double suma2 = corrIzq2 + corrDer2;

        if (suma2 >= max_corr) {
            max_corr = suma2;
            modificarLista(matriz3, matriz2, matriz1, resultado);

        }
        // Secuencia 3 2 1

        double corrIzq2b = Utils.getMaxCorrelacion(matriz2, matriz1, CANTIDAD_COLUMNAS, true);
        double corrDer2b = Utils.getMaxCorrelacion(matriz3, matriz2, CANTIDAD_COLUMNAS, true);

        double suma2b = corrIzq2b + corrDer2b;

        if (suma2b >= max_corr) {
            max_corr = suma2b;
            modificarLista(matriz1, matriz2, matriz3, resultado);

        }
        // Secuencia 1 3 2
        double corrIzq3 = Utils.getMaxCorrelacion(matriz3, matriz2, CANTIDAD_COLUMNAS, true);
        double corrDer3 = Utils.getMaxCorrelacion(matriz1, matriz3, CANTIDAD_COLUMNAS, true);

        double suma3 = corrIzq3 + corrDer3;

        if (suma3 >= max_corr) {
            max_corr = suma3;
            modificarLista(matriz2, matriz3, matriz1, resultado);
        }

        // Secuencia 2 3 1
        double corrIzq3b = Utils.getMaxCorrelacion(matriz3, matriz1, CANTIDAD_COLUMNAS, true);
        double corrDer3b = Utils.getMaxCorrelacion(matriz2, matriz3, CANTIDAD_COLUMNAS, true);

        double suma3b = corrIzq3b + corrDer3b;

        if (suma3b >= max_corr) {
            max_corr = suma3b;
            modificarLista(matriz1, matriz3, matriz2, resultado);
        }


        return resultado;
    }




    public static double getMaxCorrelacion(int[][] matriz, int[][] matriz2, int cant_col, boolean comparar_en_orden) {

        double max_corr = 0.d;


        for (int i = cant_col; i > 0; i--) {
            int[] submat_1 = Utils.getSubmatriz(matriz, i, !comparar_en_orden);
            int[] submat_2 = Utils.getSubmatriz(matriz2, i, comparar_en_orden);
            double corr = Utils.getCorrelacion(submat_1, submat_2);

            if (corr > max_corr) {
                max_corr = corr;
            }
        }
        return max_corr;


    }

    /**
     * Método estático que se encarga de redondear un valor decimal de doble precisión, a la cantidad de
     * decimales deseados. Gracias Stack Overflow por tantas alegrías.
     * @param value Valor a redondear.
     * @param places Cantidad de decimales deseados.
     * @return Valor redondeado.
     */
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /**
     * Método estático que se encarga de levantar la información sobre distribución de probabilidades
     * de un archivo de texto, cuyas entradas están expresadas de la forma (símbolo) : (frecuencia).
     * A partir de la información del archivo, se genera un mapeo símbolo - frecuencia.
     * @param archivo_fuente Archivo TXT con las distribuciones de probabilidad para cada símbolo.
     * @return Información equivalente al TXT en forma de mapa.
     */
    public static Map<String, Double> generar_mapeo_simbolo_frecuencia(File archivo_fuente)
    {
        Map<String, Double> mapa = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(archivo_fuente));

            while(br.ready()) {
                String aux = br.readLine();
                List<String> aux_elementos = Splitter.on(" : ").splitToList(aux);
               mapa.put(aux_elementos.get(0), Double.valueOf(aux_elementos.get(1)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mapa;
    }

    /**
     * Método estático que expresa una imagen en su correspondiente cadena de texto de 0s y 1s
     * según una codificación de Huffman.
     * @param img Imagen base.
     * @param codificacion Mapa que relaciona cada símbolo con su correspondiente versión en Huffman (binaria).
     * @return Cadena de texto en 0s y 1s que representa a la imagen deseada.
     */
    public static String getCadenaPixeles(BufferedImage img, Map<String, String> codificacion){
        int[] pixeles = img.getRGB(0, 0, img.getWidth(), img.getHeight(), null, 0, img.getWidth());
        int[] pixeles_rgb = new int[pixeles.length];

        StringBuilder resultado  = new StringBuilder();

        for (int i = 0; i < pixeles.length; i++)
            pixeles_rgb[i] = new Color(pixeles[i]).getRed();

        for (int aPixeles_rgb : pixeles_rgb) {
            String clave = Integer.toString(aPixeles_rgb);
            resultado.append(codificacion.get(clave));
        }

        return resultado.toString();
    }

    /**
     * Método estático que indica si la resta de dos valores es menor a un determinado umbral.
     * @param valor1 Valor 1 fuente.
     * @param valor2 Valor 2 fuente.
     * @param umbral Umbral de comparación.
     * @return Booleano que indica si está dentro del umbral tolerado.
     */
    public static boolean perteneceUmbral(int valor1, int valor2, int umbral)
    {
        return Math.abs(valor1 - valor2) <= umbral;

    }

    /**
     * Método estático que implementa el algoritmo de compresión con pérdida Run-Length para una
     * determinada fuente de información.
     * Se guarda en un archivo de texto las ocurrencias de cada símbolo.
     * @param arr Fuente de información a comprimir
     * @param umbral Umbral tolerado, indica factor de compresión.
     * @param alto alto de la imagen a comprimir
     * @param ancho de la imagen a comprimir
     */
    public static void runLength (int [] arr, int umbral,int alto, int ancho){

        int i=0;
        int j=0;
        int cant_ocurrencias=0;
        FileWriter fichero = null;
        PrintWriter pw;

        try {
            fichero = new FileWriter(new File("runlength.txt"));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        pw = new PrintWriter(fichero);
        pw.println(ancho+","+alto);
        while (i < arr.length) {
            boolean pertenece=perteneceUmbral(arr[i],arr[j], umbral);
            while((pertenece) && (j < arr.length)){
                cant_ocurrencias++;
                j++;
                if (j < arr.length)
                    pertenece = perteneceUmbral(arr[i],arr[j], umbral);
                else
                    pertenece = false;

            }

            pw.println(arr[i] + ":" + cant_ocurrencias);
            cant_ocurrencias = 0;
            i=j;
        }

        pw.close();

    }

    /**
     * Método estático que genera una imagen (representada en un arreglo unidimensional) a partir de
     * un archivo de texto que contiene como cabecera el ancho y alto de la imagen. Luego cantidad de ocurrencias para cada símbolo a medida que se
     * recorre la imagen.
     * @return Arreglo unidimensional que representa la imagen generada.
     */
    public static int [] getArregloRunLength(){


        int indice = 0;
        String linea="";
        File archivo = new File ("runlength.txt");
        FileReader fr = null;
        try {
            fr = new FileReader(archivo);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        BufferedReader br = new BufferedReader(fr);

        try {
            linea=br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int x = 0;
        int anchoInt=0;
        int altoInt=0;
        String ancho="",alto="";
        while(linea.charAt(x) != ','){
            ancho+=linea.charAt(x);
            x++;
        }
        x++;
        while(x<linea.length()){
            alto+=linea.charAt(x);
            x++;
        }
        anchoInt=Integer.parseInt(ancho);
        altoInt=Integer.parseInt(alto);
        int [] resultado = new int [anchoInt*altoInt];

        try {
            while((linea=br.readLine())!=null){

                String pixel="";
                String cantidad="";
                int numpixel;
                int cant;

                int i = 0;
                while (linea.charAt(i)!=':'){
                    pixel+=linea.charAt(i);
                    i++;

                }

                i++;
                while(i<linea.length()){
                    cantidad+=linea.charAt(i);
                    i++;
                }

                numpixel = Integer.parseInt(pixel);
                cant = Integer.parseInt(cantidad);

                for(int j=0 ; j<cant; j++){
                    resultado[indice]=numpixel;
                    indice++;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return resultado;


    }
}
