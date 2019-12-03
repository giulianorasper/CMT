import org.junit.Test;

public class AgendaTests {

    @Test
    public void testGetTopicFromPreorderString() {
        String[] list = "1.2.3.4.5.".split("\\.");
        for(String s : list) {
            System.out.println(s);
        }
    }
}
