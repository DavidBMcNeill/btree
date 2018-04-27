import java.io.IOException;

public class SearchBTree {

    private BTreeFile file;

    public SearchBTree() {

        // we have access to these:
        // ArgsSearch.useCache
        // ArgsSearch.debugLevel
        // ArgsSearch.cacheSize
        // ArgsSearch.queryFile
        // ArgsSearch.btreeFile
    }

    /**
     * The public method called when building the tree
     * @return true is success, else false
     */
    public BTreeNode search(String pattern) {
        // TODO: actually search the tree
        // we will be searching through ArgsSearch.btreeFile, which is a File instance.

        try {
            // this is the interface for the btree file on disk. we will use it to search.
            file = new BTreeFile();

            return new BTreeNode();

        } catch (IOException e) {
            System.err.printf("problem reading btree file: %s", e);
            return null;
        }

    }


}
