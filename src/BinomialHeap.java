import javax.swing.filechooser.FileSystemView;
import java.util.*;

/**
 * BinomialHeap
 * <p>
 * An implementation of binomial heap over non-negative integers.
 * Based on exercise from previous semester.
 */
public class BinomialHeap {
    public int size;
    public int sumOfRanksDeleted = 0;
    public int numberOfLinks = 0;

    public HeapNode last;
    public HeapNode min;

    public int numOfTrees;

    /**
     * public BinomialHeap()
     * <p>
     * Constructs an empty heap.
     */

    public BinomialHeap() {
        this.size = 0;
        this.last = null;
        this.min = null;
        this.numOfTrees = 0;
    }

    /**
     * Constructor for BinomialHeap with a given node
     */

    public BinomialHeap(HeapNode node) {
        // set the node to be the last and the min
        node.parent = null;
        this.last = node;
        this.min = node;

        // the size of the heap is 2^(rank+1)-1 and the number of trees is rank+1
        this.size = (int) Math.pow(2, node.rank + 1) - 1;
        this.numOfTrees = node.rank + 1;

        // set all siblings to be without a parent and update the min
        HeapNode curr = node.next;
        while (curr != node) {
            curr.parent = null;
            if (curr.item.key < this.min.item.key) {
                this.min = curr;
            }
            curr = curr.next;
        }
    }

    /**
     * pre: key > 0
     * <p>
     * Insert (key,info) into the heap and return the newly generated HeapItem.
     */

    public HeapItem insert(int key, String info) {
        // create a new node and meld it with the heap
        HeapItem item = new HeapItem(key, info);
        HeapNode node = new HeapNode(item);

        // if the heap is empty
        if (this.last == null) {
            this.last = node;
            this.min = node;
            this.size = 1;
            this.numOfTrees = 1;
            return item;
        }
        // add the node to the beginning of the heap
        HeapNode tmp = this.last.next;
        this.last.next = node;
        node.next = tmp;

        // update the min and size and number of trees
        if (node.item.key < this.min.item.key) {
            this.min = node;
        }
        this.size++;
        this.numOfTrees++;


        HeapNode prev = this.last;
        HeapNode currNode = node;
        HeapNode nextNode = node.next;
        // while the ranks are the same, link the trees
        while (currNode.rank == nextNode.rank && nextNode != this.last) {
            tmp = nextNode.next;
            currNode = linkTrees(currNode, nextNode);
            currNode.next = tmp;
            prev.next = currNode;
            nextNode = tmp;
            numOfTrees--;
        }

        // if the last node has the same rank as the first node, link them
        if (currNode.rank == nextNode.rank) {
            currNode = linkTrees(currNode, nextNode);
            currNode.next = currNode;
            this.last = currNode;
            numOfTrees--;
        }
        return item;
    }


    /**
     * Delete the minimal item
     */
    public void deleteMin() {
        if (this.empty()) {
            return;
        }
        if (this.size == 1) {
            this.last = null;
            this.min = null;
            this.size = 0;
            this.numOfTrees = 0;
            return;
        }

        HeapNode minNode = this.min;
        HeapNode newMin = minNode.next;

        sumOfRanksDeleted += minNode.rank;

        // find the prev of minNode and the new min
        HeapNode curr = minNode.next;
        while (curr != minNode) {
            if (curr.item.key < newMin.item.key) {
                newMin = curr;
            }
            if (curr.next == minNode) {
                break;
            }
            curr = curr.next;
        }
        // if minNode is last, prev of minNode is now the last node
        if (minNode == this.last) {
            this.last = curr;
        }
        this.min = newMin;
        // remove minNode from the list of trees
        curr.next = minNode.next;

        // update the NumOfTrees and size
        this.numOfTrees--;
        this.size -= (int) Math.pow(2, minNode.rank);
        // if minNode has children, build a new heap from them and meld it with the heap
        HeapNode child = minNode.child;

        if (child != null) {
            this.meld(new BinomialHeap(child));
        }
        this.min = findNewMIn();
    }

