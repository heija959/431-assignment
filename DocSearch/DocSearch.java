package DocSearch;
import java.io.*;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;

public class DocSearch {
    static final int HASH = 397; //199

    public static int h(String s){
        int t = 0;
        s=s.toLowerCase(Locale.ROOT);

        for(int i = 0; i < s.length(); i++)
        {
            t+=s.charAt(i);
        }

        return t%HASH;
    }

    public static String shaver(String content, Pattern p, String s) {
        return p.matcher(content).replaceAll(s);
    }

    public static Object readDisk(Path source) throws IOException, ClassNotFoundException {
        ObjectInputStream o = new ObjectInputStream(new BufferedInputStream(new FileInputStream(source.toFile())));
        return o.readObject();
    }

    public static LinkedHashMap<String, Float> sortByValue(LinkedHashMap<String, Float> hm)
    {
        List<Map.Entry<String, Float> > list
                = new LinkedList<Map.Entry<String, Float> >(
                hm.entrySet());
        list.sort((i1,i2) -> i1.getValue().compareTo(i2.getValue()));
        LinkedHashMap<String, Float> temp = new LinkedHashMap<String, Float>();
        for (Map.Entry<String, Float> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        Pattern shaver = Pattern.compile("[^\\w+|-]+");

        StringBuilder string = new StringBuilder();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            while ((line = in.readLine()) != null) {
                string.append(line);
                string.append(" ");
            }
        }

        String[] sstring = (shaver(string.toString().toLowerCase(Locale.ROOT),shaver," ")).split(" ");
        LinkedList<Integer> loadingrange = new LinkedList<>();

        for(String s:sstring){
            loadingrange.add(h(s));
        }

        ArrayList<HashMap<String, List<int[]>>> maps = new ArrayList<>();

        for(int i = 0; i<HASH; i++){
            HashMap<String, List<int[]>> map = new LinkedHashMap<>();
            maps.add(map);
        }

        for(int i:loadingrange){
            maps.set(i,(HashMap<String, List<int[]>>) readDisk(Path.of("index/" + i)));
        }

        String[] docnos = (String[]) readDisk(Path.of("index/docnos"));
        int[] lens = (int[]) readDisk(Path.of("index/lens"));

        List<List<int[]>> retrieved = new LinkedList<>();
        for(String s:sstring){
            retrieved.add(maps.get(h(s)).get(s));

        }

        HashMap<String, Float> documents = new HashMap<String, Float>();

        for(List<int[]> i:retrieved){
            if(i != null){
                for(int[] j:i) {
                    documents.put(docnos[j[0]], -((float) j[1] / lens[j[0]]));
                }
            }
        }
        LinkedHashMap<String, Float> documentsE = new LinkedHashMap<String, Float>(documents);
        documentsE = (sortByValue(documentsE));
        for(String s:documentsE.keySet()){
            System.out.println(s.toUpperCase(Locale.ROOT) +"\t"+(documentsE.get(s)*-1));
        }

    }

}
