package kr.co.order.app.domain.restaurant.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import kr.co.order.app.domain.user.domain.User;
import kr.co.order.app.global.util.AuthorCertification;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.sql.Timestamp;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "restaurants")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;
    private String name;
    private int maxReservationCount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime openTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime closeTime;
    @CreationTimestamp
    private Timestamp createdAt;

    public boolean certification(User user){
        return AuthorCertification.certification(this.user.getNickname(), user.getNickname());
    }

    @Builder
    public Restaurant(User user, String name, int maxReservationCount, LocalTime openTime, LocalTime closeTime){
        this.user = user;
        this.name = name;
        this.maxReservationCount = maxReservationCount;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }
}
