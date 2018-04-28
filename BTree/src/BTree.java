import java.io.File;
import java.io.IOException;

public class BTree {
    private int t; // degree
    private int maxKeys, nodeCount;
    private BTreeNode root, y;
    private BTreeFile file;
    boolean useCache; // if user elects to use cache
	Cache cache;

    public BTree() throws IOException {
        t = ArgsGenerate.degree;
        maxKeys = 2 * t - 1;
        System.out.println("maxObjects per Node: " + maxKeys);
        root = y = allocateNode();// y is child
        nodeCount = 1;// for root
        root.setId(nodeCount);
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
        t = md.degree;
        // t = ArgsGenerate.degree;

        maxKeys = 2 * t - 1;
        System.out.println("maxObjects per Node: " + maxKeys);
        root = y = allocateNode();// y is child
        nodeCount = 1;// for root
        root.setId(nodeCount-1);

    }

    public void splitChild(BTreeNode parent, int leftIndex, BTreeNode left) {
        BTreeNode right = allocateNode();
        // currently from list should be from file
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
//        file.write(child);  // disk-write(child)
//        file.write(z);      // disk-write(z)
//        file.write(x);      // disk-write(x)
    }

    public void insertNonFull(BTreeNode node, TreeObject object) {

        int numObjects = node.getNumObjects();
        int index = numObjects - 1;

//        System.out.println("numObjects: " + numObjects);


        if (node.isLeaf()) {
            // compare new object's key to
            // previous insert's key
            while (numObjects > 0 && object.getKey() < node.getObject(index).getKey()) {
                node.setObject(index + 1, node.getObject(index));
                index--;
            }
            if (index < 0) {
                node.setObject(0, object);
            } else {
                node.setObject(index, object);
            }

            file.write(node);

//             if (numObjects == 0) {
//                  node.setObject(0, object);
//             } else {
//                  node.setObject(index + 1, object);
//             }
        } else {
            // yeah we'll see
            while (index > 0 && object.getKey() < node.getObject(index).getKey()) {
                --index;
            }
            index++;
            // fake diskRead x's child at index i
            if (node.getKid(index).getNumObjects() == maxKeys) {
                splitChild(node, index, node.getKid(index));
                if (object.getKey() > node.getObject(index).getKey()) {
                    index++;
                }
            }
            // insertNonFull(node.getKid(index), object);
        }
    }

    public void insert(TreeObject object) {

        System.out.printf("inserting into tree: %s, tree objects=%d\n", object, nodeCount);

        nodeCount++;

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

    public BTreeNode allocateNode() {
        BTreeNode node = new BTreeNode();
        node.setId(nodeCount);
        nodeCount++;
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
