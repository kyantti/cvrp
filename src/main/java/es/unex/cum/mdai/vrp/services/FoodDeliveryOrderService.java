package es.unex.cum.mdai.vrp.services;

import java.util.List;

import es.unex.cum.mdai.vrp.domain.FoodDeliveryOrder;
import es.unex.cum.mdai.vrp.domain.OrderStatus;

public interface FoodDeliveryOrderService {
	public List<FoodDeliveryOrder> findAll();
	public FoodDeliveryOrder findById(Long id);
	public void save(FoodDeliveryOrder order);
	public void saveAll(List<FoodDeliveryOrder> orders);
	public List<FoodDeliveryOrder> findByStatus(OrderStatus status);
}
