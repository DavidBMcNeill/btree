public class TestQueryReader {


    public static void main(String[] args) {

        if (!ArgsSearch.validate(args)) {
            System.err.println("our arguments did not validate, quitting...");
            return;
        }

        QueryReader reader = new QueryReader();
        while (reader.hasNext()) {
            System.out.println(reader.next());
        }

    }

}
