package lib;

import static org.junit.jupiter.api.Assertions.*;

public class Assertions {
    public static void assertStringLength(String string){
        assertTrue(string.length() > 15);
    }
}
