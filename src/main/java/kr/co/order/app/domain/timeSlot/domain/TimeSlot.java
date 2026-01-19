package kr.co.order.app.domain.timeSlot.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import kr.co.order.app.domain.restaurant.domain.Restaurant;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "time_slot", uniqueConstraints = {
        @UniqueConstraint(
                columnNames = {"restaurant_id", "start_time"}
        )
})
public class TimeSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Restaurant restaurant;
    @Column(name = "start_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime startTime;
    @Column(name = "end_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime endTime;
    private int reservedCount;

    @Version
    private Long version;

    public void cancel(){
        this.reservedCount++;
    }

    public void reserve(){
        if(this.reservedCount <= 0){
            throw new IllegalArgumentException("예약이 불가능한 시간대입니다 : " + this.startTime);
        }

        this.reservedCount--;
    }

    @Builder
    public TimeSlot(Restaurant restaurant, LocalTime startTime, LocalTime endTime, int reservedCount){
        this.restaurant = restaurant;
        this.startTime = startTime;
        this.endTime = endTime;
        this.reservedCount = reservedCount;
    }
}
