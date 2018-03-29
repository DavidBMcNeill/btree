import java.io.IOException;

/**
 * Cache implementing a doubly-linked list of BTreeNodes to improve BTree runtime.  
 * Cache size specified by command-line argument from GeneBankSearch call, and GeneBankCreateBTree call. 
 * 
 * @author DavidMcNeill1
 * @param <T>
 *
 */
public class Cache<T> {

	//if node is kicked out from cache, write to file;

	//write everything in cache to file at end of simulation

	
	private IUDoubleLinkedList<T> cache;
	private int nr1 = 0;
	private int nh1 = 0;
	private int nh2 = 0;
	private int nr2 = 0;
	private int nr = 0;
	private int nh = 0;
	private double hr;
	private double hr1;
	private double hr2;

	public Cache(int size) throws IOException {
		
		this.cache = new IUDoubleLinkedList<T>();

	}

	/*
	 * 1) if 1st-level cache hit, +both cache have hit data item. +move hit data
	 * item to top on both cache
	 * 
	 * 2) if 1st-level miss AND 2nd-level hit, +move data item to top of 2nd-level
	 * cache; +add item to top of 1st-level cache
	 * 
	 * 3) if 1st-level miss, 2nd-level miss, +retrieve data item from disk +add item
	 * to top of both cache
	 */

	/**
	 * 
	 * @param element
	 * @return
	 */
	public T getObject(T element) {
		T getElement = null; 
		
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
	public boolean contains(T element) {
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
	public void addObject(T element) {
		cache.addToFront(element);
	}

	/**
	 * 
	 * @return
	 */
	public T removeObject() {
		return cache.removeLast();
	}

	/**
	 * 
	 */
	public void clearCache() {
		while (!cache.isEmpty()) {
			removeObject();
		}
	}

	/**
	 * 
	 * @param element
	 */
	public void bumpUp(T element) {
		int swapIndex = cache.indexOf(element);
		T storage = cache.remove(swapIndex);
		cache.addToFront(storage);
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