    /**
     * Return the minimal HeapItem
     */
    public HeapItem findMin() {
        return this.min.item;
    }

    public HeapNode findNewMIn() {
        HeapNode curr = this.last;
        HeapNode newMin = curr;
        do {
            if (curr.item.key < newMin.item.key) {
                newMin = curr;
            }
            curr = curr.next;
        } while (curr != this.last);
        return newMin;
    }

    /**
     * pre: 0<diff<item.key
     * <p>
     * Decrease the key of item by diff and fix the heap.
     */
    public void decreaseKey(HeapItem item, int diff) {
        item.key -= diff;
        HeapNode node = item.node;
        // if the node is not the root
        while (node.parent != null) {
            HeapNode parent = node.parent;
            // if the parent's key is bigger than the node's switch them
            if (parent.item.key > node.item.key) {
                HeapItem temp = parent.item;
                parent.item = node.item;
                node.item = temp;
                // update the node of the item
                parent.item.node = parent;
                node.item.node = node;
            }
            // if the parent is smaller than the node - stop
            else {
                break;
            }
            // continue to the parent
            node = parent;
        }
        // if the node is a root and its key is smaller than the min, update the min
        if (node.parent == null && node.item.key < this.min.item.key) {
            this.min = node;
        }

    }

    /**
     * Delete the item from the heap.
     */
    public void delete(HeapItem item) {
        // decrease the key of the item to be smaller than the min and delete the min
        this.decreaseKey(item, item.key - this.min.item.key + 1);
        this.deleteMin();
    }

