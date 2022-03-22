package DocSearch;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

public class DocParse {

    // Main driver method
    public static void main(String[] args) throws Exception {
        System.out.println("Document stage:");
        long startTime = System.nanoTime();
        //Declaring a list of DocObjects
        ArrayList<DocObject> docList = new ArrayList<>();

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
                //System.out.println(test);
                if (token.equals("</DOC")){
                    //System.out.println("END DOC");
                    //System.out.println("T,D "+docNo+textContent);
                    DocObject test = new DocObject(docNo, textContent.toString());
                    docList.add(test);
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
        //System.out.println(docList);


        long endTime = System.nanoTime();
        long duration = ((endTime - startTime)/1000000);  //divide by 1000000 to get milliseconds.
        System.out.println("Time: "+duration+"ms");

        System.out.println("Flatten and invert stage, "+docList.size()+" documents: ");
        Map<String, List<Integer>> map = new HashMap<String, List<Integer>>();
        ArrayList<String> indexToDocNo = new ArrayList<>();
        ArrayList<Integer> indexToLen = new ArrayList<>();
        for (i = 0; i < docList.size(); i++){
            indexToDocNo.add(docList.get(i).getDOCNO());
            indexToLen.add(docList.get(i).getDocLength());
            for (String word:docList.get(i).getUniqueText()){
                ArrayList<Integer> temporaryList;
                if (map.containsKey(word)){
                    temporaryList = (ArrayList<Integer>) map.get(word);
                }
                else {
                    temporaryList = new ArrayList<>();
                }
                temporaryList.add(i);
                map.put(word, temporaryList);
            }
            docList.get(i).wipe();
        }
        System.out.println("Traversed");
        endTime = System.nanoTime();
        duration = ((endTime - startTime)/1000000);  //divide by 1000000 to get milliseconds.
        System.out.println("Time: "+duration+"ms");

        Map<String, int[]> convertedmap = new HashMap<>();
        for(String word:map.keySet()){
            int[] temporaryList = map.get(word).stream().mapToInt(x->x).toArray();
            convertedmap.put(word, temporaryList);
        }

        System.out.println("Object creation...");
        InvertedIndexObject index = new InvertedIndexObject(convertedmap);
        IndexObject info = new IndexObject(indexToDocNo, indexToLen);

        System.out.println("Saving index...");

        try
        {
            FileOutputStream fos = new FileOutputStream("index");
            ObjectOutputStream oos =  new ObjectOutputStream(new BufferedOutputStream(fos));
            oos.writeObject(index);
            oos.close();
            fos.close();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }

        try
        {
            FileOutputStream fos = new FileOutputStream("indexRaw");
            ObjectOutputStream oos =  new ObjectOutputStream(new BufferedOutputStream(fos));
            oos.writeObject(convertedmap);
            oos.close();
            fos.close();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }

        System.out.println("Saved.");
        endTime = System.nanoTime();
        duration = ((endTime - startTime)/1000000);  //divide by 1000000 to get milliseconds.
        System.out.println("Time: "+duration+"ms");
        System.out.println("Saving info...");

        try
        {
            FileOutputStream fos = new FileOutputStream("info");
            ObjectOutputStream oos =  new ObjectOutputStream(new BufferedOutputStream(fos));
            oos.writeObject(info);
            oos.close();
            fos.close();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }

        System.out.println("Saved.");
        endTime = System.nanoTime();
        duration = ((endTime - startTime)/1000000);  //divide by 1000000 to get milliseconds.
        System.out.println("Time: "+duration+"ms");
    }
}