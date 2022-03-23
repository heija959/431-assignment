package DocSearch;

import java.io.Serializable;
import java.util.Map;

public class InvertedIndexObject implements Serializable {
    final Map<String, int[]> map;
    final String[] indexToDocNo;
    final int[] indexToLen;

    public InvertedIndexObject(Map<String, int[]> map, String[] indexToDocNo, int[] indexToLen){
        this.map = map;
        this.indexToDocNo = indexToDocNo;
        this.indexToLen = indexToLen;
    }

    public Map<String, int[]> getMap(){
        return map;
    }

    @SuppressWarnings("unused")
    int getLen(int i){
        return indexToLen[i];
    }

    String getDOCNO(int i){
        return indexToDocNo[i];
    }

}
