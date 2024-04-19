package plus.suja.teach.teachshop.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import plus.suja.teach.teachshop.entity.Video;

import java.util.List;

public interface VideoRepository extends JpaRepository<Video, Integer> {
    @Override
    List<Video> findAll();

    List<Video> findAllByCourseId(Integer courseId);
}
