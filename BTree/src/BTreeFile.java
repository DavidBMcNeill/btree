import java.io.RandomAccessFile;
import java.io.File;
import java.io.IOException;

public class BTreeFile {

    private RandomAccessFile file;
    private BTreeMetadata metadata;

    /**
     * The constructor to use when reading an existing BTree File.
     * @throws IOException
     */
    public BTreeFile(File btreeFile) throws IOException {
        createAccessFile(btreeFile);
    }

    /**
     * Constructor to use when generating a new BTree File.
     * @throws IOException
     */
    public BTreeFile() throws IOException {

        this.metadata = new BTreeMetadata(
            ArgsGenerate.degree,
            ArgsGenerate.sequenceLength,
            BTreeNode.SIZE
        );

        File f = generateBtreeFile();
        createAccessFile(f);
        writeTreeMetadata();
    }

    private void createAccessFile(File f) throws IOException {
        // "rwd" tells the reader to update the files's content on disk for every update.
        // this is more efficient and guarantees all modifications have been made.
        file = new RandomAccessFile(f, "rwd");
    }

    private File generateBtreeFile() throws IOException {

        String name = String.format("%s.btree.data.%s.%s",
            ArgsGenerate.geneBankFile.getAbsolutePath(),
            metadata.sequenceLength,
            metadata.degree
        );

        File f = new File(name);
        if (!f.createNewFile()) {
            throw new IOException(String.format("couldn't create file: %s", f));
        }

        return f;

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

//        file.writeInt(node.index());
        file.writeBoolean(node.isLeaf());
        file.writeLong(node.getParent());
        file.writeInt(node.numChildren());
        file.writeInt(node.numObjects());

        for (int i=0; i<node.numChildren(); i++) {
            file.writeLong(node.getChild(i));
        }
        for (int i=0; i<node.numObjects(); i++) {
            file.writeLong(node.getObject(i));
        }

        return spot;
    }

     public BTreeNode read(int nodeIndex) throws IOException {

        long spot = BTreeMetadata.SIZE + (nodeIndex * BTreeNode.SIZE);
        file.seek(spot);

        boolean isLeaf = file.readBoolean();
        long parent = file.readLong();
        int numChildren = file.readInt();
        int numObjects = file.readInt();

        long[] objects = new long[numChildren];
        for (int i=0; i<numChildren; i++) {
            objects[i] = file.readLong();
        }

        long[] children = new long[numObjects];
        for (int i=0; i<numObjects; i++) {
            children[i] = file.readLong();
        }

         // or null if not found?
        return new BTreeNode(
            nodeIndex,
            isLeaf,
            parent,
            numChildren,
            numObjects,
            objects,
            children
        );
    }

}
