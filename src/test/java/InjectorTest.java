import static org.junit.Assert.*;

import org.example.Injector;
import org.example.SomeBean;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class InjectorTest {
    @Test
    public void testIntegration() {

        Injector injector = new Injector();
        SomeBean someBean = new SomeBean();
        someBean = (SomeBean) injector.inject(someBean);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        someBean.foo();

        String actualOutput = outputStream.toString();
        String expectedOutput = "B\nC\n";

        actualOutput = actualOutput.replaceAll("\\r\\n|\\r|\\n", "");
        expectedOutput = expectedOutput.replaceAll("\\r\\n|\\r|\\n", "");

        assertEquals(expectedOutput, actualOutput);

        System.setOut(System.out);
    }
}
