package fr.petitl.antichamber.triggers;

import fr.petitl.antichamber.triggers.save.Gun;
import fr.petitl.antichamber.triggers.save.MapEntry;
import fr.petitl.antichamber.triggers.save.PinkCube;
import fr.petitl.antichamber.triggers.save.Sign;

/**
 *
 */
public enum TriggerType {
    GUN(Gun.values().length, "Gun"), //
    SIGN(Sign.values().length, "Sign"), //
    PINK_CUBE(PinkCube.values().length, "Pink Cube"), //
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
