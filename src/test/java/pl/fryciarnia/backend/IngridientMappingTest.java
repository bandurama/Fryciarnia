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
public class IngridientMappingTest
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
  void testAPIDbIngridientList()
  {
    mockMvc.perform(post("/api/ingridient/list"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(f ->
        {
          JSONObject jsonObject = new JSONObject(f.getResponse().getContentAsString());
          assert (jsonObject.has("ok"));
          assert (jsonObject.getBoolean("ok"));
          assert (jsonObject.getJSONArray("data").length() >= 17);
        });
  }

  @SneakyThrows
  @Test
  @Order(3)
  void testAPIDbIngridientInfo()
  {
    String req = "{\"uuid\":\"1f3f2af3-5ce4-4b78-8983-f2aa2696c5ef\"}";
    Cookie cookie = new Cookie("fry_sess", "admin_debug");
    mockMvc.perform(post("/api/ingridient/info").cookie(cookie).content(req))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string("{\"data\":{\"uuid\":\"1f3f2af3-5ce4-4b78-8983-f2aa2696c5ef\",\"name\":\"Ziemniaki\",\"icon\":\"/ing/ziemniaki.png\"},\"ok\":true}"))
        .andExpect(f ->
        {
          JSONObject jsonObject = new JSONObject(f.getResponse().getContentAsString());
          assert (jsonObject.has("ok"));
          assert (jsonObject.getBoolean("ok"));
        });
  }

  @SneakyThrows
  @Test
  @Order(4)
  void testAPIDbIngridientInsert()
  {
    String req = "{\"name\":\"Łzy Developera\",\"icon\":\"https://cdn-images-1.medium.com/v2/resize:fit:1200/1*DWI6QZnF2qBYNwAW_0zoXw.jpeg\",\"uuid\":\"\"}";
    Cookie cookie = new Cookie("fry_sess", "admin_debug");
    mockMvc.perform(post("/api/ingridient/insert").cookie(cookie).content(req))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(f ->
        {
          JSONObject jsonObject = new JSONObject(f.getResponse().getContentAsString());
          assert (jsonObject.has("ok"));
          assert (jsonObject.getBoolean("ok"));
        });
  }

  @SneakyThrows
  @Test
  @Order(5)
  void testAPIDbIngridientEdit()
  {
    String req = "{\"name\":\"Sznytka\",\"icon\":\"/ing/sznytka.png\",\"uuid\":\"5079c4c2-d113-43c2-9f86-72a2dd3a0d49\"}";
    Cookie cookie = new Cookie("fry_sess", "admin_debug");
    mockMvc.perform(post("/api/ingridient/edit").cookie(cookie).content(req))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(f ->
        {
          JSONObject jsonObject = new JSONObject(f.getResponse().getContentAsString());
          assert (jsonObject.has("ok"));
          assert (jsonObject.getBoolean("ok"));
        });
  }

  @SneakyThrows
  @Test
  @Order(6)
  void testAPIDbIngridientRemove()
  {
    String req = "{\"uuid\":\"30abecf4-89f7-4205-babf-0fcf7dcdf0d4\"}";
    Cookie cookie = new Cookie("fry_sess", "admin_debug");
    mockMvc.perform(post("/api/ingridient/remove").cookie(cookie).content(req))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(f ->
        {
          JSONObject jsonObject = new JSONObject(f.getResponse().getContentAsString());
          assert (jsonObject.has("ok"));
          assert (jsonObject.getBoolean("ok"));
        });
  }

}
