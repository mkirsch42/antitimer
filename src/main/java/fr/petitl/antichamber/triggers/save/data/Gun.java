package fr.petitl.antichamber.triggers.save.data;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public enum Gun {
    BLUE(0), GREEN(1), YELLOW(8), RED(3)
    ;
    private static final String PREFIX = "HazardIGFChinaSplit.TheWorld:PersistentLevel.HazardPickupFactory";
    private static Map<Integer, Gun> valueMap = new HashMap<>();

    static {
        for (Gun gun : Gun.values())
            valueMap.put(gun.id, gun);
    }

    private final int id;

    private Gun(int id) {
        this.id = id;
    }

    public static Gun fromString(String name) {
        String[] split = name.split("_");
        Gun gun = valueMap.get(Integer.parseInt(split[1]));
        if(gun == null || !split[0].equals(PREFIX))
            throw new IllegalArgumentException("Unknown gun "+name);
        return gun;
    }

    @Override
    public String toString() {
        String s = name().toLowerCase();
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}
