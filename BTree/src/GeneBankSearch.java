
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

        if (ArgsSearch.validate(args)) {

            System.out.println("our arguments validated correctly, let's go!");
            System.out.printf("useCache: %b\n", ArgsSearch.useCache);
            System.out.printf("btreeFile: %s\n", ArgsSearch.btreeFile);
            System.out.printf("queryFile: %s\n", ArgsSearch.queryFile);
            System.out.printf("cacheSize: %d\n", ArgsSearch.cacheSize);
            System.out.printf("debugLevel: %d\n", ArgsSearch.debugLevel);

        } else {
            System.err.println("our arguments did not validate, quitting...");
        }
    }



}
