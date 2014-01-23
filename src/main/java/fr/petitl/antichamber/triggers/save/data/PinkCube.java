package fr.petitl.antichamber.triggers.save.data;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public enum PinkCube {
    HIDDEN_BLUE("HazardIGFChinaSplit.2", "Blue Gun"), //
    YELLOW("HazardLeap.19", "Green Cross"), //
    RESOURCES("HazardIGFChinaSplit.13", "Managing Resources"), //
    BSOD("HazardIGFChinaSplit.11", "Blue Wall"), //
    STH_ACCESS("HazardIGFChinaSplit.3", "STH Access"), //
    FLY("HazardIGFChinaSplit.1", "Fly"), //
    DEJA_VU("HazardLeap.6", "Déjà Vu"), //
    STH_LASERS("HazardIGFChinaSplit.4", "STH Fall"), //
    STH_ELEVATOR("HazardStairway.6", "STH Small Elevator"), //
    STH_CORRIDOR("HazardStairway.17", "Yellow Room"), //
    CHASE("HazardEndGame.5", "Chase"), //
    MULTICOLOR("HazardLeap.7", "Multi-Color Windows"), //
    GALLERY("HazardIGFChinaSplit.16", "Gallery"), //
    OOB("HazardSeamless.15", "[extra] Out of Bounds"), //
    GLITCHED_GALLERY("HazardIGFChinaSplit.0", "[extra] Glitched Gallery");

    private final String id;

    private static Map<String, PinkCube> valueMap = new HashMap<>();
    static {
        for (PinkCube pinkCube : PinkCube.values())
            valueMap.put(pinkCube.id, pinkCube);
    }

    private String label;

    PinkCube(String id, String label) {
        this.id = id;
        this.label = label;
    }

    public static PinkCube fromString(String name) {
        name = name.replace("TheWorld:PersistentLevel.HazardSecretTile_", "");
        return valueMap.get(name);
    }

    @Override
    public String toString() {
        return label;
    }
}
