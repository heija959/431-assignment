package DocSearch;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.stream.Stream;

public class TestLineRead {

    static long startTime = System.nanoTime();

    public static void timer(String in) {
        long endTime = System.nanoTime();
        long duration = ((endTime - startTime) / 1000000);
        System.out.println(in + " " + duration + "ms");
        startTime = System.nanoTime();
    }

    private static LinkedList<String> bufferReaderToList(String path, LinkedList<String> list) {
        try {
            final BufferedReader in = new BufferedReader(
                    new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8));
            String line;
            while ((line = in.readLine()) != null) {
                list.add(line);
            }
            in.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static Object readDisk(Path source) throws IOException, ClassNotFoundException {
        ObjectInputStream o = new ObjectInputStream(new BufferedInputStream(new FileInputStream(source.toFile())));
        return o.readObject();
    }

    public static String readLine(String source, int linetarget){
        String output = null;

        try (Stream<String> lines = Files.lines(Paths.get(source))) {
            output = lines.skip(linetarget).findFirst().get();
        } catch (IOException e) {
            System.out.println(e);
        }

        return output;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        timer("From cold");
        int n = 4500; // The line number
        int n2 = 13000;
        int n3 = 12;
        String line;

        readLine("check", 796002);
        timer("From File.lines: ");
        readLine("check", 40);
        timer("From File.lines: ");
        readLine("check", 2);
        timer("From File.lines: ");
        readLine("check", 9000);
        timer("From File.lines: ");
        readLine("check", 6892);
        timer("From File.lines: ");
        readLine("check", 10000);
        timer("From File.lines: ");
        readLine("check", 430);
        timer("From File.lines: ");
        readLine("check", 3);
        timer("From File.lines: ");
        readLine("check", 17);
        timer("From File.lines: ");
        readLine("check", 17010);
        timer("From File.lines: ");


        readDisk(Path.of("dictionary"));
        timer("From serial");
    }
}

