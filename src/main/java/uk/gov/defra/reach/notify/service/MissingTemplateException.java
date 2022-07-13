package uk.gov.defra.reach.notify.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Template Not Found")
class MissingTemplateException extends RuntimeException {

  private static final long serialVersionUID = -8792866746572148404L;
}
