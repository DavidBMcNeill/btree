import java.io.File;
import java.io.IOException;

public class BTree {

    private int t; // degree
    private int maxKeys, maxObjs;
    private static int nodeCount = 0;
    private BTreeNode root;
    private BTreeFile file;
    private boolean useCache; // if user elects to use cache
	private Cache cache;

    public BTree() throws IOException {
        t = ArgsGenerate.degree;
        maxObjs = t - 1;
        maxKeys = 2 * t - 1;
        root = allocateNode();
        file = new BTreeFile();
        useCache = false;

		if (ArgsGenerate.useCache) {
			useCache = true;
			cache = new Cache(ArgsGenerate.cacheSize);
		}
    }

    public BTree(File f) throws IOException {
        file = new BTreeFile(f);
        BTreeMetadata md = file.readTreeMetadata();

        //System.out.println(md);
        t = md.degree;
        maxKeys = 2 * t - 1;
        root = allocateNode();  // y is child
    }

//    public void splitChild2(BTreeNode parent, int leftIndex, BTreeNode left) {
//        BTreeNode right = allocateNode();
//        right.setLeaf(left.isLeaf());
//        right.setNumObjects(t - 1);
//
//        for (int j = 0; j < t - 1; j++) { // values for j?
//            right.setObject(j, left.getObject(j + 1));
//        }
//
//        if (!left.isLeaf()) {
//            for (int j = 0; j < t; j++) {
//                right.setKid(j, left.getKid(j + t));// this too
//            }
//        }
//
//        // left.setNumObjects(t - 1); Unnecessary -- getKid decrements numKids
//        // in node
//
//        for (int j = parent.getNumObjects() + 1; j > leftIndex + 1; j--) {
//            parent.setObject(j + 1, parent.getObject(j));
//        }
//
//        parent.setKid(leftIndex + 1, right);
//
//        for (int j = parent.getNumObjects(); j > leftIndex; j--) {
//            parent.setObject(j + leftIndex, parent.getObject(j));
//        }
//
//        parent.setObject(leftIndex, left.getObject(t - 1));
//
//        // write nodes to disk
//        file.write(right);
//        file.write(left);
//        file.write(parent);
//    }

    public void splitChild(BTreeNode x, int i) {
//        System.out.printf("splitChild: time to split a node, new node: %s\n", x);
//        System.out.printf("our tree currently has %d node(s)\n", nodeCount);
        BTreeNode z = allocateNode();
        BTreeNode y = x.getKid(i);
        z.setLeaf(y.isLeaf());
        z.setNumObjects(maxObjs);

        for (int j=0; j<maxObjs; j++) {
            z.setObject(j, y.getObject(j+t));
        }

        if (!y.isLeaf()) {
            for (int j=0; j<=t; j++) {
                // System.out.printf("j=%d, t=%d, y.numKids=%d\n", j, t, y.getNumKids());
                z.setKid(j, y.getKid(j+t));
            }
        }

        y.setNumObjects(maxObjs);
        for (int j=x.getNumObjects(); j>i; i--) {
            x.setObject(j+1, x.getObject(j));
        }

        x.setObject(i, y.getObject(t));
        x.setNumObjects(x.getNumObjects()+1);

        file.write(y);
        file.write(z);
        file.write(x);
//        System.out.println("split that node successfully");
//        System.out.printf("tree now has %d node(s)\n", nodeCount);

    }

    public void insertNonFull(BTreeNode x, TreeObject k) {
//        System.out.println("INSERT NON FULL");

        int i = x.getNumObjects()-1;

        if (x.isLeaf()) {
//            System.out.println("we are going to insert an object into a leaf node");

            while (i >= 1 && k.getKey() < x.getId()) {
                x.setObject(i+1, x.getObject(i));
                i--;
            }
            // System.out.printf("insertNonFull: i=%d\n", i);

            x.setObject(i+1, k);
            x.setNumObjects(i+1);
            // System.out.printf("  WRITING: %s, nodeCount=%d\n", x, nodeCount);
            file.write(x);

        } else {
//            System.out.println("we are going to insert an object into a non-leaf node");

            // System.out.println("insertNonFull: x is a leaf");
            while (i >= 1 && k.getKey() < x.getObject(i).getKey()) {
                i--;
            }
//            System.out.printf("insertNonFull: i=%d\n", i);

            i++;
            BTreeNode n = file.read(i);
//            System.out.printf("insertNonFull: n=%s\n", n);

            if (n.getNumKids() == maxKeys) {
                splitChild(x, i);
                if (k.getKey() > x.getObject(i).getKey()) {
                    i++;
                }
            }

//            System.out.printf("insertNonFull: i=%d\n", i);
            insertNonFull(x.getKid(i-1), k);
        }
    }

    public void insert(TreeObject k) {
//        System.out.printf("INSERTING: %s\n", k);
        BTreeNode r = root;

        if (r.getNumObjects() >= maxKeys) {
//            System.out.println("insert: root node is full");
            BTreeNode s = allocateNode();
            root = s;
            s.setLeaf(false);
            s.setNumObjects(0);
            s.setKid(0, r);

            splitChild(s, 0);
            insertNonFull(s, k);

        } else {
//            System.out.println("insert: root node is not full");
            insertNonFull(r, k);
        }

    }

    public static BTreeNode allocateNode() {
//        System.out.printf("allocating new node. nodeCount=%d\n", nodeCount);

        nodeCount++;
        BTreeNode node = new BTreeNode();
        node.setId(nodeCount);
        return node;
    }
    
	/**
	 * Decides whether to write to cache or write to file. 
	 * @param node
	 * @throws IOException
	 */
	public void writeNode(BTreeNode node) throws IOException {
		if (useCache) {
			cache.add(node);
		} else {
			file.write(node);
		}
	}

    /**
     * Searches the tree for a BtreeNode with a specific key.
     * Returns the BTreeNode with the key or null if nothing found.
     * @param key key of the node
     * @return BTreeNode with the key or null
     */
    public BTreeNode search(long key) {
        return searcher(root, key);
    }

    private BTreeNode searcher(BTreeNode node, long key) {
        int i = 0;

//        System.out.printf("root=%s\n", root);
//        System.out.printf("node=%s\n", node);
//        System.out.printf("tree obj=%s\n", node.getObject(i));
//        System.out.printf("obj key=%d\n", node.getObject(i).getKey());

//        System.out.printf("\tsearched node: %s\n", node);

        while (i < node.getNumObjects()) {
            if (key > node.getObject(i).getKey()) {
                i++;
            } else {
                break;
            }
        }
        if (i < node.getNumObjects()) {
            if (key == node.getObject(i).getKey())
                return node;
        }
        if (node.isLeaf()) {
            return null;
        }
        return searcher(node.getKid(i), key);
    }

    /**
     * Traverse the tree in-order, printing each node that is visited.
     */
    public void traverseInOrder() {
        System.out.println("-----------------------");
        inOrder(root);
        System.out.println("-----------------------");
    }

    private void inOrder(BTreeNode node) {
        for (int i=0; i<node.getNumKids(); i++) {
            inOrder(node.getKid(i));
            System.out.println(node);
        }
        // inOrder(node.getKid(node.getNumKids()-1));
    }

    public BTreeMetadata getMetaData() {
        return file.readTreeMetadata();
    }

}
