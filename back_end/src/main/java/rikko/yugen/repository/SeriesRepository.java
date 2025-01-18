package rikko.yugen.repository;
import rikko.yugen.model.Series;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeriesRepository extends JpaRepository<Series, Long>{
    Series findByName(String name);
    List<Series> findByCollection(String collection);

}
