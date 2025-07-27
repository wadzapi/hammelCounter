package mel.wadzapi.interview

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.util.Assert

@SpringBootTest
class HammelCounterApplicationTests {

	@Test
	fun contextLoads() {
		Assert.state(true, "Spring context works!")
	}

}
