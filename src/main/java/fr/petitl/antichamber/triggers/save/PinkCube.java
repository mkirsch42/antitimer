package fr.petitl.antichamber.triggers.save;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public enum PinkCube {
    HIDDEN_BLUE("HazardIGFChinaSplit.TheWorld:PersistentLevel.HazardSecretTile_2", "Blue Gun"), //
    YELLOW("HazardLeap.TheWorld:PersistentLevel.HazardSecretTile_19", "Green Cross"), //
    RESOURCES("HazardIGFChinaSplit.TheWorld:PersistentLevel.HazardSecretTile_13", "Managing Resources"), //
    BSOD("HazardIGFChinaSplit.TheWorld:PersistentLevel.HazardSecretTile_11", "Blue Wall"), //
    STH_ACCESS("HazardIGFChinaSplit.TheWorld:PersistentLevel.HazardSecretTile_3", "STH Access"), //
    FLY("HazardIGFChinaSplit.TheWorld:PersistentLevel.HazardSecretTile_1", "Fly"), //
    DEJA_VU("HazardLeap.TheWorld:PersistentLevel.HazardSecretTile_6", "Déjà Vu"), //
    STH_LASERS("HazardIGFChinaSplit.TheWorld:PersistentLevel.HazardSecretTile_4", "STH Fall"), //
    STH_ELEVATOR("HazardStairway.TheWorld:PersistentLevel.HazardSecretTile_6", "STH Small Elevator"), //
    STH_CORRIDOR("HazardStairway.TheWorld:PersistentLevel.HazardSecretTile_17", "Yellow Room"), //
    CHASE("HazardEndGame.TheWorld:PersistentLevel.HazardSecretTile_5", "Chase"), //
    MULTICOLOR("HazardLeap.TheWorld:PersistentLevel.HazardSecretTile_7", "Multi-Color Windows"), //
    GALLERY("HazardIGFChinaSplit.TheWorld:PersistentLevel.HazardSecretTile_16", "Gallery"); //

    //OOB("HazardSeamless.TheWorld:PersistentLevel.HazardSecretTile_15", "Out of Bounds"), //
    //GLITCHED_GALLERY("HazardIGFChinaSplit.TheWorld:PersistentLevel.HazardSecretTile_0", "Glitched Gallery"), //

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
        return valueMap.get(name);
    }

    @Override
    public String toString() {
        return label;
    }
}
