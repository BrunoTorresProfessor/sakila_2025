package br.com.sakila;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class SakilaApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void contextLoads() {
	}

	@Test
	void languagePageLoadsDataFromDatabase() throws Exception {
		mockMvc.perform(get("/languages"))
				.andExpect(status().isOk())
				.andExpect(view().name("language"))
				.andExpect(model().attributeExists("languages", "languageForm"));
	}

	@Test
	void languageEditPageLoadsExistingLanguage() throws Exception {
		mockMvc.perform(get("/languages/1/edit"))
				.andExpect(status().isOk())
				.andExpect(view().name("language"))
				.andExpect(model().attribute("editing", true));
	}
}
