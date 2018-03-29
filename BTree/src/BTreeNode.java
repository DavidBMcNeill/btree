
/**
 * "should be inner class of BTree" [why?]
 * 
 * @author DavidMcNeill
 *
 * @param <T>
 */
public class BTreeNode<TreeObject> {

	// optimal degree calculation:
	// (BTree node meta-data) + (2t - 1) * (size of tree object in bytes) + (2t
	// ... + 1) * (size of BTreeNode pointer) <= 4096 bytes

	// int n; boolean leaf; int location (cache)
	// metadata: ADD MORE TO HELP W/ IMPLEMENTATION
	private int numChildren;
	private boolean leaf;
	private int location; // the location of this node in the cache.
	private int size;

	// ALSO CONTAINS: sequence of tree objects; child pointers; parent pointer
	// [pointers are ints -- byte location (offset) of each child in the file]

	// byte offset? which node starts on which byte on the file
	// location: byte offset of this node in file

	// pointers are ints b/c byte location of Node in file.
	private int[] objectSequence; // 2*t - 1 TreeObjects
	private int[] childPointers; // 2*t child node pointers + 1 parent pointer
	private int parent; // + 1 parent pointer

	// allocate size of node at creation, fixed: full size: 2t - 1
	public BTreeNode(int degree) {
		// x = allocateNode(); // what is this?
		leaf = true;
		size = (2 * degree) - 1;

		// n[x] = 0
		// Disk-write(x);
		// T.root = x;
	}

	public boolean leaf() {
		return leaf;
	}
}
