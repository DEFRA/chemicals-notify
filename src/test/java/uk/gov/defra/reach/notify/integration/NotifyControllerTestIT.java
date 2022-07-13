package uk.gov.defra.reach.notify.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import uk.gov.defra.reach.notify.entity.NotificationRequest;
import uk.gov.defra.reach.notify.utils.TestUtils;

public class NotifyControllerTestIT extends IntegrationCommon {

  private HttpHeaders createAuthHeaders() {
    HttpHeaders authHeaders = new HttpHeaders();
    authHeaders.add(TestUtils.AUTHORIZATION_HEADER, JWT_TOKEN);
    authHeaders.add(TestUtils.CONTENT_TYPE_HEADER, TestUtils.APPLICATION_JSON_HEADER_VALUE);
    return authHeaders;
  }

  private Map<String, String> createPersonalisation() {
    Map<String, String> personalisation = new HashMap<>();
    personalisation.put("first_name", "First Name");
    personalisation.put("last_name", "Last Name");
    personalisation.put("submission_name", "Integration Submission");
    personalisation.put("legal_entity", "Integration inc.");
    personalisation.put("email_address", "industry1@email.address");
    return personalisation;
  }

  @Test
  public void rootEndpoint_shouldReturn200() {
    ResponseEntity<?> response = REST_TEMPLATE.exchange(NOTIFY_SERVICE_URL + "/", HttpMethod.GET, new HttpEntity<>(null, null), String.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isEqualTo("ok");
  }

  @Test
  public void emailEndpoint_shouldReturn201_whenValidBody(){
    NotificationRequest body = new NotificationRequest(TestUtils.NEW_REACH_MESSAGE, TestUtils.FROM_EMAIL, createPersonalisation());
    ResponseEntity<?> response = REST_TEMPLATE.exchange(NOTIFY_SERVICE_URL + TestUtils.EMAIL_ENDPOINT, HttpMethod.POST, new HttpEntity<>(body, createAuthHeaders()), String.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
  }

  @Test
  public void emailEndpoint_shouldReturn400_whenInvalidBody() {
    NotificationRequest body = new NotificationRequest(TestUtils.NEW_REACH_MESSAGE, null, null);
    ResponseEntity<?> response = REST_TEMPLATE.exchange(NOTIFY_SERVICE_URL + TestUtils.EMAIL_ENDPOINT, HttpMethod.POST, new HttpEntity<>(body, createAuthHeaders()), String.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  public void emailEndpoint_shouldReturn404_whenEventNotFound() {
    NotificationRequest body = new NotificationRequest("Bad Event", TestUtils.FROM_EMAIL, createPersonalisation());
    ResponseEntity<?> response = REST_TEMPLATE.exchange(NOTIFY_SERVICE_URL + TestUtils.EMAIL_ENDPOINT, HttpMethod.POST, new HttpEntity<>(body, createAuthHeaders()), String.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }
}
