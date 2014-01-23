package fr.petitl.antichamber.triggers.watchers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import fr.petitl.antichamber.triggers.save.*;

/**
 *
 */
public class SaveFileWatcher extends FileWatcher<AntichamberSave> {

    private SaveChangeListener listener;
    private Set<PinkCube> oldPinkCubes = EnumSet.noneOf(PinkCube.class);
    private Set<Sign> oldSigns = EnumSet.noneOf(Sign.class);
    private Set<Gun> oldGuns = EnumSet.noneOf(Gun.class);
    private Set<MapEntry> oldMapEntries = EnumSet.noneOf(MapEntry.class);

    public SaveFileWatcher(AntichamberSave save, SaveChangeListener listener) throws IOException {
        super(save);
        this.listener = listener;
    }

    protected void fileUpdated(long l) {
        Set<Sign> signs;
        Set<PinkCube> pinkCubes;
        Set<Gun> guns;
        EnumSet<MapEntry> mapEntries;

        synchronized (save) {
            if (save.getPlayTime() == 0) {
                listener.saveReset(l);
                return;
            }

            signs = EnumSet.copyOf(save.getSigns());
            signs.removeAll(oldSigns);
            pinkCubes = EnumSet.copyOf(save.getPinkCubes());
            pinkCubes.removeAll(oldPinkCubes);
            guns = EnumSet.copyOf(save.getGuns());
            guns.removeAll(oldGuns);
            mapEntries = EnumSet.copyOf(save.getMapEntries());
            mapEntries.removeAll(oldMapEntries);

//
//            if(!save.getMapEntries().equals(oldMapEntries) || !save.getSigns().equals(oldSigns)) {
//                try {
//                    FileWriter fw = new FileWriter(new File("bidule.txt"), true);
//                    fw.write(save.getSigns().size() + "\r\n-\r\n");
//                    for (MapEntry s : mapEntries) {
//                        fw.write(s + "\r\n");
//                    }
//                    fw.write("\r\n\r\n");
//                    fw.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
            saveOldState();
        }
        // release the lock asap
        for (Sign sign : signs) {
            listener.newSign(sign, oldSigns.size(), l);
        }
        for (PinkCube pinkCube : pinkCubes) {
            listener.newSecret(pinkCube, oldPinkCubes.size(), l);
        }
        for (Gun gun : guns) {
            listener.newGun(gun, oldGuns.size(), l);
        }
        for (MapEntry mapEntry : mapEntries) {
            listener.newMapEntry(mapEntry, oldMapEntries.size(), l);
        }
    }

    private void saveOldState() {
        oldPinkCubes.clear();
        oldPinkCubes.addAll(save.getPinkCubes());
        oldSigns.clear();
        oldSigns.addAll(save.getSigns());
        oldGuns.clear();
        oldGuns.addAll(save.getGuns());
        oldMapEntries.clear();
        oldMapEntries.addAll(save.getMapEntries());
    }

}
