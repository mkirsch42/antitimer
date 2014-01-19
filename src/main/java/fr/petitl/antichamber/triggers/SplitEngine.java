package fr.petitl.antichamber.triggers;

/**
 *
 */
public interface SplitEngine {
    void fireStart(long timestamp);
    void fireSplit(long timestamp);
    void fireEnd(long timestamp);
    void fireReset();
}
