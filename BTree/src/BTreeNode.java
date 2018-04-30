
import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.LinkedList;

public class BTreeNode {

    private int id;
    private int numObjects;
    private int numKids;
    private static final int maxChildren = 2 * ArgsGenerate.degree;  // 2t
    private static final int maxObjects = maxChildren - 1;           // 2t-1

    private boolean isLeaf;

    private TreeObject[] objects;
    private BTreeNode[] kids;
    private BTreeNode parent;

    // size of this object in bytes
    public static final int SIZE =
            (6*4) +                         // 6 4b ints
            1 +                             // 1 1b boolean
            (maxObjects*TreeObject.SIZE) +  // 4 object ids of 4b int
            (maxChildren*BTreeNode.SIZE) +  // 4 kid ids of 4b int
            4;                              // 1 4b int parent id

    public BTreeNode(int t) {
//    	this.parent = parent;
        objects = new TreeObject[2*t - 1];
        kids = new BTreeNode[2*t];
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

    public void setNumObjects(int numObjects) {
        this.numObjects = numObjects;
    }

    public int getNumKids() {
        return numKids;
    }
    public void setNumKids(int num) {
        numKids = num;
    }
    
    public TreeObject getObject(int index) {
    	numObjects--;
    	TreeObject gotten = objects[index];
    	objects[index] = null;
        return gotten;
    }
    
    public TreeObject peekObject(int index) {
        return objects[index];
    }

    public void setObject(int index, TreeObject object) {
        objects[index] = object;
        numObjects++;
    }
    public BTreeNode getParent() {
        return parent;
    }
    public void setParent(BTreeNode node) {
        parent = node;
    }
    
    public BTreeNode getKid(int index) {
    	numKids--;
    	BTreeNode gotten = kids[index];
    	kids[index] = null;
        return gotten;
    }
    
    public BTreeNode peekKid(int index) {
        return kids[index];
    }

    public void setKid(int index, BTreeNode kid) {
    	kids[index] = kid;
        numKids++;
    }
    @Override
    public String toString() {
        return String.format("<Node: id=%d, leaf=%b, objects=%d>", id, isLeaf, numObjects);
    }
}
