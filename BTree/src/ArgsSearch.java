import java.io.File;

/**
 * Validator of the arguments for the GeneBankSearch class.
 * java GeneBankSearch  <useCache> <btree file> <query file> [<cache size>] [<debug level>]
 *
 * useCache:
 *    (required) true if we are going to use a cache to improve performance.
 *
 * btreeFile:
 *    (required) this is the binary data file that was created when running
 *    GeneBankCreateBTree that contains a btree that we are to search through
 *
 * queryFile:
 *      (required) this is a provided file that the professor has made. it is human-readable.
 *      it is full of searches to run. query7 for example will be filled with
 *      searches of sequences of 7 chars
 *
 * cacheSize:
 *      (optional) If useCache is true, this represents the max size of the cache.
 *      I put in a default of 500 (a default value was not specified in our handout)
 *
 * debugLevel:
 *    (optional)  default of 0. We only are required to provide *at least* 0, so 1 is
 *    not required. A level of 0 means "the output of the queries should be printed
 *    to stdout. Any diagnostic, help, or status messages must be printed to stderr".
 *    This class supports either 0 or 1.
 */
public class ArgsSearch {

    public static boolean useCache;
    public static int debugLevel = 0;
    public static int cacheSize = 500;
    public static File queryFile;
    public static File btreeFile;

    /**
     * validates that all the arguments are correct. Returns true if they are, false if not.
     * @param args array of args to validate
     * @return boolean indicating success
     */
    public static boolean validate(String[] args) {

        try {
            validateNumArgs(args.length);
            validateUseCache(args[0]);
            validateBtreeFile(args[1]);
            validateQueryFile(args[2]);

            if (args.length > 3 && useCache)
                validateCacheSize(args[3]);

            if (args.length > 4)
                validateDebugLevel(args[4]);

            if (debugLevel == 1 && !btreeFile.exists()) {
                throw new IllegalArgumentException(
                    "you must specify a btreeFile arg if debug is set to 1"
                );
            }


        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            showUsage();
            return false;
        }

        return true;
    }

    /**
     * Validates that the number of arguments is correct.
     * throws IllegalArgumentException if not.
     * @param num the number of arguments
     */
    private static void validateNumArgs(int num) {
        if (num < 3 || num > 5)
            throw new IllegalArgumentException("invalid number of arguments");
    }

    /**
     * validate the useCache argument is valid.
     * throws IllegalArgumentException if not.
     * @param s arg to validate as String
     */
    private static void validateUseCache(String s) {
        try {
            int i = Integer.parseInt(s);
            if (i == 0 || i == 1)
                useCache = i == 1;
            else
                throw new IllegalArgumentException(
                    String.format("useCache argument '%s' must be either 0 or 1", s)
                );

        } catch(NumberFormatException e) {
            throw new IllegalArgumentException(
                String.format("useCache argument '%s' must be an integer", s)
            );
        }
    }

    /**
     * validate the btreeFile argument is valid.
     * throws IllegalArgumentException if not.
     * @param s arg to validate as String
     */
    private static void validateBtreeFile(String s) {
        btreeFile = new File(s);
    }

    /**
     * validate the queryFile argument is valid.
     * throws IllegalArgumentException if not.
     * @param s arg to validate as String
     */
    private static void validateQueryFile(String s) {
        File f = new File(s);

        if (!f.exists())
            throw new IllegalArgumentException(
                String.format("invalid queryFile. path does not exit: %s", s)
            );
        else if (!f.isFile())
            throw new IllegalArgumentException(
                String.format("invalid queryFile. path is not a file: %s", s)
            );
        else if (!f.canRead())
            throw new IllegalArgumentException(
                String.format("invalid queryFile. cannot read the file: %s", s)
            );
        else
            queryFile = f;
    }

    /**
     * validate the cacheSize argument is valid.
     * throws IllegalArgumentException if not.
     * @param s arg to validate as String
     */
    private static void validateCacheSize(String s) {
        try {
            int i = Integer.parseInt(s);
            if (i > 1)
                cacheSize = i;
            else
                throw new IllegalArgumentException(
                    String.format("cacheSize argument '%s' must be larger than 1", s)
                );

        } catch(NumberFormatException e) {
            throw new IllegalArgumentException(
                String.format("cacheSize argument '%s' must be an integer", s)
            );
        }
    }

    /**
     * validate the debugLevel argument is valid.
     * throws IllegalArgumentException if not.
     * @param s arg to validate as String
     */
    private static void validateDebugLevel(String s) {
        try {
            int i = Integer.parseInt(s);
            if (i == 0 || i == 1)
                debugLevel = i;
            else
                throw new IllegalArgumentException(
                    String.format("debugLevel argument '%s' must be either 0 or 1", s)
                );
        } catch(NumberFormatException e) {
            throw new IllegalArgumentException(
                String.format("debugLevel argument '%s' must be an integer", s)
            );
        }
    }

    /** prints the usage of the GeneBankSearch */
    private static void showUsage() {
        System.out.println("USAGE:");
        System.out.println("java GeneBankSearch <useCache> <btree file> <query file> [<cache size>] [<debug level>]");
    }

}
