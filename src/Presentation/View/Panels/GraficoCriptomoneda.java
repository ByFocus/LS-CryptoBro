package Presentation.View.Panels;

import Business.Entities.Muestra;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class GraficoCriptomoneda extends JPanel {
    private final int TIEMPO_DE_MUESTREO = 10;
    private final int NUM_MAX_PUNTOS = TIEMPO_DE_MUESTREO* 60 /5;

    private final DecimalFormat FORMATO_NUMERO = new DecimalFormat("#,###");
    private final SimpleDateFormat FORMATO_FECHA_CORTO = new SimpleDateFormat("HH:mm");
    private final SimpleDateFormat FORMATO_FECHA_LARGO = new SimpleDateFormat("HH:mm:ss");

    private final int MARGEN_SUP = 20;
    private final int MARGEN_DER = 20;
    private final int MARGEN_INF = 30;
    private final int MARGEN_IZQ = 70;

    private final LinkedList<Muestra> muestras = new LinkedList<>();
    private double precioMinimoActual = 0;
    private double precioMaximoActual = 1;
    private Date primeraMuestra;
    private Date ultimaMuestra;

    public GraficoCriptomoneda() {
        setBackground(new Color(28, 36, 52));
        repaint();
    }

    public void actualizarDatos(Muestra nuevoValor) {
        //Si ya he llegado al muestreo max, borro la ultima
        if (muestras.size() == NUM_MAX_PUNTOS) {
            muestras.removeFirst();
        }
        muestras.addLast(nuevoValor);
        primeraMuestra = muestras.getFirst().getFecha();
        ultimaMuestra = muestras.getLast().getFecha();
    }

    void calcularRangoY() {
        precioMinimoActual = muestras.stream().mapToDouble(Muestra::getPrecio).min().orElse(0.0);
        precioMaximoActual = muestras.stream().mapToDouble(Muestra::getPrecio).max().orElse(1.0);

        // Añadir margen del 10% para mejor visualización
        double margen = (precioMaximoActual - precioMinimoActual) * 0.1;
        precioMinimoActual -= margen;
        precioMaximoActual += margen;

        // Redondear a múltiplos de paso adecuado
        double rangoBruto = precioMaximoActual - precioMinimoActual;
        double potencia = Math.pow(10, Math.floor(Math.log10(rangoBruto)));
        double paso = (rangoBruto/potencia < 5) ? potencia : potencia * 2;
        precioMinimoActual = Math.floor(precioMinimoActual / paso) * paso;
        precioMaximoActual = Math.ceil(precioMaximoActual / paso) * paso;
    }

    private int calcularRangoX() {
        long diferencia = ultimaMuestra.getTime() - primeraMuestra.getTime();
        long minutosTotales = TimeUnit.MILLISECONDS.toMinutes(diferencia);

        int intervalo;
        if (minutosTotales <= 1) intervalo = 15; // Cada 15 segundos para menos de 1 minutos
        else if (minutosTotales <= 5) intervalo = 60; // Cada minuto para menos de 5 minutos
        else if (minutosTotales <= 10) intervalo = 2 * 60; // Cada 2 minutos para menos de 10 minutos
        else if (minutosTotales <= 25) intervalo = 5 * 60; // Cada 5 minutos
        else intervalo = 15 * 60; // Cada 15 minutos para más de 1 hora

        return intervalo;
    }

    private int mapTiempoAPosicion(long tiempo) {
        long rangoTiempo = ultimaMuestra.getTime() - primeraMuestra.getTime();
        if (rangoTiempo == 0) return MARGEN_IZQ;

        return MARGEN_IZQ + (int) ((tiempo - primeraMuestra.getTime()) * (getWidth() - MARGEN_IZQ - MARGEN_DER) / rangoTiempo);
    }

    public void setMuestras(LinkedList<Double> valores) {
        Date ahora = new Date();
        if (valores != null) {
            SwingUtilities.invokeLater(() -> {
                muestras.clear();
                for (int i = 0; i < valores.size(); i++) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(ahora);
                    calendar.add(Calendar.SECOND, (valores.size() - i) * -5);
                    Muestra muestra = new Muestra(valores.get(i), calendar.getTime());
                    actualizarDatos(muestra);
                    repaint();
                }
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

        int areaGraficaAncho = width - MARGEN_IZQ - MARGEN_DER;
        int areaGraficaAlto = height - MARGEN_SUP - MARGEN_INF;

        if (primeraMuestra != null && ultimaMuestra != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(primeraMuestra);

            // Ajustar al primer intervalo múltiplo
            int intervalo = calcularRangoX();
            int segundos = cal.get(Calendar.SECOND);
            cal.add(Calendar.SECOND, -(segundos % intervalo));

            // Dibujar marcas principales
            while (cal.getTime().before(ultimaMuestra)) {
                Date marca = cal.getTime();
                int posicionX = mapTiempoAPosicion(marca.getTime());

                // Seleccionar formato según intervalo
                String texto = (intervalo < 60) ? FORMATO_FECHA_LARGO.format(marca) : FORMATO_FECHA_CORTO.format(marca);

                // Dibujar línea y texto
                if (posicionX >= MARGEN_IZQ && posicionX <= MARGEN_IZQ + areaGraficaAncho) {
                    g2d.setColor(new Color(70, 129, 137));
                    g2d.drawLine(posicionX, height - MARGEN_INF, posicionX, height - MARGEN_INF + 5);
                    g2d.drawString(texto, posicionX - 20, height - MARGEN_INF + 20);
                }

                cal.add(Calendar.SECOND, intervalo);
            }
        }

        calcularRangoY();

        // Dibujar líneas horizontales y etiquetas del eje Y
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.setStroke(new BasicStroke(0.5f));

        // Calcular valores dinámicos para el eje Y
        double[] valoresY = new double[6];
        double paso = (precioMaximoActual - precioMinimoActual) / 5;

        // La segunda línea desde arriba será el máximo real
        for(int i = 0; i <= 5; i++) {
            valoresY[i] = precioMinimoActual + (paso * i);
        }

        // Dibujar líneas de referencia
        for (double valor : valoresY) {
            double porcentaje = (valor - precioMinimoActual) / (precioMaximoActual - precioMinimoActual);
            int y = height - MARGEN_INF - (int)(porcentaje * areaGraficaAlto);

            g2d.setColor(new Color(70, 129, 137));
            g2d.drawLine(MARGEN_IZQ, y, width - MARGEN_DER, y);

            // Etiquetas del eje Y
            g2d.setColor(new Color(244, 233, 205));
            String textoValor = FORMATO_NUMERO.format(valor);
            FontMetrics fm = g2d.getFontMetrics();
            g2d.drawString(textoValor, MARGEN_IZQ - fm.stringWidth(textoValor) - 5, y + 5);
        }

        // Dibujar la línea del gráfico
        if (muestras.size() > 1) {
            if (muestras.getLast().getPrecio() >= muestras.getFirst().getPrecio()) {
                g2d.setColor(new Color(50, 205, 50)); // Verde
            } else {
                g2d.setColor(new Color(205, 50, 50)); // Verde
            }
            g2d.setStroke(new BasicStroke(2.0f));

            int[] xPuntos = new int[muestras.size()];
            int[] yPuntos = new int[muestras.size()];

            // Calcular las coordenadas de cada punto
            for (int i = 0; i < muestras.size(); i++) {
                // Posición X basada en el índice
                xPuntos[i] = MARGEN_IZQ + (i * areaGraficaAncho) / (muestras.size() - 1);

                // Posición Y basada en el precio
                double porcentajePrecio = (muestras.get(i).getPrecio() - precioMinimoActual) / (precioMaximoActual - precioMinimoActual);
                yPuntos[i] = height - MARGEN_INF - (int)(porcentajePrecio * areaGraficaAlto);
            }

            // Dibujar segmentos de línea
            for (int i = 0; i < muestras.size() - 1; i++) {
                g2d.drawLine(xPuntos[i], yPuntos[i], xPuntos[i + 1], yPuntos[i + 1]);
            }

            // Dibujar punto final (actual)
            g2d.fillOval(xPuntos[xPuntos.length - 1] - 4, yPuntos[yPuntos.length - 1] - 4, 8, 8);
        }
    }
}
