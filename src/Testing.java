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
        int[] arr1 = {13, 4, 6, 25, 39, 16, 0, 32, 22, 20, 37, 15, 22};
//        int[] arr2 = {25, 23, 5, 23, 4, 5, 5, 4, 3, 8, 28, 24, 38, 13, 23, 13, 37, 19};
        int n = arr1.length;
        List<Integer> list = arrTOList(arr1);
        BinomialHeap heap2 = new BinomialHeap();
        insertListToHeap(list,heap2);
        BinomialHeap heap = new BinomialHeap();
        heap.meld(heap2);

        BinomialHeap.HeapItem item804 = heap.insert(14,"14");
        heap.deleteMin();
    }
}