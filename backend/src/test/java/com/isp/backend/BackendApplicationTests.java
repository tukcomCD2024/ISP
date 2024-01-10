package com.isp.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class BackendApplicationTests {

	@Test
	void contextLoads() {
		ApplicationContext context = SpringApplication.run(BackendApplication.class);
		assertNotNull(context);
	}
}
