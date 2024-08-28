package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.dto.MpaDto;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@AutoConfigureTestDatabase
@Sql({"/schema.sql", "/test-data.sql"})
@SpringBootTest
@AutoConfigureMockMvc
class FilmorateApplicationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAllFilms() throws Exception {
        mockMvc.perform(get("/films")
                .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(5));
    }

    @Test
    public void testGetById() throws Exception {
        mockMvc.perform(get("/films/3")
                        .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(3));
    }

    @Test
    public void testCreate() throws Exception {
        FilmDto filmDto = new FilmDto();
        filmDto.setName("Test Movie Title");
        filmDto.setDescription("Test Director");
        filmDto.setGenres(List.of(new GenreDto(1, null), new GenreDto(4, null)));
        filmDto.setMpa(new MpaDto(3, null));
        filmDto.setReleaseDate(LocalDate.of(2020, Month.AUGUST, 2));

        mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(filmDto)))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(6))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Test Movie Title"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Test Director"));
    }
}
