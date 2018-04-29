import java.io.IOException;
import java.util.ArrayList;

/**
 * Cache implementing a doubly-linked list of BTreeNodes to improve BTree runtime.  
 * Cache size specified by command-line argument from GeneBankSearch call, and GeneBankCreateBTree call. 
 * 
 * @author DavidMcNeill1
 *
 */
public class Cache {

	//if node is kicked out from cache, write to file;
	//write everything in cache to file at end of simulation
	private ArrayList<BTreeNode> cache;
	int sizeLimit;
	BTreeFile file;

	public Cache(int size, BTreeFile file) throws IOException {
		sizeLimit = size;
		this.file = file;
		this.cache = new ArrayList<BTreeNode>();
	}

	/*
	 * 1) if cache hit, +move hit data
	 * item to top of cache
	 */

	/**
	 * 
	 * @param nodeIndex
	 * @return
	 */
	public BTreeNode getNode(int nodeIndex) {
		BTreeNode getNode = null; 
		
		if (nodeIndex > -1 && nodeIndex < cache.size()) {
			getNode = cache.get(nodeIndex);
		}
		return getNode;
	}
	
	public int indexOf(BTreeNode node) {
		int index = -1;
		
		index = cache.indexOf(node);
		
		return index;
	}
	
	/**
	 * 
	 * @param element
	 * @return
	 */
	public boolean contains(BTreeNode element) {
		boolean contains = false;
		
		if (cache.indexOf(element) > -1) {
			contains = true;
		}
		
		return contains;
	}

	/**
	 * Removes the last BTreeNode
	 * @return
	 */
	public void removeLast() {
		BTreeNode last = cache.remove(cache.size()-1);
		file.write(last);
	}
	
	/**
	 * 
	 * @param node
	 */
	public void add(BTreeNode node) {
		if (cache.size() > sizeLimit) {
			removeLast();
		}
		cache.add(node);
		if (cache.size() > node.getId()) {
			cache.remove(node.getId());	
		}
	}

	/**
	 * 
	 */
	public void clearCache() {
		while (!cache.isEmpty()) {
			removeLast();
		}
	}

	/**
	 * 
	 * @param element
	 */
	public void bumpUp(BTreeNode element) {
		int swapIndex = cache.indexOf(element);
		BTreeNode storage = cache.remove(swapIndex);
		cache.add(0, storage);
	}
	
	/**
	 * 
	 * @return
	 */
	public int size() {
		int size = cache.size();
		return size;
	}
}