    /**
     * Meld the heap with heap2
     */
    public void meld(BinomialHeap heap2) {
        int length1 = Integer.toBinaryString(this.size).length();
        int length2 = Integer.toBinaryString(heap2.size).length();

        if (Integer.toBinaryString(this.size).equals("0"))
            length1 = 0;
        if (Integer.toBinaryString(heap2.size).equals("0"))
            length2 = 0;

        //if this heap is empty, there is no combining of trees
        if (length1 == 0) {
            this.size = heap2.size;
            this.min = heap2.min;
            this.last = heap2.last;
            this.numOfTrees = heap2.numOfTrees;
            heap2.last = null;
            heap2.min = null;
        }

        //if both heaps aren't empty
        if (length1 != 0 && length2 != 0) {
            if (heap2.size == -11) {
                System.out.println("fix");
            } else {
                int carry = 0; //this will be the carry in the binary addition later.
                int biggestLength = Math.max(length1, length2);
                if (this.min.item.key > heap2.min.item.key)
                    this.min = heap2.min;

                HeapNode firstInHeap1 = this.last.next;
                StringBuilder binHeap1 = new StringBuilder(Integer.toBinaryString(this.size));
                binHeap1.reverse();
                char[] charsHeap1 = new char[binHeap1.length()];
                binHeap1.getChars(0, binHeap1.length(), charsHeap1, 0);

                HeapNode firstInHeap2 = heap2.last.next;
                StringBuilder binHeap2 = new StringBuilder(Integer.toBinaryString(heap2.size));
                binHeap2.reverse();
                char[] charsHeap2 = new char[binHeap2.length()];
                binHeap2.getChars(0, binHeap2.length(), charsHeap2, 0);

                //create arrays for each heap (the array's values will be the roots of trees in the heap)
                //create additional array for the final merged list
                HeapNode[] arrayH1 = new HeapNode[biggestLength];
                HeapNode[] arrayH2 = new HeapNode[biggestLength];
                HeapNode[] finalHeapArray = new HeapNode[biggestLength + 1];

                int i = 0;
                for (char c1 : charsHeap1) {

                    //if there is a tree of size i in heap1, enter the tree root in place i in the array.
                    //then move on to the next tree root in the heap
                    if (c1 == '1') {
                        arrayH1[i] = firstInHeap1;
                        firstInHeap1 = firstInHeap1.next;
                    } else {
                        arrayH1[i] = null;
                    }
                    i++;
                }

                //do the same for heap2, reset i in order to do that and not create a new variable.
                i = 0;
                for (char c2 : charsHeap2) {
                    //if there is a tree of size i in heap2, enter the tree root in place i in the array.
                    //then move on to the next tree root in the heap
                    if (c2 == '1') {
                        arrayH2[i] = firstInHeap2;
                        firstInHeap2 = firstInHeap2.next;
                    } else {
                        arrayH2[i] = null;
                    }
                    i++;
                }

                //now we want to fill the final array to be the array of the merged heaps the same way we created arrays for heap1,heap2
                //that means if in the merged heap we have a tree of size i, the root will be in arr[i], and else there'll be null.

                HeapNode tempMergedTree = null;
                boolean firstTime = true;


                for (int j = 0; j < biggestLength; j++) {

                    //in the binary addition there is no carry
                    if (carry == 0) {

                        if (arrayH1[j] != null && arrayH2[j] != null) {//if both heaps have a tree of size j link them
                            tempMergedTree = linkTrees(arrayH1[j], arrayH2[j]);
                            carry = 1;//in binary terms, we did 1+1 so we have to carry 1
                            continue;
                        }
                        if (arrayH1[j] == null && arrayH2[j] == null) {//both don't have a tree of size j
                            finalHeapArray[j] = null; //because the carry is 0
                            continue;
                        }
                        if (arrayH1[j] == null) {//if there is only in one heap, put it in the final since the carry is 0.
                            finalHeapArray[j] = arrayH2[j];
                            continue;
                        }
                        if (arrayH2[j] == null) {
                            finalHeapArray[j] = arrayH1[j];
                            continue;
                        }
                    }
                    //if the carry in the binary addition is 1
                    else {

                        if (arrayH1[j] != null && arrayH2[j] != null) {//if both heaps have a tree of size j link them
                            finalHeapArray[j] = tempMergedTree;
                            tempMergedTree = linkTrees(arrayH1[j], arrayH2[j]);
                            continue;
                        }
                        if (arrayH1[j] == null && arrayH2[j] == null) {//both don't have a tree of size j
                            //because the carry is 1, we can now put the merged tree in the final heap because there isnt a tree of that siz
                            finalHeapArray[j] = tempMergedTree;
                            carry = 0;
                            continue;
                        }
                        if (arrayH1[j] == null) {//if there is only in one heap, we link it with the temp merged since the carry is 1.
                            tempMergedTree = linkTrees(arrayH2[j], tempMergedTree);
                            continue;
                        }
                        if (arrayH2[j] == null) {
                            tempMergedTree = linkTrees(arrayH1[j], tempMergedTree);
                            continue;
                        }
                    }
                }

                if (carry == 1) {
                    finalHeapArray[biggestLength] = tempMergedTree;
                }
                // now have a representative array of the final heap.
                // remove the nulls padding it and  link the tree roots.
                int countNotNull = 0;
                for (int j = 0; j < finalHeapArray.length; j++) {
                    if (finalHeapArray[j] != null)
                        countNotNull++;
                }

                HeapNode[] finalHeapArrayRootsOnly = new HeapNode[countNotNull];
                int arrLength = 0;
                for (int j = 0; j < finalHeapArray.length; j++) {
                    if (finalHeapArray[j] != null) {
                        finalHeapArrayRootsOnly[arrLength] = finalHeapArray[j];
                        arrLength++;
                    }
                }

                if (arrLength == 1) {
                    finalHeapArrayRootsOnly[0].next = finalHeapArrayRootsOnly[0];
                    this.last = finalHeapArrayRootsOnly[0];
                } else {
                    //link every root to next one, without linking the last root.
                    for (int j = 0; j < arrLength - 1; j++) {
                        finalHeapArrayRootsOnly[j].next = finalHeapArrayRootsOnly[j + 1];
                    }
                }
                //link the last root to the first root
                finalHeapArrayRootsOnly[arrLength - 1].next = finalHeapArrayRootsOnly[0];
                this.last = finalHeapArrayRootsOnly[arrLength - 1];
                this.numOfTrees = arrLength;
                this.size += heap2.size;
            }
        }
        if (this.last != null) {
            this.min = findNewMIn();
        }
    }

