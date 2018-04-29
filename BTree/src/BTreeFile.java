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
            BTreeNode.SIZE,
            ArgsGenerate.sequenceLength
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

//        System.out.printf("ArgsGenerate.geneBankFile: %s\n", ArgsGenerate.geneBankFile);
//        System.out.printf("ArgsGenerate.geneBankFile.getAbsolutePath(): %s\n", ArgsGenerate.geneBankFile.getAbsolutePath());
//        System.out.printf("metadata.sequenceLength: %d\n", metadata.sequenceLength);
//        System.out.printf("metadata.degree: %d\n", metadata.degree);

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
     */
    public BTreeMetadata readTreeMetadata() {

        try {
            file.seek(0);

            // these values must match writeTreeMetadata().
            return new BTreeMetadata(
                file.readInt(),  // degree
                file.readInt(),  // nodeSize
                file.readInt()   // sequenceLength
            );
        } catch (IOException e) {
            System.err.printf("failed to read tree metadata: %s\n", e);
            return null;
        }

    }

    /**
     * Writes a BTreeNode to disk.
     * @param node BTreeNode
     * @return the offset from 0 in bytes where the node was placed in the file. or -1 if failure.
     */
    public long write(BTreeNode node) {

        try {

            // start after the btree metadata, then go "index" node "size"s over.
            long spot = BTreeMetadata.SIZE + (node.getId() * BTreeNode.SIZE);
            System.out.println(node.getId());
            file.seek(spot);

            // file.writeInt(node.index());
            file.writeBoolean(node.isLeaf());
            file.writeInt(node.getId());
            file.writeInt(node.getNumKids());
            file.writeInt(node.getNumObjects());

            BTreeNode kid;
            for (int i = 0; i < node.getNumKids(); i++) {
                kid = node.getKid(i);
                file.writeBoolean(kid.isLeaf());
                file.writeInt(kid.getId());
                file.writeInt(kid.getNumKids());
                file.writeInt(kid.getNumObjects());
            }

            TreeObject obj;
            for (int i = 0; i < node.getNumObjects(); i++) {
                obj = node.getObject(i);
                file.writeLong(obj.getKey());
                file.writeInt(obj.getFreq());
            }

            return spot;

        } catch(IOException e) {
            System.err.printf("cannot write: %s\n", e);
            return -1;
        }

    }

    /**
     * Reads and returns a BTreeNode from the file. Or null if failure.
     * @param nodeIndex which node to get
     * @return BTreeNode or null
     */
    public BTreeNode read(int nodeIndex) {

        try {
            // start after the btree metadata, then go "index" node "size"s over.
            long spot = BTreeMetadata.SIZE + (nodeIndex * BTreeNode.SIZE);
            file.seek(spot);

            BTreeNode node = BTree.allocateNode();
            node.setLeaf(file.readBoolean());
            node.setId(file.readInt());
            node.setNumKids(file.readInt());
            node.setNumObjects(file.readInt());

            for (int i = 0; i < node.getNumKids(); i++) {
                BTreeNode kid = BTree.allocateNode();
                kid.setLeaf(file.readBoolean());
                kid.setId(file.readInt());
                kid.setNumKids(file.readInt());
                kid.setNumObjects(file.readInt());
                node.setKid(i, kid);
            }

            for (int i = 0; i < node.getNumObjects(); i++) {
                TreeObject obj = new TreeObject(file.readLong(), file.readInt());
                node.setObject(i, obj);
            }

            return node;

        } catch (IOException e) {
            System.err.printf("cannot read: %s\n", e);
            return null;
        }
    }

    public long getFilesize() {
        try {
            return file.length();
        } catch(IOException e) {
            System.err.printf("cannot get size of btree file: %s", e);
            return -1L;
        }
    }

}
