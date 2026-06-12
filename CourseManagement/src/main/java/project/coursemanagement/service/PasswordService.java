package project.coursemanagement.service;

import project.coursemanagement.dto.request.ChangePasswordRequest;
import project.coursemanagement.dto.request.ForgotPasswordRequest;
import project.coursemanagement.dto.request.ResetPasswordRequest;
import project.coursemanagement.dto.response.ForgotPasswordResponse;

public interface PasswordService {

    void changePassword(String username, ChangePasswordRequest request);

    ForgotPasswordResponse forgotPassword(ForgotPasswordRequest request);

    void resetPassword(ResetPasswordRequest request);
}
