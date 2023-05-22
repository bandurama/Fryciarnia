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
public class OrdersMappingTest
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
  void testAPIOrdersInfo()
  {
    /* TODO: perms issues, better check this one up */
    mockMvc.perform(post("/api/orders/info/774b481c-d982-4203-b95e-91eb950c75bc"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string("{\"data\":{\"dbOrders\":{\"uuid\":\"774b481c-d982-4203-b95e-91eb950c75bc\",\"ticket\":1,\"ctime\":\"20-05-2023 21:56\",\"owner\":\"114926486642104485922\",\"holding\":\"81a9b91b-35a7-4c20-8833-c1fd35a979b3\",\"orderStatus\":\"DONE\",\"isTakeout\":false},\"adpOrderMeals\":[{\"dbMeal\":{\"uuid\":\"5deccbfa-55a0-470f-8218-4f38c070a556\",\"name\":\"Burger z wołowiną\",\"price\":17.99,\"image\":\"/meal/burger2.png\",\"icon\":\"/meal/burger.png\",\"isListed\":true},\"dbOrder\":{\"origin\":\"774b481c-d982-4203-b95e-91eb950c75bc\",\"meal\":\"5deccbfa-55a0-470f-8218-4f38c070a556\",\"quantity\":1}},{\"dbMeal\":{\"uuid\":\"5e38b43a-6510-42a7-adaf-dc33386b8048\",\"name\":\"Burger Jalapeño\",\"price\":22.99,\"image\":\"/meal/burger ostry2.jpg\",\"icon\":\"/meal/burger ostry.png\",\"isListed\":true},\"dbOrder\":{\"origin\":\"774b481c-d982-4203-b95e-91eb950c75bc\",\"meal\":\"5e38b43a-6510-42a7-adaf-dc33386b8048\",\"quantity\":1}},{\"dbMeal\":{\"uuid\":\"7e3ee843-27a5-4240-a02b-7c2e937b35e9\",\"name\":\"Duża Pepsi\",\"price\":12.99,\"image\":\"/meal/pepsi2.jpg\",\"icon\":\"/meal/pepsi.png\",\"isListed\":true},\"dbOrder\":{\"origin\":\"774b481c-d982-4203-b95e-91eb950c75bc\",\"meal\":\"7e3ee843-27a5-4240-a02b-7c2e937b35e9\",\"quantity\":1}}],\"dbHolding\":{\"uuid\":\"81a9b91b-35a7-4c20-8833-c1fd35a979b3\",\"localization\":\"Kielce, Galeria Echo, Świętokrzyska 20\",\"manager\":\"66e4243a-cfef-4614-b15d-a5b14412220d\"}},\"ok\":true}"))
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
  @Order(3)
  void testAPIOrdersListAdmin()
  {
    Cookie cookie = new Cookie("fry_sess", "admin_debug");
    mockMvc.perform(post("/api/orders/list").cookie(cookie))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(f ->
        {
          JSONObject jsonObject = new JSONObject(f.getResponse().getContentAsString());
          assert (jsonObject.has("ok"));
          assert (jsonObject.getBoolean("ok"));
          assert (jsonObject.getJSONArray("data").length() == 4);
        })
        .andReturn();
  }

  @SneakyThrows
  @Test
  @Order(3)
  void testAPIOrdersListWeb()
  {
    Cookie cookie = new Cookie("fry_sess", "web_debug");
    mockMvc.perform(post("/api/orders/list").cookie(cookie))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(f ->
        {
          JSONObject jsonObject = new JSONObject(f.getResponse().getContentAsString());
          assert (jsonObject.has("ok"));
          assert (jsonObject.getBoolean("ok"));
          assert (jsonObject.getJSONArray("data").length() == 4);
        })
        .andReturn();
  }

  @SneakyThrows
  @Test
  @Order(4)
  void testAPIOrdersListMen()
  {
    Cookie cookie = new Cookie("fry_sess", "men_debug");
    mockMvc.perform(post("/api/orders/list").cookie(cookie))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(f ->
        {
          JSONObject jsonObject = new JSONObject(f.getResponse().getContentAsString());
          assert (jsonObject.has("ok"));
          assert (jsonObject.getBoolean("ok"));
          assert (jsonObject.getJSONArray("data").length() == 3);
        })
        .andReturn();
  }

  @SneakyThrows
  @Test
  @Order(5)
  void testAPIOrdersFail()
  {
    Cookie cookie = new Cookie("fry_sess", "men_debug");
    mockMvc.perform(post("/api/orders/fail/b5ef0af2-ab72-496c-a55c-749880fbc2d1").cookie(cookie))
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
