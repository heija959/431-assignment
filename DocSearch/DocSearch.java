package DocSearch;
import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class DocSearch {

    void termsToResults(){}
    void resultsToRanks(){}
    void nArrayIntersects(){}
    void arrayIntersections(){}

    public static Object decompressObject(byte[] obj) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(obj);
        GZIPInputStream gzipIn = new GZIPInputStream(new BufferedInputStream(bais));
        ObjectInputStream objectIn = new ObjectInputStream(new BufferedInputStream(gzipIn));
        Object obju = (Object) objectIn.readObject();
        objectIn.close();

        return obju;
    }

    public static Object readDisk(Path source) throws IOException, ClassNotFoundException {
        ObjectInputStream o = new ObjectInputStream(new BufferedInputStream(new FileInputStream(source.toFile())));
        return o.readObject();
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        // Declare important index objects
        InvertedIndexObject index;
        long duration;
        long startTime;
        ArrayList<Long> durlist = new ArrayList<>();
        ArrayList<Long> surlist = new ArrayList<>();
        //noinspection MismatchedQueryAndUpdateOfCollection
        ArrayList<String> rurlist = new ArrayList<>();

        // Load index and info objects
        for (int j = 0; j <= 0; j++) {
            startTime = System.nanoTime();
            index = (InvertedIndexObject) decompressObject((byte[]) readDisk(Path.of("indexc")));

            duration = ((System.nanoTime() - startTime) / 1000000);  //divide by 1000000 to get milliseconds.
            //System.out.println("Load: " + duration + "ms");
            //System.out.println(duration);
            durlist.add(duration);
            startTime = System.nanoTime();
            Map<String, int[]> map = index.getMap();
            int[] results = (map.get("Korea"));
            //for (int i = 0; i < results.length; i++) {
            //   rurlist.add(index.getText);
            //}


            duration = ((System.nanoTime() - startTime) / 1000000);  //divide by 1000000 to get milliseconds.
            surlist.add(duration);
            //System.out.println("Search: " + duration + "ms");
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
