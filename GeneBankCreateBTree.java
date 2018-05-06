import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

/**
 * 
 * required arguments:
 * 
 * java GeneBankCreateBTree <0/1(no/with Cache)> <degree> <gbk file> <sequence
 * length> [<cache size>] [<debug level>]
 * 
 * @authors David McNeill, Ross McCusker, Jeffrey Moore
 */
public class GeneBankCreateBTree {

	private static ArrayList<TreeObject> objs;

	public static void main(String[] args) {

		if (!ArgsGenerate.validate(args)) {
			System.err.println("our arguments did not validate, quitting...");
			return;
		}		
		run();
	}

	private static void run() {
		long startTime = System.currentTimeMillis();
		// status message
		System.err.printf("Building B-tree from DNA sequences in %s ...\n", ArgsGenerate.fileName);		
		GeneParser parser = new GeneParser();
		objs = parser.parse();
		Collections.sort(objs);

		try {
			BTree tree = new BTree();
			for (TreeObject obj : objs) {
				tree.insert(obj);
//				tree.traverseInOrder();
			}

			tree.writeRoot();
			tree.getCache().clearCache();
			
		} catch (IOException e) {
			System.err.printf("cannot build tree: %s\n", e);
		}

		double duration = (System.currentTimeMillis() - startTime) * .001;
		String pattern = "#.00";
		DecimalFormat df = new DecimalFormat(pattern);
		System.err.printf("Btree from %s has been completed.\nIt took approximately %s seconds to build the tree.",
            ArgsGenerate.fileName, df.format(duration));

		if(ArgsGenerate.debugLevel == 1) {
			System.err.println("\nWriting dump file ...");
			dump();
		}
	}

	private static void dump() {
		//TODO needs to be in-order 
		String title = "dump";
		KeyCoder kc = new KeyCoder();
		PrintWriter dumpWriter = null;

		try {
			dumpWriter = new PrintWriter(title);
		} catch (Exception e) {
			// TODO: handle exception
		}

		for(TreeObject to : objs) {
			dumpWriter.printf("%s: %d\n", kc.decodeKey(to.getKey(), ArgsGenerate.sequenceLength), to.getFreq());
		}

		dumpWriter.close();
		System.err.println("dump was successfully written.");
	}
}// that's all folks