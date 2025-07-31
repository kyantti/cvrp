package es.unex.cum.mdai.vrp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import es.unex.cum.mdai.vrp.domain.FoodDeliveryOrder;
import es.unex.cum.mdai.vrp.domain.Rider;
import es.unex.cum.mdai.vrp.services.FoodDeliveryOrderService;
import es.unex.cum.mdai.vrp.services.RiderService;

@Component
public class DataBaseInitializer implements CommandLineRunner {

	private final FoodDeliveryOrderService orderService;
	private final RiderService riderService;

	public DataBaseInitializer(FoodDeliveryOrderService orderService, RiderService riderService) {
		this.orderService = orderService;
		this.riderService = riderService;
	}

	@Override
	public void run(String... args) throws Exception {
		// Initialize database with some default orders
		List<FoodDeliveryOrder> initialOrders = List.of(
				new FoodDeliveryOrder("Pablo Setrakian", 3, "C. Almorchón 13, 06800, Mérida, Badajoz"),
				new FoodDeliveryOrder("María García", 5, "Avd. Los Milagros 24, 06800, Mérida, Badajoz"),
				new FoodDeliveryOrder("Luis Fernández", 2, "Avd. Reina Sofía 18, 06800, Mérida, Badajoz"),
				new FoodDeliveryOrder("Ana Martín", 4, "Avd. Juan Carlos I 1, 06800, Mérida, Badajoz"),
				new FoodDeliveryOrder("Carlos López", 1, "C. Almendralejo 1, 06800, Mérida, Badajoz"));
		        new FoodDeliveryOrder("Luiky", 3, "C. Sta. Teresa de Jornet 38, 06800, Mérida, Badajoz");

		orderService.saveAll(initialOrders);
		
		List<Rider> riders = new ArrayList<>();
		
		riders.add(new Rider("Pablo", 5));
		
		riderService.saveAll(riders);
	
		System.out.println("Database initialized with default data:");
		orderService.findAll().forEach(System.out::println);
	}
}
