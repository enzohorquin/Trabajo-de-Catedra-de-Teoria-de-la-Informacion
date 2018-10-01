package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Define a la clase que contiene las tres imágenes fuente, así como la imagen panorámica generada y la imagen
 * panorámica comprimida por pérdida mediante la implementación del algoritmo RUN-LENGTH.
 * @author Horquin Enzo, Serrano Francisco
 */
public class ConcatenadorImagenes {

    private BufferedImage img1 = null;
    private BufferedImage img2 = null;
    private BufferedImage img3 = null;

    private int alto;
    private int ancho;
    private int size;
    private int[] arr_final;

    private BufferedImage imgFinal = null;
    private BufferedImage imgFinalRunLength = null;

    private static final int CANTIDAD_COLUMNAS = 50;

    /**
     * Retorna la imagen panorámica resultante de la concatenación de las tres imágenes fuente.
     * @return Imagen panorámica resultante.
     */
    public BufferedImage getImgFinal() {
        return imgFinal;
    }

    /**
     * Retorna la imagen resultante de la compresión con pérdida de la panorámica generada
     * anteriormente.
     * @return Retorna la imagen comprimida con pérdida.
     */
    public BufferedImage getImgFinalRunLength() {
        return imgFinalRunLength;
    }

    /**
     * Asigna al concatenador la primer imagen fuente.
     * @param archivo1 Archivo que representa la imagen en formato BMP.
     */
    public void setArchivo1(File archivo1) {
        try {
            this.img1 = ImageIO.read(archivo1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Asigna al concatenador la segunda imagen fuente.
     * @param archivo2 Archivo que representa la imagen en formato BMP.
     */
    public void setArchivo2(File archivo2) {
        try {
            this.img2 = ImageIO.read(archivo2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Asigna al concatenador la tercer imagen fuente.
     * @param archivo3 Archivo que representa la imagen en formato BMP.
     */
    public void setArchivo3(File archivo3) {
        try {
            this.img3 = ImageIO.read(archivo3);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Efectúa la compresión con pérdida a partir de la imagen panorámica concatenada anteriormente.
     * @param umbral Umbral de pérdida durante la compresión.
     */
    public void generarComprimidoRunLength(int umbral){

        Utils.runLength(Utils.convertirArregloRGBaGrises(arr_final),umbral,this.alto,this.ancho);

    }
    public void descomprimirRunLength(){
        int[] arr_runlength = Utils.getArregloRunLength();

        int[] arr_final2 = new int[arr_runlength.length];

        for (int i = 0; i < arr_runlength.length; i++) {
            arr_final2[i] = new Color(arr_runlength[i], arr_runlength[i], arr_runlength[i]).getRGB();

        }

        this.imgFinalRunLength = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
        WritableRaster rasterRL = this.imgFinalRunLength.getRaster();
        rasterRL.setDataElements(0,0,ancho,alto, arr_final2);

        // Volcado de la imagen al archivo
        try {
            ImageIO.write(this.imgFinalRunLength, "bmp", new File("foto_run_length.bmp"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Procede a unir las tres imágenes fuente y volcar a un archivo el resultado.
     */
    public void concatenar()
    {
        // Genero las matrices a partir de las imágenes
        int[][] mat_imagen_1 = Utils.generarMatrizRGB(img1, false);
        int[][] mat_imagen_2 = Utils.generarMatrizRGB(img2, false);
        int[][] mat_imagen_3 = Utils.generarMatrizRGB(img3, false);

        // Convierto las matrices RGB a escala de grises para poder ordenarlas correctamente
        int[][] mat_imagen_1_grises = Utils.convertirMatrizRGBaGrises(mat_imagen_1);
        int[][] mat_imagen_2_grises = Utils.convertirMatrizRGBaGrises(mat_imagen_2);
        int[][] mat_imagen_3_grises = Utils.convertirMatrizRGBaGrises(mat_imagen_3);

        // Dispongo las matrices en el orden correspondiente para luego ser concatenadas
        List<int[][]> result = Utils.unir(mat_imagen_1_grises, mat_imagen_2_grises, mat_imagen_3_grises);

        // Reacomodo el orden de las matrices, en orden numérico para luego poder unirlas
        mat_imagen_1 = Utils.convertirMatrizGrisesRGB(result.get(2));
        mat_imagen_2 = Utils.convertirMatrizGrisesRGB(result.get(1));
        mat_imagen_3 = Utils.convertirMatrizGrisesRGB(result.get(0));

        // Idem pero para las matrices de grises, para así poder computar la correlación correctamente
        mat_imagen_1_grises = Utils.convertirMatrizRGBaGrises(mat_imagen_1);
        mat_imagen_2_grises = Utils.convertirMatrizRGBaGrises(mat_imagen_2);
        mat_imagen_3_grises = Utils.convertirMatrizRGBaGrises(mat_imagen_3);

        // Computo las columnas en donde se dan las coincidencias entre las imágenes

        int columnaCoincidencia_imagen1_2 = Utils.getColumnasCoincidentes(mat_imagen_1_grises, mat_imagen_2_grises, CANTIDAD_COLUMNAS, true);

        int columnaCoincidencia_imagen2_3 = Utils.getColumnasCoincidentes(mat_imagen_2_grises, mat_imagen_3_grises, CANTIDAD_COLUMNAS, true);

        // Recorto la primer y segunda matriz para eliminar redundancias en la concatenación
        int[] arr_imagen_1_recortada = Utils.getSubmatriz(mat_imagen_1, mat_imagen_1[0].length - columnaCoincidencia_imagen1_2, true);
        int[][] mat_imagen_1_recortada = Utils.expandirArregloMatriz(arr_imagen_1_recortada, mat_imagen_1[0].length - columnaCoincidencia_imagen1_2, mat_imagen_1.length);
        int[] arr_imagen_2_recortada = Utils.getSubmatriz(mat_imagen_2, mat_imagen_2[0].length - columnaCoincidencia_imagen2_3, true);
        int[][] mat_imagen_2_recortada = Utils.expandirArregloMatriz(arr_imagen_2_recortada, mat_imagen_2[0].length - columnaCoincidencia_imagen2_3, mat_imagen_2.length);

        // Efectúo concatenación
        int[][] mat_final = Utils.juntarMatrices(mat_imagen_1_recortada, mat_imagen_2_recortada, mat_imagen_3);

        // Convierto la matriz en arreglo, para poder cargarla en el raster
        int[] arr_final = Utils.serializarMatriz(mat_final);

        // Dimensiones de la imagen final
        int ancho = mat_final[0].length;
        int alto = mat_final.length;

        this.alto = alto;
        this.ancho = ancho;
        this.size = arr_final.length;
        this.arr_final = arr_final;

        // Cargo en memoria la imagen donde se escribirá la panorámica
        this.imgFinal = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        // Raster donde se vuelca el contenido del arreglo de píxeles
        WritableRaster raster = this.imgFinal.getRaster();
        raster.setDataElements(0, 0, ancho, alto, arr_final);

        // Volcado de la imagen al archivo
        try {
            ImageIO.write(this.imgFinal, "bmp", new File("foto_concatenada.bmp"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
