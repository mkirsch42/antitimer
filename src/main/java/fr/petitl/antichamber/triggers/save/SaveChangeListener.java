package fr.petitl.antichamber.triggers.save;

/**
 *
 */
public interface SaveChangeListener {
    void newSign(Sign s, int count, long timestamp);
    void newGun(Gun g, int count, long timestamp);
    void newMapEntry(String m, int count, long timestamp);
    void newSecret(PinkCube s, int count, long timestamp);
    void saveReset(long timestamp);
}
