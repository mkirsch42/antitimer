package fr.petitl.antichamber.triggers.save.data;

import java.util.*;

/**
 *
 */
public enum MapEntry {
    ENTRY_8_0(8, 0, Type.ARROW, Orientation.TOP),
    ENTRY_9_0(9, 0, Type.ARROW, Orientation.TOP),
    ENTRY_13_0(13, 0, Type.ROOM),
    ENTRY_14_0(14, 0, Type.ARROW, Orientation.RIGHT),
    ENTRY_5_1(5, 1, Type.ARROW, Orientation.LEFT),
    ENTRY_6_1(6, 1, Type.ROOM),
    ENTRY_7_1(7, 1, Type.ARROW, Orientation.LEFT),
    ENTRY_8_1(8, 1, Type.ROOM),
    ENTRY_9_1(9, 1, Type.ROOM),
    ENTRY_10_1(10, 1, Type.DEADEND, Orientation.RIGHT),
    ENTRY_11_1(11, 1, Type.ARROW, Orientation.LEFT),
    ENTRY_12_1(12, 1, Type.ROOM),
    ENTRY_13_1(13, 1, Type.ROOM),
    ENTRY_14_1(14, 1, Type.ROOM),
    ENTRY_15_1(15, 1, Type.ARROW, Orientation.RIGHT),
    ENTRY_3_2(3, 2, Type.DEADEND, Orientation.TOP),
    ENTRY_4_2(4, 2, Type.ARROW, Orientation.TOP),
    ENTRY_5_2(5, 2, Type.ARROW, Orientation.TOP),
    ENTRY_6_2(6, 2, Type.ROOM),
    ENTRY_7_2(7, 2, Type.ARROW, Orientation.LEFT),
    ENTRY_8_2(8, 2, Type.ROOM),
    ENTRY_9_2(9, 2, Type.PATH, Orientation.LEFT, Orientation.BOTTOM),
    ENTRY_12_2(12, 2, Type.DEADEND, Orientation.BOTTOM),
    ENTRY_13_2(13, 2, Type.ROOM),
    ENTRY_14_2(14, 2, Type.DEADEND, Orientation.RIGHT),
    ENTRY_15_2(15, 2, Type.ROOM),
    ENTRY_16_2(16, 2, Type.ARROW, Orientation.RIGHT),
    ENTRY_2_3(2, 3, Type.ROOM),
    ENTRY_3_3(3, 3, Type.ROOM, Orientation.RIGHT),
    ENTRY_4_3(4, 3, Type.ROOM, Orientation.LEFT, Orientation.RIGHT),
    ENTRY_5_3(5, 3, Type.ROOM, Orientation.LEFT),
    ENTRY_6_3(6, 3, Type.ROOM),
    ENTRY_7_3(7, 3, Type.PATH, Orientation.LEFT, Orientation.RIGHT),
    ENTRY_8_3(8, 3, Type.PATH, Orientation.TOP, Orientation.LEFT, Orientation.BOTTOM),
    ENTRY_9_3(9, 3, Type.PATH, Orientation.TOP, Orientation.RIGHT, Orientation.BOTTOM),
    ENTRY_10_3(10, 3, Type.PATH, Orientation.LEFT, Orientation.RIGHT),
    ENTRY_11_3(11, 3, Type.PATH, Orientation.LEFT, Orientation.RIGHT, Orientation.BOTTOM),
    ENTRY_12_3(12, 3, Type.PATH, Orientation.LEFT, Orientation.RIGHT),
    ENTRY_13_3(13, 3, Type.ROOM),
    ENTRY_14_3(14, 3, Type.PATH, Orientation.LEFT, Orientation.RIGHT),
    ENTRY_15_3(15, 3, Type.ROOM),
    ENTRY_0_4(0, 4, Type.DEADEND, Orientation.LEFT),
    ENTRY_1_4(1, 4, Type.ROOM),
    ENTRY_2_4(2, 4, Type.ROOM),
    ENTRY_3_4(3, 4, Type.ROOM),
    ENTRY_4_4(4, 4, Type.ROOM),
    ENTRY_5_4(5, 4, Type.ARROW, Orientation.TOP),
    ENTRY_6_4(6, 4, Type.PATH, Orientation.TOP, Orientation.BOTTOM),
    ENTRY_7_4(7, 4, Type.ROOM),
    ENTRY_8_4(8, 4, Type.ROOM),
    ENTRY_9_4(9, 4, Type.ROOM),
    ENTRY_11_4(11, 4, Type.ARROW, Orientation.BOTTOM),
    ENTRY_13_4(13, 4, Type.ROOM),
    ENTRY_14_4(14, 4, Type.ROOM, Orientation.RIGHT),
    ENTRY_15_4(15, 4, Type.ROOM, Orientation.LEFT),
    ENTRY_16_4(16, 4, Type.ARROW, Orientation.RIGHT),
    ENTRY_1_5(1, 5, Type.ARROW, Orientation.BOTTOM),
    ENTRY_2_5(2, 5, Type.ARROW, Orientation.BOTTOM),
    ENTRY_3_5(3, 5, Type.DEADEND, Orientation.BOTTOM),
    ENTRY_4_5(4, 5, Type.ROOM),
    ENTRY_5_5(5, 5, Type.ROOM),
    ENTRY_6_5(6, 5, Type.PATH, Orientation.TOP, Orientation.BOTTOM),
    ENTRY_7_5(7, 5, Type.ARROW, Orientation.BOTTOM),
    ENTRY_8_5(8, 5, Type.DEADEND, Orientation.LEFT),
    ENTRY_9_5(9, 5, Type.ROOM, Orientation.BOTTOM),
    ENTRY_10_5(10, 5, Type.ARROW, Orientation.RIGHT),
    ENTRY_13_5(13, 5, Type.ROOM),
    ENTRY_14_5(14, 5, Type.ARROW, Orientation.BOTTOM),
    ENTRY_1_6(1, 6, Type.ARROW, Orientation.LEFT),
    ENTRY_2_6(2, 6, Type.ROOM),
    ENTRY_3_6(3, 6, Type.ROOM, Orientation.BOTTOM),
    ENTRY_4_6(4, 6, Type.ROOM),
    ENTRY_5_6(5, 6, Type.PATH, Orientation.LEFT, Orientation.RIGHT),
    ENTRY_6_6(6, 6, Type.ROOM),
    ENTRY_7_6(7, 6, Type.ROOM),
    ENTRY_8_6(8, 6, Type.ROOM),
    ENTRY_9_6(9, 6, Type.ROOM, Orientation.TOP),
    ENTRY_10_6(10, 6, Type.DEADEND, Orientation.RIGHT),
    ENTRY_12_6(12, 6, Type.DEADEND, Orientation.LEFT),
    ENTRY_13_6(13, 6, Type.ROOM),
    ENTRY_14_6(14, 6, Type.ARROW, Orientation.TOP),
    ENTRY_15_6(15, 6, Type.ARROW, Orientation.TOP),
    ENTRY_1_7(1, 7, Type.DEADEND, Orientation.TOP),
    ENTRY_2_7(2, 7, Type.ARROW, Orientation.LEFT),
    ENTRY_3_7(3, 7, Type.ROOM, Orientation.TOP),
    ENTRY_4_7(4, 7, Type.ROOM),
    ENTRY_6_7(6, 7, Type.PATH, Orientation.TOP, Orientation.BOTTOM),
    ENTRY_12_7(12, 7, Type.ARROW, Orientation.LEFT),
    ENTRY_13_7(13, 7, Type.ROOM),
    ENTRY_14_7(14, 7, Type.ROOM),
    ENTRY_15_7(15, 7, Type.ROOM),
    ENTRY_16_7(16, 7, Type.ROOM),
    ENTRY_17_7(17, 7, Type.ARROW, Orientation.RIGHT),
    ENTRY_0_8(0, 8, Type.ARROW, Orientation.LEFT),
    ENTRY_1_8(1, 8, Type.ROOM),
    ENTRY_2_8(2, 8, Type.DEADEND, Orientation.RIGHT),
    ENTRY_3_8(3, 8, Type.ARROW, Orientation.LEFT),
    ENTRY_4_8(4, 8, Type.ROOM),
    ENTRY_5_8(5, 8, Type.ROOM),
    ENTRY_6_8(6, 8, Type.PATH, Orientation.TOP, Orientation.BOTTOM),
    ENTRY_8_8(8, 8, Type.DEADEND, Orientation.TOP),
    ENTRY_10_8(10, 8, Type.ARROW, Orientation.TOP),
    ENTRY_13_8(13, 8, Type.PATH, Orientation.TOP, Orientation.RIGHT),
    ENTRY_14_8(14, 8, Type.ROOM),
    ENTRY_16_8(16, 8, Type.ROOM),
    ENTRY_17_8(17, 8, Type.ARROW, Orientation.RIGHT),
    ENTRY_1_9(1, 9, Type.ROOM),
    ENTRY_2_9(2, 9, Type.ROOM),
    ENTRY_3_9(3, 9, Type.ROOM),
    ENTRY_4_9(4, 9, Type.ROOM),
    ENTRY_5_9(5, 9, Type.ROOM, Orientation.RIGHT),
    ENTRY_6_9(6, 9, Type.ROOM, Orientation.LEFT),
    ENTRY_7_9(7, 9, Type.PATH, Orientation.LEFT, Orientation.RIGHT, Orientation.BOTTOM),
    ENTRY_8_9(8, 9, Type.ROOM, Orientation.BOTTOM),
    ENTRY_9_9(9, 9, Type.DEADEND, Orientation.LEFT),
    ENTRY_10_9(10, 9, Type.ROOM),
    ENTRY_11_9(11, 9, Type.PATH, Orientation.LEFT, Orientation.BOTTOM),
    ENTRY_14_9(14, 9, Type.ROOM),
    ENTRY_15_9(15, 9, Type.ROOM),
    ENTRY_16_9(16, 9, Type.ROOM),
    ENTRY_1_10(1, 10, Type.ARROW, Orientation.BOTTOM),
    ENTRY_2_10(2, 10, Type.ARROW, Orientation.BOTTOM),
    ENTRY_3_10(3, 10, Type.ROOM),
    ENTRY_6_10(6, 10, Type.DEADEND, Orientation.LEFT),
    ENTRY_7_10(7, 10, Type.ROOM),
    ENTRY_8_10(8, 10, Type.ROOM, Orientation.TOP),
    ENTRY_9_10(9, 10, Type.ROOM),
    ENTRY_10_10(10, 10, Type.ROOM),
    ENTRY_11_10(11, 10, Type.ROOM),
    ENTRY_12_10(12, 10, Type.ARROW, Orientation.RIGHT),
    ENTRY_14_10(14, 10, Type.ROOM),
    ENTRY_15_10(15, 10, Type.PATH, Orientation.TOP, Orientation.LEFT),
    ENTRY_16_10(16, 10, Type.ARROW, Orientation.BOTTOM),
    ENTRY_3_11(3, 11, Type.ARROW, Orientation.BOTTOM),
    ENTRY_9_11(9, 11, Type.DEADEND, Orientation.LEFT),
    ENTRY_10_11(10, 11, Type.ROOM),
    ENTRY_14_11(14, 11, Type.ARROW, Orientation.BOTTOM),;

