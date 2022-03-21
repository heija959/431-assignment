package DocSearch;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.Map;

public class DocSearch {
    public static void main(String[] args){

        IndexObject index = null;
        long startTime = System.nanoTime();

        try
        {
            FileInputStream fis = new FileInputStream("index");
            ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(fis, 4096));

            index = (IndexObject) ois.readObject();

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
        }
        //System.out.println(index);
        long endTime = System.nanoTime();
        long duration = ((endTime - startTime) / 1000000);  //divide by 1000000 to get milliseconds.
        long midTime = endTime;
        System.out.println("Time: "+duration+"ms");
        assert index != null;
        Map<String, List<Integer>> map = index.getMap();
        for (int i : map.get("Zealand")){
            System.out.println(index.getDOCNO(i));
        }
        endTime = System.nanoTime();
        duration = ((endTime - midTime) / 1000000);
        System.out.println("Time: "+duration+"ms");


    }
}
