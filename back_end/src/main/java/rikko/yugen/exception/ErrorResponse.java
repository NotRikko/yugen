package rikko.yugen.exception;

import java.util.Map;

public class ErrorResponse {
    private String message;
    private int status;
    private String error;
    private Map<String, String> fieldErrors;

    public ErrorResponse(String message, int status, String error) {
        this.message = message;
        this.status = status;
        this.error = error;
    }

    public ErrorResponse(String message, int status, String error, Map<String, String> fieldErrors) {
        this(message, status, error);
        this.fieldErrors = fieldErrors;
    }

    // Getters and setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
    public Map<String, String> getFieldErrors() { return fieldErrors; }
    public void setFieldErrors(Map<String, String> fieldErrors) { this.fieldErrors = fieldErrors; }
}