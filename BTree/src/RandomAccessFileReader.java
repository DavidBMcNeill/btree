import java.io.*;

public class RandomAccessFileReader {

    private RandomAccessFile file;
    private FileOutputStream stream;
    private StringBuilder builder;

    public RandomAccessFileReader(File file) throws IOException {
        builder = new StringBuilder();

        // we must use a binary file. create it.
        new FileOutputStream(file).close();

        // "rwd" tells the reader to update the file's contents on disk for every update.
        // this is more efficient and guarantees all modifications have been made.
        this.file = new RandomAccessFile(file, "rwd");
    }

    public long toBase4(String s) {
       builder.setLength(0);

        for (int i=0; i<s.length(); i++) {

            switch(s.charAt(i)) {
                case 'a': builder.append("00");
                case 't': builder.append("11");
                case 'c': builder.append("01");
                case 'g': builder.append("10");
                default: {
                    System.err.printf("invalid DNA base: %s is not a,t,c, or g\n", s.charAt(i));
                    return -1;
                }
            }
        }

        return Long.parseLong(builder.toString(), 2);
    }

    public String fromBase4(long key) {

        builder.setLength(0);
        String b = Long.toBinaryString(key);

        for (int i=0; i<b.length(); i+=2) {

            switch(b.substring(i, i+2)) {
                case "00": builder.append("a");
                case "11": builder.append("t");
                case "01": builder.append("c");
                case "10": builder.append("g");
                default: {
                    System.err.printf("invalid DNA base: %s is not 00,11,01, or 10\n", b.substring(i, i+2));
                    return "";
                }
            }
        }

        return builder.toString();
    }


}
