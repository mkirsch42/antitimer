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

package fr.petitl.antichamber.triggers;

import fr.petitl.antichamber.triggers.save.data.Gun;
import fr.petitl.antichamber.triggers.save.data.MapEntry;
import fr.petitl.antichamber.triggers.save.data.PinkCube;
import fr.petitl.antichamber.triggers.save.data.Sign;

/**
 *
 */
public enum TriggerType {
    GUN(Gun.values().length, "Gun"), //
    SIGN(Sign.values().length, "Sign"), //
    PINK_CUBE(13, "Pink Cube"), //
    MAP_UPDATE(MapEntry.values().length, "Map Entries"), // To correct...
    FINISH_GAME(1, "Credits");

    private int maxInstances;
    private boolean displayable;
    private String displayName;

    TriggerType(int maxInstances, String displayName) {
        this(maxInstances, true);
        this.displayName = displayName;
    }

    TriggerType(int maxInstances, boolean displayable) {
        this.maxInstances = maxInstances;
        this.displayable = displayable;
        displayName = name();
    }

    public boolean isDisplayable() {
        return displayable;
    }

    public int getMaxInstances() {
        return maxInstances;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
