package fr.petitl.antichamber.triggers.save;

import fr.petitl.antichamber.triggers.save.data.Gun;
import fr.petitl.antichamber.triggers.save.data.MapEntry;
import fr.petitl.antichamber.triggers.save.data.PinkCube;
import fr.petitl.antichamber.triggers.save.data.Sign;

/**
 *
 */
public interface SaveChangeListener {
    void newSign(Sign s, int count, long timestamp);
    void newGun(Gun g, int count, long timestamp);
    void newMapEntry(MapEntry m, int count, long timestamp);
    void newSecret(PinkCube s, int count, long timestamp);
    void saveReset(long timestamp);
}
