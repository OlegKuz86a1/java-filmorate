package ru.yandex.practicum.filmorate.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureTestDatabase
@Sql({"/schema.sql", "/test-data.sql"})
@SpringBootTest
@AutoConfigureMockMvc
@ComponentScan("ru.yandex.practicum.filmorate")
class FilmEntityControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testCreateWithEmptyName() throws Exception {
		String json = "{\"name\":\"\", \"id\":null}";
		mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON)
				.content(json)).andExpect(status().isBadRequest());
	}

	@Test
	void testCreateFilmWithoutName() throws Exception {
		String json = "{}";
		mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testUpdateFilmWithEmptyName() throws Exception {
		String json = "{\"name\":\"\", \"id\":1}";
		mockMvc.perform(put("/films").contentType(MediaType.APPLICATION_JSON)
				.content(json)).andExpect(status().isBadRequest());
	}

	@Test
	void testUpdateFilmWithoutName() throws Exception {
		String json = "{}";
		mockMvc.perform(put("/films").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isBadRequest());
	}
}