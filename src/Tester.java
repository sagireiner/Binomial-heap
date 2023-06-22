import java.util.*;

public class Tester {

    public static final int MAXKEY = 40; // keys are random value between 0 to MAXKEY exclude
    public static final int STEPS = 100; //the number of random actions to test on each heap
    public static final int MAXMELD = 100;//maximum size of heap to meld
    public static final int TESTS = 1;//number of heaps tested
    public static final int[] ACTIONS = {0,1,2,3,4};
    // 0 - insert, 1 - deleteMIn, 2 - meld, 3 - decreaseKey, 4 - delete



    public static void main(String[] args) throws Exception{
        for(int i =0; i<TESTS; i++){
            try {
                randomSteps(STEPS);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("\nDone!!!");
    }

    public static void randomSteps(int numberOfSteps) throws Exception{
        StringBuilder steps = new StringBuilder();
        int size = 0;
        List<Integer> keyslist = new ArrayList<>();
        Random rnd = new Random();
        BinomialHeap heap = new BinomialHeap();
        for(int i=0; i< numberOfSteps; i++){
            int ind = rnd.nextInt(ACTIONS.length);
            switch (ACTIONS[ind]){
                case 0:
                    int key = rnd.nextInt(MAXKEY);
                    steps.append("heap.insert(").append(key).append(",\"GG\");\n");
                    heap.insert(key,"GG" );
                    size++;
                    keyslist.add(key);
                    break;
                case 1:
                    steps.append("heap.deleteMin();\n");
                    heap.deleteMin();
                    if (size>0) size--;
                    Collections.sort(keyslist);
                    if (!keyslist.isEmpty()) keyslist.remove(0);
                    break;
                //need to add more
                case 2:
                    List<Integer> keys = randomKeysList(MAXMELD,MAXKEY);
                    BinomialHeap B = crateHeap(keys);
                    steps.append("heap.meld(").append(keys).append(")\n");
                    size += keys.size();
                    heap.meld(B);
                    keyslist.addAll(keys);
                    break;
                case 3:
                    if (heap.empty()) break;
                    List<BinomialHeap.HeapNode> lst =  toList(heap);
                    ind = rnd.nextInt(lst.size());
                    BinomialHeap.HeapItem item = lst.get(ind).getItem();
                    int key1 = item.getKey();
                    steps.append("heap.decreaseKey(").append(item.getKey()).append(")\n");
                    int diff = (item.getKey() == 0)? 0: rnd.nextInt(item.getKey());
                    heap.decreaseKey(item, diff);
                    keyslist.remove(keyslist.indexOf(key1));
                    keyslist.add(key1-diff);
                    break;
                case 4:
                    if (heap.empty()) break;
                    List<BinomialHeap.HeapNode> lst1 =  toList(heap);
                    ind = rnd.nextInt(lst1.size());
                    BinomialHeap.HeapItem item1 = lst1.get(ind).getItem();
                    int key2 = item1.getKey();
                    steps.append("heap.delete(").append(item1.getKey()).append(")\n");
                    heap.delete(item1);
                    size --;
                    keyslist.remove(keyslist.indexOf(key2));
                    break;
            }
            try {
                validityTest(heap);
            } catch (Exception e) {
                System.out.println("\nSteps Taken:\n" + steps+ "\n");
                System.out.println("\nHEAP: \n");
                heap.print();
                throw e;
            }
            if (size != heap.size()){
                System.out.println("\nSteps Taken:  " + steps + "\n");
                System.out.println("\nHEAP: \n");
                heap.print();
                throw new Exception("size is not what is should be");
            }
            List<Integer> heapKeys = keysList(heap);
            Collections.sort(heapKeys);
            Collections.sort(keyslist);
            if(!heapKeys.equals(keyslist)){
                System.out.println("\nSteps Taken:  " + steps + "\n");
                System.out.println("\nHEAP: \n");
                heap.print();
                System.out.println("Should be:" + keyslist);
                System.out.println("What we got:" + heapKeys);
                throw new Exception("KeysList do not match");
            }
        }
    }

    public static void validityTest(BinomialHeap heap) throws Exception{
//        if (toList(heap).size() != heap.size()) throw new Exception("problem with size()");
        if (heap.empty() != (heap.size() == 0)) throw new Exception("problem with empty()");
        if (heap.numTrees() != Integer.bitCount(heap.size())) throw new Exception("problem with numTrees()");
        if (heap.empty()) return;
        if (!itemNodeLink(heap)) throw new Exception("problem with item-node link");
        if (!treeSizeTest(heap)) throw new Exception("Problem with three size");
        if (!structureTest(heap)) throw new Exception("problem with heap structure");
        if (!rankTest(heap)) throw new Exception("problem with rank");
        if (!parentTest(heap)) throw new Exception("problem with parents");
        if (heap.findMin().getKey() != Collections.min(keysList(heap))) throw new Exception("problem with findMin()");
        if (!onlyOneEachTest(heap)) throw new Exception("trees of the same rank in heap found");
    }

    private static boolean treeSizeTest (BinomialHeap heap){
        for (BinomialHeap.HeapNode node: toList(heap)){
            if (node.getSubTreeSize() != Math.pow(2,node.getRank())) return false;
        }
        return true;
    }

    private static boolean onlyOneEachTest (BinomialHeap heap){
        Set<Integer> ranks = new HashSet<>();
        for (BinomialHeap.HeapNode tree: siblings(heap.getLast())){
            ranks.add(tree.getRank());
        }
        return ranks.size() == heap.numTrees();
    }
    private static boolean rankTest(BinomialHeap heap){
        for (BinomialHeap.HeapNode node: toList(heap)){
            BinomialHeap.HeapNode child = node.getChild();
            if (child == null && node.getRank() != 0) return false;
            if(child != null && node.getRank() != siblings(child).size()) return false;
        }
        return true;
    }
    private static boolean itemNodeLink (BinomialHeap heap){
        for (BinomialHeap.HeapNode node: toList(heap)){
            if (node.getItem().getNode() != node) return false;
        }
        return true;
    }
    private static boolean structureTest(BinomialHeap heap){
        for (BinomialHeap.HeapNode node: toList(heap)){
            if (!structureNodeTest(node)) return false;
        }
        return true;
    }
    private static boolean structureNodeTest (BinomialHeap.HeapNode parent){
        if (parent.getChild() == null) return true;
        for(BinomialHeap.HeapNode node: siblings(parent.getChild())){
            if(node.getItem().getKey() < parent.getItem().getKey()){
                return false;
            }
        }
        return true;
    }

    private static boolean parentTest(BinomialHeap heap){
        for (BinomialHeap.HeapNode node: toList(heap)){
            if (!parentNodeTest(node)) return false;
        }
        return true;
    }
    private static boolean parentNodeTest (BinomialHeap.HeapNode parent){
        if (parent.getChild() == null) return true;
        for(BinomialHeap.HeapNode node: siblings(parent.getChild())){
            if(node.getParent()!= parent) return false;
        }
        return true;
    }

    //***************************************************************************

    private static BinomialHeap crateHeap (List<Integer> keys)    {
        BinomialHeap heap = new BinomialHeap();
        for (int key: keys) heap.insert(key,Integer.toString(key));
        return heap;

    }

    private static List<Integer> randomKeysList (int maxLen,int maxVal){
        Random rnd = new Random();
        int  n = rnd.nextInt(maxLen);
        List<Integer> keysList = new ArrayList<>();
        for(int i =0; i< n; i++){
            keysList.add(rnd.nextInt(maxVal));
        }
        return keysList;
    }

    public static Set<BinomialHeap.HeapNode> siblings (BinomialHeap.HeapNode node)  {
        Set<BinomialHeap.HeapNode> siblings = new HashSet<>();
        siblings.add(node);
        BinomialHeap.HeapNode curr = node.getNext();
        while (curr != node){
            siblings.add(curr);
            curr = curr.getNext();
        }
        return siblings;
    }
    public static List<BinomialHeap.HeapNode> toList (BinomialHeap.HeapNode node){
        List<BinomialHeap.HeapNode> lst = new ArrayList<>();
        lst.add(node);
        BinomialHeap.HeapNode child  = node.getChild();
        if (child == null) return lst;
        for(BinomialHeap.HeapNode node1: siblings(child)){
            lst.addAll(toList(node1));
        }
        return lst;
    }

    public static List<BinomialHeap.HeapNode> toList (BinomialHeap heap){
        List<BinomialHeap.HeapNode> lst = new ArrayList<>();
        if(heap.getLast() == null) return lst;
        BinomialHeap.HeapNode last = heap.getLast();
        for(BinomialHeap.HeapNode node: siblings(last)){
            lst.addAll(toList(node));
        }
        return lst;
    }

    public static List<Integer> keysList (BinomialHeap heap){
        List<BinomialHeap.HeapNode> lst = toList(heap);
        List<Integer> ret = new ArrayList<>();
        for (BinomialHeap.HeapNode node: lst){
            ret.add(node.getItem().getKey());
        }
        return ret;
    }
}
