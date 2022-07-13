package uk.gov.defra.reach.notify.integration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import uk.gov.defra.reach.notify.utils.TestUtils;

public class SecurityTestIT extends IntegrationCommon {

  private ObjectMapper objectMapper = new ObjectMapper();

  @Test
  public void healthCheck_shouldReturn200_withoutAuthentication() {

    ResponseEntity<?> response = REST_TEMPLATE.exchange(NOTIFY_SERVICE_URL + TestUtils.HEALTH_CHECK_ENDPOINT, HttpMethod.GET, new HttpEntity<>(null, null), String.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void emptyEndpointForAzurePing_shouldReturn200_withoutAuthentication() {
    ResponseEntity<?> response = REST_TEMPLATE.exchange(NOTIFY_SERVICE_URL + "/", HttpMethod.GET, new HttpEntity<>(null, null), String.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void email_shouldReturn201_withCorrectHeaders() throws JsonProcessingException {
    HttpHeaders authHeaders = new HttpHeaders();
    authHeaders.add(TestUtils.AUTHORIZATION_HEADER, JWT_TOKEN);
    authHeaders.add(TestUtils.CONTENT_TYPE_HEADER, TestUtils.APPLICATION_JSON_HEADER_VALUE);

    String requestBody = objectMapper.writeValueAsString(TestUtils.createTestNotificationRequest(TestUtils.NEW_REACH_MESSAGE));

    ResponseEntity<?> response = REST_TEMPLATE.exchange(NOTIFY_SERVICE_URL + TestUtils.EMAIL_ENDPOINT, HttpMethod.POST, new HttpEntity<>(requestBody, authHeaders), String.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
  }

  @Test
  public void email_shouldReturn403_withMissingAuthentication() {
    ResponseEntity<?> response = REST_TEMPLATE.exchange(NOTIFY_SERVICE_URL + TestUtils.EMAIL_ENDPOINT, HttpMethod.POST, new HttpEntity<>(null, null), String.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
  }
}
