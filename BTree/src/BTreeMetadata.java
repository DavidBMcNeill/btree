public class BTreeMetadata {

    public int t;
    public int k;
    public int degree;
    public int nodeSize;  // size of a node in bytes
    public long rootOffset;

    // each int is 4 bytes, longs are 8 bytes
    private static int size = 4 + 4 + 4 + 4 + 8;

    public BTreeMetadata(int degree, int k, int t, int nodeSize, long rootOffset) {
        this.k = k;
        this.t = t;
        this.degree = degree;
        this.nodeSize = nodeSize;
        this.rootOffset = rootOffset;


    }

    public static int size() {
        return size;

    }

}
