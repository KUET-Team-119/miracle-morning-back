package com.miracle.miraclemorningback.constants;

public interface SecurityConstants {
	// 비밀키 값을 가진 JWT 키. 백엔드 애플리케이션만 알 수 있음
	public static final String JWT_KEY = "jxgEQeXHuPq8VdbyYFNkANdudQ53YUn4";
	public static final String JWT_HEADER = "Authorization";
	public static final String CLAIM_KEY_MEMBERNAME = "membername";
	public static final String CLAIM_KEY_ROLE = "role";
	public static final int EXPIRATION = 30000000;
}
