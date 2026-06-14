package project.coursemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.coursemanagement.entity.LectureMaterial;

import java.util.List;

@Repository
public interface LectureMaterialRepository extends JpaRepository<LectureMaterial, Long> {

    List<LectureMaterial> findByCourseId(Long courseId);
}