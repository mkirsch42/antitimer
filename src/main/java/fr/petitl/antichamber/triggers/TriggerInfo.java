/*
 * Copyright 2014 Loic Petit
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
