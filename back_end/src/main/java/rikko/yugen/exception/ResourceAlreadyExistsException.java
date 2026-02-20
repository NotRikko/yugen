package rikko.yugen.exception;

import org.springframework.http.HttpStatus;

public class ResourceAlreadyExistsException extends ApiException {

  public ResourceAlreadyExistsException(String resourceName, String fieldName, Object fieldValue) {
    super(HttpStatus.CONFLICT,
            String.format("%s with %s '%s' already exists",
                    resourceName,
                    fieldName,
                    fieldValue)
    );
  }
}