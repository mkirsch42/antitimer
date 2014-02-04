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
import fr.petitl.antichamber.triggers.TriggerType;

import javax.swing.*;
import java.awt.*;

/**
 *
 */
public class MapCompletionMonitor extends JFrame implements MonitorFrame {
    private ResizableLabel label = new ResizableLabel();

    public MapCompletionMonitor() {
        super("Map Completion");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        getFrame().setBackground(Color.BLACK);
        getFrame().getContentPane().setBackground(Color.BLACK);
        label.setForeground(Color.WHITE);
        add(label, BorderLayout.CENTER);
        setSize(200, 120);
    }

    @Override
    public JFrame getFrame() {
        return this;
    }

    @Override
    public void update(GameStatus status) {
        label.setText(Math.floor(status.getMapEntries().size() * 1000.0 / TriggerType.MAP_UPDATE.getMaxInstances()) / 10 + "%");
    }
}
