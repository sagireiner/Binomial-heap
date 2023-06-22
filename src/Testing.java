import java.util.*;

public class Testing {

    public static List<Integer> randomList(int start, int end){
        List<Integer> list = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            list.add(i);
        }
        Random rand = new Random(2);

        Collections.shuffle(list);
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
//        int[] arr = {1,2,3,4,5,6};
//        List<Integer> list = arrTOList(arr);
        BinomialHeap heap = new BinomialHeap();
        heap.insert(1,"1");
        heap.deleteMin();
        heap.deleteMin();
    }
}