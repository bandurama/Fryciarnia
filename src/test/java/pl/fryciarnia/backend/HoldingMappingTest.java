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
public class HoldingMappingTest
{
  @Autowired
  private MockMvc mockMvc;


  @Test
  @Order(value = 1)
  void resetDB ()
  {
    BackendMappingTest.testAPIBackendReset(mockMvc);
  }

  @SneakyThrows
  @Test
  @Order(value = 2)
  void testAPIDbHoldingInsert ()
  {
    Cookie cookie = new Cookie("fry_sess", "admin_debug");
    String req = "{\"localization\":\"Przyłogi\",\"manager\":\"1e751db3-df98-4f56-8947-36fc922823bb\",\"uuid\":\"\"}";
    mockMvc.perform(post("/api/holding/insert").cookie(cookie).content(req))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(f -> {
          JSONObject jsonObject = new JSONObject(f.getResponse().getContentAsString());
          assert (jsonObject.has("ok"));
          assert (jsonObject.getBoolean("ok"));
          assert (jsonObject.getJSONObject("data").has("localization"));
          assert (jsonObject.getJSONObject("data").getString("localization").equals("Przyłogi"));
        });
  }

  @SneakyThrows
  @Test
  @Order(value = 3)
  void testAPIDbHoldingList ()
  {
    Cookie cookie = new Cookie("fry_sess", "admin_debug");
    mockMvc.perform(post("/api/holding/list").cookie(cookie))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(f -> {
          JSONObject jsonObject = new JSONObject(f.getResponse().getContentAsString());
          assert (jsonObject.has("ok"));
          assert (jsonObject.getBoolean("ok"));
          assert (jsonObject.getJSONArray("data").length() >= 2);
        });
  }

  @SneakyThrows
  @Test
  @Order(value = 4)
  void testAPIDbHoldingEdit ()
  {
    String req = "{\"localization\":\"Kielce, Galeria Planty\",\"manager\":\"3f1ebe0a-1d69-487f-8c02-33a849f872bb\",\"uuid\":\"67905b9d-5af0-4a65-9d32-ac4b191b7bfd\"}";
    Cookie cookie = new Cookie("fry_sess", "admin_debug");
    mockMvc.perform(post("/api/holding/edit").cookie(cookie).content(req))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(f -> {
          JSONObject jsonObject = new JSONObject(f.getResponse().getContentAsString());
          assert (jsonObject.has("ok"));
          assert (jsonObject.getBoolean("ok"));
        });
  }


  @SneakyThrows
  @Test
  @Order(value = 5)
  void testAPIDbHoldingInfo ()
  {
    String req = "{\"uuid\":\"67905b9d-5af0-4a65-9d32-ac4b191b7bfd\"}";
    Cookie cookie = new Cookie("fry_sess", "admin_debug");
    mockMvc.perform(post("/api/holding/info").cookie(cookie).content(req))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string("{\"data\":{\"uuid\":\"67905b9d-5af0-4a65-9d32-ac4b191b7bfd\",\"localization\":\"Kielce, Galeria Planty\",\"manager\":\"3f1ebe0a-1d69-487f-8c02-33a849f872bb\"},\"ok\":true}"))
        .andExpect(f -> {
          JSONObject jsonObject = new JSONObject(f.getResponse().getContentAsString());
          assert (jsonObject.has("ok"));
          assert (jsonObject.getBoolean("ok"));
        });
  }

  @SneakyThrows
  @Test
  @Order(value = 6)
  void testAPIDbHoldingRemove ()
  {
    String req = "{\"uuid\":\"67905b9d-5af0-4a65-9d32-ac4b191b7bfd\"}";
    Cookie cookie = new Cookie("fry_sess", "admin_debug");
    mockMvc.perform(post("/api/holding/remove").cookie(cookie).content(req))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(f -> {
          JSONObject jsonObject = new JSONObject(f.getResponse().getContentAsString());
          assert (jsonObject.has("ok"));
          assert (jsonObject.getBoolean("ok"));
        });
  }

}
