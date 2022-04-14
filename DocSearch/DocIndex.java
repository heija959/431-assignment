package DocSearch;


import java.io.*;
import java.util.*;

/**
 * Parses a specific XML document into an inverted index to be stored on disk as an InvertedIndexObject
 *
 * @author heija959
 */
public class DocIndex {

    // Start a timer
    static long startTime = System.nanoTime();
    static final int HASH = 397; //61 199

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

    static int occur(String[]a, String s)
    {
        int counter = 0;
        for (String value : a)
            if (s.equals(value))
                counter++;

        return counter;
    }

    public static void main(String[] args) throws Exception {

        ArrayList<DocObject> docList = new ArrayList<>();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            while ((line = in.readLine()) != null) {

                if(line.isEmpty()){
                    continue;
                }
                String[] lines = line.split(" ");
                String docNo = null;
                StringBuilder textContent = new StringBuilder();
                for(String s:lines){
                    if (s.length() > 1 && s.charAt(0) == '#') {
                        docNo = s.substring(1);
                    } else {
                        textContent.append(" ");
                        textContent.append(s);
                    }
                }
                DocObject test = new DocObject(textContent.toString(),docNo);
                docList.add(test);
            }
        }


        timer("(Indexer) Reading Time:");

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

        String[] indexToDocNOConverted = indexToDocNO.toArray(new String[0]);


        for (int i = 0; i<maps.size(); i++){
            writeDisk(maps.get(i),"index/"+i);
        }

        writeDisk(indexToDocNOConverted, "index/docnos");
        int[] cindexToLen = indexToLen.stream().mapToInt(Integer::intValue).toArray();
        writeDisk(cindexToLen, "index/lens");

        timer("Completed index: ");



    }
}