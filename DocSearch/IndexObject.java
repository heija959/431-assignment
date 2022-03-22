package DocSearch;

import java.io.Serializable;

public class IndexObject implements Serializable {
    String[] indexToDocNo;
    int[] indexToLen;

    public IndexObject(String[] indexToDocNo, int[] indexToLen){
        this.indexToDocNo = indexToDocNo;
        this.indexToLen = indexToLen;
    }

    int getLen(int i){
        return indexToLen[i];
    }

    String getDOCNO(int i){
        return indexToDocNo[i];
    }
}
