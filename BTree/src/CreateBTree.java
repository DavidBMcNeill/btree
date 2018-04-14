import java.io.IOException;

public class CreateBTree {

    private BTreeFile disk;

    public CreateBTree() {

        // we have access to these:
        // ArgsGenerate.useCache
        // ArgsGenerate.degree
        // ArgsGenerate.geneBankFile
        // ArgsGenerate.sequenceLength
        // ArgsGenerate.cacheSize
        // ArgsGenerate.debugLevel

    }

    /**
     * The public method called when building the tree
     * @return true is success, else false
     */
    public boolean create() {
        return initialize() && build();
    }

    /**
     * Initialize the disk reader/writer prior to building the tree.
     * @return true is success, else false
     */
    private boolean initialize() {
        try {
            disk = new BTreeFile();
            return true;

        } catch(IOException e) {
            System.err.printf("could not create btree data file: %s", e);
            return false;
        }
    }

    /**
     * Actually build the tree, writing the tree to disk.
     * @return true is success, else false
     */
    private boolean build() {
        // TODO: actually build the tree
        return true;
    }



}
