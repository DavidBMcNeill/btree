import java.io.FileNotFoundException;
import java.io.IOException;
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

    public static void main(String[] args) {

        if (!ArgsGenerate.validate(args)) {
            System.err.println("our arguments did not validate, quitting...");
            return;
        }

        run();
        showResults();
    }

    private static void run() {

        try {
            BTree tree = new BTree();
            // KeyCoder coder = new KeyCoder();
            GeneParser parser = new GeneParser();

            for (TreeObject obj : parser.parse()) {
                System.out.println(obj);   // <-- PRINTS THE LINE
                tree.insert(obj);
            }

        } catch (IOException e) {
            System.out.printf("cannot build tree: %s\n", e);
        }
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
