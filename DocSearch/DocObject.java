package DocSearch;
import java.util.Arrays;

public class DocObject{
    final String DOCNO;
    String textContent;
    final int textLength;
    String[] uniqueWords;
    final boolean WIPE = true;

    public DocObject(String DOCNO, String textContent){
        this.DOCNO = DOCNO;
        this.textLength = textContent.length();
        this.textContent = textContent;
        convertToUniques();
    }

    public String toString(){
        return DOCNO;
    }


    int getDocLength() {
        return textLength;
    }

    String[] getUniqueText() {
        return uniqueWords;
    }

    String getDOCNO(){
        return DOCNO;
    }

    void convertToUniques(){

        // https://stackoverflow.com/questions/13796928/how-to-get-unique-values-from-array ish
        this.uniqueWords = Arrays.stream(this.textContent.split(" ")).distinct().toArray(String[]::new);

        //
        if (WIPE){this.textContent = null;} //KEEP THIS RIGHT HERE BRO! Keeps memory careful
        //
    }

    void wipe(){
        this.uniqueWords = null;
    }

}
