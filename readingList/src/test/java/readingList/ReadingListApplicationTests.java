package readingList;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ReadingListApplication.class)
@WebAppConfiguration
public class ReadingListApplicationTests {

	@Autowired
	private WebApplicationContext webContext;

	private MockMvc mockMVC;

	@Before
	public void setupMockMVC() {

		mockMVC = MockMvcBuilders.webAppContextSetup(webContext).build();
	}

	@Test
	public void homePage() throws Exception {
		mockMVC.perform(MockMvcRequestBuilders.get("/readingList")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("readingList"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("books"))
				.andExpect(MockMvcResultMatchers.model().attribute("books", Matchers.is(Matchers.empty())));
	}

	@Test
	@WithMockUser(username = "craig", password = "password", roles = "READER")
	public void postBook() throws Exception {
		mockMVC.perform(MockMvcRequestBuilders.post("/readingList").contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("title", "Book Title").param("author", "Book Author").param("isbn", "1234567")
				.param("description", "description")).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.header().string("Location", "/readingList"));

		Book expectedBook = new Book();
		expectedBook.setId(1L);
		expectedBook.setReader("readingList");
		expectedBook.setTitle("Book Title");
		expectedBook.setAuthor("Book Author");
		expectedBook.setIsbn("1234567");
		expectedBook.setDescription("description");

		mockMVC.perform(MockMvcRequestBuilders.get("/readingList")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("readingList"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("books")).andExpect(MockMvcResultMatchers
						.model().attribute("books", Matchers.contains(Matchers.samePropertyValuesAs(expectedBook))));

	}

}
