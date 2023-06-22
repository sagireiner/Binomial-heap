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
        int[] arr1 = {57, 944, 697, 950, 945, 13, 139};
        int[] arr2 = {13};
        int[] arr3 = {703, 91};
        List<Integer> list1 = arrTOList(arr1);
        List<Integer> list2 = arrTOList(arr2);
        List<Integer> list3 = arrTOList(arr3);
        BinomialHeap heap1 = new BinomialHeap();
        BinomialHeap heap2 = new BinomialHeap();
        BinomialHeap heap3 = new BinomialHeap();
        insertListToHeap(list1,heap1);
        insertListToHeap(list2,heap2);
        insertListToHeap(list3,heap3);
        BinomialHeap heap = new BinomialHeap();
        heap.insert(778,"gg");
        heap.deleteMin();
        heap.meld(heap1);
        heap.meld(heap2);
        heap.meld(heap3);
        heap.deleteMin();
    }
}