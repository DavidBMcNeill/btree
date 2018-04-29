import java.io.IOException;
import java.util.ArrayList;

/**
 * Cache implementing a doubly-linked list of BTreeNodes to improve BTree runtime.  
 * Cache size specified by command-line argument from GeneBankSearch call, and GeneBankCreateBTree call. 
 * 
 * @author DavidMcNeill1
 */
public class Cache {

	//if node is kicked out from cache, write to file;
	//write everything in cache to file at end of simulation
	private ArrayList<BTreeNode> cache;
	int sizeLimit;
	BTreeFile file;

	public Cache(int size) throws IOException {
		sizeLimit = size;
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
	 * @param node
	 */
	public void add(BTreeNode node) {
		if (cache.size() > sizeLimit) {
			removeLast();
		}
		cache.add(node);
	}

	/**
	 * Removes the last BTreeNode in the cache and writes it to disk (Using BTreeFile's write(node) method).
	 * @return
	 */
	public void removeLast() {
		BTreeNode last = cache.remove(cache.size()-1);
		file.write(last);
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
	 * @return
	 */
	public int size() {
		int size = cache.size();
		return size;
	}
}
