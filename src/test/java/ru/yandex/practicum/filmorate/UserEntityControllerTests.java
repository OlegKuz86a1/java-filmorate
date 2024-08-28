package ru.yandex.practicum.filmorate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest
@AutoConfigureMockMvc
class UserEntityControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testCreateWithEmptyLoginAndLoginWithWhitespace() throws Exception {
        String json = "{\"login\":\"\", \"id\":null}";
        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(status().isBadRequest());

        String json1 = "{\"login\":\"Rubicon 123\", \"id\":null}";
        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                .content(json1)).andExpect(status().isBadRequest());
    }

    @Test
    void testCreateWithEmailEmptyAndEmailWithoutAt() throws Exception {
        String json = "{\"email\":\"null\", \"id\":null}";
        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(status().isBadRequest());

        String json1 = "{\"email\":\"crush_mail.ru\", \"id\":null}";
        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                .content(json1)).andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateWithEmptyLoginAndLoginWithWhitespace() throws Exception {
        String json = "{\"login\":\"\", \"id\":1}";
        mockMvc.perform(put("/users").contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(status().isBadRequest());

        String json1 = "{\"login\":\"Rubicon 123\", \"id\":1}";
        mockMvc.perform(put("/users").contentType(MediaType.APPLICATION_JSON)
                .content(json1)).andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateWithEmailEmptyAndEmailWithoutAt() throws Exception {
        String json = "{\"email\":\"null\", \"id\":1}";
        mockMvc.perform(put("/users").contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(status().isBadRequest());

        String json1 = "{\"email\":\"crush_mail.ru\", \"id\":1}";
        mockMvc.perform(put("/users").contentType(MediaType.APPLICATION_JSON)
                .content(json1)).andExpect(status().isBadRequest());
    }
}