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
    final int[] indexToLen;
    final String[] indexToDocNO;

    /**
     * InvertedIndexObject constructor that sets input variables to class variables.
     * @param map          HashMap/Map of words correspondent to their index positions in the document number array.
     * @param indexToLen   Array storing the length of the body text of the document.
     * @param indexToDocNO
     */
    public InvertedIndexObject(Map<String, int[]> map, int[] indexToLen, String[] indexToDocNO) {
        this.map = map;
        this.indexToLen = indexToLen;
        this.indexToDocNO = indexToDocNO;
    }

    public InvertedIndexObject(Map<String, int[]> map) {
        this.map = map;
        this.indexToLen = null;
        this.indexToDocNO = null;
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

    String getDocNO(int i) { return indexToDocNO[i]; }




}
