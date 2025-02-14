package rikko.yugen.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "likes")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Long contentId; 

    @Column(nullable = false)
    private String contentType;

    private LocalDateTime createdAt = LocalDateTime.now();

    //Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getContentId() { 
        return contentId; 
    }
    public void setContentId(Long contentId) { 
        this.contentId = contentId; 
    }

    public String getContentType() { 
        return contentType; 
    }

    public void setContentType(String contentType) { 
        this.contentType = contentType; 
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
}
