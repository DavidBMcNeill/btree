import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Double-linked node implementation of IndexedUnsortedList.
 * 
 * @author DavidMcNeill
 *
 * @param <T>
 */
public class IUDoubleLinkedList<T> implements IndexedUnsortedList<T> {
	private LinearNode<T> head;
	private LinearNode<T> tail;
	private int size;
	private int modCount;

	public IUDoubleLinkedList() {
		head = tail = null;
		size = 0;
		modCount = 0;
	}

	@Override
	public void addToFront(T element) {
		LinearNode<T> newNode = new LinearNode<T>(element);
		newNode.setNext(head);
		if (isEmpty()) {
			tail = newNode;
		} else {
			head.setPrevious(newNode);
			newNode.setPrevious(null);
		}
		head = newNode;
		modCount++;
		size++;
	}
	
	/**
	 * added for BTree implementation
	 * @param newSize
	 */
	public void setSize(int newSize) {
		size = newSize;
	}

	@Override
	public void addToRear(T element) {
		LinearNode<T> newNode = new LinearNode<T>(element);
		newNode.setPrevious(tail);
		if (isEmpty()) {
			head = newNode;
		} else {
			tail.setNext(newNode);
		}
		tail = newNode;
		modCount++;
		size++;
	}

	@Override
	public void add(T element) {
		addToRear(element);
	}

	@Override
	public void addAfter(T element, T target) {
		LinearNode<T> targetNode = head;
		while (targetNode != null && targetNode.getElement() != target) {
			targetNode = targetNode.getNext();
		}
		if (targetNode == null) {
			throw new NoSuchElementException();
		}
		LinearNode<T> newNode = new LinearNode<T>(element);

		// NEVER LEAVE A STEP WHERE THERE'S A NODE W/NOTHING POINTING AT IT!
		// THAT WILL IRREPARABLY BREAK THE LIST!!!

		newNode.setNext(targetNode.getNext());
		newNode.setPrevious(targetNode);
		targetNode.setNext(newNode);

		if (targetNode == tail) {
			tail = newNode;
		} else {
			newNode.getNext().setPrevious(newNode);
		}
		modCount++;
		size++;
	}

	@Override
	public void add(int index, T element) {

		if (index < 0 || index > size) {
			throw new IndexOutOfBoundsException();
		}
		if (index == 0) { // adding to head
			addToFront(element);
		} else if (index == size) { // adding to tail
			addToRear(element);
		} else {
			LinearNode<T> current = head;
			for (int i = 0; i < index - 1; i++) {
				current = current.getNext();
			}
			LinearNode<T> newNode = new LinearNode<T>(element);
			newNode.setNext(current.getNext());
			newNode.setPrevious(current);
			current.setNext(newNode);
			newNode.getNext().setPrevious(newNode);
			modCount++;
			size++;
		}
	}

	@Override
	public T removeFirst() {
		ListIterator<T> lit = listIterator();
		if (!lit.hasNext()) {
			throw new NoSuchElementException();
		}
		T retVal = lit.next();
		lit.remove();
		return retVal;
	}

	@Override
	public T removeLast() {
		if (tail == null) {
			throw new NoSuchElementException();
		}
		T retVal = tail.getElement();
		if (head == tail) {
			head = tail = null;
		} else {
			tail = tail.getPrevious();
			tail.setNext(null);
		}
		size--;
		modCount++;
		return retVal;
	}

	@Override
	public T remove(T element) {
		ListIterator<T> lit = listIterator();
		T retVal = null;
		boolean foundIt = false;
		while (lit.hasNext() && !foundIt) {
			retVal = lit.next();
			if (retVal.equals(element)) {
				foundIt = true;
			}
		}

		if (!foundIt) {
			throw new NoSuchElementException();
		}
		lit.remove();
		return retVal;
	}

