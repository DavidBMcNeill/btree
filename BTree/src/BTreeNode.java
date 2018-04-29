
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

    private ArrayList<TreeObject> objects;
    private ArrayList<BTreeNode> kids;
    private BTreeNode parent;

    // size of this object in bytes
    public static final int SIZE =
            (6*4) +                         // 6 4b ints
            1 +                             // 1 1b boolean
            (maxObjects*TreeObject.SIZE) +  // 4 object ids of 4b int
            (maxChildren*BTreeNode.SIZE) +  // 4 kid ids of 4b int
            4;                              // 1 4b int parent id

    public BTreeNode() {
        objects = new ArrayList<>();
        kids = new ArrayList<>();
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
        return objects.size();
    }

    public void setNumObjects(int numObjects) {
        this.numObjects = numObjects;
    }

    public int getNumKids() {
        return kids.size();
    }
    public void setNumKids(int num) {
        numKids = num;
    }
    public TreeObject getObject(int index) {
//		numObjects--;
        return objects.get(index);
    }

    public void setObject(int index, TreeObject object) {
        objects.add(index, object);
        numObjects++;
    }
    public BTreeNode getParent() {
        return parent;
    }
    public void setParent(BTreeNode node) {
        parent = node;
    }
    public BTreeNode getKid(int index) {
//		numKids--;
        return kids.get(index);
    }

    public void setKid(int index, BTreeNode kid) {
        kids.add(index, kid);
        numKids++;
    }
    @Override
    public String toString() {
        return String.format(
            "<Node: id=%d, leaf=%b, objects=%d, kids=%d>",
            id, isLeaf, objects.size(), kids.size()
        );
    }
}
