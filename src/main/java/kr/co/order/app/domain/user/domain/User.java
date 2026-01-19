package kr.co.order.app.domain.user.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    private String password;
    @Column(unique = true)
    private String nickname;
    @Column(length = 400)
    private String refreshToken;
    @CreationTimestamp
    private Timestamp createDate;

    public void updateRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }

    @Builder
    public User(String username, String password, String nickname){
        this.username = username;
        this.password = password;
        this.nickname = nickname;
    }
}
