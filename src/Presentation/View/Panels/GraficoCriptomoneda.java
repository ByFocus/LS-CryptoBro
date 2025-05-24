package Presentation.View.Panels;

import Business.Entities.Muestra;
import Presentation.View.Popups.CryptoInfo;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * The type Grafico criptomoneda.
 */
public class GraficoCriptomoneda extends JPanel {

    private final DecimalFormat FORMAT_NUMBER = new DecimalFormat("#.#####€");
    private final SimpleDateFormat FORMAT_SHORT_DATE = new SimpleDateFormat("HH:mm");
    private final SimpleDateFormat FORMATO_LONG_DATE = new SimpleDateFormat("HH:mm:ss");

    private final int MARGIN_SUP = 20;
    private final int MARGIN_R = 40;
    private final int MARGIN_BOT = 30;
    private final int MARGIN_L = 70;

    private final LinkedList<Muestra> samples;
    private double minPrice  = 0;
    private double maxPrice = 1;

    /**
     * Instantiates a new Grafico criptomoneda.
     */
    public GraficoCriptomoneda() {
        samples = new LinkedList<>();
        setBackground(new Color(28, 36, 52));
        repaint();
    }

    /**
     * Calcular rango y.
     */
    void calcularRangoY() {
        minPrice = samples.stream().mapToDouble(Muestra::getPrecio).min().orElse(0.0);
        maxPrice = samples.stream().mapToDouble(Muestra::getPrecio).max().orElse(1.0);

        // Añadir margen del 10% para mejor visualización
        double margen = (maxPrice - minPrice) * 0.1;
        minPrice -= margen;
        maxPrice += margen;

        // Redondear a múltiplos de paso adecuado
        double rangoBruto = maxPrice - minPrice;
        double potencia = Math.pow(10, Math.floor(Math.log10(rangoBruto)));
        double paso = (rangoBruto/potencia < 5) ? potencia : potencia * 2;
        minPrice = Math.floor(minPrice / paso) * paso;
        maxPrice = Math.ceil(maxPrice / paso) * paso;
    }

    private int calcularRangoX() {
        long diferencia = samples.getLast().getFecha().getTime() - samples.getFirst().getFecha().getTime();
        long minutosTotales = TimeUnit.MILLISECONDS.toMinutes(diferencia);

        int intervalo;
        if (minutosTotales <= 1) intervalo = 15; // Cada 15 segundos para menos de 1 minutos
        else if (minutosTotales <= 5) intervalo = 60; // Cada minuto para menos de 5 minutos
        else if (minutosTotales <= 10) intervalo = 2 * 60; // Cada 2 minutos para menos de 10 minutos
        else if (minutosTotales <= 30) intervalo = 5 * 60; // Cada 5 minutos
        else if (minutosTotales <= 60) intervalo = 10 * 60;
        else intervalo = 15 * 60; // Cada 15 minutos para más de 1 hora

        return intervalo;
    }

    private int mapTiempoAPosicion(long tiempo) {
        long rangoTiempo = samples.getLast().getFecha().getTime() - samples.getFirst().getFecha().getTime();
        if (rangoTiempo == 0) return MARGIN_L;

        return MARGIN_L + (int) ((tiempo - samples.getFirst().getFecha().getTime()) * (getWidth() - MARGIN_L - MARGIN_R) / rangoTiempo);
    }

    /**
     * Sets samples.
     *
     * @param valores the valores
     */
    public void setSamples(LinkedList<Muestra> valores) {
        if (valores != null) {
            SwingUtilities.invokeLater(() -> {
                samples.clear();
                samples.addAll(valores);
                repaint();
            });
        }
    }

    public double getFirstValue() {
        return samples.getFirst().getPrecio();
    }

