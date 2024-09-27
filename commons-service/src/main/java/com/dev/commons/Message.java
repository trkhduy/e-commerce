package com.dev.commons;

public class Message {

    public static class Authentication {
        public static final String UNAUTHENTICATED = "Unauthenticated";
    }

    public static class User {
        public static final String USER_DOES_NOT_EXITED = "User does not exist";
        public static final String USER_ALREADY_EXISTED = "User already existed";
        public static final String CREATE_FAILED = "Create User failed";
    }

    public static class Role{
        public static final String DELETE = "Delete Role Successful";
    }

    public static class Permission{
        public static final String DELETE = "Delete permission successfully";

    }
}
