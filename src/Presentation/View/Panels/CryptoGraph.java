package Presentation.View.Panels;

import Business.Entities.Muestra;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * The type Grafico criptomoneda.
 */
public class CryptoGraph extends JPanel {

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
    public CryptoGraph() {
        samples = new LinkedList<>();
        setBackground(new Color(28, 36, 52));
        repaint();
    }

    /**
     * Calcular rango y.
     */
    void calculateYRange() {
        minPrice = samples.stream().mapToDouble(Muestra::getPrecio).min().orElse(0.0);
        maxPrice = samples.stream().mapToDouble(Muestra::getPrecio).max().orElse(1.0);

        // Añadir margen del 10% para mejor visualización
        double margin = (maxPrice - minPrice) * 0.1;
        minPrice -= margin;
        maxPrice += margin;

        // Redondear a múltiplos de paso adecuado
        double power = Math.pow(10, Math.floor(Math.log10(maxPrice - minPrice)));
        double step = ((maxPrice - minPrice)/power < 5) ? power : power * 2;
        minPrice = Math.floor(minPrice / step) * step;
        maxPrice = Math.ceil(maxPrice / step) * step;
    }

    private int calculateXRange() {
        long difference = samples.getLast().getFecha().getTime() - samples.getFirst().getFecha().getTime();
        long totalMinutes = TimeUnit.MILLISECONDS.toMinutes(difference);

        int interval;
        if (totalMinutes <= 1) interval = 15; // Cada 15 segundos para menos de 1 minutos
        else if (totalMinutes <= 5) interval = 60; // Cada minuto para menos de 5 minutos
        else if (totalMinutes <= 10) interval = 2 * 60; // Cada 2 minutos para menos de 10 minutos
        else if (totalMinutes <= 30) interval = 5 * 60; // Cada 5 minutos
        else if (totalMinutes <= 60) interval = 10 * 60;
        else interval = 15 * 60; // Cada 15 minutos para más de 1 hora

        return interval;
    }

    private int mapTimeToPosition(long time) {
        long timeRange = samples.getLast().getFecha().getTime() - samples.getFirst().getFecha().getTime();
        if (timeRange == 0) return MARGIN_L;

        return MARGIN_L + (int) ((time - samples.getFirst().getFecha().getTime()) * (getWidth() - MARGIN_L - MARGIN_R) / timeRange);
    }

    /**
     * Sets samples.
     *
     * @param values the values
     */
    public void setSamples(LinkedList<Muestra> values) {
        if (values != null) {
            SwingUtilities.invokeLater(() -> {
                samples.clear();
                samples.addAll(values);
                repaint();
            });
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        int graphAreaWidth = width - MARGIN_L - MARGIN_R;
        int graphAreaHeight = height - MARGIN_SUP - MARGIN_BOT;
        if (samples != null && !samples.isEmpty()) {
            if (samples.getFirst().getFecha() != null && samples.getLast().getFecha() != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(samples.getFirst().getFecha());

                // Ajustar al primer interval múltiplo
                int interval = calculateXRange();
                int seconds = cal.get(Calendar.SECOND);
                cal.add(Calendar.SECOND, -(seconds % interval));

                // Dibujar marcas principales
                while (cal.getTime().before(samples.getLast().getFecha())) {
                    Date mark = cal.getTime();
                    int xPosition = mapTimeToPosition(mark.getTime());

                    // Seleccionar formato según intervalo
                    String texto = (interval < 60) ? FORMATO_LONG_DATE.format(mark) : FORMAT_SHORT_DATE.format(mark);

                    // Dibujar línea y texto
                    if (xPosition >= MARGIN_L && xPosition <= MARGIN_L + graphAreaWidth) {
                        g2d.setColor(new Color(70, 129, 137));
                        g2d.drawLine(xPosition, height - MARGIN_BOT, xPosition, height - MARGIN_BOT + 5);
                        g2d.drawString(texto, xPosition - 20, height - MARGIN_BOT + 20);
                    }

                    cal.add(Calendar.SECOND, interval);
                }
            }
        }

        calculateYRange();

        // Dibujar líneas horizontales y etiquetas del eje Y
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.setStroke(new BasicStroke(0.5f));

        // Calcular valores dinámicos para el eje Y
        double[] yValues = new double[6];
        for(int i = 0; i <= 5; i++) {
            yValues[i] = minPrice + ((maxPrice - minPrice) / 5 * i);
        }

        // Dibujar líneas de referencia
        for (double valor : yValues) {
            double percentage = (valor - minPrice) / (maxPrice - minPrice);
            int y = height - MARGIN_BOT - (int)(percentage * graphAreaHeight);

            g2d.setColor(new Color(70, 129, 137));
            g2d.drawLine(MARGIN_L, y, width - MARGIN_R, y);

            // Etiquetas del eje Y
            g2d.setColor(new Color(244, 233, 205));
            String price = FORMAT_NUMBER.format(valor);
            FontMetrics fm = g2d.getFontMetrics();
            g2d.drawString(price, MARGIN_L - fm.stringWidth(price) - 5, y + 5);
        }

        // Dibujar la línea del gráfico
        if (samples.size() > 1) {
            if (samples.getLast().getPrecio() >= samples.getFirst().getPrecio()) {
                g2d.setColor(new Color(50, 205, 50)); // Verde
            } else {
                g2d.setColor(new Color(205, 50, 50)); // Rojo
            }
            g2d.setStroke(new BasicStroke(2.0f));

            int[] xPoints = new int[samples.size()];
            int[] yPoints = new int[samples.size()];

            // Calcular las coordenadas de cada punto
            for (int i = 0; i < samples.size(); i++) {
                // Posición X basada en el índice
                xPoints[i] = MARGIN_L + (i * graphAreaWidth) / (samples.size() - 1);

                // Posición Y basada en el precio
                double porcentajePrecio = (samples.get(i).getPrecio() - minPrice) / (maxPrice - minPrice);
                yPoints[i] = height - MARGIN_BOT - (int)(porcentajePrecio * graphAreaHeight);
            }

            // Dibujar segmentos de línea
            for (int i = 0; i < samples.size() - 1; i++) {
                g2d.drawLine(xPoints[i], yPoints[i], xPoints[i + 1], yPoints[i + 1]);
            }

            // Dibujar punto final (actual)
            g2d.fillOval(xPoints[xPoints.length - 1] - 4, yPoints[yPoints.length - 1] - 4, 8, 8);
        }
    }
}
