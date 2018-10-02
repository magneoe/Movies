package no.itminds.movies.test.repository;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import no.itminds.movies.model.login.User;
import no.itminds.movies.repository.UserRepository;

@RunWith(value=SpringRunner.class)
@DataJpaTest
@TestPropertySource(value="classpath:test-integration.properties")
public class UserRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private UserRepository userRepository;
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Transactional
	@Rollback(true)
	@Test
	public void testFindByEmail() {
		/*
		 * Test1: user exists
		 */
		final String EMAIL = "test@gmail.com";
		
		//When
		User actualUser = userRepository.findByEmail(EMAIL);
		
		//Then
		assertNotNull("Actual user should not be null", actualUser);
		assertThat("Should have firstname Harry", actualUser.getName(), Is.is("Harry"));
		assertThat("Should have lastname Hole", actualUser.getLastname(), Is.is("Hole"));
		assertThat("Should have email" + EMAIL, actualUser.getEmail(), Is.is(EMAIL));
		/*
		 * Test2: when the user should not exist
		 */
		
		actualUser = userRepository.findByEmail("unknownMail");
		
		assertNull("Actual user should be null", actualUser);
		
		/*
		 * Test3: invalid input
		 */
		
		//Arrange
		final String email2 = null;
		
		actualUser = userRepository.findByEmail(email2);
		
		//Then
		assertNull(actualUser);
		
	}

}
