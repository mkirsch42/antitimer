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

package fr.petitl.antichamber.triggers.save.data;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public enum Gun {
    BLUE(0), GREEN(1), YELLOW(8), RED(3)
    ;
    private static final String PREFIX = "HazardIGFChinaSplit.TheWorld:PersistentLevel.HazardPickupFactory";
    private static Map<Integer, Gun> valueMap = new HashMap<>();

    static {
        for (Gun gun : Gun.values())
            valueMap.put(gun.id, gun);
    }

    private final int id;

    private Gun(int id) {
        this.id = id;
    }

    public static Gun fromString(String name) {
        String[] split = name.split("_");
        Gun gun = valueMap.get(Integer.parseInt(split[1]));
        if(gun == null || !split[0].equals(PREFIX))
            throw new IllegalArgumentException("Unknown gun "+name);
        return gun;
    }

    @Override
    public String toString() {
        String s = name().toLowerCase();
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}
