public class TestArgs {

    private void validateUseCache() {
        test_validateUseCache_A();
        test_validateUseCache_B();
        test_validateUseCache_C();
        test_validateUseCache_D();
        test_validateUseCache_E();
        test_validateUseCache_F();

    }

    private void test_validateUseCache_A() {

        String[] fakeArgs = {
            "0",
            "resources/test_file",
            "resources/test_file",
            "123",
            "0"
        };

        if (!ArgsSearch.validate(fakeArgs)) {
            System.err.println("test_validateUseCacheA failed. expected args to validate.");
        }

        if (ArgsSearch.useCache) {
            System.err.println("test_validateUseCacheA failed. expected false.");
        }

    }
    private void test_validateUseCache_B() {

        String[] fakeArgs = {
                "1",
                "resources/test_file",
                "resources/test_file",
                "123",
                "0"
        };

        if (!ArgsSearch.validate(fakeArgs)) {
            System.err.println("test_validateUseCache_B failed");
        }
        if (!ArgsSearch.useCache) {
            System.err.println("test_validateUseCache_B failed. expected true.");
        }

    }
    private void test_validateUseCache_C() {

        String[] fakeArgs = {
                "3",
                "resources/test_file",
                "resources/test_file",
                "123",
                "0"
        };

        if (!ArgsSearch.validate(fakeArgs)) {
            System.err.println("test_validateUseCache_C failed");
        }



    }
    private void test_validateUseCache_D() {

        String[] fakeArgs = {
                "",
                "resources/test_file",
                "resources/test_file",
                "123",
                "0"
        };

        if (ArgsSearch.validate(fakeArgs)) {
            System.err.println("test_validateUseCache_D failed");
        }

    }
    private void test_validateUseCache_E() {

        String[] fakeArgs = {
                "-1",
                "resources/test_file",
                "resources/test_file",
                "123",
                "0"
        };

        if (ArgsSearch.validate(fakeArgs)) {
            System.err.println("test_validateUseCache_E failed");
        }

    }
    private void test_validateUseCache_F() {

        String[] fakeArgs = {
                "banana",
                "resources/test_file",
                "resources/test_file",
                "123",
                "0"
        };

        if (ArgsSearch.validate(fakeArgs)) {
            System.err.println("test_validateUseCache_F failed");
        }

    }

    private void test_validateCacheSize_A() {
        String[] fakeArgs = {
                "0",
                "resources/test_file",
                "resources/test_file",
                "123",
                "0"
        };

        if (!ArgsSearch.validate(fakeArgs)) {
            System.err.println("test_validateUseCacheA failed. expected args to validate.");
        }

        if (ArgsSearch.useCache) {
            System.err.println("test_validateUseCacheA failed. expected false.");
        }

    }


    public void main(String[] args) {
        // <0/1(no/with Cache)> <btree file> <query file> [<cache size>] [<debug level>]"

        validateUseCache();


    }




}
