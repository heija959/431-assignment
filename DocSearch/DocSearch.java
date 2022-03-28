package DocSearch;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;

public class DocSearch {

    void termsToResults(){}
    void resultsToRanks(){}
    void nArrayIntersects(){}
    void arrayIntersections(){}

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
        for (int j = 0; j <= 40; j++) {
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
        }
        System.out.println(durlist);
        System.out.println(surlist);
        //System.out.println(rurlist);
    }

    public static InvertedIndexObject grabInvertedIndex(Path source) throws IOException, ClassNotFoundException {
        Kryo kryo = new Kryo();
        kryo.register(InvertedIndexObject.class);
        Input input = new Input(new FileInputStream("index.bin"));
        InvertedIndexObject output = kryo.readObject(input, InvertedIndexObject.class);
        input.close();
        return output;
    }

}
