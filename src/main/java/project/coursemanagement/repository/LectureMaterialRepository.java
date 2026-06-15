package project.coursemanagement.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.coursemanagement.entity.LectureMaterial;

@Repository
public interface LectureMaterialRepository extends JpaRepository<LectureMaterial, Long> {

    Page<LectureMaterial> findByCourseId(Long courseId, Pageable pageable);
}