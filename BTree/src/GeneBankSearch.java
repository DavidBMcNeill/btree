import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * required arguments:
 * 
 * java GeneBankSearch <0/1 (no/with Cache)> <btree file> <query file> [<cache
 * size>] [<debug level>]
 * 
 * @authors David McNeill, Ross McCusker, Jeffrey Moore
 *
 */
public class GeneBankSearch {

	// query file contains all DNA strings of a specific sequence length that we
	// want to search for in specified btree file

	// strings are: 1 per line; all have same length as DNA sequences in btree
	// file; use [a, c, t, g], upper- or lower-case

    public static void main(String[] args) {

        if (!ArgsSearch.validate(args)) {
            System.err.println("our arguments did not validate, quitting...");
        }

        try {

            BTree tree = new BTree(ArgsSearch.btreeFile);
            KeyCoder coder = new KeyCoder();
            QueryReader reader = new QueryReader();

            while (reader.hasNext()) {

                String line = reader.next();
                long key = coder.encodeKey(line);
//                System.out.printf("string=%s, key=%dl\n", line, key);

                BTreeNode node = tree.search(key);

                if (node == null) {
                    System.out.printf("cannot find node with key %dl\n", key);
                } else {
                    System.out.printf("found node: %s\n", node);
                }
            }

        } catch (IOException e) {
            System.err.printf("error searching file: %s", e);
        }
    }

//    private static void doSomethingWithNode(BTreeNode node) {
//         TODO: print the node to the output file (i think)
//    }

    private static void showResults() {
        // TODO: show results
        if (ArgsGenerate.debugLevel == 0) {
            // something
        } else {
            // This file has no 1 level debug required,
            // though we could implement one if we want.
        }
    }

}
