package project.coursemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.coursemanagement.entity.Submission;

import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    List<Submission> findByCourseId(Long courseId);

    List<Submission> findByStudentId(Long studentId);
}
