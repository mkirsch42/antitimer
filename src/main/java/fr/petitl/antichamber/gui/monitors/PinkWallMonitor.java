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
import fr.petitl.antichamber.triggers.save.data.PinkCube;
import fr.petitl.antichamber.triggers.save.data.Sign;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

/**
 *
 */
public class PinkWallMonitor extends JFrame implements MonitorFrame {
    private SquareMatrixPanel pinkWall = new SquareMatrixPanel(5, 3, TriggerType.PINK_CUBE.getMaxInstances(), new Color(254, 65, 209));

    public PinkWallMonitor() {
        super("Pink Wall");
        setContentPane(pinkWall);
        setSize(200, 150);
    }

    @Override
    public JFrame getFrame() {
        return this;
    }

    @Override
    public void update(GameStatus status) {
        pinkWall.clear();
        Set<PinkCube> pinkCubes = status.getPinkCubes();
        for (PinkCube pink : pinkCubes) {
            pinkWall.add(pink.ordinal());
        }
    }
}
