package es.unex.cum.mdai.vrp.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.unex.cum.mdai.vrp.domain.FoodDeliveryOrder;
import es.unex.cum.mdai.vrp.domain.Rider;
import es.unex.cum.mdai.vrp.domain.RiderStatus;
import es.unex.cum.mdai.vrp.services.RiderService;

public class OrderAssigner {

    private final RiderService riderService;
    private String depotAddress = "C. Hoy Diario de Extremadura, 2, 06800, Mérida, Badajoz";

    public OrderAssigner(RiderService riderService) {
        this.riderService = riderService;
    }

    public void assignOrders(List<FoodDeliveryOrder> orders) throws Exception {
        // Calculate the total items and the capacity of available riders
        int totalItems = calculateTotalItems(orders);
        List<Rider> availableRiders = riderService.findByStatus(RiderStatus.AT_RESTAURANT);

        if (availableRiders.isEmpty()) {
            throw new NoAvailableRidersException("No riders available for assignment.");
        }

        int totalCapacity = calculateTotalCapacity(availableRiders);

        if (totalItems > totalCapacity) {
            throw new InsufficientCapacityException("Total capacity of available riders is not enough to handle the orders.");
        }

        // Check if riders have pending orders
        if (!ridersDontHavePendingOrders(availableRiders)) {
            throw new RidersWithPendingOrdersException("Some riders have pending orders.");
        }

        // Prepare data for the solver
        DataModel dataModel = prepareDataModel(orders, availableRiders);

        // Solve the assignment problem
        Solver solver = new Solver();
        Map<Integer, List<String>> solution = solver.getSolution(dataModel);

        // If solver couldn't find any solution
        if (solution == null || solution.isEmpty()) {
            throw new SolverNoSolutionException("Solver couldn't find any solution.");
        }

        // Print the solution for debugging purposes
        System.out.println(solution);

        // Assign orders to riders based on the solution
        assignOrdersToRiders(solution, orders, availableRiders);
        
        generateQRCode(availableRiders);
        
    }


    private void generateQRCode(List<Rider> availableRiders) throws Exception {
    	String origin = depotAddress;
		// Loop through the available riders
		for (Rider rider : availableRiders) {
			// Get the order's addresses
			List<String> addresses = new ArrayList<>();
			for (FoodDeliveryOrder order : rider.getCurrentOrders()) {
				addresses.add(order.getDestination());
			}
			// Generate the QR code
			String url = QRCodeGenerator.generateRouteUrl(origin, addresses);
			String fileName = "qr_" + rider.getId() +".png";
            String path = "./qr-images/" + fileName;
			QRCodeGenerator.saveQrCode(url, path);
		}
		
	}

	private int calculateTotalItems(List<FoodDeliveryOrder> orders) {
        int totalItems = 0;
        for (FoodDeliveryOrder order : orders) {
            totalItems += order.getNumOfItems();
        }
        return totalItems;
    }

    private int calculateTotalCapacity(List<Rider> availableRiders) {
        int totalCapacity = 0;
        for (Rider rider : availableRiders) {
            totalCapacity += rider.getCapacity();
        }
        return totalCapacity;
    }

    private DataModel prepareDataModel(List<FoodDeliveryOrder> orders, List<Rider> availableRiders) throws Exception {
        List<String> addresses = buildAddressesList(orders);
        long[] demands = buildDemandsArray(orders);
        long[] vehicleCapacities = buildVehicleCapacitiesArray(availableRiders);
        int vehicleNumber = availableRiders.size();
        int depot = 0;

        DataModel dataModel = new DataModel(addresses, demands, vehicleNumber, vehicleCapacities, depot);

        return dataModel;
    }

    private List<String> buildAddressesList(List<FoodDeliveryOrder> orders) {
        List<String> addresses = new ArrayList<>();
        addresses.add(depotAddress);
        for (FoodDeliveryOrder order : orders) {
            addresses.add(order.getDestination());
        }
        return addresses;
    }

    private long[] buildDemandsArray(List<FoodDeliveryOrder> orders) {
        long[] demands = new long[orders.size() + 1];
        demands[0] = 0; // Depot demand is always 0
        for (int i = 0; i < orders.size(); i++) {
            demands[i + 1] = orders.get(i).getNumOfItems();
        }
        return demands;
    }

    private long[] buildVehicleCapacitiesArray(List<Rider> availableRiders) {
        long[] vehicleCapacities = new long[availableRiders.size()];
        for (int i = 0; i < availableRiders.size(); i++) {
            vehicleCapacities[i] = availableRiders.get(i).getCapacity();
        }
        return vehicleCapacities;
    }

    private boolean ridersDontHavePendingOrders(List<Rider> availableRiders) {
        for (Rider rider : availableRiders) {
            if (!rider.getCurrentOrders().isEmpty()) {
                return false; // Return false if any rider has pending orders
            }
        }
        return true; // All riders are free
    }

    private void assignOrdersToRiders(Map<Integer, List<String>> solution, List<FoodDeliveryOrder> orders, List<Rider> availableRiders) {
        for (int i = 0; i < solution.size(); i++) {
            Rider rider = availableRiders.get(i);
            List<String> orderAddresses = solution.get(i);

            for (String orderAddress : orderAddresses) {
                for (FoodDeliveryOrder order : orders) {
                    if (order.getDestination().equals(orderAddress)) {
                        rider.getCurrentOrders().add(order);
                        order.setRider(rider);
                    }
                }
            }
        }
    }

    // Custom exceptions for different error cases
    
    public static class NoAvailableRidersException extends Exception {
        private static final long serialVersionUID = -3868951241180042525L;

		public NoAvailableRidersException(String message) {
            super(message);
        }
    }

    public static class InsufficientCapacityException extends Exception {
        private static final long serialVersionUID = -2895246188962784201L;

		public InsufficientCapacityException(String message) {
            super(message);
        }
    }

    public static class RidersWithPendingOrdersException extends Exception {
        private static final long serialVersionUID = 1427715596462080292L;

		public RidersWithPendingOrdersException(String message) {
            super(message);
        }
    }

    public static class SolverNoSolutionException extends Exception {
        private static final long serialVersionUID = -905172123045896039L;

		public SolverNoSolutionException(String message) {
            super(message);
        }
    }
}
