
import java.io.File;
import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.LinkedList;
import java.util.Collections;

public class BTreeNode<T> {

	private int id;
	private int numObjects;
	private int numKids;
	public static int maxChildren = 2 * ArgsGenerate.degree; // 2t
	public static int maxObjects = (maxChildren - 1); // 2t-1

	private boolean isLeaf;

	IUDoubleLinkedList objects;
	IUDoubleLinkedList kids;

	// size of this object in bytes
	public static int SIZE = 
			20 + // 5 4b ints
			1 + // 1 1b boolean
			(maxObjects * TreeObject.SIZE) + // 2t-1 object ids of b int //TreeObject.SIZE
			(maxChildren * BTreeNode.SIZE); // 2t kid ids of 4b int; //BTreeNode.SIZE)

	public BTreeNode() {
		// this.parent = parent;
		objects = new IUDoubleLinkedList();
		kids = new IUDoubleLinkedList();
		isLeaf = true;
		id = 0;
		numObjects = 0;
		numKids = 0;
	}

	public BTreeNode(BTreeFile f) {
		// this.parent = parent;
		BTreeMetadata md = f.readTreeMetadata();
		int degree = md.degree;
		int nodeSize = md.nodeSize;
		
		objects = new IUDoubleLinkedList();
		kids = new IUDoubleLinkedList();
		isLeaf = true;
		id = 0;
		numObjects = 0;
		numKids = 0;
	}
	
	public boolean isLeaf() {
		return isLeaf;
	}

	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumObjects() {
		return numObjects;
	}

	public int getNumKids() {
		return numKids;
	}

	public void setNumObjects(int newSize) {
		numObjects = newSize;
	}

	public void setNumKids(int newSize) {
		numKids = newSize;
	}

	public TreeObject getObject(int index) {
		numObjects--;
		return (TreeObject) objects.remove(index);
	}

	public TreeObject peekObject(int index) {
		return (TreeObject) objects.get(index);
	}

	public void setObject(int index, TreeObject object) {
		numObjects++;
		objects.add(index, object);
	}

	public BTreeNode getKid(int index) {
		numKids--;
		return (BTreeNode) kids.remove(index);
	}

	public BTreeNode peekKid(int index) {
		return (BTreeNode) kids.get(index);
	}

	public void setKid(int index, BTreeNode kid) {
		numKids++;
		kids.add(index, kid);
	}
}