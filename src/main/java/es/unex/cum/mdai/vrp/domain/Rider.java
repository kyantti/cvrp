package es.unex.cum.mdai.vrp.domain;

import java.time.Instant;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rider {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private RiderStatus status;
	private int capacity;
	private int numOfRides;
	@OneToMany
	private List<FoodDeliveryOrder> currentOrders;
	
	private Instant timeOutStart; // Time when the rider goes on the road
    private long timeOut; // Time spent on the road in seconds

    public Rider(String name, int capacity) {
        this.name = name;
        this.status = RiderStatus.AT_RESTAURANT;
        this.capacity = capacity;
        this.numOfRides = 0;
        this.currentOrders = null;
        this.timeOutStart = null;
        this.timeOut = 0;
    }

    public void startTimeOut() {
        this.timeOutStart = Instant.now(); // Record the time when rider goes on the road
    }

    public void endTimeOut() {
        if (this.timeOutStart != null) {
            this.timeOut = Instant.now().getEpochSecond() - this.timeOutStart.getEpochSecond(); // Calculate time spent
            this.timeOutStart = null; // Reset the start time
        }
    }
	
}
