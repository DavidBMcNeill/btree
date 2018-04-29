import java.io.File;
import java.io.IOException;

public class BTree {
    private int t; // degree
    private int maxKeys;
    private static int nodeCount = 0;
    private BTreeNode root;
    private BTreeFile file;
    private boolean useCache; // if user elects to use cache
	private Cache cache;

    public BTree() throws IOException {
        t = ArgsGenerate.degree;
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

    public void splitChild(BTreeNode parent, int leftIndex, BTreeNode left) {
        BTreeNode right = allocateNode();
        right.setLeaf(left.isLeaf());
        right.setNumObjects(t - 1);

        for (int j = 0; j < t - 1; j++) { // values for j?
            right.setObject(j, left.getObject(j + 1));
        }

        if (!left.isLeaf()) {
            for (int j = 0; j < t; j++) {
                right.setKid(j, left.getKid(j + t));// this too
            }
        }

        // left.setNumObjects(t - 1); Unnecessary -- getKid decrements numKids
        // in node

        for (int j = parent.getNumObjects() + 1; j > leftIndex + 1; j--) {
            parent.setObject(j + 1, parent.getObject(j));
        }

        parent.setKid(leftIndex + 1, right);

        for (int j = parent.getNumObjects(); j > leftIndex; j--) {
            parent.setObject(j + leftIndex, parent.getObject(j));
        }

        parent.setObject(leftIndex, left.getObject(t - 1));

        // write nodes to disk
        file.write(right);
        file.write(left);
        file.write(parent);
    }

    public void insertNonFull(BTreeNode node, TreeObject object) {

        int i = node.getNumObjects()-1;

        if (node.isLeaf()) {
            while (i >= 1 && object.getKey() < node.getId()) {
                node.setObject(i+1, node.getObject(i));
                i--;
            }
            node.setObject(i+1, object);
            node.setNumObjects(i+1);
            file.write(node);

        } else {
            while (i >= 1 && object.getKey() < node.getObject(i).getKey()) {
                i--;
            }

            i++;
            BTreeNode n = file.read(i);

            if (n.getNumKids() == maxKeys) {
                splitChild(node, i, node.getKid(i));
                if (object.getKey() > node.getObject(i).getKey()) {
                    i++;
                }
            }

            insertNonFull(node.getKid(i), object);
        }
    }

    public void insert(TreeObject object) {

//        System.out.printf("inserting into tree: %s, tree objects=%d\n", object, nodeCount);

        // nodeCount++;

//        BTreeNode child = allocateNode();
//        child = root;
        BTreeNode child = root;

        // System.out.println("r's id: " + r.getId());
        if (child.getNumObjects() == maxKeys) {
            BTreeNode parent = allocateNode();
            root = parent;
            // node.setId(++nodeCount);
            // System.out.println("root id: " + root.getId());

            parent.setLeaf(false);
            parent.setNumObjects(0);
            parent.setKid(0, child); // addKid now increments numKids;

            splitChild(parent, 0, child);
            insertNonFull(parent, object);

        } else {
            insertNonFull(child, object);
        }

    }

    public static BTreeNode allocateNode() {
        System.out.printf("allocating new node. nodeCount=%d\n", nodeCount);

        BTreeNode node = new BTreeNode();
        node.setId(nodeCount);
        nodeCount++;
        System.out.printf("NODE COUNT: %d\n", nodeCount);
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

        System.out.printf("\tsearched node: %s\n", node);

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
        inOrder(root);
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
