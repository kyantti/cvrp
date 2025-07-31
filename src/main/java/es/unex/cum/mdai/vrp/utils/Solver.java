package es.unex.cum.mdai.vrp.utils;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.ortools.Loader;
import com.google.ortools.constraintsolver.Assignment;
import com.google.ortools.constraintsolver.FirstSolutionStrategy;
import com.google.ortools.constraintsolver.LocalSearchMetaheuristic;
import com.google.ortools.constraintsolver.RoutingIndexManager;
import com.google.ortools.constraintsolver.RoutingModel;
import com.google.ortools.constraintsolver.RoutingSearchParameters;
import com.google.ortools.constraintsolver.main;
import com.google.protobuf.Duration;

/** Minimal VRP. */
public final class Solver {

	public Map<Integer, List<String>> getSolution(DataModel data) {
		// Load the native
		Loader.loadNativeLibraries();

		// Create Routing Index Manager
		RoutingIndexManager manager = new RoutingIndexManager(data.distanceMatrix.length, data.vehicleNumber,
				data.depot);

		// Create Routing Model.
		RoutingModel routing = new RoutingModel(manager);

		// Create and register a transit callback.
		final int transitCallbackIndex = routing.registerTransitCallback((long fromIndex, long toIndex) -> {
			// Convert from routing variable Index to user NodeIndex.
			int fromNode = manager.indexToNode(fromIndex);
			int toNode = manager.indexToNode(toIndex);
			return data.distanceMatrix[fromNode][toNode];
		});

		// Define cost of each arc.
		routing.setArcCostEvaluatorOfAllVehicles(transitCallbackIndex);

		// Add Capacity constraint.
		final int demandCallbackIndex = routing.registerUnaryTransitCallback((long fromIndex) -> {
			// Convert from routing variable Index to user NodeIndex.
			int fromNode = manager.indexToNode(fromIndex);
			return data.demands[fromNode];
		});
		routing.addDimensionWithVehicleCapacity(demandCallbackIndex, 0, // null capacity slack
				data.vehicleCapacities, // vehicle maximum capacities
				true, // start cumul to zero
				"Capacity");

		// Setting first solution heuristic.
		RoutingSearchParameters searchParameters = main.defaultRoutingSearchParameters().toBuilder()
				.setFirstSolutionStrategy(FirstSolutionStrategy.Value.PATH_CHEAPEST_ARC)
				.setLocalSearchMetaheuristic(LocalSearchMetaheuristic.Value.GUIDED_LOCAL_SEARCH)
				.setTimeLimit(Duration.newBuilder().setSeconds(1).build()).build();

		// Solve the problem.
		Assignment solution = routing.solveWithParameters(searchParameters);

		// Get the routes for each rider as a map
		Map<Integer, List<String>> routesMap = new HashMap<>();

		for (int i = 0; i < data.vehicleNumber; ++i) {
			long index = routing.start(i);
			List<String> route = new ArrayList<>();

			while (!routing.isEnd(index)) {
				long nodeIndex = manager.indexToNode(index);
				String address = data.addresses.get((int) nodeIndex); // Get the address
				route.add(address);
				index = solution.value(routing.nextVar(index));
			}

			// Add depot address
			String depotAddress = data.addresses.get(manager.indexToNode(routing.end(i)));
			route.add(depotAddress);

			// Add the route to the map with the rider ID as key
			routesMap.put(i, route);
		}
		
		return routesMap;

	}

}