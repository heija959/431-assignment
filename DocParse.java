import java.nio.charset.StandardCharsets;
import java.util.*;
import java.io.*;
import java.util.regex.Pattern;

public class DocParse {

    // Main driver method
    public static void main(String[] args) throws Exception {
        long startTime = System.nanoTime();

        //File f = new File("wsj.xml");
        //Scanner s = new Scanner(f,"utf-8"); //.useDelimiter(" ")
        Pattern p = Pattern.compile("\\W*\\s");
        Scanner s = new Scanner(new FileInputStream("wsj.xml"), StandardCharsets.UTF_8).useDelimiter(p);
        int i = 0;
        int nos = 0;
        int docs = 0;
        StringBuilder textContent = new StringBuilder();
        String docNo = null;
        while(s.hasNext()) {
            i++;
            String token = s.next();
            //boolean matches = Pattern.matches("amp", token);
            if (token.charAt(0) == '<') {
                if (token.equals("<DOCNO")){
                    
                    //System.out.println("DOC NO");
                    nos++;

                    if (s.hasNext()){
                        token = s.next();
                        docNo = token;
                    }
                }

                if (token.equals("<TEXT")){
                    textContent = new StringBuilder();
                    while (s.hasNext()){
                        token = s.next();
                        if (token.equals("</TEXT")){
                            break;
                        }

                        textContent.append(" ").append(token);

                    }
                    
                    //System.out.println("Text content: "+textContent);
                }
                if (token.equals("</DOC")){
                    //System.out.println("END DOC");
                    //System.out.println("T,D "+docNo+textContent);
                    docs++;
                }
            }
            //System.out.println(token);
            //System.out.print("Token: "+token+"\n")
            // do something with "token"
        }
        if (docs == nos){
            System.out.println("Equal.");
        } else {
            System.out.println("INEQUAL!!");
        }

        long endTime = System.nanoTime();
        long duration = ((endTime - startTime)/1000000);  //divide by 1000000 to get milliseconds.
        System.out.println("Time: "+duration+"ms");
    }
}