package io.melody.core;

import org.jongo.Jongo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(classes = TestApplication.class)
@ActiveProfiles("test")
class ClientTest01 {
	@Autowired
	@Qualifier("jongoCore")
	private Jongo jongoCore;

	@Value("${core-config.collections.template}")
	private String COLL_TEMPLATES;
	@Value("${core-config.collections.core}")
	private String COLL_CORE_VALUES;
	@Test
	void test01() {
		//log.info("test01...");
		
	}

}
