package com.company;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * Clase que define el histograma que permite visualizar de forma gráfica la probabilidad de
 * ocurrencia para cada símbolo en la imagen.
 * @author Horquin Enzo, Serrano Francisco
 */
public class Histograma {

    private BufferedImage img;
    private File arch_histograma;

    private double promedio, desvio;

    /**
     * Genera el histograma en función a la imagen proporcionada.
     * @param img Imagen sobre la cuál se tomarán los datos.
     */
    public Histograma(BufferedImage img) {
        this.img = img;
        generarHistograma();
        try {
            generarDistribuciones();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retorna el promedio en función a las ocurrencias de cada símbolo.
     * @return Promedio de ocurrencia en función a los símbolos de la imagen.
     */
    public double getPromedio() {
        return promedio;
    }

    /**
     * Retorna el desvío estándar en función a las ocurrencias de cada símbolo.
     * @return Desvío estándar para los símbolos de la imagen.
     */
    public double getDesvio() {
        return desvio;
    }

    /**
     * Método privado que se encarga de construir el histograma y posteriormente guardarlo como PNG.
     */
    private void generarHistograma()
    {
        int[] arr_img = Utils.serializarMatriz(Utils.generarMatrizRGB(img, true));

        double[] arr_posta = new double[arr_img.length];
        for (int i = 0; i < arr_img.length; i++)
            arr_posta[i] = arr_img[i];

        // Estas tres líneas contienen la magia del histograma
        HistogramDataset dataset = new HistogramDataset();
        dataset.addSeries("Histograma", arr_posta, 256);
        JFreeChart chart = ChartFactory.createHistogram("Histograma de Ocurrencias", "Color", "Cantidad Ocurrencias", dataset, PlotOrientation.VERTICAL, false, false, false);

        try {
            arch_histograma = new File("histograma.png");
            ChartUtilities.saveChartAsPNG(arch_histograma, chart, img.getWidth(), img.getHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retorna el archivo que representa la imagen con el histograma.
     * @return Archivo que representa la imagen con el histograma.
     */
    public File getArch_histograma() {
        return arch_histograma;
    }

    /**
     * Método privado que genera las distribuciones en función de la imagen proporcionada.
     * Posteriormente se bajan a un archivo de texto, además del promedio y desvío estándar.
     * @throws IOException En caso de que se produzca algún error relacionado a la I/O.
     */
    private void generarDistribuciones() throws IOException
    {
        int[] arr_img = Utils.serializarMatriz(Utils.generarMatrizRGB(img, true));

        HashMap<Integer, Double> mapa = Utils.getProbabilidades(arr_img);

        // A partir de acá se efectúa el volcado de la información
        FileWriter fichero = null;
        PrintWriter pw = null;

        fichero = new FileWriter(new File("distribucion.txt"));
        pw = new PrintWriter(fichero);

        for(Integer i : mapa.keySet())
            pw.println(i + " : " + mapa.get(i));

        try {
            fichero.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileWriter fichero2 = null;
        PrintWriter pw2 = null;

        fichero2 = new FileWriter(new File("promedio_desviacion.txt"));
        pw2 = new PrintWriter(fichero2);

        promedio = Utils.getPromedio(arr_img);
        desvio = Utils.getDesviacion(arr_img, mapa);

        pw2.println("Promedio: " + promedio);
        pw2.println("Desviación estándar: " + desvio);

        fichero2.close();
    }
}
