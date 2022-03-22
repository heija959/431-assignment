package DocSearch;

import java.io.Serializable;
import java.util.Map;

public class InvertedIndexObject implements Serializable {
    Map<String, int[]> map = null;

    public InvertedIndexObject(Map<String, int[]> map){
        this.map = map;
    }

    public Map<String, int[]> getMap(){
        return map;
    }

}
