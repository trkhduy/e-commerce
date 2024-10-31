package com.dev.constant;

public class Constants {

    public static class Authentication {
        public static final String USER_ROLE = "USER";
        public static final String ADMIN_ROLE = "ADMIN";
        public static final String SHOP_ROLE = "SHOP";
        public static final String AUTHORIZATION_PREFIX = "ROLE_";
        public static final String BEARER_TOKEN_PREFIX = "Bearer ";
    }

    public static class OrderStatus {
        public static final String NEW = "Mới";
        public static final String UNPROCESSED = "Chưa xử lý";
        public static final String PROCESSED = "Đã xử lý";
        public static final String CONFIRMED = "Đã xác nhận";
        public static final String COMPLETED = "Hoàn Tất";
        public static final String CANCELED = "Đã hủy";
    }

}
