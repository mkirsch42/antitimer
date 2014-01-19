package fr.petitl.antichamber.triggers.watchers;

import fr.petitl.antichamber.triggers.save.AntichamberSave;
import fr.petitl.antichamber.triggers.save.PinkCube;
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
        AntichamberSave save = new AntichamberSave(new File(PATH + "test.bin"));
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
            result.addAll(remains);
            cmp = save;
        }
        Assert.assertEquals(result.size(), 13);
    }
}
