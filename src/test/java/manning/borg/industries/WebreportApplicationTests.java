package manning.borg.industries;

import manning.borg.industries.config.TestWebConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = {TestWebConfig.class})
class WebreportApplicationTests {

	@Test
	void contextLoads() {
	}

}
