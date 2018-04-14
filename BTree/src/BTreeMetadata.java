public class BTreeMetadata {

    public int degree;
    public int nodeSize;  // size of a node in bytes
    public int sequenceLength;

    // each int is 4 bytes, longs are 8 bytes
    public final static int size = 4 + 4 + 4 + 4 + 8;

    public BTreeMetadata(int degree, int sequenceLength, int nodeSize) {
        this.sequenceLength = sequenceLength;
        this.degree = degree;
        this.nodeSize = nodeSize;
    }

}
