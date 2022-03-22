package DocSearch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IndexObject implements Serializable {
    ArrayList<String> indexToDocNo;
    ArrayList<Integer> indexToLen;

    public IndexObject(ArrayList<String> indexToDocNo, ArrayList<Integer> indexToLen){
        this.indexToDocNo = indexToDocNo;
        this.indexToLen = indexToLen;
    }

    int getLen(int i){
        return indexToLen.get(i);
    }

    String getDOCNO(int i){
        return indexToDocNo.get(i);
    }
}
