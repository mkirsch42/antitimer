package fr.petitl.antichamber.triggers.watchers;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Set;

import fr.petitl.antichamber.triggers.save.*;
import fr.petitl.antichamber.triggers.save.data.Gun;
import fr.petitl.antichamber.triggers.save.data.MapEntry;
import fr.petitl.antichamber.triggers.save.data.PinkCube;
import fr.petitl.antichamber.triggers.save.data.Sign;

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
            if (save.getPlayTime() == 0 && save.getMapEntries().size() == 1) {
                listener.saveReset(l);
                oldSigns.clear();
                oldSigns.addAll(save.getSigns());
                oldGuns.clear();
                oldMapEntries.clear();
                oldMapEntries.addAll(save.getMapEntries());
                oldPinkCubes.clear();
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
