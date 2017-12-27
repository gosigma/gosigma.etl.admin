
package gosigma.etl.admin;

import static org.junit.Assert.*;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
// @SpringApplicationConfiguration(classes = ReadingListApplication.class)
@SpringBootTest(classes = EtlAdminApplication.class)
@WebAppConfiguration
public class MockMvcWebTests {
	private static final String EOL = System.getProperty("line.separator");

	private static StringBuilder builder = new StringBuilder();

	@AfterClass
	public static void afterClass() {
		System.out.print(builder);
	}

	@Rule
	public TestWatcher watchman = new TestWatcher() {

		@Override
		protected void failed(Throwable e, Description description) {
			if (description != null) {
				builder.append(description);
			}
			if (e != null) {
				builder.append(' ');
				builder.append(e);
			}
			builder.append(" FAIL");
			builder.append(EOL);
		}

		@Override
		protected void succeeded(Description description) {
			if (description != null) {
				builder.append(description);
			}
			builder.append(" OK");
			builder.append(EOL);
		}
	};

	@Autowired
	private WebApplicationContext webContext;

	private MockMvc mockMvc;

	@Before
	public void setupMockMvc() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webContext).apply(springSecurity()) // needed for security testing
				.build();
	}

	@Test
	public void homePage() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/")).andExpect(MockMvcResultMatchers.status().isOk())
		// .andExpect(MockMvcResultMatchers.view().name("readingList"))
		// .andExpect(MockMvcResultMatchers.model().attributeExists("books"))
		// .andExpect(MockMvcResultMatchers.model().attribute("books",
		// Matchers.is(Matchers.empty())))
		;

		mockMvc.perform(MockMvcRequestBuilders.get("/etl"))
				.andExpect(MockMvcResultMatchers.status().is4xxClientError());
	}

	@Test
	@WithMockUser(username = "admin", password = "password", roles = "READER")
	public void homePageWithUser() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/etl")).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
		;

	}

}
