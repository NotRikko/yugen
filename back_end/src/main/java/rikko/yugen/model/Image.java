package rikko.yugen.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url; 

    @Column(nullable = false)
    private Long contentId;

    @Column(nullable = false)
    private String contentType;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
}