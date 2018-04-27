import java.io.FileNotFoundException;
import java.io.IOException;

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
        BTree tree = new BTree();
        KeyCoder coder = new KeyCoder();

        try {

            GbkReader reader = new GbkReader(
                    ArgsGenerate.geneBankFile,
                    ArgsGenerate.sequenceLength
            );

            while (reader.hasNext()) {
                String chunk = reader.next();
                long key = coder.encodeKey(chunk);
                System.out.printf("%s --> %dl\n", chunk, key);   // <-- PRINTS THE LINE
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
