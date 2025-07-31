package es.unex.cum.mdai.vrp.services;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import es.unex.cum.mdai.vrp.domain.FoodDeliveryOrder;
import es.unex.cum.mdai.vrp.domain.OrderStatus;
import es.unex.cum.mdai.vrp.repository.FoodDeliveryOrderRepository;

@Service
public class FoodDeliveryOrderServiceImpl implements FoodDeliveryOrderService{
	
    private final FoodDeliveryOrderRepository repository;
	
	public FoodDeliveryOrderServiceImpl(FoodDeliveryOrderRepository repository) {
		this.repository = repository;
	}

    @Override
    public List<FoodDeliveryOrder> findAll() {
        return StreamSupport
                .stream(repository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public void saveAll(List<FoodDeliveryOrder> orders) {
        repository.saveAll(orders);
    }

	@Override
	public FoodDeliveryOrder findById(Long id) {
		return repository.findById(id).orElse(null);
	}

	@Override
	public void save(FoodDeliveryOrder order) {
		repository.save(order);
		
	}

	@Override
	public List<FoodDeliveryOrder> findByStatus(OrderStatus status) {
		return repository.findByStatus(status);
	}

}
