package DocSearch;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Pattern;

public class DocParser {


    public static String shaver(String content, Pattern p) {

        // As a regex replace this is slower, so these two lines are fine.
        // We do sacrifice the words "hl" and "dd" here, but we recover the ability to search by date.
        // I would say that searching for dates is much more important than searching for two words that aren't real.
        content = content.replace("hl"," ");
        content = content.replace("dd"," ");

        return p.matcher(content).replaceAll("");
    }

    public static void main(String[] args) throws Exception {

        // Read all documents and delimit by words-ish
        Pattern p = Pattern.compile("\\W*\\s");
        Pattern shaver = Pattern.compile("[^\\w+|-]+");
        //Pattern shaver = Pattern.compile("[^\\w+]");
        Scanner s = new Scanner(new FileInputStream("wsj.small.xml"), StandardCharsets.UTF_8).useDelimiter(p);

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
                            token = " ";
                            continue;
                        }
                        textContent.append(" ").append(shaver(token, shaver));  // Append to string our body text until end.

                    }
                }
                if (token.equals("</doc")) {                                         // If we're end of document...
                    System.out.println("#"+textContent+"\n");

                    docs++;
                }
            }
        }
        if (docs != nos) {
            System.err.print("Unequal docs.");
        }

    }
}
