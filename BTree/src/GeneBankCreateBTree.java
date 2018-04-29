import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

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

		try {
			BTree tree = new BTree();
			for (TreeObject obj : objs) {
				//System.out.println(obj); // <-- PRINTS THE LINE
				tree.insert(obj);
			}

		} catch (IOException e) {
			System.err.printf("cannot build tree: %s\n", e);
		}

		System.err.printf("Btree from %s has been completed.\n It took approximately %d millis",
            ArgsGenerate.fileName, System.currentTimeMillis() - startTime);

		// output
		if(ArgsGenerate.debugLevel == 1) {
			dump();
		}
	}

	private static void dump() {
		//TODO needs to be in-order 
		try {
			KeyCoder kc = new KeyCoder();
			PrintWriter dumpWriter = new PrintWriter("dump", "UTF-8");
			
			for(TreeObject to : objs) {
				dumpWriter.printf("<%s> <%d>\n", kc.decodeKey(to.getKey(), ArgsGenerate.sequenceLength));
			}
			dumpWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}// that's all folks
