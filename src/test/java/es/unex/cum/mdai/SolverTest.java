package es.unex.cum.mdai;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import es.unex.cum.mdai.vrp.domain.FoodDeliveryOrder;
import es.unex.cum.mdai.vrp.domain.Rider;
import es.unex.cum.mdai.vrp.utils.DataModel;
import es.unex.cum.mdai.vrp.utils.Solver;

public class SolverTest {
	@Test
	public void test() throws Exception {
		List<FoodDeliveryOrder> orders = List.of(
				new FoodDeliveryOrder("Pablo Setrakian", 1, "C. Almorchón 13, 06800, Mérida, Badajoz"),
				new FoodDeliveryOrder("María García", 1, "Avd. Los Milagros 24, 06800, Mérida, Badajoz"),
				new FoodDeliveryOrder("Luis Fernández", 1, "Avd. Reina Sofía 18, 06800, Mérida, Badajoz"),
				new FoodDeliveryOrder("Ana Martín", 1, "Avd. Juan Carlos I 1, 06800, Mérida, Badajoz"),
				new FoodDeliveryOrder("Carlos López", 1, "C. Almendralejo 1, 06800, Mérida, Badajoz"),
		        new FoodDeliveryOrder("Luiky", 1, "C. Sta. Teresa de Jornet 38, 06800, Mérida, Badajoz"));
		        
		List<Rider> riders = List.of(new Rider("Pablo", 5), new Rider("Diego", 5));
		
		List<String> addresses = new ArrayList<>();
		
		// The addresses list is the same as the order's destination addresses but with the depot address at the beginning
		addresses.add("C. Hoy Diario de Extremadura, 2, 06800, Mérida, Badajoz");
		for (FoodDeliveryOrder order : orders) {
			addresses.add(order.getDestination());
		}
		
		// The demand array is the same as the order's demands but with the depot demand at the beginning, which is 0
		long[] demands = new long[orders.size() + 1];
		demands[0] = 0;
		for (int i = 0; i < orders.size(); i++) {
			demands[i + 1] = orders.get(i).getNumOfItems();
		}
		
		// The vehicle number is the same as the rider's number
		int vehicleNumber = riders.size();
		
		// The vehicle capacities are the same as the rider's capacities
		long[] vehicleCapacities = new long[riders.size()];
		for (int i = 0; i < riders.size(); i++) {
			vehicleCapacities[i] = riders.get(i).getCapacity();
		}
		
		// Set the depot
	    int depot = 0;
	    

	    DataModel dataModel = new DataModel(addresses, demands, vehicleNumber, vehicleCapacities, depot);

	    Solver solver = new Solver();
	    Map<Integer, List<String>> solution = solver.getSolution(dataModel);
	    
	    System.out.println(solution);
	    
	}

}
