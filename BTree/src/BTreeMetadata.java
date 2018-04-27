public class BTreeMetadata {

    public int degree;
    public int nodeSize;  // size of a node in bytes
    public int sequenceLength;

    // 3 ints at 4 bytes each
    public final static int SIZE = 3*4;

    public BTreeMetadata(int degree, int sequenceLength, int nodeSize) {
        this.sequenceLength = sequenceLength;
        this.degree = degree;
        this.nodeSize = nodeSize;
    }

    @Override
    public String toString() {
        return String.format(
            "degree=%d, nodeSize=%d, sequenceLength=%d, SIZE=%d",
            degree, nodeSize, sequenceLength, SIZE
        );
    }

}
