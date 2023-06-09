package pl.fryciarnia.backend;

import jakarta.servlet.http.Cookie;
import lombok.SneakyThrows;
import org.json.JSONObject;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RecipeMappingTest
{
  @Autowired
  private MockMvc mockMvc;

  @Test
  @Order(1)
  void resetDB()
  {
    BackendMappingTest.testAPIBackendReset(mockMvc);
  }

  @SneakyThrows
  @Test
  @Order(2)
  void testAPIDbRecipeList ()
  {
    String req = "{\"uuid\":\"5deccbfa-55a0-470f-8218-4f38c070a556\"}";
    mockMvc.perform(post("/api/recipe/list").content(req))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(f ->
        {
          JSONObject jsonObject = new JSONObject(f.getResponse().getContentAsString());
          assert (jsonObject.has("ok"));
          assert (jsonObject.getBoolean("ok"));
          assert (jsonObject.getJSONArray("data").length() == 5);
        })
        .andReturn();
  }

  @SneakyThrows
  @Test
  @Order(3)
  void testAPIDbRecipeInsert ()
  {
    Cookie cookie = new Cookie("fry_sess", "admin_debug");
    String req = "{\"step\":\"20\",\"ingridient\":\"5bc6c186-5a73-4b5c-92ac-76079922ff8e\",\"quantity\":\"100\",\"instruction\":\"Pokeczupić\",\"meal\":\"dc081f3b-80df-42dd-8576-20cc43d5004f\",\"uuid\":\"\"}";
    mockMvc.perform(post("/api/recipe/insert").content(req).cookie(cookie))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(f ->
        {
          JSONObject jsonObject = new JSONObject(f.getResponse().getContentAsString());
          assert (jsonObject.has("ok"));
          assert (jsonObject.getBoolean("ok"));
        })
        .andReturn();
  }

  @SneakyThrows
  @Test
  @Order(4)
  void testAPIDbRecipeRemove ()
  {
    Cookie cookie = new Cookie("fry_sess", "admin_debug");
    String req = "{\"uuid\":\"63b4f999-b084-43bf-b533-077812dcd372\"}";

    mockMvc.perform(post("/api/recipe/remove").content(req).cookie(cookie))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(f ->
        {
          JSONObject jsonObject = new JSONObject(f.getResponse().getContentAsString());
          assert (jsonObject.has("ok"));
          assert (jsonObject.getBoolean("ok"));
        })
        .andReturn();
  }

}
