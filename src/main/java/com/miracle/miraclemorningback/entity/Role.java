package com.miracle.miraclemorningback.entity;

public enum Role {

    TEMP_USER(Authority.TEMP_USER), // 임시 사용자 권한
    USER(Authority.USER), // 사용자 권한
    ADMIN(Authority.ADMIN); // 관리자 권한

    private final String authority;

    Role(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    public static class Authority {
        public static final String TEMP_USER = "ROLE_TEMP_USER";
        public static final String USER = "ROLE_USER";
        public static final String ADMIN = "ROLE_ADMIN";
    }
}