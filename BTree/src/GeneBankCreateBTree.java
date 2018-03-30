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
	private int cacheFlag, degree, seqLength, cacheSize, debugLevel;	
	private String gbkFileName;
	private final String CMDSUSAGE = "java GeneBankCreateBTree <0/1(no/with Cache)> <degree> <gbk file>\n"
			+ "<sequence length> [<cache size>] [<debug level>]";

	/**
	 * main
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		// workaround to avoid static methods
		GeneBankCreateBTree theTree = new GeneBankCreateBTree(args);
		theTree.peekCMDS();		
		System.out.printf("running time = %d millis", System.currentTimeMillis() - startTime);
	}

	/**
	 * if cache, gets desired size. Also gets degree,
	 * sequence-length, and debug-level
	 * degree is the degree to be used for BTree
 	 * <= int sequence length <= 31
	 * @param cmds
	 *            arguments from main
	 */
	public GeneBankCreateBTree(String[] cmds) {
		try {
			int numArgs = cmds.length;
			cacheFlag = Integer.parseInt(cmds[0]);
			degree = Integer.parseInt(cmds[1]);
			gbkFileName = cmds[2];
			seqLength = Integer.parseInt(cmds[3]);

			// cache(yes/no), if yes ensure cache size provided
			// set debug level based on numArgs
			switch (cacheFlag) {
			case 0:				
				if (numArgs == 4) {
					debugLevel = 0;
				} else if (numArgs == 5) {
					debugLevel = Integer.parseInt(cmds[4]);
				} else {
					System.err.println("Arguments,\n" + "usage: " + CMDSUSAGE);
					System.exit(-1);
				}
				break;
			case 1:				
				if (numArgs == 5) {
					cacheSize = Integer.parseInt(cmds[4]);
					debugLevel = 0;
				} else if (numArgs == 6) {
					cacheSize = Integer.parseInt(cmds[4]);
					debugLevel = Integer.parseInt(cmds[5]);
				} else {
					System.err.println("Arguments,\n" + "usage: " + CMDSUSAGE);
					System.exit(-1);
					System.exit(-1);
				}
				break;
			}

		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.err.println("usage: " + CMDSUSAGE);
			System.exit(-1);
		}
	}
	/**
	 * outputs contents of user arguments
	 */
	public void peekCMDS() {
		System.out.printf("cache choice: %d\ncacheSize: %d\ndegree: %d\nfileName: %s\nsequence length: %d\n"
				+ "debug level: %d\n", cacheFlag, cacheSize, degree, gbkFileName, seqLength, debugLevel);
	}
}
