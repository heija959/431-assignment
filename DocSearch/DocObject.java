package DocSearch;
import java.util.Arrays;

/**
 * Object type for documents derived from XML.
 *
 * @author heija959
 */
public class DocObject{

    String textContent;
    final String docNO;
    final int textLength;
    String[] uniqueWords;
    final boolean WIPE = false; // Set to false if you don't want to wipe the bodytext afterwards.

    /**
     * DocObject constructor that takes the DocObject's WSJ-XXXXXX-YYYY identifier and body text.
     * Body text is converted to all available unique words, and wiped depending on the value of WIPE.
     *
     */
    public DocObject(String textContent, String docNO){
        this.docNO = docNO;
        this.textLength = textContent.length()-1;
        this.textContent = textContent;
        convertToUniques();
    }

    /**
     * toString override to return WSJ-XXXXXX-YYYY identifier.
     *
     * @return  DOCNO
     */
    public String toString(){
        return textContent;
    }

    /**
     * Accessor for the length of a document.
     *
     * @return  textLength
     */
    int getDocLength() {
        return textLength;
    }
    String getDocNO() { return docNO; }

    /**
     * Accessor for unique words in document's body text.
     *
     * @return Unique words.
     */
    String[] getUniqueText() {
        return uniqueWords;
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
