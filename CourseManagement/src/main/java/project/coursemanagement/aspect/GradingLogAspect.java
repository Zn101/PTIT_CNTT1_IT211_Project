package project.coursemanagement.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import project.coursemanagement.dto.request.GradeSubmissionRequest;
import project.coursemanagement.dto.response.SubmissionResponse;

@Aspect
@Component
@Slf4j
public class GradingLogAspect {

    @AfterReturning(
            pointcut = "execution(* project.coursemanagement.service.impl.SubmissionServiceImpl.gradeSubmission(..))",
            returning = "result"
    )
    public void logAfterGrading(JoinPoint joinPoint, Object result) {
        GradeSubmissionRequest request = (GradeSubmissionRequest) joinPoint.getArgs()[0];
        Long lecturerId = (Long) joinPoint.getArgs()[1];
        SubmissionResponse response = (SubmissionResponse) result;

        log.info("[GRADING] Lecturer ID: {} graded Submission ID: {} with Score: {} | Status: {}",
                lecturerId,
                request.getSubmissionId(),
                response.getScore(),
                response.getStatus()
        );
    }

    @AfterThrowing(
            pointcut = "execution(* project.coursemanagement.service.impl.SubmissionServiceImpl.gradeSubmission(..))",
            throwing = "ex"
    )
    public void logAfterGradingError(JoinPoint joinPoint, Exception ex) {
        Long lecturerId = (Long) joinPoint.getArgs()[1];

        log.error("[GRADING] Lecturer ID: {} failed to grade submission | Error: {}",
                lecturerId,
                ex.getMessage()
        );
    }
}