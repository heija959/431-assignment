package DocSearch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IndexObject implements Serializable {
    Map<String, List<Integer>> map;
    ArrayList<String> indexToDocNo;
    ArrayList<Integer> indexToLen;

    public IndexObject(Map<String, List<Integer>> map, ArrayList<String> indexToDocNo, ArrayList<Integer> indexToLen){
        this.map = map;
        this.indexToDocNo = indexToDocNo;
        this.indexToLen = indexToLen;
    }

    int getLen(int i){
        return indexToLen.get(i);
    }

    String getDOCNO(int i){
        return indexToDocNo.get(i);
    }

    Map<String, List<Integer>> getMap(){
        return map;
    }

}
