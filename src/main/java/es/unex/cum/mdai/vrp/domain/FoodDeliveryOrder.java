package es.unex.cum.mdai.vrp.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodDeliveryOrder {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String customer;
	private int numOfItems;
	private String destination;
	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private OrderStatus status;
	
	@ManyToOne
	private Rider rider;
	
	 public FoodDeliveryOrder(String customer, int numOfItems, String destination) {
	        this.customer = customer;
	        this.numOfItems = numOfItems;
	        this.destination = destination;
	        this.status = OrderStatus.PENDING; // Setting status to PENDING
	        this.rider = null;
	 }

	@Override
	public String toString() {
		return "Customer: " + customer + ", Num. Items: " + numOfItems
				+ ", Address: " + destination + ", Status: " + status;
	}
	 
	 
	
}
