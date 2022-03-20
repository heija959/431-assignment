package DocSearch;

public class DocObject {
    String DOCNO;
    String textContent;
    int textLength;


    public DocObject(String DOCNO, String textContent){
        this.DOCNO = DOCNO;
        this.textContent = textContent;
        this.textLength = textContent.length();
    }

    public String toString(){
        return DOCNO;
    }


    int getDocLength() {
        return textLength;
    }

    String getTextContent() {
        return textContent;
    }

    String getDOCNO(){
        return DOCNO;
    }

}
