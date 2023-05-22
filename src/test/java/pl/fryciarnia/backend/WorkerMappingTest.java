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
public class WorkerMappingTest
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
  void testAPIDbWorkerList ()
  {
    Cookie cookie = new Cookie("fry_sess", "men_debug");
    mockMvc.perform(post("/api/worker/list").cookie(cookie))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(f ->
        {
          JSONObject jsonObject = new JSONObject(f.getResponse().getContentAsString());
          assert (jsonObject.has("ok"));
          assert (jsonObject.getBoolean("ok"));
          assert (jsonObject.getJSONObject("data").getJSONArray("hardware").length() == 2);
          assert (jsonObject.getJSONObject("data").getJSONArray("people").length() == 1);
        })
        .andReturn();
  }

  @SneakyThrows
  @Test
  @Order(3)
  void testAPIDbWorkerCookEdit ()
  {
    Cookie cookie = new Cookie("fry_sess", "men_debug");
    String req = "{\"uuid\":\"65e753c6-3e63-4d8f-9772-a6dbf592b586\",\"surname\":\"Kuchciński\"}";
    mockMvc.perform(post("/api/worker/cook/edit").cookie(cookie).content(req))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string("{\"data\":{\"uuid\":\"7cea5b2a-9b20-4525-ab68-2a4e982e1255\",\"worker\":\"65e753c6-3e63-4d8f-9772-a6dbf592b586\",\"holding\":\"81a9b91b-35a7-4c20-8833-c1fd35a979b3\",\"salary\":1500.0,\"isHardware\":false},\"ok\":true}"))
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
  void testAPIDbWorkerHire ()
  {
    Cookie cookie = new Cookie("fry_sess", "men_debug");
    mockMvc.perform(post("/api/worker/hire").cookie(cookie))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string("{\"data\":\"http://bandurama.ddns.net/register?holding\\u003d81a9b91b-35a7-4c20-8833-c1fd35a979b3\",\"ok\":true}"))
        .andReturn();
  }

  @SneakyThrows
  @Test
  @Order(5)
  void testAPIDbWorkerHolding ()
  {
    Cookie cookie = new Cookie("fry_sess", "kitchen_debug");
    mockMvc.perform(post("/api/worker/holding").cookie(cookie))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string("{\"data\":{\"uuid\":\"81a9b91b-35a7-4c20-8833-c1fd35a979b3\",\"localization\":\"Kielce, Galeria Echo, Świętokrzyska 20\",\"manager\":\"66e4243a-cfef-4614-b15d-a5b14412220d\"},\"ok\":true}"))
        .andReturn();
  }

}