    public double getLastValue() {
        return samples.getLast().getPrecio();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        int areaGraficaAncho = width - MARGIN_L - MARGIN_R;
        int areaGraficaAlto = height - MARGIN_SUP - MARGIN_BOT;
        if (samples != null && !samples.isEmpty()) {
            if (samples.getFirst().getFecha() != null && samples.getLast().getFecha() != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(samples.getFirst().getFecha());

                // Ajustar al primer intervalo múltiplo
                int intervalo = calcularRangoX();
                int segundos = cal.get(Calendar.SECOND);
                cal.add(Calendar.SECOND, -(segundos % intervalo));

                // Dibujar marcas principales
                while (cal.getTime().before(samples.getLast().getFecha())) {
                    Date marca = cal.getTime();
                    int posicionX = mapTiempoAPosicion(marca.getTime());

                    // Seleccionar formato según intervalo
                    String texto = (intervalo < 60) ? FORMATO_LONG_DATE.format(marca) : FORMAT_SHORT_DATE.format(marca);

                    // Dibujar línea y texto
                    if (posicionX >= MARGIN_L && posicionX <= MARGIN_L + areaGraficaAncho) {
                        g2d.setColor(new Color(70, 129, 137));
                        g2d.drawLine(posicionX, height - MARGIN_BOT, posicionX, height - MARGIN_BOT + 5);
                        g2d.drawString(texto, posicionX - 20, height - MARGIN_BOT + 20);
                    }

                    cal.add(Calendar.SECOND, intervalo);
                }
            }
        }
        calcularRangoY();

        // Dibujar líneas horizontales y etiquetas del eje Y
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.setStroke(new BasicStroke(0.5f));

        // Calcular valores dinámicos para el eje Y
        double[] valoresY = new double[6];
        double paso = (maxPrice - minPrice) / 5;

        // La segunda línea desde arriba será el máximo real
        for(int i = 0; i <= 5; i++) {
            valoresY[i] = minPrice + (paso * i);
        }

        // Dibujar líneas de referencia
        for (double valor : valoresY) {
            double porcentaje = (valor - minPrice) / (maxPrice - minPrice);
            int y = height - MARGIN_BOT - (int)(porcentaje * areaGraficaAlto);

            g2d.setColor(new Color(70, 129, 137));
            g2d.drawLine(MARGIN_L, y, width - MARGIN_R, y);

            // Etiquetas del eje Y
            g2d.setColor(new Color(244, 233, 205));
            String textoValor = FORMAT_NUMBER.format(valor);
            FontMetrics fm = g2d.getFontMetrics();
            g2d.drawString(textoValor, MARGIN_L - fm.stringWidth(textoValor) - 5, y + 5);
        }

        // Dibujar la línea del gráfico
        if (samples.size() > 1) {
            if (samples.getLast().getPrecio() >= samples.getFirst().getPrecio()) {
                g2d.setColor(new Color(50, 205, 50)); // Verde
            } else {
                g2d.setColor(new Color(205, 50, 50)); // Verde
            }
            g2d.setStroke(new BasicStroke(2.0f));

            int[] xPuntos = new int[samples.size()];
            int[] yPuntos = new int[samples.size()];

            // Calcular las coordenadas de cada punto
            for (int i = 0; i < samples.size(); i++) {
                // Posición X basada en el índice
                xPuntos[i] = MARGIN_L + (i * areaGraficaAncho) / (samples.size() - 1);

                // Posición Y basada en el precio
                double porcentajePrecio = (samples.get(i).getPrecio() - minPrice) / (maxPrice - minPrice);
                yPuntos[i] = height - MARGIN_BOT - (int)(porcentajePrecio * areaGraficaAlto);
            }

            // Dibujar segmentos de línea
            for (int i = 0; i < samples.size() - 1; i++) {
                g2d.drawLine(xPuntos[i], yPuntos[i], xPuntos[i + 1], yPuntos[i + 1]);
            }

            // Dibujar punto final (actual)
            g2d.fillOval(xPuntos[xPuntos.length - 1] - 4, yPuntos[yPuntos.length - 1] - 4, 8, 8);
        }
    }
}
