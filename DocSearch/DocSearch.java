package DocSearch;
import java.io.*;
import java.nio.file.Path;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class DocSearch {
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        InvertedIndexObject index = null;
        IndexObject info = null;
        long startTime = System.nanoTime();

        /*try
        {
            FileInputStream fis = new FileInputStream("index");
            ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(fis));

            index = (InvertedIndexObject) ois.readObject();

            ois.close();
            fis.close();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
        catch (ClassNotFoundException c)
        {
            System.out.println("Class not found");
            c.printStackTrace();
        }*/
        //System.out.println(index);
        index = decompressInvertedIndex(Path.of("index")) ;
        long endTime = System.nanoTime();
        long duration = ((endTime - startTime) / 1000000);  //divide by 1000000 to get milliseconds.
        System.out.println("Time: "+duration+"ms");
        assert index != null;
        Map<String, int[]> map = index.getMap();
        //int[] results = (map.get("Zealand"));
        long midTime = endTime;
        endTime = System.nanoTime();

        duration = ((endTime - midTime) / 1000000);
        System.out.println("Time: "+duration+"ms");
        /*try
        {
            FileInputStream fis = new FileInputStream("info");
            ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(fis));

            info = (IndexObject) ois.readObject();

            ois.close();
            fis.close();
        }
        catch (IOException | ClassNotFoundException ioe)
        {
            ioe.printStackTrace();
        }
        //System.out.println("Class not found");
        */
        /*
        for (int i = 0; i < results.length; i++){
            assert info != null;
            //System.out.println(info.getDOCNO(i));
        }*/
        midTime = endTime;
        endTime = System.nanoTime();
        duration = ((endTime - midTime) / 1000000);
        System.out.println("Time: "+duration+"ms");


    }

    public static InvertedIndexObject decompressInvertedIndex(Path source) throws IOException, ClassNotFoundException {
        GZIPInputStream gis = new GZIPInputStream(new BufferedInputStream(new FileInputStream(source.toFile())));
       ObjectInputStream o = new ObjectInputStream(gis);

        return (InvertedIndexObject) o.readObject();

    }
}
