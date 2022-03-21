package DocSearch;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class DocSearch {
    public static void main(String[] args){

        IndexObject index = null;
        long startTime = System.nanoTime();

        try
        {
            FileInputStream fis = new FileInputStream("index");
            ObjectInputStream ois = new ObjectInputStream(fis);

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
        System.out.println(index);
        long endTime = System.nanoTime();
        long duration = ((endTime - startTime) / 1000000);  //divide by 1000000 to get milliseconds.
        System.out.println("Time: "+duration+"ms");

    }
}
