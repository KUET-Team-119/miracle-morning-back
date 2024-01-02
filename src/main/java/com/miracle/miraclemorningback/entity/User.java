// package com.miracle.miraclemorningback.entity;

// import java.time.LocalDateTime;

// import org.hibernate.annotations.CreationTimestamp;

// import jakarta.persistence.Column;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.Table;
// import lombok.AccessLevel;
// import lombok.Builder;
// import lombok.Getter;
// import lombok.NoArgsConstructor;
// import lombok.Setter;

// @Table(name = "users")
// @NoArgsConstructor(access = AccessLevel.PROTECTED)
// @Getter
// @Setter
// public class User implements UserDetails {

// @Id
// @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
// @Column(name = "id", updatable = false)
// private Long id;

// @Column(name = "nickname", nullable = false, unique = true)
// private String nickname;

// @Column(name = "password", nullable = false)
// private String password;

// @CreationTimestamp
// @Column(name = "join_date")
// private LocalDateTime joinDate;

// @Column(name = "is_admin", columnDefinition = "boolean default false")
// private Boolean isAdmin;

// @Builder
// public User(String nickname, String password, String auth) {
// this.nickname = nickname;
// this.password = password;
// }

// }
