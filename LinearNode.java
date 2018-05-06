/**
 * The Linear Node object contains an element of generic type. The Linear Nodes
 * in the double-linked list have a link to the next node in the list AND a link
 * to the previous node in the list. This allows for adding and removing from
 * the front and rear of the list to be constant-order operations.
 * 
 * @author DavidMcNeill
 *
 * @param <T>
 */
public class LinearNode<T> {
	private T element;
	private LinearNode<T> next;
	private LinearNode<T> previous;

	/**
	 * Returns the previous LinearNode in the list.
	 * 
	 * @return the previous LinearNode<T> in the list.
	 */
	public LinearNode<T> getPrevious() {
		return previous;
	}

	/**
	 * Replaces the previous LinearNode with the new LinearNode "previous."
	 * 
	 * @param previous
	 *            the LinearNode to replace the previous LinearNode
	 */
	public void setPrevious(LinearNode<T> previous) {
		this.previous = previous;
	}

	// LinearNode that only takes an element as a parameter. Next and previous
	// are assumed to be null.
	public LinearNode(T element) {
		this.element = element;
		next = null;
		previous = null;
	}

	// LinearNode constructor that takes an element, in addition to references
	// to next and previous as parameters.
	public LinearNode(T element, LinearNode<T> next, LinearNode<T> previous) {
		this.element = element;
		this.next = next;
		this.previous = previous;
	}

	/**
	 * Returns the element contained by the current LinearNode.
	 * 
	 * @return the element contained by the current LinearNode
	 */
	public T getElement() {
		return element;
	}

	/**
	 * Replaces the current LinearNode's element with the specified element.
	 * 
	 * @param element
	 *            the element of generic type T that is replacing the current
	 *            LinearNode's element
	 */
	public void setElement(T element) {
		this.element = element;
	}

	/**
	 * Returns the next LinearNode in the list.
	 * 
	 * @return the next LinearNode in the list
	 */
	public LinearNode<T> getNext() {
		return next;
	}

	/**
	 * Replaces the next LinearNode in the list with the specified LinearNode.
	 * 
	 * @param next
	 *            the LinearNode that will replace the next LinearNode
	 */
	public void setNext(LinearNode<T> next) {
		this.next = next;
	}
}
