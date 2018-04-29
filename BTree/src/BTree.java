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
			cache = new Cache(ArgsGenerate.cacheSize, file);
		}
	}

	public BTree(File f) throws IOException {
		file = new BTreeFile(f);
		BTreeMetadata md = file.readTreeMetadata();

		// System.out.println(md);
		t = md.degree;
		maxKeys = 2 * t - 1;
		root = allocateNode(); // y is child
	}

	public void splitChild(BTreeNode parent, int leftIndex, BTreeNode left) throws IOException {
		BTreeNode right = allocateNode();
		right.setLeaf(left.isLeaf());
		right.setNumObjects(t - 1);

		for (int j = 0; j < t - 1; j++) { // values for j?
			right.setObject(j, left.getObject(left.getNumObjects() - 1)); // changed...
		}

		if (!left.isLeaf()) {
			for (int j = 0; j < t; j++) {
				right.setKid(j, left.getKid(j + t));// this too
			}
		}

		for (int j = parent.getNumObjects() + 1; j > leftIndex + 1; j--) {
			parent.setObject(j + 1, parent.getObject(j));
		}

		parent.setKid(leftIndex + 1, right);

		for (int j = parent.getNumObjects(); j > leftIndex; j--) {
			parent.setObject(j + leftIndex, parent.getObject(j));
		}

		parent.setObject(leftIndex, left.getObject(t - 1));

		// write nodes to cache or disk
		writeNode(left);
		writeNode(parent);
		writeNode(right);
	}

	public void insertNonFull(BTreeNode node, TreeObject object) throws IOException {

		int i = node.getNumObjects();

		if (node.isLeaf()) {
			while (i >= 1 && object.getKey() < node.peekObject(i-1).getKey()) {
				node.setObject(i, node.getObject(i-1));
				i--;
			}
			node.setObject(i, object);
			System.out.printf("WRITING: %s, nodeCount=%d\n", node, nodeCount); // <--
																				// PRINTS
																				// THE
																				// LINE
			writeNode(node);

		} else {
			int j = 0;
			// find correct child
			while (j < node.getNumObjects() && object.getKey() > node.getObject(j).getKey()) {
				j++;
			}
			
			if (node.peekKid(j).getNumObjects() == 2*t - 1) {
				splitChild(node,j,node.peekKid(j)); //call split on right child
				
				if (object.getKey() > node.getObject(j).getKey()) {
					j++;
				}
			}
			insertNonFull(node.peekKid(j), object);
		}
	}

	public void insert(TreeObject object) throws IOException {

		System.out.printf("inserting %s\n", object);

		// nodeCount++;

		// BTreeNode child = allocateNode();
		// child = root;
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
		nodeCount++;
		System.out.printf("allocating new node. nodeCount=%d\n", nodeCount);
		BTreeNode node = new BTreeNode();
		node.setId(nodeCount);
		return node;
	}

	/**
	 * Decides whether to write to cache or write to file.
	 * 
	 * @param node
	 * @throws IOException
	 */
	public void writeNode(BTreeNode node) throws IOException {
		if (useCache) {
			if (cache.indexOf(node) == -1) {
				cache.add(node);
			}
		} else {
			file.write(node);
		}
	}

	/**
	 * 
	 */
	public BTreeNode readNode(int nodeIndex) throws IOException {
		BTreeNode retrievedNode = null;
		if (useCache) {
			retrievedNode = cache.getNode(nodeIndex);
		} else {
			retrievedNode = file.read(nodeIndex);
		}
		return retrievedNode;
	}

	/**
	 * 
	 * @return
	 */
	public Cache getCache() {
		return cache;
	}

	/**
	 * Searches the tree for a BtreeNode with a specific key. Returns the
	 * BTreeNode with the key or null if nothing found.
	 * 
	 * @param key
	 *            key of the node
	 * @return BTreeNode with the key or null
	 */
	public BTreeNode search(long key) {
		return searcher(root, key);
	}

	private BTreeNode searcher(BTreeNode node, long key) {
		int i = 0;

		// System.out.printf("root=%s\n", root);
		// System.out.printf("node=%s\n", node);
		// System.out.printf("tree obj=%s\n", node.getObject(i));
		// System.out.printf("obj key=%d\n", node.getObject(i).getKey());

		System.out.printf("\tsearched node: %s\n", node);

		while (i < node.getNumObjects()) {
			if (key > node.peekObject(i).getKey()) {
				i++;
			} else {
				break;
			}
		}
		if (i < node.getNumObjects()) {
			if (key == node.peekObject(i).getKey())
				return node;
		}
		if (node.isLeaf()) {
			return null;
		}
		return searcher(node.peekKid(i), key);
	}

	/**
	 * Traverse the tree in-order, printing each node that is visited.
	 */
	public void traverseInOrder() {
		inOrder(root);
	}

	private void inOrder(BTreeNode node) {
		for (int i = 0; i < node.getNumKids(); i++) {
			inOrder(node.peekKid(i));
			System.out.println(node);
		}
	}

	public BTreeMetadata getMetaData() {
		return file.readTreeMetadata();
	}

}
