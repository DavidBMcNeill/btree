
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

        if (!ArgsGenerate.validate(args)) {
            System.err.println("our arguments did not validate, quitting...");
            return;
        }

        CreateBTree creator = new CreateBTree();
        if (creator.create())
            showResults();
    }

    private static void showResults() {
        // TODO: show results
        if (ArgsGenerate.debugLevel == 0) {
            // something
        } else {
            // ArgsGenerate.debugLevel is 1
            // something
        }
    }





}
