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

package fr.petitl.antichamber.timer;

import fr.petitl.antichamber.triggers.TriggerInfo;

import java.util.List;

/**
 *
 */
public interface TimerControl {
    void exit();

    void buildAndInjectRun(String name, List<TriggerInfo> splits);

    void split(long stopTime);

    void start(long stopTime);

    void reset();
}
