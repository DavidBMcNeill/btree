
/**
 * "should be inner class of BTree" [why?]
 * 
 * @author DavidMcNeill
 *
 * @param <TreeObject>
 */
public class BTreeNode<TreeObject> {

	// int n; boolean leaf; int location (cache)
	// metadata: ADD MORE TO HELP W/ IMPLEMENTATION
	private int numChildren;
	private boolean leaf;
	private int location; // the location of this node in the cache.

    // every node gets a unique 0-indexed number in the BTree when it's inserted.
    // this is needed for BTreeFile to place it in the binary file.
	private int index;

	// ALSO CONTAINS: sequence of tree objects; child pointers; parent pointer
	// [pointers are ints -- byte location (offset) of each child in the file]

	// byte offset? which node starts on which byte on the file
	// location: byte offset of this node in file

	// pointers are ints b/c byte location of Node in file.
	private int[] objectSequence; // 2*t - 1 TreeObjects
	private int[] childPointers; // 2*t child node pointers + 1 parent pointer
	private int parent; // + 1 parent pointer

    private static int size;

	// allocate size of node at creation, fixed: full size: 2t - 1
	public BTreeNode(int degree, int index) {
        this.index = index;

		// x = allocateNode(); // what is this?
		leaf = true;
		size = (2 * degree) - 1;

		// n[x] = 0
		// Disk-write(x);
		// T.root = x;
	}

	public BTreeNode() {

	}

	public static int size() {
        // optimal degree calculation:
        // (BTree node meta-data) + (2t - 1) * (size of tree object in bytes) + (2t
        // ... + 1) * (size of BTreeNode pointer) <= 4096 bytes
        return 12345;
    }

    public int index() {
	    return index;
    }

	public boolean leaf() {
		return leaf;
	}
}
