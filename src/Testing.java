import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Testing {

    public static List<Integer> randomInsertion(int n){
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            list.add((int)(Math.random()*100));
        }
        return list;
    }
    public static void insertListToHeap(List<Integer> list, BinomialHeap heap){
        for (int i = 0; i < list.size(); i++) {
            heap.insert(list.get(i),""+i);
        }
    }

    public static List<Integer> arrTOList(int[] arr){
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            list.add(arr[i]);
        }
        return list;
    }
    public static void main(String[] args) {
        BinomialHeap heap = new BinomialHeap();
//        List<Integer> list = randomInsertion(100);
        int[] arr = {78, 91, 31, 19, 37, 43, 37, 88, 38, 81,22};
        List<Integer> list = arrTOList(arr);
        insertListToHeap(list,heap);
        System.out.println(list);
        System.out.println(Integer.toBinaryString(11));
    }
}