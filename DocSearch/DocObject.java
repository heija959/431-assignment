package DocSearch;
import java.util.Arrays;

/**
 * Object type for documents derived from XML.
 *
 * @author heija959
 */
public class DocObject{

    final String DOCNO;
    String textContent;
    final int textLength;
    String[] uniqueWords;
    final boolean WIPE = true; // Set to false if you don't want to wipe the bodytext afterwards.

    /**
     * DocObject constructor that takes the DocObject's WSJ-XXXXXX-YYYY identifier and body text.
     * Body text is converted to all available unique words, and wiped depending on the value of WIPE.
     *
     * @param DOCNO         WSJ-XXXXXX-YYYY identifier.
     * @param textContent   Body text of the document
     */
    public DocObject(String DOCNO, String textContent){
        this.DOCNO = DOCNO;
        this.textLength = textContent.length();
        this.textContent = textContent;
        convertToUniques();
    }

    /**
     * toString override to return WSJ-XXXXXX-YYYY identifier.
     *
     * @return  DOCNO
     */
    public String toString(){
        return DOCNO;
    }

    /**
     * Accessor for the length of a document.
     *
     * @return  textLength
     */
    int getDocLength() {
        return textLength;
    }

    /**
     * Accessor for unique words in document's body text.
     *
     * @return Unique words.
     */
    String[] getUniqueText() {
        return uniqueWords;
    }

    /**
     * Accessor for the document's WSJ-XXXXXX-YYYY identifier.
     *
     * @return WSJ-XXXXXX-YYYY identifier.
     */
    String getDOCNO(){
        return DOCNO;
    }

    /**
     * Converts all text in the document to an array of unique words.
     *
     */
    void convertToUniques() {
        this.uniqueWords = Arrays.stream(this.textContent.split(" ")).distinct().toArray(String[]::new);
        if (WIPE) {
            this.textContent = null;
        }
    }

}
