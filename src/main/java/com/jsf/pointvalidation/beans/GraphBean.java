package com.jsf.pointvalidation.beans;

import com.jsf.pointvalidation.entity.PointResult;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;

@ManagedBean(name = "graphBean")
@RequestScoped
public class GraphBean implements Serializable {

    @ManagedProperty(value = "#{resultsBean}")
    private ResultsBean resultsBean;

    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;
    private static final int PADDING = 40;
    private static final int CENTER_X = WIDTH / 2;
    private static final int CENTER_Y = HEIGHT / 2;

    public void drawGraph() throws IOException {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, WIDTH, HEIGHT);

        double r = resultsBean.getR();
        drawArea(g2d, r);
        drawAxes(g2d);
        drawPoints(g2d, r);

        g2d.dispose();

        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();

        response.setContentType("image/png");
        ImageIO.write(image, "png", response.getOutputStream());

        context.responseComplete();
    }

    private void drawArea(Graphics2D g2d, double r) {
        int scale = (int) ((WIDTH - 2 * PADDING) / (2 * r));

        g2d.setColor(new Color(100, 149, 237, 100));

        // Прямоугольник в 1-й четверти (x: 0 до R/2, y: 0 до R)
        int rectWidth = (int) (r / 2 * scale);
        int rectHeight = (int) (r * scale);
        g2d.fillRect(CENTER_X, CENTER_Y - rectHeight, rectWidth, rectHeight);

        // Четверть круга во 2-й четверти (радиус R/2)
        int arcDiameter = (int) (r * scale);
        Arc2D arc = new Arc2D.Double(
                CENTER_X - arcDiameter, CENTER_Y - arcDiameter,
                arcDiameter, arcDiameter,
                90, 90, Arc2D.PIE);
        g2d.fill(arc);

        // Треугольник в 4-й четверти
        Path2D triangle = new Path2D.Double();
        triangle.moveTo(CENTER_X, CENTER_Y);
        triangle.lineTo(CENTER_X + r / 2 * scale, CENTER_Y);
        triangle.lineTo(CENTER_X, CENTER_Y + r / 2 * scale);
        triangle.closePath();
        g2d.fill(triangle);
    }

    private void drawAxes(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));

        // Оси X и Y
        g2d.drawLine(PADDING, CENTER_Y, WIDTH - PADDING, CENTER_Y);
        g2d.drawLine(CENTER_X, PADDING, CENTER_X, HEIGHT - PADDING);

        // Стрелки
        g2d.fillPolygon(new int[]{WIDTH - PADDING, WIDTH - PADDING - 10, WIDTH - PADDING - 10},
                new int[]{CENTER_Y, CENTER_Y - 5, CENTER_Y + 5}, 3);
        g2d.fillPolygon(new int[]{CENTER_X, CENTER_X - 5, CENTER_X + 5},
                new int[]{PADDING, PADDING + 10, PADDING + 10}, 3);

        // Метки осей
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.drawString("X", WIDTH - PADDING + 10, CENTER_Y + 5);
        g2d.drawString("Y", CENTER_X + 5, PADDING - 10);
    }

    private void drawPoints(Graphics2D g2d, double currentR) {
        int scale = (int) ((WIDTH - 2 * PADDING) / (2 * currentR));

        for (PointResult result : resultsBean.getResults()) {
            int px = CENTER_X + (int) (result.getX() * scale);
            int py = CENTER_Y - (int) (result.getY() * scale);

            if (result.isHit()) {
                g2d.setColor(new Color(0, 200, 0));
            } else {
                g2d.setColor(new Color(200, 0, 0));
            }

            g2d.fillOval(px - 4, py - 4, 8, 8);
        }
    }

    public void setResultsBean(ResultsBean resultsBean) {
        this.resultsBean = resultsBean;
    }
}
