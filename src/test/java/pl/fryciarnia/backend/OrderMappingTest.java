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
public class OrderMappingTest
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
  void testAPIOrdersOrderSuccess()
  {
    String req = "{\"holdingUUID\":\"81a9b91b-35a7-4c20-8833-c1fd35a979b3\",\"orderedMeals\":[{\"mealUUID\":\"5deccbfa-55a0-470f-8218-4f38c070a556\",\"quantity\":2},{\"mealUUID\":\"7e3ee843-27a5-4240-a02b-7c2e937b35e9\",\"quantity\":1},{\"mealUUID\":\"290343b0-6a2e-49f6-ba4f-54faf3d3d93a\",\"quantity\":1}]}";
    Cookie cookie = new Cookie("fry_sess", "web_debug");
    mockMvc.perform(post("/api/order/1").content(req).cookie(cookie))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(f ->
        {
          JSONObject jsonObject = new JSONObject(f.getResponse().getContentAsString());
          assert (jsonObject.has("ok"));
          assert (jsonObject.getBoolean("ok"));
          assert (jsonObject.getJSONObject("data").getString("owner").equals("114926486642104485922"));
          assert (jsonObject.getJSONObject("data").getString("holding").equals("81a9b91b-35a7-4c20-8833-c1fd35a979b3"));
          assert (jsonObject.getJSONObject("data").getString("orderStatus").equals("PAYING"));
          assert (jsonObject.getJSONObject("data").getBoolean("isTakeout"));
        })
        .andReturn();
  }

  @SneakyThrows
  @Test
  @Order(3)
  void testAPIOrdersOrderFail()
  {
    String req = "{\"holdingUUID\":\"81a9b91b-35a7-4c20-8833-c1fd35a979b3\",\"orderedMeals\":[{\"mealUUID\":\"5deccbfa-55a0-470f-8218-4f38c070a556\",\"quantity\":2},{\"mealUUID\":\"7e3ee843-27a5-4240-a02b-7c2e937b35e9\",\"quantity\":1},{\"mealUUID\":\"290343b0-6a2e-49f6-ba4f-54faf3d3d93a\",\"quantity\":1}]}";
    Cookie cookie = new Cookie("fry_sess", "web_debug");
    mockMvc.perform(post("/api/order/1").content(req).cookie(cookie))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(f ->
        {
          JSONObject jsonObject = new JSONObject(f.getResponse().getContentAsString());
          assert (jsonObject.has("ok"));
          assert (!jsonObject.getBoolean("ok"));
          assert (jsonObject.getString("msg").equals("NOEXE"));
        })
        .andReturn();
  }
}
