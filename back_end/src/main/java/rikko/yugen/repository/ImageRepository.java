package rikko.yugen.repository;
import rikko.yugen.model.Image;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByContentIdAndContentType(Long contentId, String contentType);
}
