package DocSearch;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Parses a specific XML document into an inverted index to be stored on disk as an InvertedIndexObject
 *
 * @author heija959
 */
public class DocParse {

    // Start a timer
    static long startTime = System.nanoTime();

    /**
     * Prints off the time as of the call of the statement, relevant to the System.nanoTime() at the beginning of the
     * class.
     *
     * @param   in  Input string print before time.
     */
    public static void timer(String in){
        long endTime = System.nanoTime();
        long duration = ((endTime - startTime)/1000000);
        System.out.println(in + " " + duration + "ms");
        startTime = System.nanoTime();
    }

    public static int[] primint(List<Integer> obj){
        return obj.stream().mapToInt(i->i).toArray();
    }

    public static int[] doDgap(List<Integer> obj){
        int[] in = obj.stream().mapToInt(i->i).toArray();
        int last = 0;
        int[] out = new int[in.length];

        for(int i = 0; i < in.length; i++) {
            int j = in[i];
            out[i] = j-last;
            last=j;
        }

        return out;
    }

    public static int[] undies(int[] obj){

        int[] back = new int[obj.length];
        int last=0;
        for(int i = 0; i < obj.length; i++) {
            int j = obj[i];
            back[i] = j+last;
            last=back[i];
        }
        return back;
    }

    public static byte[] castToByteArray(Object in) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(in);
        oos.flush();
        return bos.toByteArray();
    }


    public static void getNormal(){
    }

    public static void writeDisk(Object obj, String fname){
        try
        {
            FileOutputStream fos = new FileOutputStream(fname);
            ObjectOutputStream oos =  new ObjectOutputStream(new BufferedOutputStream(fos));
            oos.writeObject(obj);
            oos.close();
            fos.close();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

    public static Object readDisk(Path source) throws IOException, ClassNotFoundException {
        ObjectInputStream o = new ObjectInputStream(new BufferedInputStream(new FileInputStream(source.toFile())));
        return o.readObject();
    }

    public static byte[] compressObject(Object obj) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GZIPOutputStream gzipOut = new GZIPOutputStream(baos);
        ObjectOutputStream objectOut = new ObjectOutputStream(gzipOut);
        objectOut.writeObject(obj);
        objectOut.close();
        gzipOut.finish();

        return baos.toByteArray();
    }

    public static Object decompressObject(byte[] obj) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(obj);
        GZIPInputStream gzipIn = new GZIPInputStream(bais);
        ObjectInputStream objectIn = new ObjectInputStream(gzipIn);
        Object obju = (Object) objectIn.readObject();
        objectIn.close();

        return obju;
    }

    public static void main(String[] args) throws Exception {

        System.out.print("(Indexer) Reading XML: ");

        // Declaring a list of DocObjects
        ArrayList<DocObject> docList = new ArrayList<>();

        // Read all documents and delimit by words-ish
        Pattern p = Pattern.compile("\\W*\\s");
        Scanner s = new Scanner(new FileInputStream("wsj.small.xml"), StandardCharsets.UTF_8).useDelimiter(p);

        // Declare variables used in the following loop to store counts of documents, docnumbers, and body text.
        int nos = 0, docs = 0;
        String docNo = null;
        StringBuilder textContent = new StringBuilder();

        // Reading the entire XML file...
        while(s.hasNext()) {
            String token = s.next().toLowerCase(Locale.ROOT).toLowerCase(Locale.ROOT);
            if (token.charAt(0) == '<') {
                if (token.equals("<docno")){        // Detect <DOCNO> tag.
                    textContent = new StringBuilder();
                    nos++;                          // Increase count of numbers.
                    if (s.hasNext()){
                        token = s.next().toLowerCase(Locale.ROOT);
                        docNo = token;              // If there, the next token must be our document number.
                        textContent.append(docNo);
                    }
                }

                if (token.equals("<hl")){                     // Detect body text <TEXT> tag.
                    while (s.hasNext()){
                        token = s.next().toLowerCase(Locale.ROOT);
                        if (token.equals("</text")){            // Break if we detect the end of the tag.
                            break;
                        }
                        if (token.charAt(0) == '<') {
                            continue;
                        }
                        textContent.append(" ").append(token);  // Append to string our body text until end.

                                                                // I recognize this includes a leading space.
                                                                // but this doesn't impact the index, and is easier
                                                                // to read.
                    }
                }
                if (token.equals("</doc")){                                         // If we're end of document...
                    DocObject test = new DocObject(textContent.toString(),docNo);
                    docList.add(test);                                              // append a DocObject to our list.
                    docs++;
                }
            }
        }
        if (docs == nos){
            System.out.print("Equal document numbers and documents.\n");
        } else {
            System.out.print("UNEQUAL document numbers and documents! This probably won't work, proceeding anyway.\n");
        }

        timer("(Indexer) Reading XML Time:");

        System.out.println("(Indexer) Inverting "+docList.size()+" documents");

        // Declare variables for inversion process.
        Map<String, List<Integer>> map = new HashMap<>();
        ArrayList<Integer> indexToLen = new ArrayList<>();
        ArrayList<String> indexToDocNO = new ArrayList<>();

        // For every document...
        for (int i = 0; i < docList.size(); i++){

            // ...add the doc number and length to our list.
            indexToLen.add(docList.get(i).getDocLength());
            indexToDocNO.add(docList.get(i).getDocNO());

            // For all unique strings in the body text of docs...
            for (String word:docList.get(i).getUniqueText()){
                ArrayList<Integer> temporaryList;

                // Check if the word has occured in our Map, and if so, append to the previous postings for the word.
                if (map.containsKey(word)){
                    temporaryList = (ArrayList<Integer>) map.get(word);
                }
                else { // ...otherwise create a new list and append to that instead.
                    temporaryList = new ArrayList<>();
                }

                temporaryList.add(i);
                map.put(word, temporaryList);
            }
        }

        timer("(Indexer) Inverting Time:");

        System.out.println("(Indexer) Convert to primitives...");  // ...because it's better for storage

        LinkedHashMap<String, int[]> convertedmap = new LinkedHashMap<>();
        for(String word:map.keySet()){
            int[] temporaryList = map.get(word).stream().mapToInt(x->x).toArray();
            convertedmap.put(word, temporaryList);
        }

        String[] indexToDocNOConverted = indexToDocNO.toArray(new String[0]);


        File file = null;
        PrintWriter pw = null;
        HashMap<String,Integer[]> dictionary = new HashMap<>();

        try {
            List<byte[]> byteList = new ArrayList<byte[]>();
            int offset = 0;
            int machinelength = 0;
            byte[] data;


            for(String word:map.keySet()){
                //System.out.println(Arrays.toString(doDgap(map.get(word))));
                //.out.println(Arrays.toString(undies(doDgap(map.get(word)))));
                data = castToByteArray(primint((map.get(word))));
                byteList.add(data);
                machinelength += data.length;

                //for all in bytelist total length of each and compand into 1d array for writing? dual offset shit????
                dictionary.put(word, new Integer[]{offset, data.length});
                offset+=data.length;
            }
            byte[] data2 = new byte[machinelength];
            int pos = 0;
            for(byte[] bytes:byteList){
                for (int i = 0; i<bytes.length; i++){
                    data2[i] = bytes[i];
                    pos++;
                }
            }
            writeDisk(data2,"newBytes");
            System.out.println("sheesh "+ data2.length);
            System.out.println(offset);
            System.out.println("oi " + machinelength);
            System.out.println(byteList.size());


            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(
                    "checkbytes2"));
            out.writeObject(byteList);
            writeDisk(byteList.toArray(),"checkbytesX2");


            System.out.println(offset);
            System.out.println(byteList.size());
            //System.out.println(dictionary);
            System.out.println("File writing done.");
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pw != null) {
                    pw.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        writeDisk(dictionary,"dictionary");

        timer("(Indexer) Primitives Time:");

        System.out.println("(Indexer) Index object creation...");

        // Create the InvertedIndexObject to prepare for saving, from our other objects.
        InvertedIndexObject index = new InvertedIndexObject(convertedmap, indexToLen.stream().mapToInt(x->x).toArray(), indexToDocNOConverted);

        System.out.println("(Indexer) Index object saving...");

        writeDisk(index, "index");
        writeDisk(compressObject(index),"indexc");
        writeDisk(indexToDocNOConverted, "docnos");
        writeDisk(indexToLen, "lens");
        writeDisk(convertedmap, "mapA");

        //System.out.println(convertedmap.entrySet());
        timer("(Indexer) Saving Time:");
        readDisk(Path.of("index"));
        timer("(Indexer) Re-read Time:");
        readDisk(Path.of("indexc"));
        timer("(Indexer) Re-read Time C:");



    }
}