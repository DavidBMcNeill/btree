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

    // We don't need this if we have the size and the id
	private int cacheLocation;  // the location of this node in the cache.

    // pointers are ints. they are the nodeID of the
    // node in the tree. each node has a unique nodeID.

    // 2t-1 max objects in our node
    private long[] objects = new long[maxObjects];

    // 2t children of our node
    private long[] children = new long[maxChildren];

    // all except root have 1 parent pointer
    private long parent;

    // size of this object in bytes: 8 ints, 2 arrays of ints, 1 boolean
    public static final int SIZE = (8*4) + (maxObjects*4) + (maxChildren*4) + 1;

    public BTreeNode() {
        id = 0;
        init();
    }

	public BTreeNode(int id) {
        this.id = id;
        init();
	}

	// TODO: too many arguments! break this up into methods?
	public BTreeNode(int id, boolean isLeaf, long parent, int numChildren, int numObjects, long[] objects, long[] children) {
        this.id = id;
        this.isLeaf = isLeaf;
        this.parent = parent;
        this.numChildren = numChildren;
        this.numObjects = numObjects;
        this.objects = objects;
        this.children = children;
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
    public long getParent() {
        return parent;
    }
    public int numChildren() {
	    return numChildren;
    }
    public long getObject(int index) {
        return objects[index];
    }
    public long getChild(int index) {
	    return children[index];
    }
    public void setChild(BTreeNode node) {
	    children[numChildren] = node.id;
        numChildren++;
        // Arrays.sort(children);
    }
    public long[] getObjects() {
	    return objects;
    }
    public long[] getChildren() {
	    return children;
    }
}
