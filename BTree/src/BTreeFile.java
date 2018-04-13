import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class BTreeFile {

    private RandomAccessFile me;
    private BTreeMetadata metadata;

    public BTreeFile(File filename, BTreeMetadata metadata) throws IOException {
        this.metadata = metadata;

        // "rwd" tells the reader to update the files's content on disk for every update.
        // this is more efficient and guarantees all modifications have been made.
        me = new RandomAccessFile(filename, "rwd");
    }

    /**
     * Writes the BTree's metadata to the binary btree me.
     * @throws IOException
     */
    private void writeTreeMetadata() throws IOException {
        // metadata belongs at the beginning of the me
        me.seek(0);

        // ordering is important, must match readTreeMetadata.
        me.writeInt(metadata.k);
        me.writeInt(metadata.t);
        me.writeInt(metadata.degree);
        me.writeInt(metadata.nodeSize);
        me.writeLong(metadata.rootOffset);
    }

    /**
     * Reads the BTree's metadata in the binary btree
     * me, returns the data as a BTreeMetadata class.
     * @throws IOException
     */
    private BTreeMetadata readTreeMetadata() throws IOException {
        me.seek(0);

        // ordering is important, must match writeTreeMetadata.
        return new BTreeMetadata(
            me.readInt(),  // k
            me.readInt(),  // t
            me.readInt(),  // degree
            me.readInt(),  // nodeSize
            me.readLong()  // rootOffset
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
        long spot = BTreeMetadata.size() + (node.index() * BTreeNode.size());

        me.seek(spot);

        // TODO:  write stuff to the me like so:
        // me.writeInt(node.stuff);

        return spot;
    }

    // public BTreeNode read() {
    //    // TODO: read a node from disk
    // }

}
