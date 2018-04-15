
/**
 * A wrapper class for BTreeNodes being stored in the Cache, used in a list.  
 * 
 * @author DavidMcNeill
 *
 */
public class CacheListNode<TreeObject> extends BTreeNode {
	private TreeObject element;
	private CacheListNode<TreeObject> next;
	private CacheListNode<TreeObject> previous;

	/**
	 * Returns the previous CacheListNode in the list.
	 * 
	 * @return the previous CacheListNode<TreeObject> in the list.
	 */
	public CacheListNode<TreeObject> getPrevious() {
		return previous;
	}

	/**
	 * Replaces the previous BTreeNode with the new BTreeNode "previous."
	 * 
	 * @param previous
	 *            the BTreeNode to replace the previous BTreeNode
	 */
	public void setPrevious(CacheListNode<TreeObject> previous) {
		this.previous = previous;
	}



	// BTreeNode that only takes an element as a parameter. Next and previous
	// are assumed to be null.
	public CacheListNode(TreeObject element) {
		this.element = element;
		next = null;
		previous = null;
	}

	// BTreeNode constructor that takes an element, in addition to references
	// to next and previous as parameters.
	public CacheListNode(TreeObject element, CacheListNode<TreeObject> next, CacheListNode<TreeObject> previous) {
		this.element = element;
		this.next = next;
		this.previous = previous;
	}

	/**
	 * Returns the element contained by the current CacheListNode.
	 * 
	 * @return the element contained by the current CacheListNode
	 */
	public TreeObject getElement() {
		return element;
	}

	/**
	 * Replaces the current BTreeNode's element with the specified element.
	 * 
	 * @param element
	 *            the element of generic type T that is replacing the current
	 *            BTreeNode's element
	 */
	public void setElement(TreeObject element) {
		this.element = element;
	}

	/**
	 * Returns the next CacheListNode in the list.
	 * 
	 * @return the next CacheListNode in the list
	 */
	public CacheListNode<TreeObject> getNext() {
		return next;
	}

	/**
	 * Replaces the next CacheListNode in the list with the specified CacheListNode.
	 * 
	 * @param next
	 *            the CacheListNode that will replace the next CacheListNode
	 */
	public void setNext(CacheListNode<TreeObject> next) {
		this.next = next;
	}
	
}
