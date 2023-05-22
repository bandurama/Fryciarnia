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
public class MealMappingTest
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
  void testAPIDbMealList()
  {
    mockMvc.perform(post("/api/meal/list"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(f ->
        {
          JSONObject jsonObject = new JSONObject(f.getResponse().getContentAsString());
          assert (jsonObject.has("ok"));
          assert (jsonObject.getBoolean("ok"));
          assert (jsonObject.getJSONArray("data").length() == 10);
        })
        .andReturn();
  }

  @SneakyThrows
  @Test
  @Order(3)
  void testAPIDbMealInfo()
  {
    String req = "{\"uuid\":\"50e72bae-b95f-4a05-a85c-87f911ff7665\"}";
    mockMvc.perform(post("/api/meal/info").content(req))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string("{\"data\":{\"uuid\":\"50e72bae-b95f-4a05-a85c-87f911ff7665\",\"name\":\"Frytki małe\",\"price\":8.99,\"image\":\"/meal/frytki2.png\",\"icon\":\"/meal/frytki.png\",\"isListed\":true},\"ok\":true}"))
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
  void testAPIDbMealRemove()
  {
    Cookie cookie = new Cookie("fry_sess", "admin_debug");
    String req = "{\"uuid\":\"50e72bae-b95f-4a05-a85c-87f911ff7665\"}";
    mockMvc.perform(post("/api/meal/remove").content(req).cookie(cookie))
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
  @Order(5)
  void testAPIDbMealInsert()
  {
    Cookie cookie = new Cookie("fry_sess", "admin_debug");
    String req = "{\"name\":\"Prosiak Burgir\",\"price\":\"6.90\",\"image\":\"/meal/burger świnia2.jpg\",\"icon\":\"/meal/burger świnia.png\",\"isListed\":true,\"uuid\":\"0a90106e-8a78-45f9-a09b-0ca1d9f5ab66\"}";
    mockMvc.perform(post("/api/meal/edit").content(req).cookie(cookie))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string("{\"data\":{\"uuid\":\"0a90106e-8a78-45f9-a09b-0ca1d9f5ab66\",\"name\":\"Prosiak Burgir\",\"price\":6.9,\"image\":\"/meal/burger świnia2.jpg\",\"icon\":\"/meal/burger świnia.png\",\"isListed\":true},\"ok\":true}"))
        .andExpect(f ->
        {
          JSONObject jsonObject = new JSONObject(f.getResponse().getContentAsString());
          assert (jsonObject.has("ok"));
          assert (jsonObject.getBoolean("ok"));
        })
        .andReturn();
  }
}
