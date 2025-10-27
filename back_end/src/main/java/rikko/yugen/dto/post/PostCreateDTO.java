package rikko.yugen.dto.post;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
public class PostCreateDTO {
    private String content;
    private Set<String> images;
    private Long artistId;  
    private Long productId;

}
