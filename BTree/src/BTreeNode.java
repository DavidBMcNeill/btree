import java.util.Arrays;

/**
 * Represents a Node in a BTree.
 */
public class BTreeNode {

    // every node gets a unique 0-indexed number in the BTree when it's inserted.
    // this is needed for BTreeFile to place it in the binary file.
    private int id;

    private static int maxChildren = 2 * ArgsGenerate.degree;  // 2t
    private static int maxObjects = maxChildren - 1;           // 2t-1

    private boolean isLeaf;     // true if this is a leaf node
	private int numObjects;     // current number of objects stored in this node
    private int numChildren;    // current number of children stored in this node
	private int cacheLocation;  // the location of this node in the cache.

    // pointers are ints. they are the nodeID of the
    // node in the tree. each node has a unique nodeID.

    // 2t-1 max objects in our node
    private int[] objects = new int[maxObjects];

    // 2t children of our node
    private int[] children = new int[maxChildren];

    // all except root have 1 parent pointer
    private int parent;

    // size of this object in bytes: 7 ints, 2 arrays of ints, 1 boolean
    public static final int SIZE = (8*4) + (maxObjects*4) + (maxChildren*4) + 1;

    public BTreeNode() {
        id = 0;
        init();
    }

	public BTreeNode(int id) {
        this.id = id;
        init();
	}

	private void init() {
        numObjects = 0;
        isLeaf = true;
    }

    /**
     * Inserts a TreeObject into the list of objects this node contains.
     * Retuns true if insert was successful, else false.
     * @param obj TreeObject to insert
     */
	public boolean insert(int obj) {
	    if (isFull())
	        return false;

        objects[numObjects] = obj;
        numObjects++;
        Arrays.sort(objects);
        return true;
    }

    public int index() {
	    return id;
    }
	public boolean isLeaf() {
		return isLeaf;
	}
	public void isLeaf(boolean x) {
        isLeaf = x;
	}
	public int numObjects() {
        return objects.length;
    }
    public boolean isFull() {
	    return objects.length >= maxObjects;
    }
    public void setParent(BTreeNode node) {
	    parent = node.id;
    }
    public void setChild(BTreeNode node) {
	    children[numChildren] = node.id;
        numChildren++;
        // Arrays.sort(children);
    }

}
