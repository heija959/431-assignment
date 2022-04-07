package DocSearch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestDGap {

    public static int[] doDgap(List<Integer> obj){
        int[] in = obj.stream().mapToInt(i->i).toArray();
        int last = 0;
        int[] out = new int[in.length];

        for(int i = 0; i < in.length; i++) {
            int j = in[i];
            out[i] = j-last;
            last=j;
        }

        return out;
    }

    public static int[] undies(int[] obj){

        int[] back = new int[obj.length];
        int last=0;
        for(int i = 0; i < obj.length; i++) {
            int j = obj[i];
            back[i] = j+last;
            last=back[i];
        }
        return back;
    }

    public static void main(String[] args) throws Exception {

        List<Integer> in = new ArrayList<>();
        int[] oi = {11760, 11886, 12106, 15823, 16886, 20730, 21353, 28941, 30986, 39193, 40388, 42819, 44627, 46368, 46741, 50604, 55136, 57625, 57870, 61361, 66392, 66705, 69617, 70924, 73022, 75300, 75882};
        for(int x:oi){
            in.add(x);
        }
        System.out.println(Arrays.toString(oi));
        System.out.println(Arrays.toString(undies(doDgap(in))));

    }

}
