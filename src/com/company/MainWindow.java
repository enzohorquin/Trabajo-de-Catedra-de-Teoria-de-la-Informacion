package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

/**
 * Clase que define la ventana principal que se mostrará por pantalla al correr la aplicación.
 * @author Horquin Enzo, Serrano Francisco
 */
public class MainWindow {

    private JFrame frame;

    private JPanel panelSuperior;
    private JPanel panelIntermedio;
    private JPanel panelInferior;

    private JLabel imagen;

    private JButton button_cargarImagen_1;
    private JButton button_cargarImagen_2;
    private JButton button_cargarImagen_3;
    private JButton button_generarPanoramica;
    private JButton button_generarHistograma;
    private JButton button_generarCodificacion;
    private JButton button_comprimir;
    private JButton button_descomprimir;
    private JButton button_comprimir_perdida;
    private JButton button_descomprimir_perdida;
    private ConcatenadorImagenes concatenadorImagenes;

    private String aux;

    /**
     * Genera la ventana principal.
     * @param ci Concatenador de imágenes que se utilizará para unir las imágenes.
     */
    public MainWindow(ConcatenadorImagenes ci) {
        // Crea primero los paneles superior, inferior e intermedio, luego los asigna al frame contenedor de la interfaz.
        buildPanelSuperior();
        buildPanelIntermedio();
        buildPanelInferior();
        buildVentana();

        setListeners();
        concatenadorImagenes = ci;
    }


    /**
     * Método privado que se encarga de construir el panel superior de la ventana, correspondiente
     * a la visualización de la imagen.
     */
    private void buildPanelSuperior() {
        panelSuperior = new JPanel(new GridLayout());

        imagen = new JLabel("Cargar imágenes y luego generar panorámica", SwingConstants.CENTER);
        imagen.setPreferredSize(new Dimension(500, 100));

        panelSuperior.add(imagen);
    }

    /**
     * Método privado que se encarga de construir el panel intermedio de la ventana, correspondiente
     * a la primer fila de botones de la interfaz gráfica.
     */
    private void buildPanelIntermedio() {
        panelIntermedio = new JPanel(new FlowLayout());

        button_cargarImagen_1 = new JButton("Cargar Imagen 1");
        button_cargarImagen_2 = new JButton("Cargar Imagen 2");
        button_cargarImagen_3 = new JButton("Cargar Imagen 3");
        button_generarPanoramica = new JButton("Generar Panorámica");
        button_generarHistograma = new JButton("Mostrar Histograma");

        button_cargarImagen_2.setEnabled(false);
        button_cargarImagen_3.setEnabled(false);
        button_generarPanoramica.setEnabled(false);
        button_generarHistograma.setEnabled(false);

        panelIntermedio.add(button_cargarImagen_1);
        panelIntermedio.add(button_cargarImagen_2);
        panelIntermedio.add(button_cargarImagen_3);
        panelIntermedio.add(button_generarPanoramica);
        panelIntermedio.add(button_generarHistograma);
    }

    /**
     * Método privado que se encarga de construir el panel inferior de la ventana, correspondiente
     * a la segunda fila de botones de la interfaz gráfica.
     */
    private void buildPanelInferior() {
        panelInferior = new JPanel(new FlowLayout());

        button_generarCodificacion = new JButton("Generar Codificación Huffman");
        button_comprimir = new JButton("Comprimir Imagen");
        button_descomprimir = new JButton("Descomprimir Imagen");
        button_comprimir_perdida = new JButton("Comprimir con Pérdida");
        button_descomprimir_perdida = new JButton("Descomprimir con Pérdida");

        button_generarCodificacion.setEnabled(false);
        button_comprimir.setEnabled(true);
        button_descomprimir.setEnabled(true);
        button_comprimir_perdida.setEnabled(false);
        button_descomprimir_perdida.setEnabled(false);

        panelInferior.add(button_generarCodificacion);
        panelInferior.add(button_comprimir);
        panelInferior.add(button_descomprimir);
        panelInferior.add(button_comprimir_perdida);
        panelInferior.add(button_descomprimir_perdida);
    }

