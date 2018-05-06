import java.io.*;
import java.util.Iterator;

/**
 * Iterates over any query file. Use it like this:
 *
 *   QueryReader reader = new QueryReader();
 *   while (reader.hasNext()) {
 *       System.out.println(reader.next());
 *   }
 *
 *
 */

public class QueryReader implements Iterator {

    private BufferedReader reader;
    private String currentLine;
    private boolean hasNext = true;

    public QueryReader() {

        try {
            reader = new BufferedReader(
                new FileReader(ArgsSearch.queryFile)
            );
            step();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public String thisLine() {
    	return currentLine;
    }

    public String next() {
        String temp = currentLine;

        try {
            step();
            return temp;

        } catch (IOException e) {
            System.err.printf("cannot read next line: %s", e);
            return "";
        }
    }

    private void step() throws IOException {
        currentLine = reader.readLine();
        hasNext = (currentLine != null);

        if (hasNext) {
            currentLine = currentLine.toLowerCase();
//            System.out.println(currentLine);
        }

         System.out.printf("line: %s, hasNext=%b\n", currentLine, hasNext);
    }

    public boolean hasNext() {
        return hasNext;
    }
}