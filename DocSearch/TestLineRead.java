package DocSearch;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class TestLineRead {

    static long startTime = System.nanoTime();

    public static void timer(String in) {
        long endTime = System.nanoTime();
        long duration = ((endTime - startTime) / 1000000);
        System.out.println(in + " " + duration + "ms");
        startTime = System.nanoTime();
    }

    public static Object readDisk(Path source) throws IOException, ClassNotFoundException {
        ObjectInputStream o = new ObjectInputStream(new BufferedInputStream(new FileInputStream(source.toFile())));
        return o.readObject();
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        int n = 4500; // The line number
        String line;
        readDisk(Path.of("dictionary"));
        try (Stream<String> lines = Files.lines(Paths.get("check"))) {
            line = lines.skip(n).findFirst().get();
            System.out.println(line);
        } catch (IOException e) {
            System.out.println(e);
        }
        timer("Now: ");
    }
}

