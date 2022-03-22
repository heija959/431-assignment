package DocSearch;
import java.io.*;
import java.nio.file.Path;
import java.util.Map;

public class DocSearch {
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        // Declare important index objects
        InvertedIndexObject index;
        IndexObject info;
        long duration;
        long startTime = System.nanoTime();

        // Load index and info objects
        index = grabInvertedIndex(Path.of("index"));
        info = grabInfoIndex(Path.of("info"));

        duration = ((System.nanoTime() - startTime) / 1000000);  //divide by 1000000 to get milliseconds.
        System.out.println("Time: "+duration+"ms");

        Map<String, int[]> map = index.getMap();
        int[] results = (map.get("Zealand"));
        for (int i = 0; i < results.length; i++){
            assert info != null;
            info.getDOCNO(i);
        }

        duration = ((System.nanoTime() - startTime) / 1000000);  //divide by 1000000 to get milliseconds.
        System.out.println("Time: "+duration+"ms");

    }

    public static InvertedIndexObject grabInvertedIndex(Path source) throws IOException, ClassNotFoundException {
        ObjectInputStream o = new ObjectInputStream(new BufferedInputStream(new FileInputStream(source.toFile())));
        return (InvertedIndexObject) o.readObject();
    }

    public static IndexObject grabInfoIndex(Path source) throws IOException, ClassNotFoundException {
        ObjectInputStream o = new ObjectInputStream(new BufferedInputStream(new FileInputStream(source.toFile())));
        return (IndexObject) o.readObject();
    }

}
