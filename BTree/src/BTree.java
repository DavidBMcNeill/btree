
/**
 * "Project only requires insert method"
 * 
 * @author DavidMcNeill
 *
 */
public class BTree {
	
	private BTreeNode root;
	private IUDoubleLinkedList list;
	private int currentNodeID;  // incremented every time a node is inserted

	public BTree() {
        currentNodeID = 0;
	    list = new IUDoubleLinkedList();
	    root = new BTreeNode(currentNodeID);
    }

    /**
     * Insert BTreeNode into the tree.
     * @param k
     */
    public void insert(int k) {

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

        // not sure about k's type yet. should it
        // be a BTreeNode or an int for the key.

        if (root.isFull()) {
            currentNodeID++;
            BTreeNode s = new BTreeNode(currentNodeID);
            root= s;
            s.setChild(root);
            splitChild(s, 1, root);
            insertNotFull(s, k);
        } else {
            insertNotFull(root, k);
        }

    }

    public boolean splitChild(BTreeNode x, int i, BTreeNode y) {
	    // x = currentNode, i = currentNodeIndex, y = childNode

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

        // TODO: split child node in the tree
	    // TODO: get the node index (is this the variable 'i'?
	    int fakeIndex = 666;

	    BTreeNode z = new BTreeNode(fakeIndex);
        z.isLeaf(y.isLeaf());
        z.setParent(x);

        return true;
    }

    public boolean insertNotFull(BTreeNode x, int k) {
        // TODO: insertNotFull method in BTree
        return true;
    }


}
