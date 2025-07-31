package es.unex.cum.mdai.vrp.utils;

import java.util.List;

public class DataModel {
	public final List<String> addresses;
	public long[][] distanceMatrix;
	public final long[] demands;
	public final int vehicleNumber;
	public final long[] vehicleCapacities;
	public final int depot;
	
	public DataModel(List<String> addresses, long[] demands, int vehicleNumber, long[] vehicleCapacities, int depot) throws Exception {
		this.addresses = addresses;
		this.demands = demands;
		this.vehicleNumber = vehicleNumber;
		this.vehicleCapacities = vehicleCapacities;
		this.depot = depot;
		
		distanceMatrix = DistanceMatrixCalculator.getInstance().getDistanceMatrix(addresses);
	}
	
	public void setDistanceMatrix(long[][] distanceMatrix) {
		this.distanceMatrix = distanceMatrix;
	}
}
