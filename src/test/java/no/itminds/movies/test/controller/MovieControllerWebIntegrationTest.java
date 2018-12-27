package no.itminds.movies.test.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.itminds.movies.App;
import no.itminds.movies.config.WebSecurityConfig;
import no.itminds.movies.model.dto.CommentDTO;
import no.itminds.movies.model.dto.MovieDTO;
import no.itminds.movies.model.dto.VoteDTO;
import no.itminds.movies.model.login.User;
import no.itminds.movies.service.impl.TokenAuthenticationService;

@ActiveProfiles("test")
@TestPropertySource(locations = {"classpath:test-integration.properties"})
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = App.class)
@AutoConfigureMockMvc(secure=false)
@ContextConfiguration(classes= {WebSecurityConfig.class})
@Transactional
public class MovieControllerWebIntegrationTest {

	private final static Logger LOGGER = LoggerFactory.getLogger(MovieControllerWebIntegrationTest.class);
	
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private TokenAuthenticationService authenticationService;

	private JacksonTester<MovieDTO> jsonTesterMovieDTO;
	private JacksonTester<CommentDTO> jsonTesterComment;
	private JacksonTester<VoteDTO> jsonTesterVoteDTO;

	private final LocalDate dateNow = LocalDate.now();
	private final LocalDateTime dateTimeNow = LocalDateTime.now();

	private User testUser;

