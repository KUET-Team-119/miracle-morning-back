-- 사용자 테이블 생성
CREATE TABLE member (
    member_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '사용자 식별자',
    member_name VARCHAR(10) NOT NULL UNIQUE COMMENT '닉네임',
    password CHAR(5) NOT NULL  COMMENT '비밀번호',
    role ENUM('TEMP_USER','USER','ADMIN') COMMENT '권한',
    created_at DATETIME(6) COMMENT '생성 일자',
    modified_at DATETIME(6) COMMENT '수정 일자',
    PRIMARY KEY (member_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='사용자';

-- 루틴 테이블 생성
CREATE TABLE routine (
    routine_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '루틴 식별자',
    routine_name VARCHAR(255) COMMENT '루틴명',
    member_id BIGINT COMMENT '사용자 식별자',
    day_of_week VARCHAR(255) COMMENT '실천 요일',
    certification VARCHAR(255) COMMENT '인증 방법',
    start_time TIME(6) COMMENT '시작 시간',
    end_time TIME(6) COMMENT '종료 시간',
    is_activated BOOLEAN DEFAULT TRUE COMMENT '활성화 여부',
    created_at DATETIME(6) COMMENT '생성 일자',
    modified_at DATETIME(6) COMMENT '수정 일자',
    PRIMARY KEY (routine_id),
    FOREIGN KEY (member_id) REFERENCES member(member_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='루틴';

-- 기록 테이블 생성
CREATE TABLE result (
    result_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '기록 식별자',
    member_id BIGINT COMMENT '사용자 식별자',
    routine_id BIGINT COMMENT '루틴 식별자',
    done_at DATETIME(6) COMMENT '실천 시간',
    proof_file_path VARCHAR(255) COMMENT '인증 사진 경로',
    created_at DATETIME(6) COMMENT '생성 일자',
    modified_at DATETIME(6) COMMENT '수정 일자',
    PRIMARY KEY (result_id),
    FOREIGN KEY (member_id) REFERENCES member(member_id),
    FOREIGN KEY (routine_id) REFERENCES routine(routine_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='기록';

-- 오류 제보 테이블 생성
CREATE TABLE complaint (
    complaint_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '오류 제보 식별자',
    member_name VARCHAR(255) COMMENT '닉네임',
    content VARCHAR(255) COMMENT '내용',
    created_at DATETIME(6) COMMENT '생성 일자',
    modified_at DATETIME(6) COMMENT '수정 일자',
    PRIMARY KEY (complaint_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='오류 제보';