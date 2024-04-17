package plus.suja.teach.teachshop.dao;

import org.springframework.data.repository.CrudRepository;
import plus.suja.teach.teachshop.entity.Video;

import java.util.List;

public interface VideoRepository extends CrudRepository<Video, Integer> {
    @Override
    List<Video> findAll();
}
