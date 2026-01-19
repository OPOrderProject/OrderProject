package kr.co.order.app.domain.reservation.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import kr.co.order.app.domain.restaurant.domain.Restaurant;
import kr.co.order.app.domain.user.domain.User;
import kr.co.order.app.global.util.AuthorCertification;
import kr.co.order.app.global.util.ReservationStatus;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Restaurant restaurant;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;
    private String reserverName;
    @Column(name = "start_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime startTime;
    @Column(name = "end_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime endTime;
    @Enumerated(value = EnumType.STRING)
    private ReservationStatus status;
    private LocalDateTime reserveTime;

    public boolean certification(User user){
        return AuthorCertification.certification(this.reserverName, user.getNickname());
    }

    public void updateStatus(ReservationStatus status){
        this.status = status;
    }

    public void updateReserveTime(LocalTime startTime, LocalTime endTime){
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public boolean isExpired(LocalDateTime now){
        return reserveTime.plusMinutes(5).isBefore(now);
    }

    @Builder
    public Reservation(Restaurant restaurant, User user, String reserverName, LocalTime startTime,
                       LocalTime endTime, ReservationStatus status){
        this.restaurant = restaurant;
        this.user = user;
        this.reserverName = reserverName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.reserveTime = LocalDateTime.now();
    }
}
