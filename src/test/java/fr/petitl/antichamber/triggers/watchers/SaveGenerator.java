package fr.petitl.antichamber.triggers.watchers;

import org.junit.Test;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 *
 */
public class SaveGenerator {
    public static void main(String[] args) throws IOException {
        new SaveGenerator().generate();
    }
    public void generate() throws IOException {
        Scanner scanner = new Scanner(SaveGenerator.class.getResourceAsStream("/all-triggers"));
        byte intro[] = new byte[87];
        byte outro[] = new byte[415];
        byte totalSize[] = new byte[]{0,0,0,0,0,0,0,0};
        byte arraySize[] = new byte[]{1,0,0,0};
        byte stringSize[] = new byte[]{0,0,0,0};
        int offsetOutro = 166;
        InputStream is = SaveGenerator.class.getResourceAsStream("/saves/one-entry.bin");
        is.read(intro);

        is.skip(offsetOutro-intro.length);
        is.read(outro);
        is.close();

        while(scanner.hasNextLine()) {
            String s = scanner.nextLine().trim();
            String file = s.replace("TheWorld:PersistentLevel.HazardTrigger_", "");
            file = file.replace("Hazard", "").replace("ChinaSplit.", "");
            FileOutputStream f = new FileOutputStream("/tmp/generate/"+file+".bin");
            f.write(intro);
            totalSize[0] = (byte) (s.length() + 1 + 8);
            stringSize[0] = (byte) (s.length() + 1);
            f.write(totalSize);
            f.write(arraySize);
            f.write(stringSize);
            f.write(s.getBytes());
            f.write(0);
            f.write(outro);
            f.close();
        }
    }
}
