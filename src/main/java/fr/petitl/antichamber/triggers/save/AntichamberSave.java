package fr.petitl.antichamber.triggers.save;

import fr.petitl.antichamber.log.Logger;
import fr.petitl.antichamber.triggers.save.data.*;
import fr.petitl.antichamber.triggers.watchers.FileReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 *
 */
public class AntichamberSave implements FileReader {
    public static final Logger log = Logger.getLogger(AntichamberSave.class);
    private final Set<MapEntry> mapEntries;
    private final Set<Sign> signs;
    private final Set<PinkCube> pinkCubes;
    private final Set<Gun> guns;
    private final File file;
    private float playTime;
    private boolean hiddenSignHints;

    public AntichamberSave(File file) throws IOException {
        guns = EnumSet.noneOf(Gun.class);
        pinkCubes = EnumSet.noneOf(PinkCube.class);
        signs = EnumSet.of(Sign.SIGN_0);
        mapEntries = EnumSet.of(MapEntry.ENTRY_6_3);

        this.file = file;
        if (!file.exists())
            throw new IllegalStateException("Save file doesn't seem to exist - is the path correct?");
    }

    @Override
    public synchronized void read() throws IOException {
        guns.clear();
        pinkCubes.clear();
        signs.clear();
        signs.add(Sign.SIGN_0);
        mapEntries.clear();
        mapEntries.add(MapEntry.ENTRY_6_3);
        playTime = 0;
        hiddenSignHints = false;

        log.debug("Reading file " + file.getAbsolutePath());
        int retry = 3;
        do {
            try (InputStream in = new FileInputStream(file)) {
                readLittleEndian(in, 4);
                readLittleEndian(in, 4);

                String propName = readString(in);
                while (!"None".equals(propName)) {
                    parse(in, propName, readString(in));

                    propName = readString(in);
                }
                break;
            } catch (Exception e) {
                retry--;
            }
        } while (retry > 0);
    }

    private void parse(InputStream in, String propName, String propType) throws IOException {
        switch (propName) {
            case "PlayTime":
                playTime = readFloatProperty(in);
                log.debug("Play time: " + playTime);
                break;
            case "bHiddenSignHints":
                hiddenSignHints = readBoolProperty(in);
                log.debug("Hidden sign: " + hiddenSignHints);
                break;
            case "SavedPickups":
                log.debug("Gun");
                for (String prop : readArrayProperty(in)) {
                    Gun gun = Gun.fromString(prop);
                    guns.add(gun);
                    log.debug("\t" + gun.name());
                }
                break;
            case "SavedSecrets":
                log.debug("Secrets");
                for (String prop : readArrayProperty(in)) {
                    PinkCube s = PinkCube.fromString(prop);
                    if (s == null)
                        continue;
                    pinkCubes.add(s);
                    log.debug("\t" + prop);
                }
                break;
            case "SavedTriggers":
                for (String prop : readArrayProperty(in)) {
                    Trigger trigger = Trigger.fromString(prop);
                    if (trigger == null)
                        continue;
                    if (trigger.getSign() != null) {
                        signs.add(trigger.getSign());
                    }
                    Collections.addAll(mapEntries, trigger.getEntries());
                }
                break;
            case "MapArray":
                log.debug("Map");
                readArrayProperty(in);
                break;
            default:
                log.debug("Skipping unused property " + propName + "[" + propType + "]");
                readProperty(in, propType);
                break;
        }
    }

    private void readProperty(InputStream in, String propType) throws IOException {
        switch (propType) {
            case "BoolProperty":
                readBoolProperty(in);
                break;
            case "FloatProperty":
                readFloatProperty(in);
                break;
            case "ArrayProperty":
                readArrayProperty(in);
                break;
        }
    }

    private int readLittleEndian(InputStream in, int bytes) throws IOException {
        int val = 0;

        for (int i = 0; i < bytes; i++)
            val += in.read() * (1 << i * 8);

        return val;
    }

    private String readString(InputStream in) throws IOException {
        int length = readLittleEndian(in, 4);
        byte[] buff = new byte[length - 1];
        if (in.read(buff) != length - 1 || in.read() < 0) {
            throw new IOException("Illegal state in the save file while trying to read a string (so far: "
                    + new String(buff) + ")");
        }
        return new String(buff);
    }

    private boolean readBoolProperty(InputStream in) throws IOException {
        readLittleEndian(in, 8);
        return readLittleEndian(in, 1) != 0;
    }

    private String[] readArrayProperty(InputStream in) throws IOException {
        readLittleEndian(in, 8);
        int count = readLittleEndian(in, 4);

        String[] elements = new String[count];

        for (int i = 0; i < count; i++)
            elements[i] = readString(in);

        return elements;
    }

    private float readFloatProperty(InputStream in) throws IOException {
        // IEEE754
        readLittleEndian(in, 8);
        int ival = readLittleEndian(in, 4);

        int fraction = ival & ((1 << 23) - 1);
        ival >>= 23;

        int exponent = ival & ((1 << 8) - 1);
        ival >>= 8;

        int sign = ival & 1;

        double val = Math.pow(-1, sign) * (Math.pow(2, exponent - 127) * ((double) fraction / Math.pow(2, 23) + 1));
        return (float) val;
    }

    public Set<Sign> getSigns() {
        return signs;
    }

    public Set<PinkCube> getPinkCubes() {
        return pinkCubes;
    }

    public Set<Gun> getGuns() {
        return guns;
    }

    public Set<MapEntry> getMapEntries() {
        return mapEntries;
    }

    public float getPlayTime() {
        return playTime;
    }

    @Override
    public File getFile() {
        return file;
    }

    public String toString() {
        return "AntichamberSave(Map=" + mapEntries.size() + ", Signs=" + signs.size() + ", Pink=" + pinkCubes.size() + ", Guns=" + guns.size() + ")";
    }
}
