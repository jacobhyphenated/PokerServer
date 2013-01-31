package com.hyphenated.card;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
		"file:src/main/webapp/WEB-INF/spring/spring-context.xml",
		"classpath:db-context-test.xml"
})
@Transactional
/**
 * Abstract test super class for JUnit tests that need the spring context and
 * an in memory embedded database for persistence testing. 
 * @author jacobhyphenated
 *
 */
public abstract class AbstractSpringTest {
	
}
