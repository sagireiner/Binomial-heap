/**
 * BinomialHeap
 *
 * An implementation of binomial heap over non-negative integers.
 * Based on exercise from previous semester.
 */
public class BinomialHeap
{
	public int size;

	public HeapNode last;
	public HeapNode min;

	public int numOfTrees;

	/**
	 * public BinomialHeap()
	 *
	 * Constructs an empty heap.
	 *
	 */

	public BinomialHeap(){
		this.size = 0;
		this.last = null;
		this.min = null;
		this.numOfTrees = 0;
	}

	/**
	 *
	 * Constructor for BinomialHeap with a given node
	 *
	 */

	public BinomialHeap(HeapNode node){
		// set the node to be the last and the min
		node.parent = null;
		this.last = node;
		this.min = node;

		// the size of the heap is 2^(rank+1)-1 and the number of trees is rank+1
		this.size = (int) Math.pow(2, node.rank+1)-1;
		this.numOfTrees = node.rank+1;

		// set all siblings to be without a parent and update the min
		HeapNode curr = node.next;
		while (curr != node){
			curr.parent = null;
			if (curr.item.key < this.min.item.key){
				this.min = curr;
			}
			curr = curr.next;
		}
	}

	/**
	 *
	 * pre: key > 0
	 *
	 * Insert (key,info) into the heap and return the newly generated HeapItem.
	 *
	 */

	public HeapItem insert(int key, String info) {
		// create a new heap with one node and meld it with the heap
		HeapItem item = new HeapItem(key, info);
		this.meld(new BinomialHeap(new HeapNode(item)));
		return item;
	}

	/**
	 * 
	 * Delete the minimal item
	 *
	 */
	public void deleteMin() {
		HeapNode minNode = this.min;
		HeapNode newMin = minNode.next;

		// find the prev of minNode and the new min
		HeapNode curr = minNode.next;
		while (curr.next != minNode) {
			if (curr.item.key < newMin.item.key) {
				newMin = curr;
			}
			curr = curr.next;
		}
		// prev of minNode is now the last node
		if (minNode == this.last) {
			this.last = curr;
		}
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
	}

	/**
	 * 
	 * Return the minimal HeapItem
	 *
	 */
	public HeapItem findMin() {
		return this.min.item;
	} 

	/**
	 * 
	 * pre: 0<diff<item.key
	 * 
	 * Decrease the key of item by diff and fix the heap. 
	 * 
	 */
	public void decreaseKey(HeapItem item, int diff) {
		item.key -= diff;
		HeapNode node = item.node;
		// if the node is not the root
		while (node.parent!=null){
			HeapNode parent = node.parent;
			// if the parent's key is bigger than the node's switch them
			if (parent.item.key > node.item.key){
				HeapItem temp = parent.item;
				parent.item = node.item;
				node.item = temp;
				// update the node of the item
				parent.item.node = parent;
				node.item.node = node;
			}
			// if the parent is smaller than the node - stop
			else{
				break;
			}
			// continue to the parent
			node = parent;
		}
		// if the node is a root and its key is smaller than the min, update the min
		if (node.parent == null && node.item.key < this.min.item.key){
			this.min = node;
		}

	}

	/**
	 * 
	 * Delete the item from the heap.
	 *
	 */
	public void delete(HeapItem item) {
		// decrease the key of the item to be smaller than the min and delete the min
		this.decreaseKey(item, item.key - this.min.item.key + 1);
		this.deleteMin();
	}

	/**
	 * 
	 * Meld the heap with heap2
	 *
	 */
	public void meld(BinomialHeap heap2)
	{
		return; // should be replaced by student code   		
	}

	/**
	 * 
	 * Return the number of elements in the heap
	 *   
	 */
	public int size() {
		return this.size;
	}

	/**
	 * 
	 * The method returns true if and only if the heap
	 * is empty.
	 *   
	 */
	public boolean empty() {
		return this.min == null;
	}

	/**
	 * 
	 * Return the number of trees in the heap.
	 * 
	 */
	public int numTrees() {
		return this.numOfTrees;
	}

	/**
	 * Class implementing a node in a Binomial Heap.
	 *  
	 */
	public class HeapNode{
		public HeapItem item;
		public HeapNode child;
		public HeapNode next;
		public HeapNode parent;
		public int rank;

		/**
		 *
		 * Constructor for empty HeapNode
		 *
		 */

		public HeapNode(){
			this.item = null;
			this.child = null;
			this.next = null;
			this.parent = null;
			this.rank = 0;
		}

		/**
		 *
		 * Constructor for HeapNode with a given HeapItem
		 *
		 */

		public HeapNode(HeapItem item){
			this.item = item;
			this.child = null;
			this.next = this;
			this.parent = null;
			this.rank = 0;
		}
	}

	/**
	 * Class implementing an item in a Binomial Heap.
	 *  
	 */
	public class HeapItem{
		public HeapNode node;
		public int key;
		public String info;

		/**
		 *
		 * Constructor for empty HeapItem
		 *
		 */

		public HeapItem(){
			this.node = null;
			this.key = 0;
			this.info = null;
		}

		/**
		 *
		 * Constructor for HeapItem
		 *
		 */

		public HeapItem(int key, String info){
			this.key = key;
			this.info = info;
			this.node = null;
		}
	}

}
