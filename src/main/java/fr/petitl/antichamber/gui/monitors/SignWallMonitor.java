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
import fr.petitl.antichamber.gui.panels.SquareMatrixPanel;
import fr.petitl.antichamber.triggers.GameStatus;
import fr.petitl.antichamber.triggers.TriggerType;
import fr.petitl.antichamber.triggers.save.data.Sign;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

/**
 *
 */
public class SignWallMonitor extends JFrame implements MonitorFrame {
    private SquareMatrixPanel signWall = new SquareMatrixPanel(15, 8, TriggerType.SIGN.getMaxInstances(), new Color(51, 102, 255));

    public SignWallMonitor() {
        super("Sign Wall");
        add(signWall, BorderLayout.CENTER);
        setSize(650, 375);
    }

    @Override
    public JFrame getFrame() {
        return this;
    }

    @Override
    public void update(GameStatus status) {
        signWall.clear();
        Set<Sign> signs = status.getSigns();
        for (Sign sign : signs) {
            signWall.add(sign.ordinal());
        }
    }
}
