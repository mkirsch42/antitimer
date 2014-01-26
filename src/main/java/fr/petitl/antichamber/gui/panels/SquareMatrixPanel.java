package fr.petitl.antichamber.gui.panels;

import javax.swing.*;
import java.awt.*;

/**
 *
 */
public class SquareMatrixPanel extends JPanel {
    private static final int BORDER = 3;
    private int cols;
    private int rows;
    private int max;
    private int softMax;
    private Color squareColor;

    private boolean[] values;
    private Color defaultColor;

    public SquareMatrixPanel(int cols, int rows, int softMax, Color squareColor) {
        this.cols = cols;
        this.rows = rows;
        this.softMax = softMax;
        this.squareColor = squareColor;
        this.max = cols * rows;
        values = new boolean[this.max];
        defaultColor = Color.BLACK;

        //
        for (int i = 0, valuesLength = values.length; i < valuesLength; i++) {
            values[i] = Math.random() < 0.5;
        }
    }

    public void clear() {
        for (int i = 0, valuesLength = values.length; i < valuesLength; i++) {
            values[i] = false;
        }
        repaint();
    }

    public void add(int element) {
        if (element < max)
            values[element] = true;
    }

    @Override
    public void paint(Graphics g) {
        int width = getWidth();
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        ((Graphics2D) g).setRenderingHints(rh);
        g.setColor(defaultColor);
        g.fillRect(0, 0, width, getHeight());
        double squareSize = (width) * 2.0 / (3 * cols);
        //setPreferredSize(new Dimension(width, width * rows / cols));
        //setSize(new Dimension(width, width * rows / cols));
        int squareInt = (int) Math.round(squareSize);
        int r = squareInt / 4;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int idx = i * cols + j;
                if(idx >= softMax && !values[idx])
                    continue;
                g.setColor(Color.BLACK);
                int x = (int) Math.round((squareSize + j * 6 * squareSize) / 4);
                int y = (int) Math.round((squareSize + i * 6 * squareSize) / 4);
                g.fillRoundRect(x, y, squareInt, squareInt, r + BORDER * 2, r + BORDER * 2);
                g.setColor(values[idx] ? squareColor : Color.WHITE);
                g.fillRoundRect(x + BORDER, y + BORDER, squareInt - BORDER * 2, squareInt - BORDER * 2, r, r);
            }
        }
    }
}
