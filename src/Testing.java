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
        int[] arr1 = {535};
        int[] arr2 = {185, 81, 215, 508, 444, 175, 324, 304, 907, 245, 116, 114};
        int n = arr1.length;
        List<Integer> list1 = arrTOList(arr1);
        BinomialHeap heap1 = new BinomialHeap();
        BinomialHeap heap2 = new BinomialHeap();
        List<Integer> list2 = arrTOList(arr2);
        insertListToHeap(list1,heap1);
        insertListToHeap(list2,heap2);
        BinomialHeap heap = new BinomialHeap();
        heap.insert(535,"535");
        heap.meld(heap1);
        heap.deleteMin();
    }
}