package uk.gov.defra.reach.notify.integration;

import static org.assertj.core.api.Assertions.assertThat;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public class HealthcheckTestIT extends IntegrationCommon {

  @Test
  public void testHealthcheck_shouldReturnHealthyStateWhenAppIsRunningCorrectly() {
    ResponseEntity<?> response = REST_TEMPLATE.exchange(NOTIFY_SERVICE_URL + "/healthcheck", HttpMethod.GET, new HttpEntity<>(null, null), String.class);
    JSONObject healthcheckDetails = new JSONObject(response.getBody().toString());
    JSONObject dbDetails = (JSONObject) healthcheckDetails.get("details");

    String dbStatus = dbDetails.get("database").toString();

    assertThat(healthcheckDetails.get("health")).isEqualTo("HEALTHY");
    assertThat(dbStatus).isEqualTo("HEALTHY");
    assertThat(healthcheckDetails.get("version")).isNotNull();
  }
}
