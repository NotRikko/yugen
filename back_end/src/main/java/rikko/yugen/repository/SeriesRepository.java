package rikko.yugen.repository;
import rikko.yugen.model.Series;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeriesRepository extends JpaRepository<Series, Long>{
    Series findByName(String name);
}