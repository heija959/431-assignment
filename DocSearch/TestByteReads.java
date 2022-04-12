package DocSearch;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;


public class TestByteReads {

    public static boolean initf = false;
    public static final int POS = 0;
    public static final int LEN = 0;

    public static Object readDisk(Path source) throws IOException, ClassNotFoundException {
        ObjectInputStream o = new ObjectInputStream(new BufferedInputStream(new FileInputStream(source.toFile())));
        return o.readObject();
    }

    public static int[] readObjectPortion(String path, int pos, int len) throws IOException {
        RandomAccessFile f = null;
        if (!initf) {
            f = new RandomAccessFile(String.valueOf(path), "r");
            initf = true;
        }

        assert f != null;
        f.seek(pos);
        byte[] raw = new byte[len];
        for (int i = 0; i<len; i++){
            raw[i] = f.readByte();
        }

        IntBuffer intBuf = ByteBuffer.wrap(raw).order(ByteOrder.BIG_ENDIAN).asIntBuffer();
        int[] array = new int[intBuf.remaining()];
        intBuf.get(array);
        return array;

    }

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        HashMap<String, Integer[]> dictionary;

        dictionary = (HashMap<String, Integer[]>) readDisk(Path.of("dictionary"));
        //System.out.println(dictionary);
        System.out.println((Arrays.toString(dictionary.get("near"))));
        System.out.println(dictionary.keySet());
        for(Integer[] i: dictionary.values()){
            System.out.print(i[0]+" ");
        }
        System.out.println();
        for(Integer[] i: dictionary.values()){
            System.out.print(i[1]+" ");
        }
        System.out.println();
        //System.out.println(dictionary.values());
        System.out.println(Arrays.toString(readObjectPortion("newBytes", 381, 32)));

    }
}
