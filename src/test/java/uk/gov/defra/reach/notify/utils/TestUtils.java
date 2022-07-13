package uk.gov.defra.reach.notify.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.json.JSONObject;
import uk.gov.defra.reach.notify.entity.Notification;
import uk.gov.defra.reach.notify.entity.NotificationRequest;
import uk.gov.service.notify.SendEmailResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class TestUtils {

  private static UUID testNotificationUUID = UUID.fromString("123e4567-e89b-12d3-a456-556642440000");
  private static UUID testTemplateUUID = UUID.fromString("123e4577-e89b-12d3-a456-556642440000");

  private static final String TEST_BODY = "body";
  private static final String TEST_SUBJECT = "subject";
  private static final String TEST_FROM_EMAIL = "from_email";
  private static final String TEST_URI = "uri";
  private static final String TEST_REFERENCE = "reference";
  private static final String TEST_TEMPLATE_VERSION = "templateVersion";
  private static final String TEST_TEMPLATE_URI = "templateUri";
  private static final String ID = "id";
  public static final String FROM_EMAIL = "FROM_EMAIL";
  public static final String HEALTH_CHECK_ENDPOINT= "/healthcheck";
  public static final String EMAIL_ENDPOINT= "/email";
  public static final String AUTHORIZATION_HEADER= "Authorization";
  public static final String CONTENT_TYPE_HEADER= "Content-Type";
  public static final String APPLICATION_JSON_HEADER_VALUE= "application/json";
  public static final String NEW_REACH_MESSAGE = "NEW_REACH_MESSAGE";

  private static final int TEST_VERSION = 2;

  public static SendEmailResponse createSendEmailResponse() {

    JSONObject contentJsonObject = new JSONObject();
    contentJsonObject.put(TEST_BODY, TEST_BODY);
    contentJsonObject.put(TEST_SUBJECT, TEST_SUBJECT);
    contentJsonObject.put(TEST_FROM_EMAIL, TEST_FROM_EMAIL);

    JSONObject templateJsonObject = new JSONObject();
    templateJsonObject.put(ID, testTemplateUUID);
    templateJsonObject.put("version", TEST_VERSION);
    templateJsonObject.put(TEST_URI, TEST_URI);

    JSONObject notifyResponseJsonObject = new JSONObject();
    notifyResponseJsonObject.put(ID, testNotificationUUID);
    notifyResponseJsonObject.put(TEST_REFERENCE, TEST_REFERENCE);
    notifyResponseJsonObject.put("content", contentJsonObject);
    notifyResponseJsonObject.put("templateId", testTemplateUUID);
    notifyResponseJsonObject.put(TEST_TEMPLATE_VERSION, TEST_TEMPLATE_VERSION);
    notifyResponseJsonObject.put(TEST_TEMPLATE_URI, TEST_TEMPLATE_URI);
    notifyResponseJsonObject.put(FROM_EMAIL, FROM_EMAIL);
    notifyResponseJsonObject.put("template", templateJsonObject);

    return new SendEmailResponse(notifyResponseJsonObject.toString());
  }

  public static SendEmailResponse createSendEmailResponseWithoutEmail() {

    JSONObject contentJsonObject = new JSONObject();
    contentJsonObject.put(TEST_BODY, TEST_BODY);
    contentJsonObject.put(TEST_SUBJECT, TEST_SUBJECT);

    JSONObject templateJsonObject = new JSONObject();
    templateJsonObject.put(ID, testTemplateUUID);
    templateJsonObject.put("version", TEST_VERSION);
    templateJsonObject.put(TEST_URI, TEST_URI);

    JSONObject notifyResponseJsonObject = new JSONObject();
    notifyResponseJsonObject.put(ID, testNotificationUUID);
    notifyResponseJsonObject.put(TEST_REFERENCE, TEST_REFERENCE);
    notifyResponseJsonObject.put("content", contentJsonObject);
    notifyResponseJsonObject.put("templateId", testTemplateUUID);
    notifyResponseJsonObject.put(TEST_TEMPLATE_VERSION, TEST_TEMPLATE_VERSION);
    notifyResponseJsonObject.put(TEST_TEMPLATE_URI, TEST_TEMPLATE_URI);
    notifyResponseJsonObject.put(FROM_EMAIL, FROM_EMAIL);
    notifyResponseJsonObject.put("template", templateJsonObject);

    return new SendEmailResponse(notifyResponseJsonObject.toString());
  }

  public static SendEmailResponse createSendEmailResponseWithoutReference() {

    JSONObject contentJsonObject = new JSONObject();
    contentJsonObject.put(TEST_BODY, TEST_BODY);
    contentJsonObject.put(TEST_SUBJECT, TEST_SUBJECT);
    contentJsonObject.put(TEST_FROM_EMAIL, TEST_FROM_EMAIL);

    JSONObject templateJsonObject = new JSONObject();
    templateJsonObject.put(ID, testTemplateUUID);
    templateJsonObject.put("version", TEST_VERSION);
    templateJsonObject.put(TEST_URI, TEST_URI);

    JSONObject notifyResponseJsonObject = new JSONObject();
    notifyResponseJsonObject.put(ID, testNotificationUUID);
    notifyResponseJsonObject.put("content", contentJsonObject);
    notifyResponseJsonObject.put("templateId", testTemplateUUID);
    notifyResponseJsonObject.put(TEST_TEMPLATE_VERSION, TEST_TEMPLATE_VERSION);
    notifyResponseJsonObject.put(TEST_TEMPLATE_URI, TEST_TEMPLATE_URI);
    notifyResponseJsonObject.put(FROM_EMAIL, FROM_EMAIL);
    notifyResponseJsonObject.put("template", templateJsonObject);

    return new SendEmailResponse(notifyResponseJsonObject.toString());
  }

    public static Notification createNotification() {
    Notification notification =  new Notification();
    notification.setNotificationId(testNotificationUUID);
    notification.setReference(TEST_REFERENCE);
    notification.setTemplateId(testTemplateUUID);
    notification.setTemplateVersion(TEST_VERSION);
    notification.setTemplateUri(TEST_URI);
    notification.setBody(TEST_BODY);
    notification.setSubject(TEST_SUBJECT);
    notification.setFromEmail(TEST_FROM_EMAIL);
    return notification;
  }

  public static NotificationRequest createTestNotificationRequest(String event) {

    Map<String, String> testPersonalisation = new HashMap<String, String>();
    testPersonalisation.put("last_name", "testLastName");
    testPersonalisation.put("legal_entity", "testLegalEntity");
    testPersonalisation.put("submission_name", "testSubmissionName");
    testPersonalisation.put("first_name", "testFirstName");
    return new NotificationRequest(event,"test@email.com", testPersonalisation);
  }
  // Compare two notification objects disregarding UpdatedAt Values
  public static void compareNotification(Notification expected, Notification actual) {
    expected.setUpdatedAt(null);
    actual.setUpdatedAt(null);
    assertThat(actual).isEqualTo(expected);
  }

}
