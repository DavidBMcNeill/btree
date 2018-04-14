import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class BTreeFile {

    private RandomAccessFile me;
    private BTreeMetadata metadata;
    private File geneBankFile;

    public BTreeFile() throws IOException {

        this.metadata = new BTreeMetadata(
            ArgsGenerate.degree,
            ArgsGenerate.sequenceLength,
            BTreeNode.size
        );

        this.geneBankFile = generateBtreeFilename();

        // "rwd" tells the reader to update the files's content on disk for every update.
        // this is more efficient and guarantees all modifications have been made.
        me = new RandomAccessFile(generateBtreeFilename(), "rwd");
    }

    private File generateBtreeFilename() {
        return new File(String.format("%s.btree.data.%s.%s",
            geneBankFile,
            metadata.sequenceLength,
            metadata.degree
        ));
    }

    /**
     * Writes the BTree's metadata to the binary btree me.
     * @throws IOException
     */
    private void writeTreeMetadata() throws IOException {
        // metadata belongs at the beginning of the file
        me.seek(0);

        // ordering is important, must match readTreeMetadata.
        me.writeInt(metadata.degree);
        me.writeInt(metadata.nodeSize);  // same as 'root offset'
        me.writeInt(metadata.sequenceLength);
    }

    /**
     * Reads the BTree's metadata in the binary btree
     * file, returns the data as a BTreeMetadata class.
     * @throws IOException
     */
    private BTreeMetadata readTreeMetadata() throws IOException {
        me.seek(0);

        // ordering is important, must match writeTreeMetadata.
        return new BTreeMetadata(
            me.readInt(),  // degree
            me.readInt(),  // nodeSize
            me.readInt()   // sequenceLength
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
        long spot = BTreeMetadata.size + (node.index() * BTreeNode.size);

        me.seek(spot);

        // TODO:  write stuff to the me like so:
        // me.writeInt(node.stuff);

        return spot;
    }

     public BTreeNode read() {
        // TODO: read a node from disk
         return new BTreeNode();  // or null if not found
     }

}
