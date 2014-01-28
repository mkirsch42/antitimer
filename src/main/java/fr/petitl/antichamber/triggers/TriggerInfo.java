package fr.petitl.antichamber.triggers;

import java.io.Serializable;

/**
 *
 */
public class TriggerInfo implements Serializable {
    private static final long serialVersionUID = 9152038806864489141L;

    private TriggerType type;
    private Object argumentToMatch;
    private boolean mustBeComplete;

    public TriggerInfo(TriggerType type, Object argumentToMatch, boolean mustBeComplete) {
        this.type = type;
        this.argumentToMatch = argumentToMatch;
        this.mustBeComplete = mustBeComplete;
    }

    public TriggerType getType() {
        return type;
    }

    public Object getArgumentToMatch() {
        return argumentToMatch;
    }

    public boolean mustBeComplete() {
        return mustBeComplete;
    }

    public void setType(TriggerType type) {
        this.type = type;
    }

    public void setArgumentToMatch(Object argumentToMatch) {
        this.argumentToMatch = argumentToMatch;
    }

    public void setMustBeComplete(boolean mustBeComplete) {
        this.mustBeComplete = mustBeComplete;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TriggerInfo that = (TriggerInfo) o;

        if (type != that.type) return false;
        if (mustBeComplete != that.mustBeComplete) return false;
        if (mustBeComplete)
            return true;
        if (argumentToMatch == null ? that.argumentToMatch != null : !argumentToMatch.equals(that.argumentToMatch))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (argumentToMatch == null || mustBeComplete ? 0 : argumentToMatch.hashCode());
        result = 31 * result + (mustBeComplete ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return type.toString()+": "+(mustBeComplete ? "Complete" : argumentToMatch);
    }
}
