package DocSearch;
import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;

public class DocSearch {
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        // Declare important index objects
        InvertedIndexObject index;
        long duration;
        long startTime;
        ArrayList<Long> durlist = new ArrayList<Long>();


        // Load index and info objects
        for (int j = 0; j <= 10; j++) {
            startTime = System.nanoTime();
            index = grabInvertedIndex(Path.of("index"));

            duration = ((System.nanoTime() - startTime) / 1000000);  //divide by 1000000 to get milliseconds.
            //System.out.println("Load: " + duration + "ms");
            //System.out.println(duration);
            durlist.add(duration);

            Map<String, int[]> map = index.getMap();
            int[] results = (map.get("Zealand"));
            for (int i = 0; i < results.length; i++) {
                index.getDOCNO(i);
            }

            duration = ((System.nanoTime() - startTime) / 1000000);  //divide by 1000000 to get milliseconds.
            //System.out.println("Search: " + duration + "ms");
            index = null;
        }
        System.out.println(durlist);
    }

    public static InvertedIndexObject grabInvertedIndex(Path source) throws IOException, ClassNotFoundException {
        ObjectInputStream o = new ObjectInputStream(new BufferedInputStream(new FileInputStream(source.toFile())));
        return (InvertedIndexObject) o.readObject();
    }

}
