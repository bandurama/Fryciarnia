package pl.fryciarnia.backend;

import lombok.SneakyThrows;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class BackendMappingTest
{
  @SneakyThrows
  public static void testAPIBackendReset (MockMvc mockMvc)
  {
    mockMvc.perform(get("/reset")).andDo(f -> {
      JSONObject jsonObject = new JSONObject(f.getResponse().getContentAsString());
      assert(jsonObject.has("ok"));
      assert(((Boolean) jsonObject.get("ok")));
    }).andReturn();
  }
}
