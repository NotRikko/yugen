package rikko.yugen.dto.comment;

public class CommentRequestDTO {
    private Long userId;
    private String content;

    // getters and setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}