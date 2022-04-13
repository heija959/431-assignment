package DocSearch;
import java.io.*;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;


public class DocSearch {

    void termsToResults(){}
    void resultsToRanks(){}
    void nArrayIntersects(){}
    void arrayIntersections(){}

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

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        // Declare important index objects
        InvertedIndexObject index;
        ArrayList<Long> rlist = new ArrayList<>();
        Pattern whitespace = Pattern.compile("\\s*");
        Pattern p = Pattern.compile("\\W*\\s");
        Pattern shaver = Pattern.compile("[^\\w+|-]+");

        Scanner input = new Scanner(System.in);
        StringBuilder string = new StringBuilder();
        while (input.hasNext()) {
            string.append(input.nextLine());
            string.append(" ");
        }
        timer("Inned");
        String[] sstring = (shaver(string.toString().toLowerCase(Locale.ROOT),shaver," ")).split(" ");
        String[] ssstring = Arrays.stream(sstring).distinct().toArray(String[]::new);
        System.out.println(Arrays.toString(ssstring));
        timer("Truncked");
        LinkedList<Integer> loadingrange = new LinkedList<>();
        for(String s:sstring){
            loadingrange.add(h(s));
        }
        HashSet<Integer> set = new HashSet<>(loadingrange);
        ArrayList<LinkedHashMap<String, int[]>> maps = new ArrayList<>();
        for(int i = 0; i<HASH; i++){
            LinkedHashMap<String, int[]> map = new LinkedHashMap<>();
            maps.add(map);
        }

        for(int i:loadingrange){
            maps.set(i,(LinkedHashMap<String, int[]>) readDisk(Path.of("index/" + i)));
        }
        timer("Read");
        List<int[]> retrieved = new LinkedList<>();
        for(String s:sstring){
            retrieved.add(maps.get(h(s)).get(s));
        }
        timer("Retrieve");
        //1-d the list and then print pretty, figure out ranking and parsing atomization.
        System.out.println(retrieved);
        timer("Grossly printing");



        System.out.println(set);




        //int[] results = (map.get("Korea"));

    }


}
