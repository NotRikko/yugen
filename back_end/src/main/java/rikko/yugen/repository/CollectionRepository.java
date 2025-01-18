package rikko.yugen.repository;
import rikko.yugen.model.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectionRepository extends JpaRepository<Collection, Long>{
    Collection findByName(String name);

}