	@Override
	public T remove(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}
		ListIterator<T> lit = listIterator(index);
		T retVal = lit.next();
		lit.remove();
		return retVal;
	}

	@Override
	public void set(int index, T element) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}
		LinearNode<T> current = head;
		for (int i = 0; i < index; i++) {
			current = current.getNext();
		}
		current.setElement(element);
		modCount++;
	}

	@Override
	public T get(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}
		return listIterator(index).next();
	}

	@Override
	public int indexOf(T element) {
		int returnIndex = -1;
		int currentIndex = 0;
		LinearNode<T> current = head;
		while (current != null && returnIndex < 0) {
			if (current.getElement().equals(element)) {
				returnIndex = currentIndex;
			}
			current = current.getNext();
			if (current != null) {
				currentIndex++;
			}
		}
		return returnIndex;
	}

	@Override
	public T first() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		return head.getElement();
	}

	@Override
	public T last() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		return tail.getElement();
	}

	@Override
	public boolean contains(T target) {
		return (indexOf(target) > -1);
	}

	@Override
	public boolean isEmpty() {
		return (head == null);
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("[");

		Iterator<T> it = iterator();

		while (it.hasNext()) {
			str.append(it.next());
			str.append(", ");
		}
		if (!isEmpty()) { // deletes the extra comma
			str.delete(str.length() - 2, str.length()); // inclusive, exclusive
		}

		str.append("]");

		return str.toString();
	}

	@Override
	public Iterator<T> iterator() {
		return listIterator();
	}

	@Override
	public ListIterator<T> listIterator() {
		return new DLListIterator();
	}

	@Override
	public ListIterator<T> listIterator(int startingIndex) {
		return new DLListIterator(startingIndex);
	}

	/**
	 * ListIterator for IUDoubleLinkedList
	 * 
	 * Cursor position lies between element that would be returned by a call to
	 * previous() and the element that would be returned by call to next().
	 * 
	 * Iterator for list of length n has n+1 possible cursor positions.
	 * 
	 * Remove() and set(Object) NOT defined by cursor position; defined to
	 * operate on last element returned by call to next() or previous().
	 * 
	 * @author DavidMcNeill
	 */
	private class DLListIterator implements ListIterator<T> {
		private int listIterModCount;
		private LinearNode<T> nextNode;
		private LinearNode<T> lastReturned;
		private int nextIndex;

		public DLListIterator() {
			this(0);
		}

		public DLListIterator(int startingIndex) {
			if (startingIndex < 0 || startingIndex > size) {
				throw new IndexOutOfBoundsException();
			}

			nextNode = head;
			for (int i = 0; i < startingIndex; i++) {
				nextNode = nextNode.getNext();
			}
			nextIndex = startingIndex;
			listIterModCount = modCount;
			lastReturned = null;
		}

		@Override
		public boolean hasNext() {
			if (listIterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			return (nextNode != null);
		}

		@Override
		public T next() {
			if (listIterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			T retVal = nextNode.getElement();
			lastReturned = nextNode;
			nextNode = nextNode.getNext();
			nextIndex++;
			return retVal;
		}

		@Override
		public boolean hasPrevious() {
			if (listIterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			return (nextNode != head);
		}

		@Override
		public T previous() {
			if (listIterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			if (!hasPrevious()) {
				throw new NoSuchElementException();
			}
			if (nextNode == null) {
				nextNode = tail;
			} else {
				nextNode = nextNode.getPrevious();
			}
			lastReturned = nextNode;
			nextIndex--;
			return nextNode.getElement();
		}

		@Override
		public int nextIndex() {
			if (listIterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			return nextIndex;
		}

		@Override
		public int previousIndex() {
			if (listIterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			return nextIndex - 1;
		}

		@Override
		public void remove() {
			if (listIterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			if (lastReturned == null) {
				throw new IllegalStateException();
			}
			if (lastReturned == head) {
				head = head.getNext();
			} else {
				lastReturned.getPrevious().setNext(lastReturned.getNext());
			}
			if (lastReturned == tail) {
				tail = tail.getPrevious();
			} else {
				lastReturned.getNext().setPrevious(lastReturned.getPrevious());
			}
			// nextNode points to what comes after the removed lastReturned
			// [imagine taking it out and where the iterator goes to...
			if (lastReturned == nextNode) { // last move was previous()
				nextNode = nextNode.getNext();
			} else { // last move was next()
			}
			nextIndex--;
			size--;
			modCount++;
			listIterModCount++;
			lastReturned = null;
		}

		// replaces last element returned by next() or previous()
		// w/specified element.
		@Override
		public void set(T e) {
			if (listIterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			if (lastReturned == null) {
				throw new IllegalStateException();
			}
			if (size == 0) { // null b/c empty list
				throw new NullPointerException();
			} else {
				lastReturned.setElement(e);
				modCount++;
				listIterModCount++;
			}
		}

		@Override
		public void add(T e) {
			if (listIterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			LinearNode<T> newNode = new LinearNode<T>(e);
			if (size == 0) { // null b/c empty list
				head = tail = newNode;
			} else {
				// newNode.setNext(nextNode);
				if (nextNode == head) {// adding at head [to null in front of
										// head]
					head.setPrevious(newNode);
					newNode.setNext(head);
					newNode.setPrevious(null);
					head = newNode;
				} else if (nextNode == null) {
					tail.setNext(newNode);
					newNode.setPrevious(tail);
					newNode.setNext(null);
					tail = newNode;
				} else {
					nextNode.getPrevious().setNext(newNode);
					newNode.setPrevious(nextNode.getPrevious());
					nextNode.setPrevious(newNode);
					newNode.setNext(nextNode);
				}
			}
			size++;
			modCount++;
			listIterModCount++;
			nextIndex++;
			lastReturned = null;
		}
	}
}
