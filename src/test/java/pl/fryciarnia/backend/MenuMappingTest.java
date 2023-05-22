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
public class MenuMappingTest
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
  void testAPIMenu()
  {
    String req = "{\"uuid\": \"81a9b91b-35a7-4c20-8833-c1fd35a979b3\"}";
    mockMvc.perform(post("/api/menu").content(req))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string("{\"data\":[{\"uuid\":\"50e72bae-b95f-4a05-a85c-87f911ff7665\",\"name\":\"Frytki małe\",\"price\":8.99,\"image\":\"/meal/frytki2.png\",\"icon\":\"/meal/frytki.png\",\"isListed\":true},{\"uuid\":\"231df355-c0a5-4563-b405-044fc7a0ef0b\",\"name\":\"Frytki duże\",\"price\":15.99,\"image\":\"/meal/frytki2.png\",\"icon\":\"/meal/frytki.png\",\"isListed\":true},{\"uuid\":\"5deccbfa-55a0-470f-8218-4f38c070a556\",\"name\":\"Burger z wołowiną\",\"price\":17.99,\"image\":\"/meal/burger2.png\",\"icon\":\"/meal/burger.png\",\"isListed\":true},{\"uuid\":\"950c7a31-3a5b-4379-b172-afceca857ed1\",\"name\":\"Burger z kurczakiem\",\"price\":19.99,\"image\":\"/meal/burger kurczak2.png\",\"icon\":\"/meal/burger kurczak.png\",\"isListed\":true},{\"uuid\":\"85c39b20-927f-409d-a047-792d69d0ef1a\",\"name\":\"Vege Burger\",\"price\":21.99,\"image\":\"https://cdn.mcdonalds.pl/uploads/20230216150856/veggie-burger-miniatura.jpg\",\"icon\":\"https://cdn.mcdonalds.pl/uploads/20230216151225/veggie-burger.png\",\"isListed\":true},{\"uuid\":\"290343b0-6a2e-49f6-ba4f-54faf3d3d93a\",\"name\":\"Kawałki kurczaka\",\"price\":13.99,\"image\":\"/meal/kawałki kurczaka2.jpg\",\"icon\":\"/meal/kawałki kurczaka.png\",\"isListed\":true},{\"uuid\":\"5e38b43a-6510-42a7-adaf-dc33386b8048\",\"name\":\"Burger Jalapeño\",\"price\":22.99,\"image\":\"/meal/burger ostry2.jpg\",\"icon\":\"/meal/burger ostry.png\",\"isListed\":true},{\"uuid\":\"dc081f3b-80df-42dd-8576-20cc43d5004f\",\"name\":\"Mała Papsi\",\"price\":8.99,\"image\":\"/meal/pepsi2.jpg\",\"icon\":\"/meal/pepsi.png\",\"isListed\":true},{\"uuid\":\"7e3ee843-27a5-4240-a02b-7c2e937b35e9\",\"name\":\"Duża Pepsi\",\"price\":12.99,\"image\":\"/meal/pepsi2.jpg\",\"icon\":\"/meal/pepsi.png\",\"isListed\":true}],\"ok\":true}"))
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
  void testAPIMenuMeal()
  {
    String req = "{\"uuid\": \"81a9b91b-35a7-4c20-8833-c1fd35a979b3\"}";
    mockMvc.perform(post("/api/menu").content(req))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string("{\"data\":[{\"uuid\":\"50e72bae-b95f-4a05-a85c-87f911ff7665\",\"name\":\"Frytki małe\",\"price\":8.99,\"image\":\"/meal/frytki2.png\",\"icon\":\"/meal/frytki.png\",\"isListed\":true},{\"uuid\":\"231df355-c0a5-4563-b405-044fc7a0ef0b\",\"name\":\"Frytki duże\",\"price\":15.99,\"image\":\"/meal/frytki2.png\",\"icon\":\"/meal/frytki.png\",\"isListed\":true},{\"uuid\":\"5deccbfa-55a0-470f-8218-4f38c070a556\",\"name\":\"Burger z wołowiną\",\"price\":17.99,\"image\":\"/meal/burger2.png\",\"icon\":\"/meal/burger.png\",\"isListed\":true},{\"uuid\":\"950c7a31-3a5b-4379-b172-afceca857ed1\",\"name\":\"Burger z kurczakiem\",\"price\":19.99,\"image\":\"/meal/burger kurczak2.png\",\"icon\":\"/meal/burger kurczak.png\",\"isListed\":true},{\"uuid\":\"85c39b20-927f-409d-a047-792d69d0ef1a\",\"name\":\"Vege Burger\",\"price\":21.99,\"image\":\"https://cdn.mcdonalds.pl/uploads/20230216150856/veggie-burger-miniatura.jpg\",\"icon\":\"https://cdn.mcdonalds.pl/uploads/20230216151225/veggie-burger.png\",\"isListed\":true},{\"uuid\":\"290343b0-6a2e-49f6-ba4f-54faf3d3d93a\",\"name\":\"Kawałki kurczaka\",\"price\":13.99,\"image\":\"/meal/kawałki kurczaka2.jpg\",\"icon\":\"/meal/kawałki kurczaka.png\",\"isListed\":true},{\"uuid\":\"5e38b43a-6510-42a7-adaf-dc33386b8048\",\"name\":\"Burger Jalapeño\",\"price\":22.99,\"image\":\"/meal/burger ostry2.jpg\",\"icon\":\"/meal/burger ostry.png\",\"isListed\":true},{\"uuid\":\"dc081f3b-80df-42dd-8576-20cc43d5004f\",\"name\":\"Mała Papsi\",\"price\":8.99,\"image\":\"/meal/pepsi2.jpg\",\"icon\":\"/meal/pepsi.png\",\"isListed\":true},{\"uuid\":\"7e3ee843-27a5-4240-a02b-7c2e937b35e9\",\"name\":\"Duża Pepsi\",\"price\":12.99,\"image\":\"/meal/pepsi2.jpg\",\"icon\":\"/meal/pepsi.png\",\"isListed\":true}],\"ok\":true}"))
        .andExpect(f ->
        {
          JSONObject jsonObject = new JSONObject(f.getResponse().getContentAsString());
          assert (jsonObject.has("ok"));
          assert (jsonObject.getBoolean("ok"));
        })
        .andReturn();
  }
}
