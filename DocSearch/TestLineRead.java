package DocSearch;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class TestLineRead {

    static long startTime = System.nanoTime();

    public static void timer(String in) {
        long endTime = System.nanoTime();
        long duration = ((endTime - startTime) / 1000000);
        System.out.println(in + " " + duration + "ms");
    }

    public static void main(String[] args) {
        int n = 400000; // The line number
        String line;
        try (Stream<String> lines = Files.lines(Paths.get("wsj.xml"))) {
            line = lines.skip(n).findFirst().get();
            System.out.println(line);
        } catch (IOException e) {
            System.out.println(e);
        }
        timer("Now: ");
    }
}

