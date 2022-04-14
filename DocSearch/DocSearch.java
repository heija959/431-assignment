package DocSearch;
import java.io.*;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class DocSearch {
    static long startTime = System.nanoTime();
    static final int HASH = 199; //61


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

    public static String shaver(String content, Pattern p) {
        return p.matcher(content).replaceAll("");
    }




    public static Object readDisk(Path source) throws IOException, ClassNotFoundException {
        ObjectInputStream o = new ObjectInputStream(new BufferedInputStream(new FileInputStream(source.toFile())));
        return o.readObject();
    }

    public static void timer(String in){
        long endTime = System.nanoTime();
        long duration = ((endTime - startTime)/1000000);
        System.out.println(in + " " + duration + "ms");
        startTime = System.nanoTime();
    }


    public static LinkedHashMap<String, Float> sortByValue(LinkedHashMap<String, Float> hm)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Float> > list
                = new LinkedList<Map.Entry<String, Float> >(
                hm.entrySet());

        // Sort the list using lambda expression
        list.sort((i1,i2) -> i1.getValue().compareTo(i2.getValue()));

        // put data from sorted list to hashmap
        LinkedHashMap<String, Float> temp = new LinkedHashMap<String, Float>();
        for (Map.Entry<String, Float> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    public static <K, V> Map<K, V> convertToTreeMap(Map<K, V> hashMap)
    {
        Map<K, V>
                treeMap = hashMap
                // Get the entries from the hashMap
                .entrySet()

                // Convert the map into stream
                .stream()

                // Now collect the returned TreeMap
                .collect(
                        Collectors

                                // Using Collectors, collect the entries
                                // and convert it into TreeMap
                                .toMap(
                                        Map.Entry::getKey,
                                        Map.Entry::getValue,
                                        (oldValue,
                                         newValue)
                                                -> newValue,
                                        TreeMap::new));

        // Return the TreeMap
        return treeMap;
    }


    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        // Declare important index objects
        Pattern shaver = Pattern.compile("[^\\w+|-]+");

        Scanner input = new Scanner(System.in);
        StringBuilder string = new StringBuilder();
        while (input.hasNext()) {
            string.append(input.nextLine());
            string.append(" ");
        }
        //timer("Inned");
        String[] sstring = (shaver(string.toString().toLowerCase(Locale.ROOT),shaver," ")).split(" ");
        String[] ssstring = Arrays.stream(sstring).distinct().toArray(String[]::new);
        System.out.println(Arrays.toString(ssstring));
        //timer("Truncked");
        LinkedList<Integer> loadingrange = new LinkedList<>();
        for(String s:sstring){
            loadingrange.add(h(s));
        }
        HashSet<Integer> set = new HashSet<>(loadingrange);
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

        //timer("Read");

        List<List<int[]>> retrieved = new LinkedList<>();
        for(String s:sstring){
            retrieved.add(maps.get(h(s)).get(s));

        }

        HashMap<String, Float> documents = new HashMap<String, Float>();

        for(List<int[]> i:retrieved){
            if(i != null){
                for(int[] j:i) {
                    documents.put(docnos[j[0]], -((Float) (float) j[1] / lens[j[0]]));
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
