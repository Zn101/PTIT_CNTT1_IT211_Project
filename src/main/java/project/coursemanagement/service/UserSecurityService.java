package project.coursemanagement.service;

import project.coursemanagement.entity.User;

public interface UserSecurityService {

    User getCurrentUser(String username);
}