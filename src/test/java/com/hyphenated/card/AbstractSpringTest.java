package com.hyphenated.card;

import org.hibernate.SessionFactory;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	private SessionFactory sessionFactory;
	
	/**
	 * Helper function.  Due to the nature of Hibernate + Spring Transactions, the cache may get
	 * out of sync if a transaction is not committed.  The DB for the transaction may have an
	 * updated entity that hibernate cannot read back because of the cache.  Flush and clear
	 * will re-synchronize this, and also evict all entities from the persistence context.
	 */
	protected void flushAndClear(){
		sessionFactory.getCurrentSession().flush();
		sessionFactory.getCurrentSession().clear();
	}
}
