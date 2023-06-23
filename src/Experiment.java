import java.util.*;

public class Experiment {

    private static List<Integer> randomList(int n) {
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            list.add(i);
        }
        Collections.shuffle(list);
        return list;
    }

    private static List<Integer> sortedList(int n) {
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            list.add(i);
        }
        return list;
    }

    private static BinomialHeap crateHeap(List<Integer> keys) {
        BinomialHeap heap = new BinomialHeap();
        for (int key : keys) heap.insert(key, Integer.toString(key));
        return heap;
    }

    private static void deleteMin(int numOfDeletions, BinomialHeap heap) {
        for (int i = 0; i < numOfDeletions; i++) {
            heap.deleteMin();
        }
    }

    public static Map<Integer, Map<String, Double>> experiment1() {
        Map<Integer, Map<String, Double>> map = new HashMap<>();
        for (int i = 1; i <= 5; i++) {
            Map<String, Double> innerMap = new HashMap<>();
            int numberOfInsertions = (int) Math.pow(3, i + 5) - 1;
            List<Integer> list = sortedList(numberOfInsertions);
            Double start = (double) System.nanoTime();
            BinomialHeap heap = crateHeap(list);
            Double end = (double) System.nanoTime();
            double mil = 1000000;
            double time = (end - start) / mil;
            innerMap.put("Time", time);
            innerMap.put("Number of trees", (double) heap.numOfTrees);
            innerMap.put("Number of links", (double) heap.numberOfLinks);
            map.put(numberOfInsertions, innerMap);

        }
        return map;
    }

    public static Map<Integer, Map<String, Double>> experiment2() {
        Map<Integer, Map<String, Double>> map = new HashMap<>();
        for (int i = 1; i <= 5; i++) {
            Map<String, Double> innerMap = new HashMap<>();
            int numberOfInsertions = (int) Math.pow(3, i + 5) - 1;
            List<Integer> list = randomList(numberOfInsertions);
            double mil = 1000000;
            Double start = (double) System.nanoTime();
            runExperiment(map, innerMap, numberOfInsertions, list, start, mil, numberOfInsertions/2);
        }
        return map;
    }

    public static Map<Integer, Map<String, Double>> experiment3() {
        Map<Integer, Map<String, Double>> map = new HashMap<>();
        for (int i = 1; i <= 5; i++) {
            Map<String, Double> innerMap = new HashMap<>();
            int numberOfInsertions = (int) Math.pow(3, i + 5) - 1;
            List<Integer> list = sortedList(numberOfInsertions);
            Collections.reverse(list);
            Double start = (double) System.nanoTime();
            double mil = 1000000;
            double ItemToRemain = Math.pow(2,5)-1;
            int numberOfDeletions = numberOfInsertions-(int)ItemToRemain;
            runExperiment(map, innerMap, numberOfInsertions, list, start, mil,(int)numberOfDeletions);
        }
        return map;
    }

    private static void runExperiment(Map<Integer, Map<String, Double>> map, Map<String, Double> innerMap, int numberOfInsertions, List<Integer> list, Double start, double mil,int numberOfDeletions) {
        BinomialHeap heap = crateHeap(list);
        deleteMin(numberOfDeletions, heap);
        Double end = (double) System.nanoTime();
        double time = (end -start) / mil;
        innerMap.put("Time",time );
        innerMap.put("Number of trees", (double) heap.numOfTrees);
        innerMap.put("Number of links", (double) heap.numberOfLinks);
        innerMap.put("Number of deletions", (double) heap.sumOfRanksDeleted);
        map.put(numberOfInsertions, innerMap);
    }
}


