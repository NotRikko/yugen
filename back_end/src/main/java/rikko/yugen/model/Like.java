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
    @JoinColumn(name = "post_id", nullable = false, unique = true)
    private Post post;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private LocalDateTime createdAt = LocalDateTime.now();

    
}
