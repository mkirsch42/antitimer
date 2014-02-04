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

package fr.petitl.antichamber.triggers.watchers;

import fr.petitl.antichamber.log.LogLevel;
import fr.petitl.antichamber.triggers.save.AntichamberSave;
import fr.petitl.antichamber.triggers.save.data.PinkCube;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 *
 */
public class AntichamberSaveTest {
    private static final String PATH = "src/test/resources/saves/";

    @Test
    public void testFull() throws Exception {
        AntichamberSave save = new AntichamberSave(new File(PATH + "full.bin"));
        AntichamberSave.log.setLevel(LogLevel.DEBUG);
        save.read();
        System.out.println(save);
    }

    @Test
    public void testPinks() throws IOException {
        AntichamberSave cmp = new AntichamberSave(new File(PATH + "test.bin"));
        cmp.read();
        List<PinkCube> result = new ArrayList<>();
        for(int i = 1 ; i <= 13 ; i++) {
            AntichamberSave save = new AntichamberSave(new File(PATH + "pink/" +i+".bin"));
            save.read();
            HashSet<PinkCube> remains = new HashSet<>(save.getPinkCubes());
            remains.removeAll(cmp.getPinkCubes());
            //Assert.assertEquals(remains.size(), 1);
            result.addAll(remains);
            cmp = save;
        }
        Assert.assertEquals(result.size(), 13);
    }
}
