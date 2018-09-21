package no.itminds.movies.test.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import javax.persistence.PersistenceException;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import no.itminds.movies.model.login.User;
import no.itminds.movies.repository.RoleRepository;
import no.itminds.movies.repository.UserRepository;
import no.itminds.movies.service.impl.UserServiceImpl;

public class UserServiceImplTest {

	@InjectMocks
	private UserServiceImpl userService;
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private BCryptPasswordEncoder passwordEncoder;
	
	@Mock
	private RoleRepository roleRepository;
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSaveUser() {
		/*
		 * Test 1: valid user
		 */
		
		//Arrange
		final String rawPassword = "secret";
		User expectedUser = new User("magneoe@gmail.com", rawPassword, "Magnus", "Østeng");
		
		when(userRepository.saveAndFlush(expectedUser)).thenReturn(expectedUser);
		//When
		userService.saveUser(expectedUser);
		verify(userRepository).saveAndFlush(expectedUser);
		
		//Then
		assertThat(expectedUser.getPassword(), IsEqual.equalTo(passwordEncoder.encode(rawPassword)));
		assertThat(expectedUser.getActive(), Is.is(true));
		
		/*
		 * Test 2 - invalid user
		 */
		expectedUser = null;
		expectedException.expect(PersistenceException.class);
		userService.saveUser(expectedUser);
		verify(userRepository).saveAndFlush(expectedUser);
	}


	@Test
	public void testFindByEmail() {
		/*
		 * Test1 - should return a valid user
		 */
		//Arrange
		final String emailTest1 = "magneoe@gmail.com";
		User mockUser = new User(emailTest1, "secret", "Magnus", "Østeng");
		when(userService.findByEmail(emailTest1)).thenReturn(mockUser);
		
		//When
		User actualUser = userService.findByEmail(emailTest1);
		
		//Test
		assertNotNull(actualUser);
		assertThat(actualUser, IsEqual.equalTo(mockUser));
		
		/*
		 * Test2 - should return null
		 */
		
		//Arrange
		final String emailTest2 = "test@test.com";
		when(userService.findByEmail(emailTest2)).thenReturn(null);
		
		//When
		actualUser = userService.findByEmail(emailTest2);
		
		//Then
		assertNull(actualUser);
		
		/*
		 * Test3 - invalid input
		 */

		//Arrange
		final String emailTest3 = null;
		when(userService.findByEmail(emailTest3)).thenReturn(null);
		
		//When/Then
		actualUser = userService.findByEmail(emailTest3);
		
		assertNull(actualUser);

	}

}
