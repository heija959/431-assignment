package DocSearch;
import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;
//TODO Readme
//TODO Comments
//TODO Errors for no indexes and no WSJ
//TODO Errors for no terms
//TODO sort keys by length?
//TODO intersections and ranking
//TODO methodize refactor clean

public class DocSearch {
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        // Declare important index objects
        InvertedIndexObject index;
        long duration;
        long startTime;
        ArrayList<Long> durlist = new ArrayList<Long>();
        ArrayList<Long> surlist = new ArrayList<Long>();
        ArrayList<String> rurlist = new ArrayList<String>();

        // Load index and info objects
        for (int j = 0; j <= 1000; j++) {
            startTime = System.nanoTime();
            index = grabInvertedIndex(Path.of("index"));

            duration = ((System.nanoTime() - startTime) / 1000000);  //divide by 1000000 to get milliseconds.
            //System.out.println("Load: " + duration + "ms");
            //System.out.println(duration);
            durlist.add(duration);
            startTime = System.nanoTime();
            Map<String, int[]> map = index.getMap();
            int[] results = (map.get("Korea"));
            for (int i = 0; i < results.length; i++) {
                rurlist.add(index.getDOCNO(i));
            }


            duration = ((System.nanoTime() - startTime) / 1000000);  //divide by 1000000 to get milliseconds.
            surlist.add(duration);
            //System.out.println("Search: " + duration + "ms");
            index = null;
        }
        System.out.println(durlist);
        System.out.println(surlist);
        //System.out.println(rurlist);
    }

    public static InvertedIndexObject grabInvertedIndex(Path source) throws IOException, ClassNotFoundException {
        ObjectInputStream o = new ObjectInputStream(new BufferedInputStream(new FileInputStream(source.toFile())));
        return (InvertedIndexObject) o.readObject();
    }

}