    private final int x;
    private final int y;
    private final Type type;
    private Set<Orientation> orientations;
    private Orientation mainOrientation;

    private static Map<Coord, MapEntry> coordMap = new HashMap<>();

    static {
        for (MapEntry entry : values()) {
            coordMap.put(new Coord(entry.x, entry.y), entry);
        }
    }

    MapEntry(int x, int y, Type type, Orientation... arg) {
        this.x = x;
        this.y = y;
        this.type = type;
        if(arg.length > 0) {
            mainOrientation = arg[0];
            orientations = EnumSet.copyOf(Arrays.asList(arg));
        } else {
            orientations = EnumSet.noneOf(Orientation.class);
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Type getType() {
        return type;
    }

    public Set<Orientation> getOrientations() {
        return orientations;
    }

    public Orientation getMainOrientation() {
        return mainOrientation;
    }

    public static MapEntry getEntryAt(int x, int y) {
        return coordMap.get(new Coord(x, y));
    }

    private static class Coord {
        private int x;
        private int y;

        private Coord(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Coord coord = (Coord) o;

            if (x != coord.x) return false;
            if (y != coord.y) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }
    }

    /**
     *
     */
    public static enum Orientation {
        TOP,
        LEFT,
        BOTTOM,
        RIGHT;
        public Orientation opposite() {
            switch (this) {
                case TOP:
                    return BOTTOM;
                case LEFT:
                    return RIGHT;
                case BOTTOM:
                    return TOP;
                case RIGHT:
                    return LEFT;
            }
            return null;
        }
    }

    /**
     *
     */
    public static enum Type {
        PATH, DEADEND, ARROW, ROOM
    }
}