    public HeapNode getLast() {
        return last;
    }


    public HeapNode linkTrees(HeapNode t1, HeapNode t2) {
        numberOfLinks++;
        HeapNode bigTree = t1;
        HeapNode smallTree = t2;

        if (t1.item.key < t2.item.key) {
            bigTree = t2;
            smallTree = t1;
        }
        smallTree.rank++;
        if (smallTree.child != null) {
            bigTree.next = smallTree.child.next;
            smallTree.child.next = bigTree;
        } else {
            bigTree.next = bigTree;
        }
        smallTree.child = bigTree;
        bigTree.parent = smallTree;
        return smallTree;
    }

    public void print() {
        System.out.println("**********************************************************");
        System.out.println("Binomial Heap:");
        System.out.println("Size: " + size);

        if (min != null) {
            System.out.println("Minimum Node: " + min.item.key);
        } else {
            System.out.println("No minimum node.");
        }

        System.out.println("Heap Nodes:");
        if (last != null) {
            Set<HeapNode> visited = new HashSet<>();
            printHeapNode(last, 0, visited);
        } else {
            System.out.println("No heap nodes.");
        }
        System.out.println("&******************************************************************");
    } //TODO: don't forget to delete

    private void printHeapNode(HeapNode node, int indentLevel, Set<HeapNode> visited) {
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < indentLevel; i++) {
            indent.append("    ");
        }

        System.out.println(indent + "Key: " + node.item.key);
        System.out.println(indent + "Info: " + node.item.info);
        System.out.println(indent + "Rank: " + node.rank);

        visited.add(node);

        if (node.child != null && !visited.contains(node.child)) {
            System.out.println(indent + "Child:");
            printHeapNode(node.child, indentLevel + 1, visited);
        }

        if (node.next != null && !visited.contains(node.next)) {
            System.out.println(indent + "Sibling:");
            printHeapNode(node.next, indentLevel, visited);
        }
    } //TODO: don't forget to delete


    /**
     * Return the number of elements in the heap
     */
    public int size() {
        return this.size;
    }

    /**
     * The method returns true if and only if the heap
     * is empty.
     */
    public boolean empty() {
        return this.min == null;
    }

    /**
     * Return the number of trees in the heap.
     */
    public int numTrees() {
        return this.numOfTrees;
    }

    public HeapNode getMin() {
        return this.min;
    }

    /**
     * Class implementing a node in a Binomial Heap.
     */
    public class HeapNode {
        public HeapItem item;
        public HeapNode child;
        public HeapNode next;
        public HeapNode parent;
        public int rank;

        /**
         * Constructor for empty HeapNode
         */

        public HeapNode() {
            this.item = null;
            this.child = null;
            this.next = null;
            this.parent = null;
            this.rank = 0;
        }

        /**
         * Constructor for HeapNode with a given HeapItem
         */

        public HeapNode(HeapItem item) {
            this.item = item;
            item.node = this;
            this.child = null;
            this.next = this;
            this.parent = null;
            this.rank = 0;
        }

        public HeapItem getItem() {
            return item;
        }

        public HeapNode getChild() {
            return child;
        }

        public HeapNode getNext() {
            return next;
        }

        public HeapNode getParent() {
            return parent;
        }

        public int getRank() {
            return rank;
        }

        public double getSubTreeSize() {
            return Math.pow(2, this.rank);
        }
    }

    /**
     * Class implementing an item in a Binomial Heap.
     */
    public class HeapItem {
        public HeapNode node;
        public int key;
        public String info;

        /**
         * Constructor for empty HeapItem
         */

        public HeapItem() {
            this.node = null;
            this.key = 0;
            this.info = null;
        }

        /**
         * Constructor for HeapItem
         */

        public HeapItem(int key, String info) {
            this.key = key;
            this.info = info;
            this.node = null;
        }

        public HeapNode getNode() {
            return node;
        }

        public int getKey() {
            return key;
        }
    }

}
