import java.io.File;

/**
 * Validator of the arguments for the GeneBankCreateBTree class.
 * java GeneBankCreateBTree <useCache> <degree> <gbk-file> <sequence length> [<cache size>] [<debug level>]
 *
 * useCache:
 *    (required) true if we are going to use a cache to improve performance.
 *
 * degree:
 *    (required) The degree of the tree. only 0, 1, or 2 is supported. If 0 is given then
 *    the optimal degree of the tree must be automatically determined based on a 4096 byte
 *    block size and the size of a BTreeNode on disk.
 *
 * geneBankFile:
 *      (required) The raw, human genome text file that we will be converting into a btree.
 *
 * sequenceLength:
 *      (required) The length of a DNA sequence in a BTree.
 *
 * cacheSize:
 *      (optional) If useCache is true, this represents the max size of the cache.
 *      Uses a default of 500 (a default value was not specified in our handout)
 *
 * debugLevel:
 *    (optional)  default of 0. We only are required to provide *at least* 0, so 1 is
 *    not required. A level of 0 means "the output of the queries should be printed
 *    to stdout. Any diagnostic, help, or status messages must be printed to stderr".
 *    This class supports either 0 or 1.
 */

public class ArgsGenerate {

    public static boolean useCache;
    public static int degree;
    public static String geneBankFile;
    public static int sequenceLength;
    public static int cacheSize;
    public static int debugLevel;

    /**
     * validates that all the arguments are correct. Returns true if they are, false if not.
     * @param args array of args to validate
     * @return boolean indicating success
     */
    public static boolean validate(String[] args) {

        try {
            validateNumArgs(args.length);
            validateUseCache(args[0]);
            validateDegree(args[1]);
            validateGeneBankFile(args[2]);
            validateSequenceLength(args[3]);

            if (args.length > 4)
                validateCacheSize(args[4]);

            if (args.length > 5)
                validateDebugLevel(args[5]);

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
        if (num < 4 || num > 6)
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
     * validate the degree argument is valid.
     * throws IllegalArgumentException if not.
     * @param s arg to validate as String
     */
    private static void validateDegree(String s) {
        try {
            int i = Integer.parseInt(s);

            if (i == 0 ||  i == 1 || i == 2)
                degree = (i == 0) ? 4096 : i;
            else
                throw new IllegalArgumentException(
                    String.format("degree argument '%s' must be 0, 1, or 2", s)
                );

        } catch(NumberFormatException e) {
            throw new IllegalArgumentException(
                String.format("degree argument '%s' must be an integer", s)
            );
        }
    }

    /**
     * validate the geneBankFile argument is valid.
     * throws IllegalArgumentException if not.
     * @param s arg to validate as String
     */
    private static void validateGeneBankFile(String s) {
        if (s.length() > 0)
            geneBankFile = s;
        else
            throw new IllegalArgumentException(
                "geneBankFile argument cannot be blank"
            );
    }

    /**
     * validate the sequenceLength argument is valid.
     * throws IllegalArgumentException if not.
     * @param s arg to validate as String
     */
    private static void validateSequenceLength(String s) {
        try {
            int i = Integer.parseInt(s);
            if (i > 0 && i <= 31)
                sequenceLength = i;
            else
                throw new IllegalArgumentException(
                    String.format("sequenceLength argument '%s' must be larger than 1", s)
                );

        } catch(NumberFormatException e) {
            throw new IllegalArgumentException(
                String.format("sequenceLength argument '%s' must be an integer", s)
            );
        }
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
        System.out.println("java GeneBankCreateBTree <0/1(no/with Cache)> <degree> <gbk file> <sequence length> [<cache size>] [<debug level>]");
    }

}
