package DocSearch;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

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
    }

    public static void main(String[] args) throws Exception {

        System.out.print("(Indexer) Reading XML: ");

        // Declaring a list of DocObjects
        ArrayList<DocObject> docList = new ArrayList<>();

        // Read all documents and delimit by words-ish
        Pattern p = Pattern.compile("\\W*\\s");
        Scanner s = new Scanner(new FileInputStream("wsj.xml"), StandardCharsets.UTF_8).useDelimiter(p);

        // Declare variables used in the following loop to store counts of documents, docnumbers, and body text.
        int nos = 0, docs = 0;
        String docNo = null;
        StringBuilder textContent = new StringBuilder();

        // Reading the entire XML file...
        while(s.hasNext()) {
            String token = s.next();
            if (token.charAt(0) == '<') {
                if (token.equals("<DOCNO")){        // Detect <DOCNO> tag.
                    nos++;                          // Increase count of numbers.
                    if (s.hasNext()){
                        token = s.next();
                        docNo = token;              // If there, the next token must be our document number.
                    }
                }

                if (token.equals("<TEXT")){                     // Detect body text <TEXT> tag.
                    textContent = new StringBuilder();
                    while (s.hasNext()){
                        token = s.next();
                        if (token.equals("</TEXT")){            // Break if we detect the end of the tag.
                            break;
                        }
                        textContent.append(" ").append(token);  // Append to string our body text until end.

                                                                // I recognize this includes a leading space.
                                                                // but this doesn't impact the index, and is easier
                                                                // to read.
                    }
                }
                if (token.equals("</DOC")){                                         // If we're end of document...
                    DocObject test = new DocObject(docNo, textContent.toString());
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
        ArrayList<String> indexToDocNo = new ArrayList<>();
        ArrayList<Integer> indexToLen = new ArrayList<>();

        // For every document...
        for (int i = 0; i < docList.size(); i++){

            // ...add the doc number and length to our list.
            indexToDocNo.add(docList.get(i).getDOCNO());
            indexToLen.add(docList.get(i).getDocLength());

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

            docList.get(i).wipe();  // Wipe all body text from the document list.
        }

        timer("(Indexer) Inverting Time:");

        System.out.println("(Indexer) Convert to primitives...");  // ...because it's better for storage

        Map<String, int[]> convertedmap = new HashMap<>();
        for(String word:map.keySet()){
            int[] temporaryList = map.get(word).stream().mapToInt(x->x).toArray();
            convertedmap.put(word, temporaryList);
        }

        timer("(Indexer) Primitives Time:");

        System.out.println("(Indexer) Index object creation...");

        // Create the InvertedIndexObject to prepare for saving, from our other objects.
        String[] indexToDocNoConverted = indexToDocNo.toArray(new String[0]);
        InvertedIndexObject index = new InvertedIndexObject(convertedmap, indexToDocNoConverted, indexToLen.stream().mapToInt(x->x).toArray());

        System.out.println("(Indexer) Index object saving...");

        try
        {
            FileOutputStream fos = new FileOutputStream("index");
            ObjectOutputStream oos =  new ObjectOutputStream(new BufferedOutputStream(fos));
            oos.writeObject(index);
            oos.close();
            fos.close();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }

        timer("(Indexer) Saving Time:");

    }
}