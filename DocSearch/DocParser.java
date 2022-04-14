package DocSearch;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Pattern;

public class DocParser {


    // Start a timer
    static long startTime = System.nanoTime();

    /**
     * Prints off the time as of the call of the statement, relevant to the System.nanoTime() at the beginning of the
     * class.
     *
     * @param in Input string print before time.
     */
    public static void timer(String in) {
        long endTime = System.nanoTime();
        long duration = ((endTime - startTime) / 1000000);
        System.out.println(in + " " + duration + "ms");
        startTime = System.nanoTime();
    }

    public static String shaver(String content, Pattern p) {
        return p.matcher(content).replaceAll("");
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
        while (s.hasNext()) {
            String token = s.next().toLowerCase(Locale.ROOT);
            if (token.charAt(0) == '<') {
                if (token.equals("<docno")) {        // Detect <DOCNO> tag.
                    textContent = new StringBuilder();
                    nos++;                          // Increase count of numbers.
                    if (s.hasNext()) {
                        token = s.next().toLowerCase(Locale.ROOT);
                        docNo = token;              // If there, the next token must be our document number.
                        textContent.append(docNo);
                    }
                }

                if (token.equals("<hl")) {                     // Detect body text <TEXT> tag.
                    while (s.hasNext()) {
                        token = s.next().toLowerCase(Locale.ROOT);
                        if (token.equals("</text")) {            // Break if we detect the end of the tag.
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
                if (token.equals("</doc")) {                                         // If we're end of document...
                    System.out.println("#"+docNo+" %"+textContent.length()+" "+textContent);

                    DocObject test = new DocObject(textContent.toString(), docNo);
                    docList.add(test);                                              // append a DocObject to our list.
                    docs++;
                }
            }
        }
        if (docs != nos) {
            System.err.print("Unequal docs.");
        }

    }
}
