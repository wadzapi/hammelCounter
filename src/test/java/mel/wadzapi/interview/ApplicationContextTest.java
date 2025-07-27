package mel.wadzapi.interview;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;


@SpringBootTest
@ExtendWith({SpringExtension.class})
class ApplicationContextTest {

    @Test
    void contextLoads() {
        Assert.state(true, "Spring context works!");
    }

}