	@Before
	public void setUp() throws Exception {
		JacksonTester.initFields(this, mapper);
		testUser = new User("test@gmail.com", "password", "Harry", "Hole");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetMovies() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/movies/getAll").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void testGetDetails() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/movies/2/").contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print())
				.andExpect(jsonPath("$.title", Is.is("Pulp fiction")));

		// Invalid input, not found
		mockMvc.perform(MockMvcRequestBuilders.get("/api/movies/100/").contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(MockMvcResultMatchers.status().isNotFound());

	}

	@Test
	public void testGetActors() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/actors/getAll"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$.content.length()", Is.is(3)))
				.andExpect(jsonPath("$.content[0].name", Is.is("Eddie Muprpy")))
				.andExpect(jsonPath("$.content[1].name", Is.is("Samuel Jackson")))
				.andExpect(jsonPath("$.content[2].name", Is.is("Uma Thurman")));
	}

	@Test
	public void testGetGenres() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/genres/getAll"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$.content.length()", Is.is(3)));
	}

	@Test
	public void testComments() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/movies/comments?movieId=2"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$.content.length()", Is.is(2)));

		mockMvc.perform(MockMvcRequestBuilders.get("/api/movies/comments?movieId=500"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$.content.length(", Is.is(0)));

		// Invalid inputs
		mockMvc.perform(MockMvcRequestBuilders.get("/api/movies/comments?movieId="))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	public void testAddMovie() throws Exception {

		final MovieDTO SUBMITTED_MOVIE_DTO = new MovieDTO("NewAndExcitingTitle", "1956", "SuperPlot", "Good", "180",
				"9.1", "testUrl", dateNow, dateNow, 4.0, null, null, null);

		String SUBMITTED_MOVIE_JSON = jsonTesterMovieDTO.write(SUBMITTED_MOVIE_DTO).getJson();
		final String token = obtainAccessToken(testUser.getEmail(), testUser.getPassword());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/api/movies/addMovie/")
				.contentType(MediaType.APPLICATION_JSON)
				.header(TokenAuthenticationService.HEADER_STRING, token)
				.accept(MediaType.APPLICATION_JSON).content(SUBMITTED_MOVIE_JSON))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(jsonPath("$.title", Is.is("NewAndExcitingTitle")))
				.andExpect(jsonPath("$.plot", Is.is("SuperPlot"))).andExpect(jsonPath("$.year", Is.is("1956")))
				.andExpect(jsonPath("$.id", IsNull.notNullValue())).andExpect(jsonPath("$.id", Is.isA(Integer.class)))
				.andDo(MockMvcResultHandlers.print());
	}

	@Test
	public void testSubmitComment() throws Exception {
		final String TEST_TITLE = "TestTitle";
		final String TEST_COMMENT = "TestComment";
		final Long MOVIE_ID = 2l;
		CommentDTO commentDTO = new CommentDTO(TEST_TITLE, TEST_COMMENT, MOVIE_ID, dateTimeNow, testUser);

		final String SUBMITTED_COMMENT_JSON = jsonTesterComment.write(commentDTO).getJson();
		final String token = obtainAccessToken(testUser.getEmail(), testUser.getPassword());
		
		mockMvc.perform(MockMvcRequestBuilders.post("/api/movies/submitComment").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.header(TokenAuthenticationService.HEADER_STRING, token)
				.content(SUBMITTED_COMMENT_JSON))
				.andDo(MockMvcResultHandlers.log())
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(jsonPath("$.title", Is.is(TEST_TITLE))).andExpect(jsonPath("$.comment", Is.is(TEST_COMMENT)))
				.andExpect(jsonPath("$.created", IsNull.notNullValue()))
				.andExpect(jsonPath("$.movieId", Is.isA(Integer.class)))
				.andExpect(jsonPath("$.author.name", Is.isA(String.class)))
				.andExpect(jsonPath("$.author.lastname", Is.isA(String.class)))
				.andExpect(jsonPath("$.author.email").doesNotExist())
				.andExpect(jsonPath("$.author.password").doesNotExist()).andDo(MockMvcResultHandlers.print());

	}

	// @WithMockUser(value = "test@gmail.com")
	@Test
	public void testSubmitComment_InvalidInput() throws Exception {
		// Invalid MovidId
		String TEST_TITLE = "Test";
		String TEST_COMMENT = "Test";
		Long MOVIE_ID = null;
		CommentDTO commentDTO = new CommentDTO(TEST_TITLE, TEST_COMMENT, MOVIE_ID, dateTimeNow, testUser);

		String SUBMITTED_COMMENT_JSON = jsonTesterComment.write(commentDTO).getJson();

		// Invalid movieId
		mockMvc.perform(MockMvcRequestBuilders.post("/api/movies/submitComment").
				accept(MediaType.APPLICATION_JSON)
				.header(TokenAuthenticationService.HEADER_STRING,
						TokenAuthenticationService.TOKEN_PREFIX + " "
								+ authenticationService.getTokenForTesting(testUser.getEmail()))
				.contentType(MediaType.APPLICATION_JSON).content(SUBMITTED_COMMENT_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest()).andDo(MockMvcResultHandlers.print());

		// Invalid title

		TEST_TITLE = "";
		TEST_COMMENT = "Test";
		MOVIE_ID = 2l;
		commentDTO = new CommentDTO(TEST_TITLE, TEST_COMMENT, MOVIE_ID, dateTimeNow, testUser);

		SUBMITTED_COMMENT_JSON = jsonTesterComment.write(commentDTO).getJson();

		mockMvc.perform(MockMvcRequestBuilders.post("/api/movies/submitComment")
				.accept(MediaType.APPLICATION_JSON)
				.header(TokenAuthenticationService.HEADER_STRING,
						TokenAuthenticationService.TOKEN_PREFIX + " "
								+ authenticationService.getTokenForTesting(testUser.getEmail()))
				.contentType(MediaType.APPLICATION_JSON).content(SUBMITTED_COMMENT_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest()).andDo(MockMvcResultHandlers.print());

		// Invalid comment
		TEST_TITLE = "Test";
		TEST_COMMENT = null;
		MOVIE_ID = 2l;
		commentDTO = new CommentDTO(TEST_TITLE, TEST_COMMENT, MOVIE_ID, dateTimeNow, testUser);

		SUBMITTED_COMMENT_JSON = jsonTesterComment.write(commentDTO).getJson();

		mockMvc.perform(MockMvcRequestBuilders.post("/api/movies/submitComment")
				.accept(MediaType.APPLICATION_JSON)
				.header(TokenAuthenticationService.HEADER_STRING,
						TokenAuthenticationService.TOKEN_PREFIX + " "
								+ authenticationService.getTokenForTesting(testUser.getEmail()))
				.contentType(MediaType.APPLICATION_JSON).content(SUBMITTED_COMMENT_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest()).andDo(MockMvcResultHandlers.print());

		//Invalid user - no token provided
//		TEST_TITLE = "Test";
//		TEST_COMMENT = ";
//		MOVIE_ID = 2l;
//		commentDTO = new CommentDTO(TEST_TITLE, TEST_COMMENT, MOVIE_ID, dateTimeNow, testUser);
//
//		SUBMITTED_COMMENT_JSON = jsonTesterComment.write(commentDTO).getJson();
//
//		mockMvc.perform(MockMvcRequestBuilders.post("/api/movies/submitComment")
//				.accept(MediaType.APPLICATION_JSON)
//				.contentType(MediaType.APPLICATION_JSON).content(SUBMITTED_COMMENT_JSON))
//				.andExpect(MockMvcResultMatchers.status().isBadRequest()).andDo(MockMvcResultHandlers.print());
	}

	// @WithMockUser(value = "test@gmail.com")
	@Test
	public void testVote() throws Exception {

		VoteDTO voteDTO = new VoteDTO(10, 11l);

		final String SUBMITTED_VOTE_JSON = jsonTesterVoteDTO.write(voteDTO).getJson();

		mockMvc.perform(MockMvcRequestBuilders.post("/api/movies/vote").accept(MediaType.APPLICATION_JSON)
				.header(TokenAuthenticationService.HEADER_STRING,
						TokenAuthenticationService.TOKEN_PREFIX + " "
								+ authenticationService.getTokenForTesting(testUser.getEmail()))
				.contentType(MediaType.APPLICATION_JSON).content(SUBMITTED_VOTE_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$.id", Is.is(11)))
				.andExpect(jsonPath("$.averageRating", Is.is(10.0)));
	}

	public void testVote_invalidInput() throws Exception {
		VoteDTO voteDTO = new VoteDTO(null, 11l);

		final String SUBMITTED_VOTE_JSON = jsonTesterVoteDTO.write(voteDTO).getJson();

		//Invalid rating
		mockMvc.perform(MockMvcRequestBuilders.post("/api/movies/vote").accept(MediaType.APPLICATION_JSON)
				.header(TokenAuthenticationService.HEADER_STRING,
						TokenAuthenticationService.TOKEN_PREFIX + " "
								+ authenticationService.getTokenForTesting(testUser.getEmail()))
				.contentType(MediaType.APPLICATION_JSON).content(SUBMITTED_VOTE_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());

		//Invalid movieId
		voteDTO = new VoteDTO(4, null);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/movies/vote").accept(MediaType.APPLICATION_JSON)
				.header(TokenAuthenticationService.HEADER_STRING,
						TokenAuthenticationService.TOKEN_PREFIX + " "
								+ authenticationService.getTokenForTesting(testUser.getEmail()))
				.contentType(MediaType.APPLICATION_JSON).content(SUBMITTED_VOTE_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
		
		//So such movie
		voteDTO = new VoteDTO(4, 50l);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/movies/vote").accept(MediaType.APPLICATION_JSON)
				.header(TokenAuthenticationService.HEADER_STRING,
						TokenAuthenticationService.TOKEN_PREFIX + " "
								+ authenticationService.getTokenForTesting(testUser.getEmail()))
				.contentType(MediaType.APPLICATION_JSON).content(SUBMITTED_VOTE_JSON))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
		
		//Not authenticated
		voteDTO = new VoteDTO(4, 11l);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/movies/vote").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(SUBMITTED_VOTE_JSON))
				.andExpect(MockMvcResultMatchers.status().isUnauthorized());
	}
	
	
	private String obtainAccessToken(String username, String password) throws Exception {
		  
	    final String bodyContent = "{\"email\":\"" + username + "\", \"password\":\"" + password + "\"}";
	 
	    ResultActions result 
	      = mockMvc.perform(MockMvcRequestBuilders.post("/login")
	        .content(bodyContent)
	        .accept(MediaType.APPLICATION_JSON))
	        .andExpect(MockMvcResultMatchers.status().isOk());
	 
	    return result.andReturn().getResponse().getHeader(TokenAuthenticationService.HEADER_STRING);
	}
}
