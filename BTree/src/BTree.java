
/**
 * "Project only requires insert method"
 * 
 * @author DavidMcNeill
 *
 */
public class BTree {
	
	private int root; 
	
	public int search(x, K) {
		
	}
	
	public void insert(T, k) {
		// s : current node you're in. 
		// r : child node. 
		
		/*
		  r = root[T]
		  if (n[r] == 2*t - 1) { //node is full
		    BTreeNode s = allocate-node(); // BTreeNode constructor
		    root[T] = s;
		    leaf[s] = false;
		    n[s] = null;
		    c1[s] = r;
		    split(s, 1, r); // 1 is the index
		    insertNonFull(s, k); // recursive procedure
		  } else { // node isn't full
		    insertNonFull(s, k);
		  }
		 */
		
	}
	
	public void split(BTreeNode currentNode, int currentNodeIndex, BTreeNode childNode) {
		/*
		 BTreeNode z = allocate-node(); // initialize new node's attributes
		 leaf[z] = leaf[y];
		 n[z] = t - 1;
		 for (int j = 1; j <= t - 1; j++) {
		   key<j>[z] = key<j+1>[y];
		 }
		 if (!leaf[y]) {
		   for (int j = 1; j <= t; j++) {
		     c<j>[z] = c<j+1>[y];
		   }
		 }
		 n[y] = t -1;
		 for (int j = n[x] + 1; j >= i + 1; j--) {
		    c<j+1>[x] = c<j>[x];
		 }
		 c<i+1>[x] = z;
		 */
	}
	
	public void insertNonFull() {
		
	}

}
