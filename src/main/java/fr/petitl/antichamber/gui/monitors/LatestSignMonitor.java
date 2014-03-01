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

package fr.petitl.antichamber.gui.monitors;

import fr.petitl.antichamber.gui.MonitorFrame;
import fr.petitl.antichamber.gui.panels.ResizableLabel;
import fr.petitl.antichamber.triggers.GameStatus;
import fr.petitl.antichamber.triggers.save.data.Gun;
import fr.petitl.antichamber.triggers.save.data.Sign;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.Set;

/**
 *
 */
public class LatestSignMonitor extends JFrame implements MonitorFrame {
    private ResizableLabel label = new ResizableLabel();

    public LatestSignMonitor() {
        super("Latest Sign");
        label.setVerticalAlignment(SwingConstants.TOP);
        label.setForeground(Color.WHITE);
        label.setFontSizeText("120/120  ");
        label.setFontSizeRatio(5);
        getContentPane().setLayout(new BorderLayout());
        add(label, BorderLayout.CENTER);
        getFrame().setBackground(Color.BLACK);
        getFrame().getContentPane().setBackground(Color.BLACK);
        setSize(600, 100);
    }

    @Override
    public JFrame getFrame() {
        return this;
    }

    @Override
    public void update(GameStatus status) {
        Sign s = status.getLatestSign();

        label.setText("<html>&nbsp;&nbsp;" + status.getSigns().size() + "/120&nbsp;&nbsp;&nbsp;#" + (s.ordinal() + 1) + "&nbsp;&nbsp;&nbsp;&nbsp;" + s.getLabel() + "</html>");
    }

    public static void main(String[] args) {
        LatestSignMonitor l = new LatestSignMonitor();
        l.label.setText("<html>&nbsp;" + 92 + "/120&nbsp;&nbsp;&nbsp;#" + (32 + 1) + "&nbsp;&nbsp;&nbsp;&nbsp;If you lose sight of what's important, it may not be there when you need it.</html>");

        l.getFrame().setVisible(true);
    }
}
