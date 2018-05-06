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
			QueryReader reader = new QueryReader();

			while (reader.hasNext()) {
				String line = reader.thisLine();
				// remove all number data
				line = line.replaceAll("[^A-z]+", "");
				line = line.trim();
				KeyCoder coder = new KeyCoder();
				long key = 0;
				// for (int i = 0; i < line.length(); i++) {
				key = coder.encodeKey(line);
				// key = key | (part << 2*(i));
				// }

				BTreeNode node = tree.search(key);

				if (node == null) {
					System.out.printf("cannot find node with key %dl\n", key);
				} else {
					System.out.printf("found node: %s\n", node);
				}
				reader.next();
			}

		} catch (IOException e) {
			System.err.printf("error searching file: %s", e);
		}
	}

	private static void showResults() {
		// TODO: show results
		if (ArgsSearch.debugLevel == 0) {
			// something
		} else {
			// This file has no 1 level debug required,
			// though we could 'implement one if we want'.
		}
	}

}