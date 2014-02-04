/*
 * Copyright 2014 Loic Petit
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.petitl.antichamber.gui.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 *
 */
public class ResizableLabel extends JLabel {
    public static final int MIN_FONT_SIZE = 3;
    public static final int MAX_FONT_SIZE = 240;
    private Graphics g;

    public ResizableLabel() {
        init();
    }

    protected void init() {
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                adaptLabelFont(ResizableLabel.this);
            }
        });
    }

    protected void adaptLabelFont(JLabel l) {
        if (g == null) {
            return;
        }
        Rectangle r = l.getBounds();
        int span = MAX_FONT_SIZE - MIN_FONT_SIZE;
        double fontSize = span / 2.0 + MIN_FONT_SIZE;
        int ratio = 4;
        Font f = l.getFont();

        Rectangle r1 = new Rectangle();
        Rectangle r2 = new Rectangle();
        // guaranteed convergence in 8 passes
        while (ratio < 256) {
            r1.setSize(getTextSize(l, f.deriveFont(f.getStyle(), (float) fontSize)));
            r2.setSize(getTextSize(l, f.deriveFont(f.getStyle(), (float) (fontSize + 1))));
            boolean lower = r.contains(r1);
            if (lower && !r.contains(r2)) {
                break;
            }
            if (lower) {
                fontSize = fontSize + ((double) span) / ratio;
            } else {
                fontSize = fontSize - ((double) span) / ratio;
            }
            ratio *= 2;
        }

        setFont(f.deriveFont(f.getStyle(), (float) fontSize));
        repaint();
    }

    private Dimension getTextSize(JLabel l, Font f) {
        Dimension size = new Dimension();
        g.setFont(f);
        FontMetrics fm = g.getFontMetrics(f);
        size.width = (int) (fm.stringWidth(l.getText()) * 1.1);
        size.height = fm.getHeight();

        return size;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.g = g;
    }
}
