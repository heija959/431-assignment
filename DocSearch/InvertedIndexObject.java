package DocSearch;

import java.io.Serializable;
import java.util.Map;

/**
 * Represents an inverted index data structured that can be serialized to disk.
 *
 * @author heija959
 */
public class InvertedIndexObject implements Serializable {

    final Map<String, int[]> map;
    final String[] indexToDocNo;
    final int[] indexToLen;

    /**
     * InvertedIndexObject constructor that sets input variables to class variables.
     *
     * @param map          HashMap/Map of words correspondent to their index positions in the document number array.
     * @param indexToDocNo Document number array storing the WSJ-XXXXXX-YYYY identifier.
     * @param indexToLen   Array storing the length of the body text of the document.
     */
    public InvertedIndexObject(Map<String, int[]> map, String[] indexToDocNo, int[] indexToLen) {
        this.map = map;
        this.indexToDocNo = indexToDocNo;
        this.indexToLen = indexToLen;
    }
    public InvertedIndexObject(){
        this.map = null;
        this.indexToLen = null;
        this.indexToDocNo = null;
    }

    /**
     * Accessor function for the map object.
     *
     * @return Map object
     */
    public Map<String, int[]> getMap() {
        return map;
    }

    /**
     * Accessor function for the lengths of documents.
     *
     * @return Document lengths array.
     */
    @SuppressWarnings("unused")
    int getLen(int i) {
        return indexToLen[i];
    }

    /**
     * Accessor function for the document number (WSJ-XXXXXX-YYYY identifier).
     *
     * @return Document number array.
     */
    String getDOCNO(int i) {
        return indexToDocNo[i];
    }

}
