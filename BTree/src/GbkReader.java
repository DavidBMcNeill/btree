import java.io.*;
import java.util.Iterator;

public class GbkReader implements Iterator<String> {

    private BufferedReader reader;
    private int chunkSize;
    private String currentLine;
    private boolean done;

    public GbkReader(File file, int chunkSize) throws IOException {
        reader = new BufferedReader(new FileReader(file));
        this.chunkSize = chunkSize;
        start();
    }

    public void start() throws IOException  {
        currentLine = reader.readLine();
        while (!currentLine.startsWith("ORIGIN")) {
            currentLine = reader.readLine();
        }

        // we are now at the ORIGIN line. so go to the first line of chars
        String raw = reader.readLine();
        currentLine = raw.replaceAll("[\\d /]+", "");
    }

    public String next() {
        try {
            // if we don't have enough chars in our buffer to take a bite, add the next line
            if (currentLine.length() <= chunkSize) {
                String raw = reader.readLine();
                String nextLine = raw.replaceAll("[\\d /]+", "");
                currentLine = currentLine + nextLine;
            }

            String chunk;
            if (currentLine.length() >= chunkSize) {
                chunk = currentLine.substring(0, chunkSize);
                currentLine = currentLine.substring(chunkSize-1);

            } else {
                // we still don't have enough chars to take a bite. we must be at
                // the end and there not enough chars left to take a bite. so just
                // return the last few chars that remain. we're done.
                chunk = currentLine;
                done = true;
            }

            return chunk;

        } catch (IOException e) {
            System.err.printf("error: %s", e);
            return "";
        }

    }

    public boolean hasNext() {
        return !done; // !currentLine.startsWith("//");
    }


}
