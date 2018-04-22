
import java.io.*;

public class TestBTreeFile {

    public TestBTreeFile() {

    }

    public static void main(String[] args) {
        File filename = new File("btree1.dat");

        try {
            BTreeFile file = new BTreeFile(filename);

            if (filename.exists()) {
                System.out.println(filename.getAbsolutePath());
            } else {
                System.err.println("testing the btree failed to init");
            }

        } catch(IOException e) {
            System.err.printf("some fucking error: %s\n", e.getMessage());
        }


    }


}
