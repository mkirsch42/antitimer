package fr.petitl.antichamber.triggers.logger;

/**
 *
 */
public interface LogChangeListener {
    void mapClicked(long timestamp);
    void theEnd(long timestamp);

    void ghostFired(long timestamp);
}
