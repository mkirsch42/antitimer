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
import fr.petitl.antichamber.triggers.GameStatus;
import fr.petitl.antichamber.triggers.save.data.MapEntry;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Collection;

/**
 *
 */
public class MapMonitor extends JPanel implements MonitorFrame {
    private static final float PATH_SIZE = 1.0f / 4;
    private static final float DEAD_END_SIZE = 1.0f * 4 / 9;
    private static final float ARROW_FREE_SIZE = 1.0f / 8;
    private static final String TITLE = "Map";
    private final JFrame frame;
    private MapEntry[][] entries = new MapEntry[18][12];

    public MapMonitor() {
        frame = new JFrame(TITLE);
        frame.setContentPane(this);
        frame.setSize(800, 555);
        clear();
    }

    public JFrame getFrame() {
        return frame;
    }

    @Override
    public void update(GameStatus status) {
        setEntries(status.getMapEntries());
    }

    public void clear() {
        for (int x = 0; x < 18; x++) {
            for (int y = 0; y < 12; y++) {
                entries[x][y] = null;
            }
        }
    }

    public void setEntries(Collection<MapEntry> entries) {
        clear();
        for (MapEntry entry : entries) {
            this.entries[entry.getX()][entry.getY()] = entry;
        }
        repaint();
    }

    public void setEntries(MapEntry... entries) {
        setEntries(Arrays.asList(entries));
    }


    @Override
    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.WHITE);
        int size = getWidth() / 18;
        for (int x = 0; x < 18; x++) {
            for (int y = 0; y < 12; y++) {
                MapEntry entry = entries[x][y];
                if (entry == null)
                    continue;
                draw(g, entry, x, y, size, size / 2);
            }
        }
    }

    private void draw(Graphics g, MapEntry entry, int x, int y, int size, int halfSize) {
        int pathSize = (int) (size * PATH_SIZE);
        int offsetX = x * size;
        int offsetY = y * size;
        // center
        int circleSize = (int) (size * DEAD_END_SIZE);
        switch (entry.getType()) {
            case PATH:
                g.fillRect(offsetX + halfSize - pathSize / 2, offsetY + halfSize - pathSize / 2, pathSize, pathSize);
                for (MapEntry.Orientation o : entry.getOrientations()) {
                    drawPath(g, offsetX, offsetY, o, pathSize, halfSize);
                }
                break;
            case DEADEND:
                switch (entry.getMainOrientation()) {
                    case TOP:
                        offsetY += circleSize / 2;
                        break;
                    case LEFT:
                        offsetX += circleSize / 2;
                        break;
                    case BOTTOM:
                        offsetY -= circleSize / 2;
                        break;
                    case RIGHT:
                        offsetX -= circleSize / 2;
                        break;
                }
                drawPath(g, offsetX, offsetY, entry.getMainOrientation().opposite(), pathSize, halfSize);
                g.fillOval(offsetX + halfSize - circleSize / 2, offsetY + halfSize - circleSize / 2, circleSize, circleSize);
                break;
            case ARROW:
                int[] exes = new int[3];
                int[] whies = new int[3];
                exes[2] = offsetX + halfSize;
                whies[2] = offsetY + halfSize;
                switch (entry.getMainOrientation()) {
                    case TOP:
                        exes[0] = (int) (offsetX + size * 2 * ARROW_FREE_SIZE);
                        whies[0] = (int) (offsetY + size * (1 - ARROW_FREE_SIZE));
                        exes[1] = (int) (offsetX + size * (1 - 2 * ARROW_FREE_SIZE));
                        whies[1] = whies[0];
                        offsetY += halfSize / 2;
                        break;
                    case RIGHT:
                        exes[0] = (int) (offsetX + size * ARROW_FREE_SIZE);
                        whies[0] = (int) (offsetY + size * (1 - 2 * ARROW_FREE_SIZE));
                        exes[1] = exes[0];
                        whies[1] = (int) (offsetY + size * 2 * ARROW_FREE_SIZE);
                        offsetX -= halfSize / 2;
                        break;
                    case BOTTOM:
                        exes[0] = (int) (offsetX + size * 2 * ARROW_FREE_SIZE);
                        whies[0] = (int) (offsetY + size * ARROW_FREE_SIZE);
                        exes[1] = (int) (offsetX + size * (1 - 2 * ARROW_FREE_SIZE));
                        whies[1] = whies[0];
                        offsetY -= halfSize / 2;
                        break;
                    case LEFT:
                        exes[0] = (int) (offsetX + size * (1 - ARROW_FREE_SIZE));
                        whies[0] = (int) (offsetY + size * (1 - 2 * ARROW_FREE_SIZE));
                        exes[1] = exes[0];
                        whies[1] = (int) (offsetY + size * 2 * ARROW_FREE_SIZE);
                        offsetX += halfSize / 2;
                        break;
                }
                drawPath(g, offsetX, offsetY, entry.getMainOrientation().opposite(), pathSize, halfSize);

                g.fillPolygon(exes, whies, 3);
                break;
            case ROOM:
                if (hasEntry(x - 1, y, MapEntry.Orientation.LEFT))
                    drawPath(g, offsetX, offsetY, MapEntry.Orientation.LEFT, pathSize, halfSize);
                if (hasEntry(x + 1, y, MapEntry.Orientation.RIGHT))
                    drawPath(g, offsetX, offsetY, MapEntry.Orientation.RIGHT, pathSize, halfSize);
                if (hasEntry(x, y - 1, MapEntry.Orientation.TOP))
                    drawPath(g, offsetX, offsetY, MapEntry.Orientation.TOP, pathSize, halfSize);
                if (hasEntry(x, y + 1, MapEntry.Orientation.BOTTOM))
                    drawPath(g, offsetX, offsetY, MapEntry.Orientation.BOTTOM, pathSize, halfSize);
                g.setColor(Color.BLACK);
                g.fillRect(offsetX + size / 3 - 2, offsetY + size / 3 - 2, size / 3 + 4, size / 3 + 4);
                g.setColor(Color.WHITE);
                g.fillRect(offsetX + size / 3, offsetY + size / 3, size / 3, size / 3);
                break;
        }
    }

    private boolean hasEntry(int x, int y, MapEntry.Orientation o) {
        if (!(x >= 0 && x < 18 && y >= 0 && y < 12)) return false;
        MapEntry entry = entries[x][y];
        if (entry == null) return false;
        if (entry.getType() == MapEntry.Type.ROOM)
            return !entry.getOrientations().contains(o.opposite());
        if (entry.getType() == MapEntry.Type.PATH) {
            return entry.getOrientations().contains(o.opposite());
        }
        return entry.getMainOrientation() == o;
    }

    private void drawPath(Graphics g, int offsetX, int offsetY, MapEntry.Orientation orientation, int pathSize, int halfSize) {
        int width = pathSize;
        int height = pathSize;
        switch (orientation) {
            case TOP:
                offsetX += halfSize - pathSize / 2;
                offsetY -= 1;
                height = halfSize;
                break;
            case LEFT:
                offsetY += halfSize - pathSize / 2;
                offsetX -= 1;
                width = halfSize;
                break;
            case BOTTOM:
                offsetX += halfSize - pathSize / 2;
                offsetY += halfSize + 1;
                height = halfSize;
                break;
            case RIGHT:
                offsetY += halfSize - pathSize / 2;
                offsetX += halfSize + 1;
                width = halfSize;
                break;
        }
        g.fillRect(offsetX, offsetY, width, height);
    }
}
