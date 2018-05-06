import java.io.File;
import java.io.IOException;

public class BTree {
	private static int t; // degree
	private int maxKeys;
	private static int nodeCount = 1;
	private BTreeNode root;
	private BTreeFile file;
	private boolean useCache; // if user elects to use cache
	private Cache cache;

	public BTree() throws IOException {
		t = ArgsGenerate.degree;
		maxKeys = (2 * t) - 1;
		root = allocateNode();
		root.maxChildren = 2 * t;
		root.maxObjects = maxKeys;
		root.SIZE = 20 + // 5 4b ints
				1 + // 1 1b boolean
				(root.maxObjects * TreeObject.SIZE) + // 2t-1 object ids of b int //TreeObject.SIZE
				(root.maxChildren * BTreeNode.SIZE); // 2t kid ids of 4b int; //BTreeNode.SIZE)//md.nodeSize;
		file = new BTreeFile();
		useCache = ArgsGenerate.useCache;

		if (useCache) {
			useCache = true;
			cache = new Cache(ArgsGenerate.cacheSize, file);
		}
	}

	public BTree(File f) throws IOException {
		file = new BTreeFile(f);
		BTreeMetadata md = file.readTreeMetadata();

		System.out.println(md);
		System.out.println(TreeObject.SIZE);

		t = md.degree;
		maxKeys = 2 * t - 1;
		root.maxChildren = 2 * t;
		root.maxObjects = maxKeys;
		root.SIZE = md.nodeSize;
//				20 + // 5 4b ints
//				1 + // 1 1b boolean
//				(root.maxObjects * TreeObject.SIZE) + // 2t-1 object ids of b int //TreeObject.SIZE
//				(root.maxChildren * BTreeNode.SIZE); // 2t kid ids of 4b int; //BTreeNode.SIZE)//
		root = file.read(1);
		if (ArgsSearch.useCache) {
			useCache = true;
			cache = new Cache(ArgsSearch.cacheSize, file);
		}
		// System.out.println(md);
		// System.out.println(BTreeNode.SIZE);
	}

	public void splitChild(BTreeNode parent, int leftIndex, BTreeNode left) throws IOException {
		BTreeNode right = allocateNode();
		right.setLeaf(left.isLeaf());

		for (int j = 0; j < t - 1; j++) {
			right.setObject(j, left.getObject(j + t));
		}

		if (!left.isLeaf()) {
			int i = 0;
			while (left.getNumKids() > t) {
				right.setKid(i, left.getKid(t)); // t NOT t+1 b/c of 1-off error
				i++;
			}
			// for (int j = 0; j < t; j++) { // should be a while condition...
			//
			// right.setKid(j, left.getKid(j + t));
			// }
		}

		for (int j = parent.getNumObjects(); j > leftIndex; j--) {
			parent.setKid(j + 1, parent.getKid(j));
		}

		parent.setKid(leftIndex + 1, right);

		for (int j = parent.getNumObjects(); j > leftIndex; j--) {
			parent.setObject(j + 1, parent.getObject(j));
		}

		parent.setObject(leftIndex, left.getObject(t - 1));

		// write nodes to cache or disk
		writeNode(left);
		writeNode(right);
		writeNode(parent);
	}

	public void insertNonFull(BTreeNode node, TreeObject object) throws IOException {

		int i = node.getNumObjects();

		if (node.isLeaf()) {
			while (i >= 1 && object.getKey() < node.peekObject(i - 1).getKey()) {
				node.setObject(i, node.getObject(i - 1));
				i--;
			}
			node.setObject(i, object);
			System.out.printf("WRITING object %s to node %s, nodeCount=%d\n", node.peekObject(i).getKey(), node.getId(),
					nodeCount);

			if (nodeCount == 3902) {
				System.out.println("");
			}
			writeNode(node);

		} else {
			int j = 0;
			// find correct child
			while (j < node.getNumObjects() && object.getKey() > node.peekObject(j).getKey()) {
				j++;
			}

			// System.out.printf("j=%d, kids=%d, kid=%s\n", j,
			// node.getNumKids(), node.peekKid(j).getId());

			if (node.peekKid(j) == null) {
				System.err.printf("accessing kid at bad index: %s\n", node);
				return;
			}

			if (node.peekKid(j).getNumObjects() == 2 * t - 1) {
				splitChild(node, j, node.peekKid(j)); // call split on right
														// child

				if (object.getKey() > node.peekObject(j).getKey()) {
					j++;
				}
			}
			insertNonFull(node.peekKid(j), object);
		}
	}

	public void insert(TreeObject object) throws IOException {
		BTreeNode child = root;

		if (child.getNumObjects() == maxKeys) {
			BTreeNode parent = allocateNode();
			root = parent;

			parent.setLeaf(false);
			parent.setNumObjects(0);
			parent.setKid(0, child);

			splitChild(parent, 0, child);
			insertNonFull(parent, object);
		} else {
			insertNonFull(child, object);
		}
	}

	public static BTreeNode allocateNode() {
		nodeCount++;
		// System.out.printf("allocating new node. nodeCount=%d\n", nodeCount);
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
	 * Writes root node right after metadata.
	 * 
	 * @throws IOException
	 */
	public void writeRoot() throws IOException {
		root.setId(1);
		file.write(root);
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
	 * @throws IOException
	 */
	public BTreeNode search(long key) throws IOException {
		return searcher(root, key);
	}

	private BTreeNode searcher(BTreeNode node, long key) throws IOException {
		int i = 0;
		// BTreeNode searchedNode = readNode(i);

		// System.out.printf("root=%s\n", root);
		// System.out.printf("node=%s\n", node);
		// System.out.printf("tree obj=%s\n", node.getObject(i));
		// System.out.printf("obj key=%d\n", node.getObject(i).getKey());

		System.out.printf("\tsearched node: %s\n", node);

		i = 0;
		while (i < node.getNumObjects()) {
			if (key > node.peekObject(i).getKey()) {
				i++;
				if (i >= node.getNumObjects()) {
					return searcher(file.read(node.peekKid(i).getId()), key);// search
																				// right
																				// child
																				// node
				}
			} else if (key == node.peekObject(i).getKey()) {
				return node;
			} else if (key < node.peekObject(i).getKey()) {
				return searcher(file.read(node.peekKid(i).getId()), key);// search
																			// left
																			// child
																			// node.

			}
		}

		if (node.isLeaf()) {
			return null;
		}
		return null;
	}

	/**
	 * Traverse the tree in-order, printing each node that is visited.
	 */
	public void traverseInOrder() {
		System.out.println("============= BTREE NODES ===============");
		inOrder(root);
		System.out.println("=========================================");
	}

	private void inOrder(BTreeNode node) {
		for (int i = 0; i < node.getNumKids(); i++) {
			inOrder(node.peekKid(i));
		}
		System.out.println(node);
	}

	public BTreeMetadata getMetaData() {
		return file.readTreeMetadata();
	}

}