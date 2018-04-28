import java.io.IOException;
import java.util.ArrayList;

/**
 * Cache implementing a doubly-linked list of BTreeNodes to improve BTree runtime.  
 * Cache size specified by command-line argument from GeneBankSearch call, and GeneBankCreateBTree call. 
 * 
 * @author DavidMcNeill1
 * @param <T>
 *
 */
public class Cache<BTreeNode> {

	//if node is kicked out from cache, write to file;
	//write everything in cache to file at end of simulation
	private ArrayList<BTreeNode> cache;

	public Cache(int size) throws IOException {
		this.cache = new ArrayList<BTreeNode>();
	}

	/*
	 * 1) if cache hit, +move hit data
	 * item to top of cache
	 */

	/**
	 * 
	 * @param element
	 * @return
	 */
	public BTreeNode getObject(BTreeNode element) {
		BTreeNode getElement = null; 
		
		if (cache.indexOf(element) > -1) {
			int elementIndex = cache.indexOf(element);
			getElement = cache.get(elementIndex);
		} 
		
		return getElement;
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
	 * 
	 * @param element
	 */
	public void add(BTreeNode node) {
		cache.add(node);
	}

	/**
	 * Removes the last BTreeNode
	 * @return
	 */
	public BTreeNode removeLast() {
		return cache.remove(cache.size()-1);
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
