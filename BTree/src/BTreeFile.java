import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class BTreeFile {

    private RandomAccessFile file;
    private BTreeMetadata metadata;
    private File geneBankFile;

    public BTreeFile() throws IOException {

        this.metadata = new BTreeMetadata(
            ArgsGenerate.degree,
            ArgsGenerate.sequenceLength,
            BTreeNode.SIZE
        );

        this.geneBankFile = generateBtreeFilename();

        // "rwd" tells the reader to update the files's content on disk for every update.
        // this is more efficient and guarantees all modifications have been made.
        file = new RandomAccessFile(generateBtreeFilename(), "rwd");
    }

    private File generateBtreeFilename() {
        return new File(String.format("%s.btree.data.%s.%s",
            geneBankFile,
            metadata.sequenceLength,
            metadata.degree
        ));
    }

    /**
     * Writes the BTree's metadata to the binary btree file.
     * @throws IOException
     */
    private void writeTreeMetadata() throws IOException {
        // metadata belongs at the beginning of the file
        file.seek(0);

        // ordering is important, must match readTreeMetadata().
        file.writeInt(metadata.degree);
        file.writeInt(metadata.nodeSize);  // same as 'root offset'
        file.writeInt(metadata.sequenceLength);
    }

    /**
     * Reads the BTree's metadata in the binary btree
     * file, returns the data as a BTreeMetadata class.
     * @throws IOException
     */
    private BTreeMetadata readTreeMetadata() throws IOException {
        file.seek(0);

        // these values must match writeTreeMetadata().
        return new BTreeMetadata(
            file.readInt(),  // degree
            file.readInt(),  // nodeSize
            file.readInt()   // sequenceLength
        );
    }

    /**
     * Writes a BTreeNode to disk.
     * @param node BTreeNode
     * @return the offset from 0 in bytes where the node was placed in the file
     * @throws IOException
     */
    public long write(BTreeNode node) throws IOException {

        // start after the btree metadata, then go "index" node "size"s over.
        long spot = BTreeMetadata.SIZE + (node.index() * BTreeNode.SIZE);

        file.seek(spot);

        // TODO:  write stuff to the file like so:
        // file.writeInt(node.stuff);

        return spot;
    }

     public BTreeNode read() {
        // TODO: read a node from disk

        // placeholder
        BTreeNode nodeWeFound = new BTreeNode(666);

        return nodeWeFound;  // or null if not found
    }

}
