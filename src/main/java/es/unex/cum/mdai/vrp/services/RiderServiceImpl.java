package es.unex.cum.mdai.vrp.services;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import es.unex.cum.mdai.vrp.domain.Rider;
import es.unex.cum.mdai.vrp.domain.RiderStatus;
import es.unex.cum.mdai.vrp.repository.RiderRepository;

@Service
public class RiderServiceImpl implements RiderService {
	
	private final RiderRepository repository;
	
	public RiderServiceImpl(RiderRepository repository) {
		this.repository = repository;
	}

	@Override
	public void saveAll(List<Rider> riders) {
		repository.saveAll(riders);
	}

	@Override
	public void save(Rider rider) {
		repository.save(rider);
		
	}

	@Override
	public List<Rider> findAll() {
		return StreamSupport
                .stream(repository.findAll().spliterator(), false)
                .collect(Collectors.toList());
	}

	@Override
	public Rider findById(Long id) {
		return repository.findById(id).orElse(null);
	}

	@Override
	public List<Rider> findByStatus(RiderStatus status) {
		return repository.findByStatus(status);
	}

	/*@Override
	public int getCurrentMaxCapacity() {
		return repository.getTotalCapacityForRidersAtRestaurant();
	}*/

}
