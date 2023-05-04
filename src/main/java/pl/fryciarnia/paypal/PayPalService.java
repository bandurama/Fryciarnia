package pl.fryciarnia.paypal;

import java.util.Base64;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class PayPalService
{

  @Value("${paypal.client.id}")
  private String clientId;
  @Value("${paypal.client.secret}")
  private String clientSecret;

  private RestTemplate restTemplate = new RestTemplate();

  @SneakyThrows
  public String getAccessToken()
  {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    headers.setBasicAuth(clientId, clientSecret);

    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add("grant_type", "client_credentials");

    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

    String url = "https://api-m.sandbox.paypal.com/v1/oauth2/token";
    ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

    Map<String, Object> m = (new ObjectMapper()).readValue(response.getBody(), Map.class);

    if (m.containsKey("access_token"))
      return (String) m.get("access_token");

    return "nope";
  }

  public String getTransactionDetails (String transactionId)
  {
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(getAccessToken());

    String url = "https://api-m.sandbox.paypal.com/v2/checkout/orders/" + transactionId;
    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
    return response.getBody().toString();
  }
}