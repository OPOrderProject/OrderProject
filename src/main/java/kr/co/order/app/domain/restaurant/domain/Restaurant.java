package kr.co.order.app.domain.restaurant.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

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
    private String name;
    private int maxReservationCount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime openTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime closeTime;
    @CreationTimestamp
    private Timestamp createdAt;

    @Builder
    public Restaurant(String name, int maxReservationCount, LocalTime openTime, LocalTime closeTime){
        this.name = name;
        this.maxReservationCount = maxReservationCount;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }
}
