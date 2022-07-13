package uk.gov.defra.reach.notify.integration;

import org.springframework.boot.test.web.client.TestRestTemplate;

public abstract class IntegrationCommon {
  
  protected final static String JWT_TOKEN = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJhYWFhYWFhYS0wMDAwLTAwMDEtZmZmZi1mZmZmZmZmZmZmZmYiLCJjb250YWN0SWQiOiJjY2NjY2NjYy0wMDAwLTAwMDEtZmZmZi1mZmZmZmZmZmZmZmYiLCJsZWdhbEVudGl0eUlkIjpudWxsLCJhY2NvdW50SWQiOiI2MWM0NTA0ZC1lODliLTEyZDMtYTQ1Ni0xMTExMTExMTExMTEiLCJsZWdhbEVudGl0eSI6IlJpY2htb25kIENoZW1pY2FscyIsImNvbXBhbnlUeXBlIjoiTGltaXRlZCBjb21wYW55IiwibGVnYWxFbnRpdHlSb2xlIjoiUkVBQ0ggTWFuYWdlciIsImdyb3VwcyI6bnVsbCwic291cmNlIjoiQjJDIiwicm9sZSI6IklORFVTVFJZX1VTRVIiLCJlbWFpbCI6ImluZHVzdHJ5MUBlbWFpbC5jb20iLCJpYXQiOjE2MDg2NDIzMDksImV4cCI6MTY3MTc1NzUwOX0.rjuZZ9c5EbTdrYkdHRF0JsOKfZy019no2LAEM2QEtIo";

  protected static final String NOTIFY_SERVICE_URL = System.getProperty("NOTIFY_SERVICE_URL", "http://localhost:8080");

  protected static final TestRestTemplate REST_TEMPLATE = new TestRestTemplate();


}
