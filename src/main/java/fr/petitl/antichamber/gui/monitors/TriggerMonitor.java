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
import fr.petitl.antichamber.gui.panels.TriggerTrackerPanel;
import fr.petitl.antichamber.triggers.GameStatus;

import javax.swing.*;
import java.awt.*;

/**
 *
 */
public class TriggerMonitor extends JFrame implements MonitorFrame {
    private TriggerTrackerPanel triggerTrackerPanel = new TriggerTrackerPanel();
    public TriggerMonitor() {
        super("Trigger Monitor");
        setContentPane(triggerTrackerPanel.getContent());
        setBackground(Color.BLACK);
        setSize(500, 200);
    }

    @Override
    public JFrame getFrame() {
        return this;
    }

    @Override
    public void update(GameStatus status) {
        triggerTrackerPanel.getModel().setStart(status.getStartTimestamp());
        triggerTrackerPanel.getModel().setTriggers(status.getDetectedTriggers());
    }
}
