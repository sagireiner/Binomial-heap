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
        Map<Integer, Map<String, Double>> map1 = new HashMap<>();
        Map<Integer, Map<String, Double>> map2 = new HashMap<>();
        Map<Integer, Map<String, Double>> map3 = new HashMap<>();
        map1 = Experiment.experiment1();
        map2 = Experiment.experiment2();
        map3 = Experiment.experiment3();
    }
}