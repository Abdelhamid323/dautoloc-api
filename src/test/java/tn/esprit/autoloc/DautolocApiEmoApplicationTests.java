package tn.esprit.autoloc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.boot.test.context.SpringBootTest;

@EnabledIfSystemProperty(named = "autoloc.integration", matches = "true")
@SpringBootTest
class DautolocApiEmoApplicationTests {

    @Test
    void contextLoads() {
    }

}
