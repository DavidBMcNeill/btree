import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class TestBTreeFile {

    public static void main(String[] args) {
        try {

            if (!ArgsGenerate.validate(args)) {
                System.err.println("our arguments did not validate, quitting...");
                return;
            }

//            File f = new File("test_file");
//            boolean ok = f.createNewFile();

            BTreeFile file = new BTreeFile();


            long[] objects = {999L, 888L, 777L};
            long[] children = {666L, 555L, 444L};

            BTreeNode node = new BTreeNode(
                12345, true, 67890,
                3, 4, objects, children
            );

            file.write(node);
            BTreeNode n = file.read(12345);

            System.out.printf("id: %d\n", n.index());
            System.out.printf("isLeaf: %b\n", n.isLeaf());
            System.out.printf("parent: %dl\n", n.getParent());
            System.out.printf("numChildren: %dl\n", n.numChildren());
            System.out.printf("numObjects: %dl\n", n.numObjects());
            System.out.printf("objects: %s\n", Arrays.toString(n.getObjects()));
            System.out.printf("children: %s\n", Arrays.toString(n.getChildren()));

            BTreeMetadata m = file.readTreeMetadata();
            System.out.printf("metadata=%s\n", m);


        } catch(IOException e) {
            System.out.println(e.getMessage());

        }
    }

}
