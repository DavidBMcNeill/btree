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

        KeyCoder coder = new KeyCoder();

        try {
            BTree tree = new BTree();

            GeneParser parser = new GeneParser(
                ArgsGenerate.geneBankFile,
                ArgsGenerate.sequenceLength
            );

            for (TreeObject obj : parser.parse()) {
                System.out.println(obj);   // <-- PRINTS THE LINE
            }


        } catch (IOException e) {
            System.out.println(e.getMessage());

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
