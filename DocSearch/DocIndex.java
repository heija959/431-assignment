package DocSearch;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Parses a specific XML document into an inverted index to be stored on disk as an InvertedIndexObject
 *
 * @author heija959
 */
public class DocIndex {

    // Start a timer
    static long startTime = System.nanoTime();
    static final int HASH = 199; //61

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

    public static int h(String s){
        int t = 0;
        s=s.toLowerCase(Locale.ROOT);

        for(int i = 0; i < s.length(); i++)
        {
            t+=s.charAt(i);
        }

        return t%HASH;
    }

    public static String shaver(String content, Pattern p) {
        return p.matcher(content).replaceAll("");
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

    static int occur(String[]a, String s)
    {
        int counter = 0;
        for (String value : a)
            if (s.equals(value))
                counter++;

        return counter;
    }

    public static void main(String[] args) throws Exception {

        System.out.print("(Indexer) Reading XML: ");

        // Declaring a list of DocObjects
        ArrayList<DocObject> docList = new ArrayList<>();

        // Read all documents and delimit by words-ish
        Pattern p = Pattern.compile("\\W*\\s");
        Pattern shaver = Pattern.compile("[^\\w+|-]+");
        //Pattern shaver = Pattern.compile("[^\\w+]");
        Scanner s = new Scanner(new FileInputStream("wsj.xml"), StandardCharsets.UTF_8).useDelimiter(p);

        // Declare variables used in the following loop to store counts of documents, docnumbers, and body text.
        int nos = 0, docs = 0;
        String docNo = null;
        StringBuilder textContent = new StringBuilder();

        // Reading the entire XML file...
        while(s.hasNext()) {
            String token = s.next().toLowerCase(Locale.ROOT);
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
                        textContent.append(" ").append(shaver(token, shaver));  // Append to string our body text until end.

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
        List<Map<String, List<int[]>>> maps = new ArrayList<>();
        for (int i = 0; i < HASH; i++){
            Map<String, List<int[]>> emptymap = new HashMap<>();
            maps.add(emptymap);
        }
        ArrayList<Integer> indexToLen = new ArrayList<>();
        ArrayList<String> indexToDocNO = new ArrayList<>();


        // For every document...
        for (int i = 0; i < docList.size(); i++){

            // ...add the doc number and length to our list.
            indexToLen.add(docList.get(i).getDocLength());
            indexToDocNO.add(docList.get(i).getDocNO());

            // For all unique strings in the body text of docs...
            for (String word:docList.get(i).getUniqueText()){
                ArrayList<int[]> temporaryList;
                int wordHash = h(word);
                // Check if the word has occured in our Map, and if so, append to the previous postings for the word.
                if (maps.get(wordHash).containsKey(word)){
                    temporaryList = (ArrayList<int[]>) (maps.get(wordHash)).get(word);
                }
                else { // ...otherwise create a new list and append to that instead.
                    temporaryList = new ArrayList<>();
                }
                int[] add = new int[2];
                add[0] = i;
                add[1] = occur(docList.get(i).getTextArr(), word);
                temporaryList.add(add);
                maps.get(wordHash).put(word, temporaryList);
            }
        }

        timer("(Indexer) Inverting Time:");

        System.out.println("(Indexer) Convert to primitives...");  // ...because it's better for storage

        List<LinkedHashMap<String, int[]>> convertedmaps = new ArrayList<>();
        for (int i = 0; i < HASH; i++){
            LinkedHashMap<String, int[]> convertedmap = new LinkedHashMap<>();
            convertedmaps.add(convertedmap);
        }

       /* for(int i = 0; i < maps.size(); i++) {
            for (String word : maps.get(i).keySet()) {
                int[] temporaryList = (maps.get(i).get(word)).stream().mapToInt(x -> x).toArray();
                convertedmaps.get(i).put(word, temporaryList);
            }
        }*/
        if (maps.size() != HASH){
            System.out.println("Halt Hash "+maps.size());
            System.exit(130);
        }

        String[] indexToDocNOConverted = indexToDocNO.toArray(new String[0]);

        timer("(Indexer) Primitives Time:");

        System.out.println("(Indexer) Index object creation...");

        // Create the InvertedIndexObject to prepare for saving, from our other objects.
        //InvertedIndexObject index = new InvertedIndexObject(convertedmap, indexToLen.stream().mapToInt(x->x).toArray(), indexToDocNOConverted);
        for (int i = 0; i<maps.size(); i++){
            writeDisk(maps.get(i),"index/"+i);
        }

        timer("(Indexer) Object Time:");
        System.out.println("(Indexer) Index object saving...");

        //writeDisk(index, "indexU");
        writeDisk(indexToDocNOConverted, "index/docnos");
        int[] cindexToLen = indexToLen.stream().mapToInt(Integer::intValue).toArray();
        writeDisk(cindexToLen, "index/lens");
        //writeDisk(convertedmap, "mapA");
        timer("(Indexer) Saving Time:");

        timer("(Indexer) Re-read Time:");



    }
}