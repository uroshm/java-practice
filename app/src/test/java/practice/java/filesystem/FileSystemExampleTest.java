package practice.java.filesystem;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FileSystemExampleTest {
    @Test
    void testSolution() throws Exception {
        FileSystemExample example = new FileSystemExample();
        example.find("004");
    }
}
