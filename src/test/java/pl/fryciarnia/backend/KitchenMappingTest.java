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
public class KitchenMappingTest
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
  void testAPIKitchenOrders()
  {
    Cookie cookie = new Cookie("fry_sess", "kitchen_debug");
    mockMvc.perform(post("/api/kitchen/orders").cookie(cookie))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string("{\"data\":[{\"dbUser\":{\"uuid\":\"114926486642104485922\",\"isGoogleAccount\":true,\"mail\":\"banduram1@gmail.com\",\"password\":\"\",\"type\":\"Web\"},\"adpOrdersAdpOrderMealDbHolding\":{\"dbOrders\":{\"uuid\":\"b5ef0af2-ab72-496c-a55c-749880fbc2d1\",\"ticket\":1,\"ctime\":\"22-05-2023 18:46\",\"owner\":\"114926486642104485922\",\"holding\":\"81a9b91b-35a7-4c20-8833-c1fd35a979b3\",\"orderStatus\":\"PAID\",\"isTakeout\":false},\"adpOrderMeals\":[{\"dbMeal\":{\"uuid\":\"5deccbfa-55a0-470f-8218-4f38c070a556\",\"name\":\"Burger z wołowiną\",\"price\":17.99,\"image\":\"/meal/burger2.png\",\"icon\":\"/meal/burger.png\",\"isListed\":true},\"dbOrder\":{\"origin\":\"b5ef0af2-ab72-496c-a55c-749880fbc2d1\",\"meal\":\"5deccbfa-55a0-470f-8218-4f38c070a556\",\"quantity\":1}},{\"dbMeal\":{\"uuid\":\"5e38b43a-6510-42a7-adaf-dc33386b8048\",\"name\":\"Burger Jalapeño\",\"price\":22.99,\"image\":\"/meal/burger ostry2.jpg\",\"icon\":\"/meal/burger ostry.png\",\"isListed\":true},\"dbOrder\":{\"origin\":\"b5ef0af2-ab72-496c-a55c-749880fbc2d1\",\"meal\":\"5e38b43a-6510-42a7-adaf-dc33386b8048\",\"quantity\":1}},{\"dbMeal\":{\"uuid\":\"7e3ee843-27a5-4240-a02b-7c2e937b35e9\",\"name\":\"Duża Pepsi\",\"price\":12.99,\"image\":\"/meal/pepsi2.jpg\",\"icon\":\"/meal/pepsi.png\",\"isListed\":true},\"dbOrder\":{\"origin\":\"b5ef0af2-ab72-496c-a55c-749880fbc2d1\",\"meal\":\"7e3ee843-27a5-4240-a02b-7c2e937b35e9\",\"quantity\":1}}],\"dbHolding\":{\"uuid\":\"81a9b91b-35a7-4c20-8833-c1fd35a979b3\",\"localization\":\"Kielce, Galeria Echo, Świętokrzyska 20\",\"manager\":\"66e4243a-cfef-4614-b15d-a5b14412220d\"}}}],\"ok\":true}"))
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
  void testAPIKitchenReady()
  {
    Cookie cookie = new Cookie("fry_sess", "kitchen_debug");
    mockMvc.perform(post("/api/kitchen/ready/b5ef0af2-ab72-496c-a55c-749880fbc2d1").cookie(cookie))
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
  void testAPIKitchenDone()
  {
    Cookie cookie = new Cookie("fry_sess", "kitchen_debug");
    mockMvc.perform(post("/api/kitchen/done/b5ef0af2-ab72-496c-a55c-749880fbc2d1").cookie(cookie))
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