    /**
     * Método privado que se encarga de construir la ventana principal, agregando los paneles generados
     * por los métodos correspondientes.
     */
    private void buildVentana() {
        frame = new JFrame("TPE Teoría de la Información - Enzo Horquín, Francisco Serrano");

        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.add(panelSuperior);
        frame.add(panelIntermedio);
        frame.add(panelInferior);

        frame.setResizable(false);

        frame.pack();

        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    /**
     * Método privado que describe el comportamiento de la aplicación al presionarse cada botón.
     */
    private Map<String, Double> setListeners()
    {
        // Abre un selector de archivos para la primer imagen, y se la envía al concatenador
        button_cargarImagen_1.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser(Paths.get("").toAbsolutePath().toString());
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Imágenes BMP", "bmp");
            chooser.setFileFilter(filter);

            int returnVal = chooser.showOpenDialog(frame);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                concatenadorImagenes.setArchivo1(chooser.getSelectedFile());
                button_cargarImagen_1.setText(chooser.getSelectedFile().getName());
                button_cargarImagen_2.setEnabled(true);
            }
        });

        // Abre un selector de archivos para la segunda imagen, y se la envía al concatenador
        button_cargarImagen_2.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser(Paths.get("").toAbsolutePath().toString());
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Imágenes BMP", "bmp");
            chooser.setFileFilter(filter);

            int returnVal = chooser.showOpenDialog(frame);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                concatenadorImagenes.setArchivo2(chooser.getSelectedFile());
                button_cargarImagen_2.setText(chooser.getSelectedFile().getName());
                button_cargarImagen_3.setEnabled(true);
            }
        });

        // Abre un selector de archivos para la tercer imagen, y se la envía al concatenador
        button_cargarImagen_3.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser(Paths.get("").toAbsolutePath().toString());
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Imágenes BMP", "bmp");
            chooser.setFileFilter(filter);

            int returnVal = chooser.showOpenDialog(frame);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                concatenadorImagenes.setArchivo3(chooser.getSelectedFile());
                button_cargarImagen_3.setText(chooser.getSelectedFile().getName());
                button_generarPanoramica.setEnabled(true);
            }
        });

        // Procede a ordenarle al concatenador que una las imagenes, y con el resultado las muestra en el panel superior
        button_generarPanoramica.addActionListener(e -> {
            concatenadorImagenes.concatenar();

            if (button_cargarImagen_1.getText().equals(button_cargarImagen_2.getText()) || button_cargarImagen_1.getText().equals(button_cargarImagen_3.getText()) || button_cargarImagen_2.getText().equals(button_cargarImagen_3.getText()))
                JOptionPane.showMessageDialog(frame, "Se pueden producir resultados inesperados\nChequear archivos escogidos!");

            panelSuperior.remove(imagen);
            imagen = new JLabel();
            imagen.setIcon(new ImageIcon(concatenadorImagenes.getImgFinal()));
            panelSuperior.add(imagen);

            button_generarHistograma.setEnabled(true);
            button_generarCodificacion.setEnabled(true);
            button_comprimir_perdida.setEnabled(true);

            frame.pack();
        });

        // Procede a generar el histograma, y con el resultado, en formato de imagen, lo muestra en el panel superior.
        button_generarHistograma.addActionListener(e -> {
            BufferedImage img = concatenadorImagenes.getImgFinal();
            Histograma histograma = new Histograma(img);

            BufferedImage img_hist = null;
            try {
                img_hist = ImageIO.read(histograma.getArch_histograma());
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            panelSuperior.remove(imagen);
            imagen = new JLabel();
            imagen.setIcon(new ImageIcon(img_hist));
            panelSuperior.add(imagen);

            button_generarCodificacion.setEnabled(true);

            frame.pack();

            JOptionPane.showMessageDialog(frame, "Los datos correspondientes se volcaron en distribucion.txt y promedio_desviacion.txt" +
                    "\nPromedio: " + Utils.round(histograma.getPromedio(), 3) +
                    "\nDesvio Estandar: " + Utils.round(histograma.getDesvio(), 3));
        });

        // Procede a generar la codificación de Huffman a partir de los píxeles de la imagen concatenada
        button_generarCodificacion.addActionListener(e -> {

           // BufferedImage img_final = concatenadorImagenes.getImgFinal();
            //int[] arr_img = Utils.serializarMatriz(Utils.generarMatrizRGB(img_final, true));
            Map<String, Double> mapa_probs;

            mapa_probs = getProbabilidades();
            List<NodoHoja> listaNodos = new ArrayList<>();
            for (String simbolo : mapa_probs.keySet())
                listaNodos.add(new NodoHoja(mapa_probs.get(simbolo), simbolo.toString()));

            CodificadorHuffmanNuevo huffman = new CodificadorHuffmanNuevo(listaNodos);

            FileWriter fichero = null;

            try {
                fichero = new FileWriter(new File("codificacion.txt"));
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            PrintWriter pw = new PrintWriter(fichero);

            Map<String, String> mapa_simbolo_codificacion = new HashMap<>();
            CodificadorHuffmanNuevo.getCodificacion(huffman.getArbol(), new StringBuilder(), mapa_simbolo_codificacion);
           // HashMap<Integer,Double> probabilidades = Utils.getProbabilidades(arr_img);

            double suma =0.0d;

            for (String key : mapa_simbolo_codificacion.keySet()) {
                Integer clave  = Integer.parseInt(key);
                suma += mapa_simbolo_codificacion.get(key).length()*mapa_probs.get(key);
                pw.println(key + " : " + mapa_simbolo_codificacion.get(key));
            }

            pw.println("Longitud media: " + suma);

            JOptionPane.showMessageDialog(frame, "La codificación se volcó en el archivo codificacion.txt\nLongitud media: " + Utils.round(suma, 3));


            try {
                fichero.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            button_comprimir.setEnabled(true);
        });

        // Procede a comprimir el archivo
        button_comprimir.addActionListener(e -> {

            JFileChooser chooser_img = new JFileChooser(Paths.get("").toAbsolutePath().toString());
            FileNameExtensionFilter filter_img = new FileNameExtensionFilter("Imágenes BMP", "BMP");

            chooser_img.setDialogTitle("Seleccionar imagen a comprimir");
            chooser_img.setFileFilter(filter_img);

            int returnVal = chooser_img.showOpenDialog(frame);

            if (returnVal != JFileChooser.APPROVE_OPTION)
                return;

            JFileChooser chooser_probs = new JFileChooser(Paths.get("").toAbsolutePath().toString());
            FileNameExtensionFilter filter_probs = new FileNameExtensionFilter("Distribución de Probabilidades TXT", "txt");

            chooser_probs.setDialogTitle("Seleccionar TXT con las probabilidades");
            chooser_probs.setFileFilter(filter_probs);

            returnVal = chooser_probs.showOpenDialog(frame);

            if (returnVal != JFileChooser.APPROVE_OPTION)
                return;

            BufferedImage img_seleccionada = null;
            CodificadorHuffmanNuevo huffman = null;

            try {
                img_seleccionada = ImageIO.read(chooser_img.getSelectedFile());
                huffman = new CodificadorHuffmanNuevo(chooser_probs.getSelectedFile());
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            Map<String, String> mapa = new HashMap<>();
            CodificadorHuffmanNuevo.getCodificacion(huffman.getArbol(), new StringBuilder(), mapa);

            aux = Utils.getCadenaPixeles(img_seleccionada, mapa);

            int ancho = img_seleccionada.getWidth();
            int alto = img_seleccionada.getHeight();

            try {
                CodificadorHuffmanNuevo.generarComprimido(aux, "comprimido", ancho, alto);
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            JOptionPane.showMessageDialog(frame, "La imagen se comprimió exitosamente en el siguiente archivo:\ncomprimido.huff");
        });

        // Abre un selector de archivos para seleccionar la imagen comprimida
        button_descomprimir.addActionListener(e -> {
            JFileChooser chooser_huff = new JFileChooser(Paths.get("").toAbsolutePath().toString());
            FileNameExtensionFilter filter_huff = new FileNameExtensionFilter("Imágenes comprimidas HUFF", "huff");

            chooser_huff.setDialogTitle("Seleccionar imagen HUFF para descomprimir");
            chooser_huff.setFileFilter(filter_huff);

            int returnVal = chooser_huff.showOpenDialog(frame);

            if (returnVal != JFileChooser.APPROVE_OPTION)
                return;

            JFileChooser chooser_probs = new JFileChooser(Paths.get("").toAbsolutePath().toString());
            FileNameExtensionFilter filter_probs = new FileNameExtensionFilter("Distribución de Probabilidades TXT", "txt");

            chooser_probs.setDialogTitle("Seleccionar TXT con las probabilidades");
            chooser_probs.setFileFilter(filter_probs);

            returnVal = chooser_probs.showOpenDialog(frame);

            if (returnVal != JFileChooser.APPROVE_OPTION)
                return;

            CodificadorHuffmanNuevo huffman = null;
            List<String> aux = null;

            try {
                huffman = new CodificadorHuffmanNuevo(chooser_probs.getSelectedFile());
                aux = CodificadorHuffmanNuevo.leerArchivo(chooser_huff.getSelectedFile());
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            int ancho = Integer.parseInt(aux.get(0)); // Ancho
            int alto = Integer.parseInt(aux.get(1)); // Alto
            String cadena = aux.get(2); // Codificación

            List<String> decodificacion = CodificadorHuffmanNuevo.getCadena(huffman.getArbol(), new StringBuilder(cadena),ancho,alto);
            int[] arr_img_desc = new int[decodificacion.size()];

            for (int i = 0; i < arr_img_desc.length; i++){
                int color = Integer.parseInt(decodificacion.get(i));
                arr_img_desc[i] = new Color(color, color, color).getRGB();
            }

            BufferedImage img_descomprimida = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
            img_descomprimida.getRaster().setDataElements(0, 0, ancho, alto, arr_img_desc);

            try {
                ImageIO.write(img_descomprimida, "bmp", new File("descomprimido.bmp"));
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            panelSuperior.remove(imagen);
            imagen = new JLabel();
            imagen.setIcon(new ImageIcon(img_descomprimida));
            panelSuperior.add(imagen);

            frame.pack();

            JOptionPane.showMessageDialog(frame, "La imagen se descomprimió exitosamente en el siguiente archivo:\ndescomprimido.bmp");
        });

        // Comprime con pérdida
        button_comprimir_perdida.addActionListener(e -> {

            concatenadorImagenes.generarComprimidoRunLength(100);
            JOptionPane.showMessageDialog(frame, "La imagen se comprimió exitosamente en el siguiente archivo:\nrunlength.txt");
            button_descomprimir_perdida.setEnabled(true);


        });
        button_descomprimir_perdida.addActionListener(e-> {
            concatenadorImagenes.descomprimirRunLength();
            panelSuperior.remove(imagen);
            imagen = new JLabel();
            imagen.setIcon(new ImageIcon(concatenadorImagenes.getImgFinalRunLength()));
            panelSuperior.add(imagen);

            frame.pack();
        });
        return null;
    }

    private Map<String,Double> getProbabilidades() {
        Map<String,Double> mapa_resultado;

        JFileChooser chooser_probs = new JFileChooser(Paths.get("").toAbsolutePath().toString());
        FileNameExtensionFilter filter_probs = new FileNameExtensionFilter("Distribución de Probabilidades TXT", "txt");

        chooser_probs.setDialogTitle("Seleccionar TXT con las probabilidades");
        chooser_probs.setFileFilter(filter_probs);

        int returnVal = chooser_probs.showOpenDialog(frame);

        if (returnVal != JFileChooser.APPROVE_OPTION)
            return null;

        mapa_resultado = Utils.generar_mapeo_simbolo_frecuencia(chooser_probs.getSelectedFile());

        return mapa_resultado;
    }

}
