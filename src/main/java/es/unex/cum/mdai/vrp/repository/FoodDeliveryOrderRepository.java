package es.unex.cum.mdai.vrp.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import es.unex.cum.mdai.vrp.domain.FoodDeliveryOrder;
import es.unex.cum.mdai.vrp.domain.OrderStatus;

@Repository
public interface FoodDeliveryOrderRepository extends CrudRepository<FoodDeliveryOrder, Long>{
	public List<FoodDeliveryOrder> findByStatus(OrderStatus status);
}
