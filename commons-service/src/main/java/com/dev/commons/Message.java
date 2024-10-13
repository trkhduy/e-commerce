package com.dev.commons;

public class Message {

    public static class Authentication {
        public static final String UNAUTHENTICATED = "Unauthenticated";
        public static final String LOG_OUT = "Log out successfully";
    }

    public static class User {
        public static final String USER_DOES_NOT_EXITED = "User does not exist";
        public static final String USER_ALREADY_EXISTED = "User already existed";
        public static final String CREATE_FAILED = "Create User failed";
        public static final String UPDATE_FAILED = "Update User failed";
        public static final String DELETE = "Delete User Successful";
    }

    public static class Role{
        public static final String DELETE = "Delete Role Successful";
        public static final String ROLE_ALREADY_EXISTED = "Role already exists";
        public static final String ROLE_DOES_NOT_EXITED = "Role does not exist";
        public static final String CREATE_FAILED = "Create Role failed";
        public static final String UPDATE_FAILED = "Update Role failed";
    }

    public static class Permission{
        public static final String DELETE = "Delete permission successfully";
        public static final String PERMISSION_ALREADY_EXISTED = "Permission already exists";
        public static final String CREATE_FAILED = "Create Permission failed";
        public static final String UPDATE_FAILED = "Update Permission failed";
    }
}
