import java.util.ArrayList;

public class BTree {
    private int t;// degree
    private int maxKeys, nodeCount;
    private BTreeNode root, y;
    private BTreeFile file;
    // exists

    public BTree() {
        t = ArgsGenerate.degree;
        maxKeys = 2 * t - 1;
        System.out.println("maxObjects per Node: " + maxKeys);
        root = y = AllocatNode();// y is child
        nodeCount = 1;// for root
        root.setId(nodeCount);
    }

    public void splitChild(BTreeNode parent, int leftIndex, BTreeNode left) {
        BTreeNode right = new BTreeNode();
        // currently from list should be from file
        right.setLeaf(left.isLeaf());
        right.setNumObjects(t - 1);

        for (int j = 0; j < t - 1; j++) { // values for j?
            right.setObject(j, left.getObject(j + 1));
        }

        if (!left.isLeaf()) {
            for (int j = 0; j < t; j++) {
                right.setKid(j, left.getKid(j + t));// this too
            }
        }

        // left.setNumObjects(t - 1); Unnecessary -- getKid decrements numKids
        // in node

        for (int j = parent.getNumObjects() + 1; j > leftIndex + 1; j--) {
            parent.setObject(j + 1, parent.getObject(j));
        }

        parent.setKid(leftIndex + 1, right);

        for (int j = parent.getNumObjects(); j > leftIndex; j--) {
            parent.setObject(j + leftIndex, parent.getObject(j));
        }

        parent.setObject(leftIndex, left.getObject(t - 1));

        // write nodes to disk
//        file.write(child);  // disk-write(child)
//        file.write(z);      // disk-write(z)
//        file.write(x);      // disk-write(x)
    }

    public void insertNonFull(BTreeNode node, TreeObject object) {

        int numObjects = node.getNumObjects();
        int index = numObjects - 1;
        System.out.println("numObjects: " + numObjects);

        if (node.isLeaf()) {
            // compare new object's key to
            // previous insert's key
            while (numObjects > 0 && object.getKey() < node.getObject(index).getKey()) {
                node.setObject(index + 1, node.getObject(index));
                index--;
            }
            if (index < 0) {
                node.setObject(0, object);
            } else {
                node.setObject(index, object);
            }

            file.write(node);

//             if (numObjects == 0) {
//                  node.setObject(0, object);
//             } else {
//                  node.setObject(index + 1, object);
//             }
        } else {
            // yeah we'll see
            while (index > 0 && object.getKey() < node.getObject(index).getKey()) {
                --index;
            }
            index++;
            // fake diskRead x's child at index i
            if (node.getKid(index).getNumObjects() == maxKeys) {
                splitChild(node, index, node.getKid(index));
                if (object.getKey() > node.getObject(index).getKey()) {
                    index++;
                }
            }
            // insertNonFull(node.getKid(index), object);
        }
    }

    public void insert(TreeObject object) {
        BTreeNode child = root;
        // System.out.println("r's id: " + r.getId());
        if (child.getNumObjects() == maxKeys) {
            BTreeNode parent = new BTreeNode();
            root = parent;
            // node.setId(++nodeCount);
            // System.out.println("root id: " + root.getId());
            parent.setLeaf(false);
            parent.setNumObjects(0);
            parent.setKid(0, child); // addKid now increments numKids;
            splitChild(parent, 0, child);
            insertNonFull(parent, object);
        } else {
            insertNonFull(child, object);
        }
    }

    public BTreeNode AllocatNode() {
        return new BTreeNode();
    }

}