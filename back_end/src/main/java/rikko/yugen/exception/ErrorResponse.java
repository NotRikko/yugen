package rikko.yugen.exception;

public class ErrorResponse {
    private String message;
    private int status;
    private String error;

    public ErrorResponse(String message, int status, String error) {
        this.message = message;
        this.status = status;
        this.error = error;
    }

    // Getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    } 
}