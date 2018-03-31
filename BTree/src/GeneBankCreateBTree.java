
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

    public static void main(String[] args) {

        if (ArgsGenerate.validate(args)) {

            System.out.println("our arguments validated correctly, let's go!");
            System.out.printf("useCache: %b\n", ArgsGenerate.useCache);
            System.out.printf("degree: %d\n", ArgsGenerate.degree);
            System.out.printf("geneBankFile: %s\n", ArgsGenerate.geneBankFile);
            System.out.printf("sequenceLength: %d\n", ArgsGenerate.sequenceLength);
            System.out.printf("cacheSize: %d\n", ArgsGenerate.cacheSize);
            System.out.printf("debugLevel: %d\n", ArgsGenerate.debugLevel);

        } else {
            System.err.println("our arguments did not validate, quitting...");
        }










    }




}
